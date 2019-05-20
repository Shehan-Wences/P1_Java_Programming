
public class Manager extends User {
	
	public Manager() {
		
				
	}

	public Manager(String userName, String userPassword) {
		
		super(userName,userPassword);
		
	}
	
	public String getOptionList() {
		String list="Menu\n1. Show/Edit All Products\n2. View Stock\n3. Add New Products\n4. Delete Product\n5. Add New User\n\n6.Sign Out";
		return list;
	}
}




