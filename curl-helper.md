### Login

```bash
curl -i \
  -X POST \
  -H "Content-Type: application/json" \
  -d '{"email":"john.smith@example.com"}' \
  http://localhost:8080/sign-in
```

### Logout

```bash
curl -i \
  -X POST \
  --cookie '[put you cookie here]' \
  http://localhost:8080/sign-out
```

---

### List all items in the inventory

```bash
curl -i \
  -X GET \
  http://localhost:8080/items
```

### Get details of a white Diamond

```bash
curl -i \
  -X GET \
  http://localhost:8080/white-diamonds/{id}
```

### Create a new white diamond

```bash
curl -i \
  -X POST \
  --cookie '[put you cookie here]' \
  -H "Content-Type: application/json" \
  -d '{"stockName":"WD-2024-090","purchaseDate":"2026-01-16T10:15:30+01:00","origin":"South Africa","type":"white diamond", "weightCt":"1.52", "shape":"Brilliant Cut", "length":"7.45", "width":"7.42", "depth":"4.58", "whiteScale":"F", "clarity":"VS1"}' \
  http://localhost:8080/white-diamonds
```

### Update an existing white diamond

```bash
curl -i \
  -X PUT \
  --cookie '[put you cookie here]' \
  -H "Content-Type: application/json" \
  -d '{"stockName":"WD-2024-111","purchaseDate":"2026-01-16T10:15:30+01:00","origin":"South Africa","type":"white diamond", "weightCt":"1.52", "shape":"Brilliant Cut", "length":"7.45", "width":"7.42", "depth":"4.58", "whiteScale":"F", "clarity":"VS2"}' \
  http://localhost:8080/white-diamonds/{id}
```

### Get details of a colored diamond

```bash
curl -i \
  -X GET \
  http://localhost:8080/colored-diamonds/{id}
```

### Get details of a colored gemstone

```bash
curl -i \
  -X GET \
  http://localhost:8080/colored-gemstones/{id}
```

---

### Get actions for an item (lifecycle) by id

```bash
curl -i \
  -X GET \
  http://localhost:8080/items/lifecycle/{id}
```


### Update action by id if it is the most recent one

```bash
curl -i \
  -X PUT \
  --cookie '[put your cookie here]' \
  -H 'Content-Type: application/json' \
  -d '{"fromCounterpartId":5,"toCounterpartId":1,"terms":"Payment: 30 days net","category":"purchase","shipNum":"PO-2024-0001","shipDate":"2024-01-15","lotId":1,"employeeId":1,"price":13244.0,"currencyCode":"USD"}' \
  http://localhost:8080/actions/{id}
```
