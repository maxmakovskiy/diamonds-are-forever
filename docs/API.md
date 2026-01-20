# Diamonds API

## Description

The diamonds API allows to manage inventory made up of precious stones and jewelries,
as well as its transfers between the counterparties (clients/partners/internal offices). 
It uses the HTTP protocol and the JSON format.

The API is based on the CRUD pattern. 
It has the following operations:

- Sign-in/sign-out as an employee
- Show profile of a current user
- Get current state of the inventory
- Inspect an item by ID
- Inspect item's lifecycle by id
- Update item info
- Delete item
- Create new transfer
- Delete transfer

## Endpoints

### Sign-in as an employee

- `POST /sign-in`

Login to the employee's account.

#### Request

The request body must contain a JSON object with the following properties:

- `email` - the email
- `password` - the corresponding password

#### Response

The response body contains a JSON object with the following properties:

- `id` - The unique identifier of the user.
- `role` - The role of user (Cheif/Admin/Accountant/Sales)

#### Status codes

- `204` (No Content) - The user has been successfully logged in.
- `400` (Bad Request) - The request body is invalid.
- `401` (Unauthorized) - The user does not exist or the password is incorrect.

### Logout

- `POST /sign-out`

Logout from an account.

#### Request

The request body is empty.
Headers should contain cookie with with session id.

#### Response

The response body is empty. The `user` cookie is removed.

#### Status codes

- `204` (No Content) - The user has been successfully logged out.


### Profile

- `GET /profile`

If user signed-in it returns profile's info.
Headers should contain cookie with with session id.

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


### Get current state of the inventory

- `GET /items`

Get all the items registered in the system.

#### Request

The request can contain a query parameter `?isAvailable=True` to show only available items.

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

### Get item's details

- `GET /items/lifecycle/{id}`

Get all the action item has undergone.

#### Request

The request path must contain the ID of the item.

#### Response

The response body contains an with JSON objects where each of them has follwing properties:

- `actionId` - The unique identifier of action
- `fromCounterpartId` - The id of counterpart that sending the item
- `toCounterpart` - The id of counterpart receing the item
- `terms` - 
- `category` - The type (purchase/transfer to office/transfer to lab/return from lab/transfer to factory/return from factory/sale) of the action
- `shipNum` - The number of the shipment
- `shipDate` - The date when item has been shipped/received
- `lotId` - 
- `employeeId` - 
- `price` - 
- `currencyCode` - 
- `createdAt` - 
- `updatedAt` - 

#### Status codes

- `200` (OK) - The item has been successfully retrieved.
- `404` (Not Found) - The item does not exist.

---

### Delete item

- `DELETE /items/{id}`

Headers should contain cookie with valid session id.
The request path must contain the ID of the item.

#### Request

The request contains no body.

#### Response

The response body contains a JSON object with newly created white diamond.

#### Status codes

- `200` - Item with all adjasent actions has been deleted
- `401` (Unauthorized) - The user is not logged in.

---

### Get details of white diamond

- `GET /white-diamond/{id}`

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
- `whiteLevel` - The level of whiteness of the diamond

#### Status codes

- `201` (OK) - The item has been successfully updated.
- `404` (Not Found) - The item does not exist.

---

### Update item's info

- `PUT /white-diamond/{id}`

Headers should contain cookie with valid session id.
The request path must contain the ID of the item.

#### Request

The request path must contain the ID of the item.
The request body must contain a JSON white diamond object with updated fields.

#### Response

The response contains updated white diamond in Json format.

#### Status codes

- `200` (OK) - The item has been successfully updated.
- `400` (Bad Request) - The request body is invalid.
- `401` (Unauthorized) - The user is not logged in.
- `404` (Not Found) - The item does not exist.

---

### Create new white diamond

- `POST /white-diamond`

Headers should contain cookie with valid session id.

#### Request

The request contains the following fields:

- `stockName` - The stock name of the item
- `purchaseDate` - When item has been bought
- `origin` - The name of the counterparty that sold it
- `type` - The type of the item
- `weightCt` - The weight in carats of the diamond
- `shape` - The shape of the diamond
- `length` - The length of the diamond
- `width` - The width of the diamond
- `depth` - The depth of the diamond
- `clarity` - The level of clarity of the diamond
- `whiteLevel` - The level of whiteness of the diamond

#### Response

The response body contains a JSON object with newly created white diamond.

#### Status codes

- `201` - White diamond has been created
- `401` (Unauthorized) - The user is not logged in.

---

### Create a new action

- `POST /actions`

Headers should contain cookie with valid session id.

#### Request

The request body must contain a JSON object with the following properties:

- `fromCounterpartId` - The id of counterpart that sending the item
- `toCounterpart` - The id of counterpart receing the item
- `terms` - 
- `category` - The type (purchase/transfer to office/transfer to lab/return from lab/transfer to factory/return from factory/sale) of the action
- `shipNum` - The number of the shipment
- `shipDate` - The date when item has been shipped/received
- `lotId` - 
- `employeeId` - 
- `price` - 
- `currencyCode` - 

#### Response

The response contains newly create action.

#### Status codes

- `201` (Created) - The transfer has been successfully created.
- `400` (Bad Request) - The request body is invalid.
- `401` (Unauthorized) - The user is not logged in.
- `409` (Conflict) - The transfer violates business rules.

---

### Delete transfer by id

- `DELETE /transfers/{id}`

If transfer of interest is the last one for a certain item it can be removed.
Headers should contain cookie with valid session id.
The request path must contain the ID of the item.

#### Request

The request contains nothing.

#### Response

The response contains nothing.

#### Status codes

- `200` - The transfer has been successfully delete.
- `401` (Unauthorized) - The user is not logged in.

