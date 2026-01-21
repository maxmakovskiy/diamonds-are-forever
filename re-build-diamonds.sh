#!/bin/bash
docker compose -f api/dev-compose.yaml down
./mvnw spotless:apply
./mvnw clean package
docker compose -f api/dev-compose.yaml build diamonds
docker compose -f api/dev-compose.yaml up -d

