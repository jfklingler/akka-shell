
name := "akka-console"
organization := "jfklingler"
version := "1.0.0-SNAPSHOT"

scalaVersion := "2.11.6"

val akkaV  = "2.3.9"
val sprayV = "1.3.2"
val slf4jV = "1.7.10"

libraryDependencies ++= Seq(
  "org.scala-lang.modules"     %% "scala-async"          % "0.9.3",
  "com.typesafe.akka"          %% "akka-actor"           % akkaV,
  "com.typesafe.akka"          %% "akka-slf4j"           % akkaV,
  "com.typesafe.akka"          %% "akka-kernel"          % akkaV,
  "io.spray"                   %% "spray-can"            % sprayV,
  "io.spray"                   %% "spray-client"         % sprayV,
  "io.spray"                   %% "spray-json"           % "1.3.1",
  "com.typesafe.scala-logging" %% "scala-logging"        % "3.1.0",
  "org.slf4j"                  %  "slf4j-api"            % slf4jV,
  "org.slf4j"                  %  "log4j-over-slf4j"     % slf4jV,
  "ch.qos.logback"             %  "logback-classic"      % "1.1.2",
  "org.scalatest"              %% "scalatest"            % "2.2.4"  % "test",
  "io.spray"                   %% "spray-testkit"        % sprayV   % "test",
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
