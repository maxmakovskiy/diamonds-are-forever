#!/bin/bash
docker compose down
./mvnw spotless:apply
./mvnw clean package
docker compose build diamonds
docker compose up -d

