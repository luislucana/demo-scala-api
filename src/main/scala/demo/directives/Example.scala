package demo.directives

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer

import scala.io.StdIn

object Example extends App {

  implicit val system = ActorSystem("my-system")
  implicit val materializer = ActorMaterializer()
  // needed for the future flatMap/onComplete in the end
  implicit val executionContext = system.dispatcher

  val getOrPut = get | put

  val route =
    path("order" / IntNumber) { id =>
      (get | put) { ctx =>
        ctx.complete(s"Received ${ctx.request.method.name} request for order $id")
      }
    } ~
    path("neworder" / IntNumber) { id =>
      (get | put) {
        extractMethod { m =>
          complete(s"Received ${m.name} request for order $id")
        }
      }
    } ~
    path("orderagain" / IntNumber) { id =>
      getOrPut {
        extractMethod { m =>
          complete(s"Received ${m.name} request for order $id")
        }
      }
    }

  val bindingFuture = Http().bindAndHandle(route, "localhost", 8080)

  println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
  StdIn.readLine() // let it run until user presses return
  bindingFuture
    .flatMap(_.unbind()) // trigger unbinding from the port
    .onComplete(_ => system.terminate()) // and shutdown when done

}
