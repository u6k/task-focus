FROM openjdk:8-alpine AS dev

COPY . /var/task-focus
WORKDIR /var/task-focus
RUN ./mvnw clean package

FROM openjdk:8-alpine
LABEL maintainer="u6k.apps@gmail.com"

COPY --from=dev /var/task-focus/target/task-focus.jar /opt/task-focus.jar

ENV APP_DB_PATH /var/task-focus/db/task-focus
EXPOSE 8080

CMD ["java", "-jar", "/opt/task-focus.jar"]
