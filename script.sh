#!/bin/bash
-x
cd ~/IdeaProjects/notifier/
mvn clean package
cd ~
cp ~/IdeaProjects/notifier/target/endpoints.jar .
sudo docker-compose --env-file config.properties stop endpoint
sudo docker-compose --env-file config.properties build endpoint
sudo docker-compose --env-file config.properties up endpoint
+x
