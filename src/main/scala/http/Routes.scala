package http

import org.http4s._
import cats.effect._
import cats.syntax.all._
import org.http4s.implicits._
import io.circe.syntax._
import org.http4s.circe.CirceEntityEncoder._
import http.response.ResponseMeetingCodecs._
import http.response.ResponseMeeting
import org.http4s.ember.server.EmberServerBuilder
import _root_.store.MeetingStore
import cats.Applicative
import org.http4s.dsl.Http4sDsl

object Routes {
  def meetingsRoutes[F[_]: Concurrent: Applicative](
      meetingStore: MeetingStore[F]
  ) =
    object dsl extends Http4sDsl[F]
    import dsl._
    HttpRoutes.of[F] {
      case GET -> Root / "meetings" / meetingId =>
        Ok("ok")

      case POST -> Root / "meetings" / meetingId =>
        val testMeeting = ResponseMeeting(
          name = "foo",
          time = "sometime",
          location = "somewhere",
          notes = "SomeNotes"
        )

        Ok(testMeeting.asJson)
    }
}
