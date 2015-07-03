package jfklingler.akkashell

import akka.actor.{Actor, ActorLogging, ActorRef}
import com.typesafe.config.Config

class AkkaShell(config: Config) extends Actor with ActorLogging with ShellHandlerRegistrar {

  import java.net.InetSocketAddress
  import akka.io.{IO, Tcp}

  private val akkaShellConfig = config.getConfig("akka-shell")

  override def preStart(): Unit = {
    implicit val actorSystem = context.system
    IO(Tcp) ! Tcp.Bind(self, new InetSocketAddress(akkaShellConfig.getString("interface"), akkaShellConfig.getInt("port")))
  }

  override def receive: Receive = handleRegister orElse {
    case Tcp.Bound(local) => log.info("Bound to {}", local)

    case Tcp.Connected(remote, local) =>
      log.debug("Remote address {} connected", remote)
      sender ! Tcp.Register(context.actorOf(ShellConnectionHandler.props(local, remote, sender(), handlers)))
  }
}

object AkkaShell {

  import akka.actor.{ActorRefFactory, Props}

  def props(config: Config): Props = Props(classOf[AkkaShell], config)

  def createConnectionManager(implicit actorRefFactory: ActorRefFactory, config: Config): ActorRef =
    actorRefFactory.actorOf(AkkaShell.props(config), config.getString("akka-shell.connection-manager-name"))

  def registerCommand(cmdHandler: CommandHandler)(implicit actorRefFactory: ActorRefFactory, config: Config): Unit = {
    import jfklingler.akkashell.ShellHandlerRegistrar.RegisterCommandHandler

    actorRefFactory.actorSelection("/user/" + config.getString("akka-shell.connection-manager-name")) ! RegisterCommandHandler(cmdHandler)
  }
}
