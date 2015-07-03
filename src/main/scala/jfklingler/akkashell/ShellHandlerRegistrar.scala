package jfklingler.akkashell

trait ShellHandlerRegistrar {

  import akka.actor.Actor.Receive
  import builtins.{ConfigCommand, CoreCommands, EnvCommand, PropCommand}

  protected var handlers: Set[CommandHandler] = Set(CoreCommands, ConfigCommand, EnvCommand, PropCommand)

  protected def handleRegister: Receive = {
    case ShellHandlerRegistrar.RegisterCommandHandler(handler) =>
      handlers = handlers + handler
  }
}

object ShellHandlerRegistrar {
  case class RegisterCommandHandler(handler: CommandHandler)
}
