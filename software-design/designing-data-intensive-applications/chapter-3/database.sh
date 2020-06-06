#!/bin/bash

db_set(){
    echo "$1,$2" >> database
}

db_get(){
    grep "^$1," database | sed -e "s/^$1,//" | tail -n 1
}

db_set 123456 '{"name":"London","attractions":["Big Ben","London Eye"]}'
db_set 42 '{"name":"San Francisco","attractions":["Golden Gate Bridge"]}'
db_get 42