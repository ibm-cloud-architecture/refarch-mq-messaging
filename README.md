# MQ Messaging Solution
This project is part of the 'IBM Integration Reference Architecture' suite, available at [https://github.com/ibm-cloud-architecture/refarch-integration](https://github.com/ibm-cloud-architecture/refarch-integration).
It presents the implementation of an event producer to Queue managed by on-premise MQQ queue manager or on IBM Cloud.


## Configuring MQ on Cloud service

### Create a MQ service
Once logged to the IBM Cloud console, using the `create new resource` button, select in the Catalog > Integration menu the `MQ` service:

![](docs/integration-catalog.png)

In the next page enter a name, region and resource group:

![](docs/create-mq-serv.png)

Once the service is created it can be seen under the Services list of your dashboard:

![](docs/mq-service.png)

### Create a MQ manager
When you opening the service you can create a queue manager by clicking on the `create` button:

![](docs/mq-serv-home.png)

and then selecting the size and a enter a name:

![](docs/create-qmgr.png)

The queue manager is deploying, it can take some seconds...

![](docs/qmgr-deploying.png)

Then it is up and running so we need to get the connection information by downloading a json document using the `Connection information` button:

![](docs/qmrg-running.png)

### Get API key
To connect an application to a queue manager, it must have been granted permissions to access queue managers within your IBM MQ service instance. The application must connect to the queue manager using its MQ username and the password must be the application's API key.

So to create a API key do the following:


## Code

### Producer code
The producer's goal is to create new item events and send them to the queue.  
