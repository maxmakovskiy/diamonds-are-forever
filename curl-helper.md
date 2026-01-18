### List all items in the inventory


```bash
curl -i \
  -X GET \
  http://localhost:8080/items
```

### Create a new item

```bash
curl -i \
  -X POST \
  --cookie [put you cookie here] \
  -H "Content-Type: application/json" \
  -d '{"stockName":"WD-2024-090","purchaseDate":"2026-01-16T10:15:30+01:00","origin":"South Africa","type":"white diamond"}' \
  http://localhost:8080/items
```

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
  --cookie [put you cookie here] \
  http://localhost:8080/sign-out
```

