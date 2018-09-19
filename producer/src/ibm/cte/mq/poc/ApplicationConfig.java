package ibm.cte.mq.poc;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ApplicationConfig {
	public static final String MQ_PLATFORM = "mq.platform";
	public static final String MQ_QUEUEMANAGER = "mq.queueManagerName";
	public static final String MQ_QUEUENAME = "mq.queueName";
	public static final String MQ_HOST = "mq.hostname";
	public static final String MQ_PORT = "mq.listenerPort";
	public static final String MQ_CHANNEL = "mq.applicationChannelName";
	public static final String MQ_USERID = "mq.userid";
	public static final String MQ_PASSWORD = "mq.password";
	
	public Properties properties;
	
	public void loadProperties() {
		properties= new Properties();
		InputStream input = null;
		try {
			input = getClass().getClassLoader().getResourceAsStream("config.properties");
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
	
	private  void setDefaults() {
//		properties.setProperty(MQ_PLATFORM,"ibmmq-on-cloud");
		properties.setProperty(MQ_PLATFORM,"IBM MQ on Cloud");

//		properties.setProperty(MQ_QUEUEMANAGER,"BrownMqOCqmgr");
		properties.setProperty(MQ_QUEUEMANAGER,"QM_orc"); // on Cloud
		properties.setProperty(MQ_QUEUEMANAGER,"QM_MDB");
		
		properties.setProperty(MQ_QUEUENAME,"DEV.QUEUE.3"); 

//		properties.setProperty(MQ_HOST,"brownmqocqmgr-46e0.qm.us-south.mqcloud.ibm.com");
		properties.setProperty(MQ_HOST,"qm-orc-6bb6.qm.us-south.mqcloud.ibm.com");
//		properties.setProperty(MQ_HOST,"localhost");

//		properties.setProperty(MQ_PORT,"30525");
		properties.setProperty(MQ_PORT,"30073"); //QM_orc
//		properties.setProperty(MQ_PORT,"1420");
		
//		properties.setProperty(MQ_CHANNEL, "CLOUD.APP.SVRCONN");
//		properties.setProperty(MQ_CHANNEL, "CLOUD.ADMIN.SVRCONN");
		properties.setProperty(MQ_CHANNEL, "SYSTEM.DEF.SVRCONN");
		
		properties.setProperty(MQ_USERID, "owencOPPS");
		
		properties.setProperty(MQ_PASSWORD, "VPQDJz25o_M-Ti9Xfdu9WI9DKVj2xyHotWl0L9vOTZQ_");
	}

	public Properties getProperties() {
		return properties;
	}
}