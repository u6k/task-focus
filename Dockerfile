FROM openjdk:8-alpine
LABEL maintainer="u6k.apps@gmail.com"

COPY target/task-focus.jar /opt/task-focus.jar

ENV _JAVA_OPTIONS="-Duser.timezone=Asia/Tokyo -Duser.country=JP -Duser.language=ja" \
    APP_DB_PATH=/var/lib/task-focus/db/task-focus

EXPOSE 8080

CMD ["java", "-jar", "/opt/task-focus.jar"]
