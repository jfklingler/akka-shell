package jfklingler.akkashell

trait CommandHandler {

  import scala.concurrent.Future
  import scala.util.matching.Regex
  import akka.io.Tcp.{Write, WriteCommand}
  import akka.util.ByteString
  import jfklingler.akkashell

  type Command = akkashell.Command
  type Response = CommandHandler.Response
  type CommandHandler = CommandHandler.CommandHandler
  type CommandHandlerGenerator = CommandHandler.CommandHandlerGenerator
  type CommandResponseHandler = CommandHandler.CommandResponseHandler

  val commands: Seq[Command]

  val handler: CommandHandler = {
    case _ => emptyResponse
  }

  import scala.language.implicitConversions

  implicit def response2FutureResponse(r: Response): Future[Response] = Future.successful(r)

  def handlerGenerator: CommandHandlerGenerator = (_, _) => handler

  val responseHandler: CommandResponseHandler = CommandHandler.emptyResponseHandler

  def command(name: String, command: Regex, help: String): Command = Command(name, command, help)

  protected final val emptyResponse: WriteCommand = Write.empty

  protected final def response(msg: Any = Unit, wc: WriteCommand = emptyResponse): WriteCommand =
    if (msg == Unit) wc else Write(ByteString(msg.toString + "\n")) +: wc
}

object CommandHandler {

  import scala.concurrent.Future
  import akka.actor.{ActorRef, ActorRefFactory}
  import akka.io.Tcp

  type Response = Tcp.Command
  type CommandHandler = PartialFunction[String, Future[Response]]
  type CommandHandlerGenerator = (ActorRefFactory, ActorRef) => CommandHandler
  type CommandResponseHandler = PartialFunction[Any, Future[Response]]

  val emptyHandler: CommandHandler = EmptyHandler

  object EmptyHandler extends CommandHandler {
    def isDefinedAt(x: String): Boolean = false

    def apply(x: String): Future[Response] =
      throw new UnsupportedOperationException("Empty handler apply()")
  }

  val emptyResponseHandler: CommandResponseHandler = EmptyResponseHandler

  object EmptyResponseHandler extends CommandResponseHandler {
    def isDefinedAt(x: Any): Boolean = false

    def apply(x: Any): Future[Response] =
      throw new UnsupportedOperationException("Empty response handler apply()")
  }

}
