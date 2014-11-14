/**
* @file TagDAO.java
* Interface to show methods and parameters to be used for interacting with tag tables in DB
* @peerReview
* @date 15/08/2012
* @author Nick Comer
*/
package dao;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Collection;

import domain.Tag;

public interface TagDAO {	
	public void create(Integer tagid, String dateTime, String text, String type, String flag) throws SQLException;
	
	public void update(Integer tagid, String dateTime, String text, String type, String flag) throws SQLException ;
	
	public void delete(Integer tagid, String type, Timestamp updatetime) throws SQLException;
	
	public Collection<Tag> getAll(String type) throws SQLException;
	
	public Collection<Tag> getById(Integer Id, String type)throws SQLException;	
	
	public Timestamp getUpdateTime(Integer tagId, String type) throws SQLException;
	
	public void setUpdateTime(Integer tagId, String type, Timestamp t) throws SQLException;
	
	public String getFlag(Integer tagId, String type) throws SQLException;
	
	public void setFlag(Integer tagId, String flag, String type) throws SQLException;
		
	public void create(Tag tag) throws SQLException;
	
	public Collection<Tag> getByFlagByUser(String username, String type, String flag) throws SQLException;
}
