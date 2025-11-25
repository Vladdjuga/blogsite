# Build stage
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app
# Copy only pom first to leverage dependency layer caching
COPY pom.xml .
RUN mvn -q -e -DskipTests dependency:go-offline
# Copy source
COPY src ./src
# Build the Spring Boot fat jar
RUN mvn -q -DskipTests clean package

# Runtime stage (smaller JRE image)
FROM eclipse-temurin:21-jre
WORKDIR /app
# Copy built jar from build stage
COPY --from=build /app/target/blogsite-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
# Use exec form; pass JVM opts via COMPOSE or kubernetes env if needed
ENTRYPOINT ["java","-jar","/app/app.jar"]

