# Wallet Service 
This is a a Wallet Service API, which handles the creation and manipulation of wallet data. The service is written in Java and contains the following functionalities:

- Create a wallet for a user
- Change a username for a wallet
- Retrieve a wallet by its ID
- Retrieve a wallet by its associated username
- Deposit funds into a wallet by its ID or associated username
- Withdraw funds from a wallet by its ID or associated username

## Endpoints

- POST /v1/wallet/create
   * Creates a wallet for a user.
   * Request body: WalletDto
   * Response body: 
   ```json
   {
   "message":"Wallet successfully created"
   }
   ```
   
- PUT /v1/wallet/data
   * Changes the username for a wallet.
   * Request body: UserChangeDto
   * Response body:
   ```json
   {
   "message":"Username successfully changed"
   }
   ```
   
- GET /v1/wallet/get/wallet-id/{walletId}
   * Retrieves a wallet by its ID.
   * Path variable: walletId (Long)
   * Response body: 
    ```json
    {
    "walletId": 123, 
    "userName": "JohnDoe", 
    "value": 100.0
    }
    ```
   
- GET /v1/wallet/get/user-name
   * Retrieves a wallet by its associated username.
   * Response body: 
    ```json
    {
    "walletId": 123, 
    "userName": "JohnDoe", 
    "value": 100.0
    }
    ```

- PUT /v1/wallet/deposit/wallet-id/{walletId}
   * Deposits funds into a wallet by its ID.
   * Path variable: walletId (Long)
   * Request parameter: value (Double)
   * Response body: 
    ```json
    {
    "message": "Wallet successful replenished.",
    "wallet": {
        "walletId": 1234,
        "userName": "johndoe",
        "value": 500.0}
    }
    ```
   
- PUT /v1/wallet/deposit/user-name/{userName}
   * Deposits funds into a wallet by its associated username.
   * Path variable: userName (String)
   * Request parameter: value (Double)
   * Response body: 
   ```json
    {
    "message": "Wallet successful replenished.",
    "wallet": {
        "walletId": 1234,
        "userName": "johndoe",
        "value": 500.0}
    }
    ```
   
- PUT /v1/wallet/debited/wallet-id/{walletId}
   * Withdraws funds from a wallet by its ID.
   * Path variable: walletId (Long)
   * Request parameter: debited-value (Double)
   * Response body: 
   ```json
    {
    "message": "Value successful debited from wallet.",
    "wallet": {
        "walletId": 1234,
        "userName": "johndoe",
        "value": 500.0}
    }
   ```
   
- PUT /v1/wallet/debited/user-name/{userName}
   * Withdraws funds from a wallet by its associated username.
   * Path variable: userName (String)
   * Request parameter: debited-value (Double)
   * Response body: 
   ```json
    {
    "message": "Value successful debited from wallet.",
    "wallet": {
        "walletId": 1234,
        "userName": "johndoe",
        "value": 500.0}
    }
   ```
