# **License**

This project is licensed under the Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License. To view a copy of this license, visit https://creativecommons.org/licenses/by-nc-sa/4.0/.

If you would like to use this project for commercial purposes and need a different license, please contact us at support@loopitis.com to discuss your options.

# **How to make it work** - All you need is two files


If you want to use the app as is without any modification, **all you need is the compose.yaml file and config.properties file**. Copy both files to your working directory (it works both on Linux or Windows).

Make sure to have Docker Compose installed on your machine. For security reasons, it is highly recommended to run the Docker Compose on a Virtual Private Cloud (VPC) and set applicable passwords on the config.properties file (Redis password, Portainer password, and PostgreSQL passwords) even if you run it locally under a VPC. To run the Docker Compose, you should run the following command:


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
If you would like to contribute to the Loopitis project, you can pull the code and make changes to the three main classes that run:

Endpoints process: This is a Spring Boot process that acts as the gateway to receive new requests. To make changes to this process, first copy the Dockerfile.endpoints, config.properties, and compose.yaml files to your working directory. The main class for endpoints is LoppitisApplication, and the main endpoints class is LoopitisMainEndpoints. After making changes, run the command "mvn package" to generate the endpoints.jar file, which you can copy to your working directory. Next, modify the compose.yaml file for the endpoints process, specifying the Dockerfile.endpoint in the build section. Finally, run the build and up commands: "docker-compose --env-file config.properties build endpoints" and "docker-compose --env-file config.properties up".

Consumer process: This is the consumer that pulls job requests from Kafka. To make changes to this process, first copy the Dockerfile.consumer, config.properties, and compose.yaml files to your working directory. The main class for the consumer is LoopitisConsumer. After making changes, export a jar from this main class, calling it consumer.jar, and copy it to your working directory. Next, modify the compose.yaml file for the consumer process, specifying the Dockerfile.consumer in the build section. Finally, run the build and up commands: "docker-compose --env-file config.properties build consumer" and "docker-compose --env-file config.properties up".

CLI process: This can run as a standalone process. You can build and run the CLI class, or you can pack the endpoints process, but make sure to have the cli.jar. The endpoints process copies the cli.jar if it exists in your working directory.





For any question you can always contact us at support@loopitis.com
Visit our website for more information: https://loopitis.com

I would love to hear what you think about this app. Share your thoughts with us !



