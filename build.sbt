
name := "akka-shell"
organization := "jfklingler"
version := "0.1.0-SNAPSHOT"

scalaVersion := "2.11.6"

val akkaV  = "2.3.9"

libraryDependencies ++= Seq(
  "com.typesafe.akka"          %% "akka-actor"           % akkaV,
  "org.scalatest"              %% "scalatest"            % "2.2.4"  % "test",
  "com.typesafe.akka"          %% "akka-testkit"         % akkaV    % "test"
)

scalacOptions ++= Seq(
  "-feature",
  "-deprecation",
  "-unchecked",
  "-Xcheckinit",
  "-Xlint",
  "-Xverify",
  "-Yclosure-elim",
  "-Yinline",
  "-Yno-adapted-args",
  "-encoding",
  "utf8")
