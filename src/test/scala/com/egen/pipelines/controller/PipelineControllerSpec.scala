package com.egen.pipelines

import com.egen.pipelines.controller.PipelineController
import com.egen.pipelines.repository.Repository
import com.egen.pipelines.service.PipelineService
import com.google.gson.{Gson, JsonElement}
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{BeforeAndAfter, FlatSpec, Matchers}
import org.scalatra.test.scalatest.{ScalatraFlatSpec, ScalatraSuite}
import org.mockito.Mockito._
import com.google.gson.JsonElement

import scala.util.Try

class PipelineControllerSpec extends FlatSpec
  with ScalatraSuite
  with Matchers
  with ScalatraFlatSpec
  with BeforeAndAfter
  with MockitoSugar {

  val repository = mock[Repository]
  val service = mock[PipelineService]
  val pipelineController = new PipelineController(repository, service)

  addServlet(pipelineController, "/*")

  "PipelineController GET /getAll " should "give 200 and return the response" in {
    val gson = new Gson()
    val jsonElement: JsonElement = gson.fromJson("", classOf[JsonElement])
    when(repository.getAll("pipelines")).thenReturn(Try(jsonElement))
    get("/getAllPipelines") {
      status shouldBe 200
    }
  }

  "PipelineController GET /:Id" should "give 200 and return the response" in {
    val gson = new Gson()
    val jsonElement: JsonElement = gson.fromJson("", classOf[JsonElement])
    when(repository.getAll("pipeline1")).thenReturn(Try(jsonElement))
    get("/pipeline1") {
      status shouldBe 200
    }
  }


}

