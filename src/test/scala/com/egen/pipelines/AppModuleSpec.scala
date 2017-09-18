package com.egen.pipelines

import com.egen.pipelines.client.OCRClient
import com.egen.pipelines.controller.{PipelineController, ResultLogsController}
import com.egen.pipelines.kafka.Producer
import com.egen.pipelines.repository.Repository
import com.egen.pipelines.service.PipelineService
import com.egen.pipelines.utils.ImageProcessor
import org.eclipse.jetty.server.Server
import org.scalatest.{BeforeAndAfter, FlatSpec, Matchers}
import scaldi.Injectable._

class AppModuleSpec extends FlatSpec with Matchers with BeforeAndAfter{

  implicit val injector = new AppModule

  "AppModule" should "inject correct instance" in {

    val producer = inject[Producer]
    val repository =  inject[Repository]
    val oCRClient = inject[OCRClient]
    val imageProcessor =  inject[ImageProcessor]
    val pipelineService =inject[PipelineService]
    val pipelineController = inject[PipelineController]
    val resultLogsController = inject[ResultLogsController]
//    val streamsConsumer = inject[Consumer]
    val server = inject[Server]

    producer.getClass shouldBe classOf[Producer]
    repository.getClass shouldBe classOf[Repository]
    oCRClient.getClass shouldBe classOf[OCRClient]
    imageProcessor.getClass shouldBe classOf[ImageProcessor]
    pipelineService.getClass shouldBe classOf[PipelineService]
    pipelineController.getClass shouldBe classOf[PipelineController]
    resultLogsController.getClass shouldBe classOf[ResultLogsController]
//    streamsConsumer.getClass shouldBe classOf[Consumer]
    server.getClass shouldBe classOf[Server]
  }
}
