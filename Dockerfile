FROM java:8

RUN mkdir -p /opt/egen/pipelines/

ADD /target/scala-2.12/data-pipelines-unstructured.jar /opt/egen/pipelines/
RUN chmod +x /opt/egen/pipelines/data-pipelines-unstructured.jar

ADD pipelines.sh /opt/egen/pipelines/
RUN chmod +x /opt/egen/pipelines/pipelines.sh

EXPOSE 8080

CMD ["/opt/egen/pipelines/pipelines.sh"]