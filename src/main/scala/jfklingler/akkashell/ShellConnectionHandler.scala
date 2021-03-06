package jfklingler.akkashell

import java.net.InetSocketAddress
import akka.actor.{Actor, ActorLogging, ActorRef}

class ShellConnectionHandler(local: InetSocketAddress,
                             val remote: InetSocketAddress,
                             val connection: ActorRef,
                             override val handlers: Set[CommandHandler])
  extends Actor with ActorLogging with ShellHandlerRegistry {

  import scala.concurrent.Future
  import scala.util.{Failure, Success}
  import akka.actor.Terminated
  import akka.io.Tcp.{Close, Command => TcpCommand, CompoundWrite, ConnectionClosed, Received, Write, WriteCommand}
  import akka.util.ByteString

  override def preStart(): Unit = {
    // We need to know when the connection dies without sending a `Tcp.ConnectionClosed`
    context.watch(connection)

    log.info("Incoming connection from {}", remote)
    writePrompt()
  }

  implicit val shellHandler: ActorRef = self

  override def receive: Receive = handleTcp orElse handleCommandResponse andThen complete

  private def handleTcp: PartialFunction[Any, Any] = {
    case Received(data) =>
      val text = data.utf8String.trim
      log.info("Received '{}' from remote address {}", text, remote)
      handleCommand(text)

    case _: ConnectionClosed =>
      log.info("Connection from {} closed", remote)
      context.stop(self)

    case Terminated(`connection`) =>
      log.info("Connection from {} terminated", remote)
      context.stop(self)
  }


  private implicit val ec = context.dispatcher

  private def complete: Receive = {
    case w: CompoundWrite if w.nonEmpty =>
      w.toIndexedSeq.reverse.foreach(write)
      writePrompt()

    case ShellConnectionHandler.CloseConnection =>
      context.unwatch(connection)
      connection ! Close

    case w: WriteCommand =>
      write(w)
      writePrompt()

    // Erm...so, type erasure...need to figure out how to make this type-safe
    case fc: Future[TcpCommand] => fc.onComplete {
      case Success(c) => complete(c)
      case Failure(t) =>
        val msg = "Command execution failed"
        log.error(t, msg)
        complete(Write(ByteString(msg)))
    }

    case x => log.warning("Unrecognized handler response: {}", x.toString)
  }

  private def write(wc: WriteCommand) = connection ! wc

  private def writePrompt() = connection ! Write(ByteString(context.system.name + "@" + local.toString + "> "))
}

object ShellConnectionHandler {

  import akka.actor.Props
  import akka.io.Tcp.Command

  case object CloseConnection extends Command

  def props(local: InetSocketAddress,
            remote: InetSocketAddress,
            connection: ActorRef,
            handlers: Set[CommandHandler]): Props =
    Props(new ShellConnectionHandler(local, remote, connection, handlers))
}
