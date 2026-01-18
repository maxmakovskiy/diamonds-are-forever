# Create a new user

```bash
curl -i \
  -X POST \
  -H "Content-Type: application/json" \
  -d '{"stockName":"WD-2024-090","purchaseDate":"2026-01-16T10:15:30+01:00","origin":"South Africa","type":"white diamond"}' \
  http://localhost:8080/items
```
