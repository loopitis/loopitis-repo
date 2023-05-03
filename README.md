# **License**

This project is licensed under the Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License. To view a copy of this license, visit https://creativecommons.org/licenses/by-nc-sa/4.0/.

If you would like to use this project for commercial purposes and need a different license, please contact us at support@loopitis.com to discuss your options.

# **Loopitis** - All you need is two files to run it

LoopItIs is an application that helps developers stay on top of important tasks without having to constantly monitor their systems. It uses Docker Compose, Redis, PostgreSQL, and Apache Kafka to provide reliable delivery of messages between different components. Each notification comes with a unique ID that allows the developer to update LoopItIs with the state of the execution at the end of each task. This feature makes it easy for developers to monitor all their tasks and notifications in one place, so they can focus on what matters most.

If you want to try out LoopItIs, you can visit the website https://loopitis.com. To use the app as is without any modification, **all you need is the compose.yaml file and config.properties file**. Copy both files to your working directory (it works on both Linux or Windows). Make sure to have Docker Compose installed on your machine and set applicable passwords on the config file for security purposes. To run the Docker Compose, you should run the following command:


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



