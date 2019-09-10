# Exposer Microservice

This service is a java spring application that uses kafka to communicate and endpoints to expose the results.
Additionally it connects to the [notifications server](https://github.com/annie60/socketio-nodejs)
Only prepared for local run.

## Pre requirements
1. [Kafka 2.3](https://kafka.apache.org/downloads)

## Run
`mvn clean install`