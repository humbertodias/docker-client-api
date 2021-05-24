# MicroProfile generated Application

## Introduction

MicroProfile Starter has generated this MicroProfile application for you.

The generation of the executable jar file can be performed by issuing the following command

    ./gradlew clean microBundle

This will create an executable jar file **docker-client-java-demo-microbundle.jar** within the _target_ maven folder.
This can be started by executing the following command

    java -jar build/libs/ROOT-microbundle.jar

### Api

    curl http://localhost:8080/api/docker/create/busybox/sleep,900
    curl http://localhost:8080/api/docker/start/containerId
    curl http://localhost:8080/api/docker/exec/containerId/uname,-a