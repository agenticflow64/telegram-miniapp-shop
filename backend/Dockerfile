FROM gradle:8.4-jdk17 AS build

WORKDIR /app


COPY build.gradle.kts settings.gradle.kts ./
COPY gradle ./gradle
RUN gradle dependencies --no-daemon


COPY src ./src
RUN gradle build --no-daemon -x test

RUN ls /app/build/libs/

FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
#COPY --from=build /app/build/libs/*.jar app.jar

COPY --from=build /app/build/libs/ /app/

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
