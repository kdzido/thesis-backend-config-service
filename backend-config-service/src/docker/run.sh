#!/bin/sh
echo "Starting Cloud Config server"
echo "------------------------------"
java -Djava.security.egd=file:/dev/./urandom -jar /usr/local/config-service/@springBootJar@
