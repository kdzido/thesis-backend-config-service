FROM openjdk:8-jdk-alpine
RUN  apk update && apk upgrade && apk add netcat-openbsd

# Artifacts Installation
RUN mkdir -p /usr/local/config-service
ADD @springBootJar@ /usr/local/config-service
ADD run.sh run.sh
RUN chmod +x run.sh

CMD ["/run.sh"]
