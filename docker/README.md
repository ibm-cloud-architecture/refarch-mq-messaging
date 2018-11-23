# Running MQ within Docker

We are reusing the official IBM mq docker image for developers (`ibmcom/mq`). The image runs an ubuntu image. The queue manager listener listens on port 1414 for incoming connections and port 9443 is used by MQ console.
Inside the container, the default MQ installation on Ubuntu has the following objects and permissions set:
 > Queue manager QM1  
  Queue DEV.QUEUE.1  
  Channel DEV.APP.SVRCONN  
  Listener DEV.LISTENER.TCP on port 1414

To match the vSphere settings we have developed a MQ config file (brown-config.mqsc) and copy it in the Dockerfile. You can build the docker file but we also publish this image (ibmcase/mq) to the dockerhub public site. So just running the image will give you the necessary container.

Queue manager and queue data are saved in the filesystem. To avoid losing the queue manager and queue data we use volumes. Volumes are attached to containers when they are run and persist after the container is deleted. The following command creates a volume

```shell
docker volume create qm1data
```

The MQ clients use a Channel to communicate with the MQ manager and over the network. We need to create a network to support this communication
```
docker network create mq-demo-network
``` 

The script `runmqdocker.sh` start MQ as daemon. We need now to connect to the server via the command: `docker exec -ti localMQ /bin/bash`. Once logged we can verify the installation and data paths by running `dspmqver`:

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

### Console 

The MQ Console is accessible at https://localhost:9443/ibmmq/console/login.html with default user admin / passw0rd. Here is an example of console:

![](mq-console.png)

### Run Item producer


### Demo Client

We are reusing a Dockerfile under the `democlient` folder to validate the configuration. To run it, execute the following steps under the `democlient` folder:

```shell
# Be sure to be logged to IBM support web site https://www.ibm.com/support/home/, so the download will work
# Build 
docker build -t mq-demo .
# Run
docker run --network mq-demo-network -ti mq-demo
``` 