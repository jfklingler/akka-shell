package jfklingler.akkashell.builtins

import jfklingler.akkashell.CommandHandler

object CoreCommands extends CommandHandler {

  val exit = command("exit", "exit|quit".r, "Close shell session")
  val ping = command("ping", "ping".r, "Request a pong")
  val pingParam = command("ping <response>", "ping (.*)".r, "Request the specified response")

  override val commands: Seq[Command] = Seq(exit, ping, pingParam)

  override val handler: CommandHandler = {
    case exit.command() =>
      import jfklingler.akkashell.ShellConnectionHandler.CloseConnection
      CloseConnection

    case ping.command() => response("pong")

    case pingParam.command(op) => response(op)
  }
}
