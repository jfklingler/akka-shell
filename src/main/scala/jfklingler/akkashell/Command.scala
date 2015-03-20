package jfklingler.akkashell

import scala.util.matching.Regex

case class Command(name: String, command: Regex, help: String)
