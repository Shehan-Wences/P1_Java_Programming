import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class StockManagementSystem {

	// variable is declared to hold the maximum number of products that can be
	// entered in the system
	private int maxNumberproduct;
	// variable is declared for the current number of products already in the system
	private int currentNumberproduct;
	// array from Product class is declared which will hold the products
	private Product[] productList;
	// variable is declared for the current number of users already in the system
	private int currentUserNumber;
	// array from User class is declared which will hold the users
	private User[] userList;
	// logged in user is stored here
	private User currentuser;

	// constructor of the StockManagementSystem Class
	// variables are initialized in here
	public StockManagementSystem(int maxNumberproduct) {

		this.maxNumberproduct = maxNumberproduct;
		// current number of products is 0 at the beginning of the program
		// products will be loaded after using loadData()
		this.currentNumberproduct = 0;
		this.currentUserNumber = 0;

		// productList array is initialized by declaring the size of the array
		this.productList = new Product[this.maxNumberproduct];
		// userList array is initialized by declaring the size of the array
		this.userList = new User[10];
		// see the method for details
		loadData();
		userDataLoad();
		// see the method for details
		showLogin();
		return;
	}

	// reads data from users.csv file line by line and enters them into userList
	// Array
	public void userDataLoad() {

		String line = "";

		try (BufferedReader br = new BufferedReader(new FileReader("users.csv"))) {

			// reading all the lines from csv file
			// loop continues till there are lines
			while ((line = br.readLine()) != null) {

				// each comma separate values and they are added to a temporary userArray
				String[] userArray = line.split(",");

				if(userArray[0].equals("manager")) {
				// adding value from the temporary array to the usertList array
				userList[this.currentUserNumber] = new Manager(userArray[1], userArray[2]);

				// increasing current product number with each iteration
				
				}else if (userArray[0].equals("warehousestaff")) {
					userList[this.currentUserNumber] = new WarehouseStaff(userArray[1], userArray[2]);
				}
				this.currentUserNumber++;
			}
			// file wrtitting or reading exceptions are caught here
		} catch (IOException e) {
			e.printStackTrace();
		}
		return;
	}

	// reads data from csv file line by line and enters them into productList Array
	public void loadData() {

		String line = "";

		try (BufferedReader br = new BufferedReader(new FileReader("products.csv"))) {

			// reading all the lines from csv file
			// loop continues till there are lines
			while ((line = br.readLine()) != null) {

				// each comma separate values and they are added to a temporary productArray
				String[] productArray = line.split(",");

				// adding value from the temporary array to the productList array
				productList[this.currentNumberproduct] = new Product(productArray[0], productArray[1],
						Double.parseDouble(productArray[2]), Integer.parseInt(productArray[3]));

				// increasing current product number with each iteration
				this.currentNumberproduct++;

			}
			// file wrtitting or reading exceptions are caught here
		} catch (IOException e) {
			e.printStackTrace();
		}
		return;
	}

	// this method writes productList array to the csv file
	public void writeData() {
		BufferedWriter bw = null;
		//
		try {

			// Csv file path
			File file = new File("products.csv");

			// checking if the csv file already exists,if not creating a new one.
			if (!file.exists()) {
				file.createNewFile();
			}

			// creating an instance of BufferedWriter class
			bw = new BufferedWriter(new FileWriter(file));

			int b = 0;

			// writing all the values in productList array one by one
			while (b < this.currentNumberproduct) {
				bw.write("" + productList[b].getproductCode() + "," + productList[b].getproductName() + ","
						+ productList[b].getproductPrice() + "," + productList[b].getproductStock() + "");
				// writes a new line in csv file
				bw.newLine();
				b++;

			}

		} catch (IOException ioe) {
			// writing or reading to file exception will be caught here

			ioe.printStackTrace();
		} finally {
			try {
				// if there is a connection close it.
				if (bw != null)
					bw.close();
			} catch (Exception ex) {
				System.out.println("Error in closing the BufferedWriter" + ex);
			}
		}
		return;
	}

	// this method shows login menu and validates the entry before showing the main
	// menu
	public void showLogin() {

		// launching JOptionPane to input login details and assigning the input to a
		// string
		// used to store values taken from Joption panes
		String loginusername = "";
		String loginuserpassword = "";

		// boolean status is declared so that it can be used to identify if the username
		// and password is verified
		boolean status = false;
		// number of attempts allowed to enter username and password
		// it will be decremented in the loop
		int attempt = 3;
		// this loop fails if attempts reach 0, status == true which means login is
		// successful,
		// user has cancelled any of the inputdilog boxes
		while (attempt > 0 && status == false && loginusername != null && loginuserpassword != null) {
			attempt--;

			// input dialog boxes are shown and their values are stored in seperate strings
			loginusername = JOptionPane.showInputDialog("Enter Username");
			loginuserpassword = JOptionPane.showInputDialog("Enter Password");

			// this is condition passes the username and password to a method
			// if they match a entry in the array method return true
			if (validateLogin(loginusername, loginuserpassword) == true) {
				// Login success message is shown
				JOptionPane.showMessageDialog(null, "Logged in as "+currentuser.getuserName()+"", "Success", JOptionPane.INFORMATION_MESSAGE);
				// Main menu is showed
				showMainMenu();
				// boolean value of status is changed so that the loop will stop
				status = true;

			} else {
				// if login wasn't successful error is displayed
				JOptionPane.showMessageDialog(null,
						"Username/Password is incorrect (" + attempt + "attempt's remaining)", "Error",
						JOptionPane.ERROR_MESSAGE);

			}

		}
		// if logins were not successful and attempt reach the value of 0 error is
		// displayed.
		if (attempt == 0) {

			JOptionPane.showMessageDialog(null, "Contact Administrator for help", "Error", JOptionPane.ERROR_MESSAGE);
		}
		return;
	}

	// this boolean method return true is the username and password value received
	// matches a entry on the userList array
	public boolean validateLogin(String username, String password) {

		// boolean found is declared false here
		boolean found = false;

		int a = 0;
		// iterates through the userList Array
		while (a < this.currentUserNumber) {

			// if matching values are found, boolean found is changed to true
			if (userList[a].getuserName().equals(username) && userList[a].getuserPassword().equals(password)) {
				found = true;
				this.currentuser=userList[a];
			}
			a++;
		}

		return found;
	}

	// this method shows the main menu and takes user input to navigate through menu
	public void showMainMenu() {

		// creating the menu list string
		String mainmenuoptions ="";
		if (currentuser instanceof Manager) {
			mainmenuoptions = "** Logged in as "+currentuser.getuserName()+" **\n"+new Manager().getOptionList();
		}else if (currentuser instanceof WarehouseStaff) {
			mainmenuoptions = "** Logged in as "+currentuser.getuserName()+" **\n"+new WarehouseStaff().getOptionList();
		}
		
		// launching JOptionPane and assigning the input to a string
		String mainmenuchoice = JOptionPane.showInputDialog(mainmenuoptions);

		// loops runs if main menu choice not null (becomes null if only cancel is
		// selected)
		while (mainmenuchoice != null ) {

			// try catch is used to catch number format exception that might occur when
			// parsing the below string to integer
			// i think it is a good way to identify if a valid option is entered
			// specifically a number
			try {
				// converting the above mentioned string to an integer
				int mainmenuchoiceinteger = Integer.parseInt(mainmenuchoice);

				if (mainmenuchoiceinteger == 1 && currentuser instanceof Manager) {
					// see the method for more details
					showAllProducts();

				} else if (mainmenuchoiceinteger == 2 ) {

					// see the method for more details
					showStock();

				} else if (mainmenuchoiceinteger == 3 && currentuser instanceof Manager) {

					// see the method for more details
					addProducts();

				} else if (mainmenuchoiceinteger == 4 && currentuser instanceof Manager) {

					// see the method for more details
					

				} else if (mainmenuchoiceinteger == 5 && currentuser instanceof Manager) {

					// see the method for more details
					

				} else {
					// if number except 1,2,3 is entered an error message is displayed
					JOptionPane.showMessageDialog(null, "Please enter a valid option", "Error",
							JOptionPane.ERROR_MESSAGE);
				}

				mainmenuchoice = JOptionPane.showInputDialog(mainmenuoptions);
				// Anything other than number is inserted below message is displayed
			} catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(null, "Please enter a valid option", "Error", JOptionPane.ERROR_MESSAGE);
				mainmenuchoice = JOptionPane.showInputDialog(mainmenuoptions);
			}
		}
		return;

	}

	// this method display all the products and accepts user feedback to go back to
	// main menu or edit products
	public void showAllProducts() {

		boolean status = true;
		int a = 0;
		// op string is created top menu is added here
		String op = "\tAll Products\nCode\tName\tPrice";

		// this loops iterates through productList array
		while (a < currentNumberproduct) {

			// with the use of while loop products are listed one after the other vertically
			// with its information horizontally
			// by adding it to the op string
			op = op + "\n" + this.productList[a].getproductCode() + "\t" + this.productList[a].getproductName() + "\t"
					+ this.productList[a].getproductPrice() + " ";

			a++;
		}

		// adding the rest of the menu to the same op string
		op = op + "\n\n1. Edit\n\nClick Cancel to go back";

		// JTestArea is used to display the op string because otherwise TABS separating
		// text will not appear
		String inputValue = JOptionPane.showInputDialog(new JTextArea(op));

		// running a loop which fails if there is value for the above mentioned string
		while (inputValue != null && status == true) {

			// try catch is used to catch number format exception that might occur when
			// parsing the below string to integer
			// i think it is a good way to identify if a valid option is entered
			// specifically a number
			try {
				// converting the above mentioned string to an integer
				int editinteger = Integer.parseInt(inputValue);

				// in the value is 1 editProduct method is called
				if (editinteger == 1) {
					// please see the specific method
					editProduct();
					status = false;
				} else {
					// if the exception is occurred the error message is displayed below
					// error is displayed if anything other than 1 is entered
					JOptionPane.showMessageDialog(null, "Please enter a valid option", "Error",
							JOptionPane.ERROR_MESSAGE);
					inputValue = JOptionPane.showInputDialog(new JTextArea(op));

				}

			} catch (NumberFormatException e) {
				// if non-numeric value is entered error message is thrown
				JOptionPane.showMessageDialog(null, "Please enter a valid option", "Error", JOptionPane.ERROR_MESSAGE);
				// and input dialog box is shown again
				inputValue = JOptionPane.showInputDialog(new JTextArea(op));
			}

		}

		return;
	}

	// this method is used to display product code,name and stock
	public void showStock() {

		boolean menustatus = true;
		int a = 0;
		// op string is used to create the final displaying data..
		// here top menu is added
		String op = "\tAll Products\nCode\tName\tStock";

		// this loops iterates through productList array
		while (a < currentNumberproduct) {

			// with the use of while loop products are listed one after the other vertically
			// with its information horizontally
			// this is done by adding data to the same op string in each iteration
			op = op + "\n" + this.productList[a].getproductCode() + "\t" + this.productList[a].getproductName() + "\t"
					+ this.productList[a].getproductStock() + " ";

			a++;
		}

		if(currentuser instanceof Manager) {
			op = op + "\n\n\nClick Cancel to go back";
		}else {
		// at last bottom menu is added to the same op string
			op = op + "\n\n1. Update\n\nClick Cancel to go back";
		}
		// JTestArea is used to display the op string because otherwise TABS separating
		// text will not appear
		String inputValue = JOptionPane.showInputDialog(new JTextArea(op));

		// running a loop which fails if there is a value for the above mentioned string
		// running a loop which fails if there is value for the above mentioned string
		while (inputValue != null && menustatus == true) {

			// try catch is used to catch number format exception that might occur when
			// parsing the below string to integer
			// i think it is a good way to identify if a valid option is entered
			// specifically a number
			try {
				// converting the above mentioned string to an integer
				int editinteger = Integer.parseInt(inputValue);

				// in the value is 1 updateStock method is called
				if (editinteger == 1 && currentuser instanceof WarehouseStaff) {
					// please see the specific method
					menustatus = false;
					updateStock();
				} else {
					// error is displayed if anything other than 1 is entered
					JOptionPane.showMessageDialog(null, "Please enter a valid option", "Error",
							JOptionPane.ERROR_MESSAGE);
					inputValue = JOptionPane.showInputDialog(new JTextArea(op));
				}

				// if the exception is occurred the error message is displayed below
			} catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(null, "Please enter a valid option", "Error", JOptionPane.ERROR_MESSAGE);
				inputValue = JOptionPane.showInputDialog(new JTextArea(op));
			}

		}

		return;
	}

	// this method is used to add to products to productList array and the entire
	// array is re-written to the csv file
	public void addProducts() {

		// String status is established to main proper flow of input dialog will is
		// explained further down
		String status = "";
		// String input store the product name entered by user
		String name = "";
		// String code store the product code entered by user
		String code = "";
		// String prince is used to store the price of product temporarily until
		// checked to make sure its a double
		String pricestring = "";
		// DOuble value of the above string is store here
		double price = 0;

		// show the dialog to input product code and store the value in code string
		code = JOptionPane.showInputDialog("Enter Product Code");

		// i have used three While loops here because i wanted the input dialog boxes to
		// run independently, and validating is much easier this way
		// the first while loop displays input code dialog box till it is either
		// "Cancelled" or a proper code is entered
		// if the second condition was not there while will show the dialog box again
		// even if a valid input is entered
		// this would have been okay for the main menu but doesn't suit this situation
		while (code != null && status != "codepass") {

			// here i have passed the value of code to searchProduct method which will
			// return product "code" if there is or it will return "nothing" otherwise
			if (!searchProduct(code).trim().equals("nothing")) {
				// Error message will be displayed if the code matches returned value from the
				// method matches "nothing"
				// input dialog box is shown again after that to take input again
				JOptionPane.showMessageDialog(null, "Product Code Already Exists", "Error", JOptionPane.ERROR_MESSAGE);
				code = JOptionPane.showInputDialog("Enter Product Code");

				// here i check if the entered value is an empty string
			} else if (code.trim().equals("")) {
				// if the string is empty error is displayed and input dialog box is shown again
				JOptionPane.showMessageDialog(null, "Code can not be empty", "Error", JOptionPane.ERROR_MESSAGE);
				code = JOptionPane.showInputDialog("Enter Product Code");

			} else {

				// if there are no error meaning nothing is caught above
				// next input dialog box is shown to get the name
				name = JOptionPane.showInputDialog("Enter Product Name");
				// at the same time "codepass" value is assigned to status string which will be
				// useful to stop the while loop
				status = "codepass";
			}
		}
		// the second while loop is used to show input dialog box for product name
		// this loop will fail if user Cancels the dialog box or
		// value of status is "namepass" or value of status is not equal to "codepass"
		// if the value of status not equal to "codepass" means that there is no value
		// for code which mean the above while loop was cancelled
		// and the value of status equal to "namepass" means correct value is entered as
		// the name

		while (name != null && status != "namepass" && status == "codepass") {

			// checking if the name is not empty
			if (!name.trim().equals("")) {

				// if the name is not empty, status value is changed and input dialog box is
				// showed to get price of the product
				status = "namepass";
				pricestring = JOptionPane.showInputDialog("Enter Product Price");

			} else {
				// error message will be displayed and input to dialog box to get name again is
				// displayed
				JOptionPane.showMessageDialog(null, "Name can not be empty", "Error", JOptionPane.ERROR_MESSAGE);
				name = JOptionPane.showInputDialog("Enter Product Name");
			}
		}

		// the last while loop is used to show price input dialog box
		// similar to the above while loop
		// the status value is checked to see if it equals "namepass" this means there
		// is a value for name
		// other wise the loop will fails
		// loop will also fail if dialog box is cancelled or if user enters a correct
		// price
		// because the status value is changed to pricepass then

		while (pricestring != null && status != "pricepass" && status == "namepass") {

			// try catch is used to catch the number format exception that might occur when
			// converting the input price string to a double
			// this will help validate the price
			try {

				// converting the string to a double
				price = Double.parseDouble(pricestring);
				// if the above fails exception is thrown so if the program make this far it
				// mean exception is not thrown
				// status string value is changed to pricepass for the purpose of breaking the
				// while loop and other reasons which can be found further down
				status = "pricepass";

			} catch (Exception e) {
				// Exception is caught and error is displayed
				JOptionPane.showMessageDialog(null, "Enter a valid price", "Error", JOptionPane.ERROR_MESSAGE);
				// input dialog box is showed to get the price again
				pricestring = JOptionPane.showInputDialog("Enter Product Price");
			}

		}

		// if the string value of status is pricepass that means there are valid values
		// for code.name,price
		if (status.equals("pricepass")) {
			// adding the new product // to the productList array
			productList[this.currentNumberproduct] = new Product(code, name, price, 0);
			// incrementing the current number of the product
			this.currentNumberproduct++;
			// the method is called where array is re-written to csv file
			writeData();
			// message is showed to to acknowledge user
			JOptionPane.showMessageDialog(null, "Product Successfully Added", "Success",
					JOptionPane.INFORMATION_MESSAGE);

		} else {
			// message is shown is status is not equal to pricespass
			// this means user has cancelled at anypoint during the above dialog boxes
			JOptionPane.showMessageDialog(null, "Product Not Added, Cancelled by User", "Error",
					JOptionPane.INFORMATION_MESSAGE);

		}

		return;

	}

	public void updateStock() {

		// this int is used to store index of productList array of the product code
		// entered by user
		int productID = -1;
		// this string is used to store amount of stock
		String amount = "";
		// status string is declared to used to stop while loops when necessary
		String status = "";

		String code = JOptionPane.showInputDialog("Enter Product Code");

		// this while fails if user cancels the InputDialog box which requests the code
		// of the product you want to updatestock
		// or user inputs the valid code to work with
		while (code != null && status != "codepass") {

			// value of code is trimmed to get rid of whitespaces and passed to
			// searchProduct method to check if a product exists in that name
			// if it returns "nothing" that means there are not any
			if (searchProduct(code).trim().equals("nothing")) {

				// Error message is displayed there are not any matching products
				JOptionPane.showMessageDialog(null, "Product does not exist", "Error", JOptionPane.ERROR_MESSAGE);
				// input dialog is shown again to get user input
				code = JOptionPane.showInputDialog("Enter Product Code");

			} else {
				// if the program makes here it means a valid code is entered
				// Integer value of string returned from searchProduct method is taken here
				// that is store in ProductID
				productID = Integer.parseInt(searchProduct(code));
				// next menu is showed to enter the amount of stock
				amount = JOptionPane
						.showInputDialog("Enter the amount\n *Note that the above amount will be added to the stock*");
				// status is changed to codepass
				// so while loop is stopped
				status = "codepass";

			}

		}

		// this while loop fails if user cancels the dialog box or status is not equals
		// to codepass which means there is no valid code
		while (amount != null && status == "codepass" && status != "stockupdated") {

			// try catch is used to validate non-numeric values entered as stock
			try {

				// the stock added can not be less than
				if (Integer.parseInt(amount) < 1) {
					// if it is error message will be displayed
					JOptionPane.showMessageDialog(null, "Stock added should be more than 0", "Error",
							JOptionPane.ERROR_MESSAGE);
					// and input dialog is displayed again to input stock amount
					amount = JOptionPane.showInputDialog(
							"Enter the amount\n *Note that the above amount will be added to the stock*");

				} else {
					// if the program makes it this far it means there is a valid stock number
					// the stock is added to the existing stock
					int stockamount = productList[productID].getproductStock();
					stockamount = stockamount + Integer.parseInt(amount);
					productList[productID].setproductStock(stockamount);
					// status is updated to stop the loop
					status = "stockupdated";
					// success message is shown
					JOptionPane.showMessageDialog(null, "Product Stock Successfully Updated", "Success",
							JOptionPane.INFORMATION_MESSAGE);

				}

			} catch (NumberFormatException e) {
				// when non numeric values are entered error message will be displayed
				JOptionPane.showMessageDialog(null, "Enter a valid stock number", "Error", JOptionPane.ERROR_MESSAGE);
				// and input dialog is displayed again to input stock amount
				amount = JOptionPane
						.showInputDialog("Enter the amount\n *Note that the above amount will be added to the stock*");
			}
		}

		// showStock method is called to updated values and writeData method is called
		// to write update productList array to csv
		showStock();
		writeData();
		return;

	}

	// this method is used to edit product's code, name and price
	public void editProduct() {

		// string code is used to store user input
		String code = JOptionPane.showInputDialog("Enter Product Code");
		// as done previously on a method this status string is updated on a successful
		// entry and is used to stop the while
		// this will ensure that multiple unwanted gui windows being opened
		String status = "";
		// productID integer is declared
		int productID = -1;
		// editchoice string is the user input for second while loop which decides what
		// user wants to edit
		// ID,Name or Price
		String editchoice = "";
		// user input for a newCode is stored here
		String newCode = "";
		// user input for a newName is stored here
		String newName = "";
		// user input for a newPrice is stored here
		String newPrice = "";

		// this while fails if user cancels the InputDialog box which requests the code
		// of the product you want to edit
		// or user inputs the valid code to work with
		while (code != null && status != "codepass") {

			// value of code is trimmed to get rid of whitespaces and passed to
			// searchProduct method to check if a product exists in that name
			// if it returns "nothing" that means there arent any
			if (searchProduct(code).trim().equals("nothing")) {

				// Error message is displayed there arent any matching products
				JOptionPane.showMessageDialog(null, "Product does not exist", "Error", JOptionPane.ERROR_MESSAGE);
				// input dialog is shown again to get user input
				code = JOptionPane.showInputDialog("Enter Product Code");

			} else {
				// if the program makes here it means a valid code is entered
				// Integer value of string returned from searchProduct method is taken here
				// that is store in ProductID
				productID = Integer.parseInt(searchProduct(code));
				// next menu is showed to for user to choose what he wants to change about that
				// product
				editchoice = JOptionPane.showInputDialog("\"Edit Menu\n1. Edit Code\n2. Edit Name\n3. Edit Price");
				// status is changed to codepass
				// so while loop is stopped
				status = "codepass";

			}

		}
		// this while loop fails if user cancels the input dialogbox
		// or status string is not equal to "codepass" which means for this loop to run
		// the previous loop has to have a valid entry
		// or this loop fails if status has the value "pass" which means user has
		// successfully edited code,name or price
		while (editchoice != null && status == "codepass" && status != "pass") {

			// try catch is used to catch invalid entries specially non-numeric values
			try {
				// editchoice is converted to int and stored in editinteger
				int editinteger = Integer.parseInt(editchoice);

				// if input for the previous dialogbox is 1 user has chosed to edit code
				// dialog box to edit code is displayed here
				if (editinteger == 1) {

					newCode = JOptionPane.showInputDialog("Enter New Code");

					// input value is trimmed and checked if its null
					if (newCode.trim().equals("")) {
						// if its null error message is displayed
						JOptionPane.showMessageDialog(null, "Can not be empty", "Error", JOptionPane.ERROR_MESSAGE);
						// user will be returned to edit menu to choose to edit code , name or price
						// again
						editchoice = JOptionPane
								.showInputDialog("\"Edit Menu\n1. Edit Code\n2. Edit Name\n3. Edit Price");

					} else if (!searchProduct(newCode).trim().equals("nothing")) {

						// code value is trimmed and passed to searchProduct method to check if the code
						// already exists
						// if it exists the index is returned otherwise "nothing" is returned
						// so if returned value does not equals to nothing it means product already
						// exists
						// error is displayed to indicate that to the user
						JOptionPane.showMessageDialog(null, "Product Code Already Exists", "Error",
								JOptionPane.ERROR_MESSAGE);
						// user will be returned to edit menu to choose to edit code , name or price
						// again
						editchoice = JOptionPane
								.showInputDialog("\"Edit Menu\n1. Edit Code\n2. Edit Name\n3. Edit Price");

					} else {
						// if program has made it this far it means there is a proper value for the code
						// so success message is displayed
						JOptionPane.showMessageDialog(null, "Product Code Successfully Updated", "Success",
								JOptionPane.INFORMATION_MESSAGE);
						// new product code is updated to the correct array index of productList
						productList[productID].setproductCode(newCode.trim());
						// status is update to pass so that loop fails , later in this method
						// showallproducts method is called to display updated values
						// writecode method is called to write the array to csv file,later in this
						// method
						status = "pass";
					}

					// dialog box to edit name is displayed here if the user entered 2 in edit menu
				} else if (editinteger == 2) {

					newName = JOptionPane.showInputDialog("Enter New Name");

					// user input newName is trimmed and checked if it equals to null
					if (newName.trim().equals("")) {
						// if so error is displayed
						JOptionPane.showMessageDialog(null, "Can not be empty", "Error", JOptionPane.ERROR_MESSAGE);
						// and user is returned back to edit menu to choose an edit option again
						editchoice = JOptionPane
								.showInputDialog("\"Edit Menu\n1. Edit Code\n2. Edit Name\n3. Edit Price");
					} else {
						// if programme makes it this far it means there is a valid name for the product
						// success message is shown
						JOptionPane.showMessageDialog(null, "Product Name Successfully Updated", "Success",
								JOptionPane.INFORMATION_MESSAGE);
						// newName is updated to the correct prductindex on productList array
						productList[productID].setproductName(newName.trim());
						status = "pass";
						// status is update to pass so that loop fails , later in this method
						// showallproducts method is called to display updated values
						// writecode method is called to write the array to csv file,later in this
						// method
					}

					// dialog box to edit price is displayed here if the user entered 3 in edit menu
				} else if (editinteger == 3) {

					newPrice = JOptionPane.showInputDialog("Enter New Price");

					// if price is equal to null error is displayed and user is returned to Edit
					// menu to choose again
					if (newPrice.trim().equals("")) {

						JOptionPane.showMessageDialog(null, "Can not be empty", "Error", JOptionPane.ERROR_MESSAGE);
						editchoice = JOptionPane
								.showInputDialog("\"Edit Menu\n1. Edit Code\n2. Edit Name\n3. Edit Price");

					} else {
						// if the program makes this far it means correct value is entered as price
						// if not an exception is thrown which will be caught
						// the success message is shown
						JOptionPane.showMessageDialog(null, "Product Price Successfully Updated", "Success",
								JOptionPane.INFORMATION_MESSAGE);
						// newPrice is updated in the productList array under the correct index
						productList[productID].setproductPrice(Integer.parseInt(newPrice.trim()));
						// status is update to pass so that loop fails , later in this method
						// showallproducts method is called to display updated values
						// writecode method is called to write the array to csv file,later in this
						// method
						status = "pass";
					}

				} else {
					// program will run this if user entered any other number except for 1,2 or 3
					// error will be displayed
					// and user will be displayed the edit menu again
					JOptionPane.showMessageDialog(null, "Enter a valid option", "Error", JOptionPane.ERROR_MESSAGE);
					editchoice = JOptionPane.showInputDialog("\"Edit Menu\n1. Edit Code\n2. Edit Name\n3. Edit Price");

				}

			} catch (NumberFormatException e) {
				// this is used to catch specially number format exceptions that might occur
				// error will be displayed
				// and user will be displayed the edit menu again
				JOptionPane.showMessageDialog(null, "Enter a valid option", "Error", JOptionPane.ERROR_MESSAGE);
				editchoice = JOptionPane.showInputDialog("\"Edit Menu\n1. Edit Code\n2. Edit Name\n3. Edit Price");
			}

		}

		// writedata method is called to write data from productList array to csv file
		writeData();
		// this method is called to show update value
		showAllProducts();

		return;

	}

	// this method takes a string value searches the array against products codes
	// and returns the "nothing" if there isn't a matching product code
	// if there is a matching code array index of the code is returned as a string
	public String searchProduct(String code) {
		// string productID is declared as nothing so if there are no matching codes i
		// can use that to validate some if statements later in some methods
		String productID = "nothing";

		int a = 0;
		// while loops iterates through the productList array
		while (a < this.currentNumberproduct) {

			// if a match is found the index is saved to the productID string
			if (code.equals(productList[a].getproductCode())) {
				productID = "" + a + "";

			}
			a++;
		}
		// returns the productID string
		return productID;
	}

	public static void main(String[] args) {

		// creating a new object from the StockManagementSystem class and parsing 50
		// which will be used to initialize the array with a size in the constructor
		StockManagementSystem obj = new StockManagementSystem(50);
		return;

	}

}
