/**
* TripDao
* Interface to show methods and parameters to be used for interacting with trip table in DB
* @peerReview
* 24/04/2012
* Author: Daniel Jonker
*/
package dao;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Collection;

import domain.Trip;

public interface TripDAO {
	
	//class needed 
	public void create(Trip trip) throws SQLException;
	
	public void create(Integer tripId, String name, String description, Timestamp updateTime, String flag) throws SQLException;
	
	public void update(Integer tripId, String name, String description, Timestamp updateTime, String flag)throws SQLException;
	
	public void delete(Integer tripId, Timestamp updatetime)throws SQLException;
	
	public Collection<Trip> getAll()throws SQLException;
	
	public Collection<Trip> getByFlagByUser(String username, String flag) throws SQLException;
	
	public Collection<Trip> getByUser(String username)throws SQLException;
	
	public Collection<Trip> getByUserDate(String username, Timestamp date) throws SQLException;
	
	public Collection<Trip> getByName(String name)throws SQLException;
	
	public Timestamp getUpdateTime(Integer tripId) throws SQLException;
	
	public void setUpdateTime(Integer tripId, Timestamp t) throws SQLException;
	
	public String getFlag(Integer tripId) throws SQLException;
	
	public void setFlag(Integer tripId, String flag) throws SQLException;
}
