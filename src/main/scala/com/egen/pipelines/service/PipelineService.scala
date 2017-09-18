package com.egen.pipelines.service

import java.io.{File, FileInputStream}
import java.time.{LocalDateTime, ZoneOffset}
import java.util.{Base64, UUID}

import com.egen.pipelines.client.S3Client
import com.egen.pipelines.kafka.Producer
import com.egen.pipelines.model.{Pipeline, Request, S3Request}
import com.egen.pipelines.repository.Repository
import com.egen.pipelines.utils.StringParser
import org.slf4j.LoggerFactory

import scala.collection.JavaConversions._

class PipelineService(producer: Producer, topic: String, repository: Repository) {

  val MetaData = "data:image/jpeg;base64,"
  val StatusRunning  = "completed"
  val logger = LoggerFactory.getLogger(this.getClass)

  def processS3Request(requestBody: S3Request) : Int = {
    val s3client = new S3Client(requestBody.awsKey, requestBody.awsSecret)
    val objSummaries = s3client.instance.listObjects(requestBody.bucketName).getObjectSummaries
    val count  = s3client.instance.listObjectsV2(requestBody.bucketName).getKeyCount

    // creating pipeline and persisting it
    publishPipeline(Pipeline(UUID.randomUUID().toString, requestBody.pipelineName, count, StatusRunning))

    objSummaries.forEach(obj => {
      val base64 = s3client.downloadImageFromS3(obj.getBucketName, obj.getKey)
      val message = StringParser
        .messageFormat(
          obj.getKey,
          requestBody.bucketName,
          requestBody.targetDirectory,
          MetaData,
          base64,
          LocalDateTime.now(ZoneOffset.UTC).toString(),
          requestBody.pipelineName,
          s3client.instance.getObject(obj.getBucketName, obj.getKey).getObjectMetadata.getContentLength/1024d)
      producer.sendMessage(message, topic)
    })
    count // returning number of files in the bucket
  }

  def processLocalRequest(img: String, requestBody: Request) {
    val file = new File(img)
    val imageData = new Array[Byte](file.length.asInstanceOf[Int])
    new FileInputStream(file).read(imageData)
    val base64String = Base64.getEncoder.encodeToString(imageData)

    val message = StringParser
      .messageFormat(
        img.toString,
        requestBody.sourceDirectory,
        requestBody.targetDirectory,
        MetaData,
        base64String,
        LocalDateTime.now(ZoneOffset.UTC).toString(),
        requestBody.pipelineName,
        file.length()/1024)
    producer.sendMessage(message, topic)
  }

  def publishPipeline(pipeline: Pipeline) = {
    repository.createPipeline(pipeline)
  }
}
