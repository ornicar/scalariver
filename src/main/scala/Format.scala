//1
//1
package com.github.ornicar.scalariver

import scala.util.{ Try, Success, Failure }
import scalariform._
import scalariform.formatter.preferences._
import scalariform.formatter.ScalaFormatter
import tiscaf._

private[scalariver] final class Format(req: HReqData) {

  def source = req softParam "source"

  def apply: Try[String] = {

    val scalaVersion = req param "scalaVersion" getOrElse "2.11"
    val initialIndentLevel = req asInt "initialIndentLevel" getOrElse 0

    Try(ScalaFormatter.format(
      source = source,
      scalaVersion = scalaVersion,
      formattingPreferences = Format.preferences,
      initialIndentLevel = initialIndentLevel))
  }
}

object Format {
  val preferences = FormattingPreferences()
    .setPreference(CompactControlReadability, true)
    .setPreference(DoubleIndentConstructorArguments, true)
    .setPreference(DanglingCloseParenthesis, Force)
}
