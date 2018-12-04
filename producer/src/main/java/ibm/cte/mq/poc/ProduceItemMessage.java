package ibm.cte.mq.poc;

import java.io.IOException;

import com.ibm.mq.MQAsyncStatus;
import com.ibm.mq.MQException;
import com.ibm.mq.MQMessage;
import com.ibm.mq.MQPutMessageOptions;
import com.ibm.mq.MQQueue;
import com.ibm.mq.MQQueueManager;
import com.ibm.mq.constants.MQConstants;

import ibm.cte.mq.MQConfiguration;
import ibm.cte.mq.MQProducerBase;
import ibm.cte.mq.msg.Inventory;

public class ProduceItemMessage extends MQProducerBase{

    
	public static void main(String[] args) {

		System.out.println("#####################################");
		System.out.println(" Produce item inventory message to MQ ");
        ProduceItemMessage producer = new ProduceItemMessage();
        producer.init(args);
        Inventory i = producer.loadInventory();
		producer.run(i);
	}
	
	
	public void run(Inventory i) {

        MQQueueManager qMgr = null;
        try {
            qMgr = getConfig().createQueueManager();
            
            // MQOO_OUTPUT = Open the queue to put messages. The queue is opened for use with subsequent MQPUT calls.
            // MQOO_INPUT_AS_Q_DEF = Open the queue to get messages using the queue-defined default.
            // The queue is opened for use with subsequent MQGET calls. The type of access is either
            // shared or exclusive, depending on the value of the DefInputOpenOption queue attribute.
            int openOptions = MQConstants.MQOO_OUTPUT | MQConstants.MQOO_INPUT_AS_Q_DEF;

            // creating destination
            MQQueue queue = qMgr.accessQueue(getConfig().getProperties().getProperty(MQConfiguration.MQ_QUEUENAME), 
            		openOptions);

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
            message.writeString(parser.toJson(i));
            queue.put(message, pmo);
            queue.close();

            MQAsyncStatus asyncStatus = qMgr.getAsyncStatus();
            System.out.println("Result " + asyncStatus.completionCode);
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
}