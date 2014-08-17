package twitchtally

import akka.actor.{ ActorSystem, Actor, Props, ActorLogging, ActorRef, ActorRefFactory }
import akka.io.IO
import spray.can.Http
import spray.can.server.UHttp
import spray.can.websocket
import spray.can.websocket.frame.{ BinaryFrame, TextFrame }
import spray.http.HttpRequest
import spray.can.websocket.FrameCommandFailed
import spray.routing.HttpServiceActor

object SimpleServer {

  final case class Push(msg: String)

  object WebSocketServer {
    def props(tmpallemotes: String) = Props(classOf[WebSocketServer], tmpallemotes)
  }
  class WebSocketServer(tmpallemotes: String) extends Actor with ActorLogging {

    def receive = {
      // when a new connection comes in we register a WebSocketConnection actor as the per connection handler
      case Http.Connected(remoteAddress, localAddress) =>
        val serverConnection = sender()
        val conn = context.actorOf(WebSocketWorker.props(serverConnection, tmpallemotes))
        serverConnection ! Http.Register(conn)
    }
  }

  object WebSocketWorker {
    def props(serverConnection: ActorRef, tmpallemotes: String) = Props(classOf[WebSocketWorker], serverConnection, tmpallemotes)

  }
  class WebSocketWorker(val serverConnection: ActorRef, tmpallemotes: String) extends HttpServiceActor with websocket.WebSocketServerWorker {
    override def receive = handshaking orElse businessLogicNoUpgrade orElse closeLogic

    def businessLogic: Receive = {
      // just bounce frames back for Autobahn testsuite
      case x @ (_: BinaryFrame | _: TextFrame) => {
        log.info("frame received", x)
        sender() ! x
      }

      case Push(msg) => {
        log.info("push received", msg)
        send(TextFrame(msg))
      }

      case x: FrameCommandFailed =>
        log.error("frame command failed", x)

      case x: HttpRequest => {
        log.info("received an HttpRequest, but what to do in this context?", x)
      }
    }

    def businessLogicNoUpgrade: Receive = {

      implicit val refFactory: ActorRefFactory = context
      runRoute {
        oldroute
      }
    }

    val oldroute = {
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
            tmpallemotes
          }

        }

    }

  }

}