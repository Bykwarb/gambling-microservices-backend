# User Service API
This API provides endpoints to manage user entities.

## Endpoints
- POST /v1/user-service/create
  * Request body:
  ```json
  {
    "username": "exampleUser",
    "email": "user@example.com",
    "password": "password123"
  }
   ```
  * Response body:
  ```json
  {
    "message": "User successfully created"
  }
  ```
  * Response status code:
    - **200 OK on success**
    - **409 Conflict if user already exists**

- DELETE /v1/user-service/delete/id/{userId}
  * Path variable: userId (Long)
  * Response body:
  ```json
  {
    "message": "User successfully deleted"
  }
  ```
  * Response status code:
    - **200 OK on success**
    - **404 Not Found if user does not exist**
   
- GET /v1/user-service/get/id/{userId}
  * Path variable: userId (Long)
  * Response body:
  ```json
  {
    "id": 1,
    "username": "exampleUser",
  }
  ```
  * Response status code:
    - **200 OK on success**
    - **404 Not Found if user does not exist**

- GET /v1/user-service/get/username/{username}
  * Path variable: username (String)
  * Response body:
  ```json
  {
    "id": 1,
    "username": "exampleUser",
  }
  ```
  * Response status code:
    - **200 OK on success**
    - **404 Not Found if user does not exist**
