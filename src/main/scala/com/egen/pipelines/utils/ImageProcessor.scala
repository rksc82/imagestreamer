package com.egen.pipelines.utils

import java.io.FileOutputStream
import java.time.{LocalDateTime, ZoneOffset}
import java.util.Base64

import com.egen.pipelines.client.OCRClient
import com.egen.pipelines.kafka.Producer
import com.egen.pipelines.model.{Message, ProcessedJson}
import com.egen.pipelines.repository.Repository
import com.sun.tools.internal.ws.wsdl.document.jaxws.Exception
import org.json4s.DefaultFormats
import org.json4s.jackson.Serialization.write
import org.slf4j.LoggerFactory

class ImageProcessor(repository: Repository, producer: Producer, oCRClient: OCRClient, resultsTopic: String) {

  implicit val formats = DefaultFormats
  val logger = LoggerFactory.getLogger(this.getClass)

  def processImage(message: Message, count: Integer) = {

    val imageOutFile = new FileOutputStream(s"${message.targetLocation}TransformedImage${count}.jpg")
    val imageByteArray = Base64.getDecoder().decode(message.base64Image)
    println("Writing image....")
    imageOutFile.write(imageByteArray)
  }

  def processText(message: Message): String = {
    try {
      val text = oCRClient.postHttpContentFromFile(s"${message.metadata}${message.base64Image}")
      println("Extracting image....")

      val processTime = calculateProcessedTime(LocalDateTime.parse(message.startDateTime), LocalDateTime.now(ZoneOffset.UTC))
      println("processedTime=========>" + processTime)
      val processedJson = ProcessedJson(message.name,
        message.sourceLocation,
        message.targetLocation,
        message.metadata,
        message.base64Image,
        message.startDateTime,
        LocalDateTime.now(ZoneOffset.UTC).toString,
        text,
        true,
        null,
        message.pipelineName,
        processTime,
        message.imageSize,
        (message.imageSize) / processTime)
      repository.executeQuery(processedJson, message.pipelineName)
      write(processedJson).toString
    } catch {
      case e: Exception => {logger.info("Exception Occured:" + e)
      return null}
    }
  }

  def calculateProcessedTime(startDateTime: LocalDateTime, endDateTime: LocalDateTime): Long = {
    java.time.Duration.between(startDateTime,endDateTime).getSeconds
  }
}
