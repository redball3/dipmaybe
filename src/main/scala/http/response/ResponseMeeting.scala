package http.response

import io.circe.generic.semiauto._
import io.circe.{Encoder, Decoder}

case class ResponseMeeting(
    name: String,
    time: String,
    location: String,
    notes: String
)

object ResponseMeetingCodecs {
  implicit val responseMeetingEncoder: Encoder[ResponseMeeting] = deriveEncoder
  implicit val responseMeetingDecoder: Decoder[ResponseMeeting] = deriveDecoder
}
