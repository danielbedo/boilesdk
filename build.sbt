name := "ml-helper"
version := "0.0.0"
scalaVersion := "2.11.7"

resolvers ++= Seq(
  Resolver.sonatypeRepo("releases"),
  Resolver.sonatypeRepo("snapshots")
)

libraryDependencies ++= Seq(
  "org.scalaz" %% "scalaz-core" % "7.1.3",
  "com.chuusai" %% "shapeless" % "2.2.5",
  "org.scalatest" % "scalatest_2.11" % "2.2.4" % "test"
)
