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

  val route = complete {
    main
  }

  val main =
    <html>
      <head>
        <title>All your twitch emotes are belong to us</title>
      </head>
      <body>
        <ul data-bind="foreach: emotes">
          <li data-bind="css: changed"><img data-bind="attr: { src: url, alt: name}"/><span class="tally" data-bind="text: tally"></span></li>
        </ul>
        <script src="//ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
        <script src="//cdnjs.cloudflare.com/ajax/libs/knockout/3.1.0/knockout-min.js"></script>
        <script src="js/main.js"></script>
      </body>
    </html>
}

