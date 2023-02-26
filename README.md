#Gambling Microservices Backend
This is the backend for a gambling web application that has been split up into microservices.

Table of Contents
Installation
Usage
Architecture
Contributing
License

##Installation

1. Clone the repository:
bash
git clone https://github.com/Bykwarb/gambling-microservices-backend.git

2. Install dependencies:

npm install
Create a .env file in the root directory of the project and fill in the required variables according to the .env.example file.

Start the application:

sql
npm start

##Usage
The application consists of several microservices, each of which has its own API. The microservices and their APIs are:

- Auth microservice - responsible for user authentication and authorization
- User microservice - responsible for user management
- Wallet microservice - responsible for wallet management and transactions
- Game microservice - responsible for game management

Each microservice has its own README.md file, which explains how to use the API of that particular microservice.
To use the entire application, you will need to run all of the microservices. You can do this by following the instructions in each of the microservice's README.md files.

##Architecture
The application is built using a microservices architecture. Each microservice is a separate Node.js application that communicates with the other microservices over a RESTful API.

The microservices communicate with each other asynchronously using RabbitMQ, which is a message broker. When a microservice needs to communicate with another microservice, it sends a message to a queue, which is then picked up by the other microservice.
