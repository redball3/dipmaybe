import sbt._

object Versions {
  val Http4s = "1.0.0-M40"
  val Cats = "2.10.0"
  val CatsEffect = "3.5.4"
  val Circe = "0.14.1"
  val Log4Cats = "2.6.0"
  val ScalaCache = "1.0.0-M6"
}

object Dependencies {
  val main = Seq(
    // http4s
    "org.http4s" %% "http4s-ember-client" % Versions.Http4s,
    "org.http4s" %% "http4s-dsl" % Versions.Http4s,
    "org.http4s" %% "http4s-ember-server" % Versions.Http4s,
    "org.http4s" %% "http4s-circe" % Versions.Http4s,

    // cats
    "org.typelevel" %% "cats-effect" % Versions.CatsEffect,
    "org.typelevel" %% "cats-core" % Versions.Cats,

    // circe
    "io.circe" %% "circe-core" % Versions.Circe,
    "io.circe" %% "circe-generic" % Versions.Circe,
    "io.circe" %% "circe-parser" % Versions.Circe,

    // logging
    "org.typelevel" %% "log4cats-slf4j" % Versions.Log4Cats,

    // caching
    "com.github.cb372" %% "scalacache-core" % Versions.ScalaCache,
    "com.github.cb372" %% "scalacache-caffeine" % Versions.ScalaCache
  )

  val test = Seq(
    "org.scalameta" %% "munit" % "0.7.29" % Test
  )

  val All = main ++ test
}
