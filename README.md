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
If you'd like to contribute to the project, you can pull the code. There are three main classes that run: the Spring Boot process, which acts as the gateway to receive new requests, the consumer, which pulls job requests from Kafka and a CLI process
  

Visit our website for more information: https://loopitis.com.



