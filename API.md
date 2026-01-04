# Diamonds API

## Desciption

The diamonds API allows to manage inventory made up of precious stones and jewelries,
as well as its transfers between the countparties (clients/partners/internal offices). 
It uses the HTTP protocol and the JSON format.

The API is based on the CRUD pattern. 
It has the following operations:

- Sign-in/sign-out as an employee
- Show profile of a current user
- Manage employees' accounts
- Get current state of the inventory
- Inspect an item by ID and the stages it has undergone
- Update item info
- Inspect all the transfers commited in the system (puchase,transfer to lab, return from lab, transfer to factory, return from factory, transfer to office return from office, sale)
- Create a new transfer
- Inspect existing counterparties and add a new one
- Inspect all the certificates 


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


### Get all users

- `GET /users`

User must be logged-in and have admin-level privileges.
Gets all the users registered in the system.

#### Request

Request contains no body.

#### Response

The response body contains a JSON array with a following properties:

- `userId` - The unique id of the user
- `firstName` - The first name of the user
- `lastName` - The last name of the user
- `isActive` - Indication if current user is active

#### Status codes

- `200` (OK) - The users has been successfully retrieved.
- `401` (Unauthorized) - The current user is not logged in.


### Update the user's profile

- `PATCH /users/{id}`

User must be logged-in and have admin-level privileges.
Invidual cannot update its profile by himself.

#### Request

The request path must contain the ID of the item.
The request body must contain a JSON object with the properties to update.

#### Response

The reseponse has no body.

#### Status codes

- `200` (OK) - The user has been successfully updated.
- `400` (Bad Request) - The request body is invalid.
- `401` (Unauthorized) - The user is not logged in.
- `404` (Not Found) - The user does not exist.


### Get all white diamonds, colored diamonds, colored gemstones and jewelries 

- `GET /items`

User must be signed-in.
Get all the items registered in the system.

#### Request

The request can contain the following query parameters:

- `isAvailable` - boolean flag that indicates if we are intereseted only in available white diamonds (avaible = not sold, not sent to lab or factory)

#### Response

The response body contains a JSON array with the following properties:

- `itemId` - The unique identifier of the item.
- `itemType` - The type (white diamond/colored diamond/gemstone/jewelry) of the item
- `stockName` - The stock name of the item
- `purchaseDate` - When item has been bought
- `supplier` - The name of the counterparty that sold it
- `responsibleOffice` - The name of the office that moved (transfered) an item last time
- `isAvailable` - Availability

#### Status codes

- `200` (OK) - The items have been successfully retrieved
- `401` (Unauthorized) - The user is not logged in.


### Get item's details

- `GET /item/{id}`

User must be logged-in.
Get all the details about one specific item by its ID.

#### Request

The request path must contain the ID of the item.

#### Response

The response body contains a JSON object with various properties that depend on the kind of item it represents.

It has commun part:

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
- `gemColor` - The colore of the gemstone
- `treatment` - The treatment the gemstone has undergone

If item represents jewelry then it contains:

- `jewelryType` - The type of the jewelry (Ring/Necklace/Earrings/etc.)
- `grossWeightGr` - The weight in grams of the jewelry
- `metalType` - The type of the metal
- `metalWeightGr` - The weight of the metal in grams
- `totalCenterStoneQty` - The quantity of the center stones
- `totalCenterStoneWeightCt` - The weight in carats of the center stones
- `centerStoneType` - The type of the center stones
- `totalSideStoneQty` - The quantity of the side stones
- `totalSideStoneWeightCt` - The weight in carats of the side stones
- `sideStoneType` - The type of the side stones

#### Status codes

- `200` (OK) - The item has been successfully retrieved.
- `401` (Unauthorized) - The user is not logged in.
- `404` (Not Found) - The item does not exist.


### Get all the stages in item's lifecycle

- `PUT /lifecycle/{id}`

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


### Get all the transfers

- `GET /transfers`

User must be logged-in.
Retreive all the transfers registered in the system.

#### Request

The request is empty.

#### Response

The response body contains a JSON array with a following properties:

- `transferId` - The unique identifier of the transfer
- `transferType` - The type (purchase/transfer to office/transfer to lab/return from lab/transfer to factory/return from factory/sale) of the transfer
- `transferNum` - The number used to indentify delivery
- `fromCounterpart` - The unique identifier of the transfer
- `toCounterpart` - The unique identifier of the transfer
- `shipDate` - The unique identifier of the transfer
- `items` - Array of the ids of items included in a transfer

#### Status codes

- `200` (OK) - The transfers has been successfully retrieved.
- `401` (Unauthorized) - The user is not logged in.


### Get a specific transfer

- `GET /transfers/{id}`

Inspect specific transfer in the details.

#### Request

The request path must contain the ID of the transfer.

#### Response

The response body can be different depending on transfer type user is trying to reach, but has common part over all types of transfers:

- `transferId` - The unique identifier of the transfer
- `transferType` - The type (purchase/transfer to office/transfer to lab/return from lab/transfer to factory/return from factory/sale) of the transfer
- `transferNum` - The number used to indentify delivery
- `fromCounterpart` - The name of the counterparty that did the transfer
- `toCounterpart` - The name of the counterparty that received the transfer
- `shipDate` - Date when transfer has been done
- `expectedReturnDate` - If it is return-style transfer then this represents date when item should arrived back
- `originalTransferId` - If it is return-style transfer then this represents id of corresponding transfer that has been used to send an item in the first place
- `items` - Array of the ids of items included in a transfer
- `creatorId` - Id of the user that created this transfer
- `terms` - The terms of the transfer
- `remarks` - Additional remarks
- `createdAt` - Date when transfer has been registered in the system
- `updatedAt` - Date when transfer has been updated last time

#### Status codes

- `200` (OK) - The transfer has been successfully retrieved.
- `401` (Unauthorized) - The user is not logged in.
- `404` (Not Found) - The transfer does not exist.


### Create a new transfer

- `POST /transfers`

User must be logged in.
Registers new transfer in the system and ties items to it.

#### Request

The request body must contain a JSON object with the following properties:

- `transferType` - The type (purchase/transfer to office/transfer to lab/return from lab/transfer to factory/return from factory/sale) of the transfer
- `transferNum` - The number used to indentify delivery
- `fromCounterpart` - The name of the counterparty that did the transfer
- `toCounterpart` - The name of the counterparty that received the transfer
- `shipDate` - Date when transfer has been done
- `expectedReturnDate` - If it is return-style transfer then this represents date when item should arrived back
- `originalTransferId` - If it is return-style transfer then this represents id of corresponding transfer that has been used to send an item in the first place
- `terms` - The terms of the transfer
- `remarks` - Additional remarks

Depending of the transfer type it might contain other properties.

1. If transfer is a purchase then user actually need to provide all the information about item of concern as well as its certificate if we are talking about precious stones:

- `stockName` - The stock name of the item
- `purchaseDate` - When item has been bought
- `supplier` - The name of the counterparty that sold it
- `origin` - The country of origin
- `responsibleOffice` - The name of the office that moved (transfered) an item last time

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
- `gemColor` - The colore of the gemstone
- `treatment` - The treatment the gemstone has undergone

If item represents jewelry then it contains:

- `jewelryType` - The type of the jewelry (Ring/Necklace/Earrings/etc.)
- `grossWeightGr` - The weight in grams of the jewelry
- `metalType` - The type of the metal
- `metalWeightGr` - The weight of the metal in grams
- `totalCenterStoneQty` - The quantity of the center stones
- `totalCenterStoneWeightCt` - The weight in carats of the center stones
- `centerStoneType` - The type of the center stones
- `totalSideStoneQty` - The quantity of the side stones
- `totalSideStoneWeightCt` - The weight in carats of the side stones
- `sideStoneType` - The type of the side stones

For white diamond/colored diamond/colored gemstone, user ought to provide certificate:

- `certificateNum` - The number of the the certificate
- `certificateLabName` - The lab's name
- `certificateIssueDate` - Date when it was issued
- `certificateShape` - Shape of the stone
- `certificateWeightCt` - Weight in carats of the stone
- `certificateLength` - Length of the stone
- `certificateWidth` - Width of the stone
- `certificateClarity` - Clarity of the diamond
- `certificateColor` - Color of the diamond
- `certificateTreatment` - Treatment of the gemstone
- `certificateGemType` - The type of gemstone


2. If transfer is return from lab:

- `oldCertificateNum` - The number of the old certificate
- `newCertificateNum` - The number of the new certificate issued by the lab
- `newCertificateLabName` - The lab name
- `newCertificateIssueDate` - Date when it was issued

Following properties could not be all included since they depends on the item's type:

- `newCertificateShape` - Shape of the stone
- `newCertificateWeightCt` - Weight in carats of the stone
- `newCertificateLength` - Length of the stone
- `newCertificateWidth` - Width of the stone
- `newCertificateClarity` - Clarity of the diamond
- `newCertificateColor` - Color of the diamond
- `newCertificateTreatment` - Treatment of the gemstone
- `newCertificateGemType` - The type of gemstone

3. If transfer is return from factory (appliable to the stones):

- `beforeWeightCt` - Weight in carats before the factory's treatment
- `beforeShape` - Shape before the factory's treatment
- `beforeLength` - Length before the factory's treatment
- `beforeWidth` - Width before the factory's treatment
- `beforeDepth` - Depth before the factory's treatment
- `afterWeightCt`-  Weight in carats after the factory's treatment
- `afterShape` - Shape after the factory's treatment
- `afterLength` - Length after the factory's treatment
- `afterWidth` - Width after the factory's treatment
- `afterDepth` - Depth after the factory's treatment

4. If transfer is sale:

- `paymentMethod` - Method of payment
- `paymentStatus` - Status of payment

#### Response

The response contains no body.

#### Status codes

- `201` (Created) - The transfer has been successfully created.
- `400` (Bad Request) - The request body is invalid.
- `401` (Unauthorized) - The user is not logged in.
- `409` (Conflict) - The transfer violates business rules.


### Get all existing counterparties

- `GET /counterparties`

User must be logged-in.
Retrieves all the registered counterparties.

#### Request

The request contains no body.

#### Response

The response body contains a JSON array with the following properties:

- `counterpartId` - The unique id of the counterpart in the system
- `counterpartName` - The counterpart's name
- `city` - The city where its HQ located
- `postalCode` - The postal code of the city
- `country` - The counter where its HQ located
- `isActive` - If it is active in the system or it has been archived due to various reasons
- `accountTypes` - is array of different roles that counterpart could play: client, supplier, manufacturer, office

#### Status codes

- `200` (OK) - The counterparties list has been successfully retrieved.
- `401` (Unauthorized) - The user is not logged in.


### Add a new counterpart

- `POST /counterparties`

User must be logged-in and have admin-level privileges.
Creates a new counterpart.

#### Request

The request body must containt a JSON object with the following properties:

- `counterpartName` - The counterpart's name
- `phoneNumber` - The contact phone number
- `city` - The city where its HQ located
- `postalCode` - The postal code of the city
- `country` - The counter where its HQ located
- `email` - The contact email
- `isActive` - If it is active in the system or it has been archived due to various reasons
- `accountTypes` - is array of different roles that counterpart could play: client, supplier, manufacturer, office

##### Response

The response contains no body.

#### Status codes

- `201` (Created) - The counterparty has been successfully created.
- `400` (Bad Request) - The request body is invalid.
- `401` (Unauthorized) - The user is not logged in.
- `409` (Conflict) - The counterparty already exists.


### Get counterpart's details

- `GET /counterparties/{id}`

User must be logged-in.
Retreives all the info about certain counterpart.

#### Request

The request path must contain the ID of the counterpart.

#### Response

Response body contains the following fields:

- `counterpartId` - The unique id of the counterpart in the system
- `counterpartName` - The counterpart's name
- `phoneNumber` - The contact phone number
- `city` - The city where its HQ located
- `postalCode` - The postal code of the city
- `country` - The counter where its HQ located
- `email` - The contact email
- `isActive` - If it is active in the system or it has been archived due to various reasons
- `accountTypes` - is array of different roles that counterpart could play: client, supplier, manufacturer, office

#### Status codes

- `200` (OK) - The counterparty has been successfully retrieved.
- `401` (Unauthorized) - The user is not logged in.


### Get all the certificates

- `GET /certificates`

User must be logged-in.
Retreives all the certificates currently registered.

#### Request

Request contains no body.

#### Response

The response body contains a JSON array with the following properties:

- `certificateNum` - The certificate's number
- `certificateLab` - The name of lab
- `certificateIssueDate` - Date when certificate has been issued
- `stockName` - Item's stock name that is being certified by certificate

#### Status codes

- `200` (OK) - The certificates have been successfully retrieved
- `401` (Unauthorized) - The user is not logged in.


### Get certificate's details

- `GET /certificates/{id}`

User must be logged-in.
Gets all the details about specific certificate

#### Request

The request path must contain the ID of the certificate.

#### Response

The response body contains a JSON object with the following properties:

- `certificateId` - The ID of the the certificate
- `stockName` - Item's stock name that is being certified by certificate
- `certificateNum` - The number of the the certificate
- `certificateLabName` - The lab's name
- `certificateIssueDate` - Date when it was issued
- `certificateShape` - Shape of the stone
- `certificateWeightCt` - Weight in carats of the stone
- `certificateLength` - Length of the stone
- `certificateWidth` - Width of the stone
- `certificateClarity` - Clarity of the diamond
- `certificateColor` - Color of the diamond
- `certificateTreatment` - Treatment of the gemstone
- `certificateGemType` - The type of gemstone

#### Status codes

- `200` (OK) - The certificate has been successfully retrieved.
- `401` (Unauthorized) - The user is not logged in.
- `404` (Not Found) - The certificate does not exist.

