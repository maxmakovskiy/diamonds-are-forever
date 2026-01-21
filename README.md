<div align="center">
  <img src="diamonds.jpg" alt="Best Matching 25-th iteration" width="500">

# Diamonds Are Forever

</div>


---

### Setup guide

#### On local dev machine

1. Clone repo

````bash
git clone https://github.com/maxmakovskiy/diamonds-are-forever.git
````

2. Build it

````bash
./mvnw dependency:go-offline clean compile package
````

3. Build the Docker image

```bash
docker build -t diamonds_are_forever .
```

4. Push to the `ghcr.io` registry

```bash
docker login ghcr.io -u <username>
docker tag diamonds_are_forever ghcr.io/<username>/diamonds_are_forever:latest
docker push ghcr.io/<username>/diamonds_are_forever:latest
```

#### On remote prod machine

5. Clone repo

````bash
git clone https://github.com/maxmakovskiy/diamonds-are-forever.git
````

6. Pull the image with application

```bash
docker compose -f api/docker-compose.yaml pull
```

7. Start reverse proxy

````bash
docker compose -f traefik/docker-compose.yaml up -d
````

8. Start application with database

````bash
docker compose -f api/docker-compose.yaml up -d
````

9. To stop running containers run

````bash
docker compose -f api/docker-compose.yaml down
docker compose -f traefik/docker-compose.yaml down
````

10. (optional) Check the logs

````bash
docker compose -f api/docker-compose.yaml logs postgresql
````

11. (optional) Remove volume created by posgresql to enfore re-populating DB

````bash
docker volume ls
docker volume rm api_db-data
````

---

### Acknowledgements

Photo for logo has been taken from the work of [Edgar Soto](https://unsplash.com/@edgardo1987?utm_source=unsplash&utm_medium=referral&utm_content=creditCopyText) that is pusblished on [Unsplash](https://unsplash.com/photos/two-diamond-studded-silver-rings-gb0BZGae1Nk?utm_source=unsplash&utm_medium=referral&utm_content=creditCopyText).
