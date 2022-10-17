ThisBuild / version := "1.0.0-SNAPSHOT"
ThisBuild / scalaVersion := "2.12.10"

lazy val root = (project in file("."))
  .settings(
    name := "lift-management-system",
    libraryDependencies ++= Seq (
      "org.scalatest" %% "scalatest" % "3.2.12" % Test
    )
  )
enablePlugins(AssemblyPlugin)
