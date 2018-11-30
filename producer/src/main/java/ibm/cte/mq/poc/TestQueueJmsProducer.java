package ibm.cte.mq.poc;

import javax.jms.Destination;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.TextMessage;

import com.google.gson.Gson;
import com.ibm.msg.client.jms.JmsConnectionFactory;
import com.ibm.msg.client.jms.JmsFactoryFactory;
import com.ibm.msg.client.wmq.WMQConstants;

public class TestQueueJmsProducer {
	private static int status = 1;
	private static JMSContext context = null;
	private static Destination destination = null;
	private static JMSProducer producer = null;
	
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

			Inventory i = new Inventory();
			i.setCost(10);
			i.setItemId(12345);
			i.setQuantity(1);
			i.setSite("Site01");
			i.setSupplierId(1);
			Gson parser = new Gson();
			TextMessage message = context.createTextMessage(parser.toJson(i));
			producer = context.createProducer();
			producer.send(destination, message);
			recordSuccess();
    	} catch (JMSException jmsex) {
			recordFailure(jmsex);
		}
    }
    
    /**
	 * Record this run as successful.
	 */
	private static void recordSuccess() {
		System.out.println("SUCCESS");
		status = 0;
		return;
	}
	
    private static void recordFailure(Exception ex) {
		if (ex != null) {
			if (ex instanceof JMSException) {
				processJMSException((JMSException) ex);
			} else {
				System.out.println(ex);
			}
		}
		System.out.println("FAILURE");
		status = -1;
		return;
	}

	/**
	 * Process a JMSException and any associated inner exceptions.
	 *
	 * @param jmsex
	 */
	private static void processJMSException(JMSException jmsex) {
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