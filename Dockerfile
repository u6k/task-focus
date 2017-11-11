FROM openjdk:8-alpine
LABEL maintainer="u6k.apps@gmail.com"

COPY target/task-focus.jar /opt/task-focus.jar

EXPOSE 8080
ENV _JAVA_OPTIONS="-Duser.timezone=Asia/Tokyo -Duser.country=JP -Duser.language=ja"

CMD ["java", "-jar", "/opt/task-focus.jar"]
