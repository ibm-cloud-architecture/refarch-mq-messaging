package ibm.cte.mq.jms;

import javax.jms.Destination;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;

import com.google.gson.Gson;
import com.ibm.msg.client.jms.JmsConnectionFactory;
import com.ibm.msg.client.jms.JmsFactoryFactory;
import com.ibm.msg.client.wmq.WMQConstants;

import ibm.cte.mq.MQConfiguration;
import ibm.cte.mq.msg.Inventory;

public class JMSItemConsumer {
	protected  Gson parser = new Gson();
	MQConfiguration config = new MQConfiguration();;
	
	public JMSItemConsumer() {
    	getConfig().loadProperties();
	}
	
    public static void main(String[] args) {
    
    	JMSItemConsumer jmsConsumer = new JMSItemConsumer();
    	try {
    		JMSContext context = jmsConsumer.buildContext();
    		System.out.println("########################################");
    		System.out.println(" Consumer for inventory message from MQ ");
    		System.out.println(" Queue: " + jmsConsumer.getConfig().getProperties().getProperty(MQConfiguration.MQ_QUEUENAME));
    		System.out.println("########################################\n\n  Waiting....");
    		jmsConsumer.run(context,jmsConsumer.getConfig().getProperties().getProperty(MQConfiguration.MQ_QUEUENAME));
    	} catch(JMSException e ) {
    		e.printStackTrace();
    	}
			
    }
    
    public JMSContext buildContext() throws JMSException {
    	
    	// Create a connection factory
		JmsFactoryFactory ff = JmsFactoryFactory.getInstance(WMQConstants.WMQ_PROVIDER);
		JmsConnectionFactory cf = ff.createConnectionFactory();
		cf.setStringProperty(WMQConstants.WMQ_HOST_NAME, getConfig().getProperties().getProperty(MQConfiguration.MQ_HOST));
		cf.setIntProperty(WMQConstants.WMQ_PORT, Integer.parseInt(getConfig().getProperties().getProperty(MQConfiguration.MQ_PORT)));
		cf.setStringProperty(WMQConstants.WMQ_CHANNEL,getConfig().getProperties().getProperty(MQConfiguration.MQ_CHANNEL));
		cf.setIntProperty(WMQConstants.WMQ_CONNECTION_MODE, WMQConstants.WMQ_CM_CLIENT);
		cf.setStringProperty(WMQConstants.WMQ_QUEUE_MANAGER, getConfig().getProperties().getProperty(MQConfiguration.MQ_QUEUEMANAGER));
		cf.setStringProperty(WMQConstants.WMQ_APPLICATIONNAME, "JmsGetItem");
		cf.setBooleanProperty(WMQConstants.USER_AUTHENTICATION_MQCSP, true);
		cf.setStringProperty(WMQConstants.USERID, getConfig().getProperties().getProperty(MQConfiguration.MQ_USERID));
		cf.setStringProperty(WMQConstants.PASSWORD, getConfig().getProperties().getProperty(MQConfiguration.MQ_PASSWORD));

		// Create JMS objects
		return cf.createContext();
    }
    
    public void run(JMSContext context,String queueName) {
    	try {
    		Destination destination = context.createQueue("queue:///" + queueName);
    		JMSConsumer consumer = context.createConsumer(destination); // autoclosable
    		while (true) {
				String receivedMessage = consumer.receiveBody(String.class, 15000); // in ms or 15 seconds
				if (receivedMessage != null) {
					System.out.println("\nReceived message:\n" + receivedMessage);
					Inventory i = getParser().fromJson(receivedMessage,Inventory.class);
					System.out.println("\nInventory:\n" + i.toString());
				}
				
    		}
    	} catch(Exception e) {
    		e.printStackTrace();
    	}
    }

	public Gson getParser() {
		return parser;
	}

	public MQConfiguration getConfig() {
		return config;
	}
}
