# Build application
FROM openjdk:8-alpine AS dev

## Build application
WORKDIR /var/task-focus
COPY . .
RUN ./mvnw -Duser.timezone=Asia/Tokyo -Duser.country=JP -Duser.language=ja clean package

# Package application
FROM openjdk:8-alpine
LABEL maintainer="u6k.apps@gmail.com"

## Copy application
COPY --from=dev /var/task-focus/target/task-focus.jar /opt/task-focus.jar

## Setting docker
ENV APP_DB_PATH /var/task-focus/db/task-focus
EXPOSE 8080

CMD ["java", "-Duser.timezone=Asia/Tokyo", "-Duser.country=JP", "-Duser.language=ja", "-jar", "/opt/task-focus.jar"]
