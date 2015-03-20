package jfklingler.akkashell

trait ShellHandlerRegistrar {

  import builtins.{ConfigCommand, CoreCommands, EnvCommand, PropCommand}

  protected var handlers: Set[CommandHandler] = Set(CoreCommands, ConfigCommand, EnvCommand, PropCommand)

  protected def handleRegister: PartialFunction[Any, Unit] = {
    case ShellHandlerRegistrar.RegisterCommandHandler(handler) =>
      handlers = handlers + handler
  }
}

object ShellHandlerRegistrar {
  case class RegisterCommandHandler(handler: CommandHandler)
}
