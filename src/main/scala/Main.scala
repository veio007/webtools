package com.example  // remember this package in the sbt project definition
import java.util.concurrent.LinkedBlockingQueue

import org.eclipse.jetty.server.{Server, ServerConnector}
import org.eclipse.jetty.servlet.{DefaultServlet, ServletContextHandler}
import org.eclipse.jetty.util.thread.QueuedThreadPool
import org.eclipse.jetty.webapp.WebAppContext
import org.scalatra.servlet.ScalatraListener

object JettyLauncher { // this is my entry object as specified in sbt project definition
def main(args: Array[String]) {
    val port = if(System.getenv("PORT") != null) System.getenv("PORT").toInt else 8000

    println(port)

    val pool = new QueuedThreadPool(50, 50, 3000, new LinkedBlockingQueue[Runnable](10000))

    val server = new Server(pool)

    val connector: ServerConnector = new ServerConnector(server)
    connector.setAcceptQueueSize(1000)
    connector.setPort(port)
    connector.setIdleTimeout(3000)

    val context = new WebAppContext()
    context setContextPath "/"
    context.setResourceBase("src/main/webapp")
    context.addEventListener(new ScalatraListener)
    context.addServlet(classOf[DefaultServlet], "/")

    server.addConnector(connector)
    server.setHandler(context)

    server.start
    server.join
}
}