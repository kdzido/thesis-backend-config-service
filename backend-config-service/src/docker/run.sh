#!/bin/sh

echo "-> Waiting for the Eureka service to start on port $EUREKASERVICE_PORT...."
while ! `nc -z eurekaservice $EUREKASERVICE_PORT`; do sleep 3; done
echo "--> Eureka service has started"

echo "-> Starting Cloud Config service with Eureka endpoint: $EUREKASERVICE_URI"
java    -Djava.security.egd=file:/dev/./urandom \
        -Deureka.client.serviceUrl.defaultZone=$EUREKASERVICE_URI \
        -jar /usr/local/config-service/@springBootJar@
