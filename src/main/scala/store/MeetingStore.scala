package store

import cats.Functor
import cats.Monad
import cats.effect.std.UUIDGen
import cats.syntax.all._
import http.response.ResponseMeeting
import scalacache.Cache

import java.sql.Timestamp
import java.time.ZonedDateTime
import java.util.UUID

case class StorageMeeting(
    name: String,
    time: ZonedDateTime,
    location: Location,
    notes: String
) {
  def toResponseMeeting: ResponseMeeting = {
    val ts = Timestamp.valueOf(time.toLocalDateTime())
    ResponseMeeting(
      name,
      ts.toString(),
      s"lat: ${location.lat}  long: ${location.long}",
      notes
    )

  }
}

case class Location(
    lat: Float,
    long: Float
)

trait MeetingStore[F[_]] {
  def getMeeting(id: UUID): F[Option[StorageMeeting]]
  def putMeeting(meeting: StorageMeeting): F[UUID]
  def deleteMeeting(id: UUID): F[Unit]
}

object MeetingStore {
  def fromCache[F[_]: Functor: Monad: UUIDGen](
      cache: Cache[F, UUID, StorageMeeting]
  ): MeetingStore[F] =
    new MeetingStore[F] {
      def getMeeting(id: UUID): F[Option[StorageMeeting]] = cache.get(id)
      def putMeeting(meeting: StorageMeeting): F[UUID] =
        for {
          id <- UUIDGen.randomUUID
          _ <- cache.put(id)(meeting, ttl = None)
        } yield id

      def deleteMeeting(id: UUID): F[Unit] = cache.remove(id)
    }
}
