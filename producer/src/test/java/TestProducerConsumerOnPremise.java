import ibm.cte.mq.poc.Inventory;
import ibm.cte.mq.poc.InventoryConsumer;
import ibm.cte.mq.poc.ProduceItemMessage;

public class TestProducerConsumerOnPremise {

	
	public static void main(String[] args) {
		
		System.out.println(" 1- Create the consumer thread");
		InventoryConsumer consumer = new InventoryConsumer();
		consumer.configure("src/main/resources/config.properties");
		//Thread t = new Thread(consumer,"Consumer");
		// t.start();
		
		System.out.println(" 2- Create the producer to send one item");
		ProduceItemMessage producer = new ProduceItemMessage();
		producer.configure(new String[] {"src/main/resources/config.properties","data/item1.json"});
		Inventory i = producer.loadInventory();
	
		System.out.println("Sent this item:" + i.toString());
		producer.run(i);
		
		//t.interrupt();
		consumer.run();
	}

}
