package demo.marshalling.contracts

import play.api.libs.json.{ Json, OWrites }

case class Response(message: String)

object Response {
  implicit val ResponseWrites: OWrites[Response] = Json.writes[Response]
}
