package com.egen.pipelines.model

import org.scalatest.mockito.MockitoSugar
import org.scalatest.{FlatSpec, Matchers}

class RequestSpec extends FlatSpec with Matchers with MockitoSugar {

  val sourceDirectory = "/source/sourceDirectory"
  val targetDirectory = "/target/targetDirectory"
  val pipelineName = "pipelineName"

  "Request " should "contain " in {
    val request = new Request(sourceDirectory, targetDirectory, pipelineName)
    request.sourceDirectory shouldBe sourceDirectory
    request.targetDirectory shouldBe targetDirectory
    request.pipelineName shouldBe pipelineName
  }
}
