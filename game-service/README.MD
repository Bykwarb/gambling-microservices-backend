# GAME-SERVICE API
This is a RESTful API for the game service.

## Endpoints
- POST /v1/game/slot-machine/play
  * Request body:
  ```json
  {
    "betValue": 5.0,
    "lines": 3,
    "betterUserName":"Test"
  }
   ```
  * Response body:
  ```json
  {
  "result":0.0,
  "status":"Lose",
  "symbols":[
  ["BAR","SEVEN","BAR"],
  ["CHERRY","BAR","CHERRY"],
  ["BAR","SEVEN","LEMON"]],
  "winLines":{"LeftDiagonal":"BAR"}
  }
  ```
