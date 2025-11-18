# ===== 1) Build stage =====
FROM maven:3.9.9-eclipse-temurin-21 AS build

# Set working directory
WORKDIR /app

# Copy build configuration first (better cache usage)
COPY pom.xml ./

# Download dependencies (cached if pom.xml unchanged)
RUN mvn -B dependency:go-offline

# Copy source code
COPY src ./src

# Build the application (adjust if your jar name or module is different)
RUN mvn -B clean package -DskipTests

# ===== 2) Runtime stage =====
FROM eclipse-temurin:21-jre-alpine

# Set a non-root user for better security (optional but recommended)
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
USER appuser

WORKDIR /app

# Copy the fat jar from the build stage
# Adjust the jar filename to match your project (check target/ after build)
COPY --from=build /app/target/*.jar app.jar

# Environment variables (adjust as needed)
ENV JAVA_OPTS=""

# Expose default Spring Boot port (change if different)
EXPOSE 8081

# Entry point
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]