/**
 * 
 */
package dao;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Collection;

import domain.Coordinate;

/**
 * @author jonda741, cuiju739
 * 	
 */
public interface CoordinateDAO {
	
	public void create(Integer tripId, Double lat, Double longi, Timestamp dtime, String flag) throws SQLException;
	//public void delete() throws SQLException;	
	public Collection<Coordinate> getAll() throws SQLException;
	
	public Collection<Coordinate> getByTrip(Integer tripId) throws SQLException;
	
	public Collection<Coordinate> getByUser(String username) throws SQLException;
	
	public Collection<Coordinate> getByUserDate(String username, Timestamp updatetime) throws SQLException;
	
	public String getFlag(Integer tripId) throws SQLException;
	
	public void setFlag(Integer tripId, String flag) throws SQLException;
	
	public void create(Coordinate coordinate) throws SQLException;
	
	public Collection<Coordinate> getNewByUser(String username) throws SQLException;
}
