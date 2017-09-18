package com.egen.pipelines.repository

import java.net.InetAddress

import com.egen.pipelines.model.{Pipeline, ProcessedJson}
import com.google.gson.{Gson, JsonElement}
import org.elasticsearch.action.index.IndexRequest
import org.elasticsearch.client.transport.TransportClient
import org.elasticsearch.common.settings.Settings
import org.elasticsearch.common.transport.InetSocketTransportAddress
import org.json4s._
import org.json4s.jackson.Serialization
import org.json4s.jackson.Serialization.write
import org.slf4j.LoggerFactory

import scala.collection.JavaConversions._
import scala.util.Try

class Repository(esHost: String, esPort: Integer) {

  val PIPELINES = "pipelines"
  val logger = LoggerFactory.getLogger(getClass)
  val clientSettings = Settings.settingsBuilder
                               .put("cluster.name", "elasticsearch")
                               .build
  val address = new InetSocketTransportAddress(InetAddress.getByName(esHost),esPort)
  val client = TransportClient.builder()
                              .settings(clientSettings)
                              .build()
                              .addTransportAddress(address)
  val gson = new Gson()

  def executeQuery(processedJson: ProcessedJson, index: String) = {
    implicit val formats = DefaultFormats
    val indexRequest: IndexRequest = new IndexRequest(index, index).source(write(processedJson))
    client.index(indexRequest).get()
  }

  def getAll(index: String) : Try[JsonElement] = {
    Try {
      val response = client.prepareSearch(index).setSource("{size:100}").execute().actionGet()
      val output = response.getHits.getHits.toStream.map(searchHit => searchHit.getSource).toArray
      gson.toJsonTree(output)
    }
  }

  def getPipelineForId(id: String): Try[Pipeline] = {
    val response = client.prepareGet(PIPELINES, PIPELINES, id).get().getSource

    implicit val formats = Serialization.formats(NoTypeHints)
    gson.fromJson(Serialization.write(response.toMap), classOf[Pipeline]) //TODO stuck at response to pipeline conversion
  }

  def createPipeline(pipeline: Pipeline) = {
    implicit val formats = DefaultFormats
    val indexRequest: IndexRequest = new IndexRequest(PIPELINES, PIPELINES).source(write(pipeline))
    client.index(indexRequest).get()
  }
}