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

#### Response

The response body is empty. The `user` cookie is removed.

#### Status codes

- `204` (No Content) - The user has been successfully logged out.


### Profile

- `GET /profile`

If user signed-in it returns profile's info.

#### Request

The request body is empty.

#### Response

The response body contains a JSON object with the following properties:

- `userId` - The unique identifier of the user.
- `firstName` - The first name of the user.
- `lastName` - The last name of the user.
- `email` - The email address of the user.
- `role` - The role of the user.

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
- `itemType` - The type (white diamond/colored diamond/gemstone/jewelry) of the item
- `stockName` - The stock name of the item
- `purchaseDate` - When item has been bought

#### Status codes

- `200` (OK) - The items have been successfully retrieved


### Get item's details

- `GET /item/{id}`

Get all the details about one specific item by its ID.

#### Request

The request path must contain the ID of the item.

#### Response

The response body contains a JSON object with various properties that depend on the kind of item it represents.

It has common part:

- `itemId` - The unique identifier of the item.
- `stockName` - The stock name of the item
- `purchaseDate` - When item has been bought
- `supplier` - The name of the counterparty that sold it
- `origin` - The country of origin
- `responsibleOffice` - The name of the office that moved (transfered) an item last time
- `isAvailable` - Availability
- `createdAt` - When item has been created
- `updatedAt` - When item's info has been updated

If item represents white diamond then it contains:

- `weightCt` - The weight in carats of the diamond
- `shape` - The shape of the diamond
- `length` - The length of the diamond
- `width` - The width of the diamond
- `depth` - The depth of the diamond
- `clarity` - The level of clarity of the diamond
- `whiteLevel` - The level of whiteness of the diamond

If item represents colored diamond then it contains:

- `weightCt` - The weight in carats of the diamond
- `shape` - The shape of the diamond
- `length` - The length of the diamond
- `width` - The width of the diamond
- `depth` - The depth of the diamond
- `clarity` - The level of clarity of the diamond
- `gemType` - The type of the gemstone
- `fancyIntensity` - The intensity of the color
- `fancyOverton` - The overtone of the diamond
- `fancyColor` - The colore of the diamond

If item represents colored gemstone then it contains:

- `weightCt` - The weight in carats of the diamond
- `shape` - The shape of the diamond
- `length` - The length of the diamond
- `width` - The width of the diamond
- `depth` - The depth of the diamond
- `gemType` - The type of the gemstone
- `gemColor` - The color of the gemstone
- `treatment` - The treatment the gemstone has undergone

#### Status codes

- `200` (OK) - The item has been successfully retrieved.
- `404` (Not Found) - The item does not exist.

### Update white diamond

- `PATCH /white-diamond/{id}`

User must be logged in and has corresponding role to edit item's info.
Item's type cannot be updated, since it would harm all underlying database structure.

#### Request

The request path must contain the ID of the item.
The request body must contain a JSON object with the properties to update.

#### Response

The response contains updated `WhiteDiamond`

#### Status codes

- `201` (OK) - The item has been successfully updated.
- `400` (Bad Request) - The request body is invalid.
- `401` (Unauthorized) - The user is not logged in.
- `404` (Not Found) - The item does not exist.

---

### Update item's info

- `PATCH /items/{id}`

User must be logged in and has corresponding role to edit item's info.
Item's type cannot be updated, since it would harm all underlying database structure.

#### Request

The request path must contain the ID of the item.
The request body must contain a JSON object with the properties to update.

#### Response

The response has no body.

#### Status codes

- `200` (OK) - The item has been successfully updated.
- `400` (Bad Request) - The request body is invalid.
- `401` (Unauthorized) - The user is not logged in.
- `404` (Not Found) - The item does not exist.

---

### Get all the stages in item's lifecycle

- `GET /lifecycle/{id}`

User must be logged in to access this API point.
Get all the stages (purchase/transfers/sale) in item's lifecycle.

#### Request

The request path must contain the ID of the item.

#### Response

The response body contains a JSON object with the following properties:

- `itemId` - The unique identifier of the item
- `itemType` - The type (white diamond/colored diamond/gemstone/jewelry) of the item
- `lifecycle` - The array of transfers

Each transfer has the following properties:

- `transferId` - The unique identifier of the transfer
- `transferType` - The type (purchase/transfer to office/transfer to lab/return from lab/transfer to factory/return from factory/sale) of the transfer
- `fromCounterpart` - The unique identifier of the transfer
- `toCounterpart` - The unique identifier of the transfer
- `shipDate` - The unique identifier of the transfer

#### Status codes

- `200` (OK) - The item's lifecycle has been successfully retrieved.
- `401` (Unauthorized) - The user is not logged in.
- `404` (Not Found) - The item does not exist.


### Create a new transfer

- `POST /transfers`

User must be logged in.
Registers new transfer in the system and ties items to it.

#### Request

The request body must contain a JSON object with the following properties:

- `transferType` - The type (purchase,transfer or sale) of the transfer
- `transferNum` - The number used to identify delivery
- `fromCounterpart` - The name of the counterparty that did the transfer
- `toCounterpart` - The name of the counterparty that received the transfer
- `shipDate` - Date when transfer has been done
- `terms` - The terms of the transfer
- `remarks` - Additional remarks

#### Response

The response contains nothing.

#### Status codes

- `201` (Created) - The transfer has been successfully created.
- `400` (Bad Request) - The request body is invalid.
- `401` (Unauthorized) - The user is not logged in.
- `409` (Conflict) - The transfer violates business rules.


### Delete transfer by id

- `DELETE /transfers/{id}`

User must be logged in.
If transfer of interest is the last one for certain item it can be removed.

#### Request

The request contains nothing.

#### Response

The response contains nothing.

#### Status codes

- `200` (Created) - The transfer has been successfully delete.
- `400` (Bad Request) - The request body is invalid.
- `401` (Unauthorized) - The user is not logged in.

