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

# Generate passowrd for DB

# Generate a random password for db
db_password=$(openssl rand -base64 12)

# Update the value of the db_password property in the config.properties file
sed -i "s/^db_password=.*/db_password=$db_password/" config.properties


##### Generate password for redis ######


# Generate a random password for redis
redis_password=$(openssl rand -base64 12)

# Update the value of the db_password property in the config.properties file
sed -i "s/^redis_password=.*/redis_password=$db_password/" config.properties


##### Generate password for Endpoint  #######


# Generate a random password
loopitis_password=$(openssl rand -base64 12)

# Update the value of the db_password property in the config.properties file
sed -i "s/^loopitis_password=.*/loopitis_password=$loopitis_password/" config.properties


echo "New generated loopitis user&password:"
echo "user: user"
echo "password: $loopitis_password"

echo "This password should be used to call loopitis endpoints using a  Basic Authorization in the header"

docker-compose --env-file config.properties up






#curl -sSL https://bit.ly/3W0DUPt | bash
#or
#curl -sSL https://raw.githubusercontent.com/loopitis/loopitis-repo/master/src/main/resources/config/run.sh | bash
