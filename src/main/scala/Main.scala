package main

import cats.effect.IO
import cats.effect.IOApp
import cats.syntax.all.*
import com.comcast.ip4s._
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.server.Router
import http.Routes
import org.typelevel.log4cats.slf4j.Slf4jFactory
import org.typelevel.log4cats.LoggerFactory
import cats.effect.ExitCode
import scalacache._
import scalacache.caffeine
import scalacache.caffeine.CaffeineCache
import cats.effect.std.UUIDGen
import store.MeetingStore
import java.util.UUID
import store.StorageMeeting

object DipMaybeService extends IOApp {

  override def run(args: List[String]): IO[ExitCode] = {
    implicit val loggerFactory: LoggerFactory[IO] = Slf4jFactory.create[IO]

    val thing = CaffeineCache[IO, UUID, StorageMeeting].toResource.map(cache =>
      MeetingStore.fromCache(cache)
    )

    val server = for {
      meetingsCache <- CaffeineCache[IO, UUID, StorageMeeting].toResource
      meetingStore = MeetingStore.fromCache(meetingsCache)
      httpApp = Router("/" -> Routes.meetingsRoutes(meetingStore)).orNotFound
      server <- EmberServerBuilder
        .default[IO]
        .withHost(ipv4"0.0.0.0")
        .withPort(port"8080")
        .withHttpApp(httpApp)
        .build
    } yield server

    server
      .use(_ => IO.never)
      .as(ExitCode.Success)
  }

}
