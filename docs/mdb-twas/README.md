# Configuring MDB resources on WebSphere Application Server

Navigate to: Resources->JMS->Queue connection factories

#### Create Queue Connection Factory

Select the scope and click the "New" button.

![](./001-QCF.png)

Select "WebSphere MQ messaging provider" and click the "OK" button.

![](./003-QCF.png)

Enter the "Name" and "JNDI name" and click the "Next" button.

![](./006-QCF.png)

Select the "Enter all the required information into this wizard" button and click "Next".

![](./009-QCF.png)

Enter the queue manager name and click the "Next" button.

![](./012-QCF.png)

Enter the hostname, port and server connection channel and click "Next".

![](./015-QCF.png)

Click the "Next" button (Test won't work).

![](./018-QCF.png)

Preview your selections and click the "Finish" button.

![](./021-QCF.png)

Save your configuration changes.

#### Create Request Queue

Navigate to: Resources->JMS->Queues

Select the scope and click the "New" button.

![](./101-RequestQ.png)

Select WebSphere MQ messaging provider and click "OK".

![](./103-RequestQ.png)

Fill in the Name, JNDI name and Queue name fields and click "OK".

![](./109-RequestQ.png)

Save your configuration changes.

#### Create Response Queue

Navigate to: Resources->JMS->Queues

Select the scope and click the "New" button.

![](./201-ResponseQ.png)

Select WebSphere MQ messaging provider and click "OK".

![](./203-ResponseQ.png)

Fill in the Name, JNDI name and Queue name fields and click "OK".

![](./206-ResponseQ.png)

Save your configuration changes.

![](./209-ResponseQ.png)

Here are the two queues you just created.

![](./212-ResponseQ.png)

#### Create Activation Specification

Navigate to: Resources->JMS->Activation specifications

Click the "New" button.

![](./301-ActivationSpec.png)

Select WebSphere MQ messaging provider and click "OK".

![](./303-ActivationSpec.png)

Enter the Name and JNDI name and click "Next".

![](./306-ActivationSpec.png)

Enter the destination JNDI name (which is the request queue) and click "Next".

![](./309-ActivationSpec.png)

Select the "Enter all the required information into this wizard" button and click "Next".

![](./312-ActivationSpec.png)

Enter the queue manager name and click "Next".

![](./315-ActivationSpec.png)

Enter the hostname, port and server connection channel and click "Next".

![](./318-ActivationSpec.png)

Click the "Next" button (Test won't work).

![](./321-ActivationSpec.png)

Review your selections and click "Finish".

![](./324-ActivationSpec.png)

Save your configuration changes.

![](./327-ActivationSpec.png)

#### Create JAAS Authentication Alias

Go back to the main page for the Activation Specification which you just completed and click the "JAAS - J2C authentication data" link.

![](./400-AuthAlias.png)

Click the "New" button.

![](./401-AuthAlias.png)

Enter the alias, user ID and password. The user ID will be your application name and the password will be the API key matched the the application name. Click the "OK" button.

![](./403-AuthAlias.png)

Save you configuration changes.

![](./406-AuthAlias.png)

#### Create-Deploy-EAR

Navigate to: Applications->New Application

![](./501-Deploy-EAR.png)

Upload the EAR file and click "Next".

![](./503-Deploy-EAR.png)

![](./506-Deploy-EAR.png)

Select the "Detailed" path and click "Next".

![](./509-Deploy-EAR.png)

Check the "Deploy enterprise beans" and click "Next".

![](./512-Deploy-EAR.png)
![](./515-Deploy-EAR.png)

Map the EJB and click "Next".

![](./518-Deploy-EAR.png)

Click "Next".

![](./521-Deploy-EAR.png)

Click "Next".

![](./524-Deploy-EAR.png)

Click "Next".

![](./527-Deploy-EAR.png)

Make sure your Activation specification parameters match the resources you created. Click "Next".

![](./530-Deploy-EAR.png)

Make sure your response queue parameter matches the resource you created. Click "Next".

![](./533-Deploy-EAR.png)

Make sure your connection queue factory parameter matches the resource you created. Click "Next".

![](./536-Deploy-EAR.png)
![](./537-Deploy-EAR.png)

Click "Continue".

![](./539-Deploy-EAR.png)

Click "Next".

![](./542-Deploy-EAR.png)

Click "Next".

![](./545-Deploy-EAR.png)

Review your selections and click "Finish".

![](./548-Deploy-EAR.png)
![](./551-Deploy-EAR.png)

Make sure the EAR deploys and then Save your configuration.

![](./554-Deploy-EAR.png)

After you deploy the EAR, you have another step to do. You have to set your Authentication Alias on the application's connection factory.

In the WAS Admin Console, navigate to Applications->All applications and click on your application. Once your application details page (not shown) opens, click on the "Resource references" link (lower left).

Select the EJB, and click on the "Modify Resource Authentication Method" button. Now you will see this dialog.

![](./600-Set-Auth-Alias-Connection-Factory.png)

Check the "user default method" radio button and use the pull-down list to select your authentication alias. Lastly, click the "Apply" button.

![](./610-Set-Auth-Alias-Connection-Factory.png)

The "Authentication data entry" should be set to your authentication alias (and not "None"). Lastly, click on the "OK" button (not shown here).

![](./620-Set-Auth-Alias-Connection-Factory.png)

On this page, accept the warning and click the "Continue" button.

![](./630-Set-Auth-Alias-Connection-Factory.png)

#### Final Configuration Steps

First, you need to add this JVM command line parameter:

-Dcom.ibm.mq.cfg.jmqi.useMQCSPauthentication=true

Navigate to "All servers" and click on your server. 
Then, expand "Java and Process Management" and click on "Process definition". Then, click on the "Java Virtual Machine" link.
Finally, add the JVM command line parameter to the text box labeled "Generic JVM arguments".

![](./700-Add-JVM-Param.png)

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