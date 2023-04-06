#!/bin/bash

echo "Select an option:"
echo "1. endpoints"
echo "2. consumer"

set -x
read option

case $option in
  1)
    echo "You selected endpoint"
    cd ~/IdeaProjects/notifier/
    mvn clean package
    cd ~
    cp ~/IdeaProjects/notifier/target/endpoints.jar .
    sudo docker-compose --env-file config.properties stop endpoint
    sudo docker-compose --env-file config.properties build endpoint
    sudo docker-compose --env-file config.properties up endpoint
    ;;
  2)
    echo "You selected consumer"
        cd ~/IdeaProjects/notifier/
        mvn clean package
        cd ~
        cp ~/IdeaProjects/notifier/target/consumer.jar .
        sudo docker-compose --env-file config.properties stop consumer
        sudo docker-compose --env-file config.properties build consumer
        sudo docker-compose --env-file config.properties up consumer
    ;;
  *)
    echo "Invalid option. Please try again."
    ;;
esac
set +x
