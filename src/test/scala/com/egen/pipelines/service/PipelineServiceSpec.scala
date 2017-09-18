package com.egen.pipelines.service

import com.egen.pipelines.kafka.Producer
import com.egen.pipelines.model.Pipeline
import com.egen.pipelines.repository.Repository
import org.mockito.Mockito.verify
import org.scalatest.{BeforeAndAfter, FlatSpec, Matchers}
import org.scalatest.mockito.MockitoSugar
import org.scalatra.test.scalatest.{ScalatraFlatSpec, ScalatraSuite}

class PipelineServiceSpec extends FlatSpec
  with ScalatraSuite
  with Matchers
  with ScalatraFlatSpec
  with BeforeAndAfter
  with MockitoSugar {

  val repository = mock[Repository]
  val producer = mock[Producer]
  val topic = "topic"

  "Pipeline Service publishPipeline" should "publish pipeline to the elasticsearch" in {
    val pipeline = new Pipeline("id", "name", 10, "running")
    val pipelineService = new PipelineService(producer, topic, repository)
    pipelineService.publishPipeline(pipeline)
    verify(repository).createPipeline(pipeline)
  }

}
