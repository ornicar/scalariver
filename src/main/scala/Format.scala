package com.github.ornicar.scalariver

import scala.util.{ Try, Success, Failure }
import scalariform._
import scalariform.formatter.preferences._
import scalariform.formatter.ScalaFormatter
import tiscaf._

private[scalariver] final class Format(req: HReqData) {

  def source = {
    req softParam "source"
    // horrible workaround for https://github.com/gnieh/tiscaf/issues/7
  } drop 4

  def apply: Try[String] = {

    def scalaVersion = req param "scalaVersion" getOrElse "2.10"
    def initialIndentLevel = req asInt "initialIndentLevel" getOrElse 0

    def preferences = new FormattingPreferences(
      AllPreferences.preferencesByKey map {
        case (key, descriptor) ⇒ {
          val setting = descriptor match {
            case desc: BooleanPreferenceDescriptor ⇒
              Some(if (req.param(key).isDefined) "true" else "false")
            case desc ⇒ req param key
          }
          val parsed = setting flatMap { v ⇒
            descriptor.preferenceType.parseValue(v).right.toOption
          } getOrElse descriptor.defaultValue
          descriptor -> parsed
        }
      } toMap)

    Try(ScalaFormatter.format(
      source = source,
      scalaVersion = scalaVersion,
      formattingPreferences = preferences,
      initialIndentLevel = initialIndentLevel))
  }
}
