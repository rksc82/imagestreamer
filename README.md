# imagestreamer

This is a scala sbt based project written using Scalatra

It is a backend engine which images into stream bytes and  produces the data on kafka topic. 
Consumer consumes these byte stream of images and extracts the text information using OCR online library
The data extracted is ingested to elastic search
