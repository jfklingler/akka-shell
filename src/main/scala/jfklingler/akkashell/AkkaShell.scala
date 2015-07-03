package jfklingler.akkashell

import akka.actor.{ActorRef, Actor, ActorLogging}
import com.typesafe.config.Config

class AkkaShell(config: Config) extends Actor with ActorLogging with ShellHandlerRegistrar {

  import java.net.InetSocketAddress
  import akka.io.{IO, Tcp}

  private val akkaShellConfig = config.getConfig("akka-shell")

  override def preStart(): Unit = {
    implicit val actorSystem = context.system
    IO(Tcp) ! Tcp.Bind(self, new InetSocketAddress(akkaShellConfig.getString("interface"), akkaShellConfig.getInt("port")))
  }

  override def receive: Actor.Receive = handleRegister orElse {
    case Tcp.Bound(local) => log.info("Bound to {}", local)

    case Tcp.Connected(remote, local) =>
      log.debug("Remote address {} connected", remote)
      sender ! Tcp.Register(context.actorOf(ShellConnectionHandler.props(local, remote, sender(), handlers)))
  }
}

object AkkaShell {

  import akka.actor.{ActorRefFactory, Props}

  val DefaultConnectionManagerName = "akka-shell"

  def props(config: Config): Props = Props(classOf[AkkaShell], config)

  def createConnectionManager(implicit actorRefFactory: ActorRefFactory, config: Config): ActorRef =
    actorRefFactory.actorOf(AkkaShell.props(config), DefaultConnectionManagerName)

  def registerCommand(cmdHandler: CommandHandler)(implicit actorRefFactory: ActorRefFactory): Unit = {
    import scala.concurrent.duration._
    import jfklingler.akkashell.ShellHandlerRegistrar.RegisterCommandHandler

    val timeout = 2.seconds
    implicit val ec = actorRefFactory.dispatcher
    actorRefFactory.actorSelection("/user/" + DefaultConnectionManagerName).resolveOne(timeout).onSuccess {
      case connectionManager => connectionManager ! RegisterCommandHandler(cmdHandler)
    }
  }
}
