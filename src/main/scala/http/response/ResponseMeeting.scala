package http.response

import io.circe.Decoder
import io.circe.Encoder
import io.circe.generic.semiauto._

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
