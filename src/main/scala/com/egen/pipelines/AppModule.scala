package com.egen.pipelines

import com.egen.pipelines.client.OCRClient
import com.egen.pipelines.controller.{PipelineController, ResultLogsController}
import com.egen.pipelines.kafka.Producer
import com.egen.pipelines.repository.Repository
import com.egen.pipelines.service.PipelineService
import com.egen.pipelines.utils.ImageProcessor
import scaldi.Module
import org.eclipse.jetty.server.Server

class AppModule extends Module {

  val resultsTopic = "jsonImageData"
  val dataImageTopic = "imageJsonData"
  val serverPort = 8090
  val brokerHost = "localhost:9092"
  val esHost = "localhost"
  val esPort = 9300

  bind[Repository] to new Repository(esHost, esPort)
  bind[OCRClient] to new OCRClient
  bind[Producer] to new Producer(brokerHost)
  bind[ImageProcessor] to new ImageProcessor(inject[Repository], inject[Producer], inject[OCRClient], resultsTopic)
  bind[PipelineService] to new PipelineService(inject[Producer], dataImageTopic, inject[Repository])
  bind[PipelineController] to new PipelineController(inject[Repository], inject[PipelineService])
  bind[ResultLogsController] to new ResultLogsController(brokerHost, resultsTopic)
 // bind[Consumer] to new Consumer(inject[ImageProcessor], inject[Producer], brokerHost, dataImageTopic, resultsTopic)
  bind[Server] to new Server(serverPort)

}
