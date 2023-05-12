# LOOP IT IS - Schedule and Automate Recurring Tasks

Loopitis (https://loopitis.com) is an on-premise software that you can use to schedule and automate recurring tasks such as generating reports or sending notifications. With Loopitis, you can easily schedule tasks to run at specific intervals and track their progress. Additionally, Loopitis can also be used to check that all your processes are functioning well and monitor tasks in real-time. It provides an easy-to-use interface for creating and managing tasks, as well as comprehensive reporting to help you keep track of all your automated processes. With Loopitis, you can save time and reduce the risk of errors by automating repetitive tasks, allowing you to focus on more important work.

## Features
Loopitis provides the following features:

* **Flexible scheduling**: Schedule tasks to run at any interval you need, whether it's every hour, every day, every week, or something else entirely.
* **Advanced tracking:** Track the progress of each task in real-time, including the number of times it has run and whether it has succeeded or failed.
* **Customizable payloads:** Send any additional data required for each task to ensure it runs smoothly.
* **Powerful callback options:** Receive notifications of each task's completion status via HTTP callbacks to your specified endpoint.
* **Easy installation:** Use Docker Compose to quickly set up and run the Loopitis software.


# Installation

Setting up Loopitis is quick and easy using Docker Compose. Follow these steps to get started:

1. Install Docker Compose on your machine.
2. Run the following command in your terminal:
```bash
curl -sSL https://bit.ly/3W0DUPt | bash
```

This command will download and run a script that sets up Loopitis on your machine.

If you prefer to install Loopitis manually, follow these steps:

1. Copy the '**compose.yaml**', '**init.sql**', and '**config.properties**' files from the GitHub repository.
2. Update the credentials in the config.properties file to ensure secure access.
3. Run the following command in your terminal:

```shell
docker-compose --env-file config.properties up
```

This command will start the Loopitis software.

# Usage

To send a POST request, set the content-type to application/json and send the request to localhost:8080/set/notifier.

The POST request body can be:

```shell
curl -X POST \
-H "Content-Type: application/json" \
-H "Authorization: Basic <your_base64_encoded_credentials>" \
-d '{
  "interval": 10000,
  "delay": 1000,
  "occurrences": 15,
  "name": "my job request",
  "return_url": "<your return url>",
  "payload": "<your payload>",
  "callback_type": "POST",
  "notify_status_not_ok": "<return url for error notifications>"

}' \
http://localhost:8080/set/notifier

```

Here's a breakdown of the options used in this command:

* -X POST specifies that we want to make a POST request.
* -H "Content-Type: application/json" sets the Content-Type header to application/json.
* -H "Authorization: Basic <your_base64_encoded_credentials>" sets the Authorization header to a base64-encoded string of your credentials in the format username:password.
* -d '<JSON data>' sets the request body to the JSON data provided. Note that the JSON data should be enclosed in single quotes (') to prevent the shell from interpreting any special characters.
  http://localhost:8080 is the URL we want to make the request to.
* Replace <your_base64_encoded_credentials> with your actual base64-encoded credentials.


To do this, you would provide a JSON payload that includes the following information:

* "interval": the frequency of the recurring task (e.g. "1h" for every hour or 1w+3h+20m for every 1 week and 3 hours
  and 20 minutes)
* "delay": the time delay in milliseconds before the task starts executing
* "occurrences": the number of times the task should repeat
* "name": a name for your job request
* "return_url": the URL where Loopitis should send the callback
* "payload": any additional data that the task requires
* "callback_type": the HTTP method that Loopitis should use for the callback (e.g. POST, GET, etc.)
* "notify_status_not_ok": The url to call (POST call) if status code of a call to <return_url> is not 200 (or time out
  has reached)

Once Loopitis receives your job request, it will wait for the specified delay before executing the task. It will then
send an HTTP call to the return URL for the specified number of occurrences with the payload you provided.

Loopitis will also provide a response that includes an ID for your job request and an internal ID for tracking purposes.

# Why use Loopitis
If you ever had to deal with repetitive tasks that ended up causing more trouble than they were worth. Sometimes, a
simple scheduler isn't enough for developers dealing with large amounts of repetitive tasks. If a scheduler goes down,
it can be difficult to determine when, why, and how many tasks were completed before it failed. Additionally, it can be
challenging to track when a specific task was last completed and to receive notifications when tasks fail. For example,
in our last project, we had to generate a report for every user in the system every 10 minutes. With 1000 users, that
meant generating an average of 16 reports per second. It was hard to keep track of which reports had failed and why, and
we only discovered the issues when users reported them. Loopitis solves this problem by allowing developers to schedule
specific methods to be called for each user at set intervals, rather than relying on a centralized scheduler. This way,
developers can easily track when a specific task was last completed and receive notifications when tasks fail, making it
easier to keep everything running smoothly.

The LoopItIs team is dedicated to providing a professional and reliable service to customers, with a commitment to being
responsive, communicative, and attentive to their needs. Technical assistance and support are available whenever needed,
ensuring a seamless experience from start to finish.

# Example of usage:

Say you want to pull a report every 1.5 hour for user 1234 ,

send a POST request to Loopitis with the following json:

```json
{
  "interval": "1h+30m",
  "delay": "1m",
  "occurrences": 1000,
  "name": "my job request",
  "return_url": "https://myApp.com/callme",
  "payload": "{\"request_report_for_user_id\":1234}",
  "callback_type": "POST",
  "notify_status_not_ok": "https://myApp.com/alert"
}
```

Loopitis returns a response similar to this one:

```json
{
  "id": "e67416bd-8bd5-4b3d-abf3-67e19884f8e3",
  "internal_id": 8
}
```

From that moment Loopitis gets your job request it will wait {delay} milliseconds (in the example 5 minutes) before it
starts executing.
Loopitis will send an HTTP call {callback_type} (POST in this case) to the {return_url} (https://myapp.com/callme in
this case) for {occurences} times (1000 in this case) with payload {payload}
If anything goes wrong , status code was not received on a call or any status code that is not ok (200) Loopitis will
send a POST call to {notify_status_not_ok} . Note that it is better to send an alert to a different server just in case
the server is down.

# Usage Example #2:

Loopitis can be utilized to monitor the behavior of your process. For instance, if you want to test your database
connection every hour, you can set up Loopitis with a scheduler to call a method in your application that performs the
test. If the connection is not responding or faulty, Loopitis will send an alert to the URL specified in
{notify_status_not_ok} (if provided). You can set up the desired alert type to receive under this path, such as email or
SMS. However, it is important to note that the implementation of the alert is entirely the user's responsibility once
they receive it from Loopitis. The user must define and set up the necessary steps to address any issues that may arise
with their application.

# **License**

This project is licensed under the Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License. To
view a copy of this license, visit https://creativecommons.org/licenses/by-nc-sa/4.0/.

In a human-readable summary here is what you can do:

* Share — copy and redistribute the material in any medium or format
* Adapt — remix, transform, and build upon the material

**Under the following terms:**

* Attribution — You must give appropriate credit, provide a link to the license, and indicate if changes were made. You
  may do so in any reasonable manner, but not in any way that suggests the licensor endorses you or your use.
* NonCommercial — You may not use the material for commercial purposes.
* ShareAlike — If you remix, transform, or build upon the material, you must distribute your contributions under the
  same license as the original.

No additional restrictions — You may not apply legal terms or technological measures that legally restrict others from
doing anything the license permits.

If you would like to use this project for commercial purposes and need a different license, please contact us at
support@loopitis.com to discuss your options.

# Scale with Loopitis

Loopitis was designed to scale ! It is built with several processes that runs together.
A producer, Kafka as the queue and a consumer.
You can set as many consumers as you need. You can set in config.properties the max number of threads that each consumer
will use for executing jobs.
To set more than one consumer simply use --scale as in the following example:

```shell
docker-compose --env-file config.properties up --scale consumer=3
```

# **Contributing**

All code written and built in Java 19 , using openJDK
If you would like to contribute to the Loopitis project, you can pull the code and make changes to the three processes (
main files) classes that run:

Endpoints process: This is a Spring Boot process that acts as the gateway to receive new requests. To make changes to
this process, first copy the Dockerfile.endpoints, config.properties, and compose.yaml files to your working directory.
The main class for endpoints is LoppitisApplication, and the main endpoints class is LoopitisMainEndpoints. After making
changes, run the command "mvn package" to generate the endpoints.jar file, which you can copy to your working directory.
Next, modify the compose.yaml file for the endpoints process, specifying the Dockerfile.endpoint in the build section.
Finally, run the build and up commands: "docker-compose --env-file config.properties build endpoints" and "
docker-compose --env-file config.properties up".

Consumer process: This is the consumer that pulls job requests from Kafka. To make changes to this process, first copy
the Dockerfile.consumer, config.properties, and compose.yaml files to your working directory. The main class for the
consumer is LoopitisConsumer. After making changes, export a jar from this main class, calling it consumer.jar, and copy
it to your working directory. Next, modify the compose.yaml file for the consumer process, specifying the
Dockerfile.consumer in the build section. Finally, run the build and up commands: "docker-compose --env-file
config.properties build consumer" and "docker-compose --env-file config.properties up".

CLI process: This can run as a standalone process. You can build and run the CLI class, or you can pack the endpoints
process, but make sure to have the cli.jar. The endpoints process copies the cli.jar if it exists in your working
directory.

For any question you can always contact us at support@loopitis.com
Visit our website for more information: https://loopitis.com

I would love to hear what you think about this app. Share your thoughts with us !



