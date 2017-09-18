package com.egen.pipelines.controller

import java.util
import java.util.Properties

import org.apache.kafka.clients.consumer.KafkaConsumer
import org.json4s._
import org.scalatra._
import org.scalatra.atmosphere._
import org.scalatra.json.{JValueResult, JacksonJsonSupport}
import org.scalatra.scalate.ScalateSupport
import org.slf4j.LoggerFactory

import scala.collection.JavaConverters._
import scala.concurrent.ExecutionContext.Implicits.global

class ResultLogsController(brokerHost: String, resultsTopic:String)
  extends ScalatraServlet
  with ScalateSupport with JValueResult
  with JacksonJsonSupport with SessionSupport
  with AtmosphereSupport {

  implicit protected val jsonFormats: Formats = DefaultFormats
  val logger = LoggerFactory.getLogger(this.getClass)

  atmosphere("/processResults") {

    new AtmosphereClient {
      def receive: AtmoReceive = {
        case Connected =>
          logger.info("Client %s is connected" format uuid)
          val props = new Properties()
          props.put("bootstrap.servers", brokerHost)
          props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
          props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
          props.put("group.id", "something")

          val consumer = new KafkaConsumer[String, String](props)
          logger.info("Web Consumer started...")
          consumer.subscribe(util.Collections.singletonList(resultsTopic))
          val records = consumer.poll(10)
          for (record <- records.asScala) {
              logger.info("Processed record....." + record.value)
              records.asScala.foreach(record => broadcast(record.value(), Everyone))

          }
      }
    }
  }
}