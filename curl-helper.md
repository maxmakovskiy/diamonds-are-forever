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
 
