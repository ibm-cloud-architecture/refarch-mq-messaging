package ibm.cte.mq.poc;

import java.io.IOException;

import com.ibm.mq.MQException;
import com.ibm.mq.MQGetMessageOptions;
import com.ibm.mq.MQMessage;
import com.ibm.mq.MQQueue;
import com.ibm.mq.MQQueueManager;
import com.ibm.mq.constants.MQConstants;

import ibm.cte.mq.MQConfiguration;

public class InventoryConsumer implements Runnable {
	
	private MQConfiguration config = new MQConfiguration();
	
	public InventoryConsumer() {	
	}

	@Override
	public void run() {
		System.out.println(" Consumer ready to go ...");
		 MQQueueManager qMgr = null;
	     try {
	         qMgr = getConfig().createQueueManager();
	         MQMessage retrievedMessage = new MQMessage();
	         retrievedMessage.messageId = new byte[] {1};
	         MQGetMessageOptions gmo = new MQGetMessageOptions();
	         gmo.waitInterval= 15000;
	         gmo.options = MQConstants.MQGMO_WAIT;
	         int openOptions = MQConstants.MQOO_OUTPUT | MQConstants.MQOO_INPUT_AS_Q_DEF;

	         // creating destination
	         MQQueue queue = qMgr.accessQueue(getConfig().getProperties().getProperty(MQConfiguration.MQ_QUEUENAME, "REQ.BROWN"), 
	            		openOptions);
	        	 queue.get(retrievedMessage,gmo);
	        	 String msgText = retrievedMessage.readUTF();
		         System.out.println("The message is: " + msgText);     
	     } catch (MQException e) {
	        	e.printStackTrace();
	        
		 } catch (IOException e) {
			e.printStackTrace();
		} finally {
	            try {
	            	if (qMgr != null)
	            		qMgr.disconnect();
	            } catch (MQException e) {
	                e.printStackTrace();
	            }
	        }
	}

	public MQConfiguration getConfig() {
		return config;
	}

	public void configure(String configFileName) {
	   this.getConfig().loadProperties(configFileName);
	   System.out.println("Platform:" + this.getConfig().getProperties().getProperty(MQConfiguration.MQ_PLATFORM));
		System.out.println("Queue Manager:" + this.getConfig().getProperties().getProperty(MQConfiguration.MQ_QUEUEMANAGER));
		System.out.println("Queue Name:" + this.getConfig().getProperties().getProperty(MQConfiguration.MQ_QUEUENAME));
		System.out.println("channel:" + this.getConfig().getProperties().getProperty(MQConfiguration.MQ_CHANNEL));
		System.out.println("hostname:" + this.getConfig().getProperties().getProperty(MQConfiguration.MQ_HOST));
		System.out.println("port:" + this.getConfig().getProperties().getProperty(MQConfiguration.MQ_PORT));
		System.out.println("userid:" + this.getConfig().getProperties().getProperty(MQConfiguration.MQ_USERID));
		System.out.println("password:" + this.getConfig().getProperties().getProperty(MQConfiguration.MQ_PASSWORD));
   
	}


	public static void main(String[] args) {

		System.out.println("#######################################");
		System.out.println(" Consumer item inventory message to MQ ");
		InventoryConsumer consumer = new InventoryConsumer();
		if (args.length == 1) {
			consumer.configure(args[0]);
        } else {
            // load config.properties from class loader);
			consumer.configure("src/main/resources/config.properties");
        }
	
        consumer.run();
	}
	
}
