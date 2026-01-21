<div align="center">
  <img src="diamonds.jpg" alt="image of diamonds" width="500">

# Diamonds Are Forever

</div>

---

## Description

This project represents a web application that manages inventory of
white diamonds, colored diamonds and colored gemstones.

You can:

- inspect current state of the inventory as a visitor
- add/modify/delete item as a authorized user
- create/delete action (transfer) that certain item undergoes

---

## Table of Contents

1. [Project description](#description)
2. [`API` description](#api-description)
2. [Developer's guide](#developers-guide)
3. [Deployment guide](#deployment-guide)
4. [Test `API` with `curl`](#test-api-with-curl)
5. [Caching strategy](#caching-strategy)
6. [Use cases](#use-cases)
7. [Further improvements](#further-improvements)
8. [Repository structure](#repository-structure)
9. [Dependencies](#dependencies)
11. [Authors](#authors)
12. [Acknowledgements](#acknowledgements)

---

### API description


---

### Developer's guide

1. Clone repo

````bash
git clone https://github.com/maxmakovskiy/diamonds-are-forever.git
````

2. Build it

````bash
./mvnw dependency:go-offline clean compile package
````

3. Run docker compose configuration for the development

````bash
docker compose -f api/dev-compose.yaml
````

It is going to:

- build docker image with the application locally (if it's been running for the first time)
- pull database image and start its container  

4. Everytime you are doing the changes you need to basically rebuild image with application.

5. (optional) To avoid annoying steps above you can run `re-build-diamonds.sh`. 
It will basically do all the steps above for you.

````bash
$ cat re-build-diamonds.sh
#!/bin/bash
docker compose -f api/dev-compose.yaml down
./mvnw spotless:apply
./mvnw clean package
docker compose -f api/dev-compose.yaml build diamonds
docker compose -f api/dev-compose.yaml up -d
````

---

### Deployment guide

Let's suppose that you have added a new set of features
to this application, they have been merged and
know you want to deploy it.

Basically there few stages that you need to go through:

- obtain virtual machine
- setup it up properly (SSH + Docker Compose)
- obtain a domain name
- validate that your remote machine is reachable with DNS
- build (locally) the app and publish it to `ghcr.io`
- clone the repo to the remote machine
- run reverse proxy (`Traefit`)
- run the app

1. Obtain virtual machine from any cloud provider.

For this project we have been using the one from Azure Cloud
because of their offer to student (free `$100` in credits).

Minimal requirements for the VM:

- VM architecture: x64
- Exposed ports: 80 for HTTP, 443 for HTTPS, 22 for SSH
- vRAM:
- vCPU:

2. To connect to the remote machine we recommend to use SSH
since this is industry standard.

On your local machine:
- (if you do not have one) generate public-private pair of keys using `ssh-keygen` (with `ed25519` algorithm):

````bash
ssh-keygen -t ed25519 -C "your_email@example.com"
````

- start `ssh-agent` that is going to manage your keys

````bash
eval "$(ssh-agent -s)"
````

- add newly created private (so-called identity) key to the `ssh-agent`:

````bash
ssh-add ./.ssh/[name of your private key]
````

For more details guide please refer to [GitHub Docs](https://docs.github.com/en/authentication/connecting-to-github-with-ssh/generating-a-new-ssh-key-and-adding-it-to-the-ssh-agent).

- connect to you the remote machine with SSH:

````bash
ssh -i ~/.ssh/[name of your private key] [username of on the VM]@[public id of the VM]
````
For the first time connection you ought to confirm that the key SSH server is adding right now to `./ssh/known_hosts`
is indeed your public key, and you can achieve it by looking (comparing) the hash (fingerprint) 
public key from the VM with hash of the public key stored locally in `.ssh`.

3. Install Docker and Docker Compose

Basically, you just need to follow [official guide](https://docs.docker.com/engine/install/).<br>
Once it is done, if you want to manage docker as non-root user follow [post-installation steps](https://docs.docker.com/engine/install/linux-postinstall/).


3. Obtain a domain name

There are many domain name provider.
For this project with we are using [Dynu](https://www.dynu.com).

We need to add resource record of type `A` that will provide mapping between obtained domain name and public IP address of the VM. 
Since we are using reverse proxy `Traefik` and we would like to access its dashbord,
we need to add also wildcard resource record of type `A` that will provide
mapping between from any subdomain to public IP of the VM.

4. Validate DNS resolution

For this project we have got following DNS records:

| Hostname                | Type | IP             |
|-------------------------|------|----------------|
| *.diamonds.ddnsfree.com | A    | 158.158.120.68 |
| *.diamonds.ddnsfree.com | AAAA | 158.158.120.68 |
| diamonds.ddnsfree.com   | A    | 158.158.120.68 |
| diamonds.ddnsfree.com   | AAAA | 158.158.120.68 |

By default, in Dynu record type for `A/AAAA`.

Now we can check it with `nslookup` tool using next command:

````bash
nslookup diamonds.ddnsfree.com
````

If everything is fine we should get:

````bash
$ nslookup diamonds.ddnsfree.com
Server:         10.255.255.254
Address:        10.255.255.254#53

Non-authoritative answer:
Name:   diamonds.ddnsfree.com
Address: 158.158.120.68
````

5. Locally (assume repository has been already cloned), build the application: 

````bash
./mvnw dependency:go-offline clean compile package
````

6. Build the Docker image

```bash
docker build -t diamonds_are_forever .
```

7. Push to the `ghcr.io` registry

To accomplish it we need to:

- login into remote docker registry - Github Container registry (in our case)
- tag properly earlier built image
- push it to the remote registry

```bash
docker login ghcr.io -u <username>
docker tag diamonds_are_forever ghcr.io/<username>/diamonds_are_forever:latest
docker push ghcr.io/<username>/diamonds_are_forever:latest
```

8. On remote machine, clone the repository

````bash
git clone https://github.com/maxmakovskiy/diamonds-are-forever.git
````

9. Pull the image with application from remote registry

```bash
docker compose -f api/compose.yaml pull
```

10. Start reverse proxy - `Traefik`

````bash
docker compose -f traefik/compose.yaml up -d
````

11. Start application with database

````bash
docker compose -f api/compose.yaml up -d
````

12. To stop running containers run

````bash
docker compose -f api/compose.yaml down
docker compose -f traefik/compose.yaml down
````

10. (optional) Check the logs

````bash
docker compose -f api/compose.yaml logs postgresql
````

11. (optional) Remove volume created by posgresql to enfore re-populating DB

````bash
docker volume ls
docker volume rm api_db-data
````

---

### Test `API` with `curl`


---

### Caching strategy


---

### Use cases


---

### Further improvements


---

### Repository structure


---

### Dependencies

- [Jdbi](https://jdbi.org/)
- [Slf4j](https://www.slf4j.org/)
- [PostgreSQL JDBC Driver](https://jdbc.postgresql.org/)
- [Jakson](https://github.com/FasterXML/jackson-databind)

---

### Authors


---

### Acknowledgements

Since we were trying to integrate Posgres, we have searched
through a some resources:
- [How to get started with user auth and session management](https://github.com/javalin/javalin/issues/541)
- [Kotlin Admin Template](https://github.com/tipsy/kotlin-admin-template/tree/main)
- [Continued questions about session management](https://github.com/javalin/javalin/issues/554)
- [Jdbi 3 Developer Guide](https://jdbi.org/)

Photo for logo has been taken from the work of [Edgar Soto](https://unsplash.com/@edgardo1987?utm_source=unsplash&utm_medium=referral&utm_content=creditCopyText) that is pusblished on [Unsplash](https://unsplash.com/photos/two-diamond-studded-silver-rings-gb0BZGae1Nk?utm_source=unsplash&utm_medium=referral&utm_content=creditCopyText).

