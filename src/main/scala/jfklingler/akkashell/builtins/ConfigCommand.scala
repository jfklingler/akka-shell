package jfklingler.akkashell.builtins

import jfklingler.akkashell.CommandHandler

object ConfigCommand extends CommandHandler {

  import com.typesafe.config.{ConfigFactory, ConfigRenderOptions}

  val config = ConfigFactory.load
  val confRenderOptions = ConfigRenderOptions.defaults().setOriginComments(false).setComments(false).setFormatted(true).setJson(false)

  val conf = command("config", "config".r, "Show configuration")
  val confParam = command("config <section>", "config (.*)".r, "Show specified configuration section")

  override val commands: Seq[Command] = Seq(conf, confParam)

  override val handler: CommandHandler = {
    case confParam.command(k) if !k.trim.isEmpty =>
      if (config.hasPath(k))
        response(config.getValue(k).render(confRenderOptions))
      else
        response(s"Config property not found: $k")

    case conf.command() =>
      response(config.root().render(confRenderOptions))
  }
}
