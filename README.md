## E-Bank API

#### This project aims to build a e-bank API using Kafka and SpringBoot.
You can view the API documentation by running the app.And visit localhost:1000/swagger-ui/

### Dependencies:
   #### Java 1.8
   #### SpringBoot 2.7.2
   #### Kafka 3.4.0
   
### Main endpoints include:
1. /auth 
    1. POST /signup 
    2. POST /login
    3. GET /logout
2. /user
    1. POST /creation
    2. PUT /update
    3. GET /{userId}
3. /account
    1. POST /creation
    2. PUT /update
    3. GET /{userId}
    4. GET /{accountId}
    5. GET /{accountId}/transactions
4. /transaction
    1. POST /deposit
    2. POST /withdraw
    3. POST /sendReceive
    4. GET /status 
    
    
#### Each user will have many accounts and each account in one currency.
#### Each account will have many transactions.

