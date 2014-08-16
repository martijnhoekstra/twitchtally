package twitchtally

import akka.actor.Actor
import akka.actor.ActorRef
import akka.actor.Props
import spray.http.Uri

class EmoteCounter(_tally: Map[String, (Uri, Long)]) extends Actor {
  var tally = _tally

  def receive = {
    case name: String => {
      tally.get(name) match {
        case Some(value) => {
          val (url, count) = value
          tally = tally + ((name, (url, count + 1)))
        }
        case None => ()
      }

    }
    case emote: Emote => {
      tally.get(emote.name) match {
        case None => tally + ((emote.name, (emote.url, 0l)))
        case _    => ()
      }

    }
  }
}

object EmoteCounter {
  def apply(tally: Map[String, (Uri, Long)]): Props = Props(new EmoteCounter(tally))
}

case class Emote(name: String, url: Uri)