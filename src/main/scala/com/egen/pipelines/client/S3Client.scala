package com.egen.pipelines.client

import java.awt.image.BufferedImage
import java.io.{ByteArrayOutputStream, File}
import java.util.Base64
import javax.imageio.ImageIO

import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.model.{GetObjectRequest, PutObjectRequest}


class S3Client (key: String, secret: String) {

  private val _instance = new AmazonS3Client(new BasicAWSCredentials(key, secret))
  def instance() = _instance

  def encodeBase64URL(imgBuf: BufferedImage): String = {
    imgBuf match {
      case null => null
      case _ => {
        val out = new ByteArrayOutputStream
        ImageIO.write(imgBuf, "PNG", out)
        Base64.getEncoder.encodeToString(out.toByteArray)
      }
    }
  }

  def saveImageToS3(bucketName: String, imgBuf: BufferedImage, id: String): String = {
    val file = File.createTempFile(id, ".png")
    file.deleteOnExit()
    ImageIO.write(imgBuf, "png", file)
    instance.putObject(new PutObjectRequest(bucketName, id, file))
    file.delete
    bucketName
  }

  def downloadImageFromS3(bucketName: String, id: String): String = {
    val obj = instance.getObject(new GetObjectRequest(bucketName, id))
    encodeBase64URL(ImageIO.read(obj.getObjectContent))
  }
}
