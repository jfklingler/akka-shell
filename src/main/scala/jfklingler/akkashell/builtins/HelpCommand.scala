package jfklingler.akkashell.builtins

import jfklingler.akkashell.{Command, CommandHandler}

class HelpCommand(cmds: Set[Command]) extends CommandHandler {

  val help = command("help", "help".r, "List available commands")
  val helpCmd = command("help <command>", "help (.*)".r, "Print help for specified command")

  override val commands: Seq[Command] = Seq(help, helpCmd)

  private val allCmds = cmds ++ commands

  override val handler: CommandHandler = {
    case help.command() => response(emptyHelpResponse)
    case helpCmd.command(cmd) => response(cmdHelp(cmd))
  }

  private def cmdHelp(cmd: String) =
    allCmds.filter(_.name.startsWith(cmd)).map(c => c.name + ": " + c.help).toSeq.sorted match {
      case helps if helps.isEmpty => s"No help found for '$cmd'"
      case helps => helps.mkString("\n")
    }

  private val helpCols = 5
  private lazy val emptyHelpResponse =
    "Available commands:\n" +
      allCmds.map(_.name).toSeq.sorted.grouped(helpCols).map(_.mkString("\t\t")).mkString("\n") +
      "\nType 'help <command>' for more information."
}
