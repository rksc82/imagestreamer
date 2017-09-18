package com.egen.pipelines.kafka

import net.manub.embeddedkafka.{EmbeddedKafka, EmbeddedKafkaConfig}
import org.scalatest.{BeforeAndAfterAll, FlatSpec, Matchers}

class KafkaProducerSpec extends FlatSpec with EmbeddedKafka with Matchers with BeforeAndAfterAll {

  "Producer with message" should "produce expected message on the topic" in {
    val userDefinedConfig = EmbeddedKafkaConfig(kafkaPort = 9191, zooKeeperPort = 2185)

    val producer = new Producer("localhost:9191")
    producer.sendMessage("message", "TOPIC")
    val message = consumeFirstStringMessageFrom("TOPIC")
    message shouldBe "message"
  }
}
