
abstract public class  User {

	// used to store User name
	private String userName;
	// used to store User's password
	private String userPassword;

	public User() {
		
	}
	// constructor of User class
	public User(String userName, String userPassword) {
		// values from arguments are store in above declares strings
		this.userName = userName;
		this.userPassword = userPassword;
	}
	
	abstract public String getOptionList();

	public String getuserName() {
		return this.userName;
	}
	public String getuserPassword() {
		return this.userPassword;
	}
	public void setuserName(String userName) {
		this.userName=userName;
	}
	public void setuserPassword(String userPassword) {
		this.userPassword=userPassword;
	}
	
}
