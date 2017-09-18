package com.egen.pipelines.controller

import java.io.File
import java.util.UUID

import org.json4s.jackson.Serialization.read
import org.json4s.jackson.Serialization.write
import com.egen.pipelines.model.{Pipeline, Request, Response, S3Request}
import com.egen.pipelines.repository.Repository
import com.egen.pipelines.service.PipelineService
import org.json4s.{DefaultFormats, Formats}
import org.scalatra.json.{JValueResult, JacksonJsonSupport}
import org.scalatra.scalate.ScalateSupport
import org.scalatra.{CorsSupport, ScalatraServlet, _}
import org.slf4j.LoggerFactory

import scala.util.{Failure, Success}

class PipelineController(repository: Repository, service: PipelineService) extends ScalatraServlet with CorsSupport
  with ScalateSupport with JValueResult
  with JacksonJsonSupport with SessionSupport {

  protected implicit val jsonFormats: Formats = DefaultFormats

  val StatusRunning = "completed"
  val logger = LoggerFactory.getLogger(this.getClass)
  val headers = Map("Access-Control-Allow-Origin" -> "*",
                   "Access-Control-Allow-Methods" -> "POST, GET, OPTIONS, DELETE",
                   "Access-Control-Max-Age" -> "3600",
                   "Access-Control-Allow-Headers" -> "x-requested-with, content-type")

  get("/:index"){
      repository.getAll({params("index")}) match {
         case Success(response) => Ok(response, headers)
         case Failure(e) => NotFound(e)
   }
  }

  get("/getAllPipelines") {
    repository.getAll("pipelines") match {
      case Success(response) => Ok(response, headers)
      case Failure(e) => NotFound(e)
    }
  }

  post("/start/localInput") {
    val requestBody = read[Request](request.body)
    val imgFiles = (new File(requestBody.sourceDirectory))
                    .listFiles()
                    .filter(name => name.getName.endsWith(".jpg") || name.getName.endsWith(".gif") || name.getName.endsWith(".png"))
    service.publishPipeline(Pipeline(UUID.randomUUID().toString, requestBody.pipelineName, imgFiles.length, StatusRunning))
    imgFiles.foreach(imgFile =>
                    service.processLocalRequest(imgFile.toString, requestBody))

    Ok(write(Response(requestBody.pipelineName, imgFiles.length)), headers)
  }

  post("/start/s3Input") {
    try {
      val requestBody = read[S3Request](request.body)
      Ok(write(Response(requestBody.pipelineName, service.processS3Request(requestBody))), headers)
    } catch {
      case e: Exception => InternalServerError(s"Error accessing S3 bucket:${e}")
    }
  }
}
