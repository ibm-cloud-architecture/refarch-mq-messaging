package ibm.cte.mq.jms;

import javax.jms.Destination;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.TextMessage;

import com.ibm.msg.client.jms.JmsConnectionFactory;
import com.ibm.msg.client.jms.JmsFactoryFactory;
import com.ibm.msg.client.wmq.WMQConstants;

import ibm.cte.mq.MQConfiguration;
import ibm.cte.mq.MQProducerBase;
import ibm.cte.mq.msg.Inventory;

public class JMSItemProducer extends MQProducerBase {	
	
    public static void main(String[] args) {
		System.out.println("#####################################");
		System.out.println(" Produce item inventory message to MQ ");
    	JMSItemProducer jmsItemProducer = new JMSItemProducer();
    	jmsItemProducer.init(args);
    	Inventory i = jmsItemProducer.loadInventory();	
		jmsItemProducer.run(i);
    }
    
    
    public JMSContext buildJMSContext() throws JMSException {
    	// Create a connection factory
		JmsFactoryFactory ff = JmsFactoryFactory.getInstance(WMQConstants.WMQ_PROVIDER);
		JmsConnectionFactory cf = ff.createConnectionFactory();
		cf.setStringProperty(WMQConstants.WMQ_HOST_NAME, getConfig().getProperties().getProperty(MQConfiguration.MQ_HOST));
		cf.setIntProperty(WMQConstants.WMQ_PORT, Integer.parseInt(getConfig().getProperties().getProperty(MQConfiguration.MQ_PORT)));
		cf.setStringProperty(WMQConstants.WMQ_CHANNEL,getConfig().getProperties().getProperty(MQConfiguration.MQ_CHANNEL));
		cf.setIntProperty(WMQConstants.WMQ_CONNECTION_MODE, WMQConstants.WMQ_CM_CLIENT);
		cf.setStringProperty(WMQConstants.WMQ_QUEUE_MANAGER, getConfig().getProperties().getProperty(MQConfiguration.MQ_QUEUEMANAGER));
		cf.setStringProperty(WMQConstants.WMQ_APPLICATIONNAME, "JmsPutItem");
		cf.setBooleanProperty(WMQConstants.USER_AUTHENTICATION_MQCSP, true);
		cf.setStringProperty(WMQConstants.USERID, getConfig().getProperties().getProperty(MQConfiguration.MQ_USERID));
		cf.setStringProperty(WMQConstants.PASSWORD, getConfig().getProperties().getProperty(MQConfiguration.MQ_PASSWORD));

		// Create JMS objects
		JMSContext context = cf.createContext();
		return context;
    }
    
    /**
     * Process to send the inventory to the queue
     * @param inventory to send to the MQ queue
     */
    public void run(Inventory i) {
		JMSContext context;
		Destination destination = null;
		JMSProducer producer = null;
		try {
			context = buildJMSContext();
			destination = context.createQueue("queue:///" + getConfig().getProperties().getProperty(MQConfiguration.MQ_QUEUENAME));
			TextMessage message = context.createTextMessage(parser.toJson(i));
			producer = context.createProducer();
			producer.send(destination, message);
			recordSuccess();
		} catch (JMSException e) {
			recordFailure(e);
		}
    }
    
    
    /**
	 * Record this run as successful.
	 */
	private void recordSuccess() {
		System.out.println("SUCCESS");
	}
	
    private void recordFailure(Exception ex) {
		if (ex != null) {
			if (ex instanceof JMSException) {
				processJMSException((JMSException) ex);
			} else {
				System.out.println(ex);
			}
		}
		System.out.println("FAILURE");
	}

	/**
	 * Process a JMSException and any associated inner exceptions.
	 *
	 * @param jmsex
	 */
	private void processJMSException(JMSException jmsex) {
		System.out.println(jmsex);
		Throwable innerException = jmsex.getLinkedException();
		if (innerException != null) {
			System.out.println("Inner exception(s):");
		}
		while (innerException != null) {
			System.out.println(innerException);
			innerException = innerException.getCause();
		}
		return;
	}
}