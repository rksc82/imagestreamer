name := "data-pipelines-unstructured"
organization := "com.egen.pipelines"

version := "0.1"

scalaVersion := "2.12.0"

mainClass in assembly := some("com.egen.pipelines.JettyLauncher")
assemblyJarName := "data-pipelines-unstructured.jar"

assemblyMergeStrategy in assembly := {
  case PathList("org", "joda", "time", "base", "BaseDateTime.class") => MergeStrategy.first
  case x =>
    val oldStrategy = (assemblyMergeStrategy in assembly).value
    oldStrategy(x)
}

libraryDependencies ++= Seq(
  "org.eclipse.jetty" % "jetty-webapp" % "9.1.5.v20140505",
  "org.eclipse.jetty" % "jetty-plus" % "9.1.5.v20140505",
  "org.eclipse.jetty.websocket" % "websocket-server" % "9.1.5.v20140505",
  "org.elasticsearch" % "elasticsearch" % "2.3.5",
  "org.apache.kafka" % "kafka-clients" % "0.11.0.0",
  "org.apache.kafka" % "kafka-streams" % "0.11.0.0",
  "org.apache.httpcomponents" % "httpclient" % "4.5.3",
  "com.google.code.gson" % "gson" % "2.8.1",
  "org.json4s" % "json4s-jackson_2.12" % "3.5.3",
  "org.codehaus.jackson" % "jackson-core-asl" % "1.1.0",
  "org.scalatra" % "scalatra-json_2.12" % "2.5.1",
  "org.scalatra" % "scalatra-atmosphere_2.12" % "2.5.1",
  "org.scalatra" % "scalatra-scalate_2.12" % "2.5.1",
  "org.scaldi" % "scaldi_2.12" % "0.5.8",
  "org.scalatest" % "scalatest_2.12" % "3.0.4" % "test",
  "com.amazonaws" % "aws-java-sdk" % "1.11.186",
  "net.manub" % "scalatest-embedded-kafka_2.12" % "0.15.1" % "test",
  "org.mockito" % "mockito-core" % "1.8.5" % "test",
  "org.scalatra" % "scalatra-scalatest_2.12" % "2.5.1",
  "com.github.tomakehurst" % "wiremock-standalone" % "2.6.0"
)

