import sbt._, Keys._

object ScalariverProject extends Build {

  val scalariver = Project("scalariver", file(".")).settings(
    organization := "com.github.ornicar",
    name := "scalariver",
    version := "1.0",
    scalaVersion := "2.10.3",
    resourceDirectories in Compile := List(),
    libraryDependencies := Seq(
      "org.gnieh" %% "tiscaf" % "0.8",
      "org.scalariform" %% "scalariform" % "0.1.4"),
    resolvers := Seq(
      "sonatype" at "http://oss.sonatype.org/content/repositories/releases",
      "sonatype snapshots" at "http://oss.sonatype.org/content/repositories/snapshots"),
    scalacOptions := Seq("-deprecation", "-unchecked", "-feature", "-language:_")
  ).settings(com.github.retronym.SbtOneJar.oneJarSettings: _*)
}
