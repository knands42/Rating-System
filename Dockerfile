FROM gradle:7.2.0-jdk17 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build

FROM openjdk:17-jdk-alpine3.14 AS final
RUN mkdir /app
COPY --from=build /home/gradle/src/build/libs/*all.jar /app/app.jar
ENTRYPOINT ["java","-jar","/app/app.jar"]