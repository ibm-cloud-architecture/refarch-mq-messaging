# Running MQ within Docker

We are reusing the official IBM mq docker image for developers (`ibmcom/mq`). The image runs an ubuntu image. The queue manager listener listens on port 1414 for incoming connections and port 9443 is used by MQ console.
Inside the container, the default MQ installation on Ubuntu has the following objects and permissions set:
 > Queue manager QM1  
  Queue DEV.QUEUE.1  
  Channel DEV.APP.SVRCONN  
  Listener DEV.LISTENER.TCP on port 1414

To match the vSphere settings used with the lift and shift scenario, we have developed a MQ config file (brown-config.mqsc) and used it to configure the MQ docker container. You can build this image from our  dockerfile if you want to modify the configuration, or better use our published public image (ibmcase/brownmq). Just running this image will give you the necessary container. The command `build.sh` can be used to build your own image and the script `runmqdocker.sh` to start a localMQ.

Queue manager and queue data are saved in the filesystem. To avoid losing the queue manager and queue data we use volumes. Volumes are attached to containers when they are run and persist after the container is deleted. The following command creates a volume

```shell
docker volume create qm1data
```

The remote MQ clients use a `Channel` to communicate with the MQ manager and over the network. We need to create a network to support this communication
```
docker network create mq-brown-network
``` 

The script `runmqdocker.sh` starts MQ as daemon. We need now to connect to the server via the command: `docker exec -ti localMQ /bin/bash`. Once logged we can verify the installation and data paths by running `dspmqver`:

```shell
(mq:9.1.0.0)root@38c35b86d5a5:/# dspmqver
Name:        IBM MQ
Version:     9.1.0.0
Level:       p910-L180709.DE
BuildType:   IKAP - (Production)
Platform:    IBM MQ for Linux (x86-64 platform)
Mode:        64-bit
O/S:         Linux 4.9.125-linuxkit
InstName:    Installation1
InstDesc:    
Primary:     Yes
InstPath:    /opt/mqm
DataPath:    /var/mqm
MaxCmdLevel: 910
LicenseType: Developer
```

Display your running queue managers:

```
(mq:9.1.0.0)root@38c35b86d5a5:/# dspmq
QMNAME(LQM1)                                              STATUS(Running)
```

### MQ Console 

The MQ Console is accessible at https://localhost:9443/ibmmq/console/login.html with default user admin / passw0rd. Here is an example of console layout with Queue manager (LQM1), channels (CLOUD.APP.SRVCONN) and queues (REQ.BROWN):

![](mq-console.png)

### Test Inventory JMS Consumer / Producer

We propose two implementations for the item producer: a JMS one or a pure MQ Java api one. To run the JMS producer and consumer do the following steps:

1. Start a terminal window (bash or shell)
1. cd `producer` folder
1. Start the JMS consumer that is listening to message arriving on REQ.BROWN queue and parses it as an Inventory:  `./runJmsMQConsumer.sh`.
1. In a second terminal window, under the `producer` folder, start the producer code that will parse an inventory definition defined in the `data/item1.json` and sends it to the REQ.BROWN queue. 

The trace on Producer side should look like:
> #####################################  
 Produce item inventory message to MQ  
Platform:IBM MQ on Premise  
Queue Manager:LQM1  
Queue Name:REQ.BROWN  
channel:CLOUD.APP.SVRCONN   
hostname:localhost   
port:1414   
userid:admin   
password:passw0rd   
SUCCESS  

And on the consumer side the trace is :
> ########################################  
 Consumer for inventory message from MQ   
 Queue: REQ.BROWN  
 ########################################  
  Waiting....  
  Received message:
{"itemId":12,"quantity":10,"site":"Manif01","supplierId":5,"cost":12.0}  
Inventory:
item= 12 for quantity= 10 @ Manif01 from 5

