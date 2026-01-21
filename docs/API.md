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

### 5. Get item's lifecycle

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

### 6. Get details of a specific white diamond

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

