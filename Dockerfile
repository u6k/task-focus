FROM openjdk:8-alpine
MAINTAINER u6k.apps@gmail.com

VOLUME /opt
WORKDIR /opt
COPY target/task-focus-webapp.jar .

VOLUME /var/lib/task-focus-webapp/hsqldb

EXPOSE 8080

CMD ["java", "-jar", "/opt/task-focus-webapp.jar"]
