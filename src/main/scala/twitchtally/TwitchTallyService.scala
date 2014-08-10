package twitchtally

import scala.concurrent.duration.Duration
import spray.routing.HttpService
import spray.routing.authentication.BasicAuth
import spray.routing.directives.CachingDirectives._
import spray.httpx.encoding._
import akka.actor.ActorSystem
import akka.actor.Actor

class TwitchTallyServiceActor extends TwitchTallyService with Actor {

  // the HttpService trait defines only one abstract member, which
  // connects the services environment to the enclosing actor or test
  def actorRefFactory = context

  // this actor only runs our route, but you could add
  // other things here, like request stream processing,
  // timeout handling or alternative handler registration
  def receive = runRoute(route)

}

trait TwitchTallyService extends HttpService {

  //implicit def executionContext = actorRefFactory.dispatcher

  //val route = {
  //pathSingleSlash {
  //  getFromResource("/main.html")
  //  }
  //  } // ~
  //  pathPrefix("js") { complete { "it works too~!" } } //getFromResourceDirectory("/js") }

  val route = getFromResource("/html/main.html")
}

