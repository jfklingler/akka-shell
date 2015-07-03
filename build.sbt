name         := "akka-shell"
organization := "jfklingler"
description  := "A pluggable command shell for Akka applications."

licenses += ("MIT", url("http://opensource.org/licenses/MIT"))

scalaVersion := "2.11.7"
crossScalaVersions := Seq("2.10.5", "2.11.7")

val akkaV  = "2.3.11"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor"   % akkaV,
  "org.scalatest"     %% "scalatest"    % "2.2.4"  % "test",
  "com.typesafe.akka" %% "akka-testkit" % akkaV    % "test"
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
  "-encoding", "utf8")
