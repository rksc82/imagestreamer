package com.egen.pipelines.kafka

import java.time.{LocalDateTime, ZoneOffset}
import java.util.Properties

import com.egen.pipelines.client.OCRClient
import com.egen.pipelines.model.{Message, ProcessedJson}
import com.egen.pipelines.repository.Repository
import com.egen.pipelines.utils.ImageProcessor
import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.streams.kstream.{KStream, KStreamBuilder}
import org.apache.kafka.streams.{KafkaStreams, StreamsConfig}
import org.json4s.DefaultFormats
import org.json4s.jackson.Serialization.write
import org.slf4j.LoggerFactory

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.language.implicitConversions
import scala.util.{Failure, Success}

object Customer extends App {

  val resultsTopic = "jsonImageData"
  val dataImageTopic = "imageJsonData"
  val serverPort = 8090
  val brokerHost = "localhost:9092"
  val esHost = "localhost"
  val esPort = 9300

  val repository = new Repository(esHost, esPort)
  val producer = new Producer(brokerHost)
  val oCRClient = new OCRClient
  val imageProcessor = new ImageProcessor(repository, producer, oCRClient, resultsTopic)

  val logger = LoggerFactory.getLogger(this.getClass)
  implicit val formats = DefaultFormats
  val config = {
    val properties = new Properties()
    properties.put(StreamsConfig.APPLICATION_ID_CONFIG, "stream-application")
    properties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, brokerHost)
    properties.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass)
    properties.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass)
    properties
  }

  var count = 0

  println("Consumer starting...")
  val builder = new KStreamBuilder
  val textLines: KStream[String, String] = builder.stream(dataImageTopic)
  textLines.foreach((key, value) => {
    val recordArray = value.split(" ")
    val message = Message(recordArray(0),
      recordArray(1),
      recordArray(2),
      recordArray(3),
      recordArray(4),
      recordArray(5),
      recordArray(6),
      recordArray(7).toDouble
    )
    val processedImage = Future {
      imageProcessor.processImage(message, count)
    }
    processedImage onComplete {
      case Success(f) => println("Image Processed successfully")
      case Failure(f) => println("image processing failed"+ f)
    }
    count = count + 1
    val processTextFuture: Future[String] = Future {
      imageProcessor.processText(message)
    }
    processTextFuture onComplete {
      case Success(processedText) => producer.sendMessage(processedText, resultsTopic)
      case Failure(f) => { print(s"Image Processing failed"+f)
        producer.sendMessage(failedMessage(message), resultsTopic)
      }
    }
  })

  val streams = new KafkaStreams(builder, config)
  streams.start()


  def failedMessage(message: Message): String = {
    val failedJson = ProcessedJson(message.name,
                                   message.sourceLocation,
                                   message.targetLocation,
      message.metadata,
      message.base64Image,
      message.startDateTime,
      LocalDateTime.now(ZoneOffset.UTC).toString,
      null,
      false,
      "Image Processing text failed",
      message.pipelineName,
      0,
      message.imageSize,
      0)

    write(failedJson).toString
  }
}

