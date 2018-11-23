import inventory.ws.DALService;
import inventory.ws.DALServiceService;

public class TestInventoryWS {
	static DALService serv;
	
	public TestInventoryWS() {}
	
    public static void main(String[] args) {
        URL dalUrl = new URL("http://dal.brown.case/inventory/ws?wsdl");;
		serv= (new DALServiceService(dalUrl)).getDALServicePort();
    }
}