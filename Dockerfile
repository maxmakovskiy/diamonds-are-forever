# build stage
FROM eclipse-temurin:21-jdk AS builder
WORKDIR /app

COPY pom.xml .
COPY mvnw .
COPY ./.mvn ./.mvn
RUN ./mvnw dependency:go-offline clean package -Dmaven.main.skip -Dmaven.test.skip && rm -r target

COPY src ./src
RUN ./mvnw clean compile package

# run stage
FROM eclipse-temurin:21-jre AS runner
WORKDIR /app
COPY --from=builder /app/target/diamonds-are-forever-1.0-SNAPSHOT.jar /app/diamonds-are-forever-1.0-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "diamonds-are-forever-1.0-SNAPSHOT.jar"]

