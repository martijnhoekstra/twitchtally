package twitchtally

import scala.concurrent.duration.Duration
import spray.routing.HttpService
import spray.routing.authentication.BasicAuth
import spray.routing.directives.CachingDirectives._
import spray.httpx.encoding._
import akka.actor.ActorSystem
import akka.actor.Actor
import scala.concurrent.ExecutionContext
import scala.concurrent.Future
import spray.http._
import spray.client.pipelining._
import spray.json._
import spray.httpx.SprayJsonSupport._
import scala.concurrent.duration._
import scala.concurrent.Await

class TwitchTallyServiceActor extends HttpService with Actor {

  // the HttpService trait defines only one abstract member, which
  // connects the services environment to the enclosing actor or test
  def actorRefFactory = context

  // this actor only runs our route, but you could add
  // other things here, like request stream processing,
  // timeout handling or alternative handler registration
  def receive = runRoute(route)

  val route = {
    pathSingleSlash {
      getFromResource("public/html/main.html")
    } ~
      pathPrefix("js") {
        getFromResourceDirectory("public/js")
      } ~
      pathPrefix("css") {
        getFromResourceDirectory("public/css")
      } ~
      path("emoticons") {
        complete {
          getAllEmotes
        }

      }

  }

  val getAllEmotes = {

    implicit def ec: ExecutionContext = context.dispatcher

    val pipeline: HttpRequest => Future[HttpResponse] = sendReceive
    Await.result(pipeline(Get("http://api.twitch.tv/kraken/chat/emoticons")).map(res => res.entity.asString), 20 seconds)
  }
}