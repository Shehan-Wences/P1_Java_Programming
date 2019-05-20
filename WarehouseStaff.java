
public class WarehouseStaff extends User {
	
	public WarehouseStaff() {
		
				
	}

	public WarehouseStaff(String userName, String userPassword) {
		
		super(userName,userPassword);
		
	}
	
	public String getOptionList() {
		String list="Menu\n2. View/Update Stock\n\n6.Sign Out";
		return list;
	}
}
