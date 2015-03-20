package jfklingler.akkashell

import akka.actor.Actor

trait ShellHandlerRegistry {
  this: Actor =>

  protected val handlers: Set[CommandHandler]

  protected def handleCommand(text: String) = {
    import builtins.{HelpCommand, UnknownCommand}
    (handlers.foldLeft(CommandHandler.emptyHandler)((acc, sh) => acc orElse sh.handlerGenerator(context, self))
      orElse new HelpCommand(handlers.flatMap(_.commands)).handler
      orElse UnknownCommand.handler)(text)
  }

  protected def handleCommandResponse =
    handlers.foldLeft(CommandHandler.emptyResponseHandler)((acc, sh) => acc orElse sh.responseHandler)
}
