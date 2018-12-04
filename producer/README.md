# Inventory message producer

This folder includes different MQ producers for testing MQ messaging solution. We are using two approaches to integrate with MQ: JMS APIs or pure MQ APIs.
The producer's goal is to create "new item" events and send them to MQ queue. This is to simulate a warehouse backend service with mechanical systems which can scan item when reaching a specific part of the warehouse. The event will be processed to persist data in an Inventory database.

## JMS Producer

The code is under the src/main/java with the package `ibm.cte.mq.jms`. As any JMS implementation the code needs to do the following:

* Build a JMF Factory using one of the JMS providers available. MQ comes with a JMS provider.
* Create a connection factory and define the connection parameters to connect to the MQ queue manager host.
* Build a [JMS Context](https://docs.oracle.com/javaee/7/api/javax/jms/JMSContext.html) is a new JMS 2.0 API to manage connection and session. 
* Define the JMS resource destination of the message: here we use the MQ Queue,
* Define the text message from a JSON serialized Inventory object
* Use a JMS producer to send the message to the queue
* Close the connection if there is no more message to send

### Build and run

To build the jar for the producer run maven command: `mvn install`. To run use the script runJmsMQProducer.sh.

If you want to define your own inventory object you can copy the json file in the `data` folder:

```json
{ itemId : 12,
  quantity: 10,
  site: "Manif01",
  supplierId: 5,
  cost: 12
}
```

For information about the identifier used see [the Inventory DB2 project](https://github.com/ibm-cloud-architecture/refarch-integration-inventory-db2).

### Test with local MQ

The `runJmsMQProducer.sh` script uses the config.properties loaded in the classpath which uses a localhost deployed MQ queue manager. So it will run with the docker based deployment described [here](../docker/README.md).

### Test with MQ on IBM Cloud 

The `src/main/resources/config-ioc.properties` includes parameters to connect to your remote MQ queue manager deployed on IBM Cloud. The script `runJmsRemoteMQProducer.sh` uses this file as program argument so the producer connects to this Queue manager. 