package com.egen.pipelines.utils

import org.scalatest.{BeforeAndAfterAll, FlatSpec, Matchers}

class StringParserSpec extends FlatSpec with Matchers with BeforeAndAfterAll {

  "StringParser messageFormat" should "format the message when all values passed" in {
     val str =  StringParser.messageFormat("image01.png", "sourceLocation", "targetLocation", "data:image/jpeg;base64,", "base64Image", "2017-08-22T20:03:43.132", "pipeline1", 20)
     str shouldBe "image01.png sourceLocation targetLocation data:image/jpeg;base64, base64Image 2017-08-22T20:03:43.132 pipeline1 20.0"
    }

  "StringParser messageFormat" should "return empty string" in {
    val str =  StringParser.messageFormat("","","","","","","", 0)
    str shouldBe "       0.0"
  }
}
