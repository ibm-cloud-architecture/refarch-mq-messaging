package ibm.cte.mq;

import java.io.FileNotFoundException;
import java.io.FileReader;

import com.google.gson.Gson;

import ibm.cte.mq.msg.Inventory;

public class MQProducerBase {
	protected MQConfiguration config = new MQConfiguration();
	protected String itemFileName;
    protected static Gson parser = new Gson();
	
    
    public void init(String[] args) {
		if (args.length == 2) {
            this.getConfig().loadProperties(args[0]);
            this.setItemFileName(args[1]);
        } else {
            // load config.properties from class loader
            this.getConfig().loadProperties();
            this.setItemFileName(args[0]);
        }
		System.out.println("Platform:" + this.getConfig().getProperties().getProperty(MQConfiguration.MQ_PLATFORM));
		System.out.println("Queue Manager:" + this.getConfig().getProperties().getProperty(MQConfiguration.MQ_QUEUEMANAGER));
		System.out.println("Queue Name:" + this.getConfig().getProperties().getProperty(MQConfiguration.MQ_QUEUENAME));
		System.out.println("channel:" + this.getConfig().getProperties().getProperty(MQConfiguration.MQ_CHANNEL));
		System.out.println("hostname:" + this.getConfig().getProperties().getProperty(MQConfiguration.MQ_HOST));
		System.out.println("port:" + this.getConfig().getProperties().getProperty(MQConfiguration.MQ_PORT));
		System.out.println("userid:" + this.getConfig().getProperties().getProperty(MQConfiguration.MQ_USERID));
		System.out.println("password:" + this.getConfig().getProperties().getProperty(MQConfiguration.MQ_PASSWORD));
    }
    
	public MQConfiguration getConfig() {
		return config;
	}

	public String getItemFileName() {
		return itemFileName;
	}

	public void setItemFileName(String itemFileName) {
		this.itemFileName = itemFileName;
	}
	
	public Inventory loadInventory() {
		FileReader fs;
		Inventory i;
		try {
			fs = new FileReader(this.getItemFileName());
			i = parser.fromJson(fs, Inventory.class);
			return i;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			i = new Inventory();
			i.setItemId(new Long(1));
			i.setQuantity(1);
			i.setSite("Site01");
			i.setCost(10);
		}
		return i;
	}

	public static Gson getParser() {
		return parser;
	}
}
