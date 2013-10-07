package com.github.ornicar.scalariver

import scala.util.{ Try, Success, Failure }
import scalariform._
import scalariform.formatter.preferences._
import scalariform.formatter.ScalaFormatter
import tiscaf._

private[scalariver] object Format {

  def apply(req: HReqData): Try[String] = {

    def source = req softParam "source"

    def scalaVersion = req param "scalaVersion" getOrElse "2.10"
    def initialIndentLevel = req asInt "initialIndentLevel" getOrElse 0

    def preferences = new FormattingPreferences(
      AllPreferences.preferencesByKey map {
        case (key, descriptor) ⇒ descriptor -> (req param key flatMap { v ⇒
          descriptor.preferenceType.parseValue(v).right.toOption
        } getOrElse descriptor.defaultValue)
      } toMap
    )

    Try(ScalaFormatter.format(
      source = source,
      scalaVersion = scalaVersion,
      formattingPreferences = preferences,
      initialIndentLevel = initialIndentLevel
    ))
  }
}
