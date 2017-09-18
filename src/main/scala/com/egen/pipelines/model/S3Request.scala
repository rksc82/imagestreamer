package com.egen.pipelines.model

case class S3Request (targetDirectory: String,
                      pipelineName: String,
                      bucketName: String,
                      awsKey: String,
                      awsSecret: String)


