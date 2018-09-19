# MQ Messaging Solution
This project is part of the 'IBM Integration Reference Architecture' suite, available at [https://github.com/ibm-cloud-architecture/refarch-integration](https://github.com/ibm-cloud-architecture/refarch-integration).

Updated 09/19/2018

It presents the implementation of an event producer, creating inventory update event, posted to a queue managed by IBM MQ Queue managed running on-premise or on IBM Cloud.

The Messaging application is integrated into the [inventory management](https://github.com/ibm-cloud-architecture/refarch-integration-inventory-dal) application.

The event producer is a Java application using the MQ APIs to connect to a queue manager and send message as text. The payload is a json document representing a new item added to a warehouse. The MQ manager is defined with IBM Cloud and an Event Listener, implemented as Message Driven Bean deployed on traditional WebSphere Application Server. This code will use the inventory data access layer service to persist data into the Inventory Database on DB2.

## Table of contents
* [Environments](#environments)
* [MQ on IBM Cloud](#configuring-mq-on-ibm-cloud-service)
* [Producer Code](#producer)
* [Consumer Code as MDB](#consumer)

## Environments
We have two environments:
### On premise MQ server

![](docs/SaaS-start.png)

#### Configuration Queue manager

![](docs/artifacts/image001.png)

![](docs/artifacts/image002.png)

![](docs/artifacts/image003.png)

![](docs/artifacts/image004.png)

### IBM Cloud deployment

![](docs/SaaS-endState.png)


#### Configuring MQ on IBM Cloud service

##### Create a MQ service
Once logged to the IBM Cloud console, using the `create new resource` button, select in the Catalog > Integration menu the `MQ` service:

![](docs/integration-catalog.png)

In the next page enter a name, region and resource group:

![](docs/create-mq-serv.png)

Once the service is created it can be seen under the Services list of your dashboard:

![](docs/mq-service.png)

##### Create a MQ manager
When you opening the service you can create a queue manager by clicking on the `create` button:

![](docs/mq-serv-home.png)

and then selecting the size and a enter a name:

![](docs/create-qmgr.png)

The queue manager is deploying, it can take some seconds...

![](docs/qmgr-deploying.png)

Then it is up and running so we need to get the connection information by downloading a json document using the `Connection information` button:

![](docs/qmrg-running.png)

##### Get API key
To connect an application to a queue manager, it must have been granted permissions to access queue managers within your IBM MQ service instance. The application must connect to the queue manager using its MQ username and the password must be the application's API key.

So to create a API key do the following:


## Code

### Producer
The producer's goal is to create "new item" events and send them to the queue. This is to simulate a warehouse backend service with mechanical systems which can scan item when reaching a specific part of the warehouse. The event will be processed to persist data in an Inventory database.

### Consumer
BrownEAR is a JEE application that contains one MDB (message
driven bean) EJB that reads a message from a request queue and then writes the
same message (with “Hello” as a prefix) to a response queue.
