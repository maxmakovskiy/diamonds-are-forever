#!/bin/bash
docker compose -f api/dev-compose.yaml down
./mvnw spotless:apply
docker compose -f api/dev-compose.yaml build diamonds
docker compose -f api/dev-compose.yaml up -d postgresql
docker compose -f api/dev-compose.yaml up -d diamonds

