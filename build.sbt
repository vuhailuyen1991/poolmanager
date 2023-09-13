name := """poolmanager"""
organization := "vn.com.techcombank"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.13.12"

libraryDependencies += guice
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "5.1.0" % Test
libraryDependencies += "org.apache.commons" % "commons-math3" % "3.6.1"

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "vn.com.techcombank.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "vn.com.techcombank.binders._"
