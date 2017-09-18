#!/usr/bin/env bash

#kill and remove existing containers
docker kill $(docker ps -a -q)
docker rm $(docker ps -a -q)

#remove all untagged images
docker rmi $(docker images | grep "^<none>" | awk "{print $3}")