# build
FROM eclipse-temurin:21-jdk AS builder
WORKDIR /app
COPY . .
RUN ./mvnw dependency:go-offline clean compile package

# copy and run
FROM eclipse-temurin:21-jre AS runner
WORKDIR /app
COPY --from=builder /app/target/diamonds-are-forever-1.0-SNAPSHOT.jar /app/diamonds-are-forever-1.0-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "diamonds-are-forever-1.0-SNAPSHOT.jar"]

