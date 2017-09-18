package com.egen.pipelines.model

import org.scalatest.mockito.MockitoSugar
import org.scalatest.{FlatSpec, Matchers}

class PipelineSpec extends FlatSpec with Matchers with MockitoSugar {

  val id = "ID";
  val name = "NAME"
  val imageCount = 10
  val status = "running"

  "Pipeline " should "contain " in {
    val pipeline = new Pipeline(id, name, imageCount, status)
    pipeline.id shouldBe id
    pipeline.name shouldBe name
    pipeline.totalImages shouldBe imageCount
    pipeline.status shouldBe status
  }
}
