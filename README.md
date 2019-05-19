## Money Transfer API
RESTful API (including data model and the backing implementation) for money transfers between accounts.

## Requirements
* Java 8
* Maven

## Technologies
* Apache Spark for REST API
* Embedded Jetty server
* In-memory H2 db
* Slf4j logging
* Gson
* JUnit
* REST Assured

## What's implemented
* User handling: selection, creation, update, delete
* Account handling: selection, creation, update, delete
* Money Transfer handling: selection, creation (transfer itself), update, delete
* In-memory database
* Standalone api
* Logging of errors
* Slight validation on input fields
* Unit-tests (covers repository)
* Integration tests (REST Assured)

## How to run
From project folder:
```
    mvn clean package
```
which will build project, execute all tests (total 90) and create _money-transfer-api-1.0-jar-with-dependencies.jar_ in /target directory.

Then go to /target directory (or use already prepared jar in root directory of this repo) and execute:
```
    java -jar money-transfer-api-1.0-jar-with-dependencies.jar
```
which will run embedded Jetty server on port 4567 of http://localhost

## How to use
### User Service
#### 1. Find all users
```
GET http://localhost:4567/money-transfer/users
```
Response:
```
HTTP 200 OK
{
"data":[
    {
        "id": 1,
        "userName": "Julia Rudenko",
        "email": "juli@gmail.com",
        "createdAt": "May 1, 2019 5:00:00 PM"
    },
    {
        "id": 2,
        "userName": "John Snow",
        "email": "john@gmail.com",
        "createdAt": "Apr 1, 2019 10:00:00 AM"
        }
    ]
}
```
#### 2. Find user by id
```
GET http://localhost:4567/money-transfer/users/1
```
Response:
```
HTTP 200 OK
{
"data":
    {
        "id": 1,
        "userName": "Julia Rudenko",
        "email": "juli@gmail.com",
        "createdAt": "May 1, 2019 5:00:00 PM"
    }
}
```
#### 3. Create user
```
POST http://localhost:4567/money-transfer/users
{
  "userName":"Jaime Lannister",
  "email":"jaime@gmail.com"
}
```
Response:
```
HTTP 200 OK
{
    "data":{
        "id": 3,
        "userName": "Jaime Lannister",
        "email": "jaime@gmail.com",
        "createdAt": "May 18, 2019 11:49:27 PM"
    }
}
```
#### 4. Update user
```
PUT http://localhost:4567/money-transfer/users
{
  "id":"2",
  "userName":"Jaime Lannister",
  "email":"jaime@gmail.com"
}
```
Response:
```
HTTP 200 OK
{
    "data":{
        "id": 2,
        "userName": "Jaime Lannister",
        "email": "jaime@gmail.com",
        "createdAt": "Apr 1, 2019 10:00:00 AM"
    }
}
```
#### 5. Delete user
```
DELETE http://localhost:4567/money-transfer/users/1
```
Response:
```
HTTP 200 OK
```

### Account Service
#### 1. Find all accounts
```
GET http://localhost:4567/money-transfer/accounts
```
Response:
```
HTTP 200 OK
{
"data":[
    {
        "id": 1,
        "userId": 1,
        "balance": 100,
        "currency": "UAH",
        "createdAt": "May 17, 2019 5:00:00 PM"
    },
    {
        "id": 2,
        "userId": 2,
        "balance": 200,
        "currency": "UAH",
        "createdAt": "May 7, 2019 7:00:00 AM"
    }
]
}
```
#### 2. Find account by id
```
GET http://localhost:4567/money-transfer/accounts/1
```
Response:
```
HTTP 200 OK
{
"data":
    {
        "id": 1,
        "userId": 1,
        "balance": 100,
        "currency": "UAH",
        "createdAt": "May 17, 2019 5:00:00 PM"
    }
}
```
#### 3. Create account
```
POST http://localhost:4567/money-transfer/accounts
{
  "userId": 1,
  "balance": 10,
  "currency": "EUR"
}
```
Response:
```
HTTP 200 OK
{
    "data":{
        "id": 3,
        "userId": 1,
        "balance": 10,
        "currency": "EUR",
        "createdAt": "May 19, 2019 4:25:17 PM"
    }
}
```
#### 4. Update account
```
PUT http://localhost:4567/money-transfer/accounts
{
  "id": "1",
  "balance": 1000,
  "currency": "EUR"
}
```
Response:
```
HTTP 200 OK
{
    "data":{
        "id": 1,
        "userId": 1,
        "balance": 1000,
        "currency": "EUR",
        "createdAt": "May 17, 2019 5:00:00 PM"
    }
}
```
#### 5. Delete account
```
DELETE http://localhost:4567/money-transfer/accounts/1
```
Response:
```
HTTP 200 OK
```

### Transfer Service
#### 1. Find all transfers
```
GET http://localhost:4567/money-transfer/transfers
```
Response:
```
HTTP 200 OK
{
"data":[
    {
    "id": 1,
    "sourceAccountId": 1,
    "destinationAccountId": 2,
    "sum": 100,
    "currency": "UAH",
    "createdAt": "May 17, 2019 5:00:00 PM"
    }
]
}
```
#### 2. Find transfer by id
```
GET http://localhost:4567/money-transfer/transfers/1
```
Response:
```
HTTP 200 OK
{
"data":{
    "id": 1,
    "sourceAccountId": 1,
    "destinationAccountId": 2,
    "sum": 100,
    "currency": "UAH",
    "createdAt": "May 17, 2019 5:00:00 PM"
}
}
```
#### 3. Create transfer (transfer "sum" from source account to destination account)
```
POST http://localhost:4567/money-transfer/transfers
{
  "sourceAccountId": 1,
  "destinationAccountId": 2,
  "sum": 50,
  "currency": "UAH"
}
```
Response:
```
HTTP 200 OK
{
"data":{
    "id": 2,
    "sourceAccountId": 1,
    "destinationAccountId": 2,
    "sum": 50,
    "currency": "UAH",
    "createdAt": "May 20, 2019 1:04:49 AM"
}
}
```
#### 4. Update transfer
```
PUT http://localhost:4567/money-transfer/transfers
{
  "id":"1",
  "sum":"66",
  "currency":"GBP"
}
```
Response:
```
HTTP 200 OK
{
"data":{
    "id": 1,
    "sourceAccountId": 1,
    "destinationAccountId": 2,
    "sum": 66,
    "currency": "GBP",
    "createdAt": "May 17, 2019 5:00:00 PM"
}
}
```
#### 5. Delete transfer
```
DELETE http://localhost:4567/money-transfer/transfers/1
```
Response:
```
HTTP 200 OK
```


Endpoints also can return next HTTP response codes:
* 204 No Content - if user tries to find non-existant resource
* 400 Bad request - if user tries to pass invalid data
* 404 Not Found - if requested resource cannot be found
* 500 Internal Server Error - if something went wront with API

## What can be improved
* Add authentication & authorization
* Add more detailed validation of input fields (email, currency etc.)
* Add currency converting, if transfer between accounts with different currencies
* Add high-load testing
* Add pagination for findAll endpoints
* Add interfaces for services & repositories
* Add builders for model classes
* Add unit-tests on service layer (though it mostly copies repository methods)
