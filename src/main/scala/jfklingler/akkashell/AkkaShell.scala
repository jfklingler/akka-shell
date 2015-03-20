package jfklingler.akkashell

import akka.actor.{Actor, ActorLogging}
import com.typesafe.config.Config

class AkkaShell(config: Config) extends Actor with ActorLogging with ShellHandlerRegistrar {

  import java.net.InetSocketAddress
  import akka.io.{IO, Tcp}

  override def preStart(): Unit = {
    implicit val actorSystem = context.system
    IO(Tcp) ! Tcp.Bind(self, new InetSocketAddress(config.getString("interface"), config.getInt("port")))
  }

  override def receive: Actor.Receive = handleRegister orElse {
    case Tcp.Bound(local) => log.info("Bound to {}", local)

    case Tcp.Connected(remote, local) =>
      log.debug("Remote address {} connected", remote)
      sender ! Tcp.Register(context.actorOf(ShellConnectionHandler.props(local, remote, sender(), handlers)))
  }
}

object AkkaShell {

  import akka.actor.Props

  def props(config: Config): Props = Props(classOf[AkkaShell], config)
}





