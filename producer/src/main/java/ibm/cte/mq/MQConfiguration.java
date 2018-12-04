package ibm.cte.mq;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;
import java.util.Properties;

import com.ibm.mq.MQException;
import com.ibm.mq.MQQueueManager;
import com.ibm.mq.constants.MQConstants;

/**
 * Define properties to load from config files.
 * 
 * Define factory methods to create Queue manager
 * @author jerome boyer
 *
 */
public class MQConfiguration {
	public static final String MQ_PLATFORM = "mq.platform";
	public static final String MQ_QUEUEMANAGER = "mq.queueManagerName";
	public static final String MQ_QUEUENAME = "mq.queueName";
	public static final String MQ_HOST = "mq.hostname";
	public static final String MQ_PORT = "mq.listenerPort";
	public static final String MQ_CHANNEL = "mq.applicationChannelName";
	public static final String MQ_USERID = "mq.userid";
	public static final String MQ_PASSWORD = "mq.password";
	
	public Properties properties = new Properties();
	
	public void loadProperties() {
		loadPropertiesFromStream(getClass().getClassLoader().getResourceAsStream("config.properties"));
	}
	
	private void loadPropertiesFromStream(InputStream input){
		try {
			properties.load(input);
		} catch (IOException ex) {
			ex.printStackTrace();
			setDefaults();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	public void loadProperties(String fn) {
		try {
			loadPropertiesFromStream(new FileInputStream(fn));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			setDefaults();
		}
	}
	
	private  void setDefaults() {
		properties.setProperty(MQ_PLATFORM,"IBM MQ");
		properties.setProperty(MQ_QUEUEMANAGER,"LQM1");
		properties.setProperty(MQ_QUEUENAME,"REQ.BROWN"); 
		properties.setProperty(MQ_HOST,"localhost");
		properties.setProperty(MQ_PORT,"1414");
		properties.setProperty(MQ_CHANNEL, "CLOUD.APP.SVRCONN");		
		properties.setProperty(MQ_USERID, "admin");
		properties.setProperty(MQ_PASSWORD, "admin01");
	}

	public Properties getProperties() {
		return properties;
	}
	
	public MQQueueManager createQueueManager() throws MQException {
		Hashtable<String, Object> props = new Hashtable<String, Object>();
        props.put(MQConstants.CHANNEL_PROPERTY, getProperties().getProperty(MQConfiguration.MQ_CHANNEL));
      	int integerPortNumber = Integer.parseInt(getProperties().getProperty(MQConfiguration.MQ_PORT));	
        props.put(MQConstants.PORT_PROPERTY, integerPortNumber);
      	props.put(MQConstants.HOST_NAME_PROPERTY, getProperties().getProperty(MQConfiguration.MQ_HOST));
        
        props.put(MQConstants.USER_ID_PROPERTY, getProperties().getProperty(MQConfiguration.MQ_USERID));
        props.put(MQConstants.PASSWORD_PROPERTY, getProperties().getProperty(MQConfiguration.MQ_PASSWORD));
        props.put(MQConstants.USE_MQCSP_AUTHENTICATION_PROPERTY, true);

        MQQueueManager qMgr = new MQQueueManager(getProperties().getProperty(MQConfiguration.MQ_QUEUEMANAGER), props);
        return qMgr;
	}
}