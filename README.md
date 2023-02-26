# Gambling Microservices Backend
This is the backend for a gambling web application that has been split up into microservices.

## Usage
The application consists of several microservices, each of which has its own API. The microservices and their APIs are:

- Auth microservice - responsible for user authentication and authorization
- User microservice - responsible for user management
- Wallet microservice - responsible for wallet management and transactions
- Game microservice - responsible for game management

## Architecture
The application is built using a microservices architecture. Each microservice is a separate Java application that communicates with the other microservices over a RESTful API and messaging.

The microservices communicate with each other asynchronously using Kafka, which is a message broker. When a microservice needs to communicate with another microservice, it sends a message to a queue, which is then picked up by the other microservice.
