package twitchtally

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