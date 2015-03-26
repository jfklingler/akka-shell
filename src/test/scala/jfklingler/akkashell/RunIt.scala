package jfklingler.akkashell

object RunIt extends App {

  import akka.actor.ActorSystem
  import com.typesafe.config.ConfigFactory
  import scala.concurrent.duration._

  val config = ConfigFactory.load()
  val system = ActorSystem("fatTest", config)

  AkkaShell.createConnectionManager(system, config)

  Thread.sleep(30.seconds.toMillis)

  system.shutdown()
  system.awaitTermination(5.seconds)
  sys.exit()
}
