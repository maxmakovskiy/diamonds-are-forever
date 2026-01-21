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
### API of the app

# API

## Description

The diamonds API allows to manage inventory made up of precious stones and jewelries,
as well as its transfers between the counterparties (clients/partners/internal offices).
It uses the HTTP protocol and the JSON format.

The API is based on the CRUD pattern.
It has the following operations:

- Sign-in/sign-out as an employee
- Show profile of a current user
- Get current state of the inventory
- Filter items by availability or type
- Inspect an item by ID
- Inspect item's lifecycle by id
- Create items
- Update items' info
- Delete items
- Create new actions
- Delete actions

## Endpoints

### 1. Sign-in as an employee

- `POST /sign-in`

Login to the employee's account.

#### Request

The request body must contain a JSON object with the following properties:

- `email` - the email

#### Response

The response body is empty. a session cookie is set in the response headers.

#### Status codes

- `204` (No Content) - The user has been successfully logged in.
- `400` (Bad Request) - The request body is invalid.
- `401` (Unauthorized) - The user does not exist or the password is incorrect.

#### Response headers example:
```bash
Date: Wed, 21 Jan 2026 09:25:32 GMT
Content-Type: text/plain
Set-Cookie: JSESSIONID=node09s6bo6ppxq7b1vqge50l367fn0.node0; Path=/; HttpOnly
Expires: Thu, 01 Jan 1970 00:00:00 GMT
```

### 2. Logout

- `POST /sign-out`

Logout from an account.

#### Request

The request body is empty.
Headers should contain cookie with with session id.

#### Response

The response body is empty. The `user` cookie is removed.

#### Status codes

- `204` (No Content) - The user has been successfully logged out.


### 3. Profile

- `GET /profile`

If user signed-in it returns profile's info.
Headers should contain cookie with session id.

#### Request

The request body is empty.

#### Response

The response body contains a JSON object with the following properties:

- `employeeId` - The unique identifier of the user.
- `counterpartId` - The unique identifier of the user's office.
- `firstName` - The first name of the user.
- `lastName` - The last name of the user.
- `email` - The email address of the user.
- `role` - The role of the user.
- `isActive` - If user's account is considering to be active.

#### Status codes

- `200` (OK) - The user has been successfully retrieved.
- `401` (Unauthorized) - The user is not logged in.


---


### 4. Get current state of the inventory

- `GET /items`

Get all the items registered in the system.

#### Request

The request can contain query parameter:
- `?isAvailable=True` to show only available items.
- `?type={type}` to filter by item type (`white%20diamond`, `colored%20diamond`, `colored%20gemstone`)

#### Response

The response body contains a JSON array with the following properties:

- `itemId` - The unique identifier of the item.
- `stockName` - The stock name of the item
- `purchaseDate` - When item has been bought
- `origin` - provenance of the item
- `type` - The type (white diamond/colored diamond/gemstone/jewelry) of the item
- `createdAt` - The timestamp when item has been added to the system
- `updatedAt` - The timestamp when item has been last time modified

#### Status codes

- `200` (OK) - The items have been successfully retrieved

---

### 5. Get item's details

- `GET /items/lifecycle/{id}`

Get all the action item has undergone.

#### Request

The request path must contain the ID of the item.

#### Response

The response body contains an with JSON objects where each of them has follwing properties:

- `actionId` - The unique identifier of action
- `fromCounterpartId` - The id of counterpart that sending the item
- `toCounterpart` - The id of counterpart receing the item
- `terms` - Terms of the shipment
- `category` - The type (purchase/transfer to office/transfer to lab/return from lab/transfer to factory/return from factory/sale) of the action
- `shipNum` - The number of the shipment
- `shipDate` - The date when item has been shipped/received
- `lotId` - the item id
- `employeeId` - the employee Id who handle the action
- `price` - price of the action
- `currencyCode` -  currency code suce as USD, EUR, etc..
- `createdAt` - when action was created
- `updatedAt` - when action was last updated

#### Status codes

- `200` (OK) - The item has been successfully retrieved.
- `404` (Not Found) - `The item does not exist.`

---

### Delete item

- `DELETE /items/{id}`

Headers should contain cookie with valid session id.
The request path must contain the ID of the item.

#### Request

The request path must contain the item's lot id.
The request body is empty.

#### Response

The response body is empty.

#### Status codes

- `200` (OK) - Item with all adjasent actions has been deleted
- `401` (Unauthorized) - The user is not logged in.
- `404` (Not Found) - The item does not exist.
---

### 6. Get details of white diamond

- `GET /white-diamonds/{id}`

#### Request

The request path must contain the ID of the item.

#### Response

The response contains `WhiteDiamond`:

- `itemId` - The unique identifier of the item.
- `stockName` - The stock name of the item
- `purchaseDate` - When item has been bought
- `origin` - The name of the counterparty that sold it
- `type` - The type of the item
- `createdAt` - When item has been created
- `updatedAt` - When item's info has been updated
- `weightCt` - The weight in carats of the diamond
- `shape` - The shape of the diamond
- `length` - The length of the diamond
- `width` - The width of the diamond
- `depth` - The depth of the diamond
- `clarity` - The level of clarity of the diamond
- `whiteScale` - The level of whiteness of the diamond

#### Status codes

- `200` (OK) - The item has been successfully retrieved.
- `404` (Not Found) - The item does not exist.

---

### 7. Update white diamond's info

- `PUT /white-diamonds/{id}`

Headers should contain cookie with valid session id.

#### Request

The request path must contain the ID of the item.
The request body must contain a JSON white diamond object with updated fields.

- `stockName` - The stock name of the item
- `purchaseDate` - When item has been bought
- `origin` - The name of the counterparty that sold it
- `weightCt` - The weight in carats of the diamond
- `shape` - The shape of the diamond
- `length` - The length of the diamond
- `width` - The width of the diamond
- `depth` - The depth of the diamond
- `clarity` - The level of clarity of the diamond
- `whiteScale` - The level of whiteness of the diamond
-
#### Response

The response contains updated white diamond in Json format.


#### Status codes

- `200` (OK) - The item has been successfully updated.
- `400` (Bad Request) - The request body is invalid.
- `401` (Unauthorized) - The user is not logged in.
- `404` (Not Found) - The item does not exist.

---

### 8. Create new white diamond

- `POST /white-diamonds`

Headers should contain cookie with valid session id.

#### Request

The request contains the following fields:

- `stockName` - The stock name of the item
- `purchaseDate` - When item has been bought
- `origin` - The name of the counterparty that sold it
- `weightCt` - The weight in carats of the diamond
- `shape` - The shape of the diamond
- `length` - The length of the diamond
- `width` - The width of the diamond
- `depth` - The depth of the diamond
- `clarity` - The level of clarity of the diamond
- `whiteScale` - The level of whiteness of the diamond

#### Response

The response body contains a JSON object with newly created white diamond.

#### Status codes

- `201` (Created) - White diamond has been created
- `400` (Bad Request) - The request body is invalid.
- `401` (Unauthorized) - The user is not logged in.

---


### 9. Get details of colored diamond

- `GET /colored-diamonds/{id}`

#### Request

The request path must contain the ID of the item.

#### Response

The response contains `ColoredDiamond` object:

- `lotId` - The unique identifier of the item.
- `stockName` - The stock name of the item
- `purchaseDate` - When item has been bought
- `origin` - The name of the counterparty that sold it
- `type` - The type of the item (`colored diamond`)
- `createdAt` - When item has been created
- `updatedAt` - When item's info has been updated
- `weightCt` - The weight in carats of the diamond
- `shape` - The shape of the diamond
- `length` - The length of the diamond
- `width` - The width of the diamond
- `depth` - The depth of the diamond
- `gemType` - `Diamond`
- `fancyIntensity` - The intensity of color
- `fancyOvertone` - The overtone color
- `fancyColor` - The primary fancy color
- `clarity` - The level of clarity of the diamond


#### Status codes

- `200` (OK) - The item has been successfully retrieved.
- `404` (Not Found) - The item does not exist.

---

### 10. Update colored diamonds info

- `PUT /colored-diamonds/{id}`

Headers should contain cookie with valid session id.

#### Request

The request path must contain the ID of the item.
The request body must contain a JSON colored diamond object with updated fields.
- `lotId` - The unique identifier of the item.
- `stockName` - The stock name of the item
- `purchaseDate` - When item has been bought
- `origin` - The name of the counterparty that sold it
- `type` - The type of the item (`colored diamond`)
- `createdAt` - When item has been created
- `updatedAt` - When item's info has been updated
- `weightCt` - The weight in carats of the diamond
- `shape` - The shape of the diamond
- `length` - The length of the diamond
- `width` - The width of the diamond
- `depth` - The depth of the diamond
- `gemType` - `Diamond`
- `fancyIntensity` - The intensity of color
- `fancyOvertone` - The overtone color
- `fancyColor` - The primary fancy color
- `clarity` - The level of clarity of the diamond

#### Response

The response contains updated fancy diamond in Json format.


#### Status codes

- `200` (OK) - The item has been successfully updated.
- `400` (Bad Request) - The request body is invalid.
- `401` (Unauthorized) - The user is not logged in.
- `404` (Not Found) - The item does not exist.

---

### 11. Create new colored diamond

- `POST /colored-diamonds`

Headers should contain cookie with valid session id.

#### Request

The request contains the following fields:

- `stockName` - The stock name of the item
- `purchaseDate` - When item has been bought
- `origin` - The name of the counterparty that sold it
- `weightCt` - The weight in carats of the diamond
- `shape` - The shape of the diamond
- `length` - The length of the diamond
- `width` - The width of the diamond
- `depth` - The depth of the diamond
- `gemType` - The type of gem (`Diamond`)
- `fancyIntensity` - The intensity of color
- `fancyOvertone` - The overtone color
- `fancyColor` - The primary fancy color
- `clarity` - The level of clarity of the diamond

#### Response

The response body contains a JSON object with newly created colored diamond.

#### Status codes

- `201` (Created) - fancy diamond has been created
- `400` (Bad Request) - The request body is invalid.
- `401` (Unauthorized) - The user is not logged in.

---

### 12. Get details of colored gemstone info

- `GET /colored-gemstones/{id}`

#### Request

The request path must contain the ID of the item.
The request body is empty

#### Response

The response contains `ColoredGemstone` object:
- `stockName` - The stock name of the item
- `purchaseDate` - When item has been bought
- `origin` - The provenance
- `weightCt` - The weight in carats
- `shape` - The shape
- `length` - The length
- `width` - The width
- `depth` - The depth
- `gemType` - The type of gem (Sapphire, Emerald, Ruby, Diamond)
- `gemColor` - The color of the gemstone
- `treatment` - Treatment applied (No heat, heated, No oil, Minor Oil, Oiled)


#### Status codes

- `200` (OK) - The item has been successfully retrieved.
- `404` (Not Found) - The item does not exist.

---

### 13. Update colored gemstone info

- `PUT /colored-gemstones/{id}`

Headers should contain cookie with valid session id.

#### Request

The request path must contain the ID of the item.
The request body must contain a JSON colored gemstone  object with updated fields.
- `stockName` - The stock name of the item
- `purchaseDate` - When item has been bought
- `origin` - The provenance
- `weightCt` - The weight in carats
- `shape` - The shape
- `length` - The length
- `width` - The width
- `depth` - The depth
- `gemType` - The type of gem (Sapphire, Emerald, Ruby, Diamond)
- `gemColor` - The color of the gemstone
- `treatment` - Treatment applied (No heat, heated, No oil, Minor Oil, Oiled)

#### Response

The response contains updated colored gemstone  in Json format.


#### Status codes

- `200` (OK) - The item has been successfully updated.
- `400` (Bad Request) - The request body is invalid.
- `401` (Unauthorized) - The user is not logged in.
- `404` (Not Found) - The item does not exist.

---

### 14. Create new colored gemstone

- `POST /colored-gemstones`

Headers should contain cookie with valid session id.

#### Request

The request contains the following fields:

- `stockName` - The stock name of the item
- `purchaseDate` - When item has been bought
- `origin` - The provenance
- `weightCt` - The weight in carats
- `shape` - The shape
- `length` - The length
- `width` - The width
- `depth` - The depth
- `gemType` - The type of gem (Sapphire, Emerald, Ruby, Diamond)
- `gemColor` - The color of the gemstone
- `treatment` - Treatment applied (No heat, heated, No oil, Minor Oil, Oiled)

#### Response

The response body contains a JSON object with newly created colored gemstone.

#### Status codes

- `201` (Created) - colored gemstone has been created
- `400` (Bad Request) - The request body is invalid.
- `401` (Unauthorized) - The user is not logged in.


---
### 15. Create a new action

- `POST /actions`

Headers should contain cookie with valid session id.

#### Request

The request body must contain a JSON object with the following properties:

- `fromCounterpartId` - The id of counterpart that sending the item
- `toCounterpartId` - The id of counterpart receing the item
- `terms` - Terms of the shipment
- `category` - The type (purchase/transfer to office/transfer to lab/return from lab/transfer to factory/return from factory/sale) of the action
- `shipNum` - The number of the shipment
- `shipDate` - The date when item has been shipped/received
- `lotId` - The item ID
- `employeeId` - the employee handling the action
- `price` - the price of the action
- `currencyCode` - Currency code (USD, CHF, EUR, NTD, and etc)

#### Response

The response contains newly create action.

#### Status codes

- `201` (Created) - The action has been successfully created.
- `400` (Bad Request) - The request body is invalid.
- `401` (Unauthorized) - The user is not logged in.
- `409` (Conflict) - The transfer violates business rules.


---

### 16. Delete action by id

- `DELETE /actions/{id}`

Only the most recent action for the item can be removed.
Headers should contain cookie with valid session id.


#### Request

The request path must contain the id of the action
The request body is empty

#### Response

The response body is empty

#### Status codes

- `200` (OK) - The action has been successfully deleted.
- `401` (Unauthorized) - The user is not logged in.
- `404` (Not Found) - The action does not exist.
- `409` (Conflict) - The aciton violates business rules.


---

### Test `API` with `curl`

### 1. Login

#### CURL template
```bash
curl -i \
  -X POST \
  -H "Content-Type: application/json" \
  -c cookie.txt
  -d '{"email":"john.smith@example.com"}' \
  http://localhost:8080/sign-in
```

#### Input example
```bash
curl -i -X POST \
  -H "Content-Type: application/json" \
  -c cookies.txt \
  -d '{
    "email": "john.smith@example.com"
  }' \
  http://localhost:8080/sign-in
```

#### Output example
```bash
HTTP/1.1 204 No Content
Set-Cookie: JSESSIONID=node09s6bo6ppxq7b1vqge50l367fn0.node0; Path=/; HttpOnly

```

### 2. Logout

#### CURL template
```bash
curl -i \
  -X POST \
  --cookie '[put you cookie here]' \
  http://localhost:8080/sign-out
```

#### Input example
```bash
curl -i -X POST \
  -b cookies.txt \
  http://localhost:8080/sign-out
```

#### Output example
```bash
HTTP/1.1 204 No Content
Date: Wed, 21 Jan 2026 20:39:04 GMT
Content-Type: text/plain
```


---

### 3. Users Profile

#### CURL template
```bash
curl -i -X GET \
  -b cookies.txt \
  http://localhost:8080/profile
```

#### Output example
```bash
HTTP/1.1 200 OK
Date: Wed, 21 Jan 2026 20:57:58 GMT
Content-Type: application/json
Content-Length: 136

{"employeeId":1,"counterpartId":1,"firstName":"John","lastName":"Smith","email":"john.smith@example.com","role":"Chief","isActive":true}%  
```


--- 

### 4. Get current state of the inventory

#### CURL template
```bash
curl -i \
  -X GET \
  http://localhost:8080/items
```

#### Output example
```bash

HTTP/1.1 200 OK
Date: Wed, 21 Jan 2026 20:44:38 GMT
Content-Type: application/json
Content-Length: 4117

[{"lotId":11,"stockName":"RB-2024-001","purchaseDate":"2024-01-18T09:30:00Z","origin":"Thailand","type":"colored gemstone","createdAt":"2026-01-21T08:57:40.621326Z","updatedAt":"2026-01-21T08:57:40.621326Z"},{"lotId":2,"stockName":"WD-2024-002","purchaseDate":"2024-01-20T14:30:00Z","origin":"Botswana","type":"white diamond","createdAt":"2026-01-21T08:57:40.621326Z","updatedAt":"2026-01-21T08:57:40.621326Z"},{"lotId":14,"stockName":"SP-2024-001","purchaseDate":"2024-01-22T10:15:00Z","origin":"Kashmir","type":"colored gemstone","createdAt":"2026-01-21T08:57:40.621326Z","updatedAt":"2026-01-21T08:57:40.621326Z"},{"lotId":6,"stockName":"CD-2024-001","purchaseDate":"2024-01-25T11:00:00Z","origin":"South Africa","type":"colored diamond","createdAt":"2026-01-21T08:57:40.621326Z","updatedAt":"2026-01-21T08:57:40.621326Z"},{"lotId":17,"stockName":"EM-2024-001","purchaseDate":"2024-02-01T11:20:00Z","origin":"Colombia","type":"colored gemstone","createdAt":"2026-01-21T08:57:40.621326Z","updatedAt":"2026-01-21T08:57:40.621326Z"},{"lotId":3,"stockName":"WD-2024-003","purchaseDate":"2024-02-05T09:15:00Z","origin":"India","type":"white diamond","createdAt":"2026-01-21T08:57:40.621326Z","updatedAt":"2026-01-21T08:57:40.621326Z"},{"lotId":12,"stockName":"RB-2024-002","purchaseDate":"2024-02-08T14:45:00Z","origin":"Myanmar","type":"colored gemstone","createdAt":"2026-01-21T08:57:40.621326Z","updatedAt":"2026-01-21T08:57:40.621326Z"},{"lotId":4,"stockName":"WD-2024-004","purchaseDate":"2024-02-10T11:45:00Z","origin":"Russia","type":"white diamond","createdAt":"2026-01-21T08:57:40.621326Z","updatedAt":"2026-01-21T08:57:40.621326Z"},{"lotId":7,"stockName":"CD-2024-002","purchaseDate":"2024-02-12T15:30:00Z","origin":"Australia","type":"colored diamond","createdAt":"2026-01-21T08:57:40.621326Z","updatedAt":"2026-01-21T08:57:40.621326Z"},{"lotId":15,"stockName":"SP-2024-002","purchaseDate":"2024-02-14T15:30:00Z","origin":"Sri Lanka","type":"colored gemstone","createdAt":"2026-01-21T08:57:40.621326Z","updatedAt":"2026-01-21T08:57:40.621326Z"},{"lotId":5,"stockName":"WD-2024-005","purchaseDate":"2024-02-15T13:20:00Z","origin":"Botswana","type":"white diamond","createdAt":"2026-01-21T08:57:40.621326Z","updatedAt":"2026-01-21T08:57:40.621326Z"},{"lotId":16,"stockName":"SP-2024-003","purchaseDate":"2024-03-05T09:45:00Z","origin":"Kashmir","type":"colored gemstone","createdAt":"2026-01-21T08:57:40.621326Z","updatedAt":"2026-01-21T08:57:40.621326Z"},{"lotId":8,"stockName":"CD-2024-003","purchaseDate":"2024-03-08T10:45:00Z","origin":"India","type":"colored diamond","createdAt":"2026-01-21T08:57:40.621326Z","updatedAt":"2026-01-21T08:57:40.621326Z"},{"lotId":13,"stockName":"RB-2024-003","purchaseDate":"2024-03-12T10:20:00Z","origin":"Thailand","type":"colored gemstone","createdAt":"2026-01-21T08:57:40.621326Z","updatedAt":"2026-01-21T08:57:40.621326Z"},{"lotId":18,"stockName":"EM-2024-002","purchaseDate":"2024-03-15T14:40:00Z","origin":"Colombia","type":"colored gemstone","createdAt":"2026-01-21T08:57:40.621326Z","updatedAt":"2026-01-21T08:57:40.621326Z"},{"lotId":9,"stockName":"CD-2024-004","purchaseDate":"2024-03-22T14:00:00Z","origin":"South Africa","type":"colored diamond","createdAt":"2026-01-21T08:57:40.621326Z","updatedAt":"2026-01-21T08:57:40.621326Z"},{"lotId":19,"stockName":"EM-2024-003","purchaseDate":"2024-04-08T09:55:00Z","origin":"Zambia","type":"colored gemstone","createdAt":"2026-01-21T08:57:40.621326Z","updatedAt":"2026-01-21T08:57:40.621326Z"},{"lotId":20,"stockName":"WD-TEST-001","purchaseDate":"2026-01-21T10:00:00Z","origin":"South Africa","type":"white diamond","createdAt":"2026-01-21T13:11:15.683492Z","updatedAt":"2026-01-21T13:11:15.683492Z"},{"lotId":21,"stockName":"WD-API-TEST-001","purchaseDate":"2026-01-21T10:00:00Z","origin":"South Africa","type":"white diamond","createdAt":"2026-01-21T13:14:40.29647Z","updatedAt":"2026-01-21T13:14:40.29647Z"},{"lotId":1,"stockName":"WD-UPDATED","purchaseDate":"2026-01-21T10:00:00Z","origin":"South Africa","type":"white diamond","createdAt":"2026-01-21T08:57:40.621326Z","updatedAt":"2026-01-21T13:27:31.437095Z"}]%     
```

---
### 5. Get item's lifecycle

#### CURL template
```bash
curl -i \
  -X GET \
  http://localhost:8080/items/lifecycle/{id}
```

#### Output example
```bash
HTTP/1.1 200 OK
Date: Wed, 21 Jan 2026 12:32:24 GMT
Content-Type: application/json
Content-Length: 306

[{"actionId":1,"fromCounterpartId":5,"toCounterpartId":1,"terms":"Payment: 30 days net","category":"purchase","shipNum":"PO-2024-0001","shipDate":"2024-01-15","lotId":1,"employeeId":1,"price":13244.0,"currencyCode":"USD","createdAt":"2026-01-21T08:57:40.621326Z","updatedAt":"2026-01-21T08:57:40.621326Z"}]%        

```

---

### 6. Get details of a specific white diamond

#### CURL template
```bash
curl -i \
  -X GET \
  http://localhost:8080/white-diamonds/{id}
```

#### Input example
```bash
curl -i -X GET \
  http://localhost:8080/white-diamonds/1
```

#### Output example
```bash
HTTP/1.1 200 OK
Date: Wed, 21 Jan 2026 20:48:21 GMT
Content-Type: application/json
Content-Length: 315

{"lotId":1,"stockName":"WD-UPDATED","purchaseDate":"2026-01-21T10:00:00Z","origin":"South Africa","type":"white diamond","createdAt":"2026-01-21T08:57:40.621326Z","updatedAt":"2026-01-21T13:27:31.437095Z","weightCt":2.5,"shape":"Brilliant Cut","length":8.5,"width":8.3,"depth":5.2,"whiteScale":"E","clarity":"VVS2"}%   
```

---
### 7. Update white diamond's info

#### CURL template
```bash
curl -i -X PUT \
  -b cookies.txt \
  -H "Content-Type: application/json" \
  -d '{
    "stockName":{stockName},
    "purchaseDate":{purchaseDate},
    "origin":{origin},
    "weightCt":{weightCt},
    "shape":"{shape},
    "length":{length},
    "width":{width},
    "depth":{depth},
    "whiteScale":{whiteScale},
    "clarity":{clarity}
  }' \
  http://localhost:8080/white-diamonds/{id}

```

#### Input example
```bash
curl -i -X PUT \
  -b cookies.txt \
  -H "Content-Type: application/json" \
  -d '{
    "stockName":"WD-UPDATED",
    "purchaseDate":"2026-01-21T10:00:00Z",
    "origin":"South Africa",
    "weightCt":2.50,
    "shape":"Brilliant Cut",
    "length":8.5,
    "width":8.3,
    "depth":5.2,
    "whiteScale":"E",
    "clarity":"VVS2"
  }' \
  http://localhost:8080/white-diamonds/1

```

#### Output example
```bash 
HTTP/1.1 200 OK
Date: Wed, 21 Jan 2026 20:51:05 GMT
Content-Type: application/json
Content-Length: 315

{"lotId":1,"stockName":"WD-UPDATED","purchaseDate":"2026-01-21T10:00:00Z","origin":"South Africa","type":"white diamond","createdAt":"2026-01-21T08:57:40.621326Z","updatedAt":"2026-01-21T20:51:05.604464Z","weightCt":2.5,"shape":"Brilliant Cut","length":8.5,"width":8.3,"depth":5.2,"whiteScale":"E","clarity":"VVS2"}%    
```

### 8. Create new white diamond

#### CURL template
```bash
curl -i -X POST \
  -b cookies.txt \
  -H "Content-Type: application/json" \
  -d '{
    "stockName": "{stockName}",
    "purchaseDate": "{purchaseDate}",
    "origin": "{origin}",
    "weightCt": {weightCt},
    "shape": "{shape}",
    "length": {length},
    "width": {width},
    "depth": {depth},
    "whiteScale": "{whiteScale}",
    "clarity": "{clarity}"
  }' \
  http://localhost:8080/white-diamonds

```

#### input example
```bash
curl -i -X POST \
  -b cookies.txt \
  -H "Content-Type: application/json" \
  -d '{
    "stockName":"WD-API-TEST-001",
    "purchaseDate":"2026-01-21T10:00:00Z",
    "origin":"South Africa",
    "weightCt":2.15,
    "shape":"Brilliant Cut",
    "length":8.2,
    "width":8.2,
    "depth":5.0,
    "whiteScale":"E",
    "clarity":"VVS1"
  }' \
  http://localhost:8080/white-diamonds


```

#### Output example
```bash 
HTTP/1.1 201 Created
Date: Wed, 21 Jan 2026 21:02:16 GMT
Content-Type: application/json
Content-Length: 322

{"lotId":22,"stockName":"WD-API-TEST-001","purchaseDate":"2026-01-21T10:00:00Z","origin":"South Africa","type":"white diamond","createdAt":"2026-01-21T21:02:16.375596Z","updatedAt":"2026-01-21T21:02:16.375596Z","weightCt":2.15,"shape":"Brilliant Cut","length":8.2,"width":8.2,"depth":5.0,"whiteScale":"E","clarity":"VVS1"}%  
```

---

### 9. Get details of colored diamond

#### CURL template
```bash
curl -i -X GET \
  http://localhost:8080/colored-diamonds/{id}
```

#### input example
```bash
curl -i -X GET \
  http://localhost:8080/colored-diamonds/6

```

#### Output example
```bash 
HTTP/1.1 200 OK
Date: Wed, 21 Jan 2026 21:06:07 GMT
Content-Type: application/json
Content-Length: 394

{"lotId":6,"stockName":"CD-2024-001","purchaseDate":"2024-01-25T11:00:00Z","origin":"South Africa","type":"colored diamond","createdAt":"2026-01-21T08:57:40.621326Z","updatedAt":"2026-01-21T08:57:40.621326Z","weightCt":1.85,"shape":"Brilliant Cut","length":7.92,"width":7.89,"depth":4.88,"gemType":"Diamond","fancyIntensity":"Fancy","fancyOvertone":"None","fancyColor":"Yellow","clarity":"VS1"}%    

```

---

### 10. Update colored diamond info

#### CURL template
```bash
curl -i -X PUT \
  -b cookies.txt \
  -H "Content-Type: application/json" \
  -d '{
    "stockName": "{stockName}",
    "purchaseDate": "{purchaseDate}",
    "origin": "{origin}",
    "weightCt": {weightCt},
    "shape": "{shape}",
    "length": {length},
    "width": {width},
    "depth": {depth},
    "gemType": "Diamond",
    "fancyIntensity": "{fancyIntensity}",
    "fancyOvertone": "{fancyOvertone}",
    "fancyColor": "{fancyColor}",
    "clarity": "{clarity}"
  }' \
  http://localhost:8080/colored-diamonds/{id}

```

#### input example
```bash
curl -i -X PUT \
  -b cookies.txt \
  -H "Content-Type: application/json" \
  -d '{
    "stockName":"CD-2024-001-UPDATED",
    "purchaseDate":"2024-01-25T11:00:00Z",
    "origin":"South Africa",
    "weightCt":1.85,
    "shape":"Brilliant Cut",
    "length":7.92,
    "width":7.89,
    "depth":4.88,
    "gemType":"Diamond",
    "fancyIntensity":"Fancy",
    "fancyOvertone":"None",
    "fancyColor":"Yellow",
    "clarity":"VS2"
  }' \
  http://localhost:8080/colored-diamonds/6

```

#### Output example
```bash 
curl -i -X PUT \ 
  -b cookies.txt \                     
  -H "Content-Type: application/json" \
  -d '{                                    
    "stockName":"WD-UPDATED",  
    "purchaseDate":"2026-01-21T10:00:00Z",
    "origin":"South Africa",
    "weightCt":2.50,
    "shape":"Brilliant Cut",
    "length":8.5,
    "width":8.3,
    "depth":5.2,
    "whiteScale":"E",
    "clarity":"VVS2"
  }' \
  http://localhost:8080/white-diamonds/1
HTTP/1.1 200 OK
Date: Wed, 21 Jan 2026 21:10:46 GMT
Content-Type: application/json
Content-Length: 315

{"lotId":1,"stockName":"WD-UPDATED","purchaseDate":"2026-01-21T10:00:00Z","origin":"South Africa","type":"white diamond","createdAt":"2026-01-21T08:57:40.621326Z","updatedAt":"2026-01-21T21:10:46.295369Z","weightCt":2.5,"shape":"Brilliant Cut","length":8.5,"width":8.3,"depth":5.2,"whiteScale":"E","clarity":"VVS2"}%     


```

---

### 11. Create new colored diamond

#### CURL template
```bash
curl -i -X POST \
  -b cookies.txt \
  -H "Content-Type: application/json" \
  -d '{
    "stockName": "{stockName}",
    "purchaseDate": "{purchaseDate}",
    "origin": "{origin}",
    "weightCt": {weightCt},
    "shape": "{shape}",
    "length": {length},
    "width": {width},
    "depth": {depth},
    "gemType": "Diamond",
    "fancyIntensity": "{fancyIntensity}",
    "fancyOvertone": "{fancyOvertone}",
    "fancyColor": "{fancyColor}",
    "clarity": "{clarity}"
  }' \
  http://localhost:8080/colored-diamonds

```

#### input example
```bash

curl -i -X POST \
  -b cookies.txt \
  -H "Content-Type: application/json" \
  -d '{
    "stockName":"PO-API-TEST-005",
    "purchaseDate":"2026-01-21T10:00:00Z",
    "origin":"South Africa",
    "weightCt":1.85,
    "shape":"Brilliant Cut",
    "length":7.92,
    "width":7.89,
    "depth":4.88,
    "gemType":"Diamond",
    "fancyIntensity":"Fancy",
    "fancyOvertone":"None",
    "fancyColor":"Yellow",
    "clarity":"VS1"
  }' \
  http://localhost:8080/colored-diamonds



```

#### Output example
```bash 

HTTP/1.1 201 Created
Date: Wed, 21 Jan 2026 21:13:01 GMT
Content-Type: application/json
Content-Length: 399

{"lotId":23,"stockName":"PO-API-TEST-005","purchaseDate":"2026-01-21T10:00:00Z","origin":"South Africa","type":"colored diamond","createdAt":"2026-01-21T21:13:01.461076Z","updatedAt":"2026-01-21T21:13:01.461076Z","weightCt":1.85,"shape":"Brilliant Cut","length":7.92,"width":7.89,"depth":4.88,"gemType":"Diamond","fancyIntensity":"Fancy","fancyOvertone":"None","fancyColor":"Yellow","clarity":"VS1"}%  

```

---

### 12. Get details of colored gemstone info

#### CURL template
```bash
curl -i -X GET \
  http://localhost:8080/colored-gemstones/{id}

```

#### input example
```bash
curl -i -X GET \
  http://localhost:8080/colored-gemstones/11
  
```

#### Output example
```bash 
HTTP/1.1 200 OK
Date: Wed, 21 Jan 2026 21:14:18 GMT
Content-Type: application/json
Content-Length: 332

{"lotId":11,"stockName":"RB-2024-001","purchaseDate":"2024-01-18T09:30:00Z","origin":"Thailand","type":"colored gemstone","createdAt":"2026-01-21T08:57:40.621326Z","updatedAt":"2026-01-21T08:57:40.621326Z","weightCt":2.45,"shape":"Oval","length":8.85,"width":6.75,"depth":4.52,"gemType":"Ruby","gemColor":"Red","treatment":"heated"}%   

```

---

### 13. Update colored gemstone info

#### CURL template
```bash
curl -i -X PUT \
  -b cookies.txt \
  -H "Content-Type: application/json" \
  -d '{
    "stockName": "{stockName}",
    "purchaseDate": "{purchaseDate}",
    "origin": "{origin}",
    "weightCt": {weightCt},
    "shape": "{shape}",
    "length": {length},
    "width": {width},
    "depth": {depth},
    "gemType": "{gemType}",
    "gemColor": "{gemColor}",
    "treatment": "{treatment}"
  }' \
  http://localhost:8080/colored-gemstones/{id}

```

#### Input example
```bash
curl -i -X PUT \
  -b cookies.txt \
  -H "Content-Type: application/json" \
  -d '{
    "stockName":"SP-2024-001-UPDATED",
    "purchaseDate":"2024-01-22T10:15:00Z",
    "origin":"Kashmir",
    "weightCt":3.20,
    "shape":"Oval",
    "length":9.4,
    "width":7.8,
    "depth":5.1,
    "gemType":"Sapphire",
    "gemColor":"Blue",
    "treatment":"No heat"
  }' \
  http://localhost:8080/colored-gemstones/14


```

#### Output example
```bash 
HTTP/1.1 200 OK
Date: Wed, 21 Jan 2026 21:15:58 GMT
Content-Type: application/json
Content-Length: 341

{"lotId":14,"stockName":"SP-2024-001-UPDATED","purchaseDate":"2024-01-22T10:15:00Z","origin":"Kashmir","type":"colored gemstone","createdAt":"2026-01-21T08:57:40.621326Z","updatedAt":"2026-01-21T21:15:58.600217Z","weightCt":3.2,"shape":"Oval","length":9.4,"width":7.8,"depth":5.1,"gemType":"Sapphire","gemColor":"Blue","treatment":"No heat"}%   
```

---

### 14. Create new colored gemstone

#### CURL template
```bash
curl -i -X POST \
  -b cookies.txt \
  -H "Content-Type: application/json" \
  -d '{
    "stockName": "{stockName}",
    "purchaseDate": "{purchaseDate}",
    "origin": "{origin}",
    "weightCt": {weightCt},
    "shape": "{shape}",
    "length": {length},
    "width": {width},
    "depth": {depth},
    "gemType": "{gemType}",
    "gemColor": "{gemColor}",
    "treatment": "{treatment}"
  }' \
  http://localhost:8080/colored-gemstones


```

#### Input example
```bash
curl -i -X POST \
  -b cookies.txt \
  -H "Content-Type: application/json" \
  -d '{
    "stockName":"SP-API-TEST-001",
    "purchaseDate":"2026-01-21T10:30:00Z",
    "origin":"Sri Lanka",
    "weightCt":3.45,
    "shape":"Oval",
    "length":9.6,
    "width":7.8,
    "depth":5.1,
    "gemType":"Sapphire",
    "gemColor":"Blue",
    "treatment":"No heat"
  }' \
  http://localhost:8080/colored-gemstones


```

#### Output example
```bash 
HTTP/1.1 201 Created
Date: Wed, 21 Jan 2026 21:17:15 GMT
Content-Type: application/json
Content-Length: 340

{"lotId":24,"stockName":"SP-API-TEST-001","purchaseDate":"2026-01-21T10:30:00Z","origin":"Sri Lanka","type":"colored gemstone","createdAt":"2026-01-21T21:17:15.707821Z","updatedAt":"2026-01-21T21:17:15.707821Z","weightCt":3.45,"shape":"Oval","length":9.6,"width":7.8,"depth":5.1,"gemType":"Sapphire","gemColor":"Blue","treatment":"No heat"}%  
```

---

### 15. Create a new action

#### CURL template
```bash
curl -i -X POST \
  -b cookies.txt \
  -H "Content-Type: application/json" \
  -d '{
    "fromCounterpartId": {fromCounterpartId},
    "toCounterpartId": {toCounterpartId},
    "terms": "{terms}",
    "category": "{category}",
    "shipNum": "{shipNum}",
    "shipDate": "{shipDate}",
    "lotId": {lotId},
    "employeeId": {employeeId},
    "price": {price},
    "currencyCode": "{currencyCode}"
  }' \
  http://localhost:8080/actions


```

#### input example
```bash
curl -i -X POST \
  -b cookies.txt \
  -H "Content-Type: application/json" \
  -d '{
    "fromCounterpartId": 1,
    "toCounterpartId": 17,
    "terms": "Transfer to lab for certification",
    "category": "transfer to lab",
    "shipNum": "LAB-2026-0001",
    "shipDate": "2026-01-21",
    "lotId": 6,
    "employeeId": 1,
    "price": 15000.00,
    "currencyCode": "USD"
  }' \
  http://localhost:8080/actions


```

#### Output example
```bash 
HTTP/1.1 200 OK
Date: Wed, 21 Jan 2026 21:18:22 GMT
Content-Type: application/json
Content-Length: 327

{"actionId":21,"fromCounterpartId":1,"toCounterpartId":17,"terms":"Transfer to lab for certification","category":"transfer to lab","shipNum":"LAB-2026-0001","shipDate":"2026-01-21","lotId":6,"employeeId":1,"price":15000.0,"currencyCode":"USD","createdAt":"2026-01-21T21:18:22.502702Z","updatedAt":"2026-01-21T21:18:22.502702Z"}%      
```

---

### 16. Delete action by id

#### CURL template
```bash
curl -i -X DELETE \
  -b cookies.txt \
  http://localhost:8080/actions/{id}
```

#### Input example
```bash
curl -i -X DELETE \
  -b cookies.txt \
  http://localhost:8080/actions/21

```

### Output example
```bash 
 curl -i -X DELETE \
  -b cookies.txt \
  http://localhost:8080/actions/21
HTTP/1.1 200 OK
Date: Wed, 21 Jan 2026 21:19:42 GMT
Content-Type: text/plain
Content-Length: 0
```




---

### Caching strategy


---

### Use cases

- Inventory viewing: 
    
    Visitors can browse the inventory and inspect item details and lifecycle history.

- Inventory management: 

    Authorized employees can create, update, and delete diamonds and gemstones.

- Inventory Lifecycle tracebility:

    Record and review item movements such as purchases, transfers, and sales.


---

### Further improvements

- Validation + business rules hardening: 
    Enforce stricter input validation (enum values, numeric ranges, date formats) and clearer conflict responses for invalid lifecycle transitions.

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

