package com.egen.pipelines

import org.eclipse.jetty.server.Server
import org.eclipse.jetty.servlet.DefaultServlet
import org.eclipse.jetty.webapp.WebAppContext
import org.scalatra.servlet.ScalatraListener
import org.slf4j.LoggerFactory

import scala.concurrent.Future
import scala.concurrent._
import ExecutionContext.Implicits.global
import scaldi.Injectable._

object JettyLauncher{

  implicit val injector = new AppModule

  def main(args: Array[String]) {
//    val streamsConsumer = inject[Consumer]
    val logger = LoggerFactory.getLogger(this.getClass)

    val context = new WebAppContext()
    context setContextPath "/"
    context.setResourceBase("src/main/scala/webapp")
    context.addEventListener(new ScalatraListener)
    context.addServlet(classOf[DefaultServlet], "/")

    val server = inject[Server]
    server.setHandler(context)
    server.start

    Future {
//      streamsConsumer.start
      print("Server starting....")
      server.join
    }
  }
}