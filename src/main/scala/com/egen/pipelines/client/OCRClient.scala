package com.egen.pipelines.client

import java.net.URLEncoder

import com.google.gson.{Gson, JsonElement}
import org.apache.http.client.methods.{HttpGet, HttpPost}
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.DefaultHttpClient

class OCRClient {

  val apiKey = "64f37537b688957"

  def getHttpContent(url: String, language: String = "eng") : String = {
    val client = new DefaultHttpClient()

    val outputUrl = s"https://api.ocr.space/parse/imageurl?apikey=$apiKey&url=$url&language=$language&isOverlayRequired=true"
    val response = client.execute(new HttpGet(outputUrl)).getEntity.getContent

    io.Source.fromInputStream(response).mkString
  }

  def postHttpContentFromFile(image: String, language: String = "eng") : String = {
    val client = new DefaultHttpClient()

    val stringEntity = new StringEntity(
      mapToString(
        Map("apikey" -> URLEncoder.encode(apiKey, "UTF-8"),
            "language" -> URLEncoder.encode(language, "UTF-8"),
            "base64Image"-> URLEncoder.encode(image, "UTF-8"),
            "isOverlayRequired" -> "true")))

    val post = new HttpPost("https://api.ocr.space/parse/image")
    post.addHeader("Content-Type", "application/x-www-form-urlencoded")
    post.setEntity(stringEntity)

    val response = io.Source.fromInputStream(client.execute(post).getEntity.getContent).mkString
    val parsed_output = new Gson().fromJson(response, classOf[JsonElement])
      .getAsJsonObject
      .get("ParsedResults")
      .getAsJsonArray
      .get(0)
      .getAsJsonObject
      .get("ParsedText")

    parsed_output.toString
  }

  def mapToString(map: Map[String, String]): String = {
    val str = new StringBuilder
    for ((k,v) <- map) {
      str.append(k).append("=").append(v).append("&")
    }
    str.toString
  }
}