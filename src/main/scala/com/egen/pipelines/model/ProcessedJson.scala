package com.egen.pipelines.model

case class ProcessedJson(name: String,
                         sourceLocation: String,
                         targetLocation: String,
                         metadata: String,
                         base64Image: String,
                         startDateTime: String,
                         endDateTime:String,
                         text: String,
                         isValid: Boolean,
                         stackTrace: String,
                         pipelineName: String,
                         processTime: Long,
                         imageSize:  Double,
                         throughput: Double)