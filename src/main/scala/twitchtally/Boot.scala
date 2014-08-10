package twitchtally

import akka.actor.{ ActorSystem, Props }
import akka.io.IO
import spray.can.Http

object Boot extends App {
  // we need an ActorSystem to host our application in
  implicit val system = ActorSystem("twitchtally-system")

  // create and start our service actor
  val service = system.actorOf(Props[TwitchTallyServiceActor], "twitchtally-service")

  // start a new HTTP server on port 8080 with our service actor as the handler
  IO(Http) ! Http.Bind(service, "localhost", port = 8080)
}
