package ibm.cte.mq.poc;

import java.io.IOException;

import com.ibm.mq.MQException;
import com.ibm.mq.MQGetMessageOptions;
import com.ibm.mq.MQMessage;
import com.ibm.mq.MQQueue;
import com.ibm.mq.MQQueueManager;
import com.ibm.mq.constants.MQConstants;

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
	         MQQueue queue = qMgr.accessQueue("RESP.BROWN", 
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
	}

	public static void main(String[] args) {

		System.out.println("#######################################");
		System.out.println(" Consumer item inventory message to MQ ");
		InventoryConsumer consumer = new InventoryConsumer();
		consumer.configure("src/main/resources/config.properties");
        consumer.run();
	}
	
}
