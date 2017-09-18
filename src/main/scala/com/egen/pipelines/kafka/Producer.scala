package com.egen.pipelines.kafka

import java.util.Properties

import org.apache.kafka.clients.producer._
import org.slf4j.LoggerFactory

class Producer(brokerHost: String) {

  val logger = LoggerFactory.getLogger(this.getClass)
  val kafkaStringSerializer = "org.apache.kafka.common.serialization.StringSerializer"
  val batchSize: java.lang.Integer = 64 * 1024
  val linger: java.lang.Integer = 5000

  val props = new Properties()
  props.put("key.serializer", kafkaStringSerializer)
  props.put("value.serializer", kafkaStringSerializer)
  props.put("batch.size", batchSize)
  props.put("linger.ms", linger)
  props.put("bootstrap.servers", brokerHost)

  val producer = new KafkaProducer[String, String](props)

  def sendMessage(message: String, topic: String) = {
    logger.info(s"Topic:${topic}, Message ======>${message}" )
    val record: ProducerRecord[String, String] = new ProducerRecord(topic, message)
    producer.send(record)
  }
}
