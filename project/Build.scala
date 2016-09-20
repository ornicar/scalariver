import sbt._, Keys._

object ScalariverProject extends Build {

  val scalariver = Project("scalariver", file(".")).settings(
    organization := "com.github.ornicar",
    name := "scalariver",
    version := "1.1",
    scalaVersion := "2.11.8",
    resourceDirectories in Compile := List(),
    libraryDependencies := Seq(
      "org.gnieh" %% "tiscaf" % "0.9",
      "org.scalariform" %% "scalariform" % "0.1.8"),
    resolvers := Seq(
      "sonatype" at "http://oss.sonatype.org/content/repositories/releases",
      "sonatype snapshots" at "http://oss.sonatype.org/content/repositories/snapshots"),
    scalacOptions := Seq(
      "-deprecation", "-unchecked", "-feature", "-language:_",
      "-Ybackend:GenBCode", "-Ydelambdafy:method", "-target:jvm-1.8")
  ).settings(com.github.retronym.SbtOneJar.oneJarSettings: _*)
}
