package com.egen.pipelines.model

case class Request (sourceDirectory: String,
                    targetDirectory: String,
                    pipelineName: String)