package ibm.cte.mq.poc;

import javax.jms.Destination;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;

import com.ibm.msg.client.jms.JmsConnectionFactory;
import com.ibm.msg.client.jms.JmsFactoryFactory;
import com.ibm.msg.client.wmq.WMQConstants;

public class TestQueueJmsConsumer {
	private static int status = 1;
	private static JMSContext context = null;
	private static Destination destination = null;
	private static JMSConsumer consumer = null;
	
    public static void main(String[] args) {
    	MQConfiguration cfg = new MQConfiguration();
    	cfg.loadProperties();
    	try {
			// Create a connection factory
			JmsFactoryFactory ff = JmsFactoryFactory.getInstance(WMQConstants.WMQ_PROVIDER);
			JmsConnectionFactory cf = ff.createConnectionFactory();
			cf.setStringProperty(WMQConstants.WMQ_HOST_NAME, cfg.getProperties().getProperty(MQConfiguration.MQ_HOST));
			cf.setIntProperty(WMQConstants.WMQ_PORT, Integer.parseInt(cfg.getProperties().getProperty(MQConfiguration.MQ_PORT)));
			cf.setStringProperty(WMQConstants.WMQ_CHANNEL,cfg.getProperties().getProperty(MQConfiguration.MQ_CHANNEL));
			cf.setIntProperty(WMQConstants.WMQ_CONNECTION_MODE, WMQConstants.WMQ_CM_CLIENT);
			cf.setStringProperty(WMQConstants.WMQ_QUEUE_MANAGER, cfg.getProperties().getProperty(MQConfiguration.MQ_QUEUEMANAGER));
			cf.setStringProperty(WMQConstants.WMQ_APPLICATIONNAME, "JmsPutItem");
			cf.setBooleanProperty(WMQConstants.USER_AUTHENTICATION_MQCSP, true);
			cf.setStringProperty(WMQConstants.USERID, cfg.getProperties().getProperty(MQConfiguration.MQ_USERID));
			cf.setStringProperty(WMQConstants.PASSWORD, cfg.getProperties().getProperty(MQConfiguration.MQ_PASSWORD));

			// Create JMS objects
			context = cf.createContext();
			destination = context.createQueue("queue:///" + cfg.getProperties().getProperty(MQConfiguration.MQ_QUEUENAME));
			consumer = context.createConsumer(destination); // autoclosable
			String receivedMessage = consumer.receiveBody(String.class, 15000); // in ms or 15 seconds

			System.out.println("\nReceived message:\n" + receivedMessage);
    	} catch(Exception e) {
    		
    	}
    }
}
