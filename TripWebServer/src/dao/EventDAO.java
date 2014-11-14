/**
* POIDAO
* Interface to show methods and parameters to be used for interacting with POI table in DB
* @peerReview
* 19/09/2012
* Author: Daniel Jonker, June Cui
*/
package dao;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Collection;

import domain.Event;

public interface EventDAO {
	
	public void create(Integer eventId, double lattitude, double longitude, Timestamp dateTime, String name, String description, Integer tripId, Timestamp updateTime, String flag) throws SQLException;
	
	public void update(Integer eventId, double lattitude, double longitude, Timestamp dateTime, String name, String description, Integer tripId, Timestamp updateTime, String flag) throws SQLException ;
	
	public void delete(Integer eventId, Timestamp updatetime) throws SQLException;
	
	public Collection<Event> getAll() throws SQLException;
	
	public Collection<Event> getById(Integer Id)throws SQLException;
	
	public Collection<Event> getByName(String name)throws SQLException;
	
	public Collection<Event> getByTripId(Integer tripId)throws SQLException; //either tripid or name??
	
	public Collection<Event> getByUserDate(String username, Timestamp date) throws SQLException;
	
	public Timestamp getUpdateTime(Integer eventId) throws SQLException;
	
	public void setUpdateTime(Integer eventId, Timestamp t) throws SQLException;
	
	public String getFlag(Integer eventId) throws SQLException;
	
	public void setFlag(Integer eventId, String flag) throws SQLException;
	
	public void create(Event event) throws SQLException;
	
	public Collection<Event> getByFlagByUser(String username, String flag) throws SQLException;	
}



