# MQ Messaging Solution
This project is part of the 'IBM Integration Reference Architecture' suite, available at [https://github.com/ibm-cloud-architecture/refarch-integration](https://github.com/ibm-cloud-architecture/refarch-integration).

Updated 10/5/2018

It presents the implementation of an event producer, creating inventory update event, posted to a queue managed by IBM MQ Queue managed running on-premise or on IBM Cloud.

The Messaging application is integrated into the [inventory management](https://github.com/ibm-cloud-architecture/refarch-integration-inventory-dal) application.

The event producer is a Java application using the MQ APIs to connect to a queue manager and send message as text. The payload is a json document representing a new item added to a warehouse. The MQ manager is defined with IBM Cloud and an Event Listener, implemented as Message Driven Bean deployed on traditional WebSphere Application Server. This code will use the inventory data access layer service to persist data into the Inventory Database on DB2.

## Table of contents
* [Environments](#environments)
* [MQ on IBM Cloud](#configuring-mq-on-ibm-cloud-service)
* [Producer Code](#producer)
* [Consumer Code as MDB](#consumer)

## Environments
We have two environments: on-premise and IBM Cloud.

### On premise MQ server

![](docs/SaaS-start.png)

#### Configuration Queue manager
Start MQ Explorer from your server installation
![](docs/artifacts/mq-explorer.png)

Under the Queue Managers folder look at the standard QM definition.
![](docs/artifacts/image001.png)
#### Queues
This is a standard QM definition.
![](docs/artifacts/image002.png)

Under `Channels`, one server connection channel (CLOUD.APP.SVRCONN) was defined for the MDB to exclusively use to connect to the queue manager.

![](docs/artifacts/image003.png)

For the CLOUD.APP.SVRCONN channel, the MCA user ID was set
to `admin`. This means that the MDB application will be connected to the Queue Manager as “admin” which is a user ID defined on the operating system where the Queue Manager is running.

![](docs/artifacts/image004.png)

#### Channel Authentication Record
One channel authentication record was added to the CLOUD.APP.SVRCONN to not block users.

![](docs/artifacts/image005.png)

#### Listener
One listener was defined with port 1415 for the MDB application to connect into

![](docs/artifacts/image006.png)

#### Runmqsc Commands
These commands were issued with the “runmqsc” CLI to allow clients
to connect.

```
SET CHLAUTH(CLOUD.APP.SVRCONN) TYPE(BLOCKUSER) USERLIST('nobody')`

ALTER AUTHINFO(SYSTEM.DEFAULT.AUTHINFO.IDPWOS) AUTHTYPE(IDPWOS) CHCKCLNT(OPTIONAL)

REFRESH SECURITY TYPE(CONNAUTH)
```

### Configuring WebSphere Application Server to access MQ
As the MDB will be deployed as EJB on WebSphere Application Server, we need to do some configuration:

#### Define Queue Connection Factory

![](docs/artifacts/image007.png)

![](docs/artifacts/image008.png)

![](docs/artifacts/image009.png)

#### Defining Queues

![](docs/artifacts/image010.png)

![](docs/artifacts/image011.png)

#### Defining activation specification

![](docs/artifacts/image012.png)

![](docs/artifacts/image013.png)

![](docs/artifacts/image014.png)

#### Authentication Alias

![](docs/artifacts/image015.png)

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

##### Create a MQ Queue Manager
When you opening the service you can create a queue manager by clicking on the `create` button:

![](docs/mq-serv-home.png)

and then selecting the size and a enter a name:

![](docs/create-qmgr.png)

The queue manager is deploying, it can take some seconds...

![](docs/qmgr-deploying.png)

Then it is up and running so we need to get the connection information by downloading a JSON document using the `Connection information` button.
Save the connection information file for a later step.

Next, let's setup a user account and an application account.
If you click the "back" arrow, you will see the list of Queue Managers. In this case it's only the one you created.

![](docs/qmgr-list.png)

Click on the queue manager to manage it. Click on the "User permissions" and then the "Add User" button to add a user.

![](docs/qmgr-define-admin-user.png)

Now enter the email address for the user. Next click the "Generate MQ username". the MQ username is the username you need to use when connecting to the queue manager. Lastly, click the "Add permissions" button to create the user.

![](docs/qmgr-define-admin-user-details.png)

This screenshot shows the  newly created user with administrator privileges.

![](docs/qmgr-define-admin-user-results.png)

Now you have to create an application. Creating an application gives you a way to have MQ clients connect to your queue manager. Click on the "Application permissions" button. Next, click on the "Add application" button.

![](docs/qmgr-define-application.png)

Enter an application name and click on the "Generate MQ username" button. 

![](docs/qmgr-define-application-details.png)

Next, click on the "Add and generate API key" button.

![](docs/qmgr-define-application-details-Generate-MQ-Username.png)

You will see a dialog box where you can download your API key for your new application. The API key is your password when connecting to your queue manager.

![](docs/qmgr-define-application-details-Add-Application.png)

Here is the resulting dialog after clicking the "Download" button.

![](docs/qmgr-define-application-details-Download-API-key.png)

This screenshot shows you the application that you just created.

![](docs/qmgr-define-application-results.png)

You will have to retrieve your connection information for your queue manager. Click on the queue manager.

![](docs/qmgr-define-admin-user-get-API-key-goto-QM-page.png)

Click on the "Administration" tab.

![](docs/qmgr-define-admin-user-get-API-key-goto-QM-details-page.png)

Click on the "Connection information" button.

![](docs/qmgr-define-admin-user-get-API-key-goto-QM-details-page-Adminstrators.png)

You will see a dialog to download your connection information. You can download the connection information in two formats (plain text or JSON). Here the "JSON text format" link is used.

![](docs/qmgr-define-admin-user-get-API-key-goto-QM-details-page-Adminstrators-Download-API-Key.png)

Save your connection information.

![](docs/qmgr-define-admin-user-get-API-key-goto-QM-details-page-Adminstrators-Download-Connection-Info.png)

Now, click the "Create IBM Cloud API key" button and you will see this dialog. Click the "Download" button.

![](docs/qmgr-define-admin-user-get-API-key-goto-QM-details-page-Adminstrators-Change-Download-API-Key.png)

Now you have to create queues on your queue manager. Click the "Launch MQ Console" button and the MQ Console will open in another browser tab. Enter your administrator username and API key (which is the password).

![](docs/qmgr-login-MQ-Console.png)

Create a request queue.

![](docs/qmgr-login-MQ-Console-Create-Queue-REQ.BROWN.png)

Create a response queue.

![](docs/qmgr-login-MQ-Console-Create-Queue-RESP.BROWN.png)



#### Configuring EAR on WebSphere

Log into the WebSphere Admin Console.

Navigate to: Resources->JMS->Queue connection factories

##### Create Queue Connection Factory

Select the scope and click the "New" button.

![](docs/001-QCF.png)

Select "WebSphere MQ messaging provider" and click the "OK" button.

![](docs/003-QCF.png)

Enter the "Name" and "JNDI name" and click the "Next" button.

![](docs/006-QCF.png)

Select the "Enter all the required information into this wizard" button and click "Next".

![](docs/009-QCF.png)

Enter the queue manager name and click the "Next" button.

![](docs/012-QCF.png)

Enter the hostname, port and server connection channel and click "Next".

![](docs/015-QCF.png)

Click the "Next" button (Test won't work).

![](docs/018-QCF.png)

Preview your selections and click the "Finish" button.

![](docs/021-QCF.png)

Save your configuration changes.


##### Create Request Queue

Navigate to: Resources->JMS->Queues

Select the scope and click the "New" button.

![](docs/101-RequestQ.png)

Select WebSphere MQ messaging provider and click "OK".

![](docs/103-RequestQ.png)

Fill in the Name, JNDI name and Queue name fields and click "OK".

![](docs/109-RequestQ.png)

Save your configuration changes.

##### Create Response Queue

Navigate to: Resources->JMS->Queues

Select the scope and click the "New" button.

![](docs/201-ResponseQ.png)

Select WebSphere MQ messaging provider and click "OK".

![](docs/203-ResponseQ.png)

Fill in the Name, JNDI name and Queue name fields and click "OK".

![](docs/206-ResponseQ.png)

Save your configuration changes.

![](docs/209-ResponseQ.png)

Here are the two queues you just created.

![](docs/212-ResponseQ.png)

##### Create Activation Specification

Navigate to: Resources->JMS->Activation specifications

Click the "New" button.

![](docs/301-ActivationSpec.png)

Select WebSphere MQ messaging provider and click "OK".

![](docs/303-ActivationSpec.png)

Enter the Name and JNDI name and click "Next".

![](docs/306-ActivationSpec.png)

Enter the destination JNDI name (which is the request queue) and click "Next".

![](docs/309-ActivationSpec.png)

Select the "Enter all the required information into this wizard" button and click "Next".

![](docs/312-ActivationSpec.png)

Enter the queue manager name and click "Next".

![](docs/315-ActivationSpec.png)

Enter the hostname, port and server connection channel and click "Next".

![](docs/318-ActivationSpec.png)

Click the "Next" button (Test won't work).

![](docs/321-ActivationSpec.png)

Review your selections and click "Finish".

![](docs/324-ActivationSpec.png)

Save your configuration changes.

![](docs/327-ActivationSpec.png)

##### Create JAAS Authentication Alias

Go back to the main page for the Activation Specification which you just completed and click the "JAAS - J2C authentication data" link.

![](docs/400-AuthAlias.png)

Click the "New" button.

![](docs/401-AuthAlias.png)

Enter the alias, user ID and password. The user ID will be your application name and the password will be the API key matched the the application name. Click the "OK" button.

![](docs/403-AuthAlias.png)

Save you configuration changes.

![](docs/406-AuthAlias.png)

##### Create-Deploy-EAR

Navigate to: Applications->New Application

![](docs/501-Deploy-EAR.png)

Upload the EAR file and click "Next".

![](docs/503-Deploy-EAR.png)

![](docs/506-Deploy-EAR.png)

Select the "Detailed" path and click "Next".

![](docs/509-Deploy-EAR.png)

Check the "Deploy enterprise beans" and click "Next".

![](docs/512-Deploy-EAR.png)
![](docs/515-Deploy-EAR.png)

Map the EJB and click "Next".

![](docs/518-Deploy-EAR.png)

Click "Next".

![](docs/521-Deploy-EAR.png)

Click "Next".

![](docs/524-Deploy-EAR.png)

Click "Next".

![](docs/527-Deploy-EAR.png)

Make sure your Activation specification parameters match the resources you created. Click "Next".

![](docs/530-Deploy-EAR.png)

Make sure your response queue parameter matches the resource you created. Click "Next".

![](docs/533-Deploy-EAR.png)

Make sure your connection queue factory parameter matches the resource you created. Click "Next".

![](docs/536-Deploy-EAR.png)
![](docs/537-Deploy-EAR.png)

Click "Continue".

![](docs/539-Deploy-EAR.png)

Click "Next".

![](docs/542-Deploy-EAR.png)

Click "Next".

![](docs/545-Deploy-EAR.png)

Review your selections and click "Finish".

![](docs/548-Deploy-EAR.png)
![](docs/551-Deploy-EAR.png)

Make sure the EAR deploys and then Save your configuration.

![](docs/554-Deploy-EAR.png)

After you deploy the EAR, you have another step to do. You have to set your Authentication Alias on the application's connection factory.

In the WAS Admin Console, navigate to Applications->All applications and click on your application. Once your application details page (not shown) opens, click on the "Resource references" link (lower left).

Select the EJB, and click on the "Modify Resource Authentication Method" button. Now you will see this dialog.

![](docs/600-Set-Auth-Alias-Connection-Factory.png)

Check the "user default method" radio button and use the pull-down list to select your authentication alias. Lastly, click the "Apply" button.

![](docs/610-Set-Auth-Alias-Connection-Factory.png)

The "Authentication data entry" should be set to your authentication alias (and not "None"). Lastly, click on the "OK" button (not shown here).

![](docs/620-Set-Auth-Alias-Connection-Factory.png)

On this page, accept the warning and click the "Continue" button.

![](docs/630-Set-Auth-Alias-Connection-Factory.png)

##### Final Configuration Steps

First, you need to add this JVM command line parameter:

-Dcom.ibm.mq.cfg.jmqi.useMQCSPauthentication=true

Navigate to "All servers" and click on your server. 
Then, expand "Java and Process Management" and click on "Process definition". Then, click on the "Java Virtual Machine" link.
Finally, add the JVM command line parameter to the text box labeled "Generic JVM arguments".

![](docs/700-Add-JVM-Param.png)

NOTE #1: Queue Permissions

The cloud-based queue manager will create default queues (DEV.QUEUE.1, DEV.QUEUE.2, DEV.QUEUE.3). They will have the correct permissions for the application user names and passwords that you configure. If you use them, you don't have to do anything more.

But, if you create your own queues, you have to modify the permissions for your queues. The following link (see line 48) shows the runmqsc commands that configure the default rules to allow access to the "DEV.*" queues, so you would need to execute an equivalent command to grant permission to your own queues:

https://github.com/ibm-messaging/mq-container/blob/4d4051312eb9d95a086e2ead76482d1f1616d149/incubating/mqadvanced-server-dev/10-dev.mqsc.tpl#L48

NOTE #2: TLS between WAS and MQ

If you want to encrypt the communication between WAS and MQ, here is a scenario that secures a WAS to MQ application using TLS. You can leverage this next link and adapt it to your environment:

https://www.ibm.com/support/knowledgecenter/prodconn_1.0.0/com.ibm.scenarios.wmqwassecure.doc/topics/cfgssl_was.htm?cp=SSFKSJ_9.0.0


NOTE #3: When connecting to the queue manager, the WebSphere application must pass its username and password (API key) to the cloud based queue manager. There is an issue with the fact that the length of the API key is too long. To address this, you have to use the MQCSP authentication mode.

WAS v8.5.5 includes an old version of the MQ resource adapter (from MQ v7.1, see https://www-01.ibm.com/support/docview.wss?uid=swg21248089) which doesn't have the capability to control the compatibility mode, so we are basically sure that only a subset of the password is being sent, and so causing the problem you are seeing.

There isn't an option to update the MQ resource adapter inside WAS v8.5.5 so the path we would recommend is to use WAS v9.0 as the target server version. 

WAS 9.0 includes a more up to date MQ resource adapter version against which you can then set a generic JVM argument 
"-Dcom.ibm.mq.cfg.jmqi.useMQCSPauthentication=true" in order to ensure the full password is transmitted as detailed here https://www.ibm.com/support/knowledgecenter/en/SSFKSJ_9.0.0/com.ibm.mq.sec.doc/q118680_.html (note that this applies to all MQ applications running in that appserver JVM)

## Code

### Producer
The producer's goal is to create "new item" events and send them to the queue. This is to simulate a warehouse backend service with mechanical systems which can scan item when reaching a specific part of the warehouse. The event will be processed to persist data in an Inventory database.

### Consumer
BrownEAR is a JEE application that contains one MDB (message
driven bean) EJB that reads a message from a request queue and then writes the
same message (with “Hello” as a prefix) to a response queue.

### EAR deployment
When you deploy the EAR, make sure the activation specification parameters are correct and match your artifact names.

![](docs/artifacts/image016.png)

![](docs/artifacts/image017.png)
