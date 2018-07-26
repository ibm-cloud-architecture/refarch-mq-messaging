package ibm.cte.mq.poc;

import java.io.IOException;
import java.util.Hashtable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.mq.MQAsyncStatus;
import com.ibm.mq.MQException;
import com.ibm.mq.MQMessage;
import com.ibm.mq.MQPutMessageOptions;
import com.ibm.mq.MQQueue;
import com.ibm.mq.MQQueueManager;
import com.ibm.mq.constants.MQConstants;

public class ProduceItemMessage {
	final static Logger logger = LoggerFactory.getLogger("ProduceItemMessage");
	private ApplicationConfig config = new ApplicationConfig();
	
	public static void main(String[] args) {

		logger.info("##############################");
		logger.info(" Produce item inventory message to MQ ");
		ProduceItemMessage producer = new ProduceItemMessage();
		producer.getConfig().loadProperties();
		logger.info("hostname:" + producer.getConfig().getProperties().getProperty(ApplicationConfig.MQ_HOST));
		logger.info("port:" + producer.getConfig().getProperties().getProperty(ApplicationConfig.MQ_PORT));
		producer.run();
	}
	
	

	
	public void run() {
		Hashtable<String, Object> props = new Hashtable<String, Object>();
        props.put(MQConstants.CHANNEL_PROPERTY, getConfig().getProperties().getProperty(ApplicationConfig.MQ_CHANNEL));
        props.put(MQConstants.PORT_PROPERTY, getConfig().getProperties().getProperty(ApplicationConfig.MQ_PORT));
        props.put(MQConstants.HOST_NAME_PROPERTY, getConfig().getProperties().getProperty(ApplicationConfig.MQ_HOST));
        String qManager = getConfig().getProperties().getProperty(ApplicationConfig.MQ_QUEUEMANAGER);
        String queueName = "ITEMQ";
        MQQueueManager qMgr = null;
        try {
            qMgr = new MQQueueManager(qManager, props);

            // MQOO_OUTPUT = Open the queue to put messages. The queue is opened for use with subsequent MQPUT calls.
            // MQOO_INPUT_AS_Q_DEF = Open the queue to get messages using the queue-defined default.
            // The queue is opened for use with subsequent MQGET calls. The type of access is either
            // shared or exclusive, depending on the value of the DefInputOpenOption queue attribute.
            int openOptions = MQConstants.MQOO_OUTPUT | MQConstants.MQOO_INPUT_AS_Q_DEF;

            // creating destination
            MQQueue queue = qMgr.accessQueue(queueName, openOptions);

            // specify the message options...
            MQPutMessageOptions pmo = new MQPutMessageOptions(); // default
            // MQPMO_ASYNC_RESPONSE option requests that an MQPUT or MQPUT1 operation
            // is completed without the application waiting for the queue manager to complete the call.
            // Using this option can improve messaging performance
            pmo.options = MQConstants.MQPMO_ASYNC_RESPONSE;

            // create message
            MQMessage message = new MQMessage();
            // MQFMT_STRING = The application message data can be either an SBCS string (single-byte character set),
            // or a DBCS string (double-byte character set). Messages of this format can be converted
            // if the MQGMO_CONVERT option is specified on the MQGET call.
            message.format = MQConstants.MQFMT_STRING;
            message.writeString("<message>Hello</message>");
            queue.put(message, pmo);
            queue.close();

            MQAsyncStatus asyncStatus = qMgr.getAsyncStatus();
            logger.info("Result " + asyncStatus.completionCode);
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




	public ApplicationConfig getConfig() {
		return config;
	}

}
