package domain;

import java.sql.Timestamp;

public class Account {
	private String username;
	private String password;
	private String email;
	private String firstName;
	private String lastName;
	private Timestamp updatetime;
	private String flag;
	
	public Account(String user, String pass, String mail, String first, String last, Timestamp time, String flag){
		this.username = user;
		this.password = pass;
		this.email = mail;
		this.firstName = first;
		this.lastName = last;
		this.updatetime = time;
		this.flag = flag;
	}
	
	public Account(){
		
	}
	
	
	
	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getFirstName() {
		return firstName;
	}

	public Timestamp getUpdatetime() {
		return updatetime;
	}

	public void setUpdatetime(Timestamp updatetime) {
		this.updatetime = updatetime;
	}

	public String getPassword() {
		return password;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	//This is to compare password input from user with password from domain. 
	//@return: 1 = correct password, 0 = incorrect password, -1 = error. Will set up logging, or another way to pass error back at some stage.
	public int checkPassword(String newPassword){
		try{
		if (password.equals(newPassword)){
			return 1;
		} else {
			return 0;
		}
		} catch (Exception e){
			//Put a log in
			return 3;
		}
	
	}

	@Override
	public String toString() {
		return "Account [username=" + username + ", password=" + password
				+ ", email=" + email + ", firstName=" + firstName
				+ ", lastName=" + lastName + ", updatetime=" + updatetime
				+ ", flag=" + flag + "]";
	}
	
	
	
	

}
