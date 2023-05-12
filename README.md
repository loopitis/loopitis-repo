# LOOP IT IS - Schedule and Automate Recurring Tasks

Loopitis (https://loopitis.com) is an on-premise cross-platform software that you can use to schedule and automate recurring tasks such as generating reports or sending notifications.
With Loopitis, you can easily schedule tasks to run at specific intervals and track their progress.
Additionally, Loopitis can also be used to check that all your processes are functioning well and monitor tasks in real-time. It provides an easy-to-use interface for creating and managing tasks, as well as comprehensive reporting to help you keep track of all your automated processes. With Loopitis, you can save time and reduce the risk of errors by automating repetitive tasks, allowing you to focus on more important work.

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

## Request a job

To request a job, send a POST request to the /set/notifier endpoint. The request can include the following parameters in the request body:

* interval: the frequency of the recurring task (e.g. "1h" for every hour or 1w+3h+20m for every 1 week and 3 hours and 20 minutes).
* delay: the time delay in milliseconds before the task starts executing.
* occurrences: the number of times the task should repeat.
* name: a name for your job request.
* return_url: the URL where Loopitis should send the callback.
* payload: any additional data that the task requires.
* callback_type: the HTTP method that Loopitis should use for the callback (e.g. POST, GET, etc.).
* notify_status_not_ok: The URL to call (POST call) if the status code of a call to return_url is not 200 (or timeout has reached).
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

* Replace <your_base64_encoded_credentials> with your actual base64-encoded credentials.

Once Loopitis receives your job request, it will wait for the specified delay before executing the task. It will then send an HTTP call to the return URL for the specified number of occurrences with the payload you provided.

Loopitis will also provide a response that includes an ID for your job request and an internal ID for tracking purposes.

On every call to return_url, Loopitis will provide the following data:

```shell
{  
  "parentRequestId": "d0f3b9f2-d973-4f15-b371-56ddd8a741bf",
  "parentRequestName": "my first job",
  "requestedTimes": 4,
  "executionNumber": 3,
  "executionId": "f8b667c9-9485-4a82-a63d-291920662831",
  "payload": "<your job's payload>>",
  "showRequest": "http://localhost:8080/requests/get_list?requestId=d0f3b9f2-d973-4f15-b371-56ddd8a741bf",
  "cancelLink": "http://localhost:8080/requests/cancel_task?requestId=d0f3b9f2-d973-4f15-b371-56ddd8a741bf"
}
```
Here's a breakdown of the data that Loopitis provides:

* parentRequestId: the job ID.
* parentRequestName: the job name.
* requestedTimes: the number of occurrences requested in the job.
* executionNumber: the current execution number out of the requested times.
* executionId: the execution ID.
* payload: the payload that was defined in the job request.
* showRequest: a link to show the request details.
* cancelLink: a link to cancel future executions.

If the user specified a notify_status_not_ok URL in the job request,
Loopitis will also send an HTTP call to that URL if the status code returned by the return_url 
is not 200 or if the timeout has been reached. The payload of the notify_status_not_ok
call will include the following data:

```
{
  "parentRequestId": "d0f3b9f2-d973-4f15-b371-56ddd8a741bf",
  "executionId": "f8b667c9-9485-4a82-a63d-291920662831",
  "callback_status_code": 404
}
```

The notify_status_not_ok payload includes the job ID, the execution ID,
and the HTTP status code returned by the callback.


To send back more data about the task execution, the user can include the execution ID in the response payload to the return URL. 
For example, if the user wants to send a "success" or "failure" status, they can include a "status" key in the JSON payload with 
the corresponding value. Loopitis will include this data which can be then monitored.

Overall, this feature allows the user to monitor the status of their tasks and take appropriate actions if any issues arise.

to send back data about that executions you can use /set/execution/comment/

```shell
{
  "executionId": "<the execution id>",
  "comment": "<the data to add to this execution>"  
}
```

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



