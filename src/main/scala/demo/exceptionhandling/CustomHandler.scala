package demo.exceptionhandling

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import StatusCodes._
import akka.http.scaladsl.server._
import Directives._
import akka.stream.ActorMaterializer

object CustomHandler extends App {

  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()

  val myExceptionHandler = ExceptionHandler {
    case _: ArithmeticException =>
      extractUri { uri =>
        println(s"Request to $uri could not be handled normally")
        complete(HttpResponse(InternalServerError, entity = "Bad numbers, bad result!!!"))
      }
  }

  val route: Route =
    handleExceptions(myExceptionHandler) {
      path("divide") {
        complete((1 / 0).toString) //Will throw ArithmeticException
      }
    }

  Http().bindAndHandle(route, "localhost", 8080)

}
