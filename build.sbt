name := """Njam.ba"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  javaJdbc,
  javaEbean,
  cache,
  javaWs,
  "org.mindrot" % "jbcrypt" % "0.3m",
  "org.webjars" %% "webjars-play" % "2.3.0-2",
  "org.webjars" % "bootstrap" % "3.1.1-2",
  "com.typesafe.play" %% "play-mailer" % "2.4.0",
  "net.sourceforge.htmlunit" % "htmlunit" % "2.14" % "test",
  "com.paypal.sdk" % "rest-api-sdk" % "1.2.0",
  "org.imgscalr" % "imgscalr-lib" % "4.2"
  )
