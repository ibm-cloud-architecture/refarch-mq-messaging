package ibm.cte.mq.msg;

/**
 * Inventory element supporting the message structure
 *
 */
public class Inventory {
	
	private long itemId;
	private Integer quantity;
	private String site;
	private long supplierId;
	private double cost;

	public Inventory(){}
	
	public String toString(){
		return "item= "+getItemId()+" for quantity= "+getQuantity()+" @ "+getSite()+" from "+getSupplierId();
	}

	public long getItemId() {
		return itemId;
	}

	public void setItemId(long itemId) {
		this.itemId = itemId;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public String getSite() {
		return site;
	}

	public void setSite(String site) {
		this.site = site;
	}

	public long getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(long supplierId) {
		this.supplierId = supplierId;
	}

	public double getCost() {
		return cost;
	}

	public void setCost(double cost) {
		this.cost = cost;
	}
	
	

}
