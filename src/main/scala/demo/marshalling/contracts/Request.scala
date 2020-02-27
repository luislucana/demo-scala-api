package demo.marshalling.contracts

import play.api.libs.json.{ Json, Reads }

case class Request(name: String)

object Request {
  implicit val RequestReads: Reads[Request] = Json.reads[Request]
}