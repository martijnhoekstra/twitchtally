package twitchtally

import akka.actor.{ ActorSystem, Props }
import akka.io.IO
import scala.concurrent.Future
import scala.concurrent.ExecutionContext
import spray.can.Http
import spray.can.server.UHttp

object Boot extends App {
  // we need an ActorSystem to host our application in
  val port = {
    try {
      System.getenv("PORT").toInt
    } catch {
      case e: java.lang.NumberFormatException => {
        println("port not a number")
        8080
      }
      case e: Throwable => {
        println("dafuq?")
        println(e)
        8080
      }
    }
  }

  val tmpallemotes = {
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
    val tmpsystem = ActorSystem("tmp")

    implicit def iec: ExecutionContext = tmpsystem.dispatcher
    implicit def arf: ActorSystem = tmpsystem

    //val pipeline: HttpRequest => Future[EmotesResponseContainer] = sendReceive ~> umarshall[EmotesResponseContainer]
    //val femotecontainer = pipeline(Get("http://api.twitch.tv/kraken/chat/emoticons"))
    //val femotes = fmotecontainer.map(ec => ec.emotes.map(er => Emote(er.regex, er.images.map(i => i.url).head)))
    val pipeline: HttpRequest => Future[HttpResponse] = sendReceive

    val result = Await.result(pipeline(Get("http://api.twitch.tv/kraken/chat/emoticons")).map(res => res.entity.asString), 20 seconds)
    tmpsystem.shutdown()
    result
  }

  implicit val system = ActorSystem("twitchtally-system")

  //create and start our websocket server "wheehoo!"
  val websocktserver = system.actorOf(SimpleServer.WebSocketServer.props(tmpallemotes), "websocket")

  // create and start our service actor
  //val service = system.actorOf(Props[TwitchTallyServiceActor], "twitchtally-service")

  /*
  implicit def ec: ExecutionContext = system.dispatcher

  val pipeline: HttpRequest => Future[EmotesResponseContainer] = sendReceive ~> umarshall[EmotesResponseContainer]
  val femotecontainer = pipeline(Get("http://api.twitch.tv/kraken/chat/emoticons"))
  val femotes = fmotecontainer.map(ec => ec.emotes.map(er => Emote(er.regex, er.images.map(i => i.url).head)))
  val emotes = Await.result(femotes, 20 seconds)
  val fmap = femotes.map(emotes => emotes.map(em => (em.name, (em.Uri, 0))))
  val map = Await.result(fmap, 20 seconds)
  */
  IO(UHttp) ! Http.Bind(websocktserver, "0.0.0.0", port = port)

  //IO(Http) ! Http.Bind(service, "0.0.0.0", port = port)

}