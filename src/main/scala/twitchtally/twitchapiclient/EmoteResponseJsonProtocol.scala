package twitchtally.twitchapiclient

import spray.json._

case class EmotesResponseContainer(`_links`: EmoteLinks, emotes: List[EmotesResponse])
case class EmotesResponse(regex: String, images: List[ImageResponse])
case class EmoteLinks(self: String)
case class ImageResponse(emoticon_set: Option[Int], height: Int, width: Int, url: String)

object EmoteReponseJsonProtocol extends DefaultJsonProtocol {
  implicit val EmoteLinksFormat = jsonFormat1(EmoteLinks)
  implicit val ImageFormat = jsonFormat4(ImageResponse)
  implicit val EmotesFormat = jsonFormat2(EmotesResponse)
  implicit val EmotesResponseContainerProtocol = jsonFormat2(EmotesResponseContainer)
}