package com.egen.pipelines.utils

object StringParser {
   def messageFormat(img: String,
                     sourceLocation:String,
                     targetLocation: String,
                     metaData: String,
                     base64Image: String,
                     startDateTime: String,
                     pipelineName: String,
                     imageSize: Double )=
     s"${img} ${sourceLocation} ${targetLocation} ${metaData} ${base64Image} ${startDateTime} ${pipelineName} ${imageSize}"
}
