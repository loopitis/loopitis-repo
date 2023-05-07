
# LOOP IT IS



Spoiler alert, example of usage: 
Say you want to get a report every 1 hour for user 1234 , 

send a POST request to Loopitis with the following json: 
{
  "interval": "1h",
  "delay": "5m"",    
  "occurrences": 1000,
  "name": "my job request",
  "return_url": "https://myApp.com/callme",  
  "payload": "{"request_report_for_user_id":"1234}",
  "callback_type": "POST"
}

Loopitis returns a response similar to this one:
{
    "id": "e67416bd-8bd5-4b3d-abf3-67e19884f8e3",
    "internal_id": 8
}
 

from that moment Loopitis gets your job request it will wait {delay} milliseconds (in the example 1 second) before it starts executing. 
Loopitis will send an HTTP call {callback_type} (POST in this case) to the {return_url} (https://myapp.com/callme int this case) for {occurences} times (1000 in this case) with payload {payload} 

Loopitis is an on-premise software that you can use to schedule and automate recurring tasks. For instance, if you need to receive a report every hour for a specific user, you can send a POST request to Loopitis with the necessary details.

To do this, you would provide a JSON payload that includes the following information:

"interval": the frequency of the recurring task (e.g. "1h" for every hour)
"delay": the time delay in milliseconds before the task starts executing
"occurrences": the number of times the task should repeat
"name": a name for your job request
"return_url": the URL where Loopitis should send the callback
"payload": any additional data that the task requires
"callback_type": the HTTP method that Loopitis should use for the callback (e.g. POST, GET, etc.)
Once Loopitis receives your job request, it will wait for the specified delay before executing the task. It will then send an HTTP call to the return URL for the specified number of occurrences with the payload you provided.

Loopitis will also provide a response that includes an ID for your job request and an internal ID for tracking purposes.

Have you ever had to deal with repetitive tasks that ended up causing more trouble than they were worth? Sometimes, a simple scheduler isn't enough for developers dealing with large amounts of repetitive tasks. If a scheduler goes down, it can be difficult to determine when, why, and how many tasks were completed before it failed. Additionally, it can be challenging to track when a specific task was last completed and to receive notifications when tasks fail. For example, in my last project, I had to generate a report for every user in the system every 10 minutes. With 1000 users, that meant generating an average of 16 reports per second. It was hard to keep track of which reports had failed and why, and I only discovered the issues when users reported them. Loopitis solves this problem by allowing developers to schedule specific methods to be called for each user at set intervals, rather than relying on a centralized scheduler. This way, developers can easily track when a specific task was last completed and receive notifications when tasks fail, making it easier to keep everything running smoothly.

The LoopItIs team is dedicated to providing a professional and reliable service to customers, with a commitment to being responsive, communicative, and attentive to their needs. Technical assistance and support are available whenever needed, ensuring a seamless experience from start to finish.

# **License**

This project is licensed under the Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License. To view a copy of this license, visit https://creativecommons.org/licenses/by-nc-sa/4.0/.

If you would like to use this project for commercial purposes and need a different license, please contact us at support@loopitis.com to discuss your options.

# **How to make it work** - All you need is two files


If you want to use the app as is without any modification, **all you need is the compose.yaml file and config.properties file**. Copy both files from resources/config to your working directory (it works both on Linux or Windows).

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
All code written and built in Java 19 , using openJDK
If you would like to contribute to the Loopitis project, you can pull the code and make changes to the three processes (main files) classes that run:

Endpoints process: This is a Spring Boot process that acts as the gateway to receive new requests. To make changes to this process, first copy the Dockerfile.endpoints, config.properties, and compose.yaml files to your working directory. The main class for endpoints is LoppitisApplication, and the main endpoints class is LoopitisMainEndpoints. After making changes, run the command "mvn package" to generate the endpoints.jar file, which you can copy to your working directory. Next, modify the compose.yaml file for the endpoints process, specifying the Dockerfile.endpoint in the build section. Finally, run the build and up commands: "docker-compose --env-file config.properties build endpoints" and "docker-compose --env-file config.properties up".

Consumer process: This is the consumer that pulls job requests from Kafka. To make changes to this process, first copy the Dockerfile.consumer, config.properties, and compose.yaml files to your working directory. The main class for the consumer is LoopitisConsumer. After making changes, export a jar from this main class, calling it consumer.jar, and copy it to your working directory. Next, modify the compose.yaml file for the consumer process, specifying the Dockerfile.consumer in the build section. Finally, run the build and up commands: "docker-compose --env-file config.properties build consumer" and "docker-compose --env-file config.properties up".

CLI process: This can run as a standalone process. You can build and run the CLI class, or you can pack the endpoints process, but make sure to have the cli.jar. The endpoints process copies the cli.jar if it exists in your working directory.





For any question you can always contact us at support@loopitis.com
Visit our website for more information: https://loopitis.com

I would love to hear what you think about this app. Share your thoughts with us !



