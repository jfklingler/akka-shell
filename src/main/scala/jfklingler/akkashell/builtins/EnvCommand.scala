package jfklingler.akkashell.builtins

import jfklingler.akkashell.CommandHandler

object EnvCommand extends CommandHandler {

  val env = command("env", "env".r, "Show environment variables")
  val envParam = command("env <name>", "env (.*)".r, "Show specified environment variable value")

  override val commands: Seq[Command] = Seq(env, envParam)

  override val handler: CommandHandler = {
    case envParam.command(k) if !k.trim.isEmpty =>
      sys.env.get(k) match {
        case Some(v) => response(v)
        case None    => response(s"Environment variable not defined: $k")
      }

    case env.command() =>
      sys.env.foldLeft(emptyResponse) { (wc, kv) =>
        response(kv._1 + " = " + kv._2, wc)
      }
  }
}
