package com.egen.pipelines.model

import org.scalatest.mockito.MockitoSugar
import org.scalatest.{FlatSpec, Matchers}

class S3RequestSpec extends FlatSpec with Matchers with MockitoSugar {

  val targetDirectory = "targetDirectory"
  val pipelineName = "pipelineName"
  val bucketName = "bucketName"
  val awsKey = "awsKey"
  val awsSecret = "awsSecret"

  "S3Request " should "contain " in {
    val request = new S3Request(targetDirectory, pipelineName, bucketName, awsKey, awsSecret)
    request.targetDirectory shouldBe targetDirectory
    request.pipelineName shouldBe pipelineName
    request.bucketName shouldBe bucketName
    request.awsKey shouldBe awsKey
    request.awsSecret shouldBe awsSecret
  }
}
