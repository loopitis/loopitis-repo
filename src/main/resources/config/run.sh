#!/bin/bash

##########################################################################################################################
############# This file is made for the lazy developer. You can run loopitis using this file only            #############
############# Assuming you have docker-composed installed on your machine                                    #############
############# This script copies the three files you need in order to run loopitis                           #############
############# The compose.yaml file - which defines the processes and which repository on docker-hub to get  #############
############# The init.sql - for initiliaze the DB (creste a schema and tables needed)                       #############
############# The config.properties which defines the general configuration along with                       #############
############# user&passwords (it is strongly advised to change all user and passwords in that file           #############
##########################################################################################################################

# Array of URLs to download
URLS=(
    "https://raw.githubusercontent.com/loopitis/loopitis-repo/master/src/main/resources/config/compose.yaml"
    "https://raw.githubusercontent.com/loopitis/loopitis-repo/master/src/main/resources/config/config.properties"
    "https://raw.githubusercontent.com/loopitis/loopitis-repo/master/src/main/resources/config/init.sql"
)

# Download each file in the array using curl
for url in "${URLS[@]}"
do
    # The file name to save the downloaded file as
    FILE_NAME=$(basename "$url")

    # Download the file using curl
    curl -o "$FILE_NAME" "$url"
done

docker-compose --env-file config.properties up
