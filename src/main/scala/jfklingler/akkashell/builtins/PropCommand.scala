package jfklingler.akkashell.builtins

import jfklingler.akkashell.CommandHandler

object PropCommand extends CommandHandler {

  val prop = command("props", "props".r, "Show system properties")
  val propParam = command("prop <name>", "prop (.*)".r, "Show specified environment variable value")

  override val commands: Seq[Command] = Seq(prop, propParam)

  override val handler: CommandHandler = {
    case propParam.command(k) if !k.trim.isEmpty =>
      sys.props.get(k) match {
        case Some(v) => response(v)
        case None => response(s"Property not defined: $k")
      }

    case prop.command() =>
      sys.props.foldLeft(emptyResponse) { (wc, kv) =>
        response(kv._1 + " = " + kv._2, wc)
      }
  }
}
