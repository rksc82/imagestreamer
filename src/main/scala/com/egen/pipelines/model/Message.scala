package com.egen.pipelines.model

case class Message (name: String,
                    sourceLocation: String,
                    targetLocation: String,
                    metadata: String,
                    base64Image: String,
                    startDateTime: String,
                    pipelineName: String,
                    imageSize: Double)