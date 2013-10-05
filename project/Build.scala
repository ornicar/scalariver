import sbt._
import Keys._

object ScalariverProject extends Build {

  val scalariver = (Project("scalariver", file(".")) settings (
    organization := "com.github.ornicar",
    name := "scalariver",
    version := "0.1",
    scalaVersion := "2.10.3",
    libraryDependencies ++= dependencies,
    resourceDirectories in Compile := List(),
    scalacOptions := Seq("-deprecation", "-unchecked", "-feature", "-language:_")) 

  val compilerOptions = Seq("-deprecation", "-unchecked", "-feature", "-language:_")
}
