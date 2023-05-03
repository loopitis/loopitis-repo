# **License**

This project is licensed under the Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License. To view a copy of this license, visit https://creativecommons.org/licenses/by-nc-sa/4.0/.

If you would like to use this project for commercial purposes and need a different license, please contact us at support@loopitis.com to discuss your options.

# **Loopitis**

Loopitis is a sample application that showcases the use of Docker Compose, Redis, PostgreSQL, and Apache Kafka. It is a messaging system that provides reliable delivery of messages between different components.

**Getting Started**

To get started, you need to have Docker Compose installed on your machine. After cloning the repository, follow these steps:

Copy the compose.yaml and config.properties files to your working directory.

Set applicable passwords on the config.properties file (Redis password, Portainer password, and PostgreSQL passwords).

Run the following command to start the Docker Compose:

> docker-compose --env-file config.properties up

If you're using Windows, use docker compose instead of docker-compose.

You can use the -d flag if you want to detach it.

Once the containers are up and running, you can communicate with the endpoint container through port 8080.

To send a POST request, set the content-type to application/json and send the request to localhost:8080/set/notifier.

The POST request body can be:


{
  "interval": 10000,
  "delay": 1000,
  "occurrences": 15,
  "name": "my job request",
  "return_url": "<your return url>",
  "payload": "<your payload>",
  "callback_type": "POST"
}
  
# **Contributing**
If you'd like to contribute to the project, you can pull the code. There are three main classes that run: the Spring Boot process, which acts as the gateway to receive new requests, the consumer, which pulls job requests from Kafka and a CLI process
  

Visit our website for more information: https://loopitis.com.



