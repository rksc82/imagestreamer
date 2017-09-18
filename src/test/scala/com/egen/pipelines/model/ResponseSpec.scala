package com.egen.pipelines.model

import org.scalatest.mockito.MockitoSugar
import org.scalatest.{FlatSpec, Matchers}

class ResponseSpec extends FlatSpec with Matchers with MockitoSugar {

  val name = "NAME"
  val imageCount = 10

  "Response " should "contain " in {
    val response = new Response(name, imageCount)
    response.name shouldBe name
    response.totalImages shouldBe imageCount
  }
}
