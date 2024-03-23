package http

import _root_.store.MeetingStore
import cats.data.Validated.Invalid
import cats.data.Validated.Valid
import cats.effect._
import cats.syntax.all._
import http.response.ResponseMeeting
import http.response.ResponseMeetingCodecs._
import http.validation.MeetingRequestValidator
import org.http4s._
import org.http4s.circe.CirceEntityEncoder._
import org.http4s.circe._
import org.http4s.dsl.Http4sDsl

import java.util.UUID
import cats.instances.UUIDInstances
import scala.util.Try
import scala.util.Failure
import scala.util.Success

object Routes {
  def meetingsRoutes[F[_]: Concurrent](
      meetingStore: MeetingStore[F]
  ) =
    object dsl extends Http4sDsl[F]
    import dsl._

    implicit val meetingsDecoder: EntityDecoder[F, ResponseMeeting] =
      jsonOf[F, ResponseMeeting]
    HttpRoutes.of[F] {
      case GET -> Root / "meetings" / meetingId =>
        Try(UUID.fromString(meetingId)) match
          case Failure(exception) =>
            BadRequest("Meeting must be a valid uuid")
          case Success(value) =>
            meetingStore.getMeeting(value).flatMap {
              case None =>
                NotFound("meeting not found")
              case Some(value) =>
                Ok(value.toResponseMeeting)
            }

      case req @ POST -> Root / "meetings" =>
        req
          .as[ResponseMeeting]
          .flatMap(meeting =>
            MeetingRequestValidator.validateMeetingRequest(meeting) match
              case Valid(a) =>
                val id: F[UUID] = meetingStore.putMeeting(a)

                val resp = id.map(id => id.toString())
                Ok(resp)
              case Invalid(e) =>
                val errString = e.map(_.errorMessage).mkString_(",\n")
                BadRequest(errString)
          )

    }
}
