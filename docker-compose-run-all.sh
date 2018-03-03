#!/usr/bin/env bash

# TODO ./gradlew clean build buildDockerImage does not work
./gradlew :backend-eureka-service:clean :backend-eureka-service:build :backend-eureka-service:buildDockerImage && \
    ./gradlew :backend-config-service:clean :backend-config-service:build :backend-config-service:buildDockerImage && \
    ./gradlew :backend-sample-service:clean :backend-sample-service:build :backend-sample-service:buildDockerImage && \
    docker-compose -f docker/common/docker-compose.yml up
