/**
* AccountDAO
* Interface to show methods and parameters to be used for interacting with account table in DB
* @peerReview
* 24/04/2012
* Author: Daniel Jonker
*/
package dao;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Collection;

import domain.Account;

public interface AccountDAO{
	
	public void create(String username, String password, String email, String firstName, String lastName, Timestamp updateTime, String flag) throws SQLException; //done
	
	public void update(String username, String email, String firstName, String lastName, Timestamp updateTime, String flag) throws SQLException; //done
	
	public void changePassword(String username, String oldPassword, String newPassword) throws SQLException;
	
	public void delete(String username) throws SQLException;
	
	public boolean usernameExists(String username) throws SQLException;
	
	public Collection<Account> getAll() throws SQLException; // done
	
	public Account getByUsername(String username) throws SQLException;
	
	public Account getByEmail(String email) throws SQLException;

	public Timestamp getUpdateTime(String username) throws SQLException;
	
	public void setUpdateTime(String username, Timestamp t) throws SQLException;
	
	public String getFlag(String username) throws SQLException;
	
	public void setFlag(String username, String flag) throws SQLException;
}
