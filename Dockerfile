FROM gradle:8.10.2-jdk21 AS builder
WORKDIR /app

COPY build.gradle settings.gradle gradlew ./
COPY gradle gradle
RUN ./gradlew dependencies --no-daemon

COPY src src
RUN ./gradlew clean build -x test --no-daemon


FROM eclipse-temurin:21-jdk
WORKDIR /app

COPY --from=builder /app/build/libs/*.jar app.jar

ENV SPRING_PROFILES_ACTIVE=dev
ENV CONFIG_SERVER_URI=http://config-server:8888
ENV EUREKA_URI=http://eureka-server:8761/eureka
ENV DB_HOST=postgres
ENV DB_PORT=5432
ENV DB_USER=postgres
ENV DB_PASSWORD=100juanU

EXPOSE 8081
HEALTHCHECK CMD curl -f http://localhost:8086/actuator/health || exit 1

ENTRYPOINT ["java","-jar","app.jar"]
