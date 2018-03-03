#!/usr/bin/env bash

./gradlew :backend-config-service:clean :backend-config-service:build :backend-config-service:buildDockerImage && \
    docker run -ti --rm -p 8888:8888 kdzido/thesis-configservice:latest

# -ti - interactive mode (ctrl-c) to stop container
# --rm - container will be removed after stop
