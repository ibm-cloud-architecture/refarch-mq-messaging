package ibm.cte.mq.poc;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ApplicationConfig {
	public static final String MQ_PLATFORM = "mq.platform";
	public static final String MQ_QUEUEMANAGER = "mq.queueManagerName";
	public static final String MQ_HOST = "mq.hostname";
	public static final String MQ_PORT = "mq.listenerPort";
	public static final String MQ_CHANNEL = "mq.applicationChannelName";
	
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
		properties.setProperty(MQ_PLATFORM,"ibmmq-on-cloud");
		properties.setProperty(MQ_QUEUEMANAGER,"BrownMqOCqmgr");
		properties.setProperty(MQ_HOST,"brownmqocqmgr-46e0.qm.us-south.mqcloud.ibm.com");
		properties.setProperty(MQ_PORT,"30525");
		properties.setProperty(MQ_CHANNEL, "CLOUD.APP.SVRCONN");
	}

	public Properties getProperties() {
		return properties;
	}
}
