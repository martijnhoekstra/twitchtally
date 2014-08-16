package twitchtally

import akka.actor.{ ActorSystem, Props }
import akka.io.IO
import scala.concurrent.Future
import spray.can.Http

object Boot extends App {
  // we need an ActorSystem to host our application in
  val port = {
    try {
      System.getenv("PORT").toInt
    } catch {
      case e: java.lang.NumberFormatException => {
        println("port not a number")
        80
      }
      case e: Throwable => {
        println("dafuq?")
        println(e)
        80
      }

    }
  }
  implicit val system = ActorSystem("twitchtally-system")

  def channelsetup = {
    val subemotes: Future[Map[String, Set[String]]] = ???
    val channels: Future[List[String]] = ???

  }

  // create and start our service actor
  val service = system.actorOf(Props[TwitchTallyServiceActor], "twitchtally-service")

  // start a new HTTP server on port 8080 with our service actor as the handler
  IO(Http) ! Http.Bind(service, "localhost", port = port)

}

class Matcher(in: Iterator[Byte], matchees: List[String]) {
  var inProgress: List[(String, Int)] = Nil
  var done: List[String] = Nil

  def run: List[String] = {
    while (in.hasNext) {
      val next = in.next
      if (next == 10) {
        return done
      } else {
        val nextprogress: List[Either[String, (String, Int)]] = (inProgress ++ matchees.map(s => (s, 0))) collect {
          case t if t._1(t._2) == next => if (t._2 == t._1.length - 1) {
            Left(t._1)
          } else {
            Right(t._1, t._2 + 1)
          }
        }
        inProgress = nextprogress.collect { case Right(x) => x }
        done = done ++ nextprogress.collect { case Left(x) => x }
      }
    }
    done
  }
}

class RingBuffer(size: Int) {
  val buffer = new Array[Byte](size)
  var pos = 0
  def put(b: Byte) = {
    buffer(pos) = b
    pos = (pos + 1) % (size - 1)
  }
  def last(amount: Int) = {
    val startpos = (pos - amount) % (size - 1)
    val endpos = pos
  }

}

case class PartialMatch()