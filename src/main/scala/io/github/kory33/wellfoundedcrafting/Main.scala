package io.github.kory33.wellfoundedcrafting

import scala.scalajs.js
import scala.scalajs.js.annotation.*

import org.scalajs.dom
import cats.effect.IOApp
import cats.effect.IO
import cats.effect.ExitCode
import cats.implicits.*
import cats.effect.unsafe.IORuntime

// import javascriptLogo from "/javascript.svg"
@js.native @JSImport("/javascript.svg", JSImport.Default)
val javascriptLogo: String = js.native

object Main extends IOApp {
  def run(args: List[String]): cats.effect.IO[cats.effect.ExitCode] = {
    given IORuntime = runtime

    val setupAppElement = IO {
      dom.document.querySelector("#app").innerHTML = s"""
        <div>
          <a href="https://vitejs.dev" target="_blank">
            <img src="/vite.svg" class="logo" alt="Vite logo" />
          </a>
          <a href="https://developer.mozilla.org/en-US/docs/Web/JavaScript" target="_blank">
            <img src="$javascriptLogo" class="logo vanilla" alt="JavaScript logo" />
          </a>
          <h1>Hello Scala.js!</h1>
          <div class="card">
            <button id="counter" type="button"></button>
          </div>
          <p class="read-the-docs">
            Click on the Vite logo to learn more
          </p>
        </div>
      """
    }

    def setupCounter(element: dom.Element): IO[Unit] = {
      for {
        counter <- IO.ref(0)
        setCounter = (count: Int) =>
          counter.set(count) >> IO { element.innerHTML = s"count is ${count}" }
        incrementCounter = counter.updateAndGet(_ + 3) >>= setCounter
        _ <- IO {
          element.addEventListener("click", _ => incrementCounter.unsafeRunAndForget())
        }
        _ <- setCounter(0)
      } yield ()
    }

    for {
      _ <- setupAppElement
      _ <- IO(dom.document.getElementById("counter")) >>= setupCounter
      _ <- IO.println("Hello, world!")
    } yield ExitCode.Success
  }
}
