package com.egen.pipelines.utils

import com.egen.pipelines.client.OCRClient
import com.egen.pipelines.kafka.Producer
import com.egen.pipelines.model.Message
import com.egen.pipelines.repository.Repository
import org.mockito.Mockito._
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{BeforeAndAfterAll, FlatSpec, Matchers}

class ImageProcessorSpec extends FlatSpec with Matchers with MockitoSugar with BeforeAndAfterAll {

  "ImageProcessor process text" should "process text" in {
    val mockRepository = mock[Repository]
    val mockOcrClient = mock[OCRClient]
    val mockProducer = mock[Producer]
    val imageProcessor = new ImageProcessor(mockRepository, mockProducer, mockOcrClient, "topic" )
    imageProcessor.processText(Message("value1", "value2", "value3","value4","value5", "2009-11-15T14:12:12", "value7", 20))
    verify(mockOcrClient).postHttpContentFromFile("value3value4")
  }

  "ImageProcessor process Image" should "process image" in {
    val mockRepository = mock[Repository]
    val mockOcrClient = mock[OCRClient]
    val mockProducer = mock[Producer]

    val imageProcessor = new ImageProcessor(mockRepository, mockProducer, mockOcrClient, "topic")
    imageProcessor.processImage(Message("value1", "value2", "value3","value4","value5", "2009-11-15T14:12:12", "value7", 20), 0)
  }
}
