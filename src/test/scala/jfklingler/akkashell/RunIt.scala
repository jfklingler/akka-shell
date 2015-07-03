package jfklingler.akkashell

object RunIt extends App {

  import akka.actor.ActorSystem
  import com.typesafe.config.ConfigFactory
  import scala.concurrent.duration._

  implicit val config = ConfigFactory.load()
  implicit val system = ActorSystem("fatTest", config)

  AkkaShell.createConnectionManager

  Thread.sleep(30.seconds.toMillis)

  system.shutdown()
  system.awaitTermination(5.seconds)
  sys.exit()
}
