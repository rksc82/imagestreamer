# imagestreamer

This is a scala sbt based project written using Scalatra

It has a backend engine which produces images stream bytes of data on kafka topic. 
Consumer consumes these byte stream of images and extracts the text information using OCR online library
The data extracted is ingested to elastic search
