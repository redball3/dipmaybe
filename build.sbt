val scala3Version = "3.4.0"

lazy val root = project
  .in(file("."))
  .settings(
    name := "dipMaybe",
    version := "0.1.0-SNAPSHOT",
    scalaVersion := scala3Version,
    libraryDependencies ++= Dependencies.All,
    semanticdbEnabled := true,
    scalacOptions += "-Wunused:imports"
  )
