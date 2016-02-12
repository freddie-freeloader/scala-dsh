name := "scala-DSH"

version := "1.0"

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  "io.getquill" %% "quill-core" % "0.3.0"
)
libraryDependencies ++= Seq(
  "io.circe" %% "circe-core" % "0.2.1",
  "io.circe" %% "circe-generic" % "0.2.1",
  "io.circe" %% "circe-parse" % "0.2.1"
)


lazy val doobieVersion = "0.2.3"

libraryDependencies ++= Seq(
  "org.tpolecat" %% "doobie-core"               % doobieVersion,
  "org.tpolecat" %% "doobie-contrib-postgresql" % doobieVersion,
  "org.scalaz"   %% "scalaz-core"               % "7.1.6"
)

