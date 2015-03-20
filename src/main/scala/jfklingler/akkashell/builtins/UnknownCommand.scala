package jfklingler.akkashell.builtins

import jfklingler.akkashell.CommandHandler

object UnknownCommand extends CommandHandler {

  override val commands: Seq[Command] = Seq.empty

  override val handler: CommandHandler = {
    case empty if empty.isEmpty => emptyResponse
    case unknown => response("command not found: " + unknown.toString)
  }
}
