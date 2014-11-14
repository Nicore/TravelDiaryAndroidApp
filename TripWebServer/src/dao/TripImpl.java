package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;

import org.h2.jdbc.JdbcSQLException;

import domain.Trip;

public class TripImpl implements TripDAO {
   
	@Override
	public void create(Integer tripId, String name, String description,
		Timestamp updateTime, String flag) throws SQLException {

		Connection con = h2connection.getConnection();
		PreparedStatement pstmt = null;
		
		try {
            con.setAutoCommit(false);
            pstmt = con.prepareStatement("INSERT INTO trip (trip_id, name, description, updatetime, flag) VALUES (?, ?, ?, ?, ?, ?);");
            pstmt.setInt(1, tripId);
            pstmt.setString(2, name);
            pstmt.setString(3, description);
            pstmt.setTimestamp(4, updateTime);
            pstmt.setString(5, flag);
            
            pstmt.executeUpdate();

            con.commit();
            pstmt.close();

        } finally {
            if (con != null) con.close();
        }
	}
	
	@Override
	public void create(Trip trip) throws SQLException {
		Connection con = h2connection.getConnection();
		PreparedStatement pstmt = null;
		
		try {
            con.setAutoCommit(false);
            pstmt = con.prepareStatement("INSERT INTO trip (trip_id, name, description, updatetime, flag) VALUES (?, ?, ?, ?, ?);");
            pstmt.setInt(1, trip.getTripId());
            pstmt.setString(2, trip.getName());
            pstmt.setString(3, trip.getDescription());
            pstmt.setTimestamp(4, trip.getUpdatetime());
            pstmt.setString(5, trip.getFlag());
            
            pstmt.executeUpdate();

            con.commit();
            pstmt.close();

        }finally {
            if (con != null) con.close();
        }		
	}	
	
	@Override
	public void update(Integer tripId, String name, String description,
			Timestamp updateTime, String flag) throws SQLException {
		
		Connection con = h2connection.getConnection();
		PreparedStatement pstmt = null;
		
		try {
            con.setAutoCommit(false);
            pstmt = con.prepareStatement("UPDATE trip set name = ?, description = ?, updatetime = ?, flag = ? where trip_id = ?;");
            pstmt.setString(1, name);
            pstmt.setString(2, description);
            pstmt.setTimestamp(3, updateTime);
            pstmt.setString(4, flag);
            pstmt.setInt(5, tripId);
            pstmt.executeUpdate();

            con.commit();
            pstmt.close();

        } finally {
            if (con != null) con.close();
        }
		
	}

	@Override
	public void delete(Integer tripId, Timestamp updatetime) throws SQLException{
		PreparedStatement pstmt = null;
		Connection con = h2connection.getConnection();
		
		try{
			con.setAutoCommit(false);
			pstmt = con.prepareStatement("update trip set flag = 'd', updatetime = ? where trip_id = ?;");
			pstmt.setTimestamp(1, updatetime);
			pstmt.setInt(2, tripId);			
			
			pstmt.executeUpdate();	
			
			con.commit();
            pstmt.close();
			
		} catch (SQLException e){
			System.out.println("ERROR");
			e.printStackTrace();
		}finally {
			if (con != null) con.close();
		}	
	}

	@Override
	public Collection<Trip> getAll() throws SQLException {
		Statement stmt = null;
		Connection con = h2connection.getConnection();
		Collection<Trip> trips = new ArrayList<Trip>();
		
		String query = "select * from trip";
		try {
	        stmt = con.createStatement();
	        ResultSet rs = stmt.executeQuery(query);
	        while (rs.next()) {	        	
	            Integer tripId = rs.getInt("trip_id");
	            String name = rs.getString("name");
	            String description = rs.getString("description");
	            Timestamp time = rs.getTimestamp("UPDATETIME");
	            String flag = rs.getString("flag");
	            trips.add(new Trip(tripId, name, description, time, flag));
	        }
	    } catch (SQLException e ) {
	        System.out.println("ERROR");
	        e.printStackTrace();
	    } finally {
	    	if (con != null) con.close();
	        if (stmt != null) { stmt.close(); }
	    }
		return trips;
	}

	@Override
	public Collection<Trip> getByUser(String username) throws SQLException {
		PreparedStatement pstmt = null;
		Connection con = h2connection.getConnection();
		Collection<Trip> trips = new ArrayList<Trip>();
		
		try {
			pstmt = con.prepareStatement("select * from account_trip ti inner join trip t on ti.trip_id = t.trip_id where ti.username = ?;");
			pstmt.setString(1, username);
	        ResultSet rs = pstmt.executeQuery();
	        while (rs.next()) {
	        	
	        	Integer tripId = rs.getInt("trip_id");
	            String dname = rs.getString("name");
	            String description = rs.getString("description");
	            Timestamp dtime = rs.getTimestamp("UPDATETIME");
	            String flag = rs.getString("flag");
	            
	            trips.add(new Trip(tripId, dname, description, dtime, flag));	            
	        }
	    } catch (SQLException e ) {
	        System.out.println("ERROR");
	        e.printStackTrace();
	    } finally {
	    	if (con != null) con.close();
	        if (pstmt != null) { pstmt.close(); }
	    }
		return trips;
	}

	/**
	 * This method get trip by trip name
	 */
	@Override
	public Collection<Trip> getByName(String name) throws SQLException {
		PreparedStatement pstmt = null;
		Connection con = h2connection.getConnection();
		Collection<Trip> trips = new ArrayList<Trip>();
		
		try {
			pstmt = con.prepareStatement("select * from trip where name = ?");
			pstmt.setString(1, name);
	        ResultSet rs = pstmt.executeQuery();
	        while (rs.next()) {
	        	
	        	Integer tripId = rs.getInt("trip_id");
	            String dname = rs.getString("name");
	            String description = rs.getString("description");
	            Timestamp time = rs.getTimestamp("UPDATETIME");
	            String flag = rs.getString("flag");
	            
	            trips.add(new Trip(tripId, dname, description, time, flag));	            
	        }
	    } catch (SQLException e ) {
	        System.out.println("ERROR");
	        e.printStackTrace();
	    } finally {
	    	if (con != null) con.close();
	        if (pstmt != null) { pstmt.close(); }
	    }
		return trips;
	}

	@Override
	public Collection<Trip> getByUserDate(String username, Timestamp date)throws SQLException {
		PreparedStatement pstmt = null;
		Connection con = h2connection.getConnection();
		Collection<Trip> trips = new ArrayList<Trip>();
		
		try {
			//pstmt = con.prepareStatement("select * from trip where username = ? and updatetime > ?");
			pstmt = con.prepareStatement("select * from account_trip ti inner join trip t on ti.trip_id = t.trip_id where ti.username = ? and t.updatetime > ?;");
			
			pstmt.setString(1, username);
			pstmt.setTimestamp (2, date);
	        ResultSet rs = pstmt.executeQuery();
	        while (rs.next()) {
	        	
	        	Integer tripId = rs.getInt("trip_id");
	            String dname = rs.getString("name");
	            String description = rs.getString("description");
	            Timestamp dtime = rs.getTimestamp("UPDATETIME");
	            String flag = rs.getString("flag");
	            
	            trips.add(new Trip(tripId, dname, description, dtime, flag));	            
	        }
	    } catch (SQLException e ) {
	        System.out.println("ERROR");
	        e.printStackTrace();
	    } finally {
	    	if (con != null) con.close();
	        if (pstmt != null) { pstmt.close(); }
	    }
		return trips;
	}	
	
	@Override
	public Timestamp getUpdateTime(Integer tripId) throws SQLException{
		Timestamp result = null;
		PreparedStatement pstmt = null;
		Connection con = h2connection.getConnection();
		
		try{
			pstmt = con.prepareStatement("select updatetime FROM trip where trip_id = ?");
			pstmt.setInt(1, tripId);
			ResultSet rs = pstmt.executeQuery();
			rs.first();

			result = rs.getTimestamp(1);
			
			//exists = false;
			
			
		} catch (SQLException e){
			System.out.println("ERROR");
			e.printStackTrace();
		}finally {
			if (con != null) con.close();
		}
		
		return result;	
	}
	
	@Override
	public void setUpdateTime(Integer tripId, Timestamp t) throws SQLException{
		PreparedStatement pstmt = null;
		Connection con = h2connection.getConnection();
		
		try{
			pstmt = con.prepareStatement("update trip set updatetime= ? where trip_id = ?;");
			pstmt.setTimestamp(1, t);
			pstmt.setInt(2, tripId);			
			
			pstmt.executeUpdate();
			
			//exists = false;
			
			
		} catch (SQLException e){
			System.out.println("ERROR");
			e.printStackTrace();
		}finally {
			if (con != null) con.close();
		}		
	}
	
	@Override
	public String getFlag(Integer tripId) throws SQLException{
		String result = null;
		PreparedStatement pstmt = null;
		Connection con = h2connection.getConnection();
		
		try{
			pstmt = con.prepareStatement("select flag FROM trip where trip_id = ?");
			pstmt.setInt(1, tripId);
			ResultSet rs = pstmt.executeQuery();
			rs.first();

			result = rs.getString("flag");
			
			//exists = false;
			
			
		} catch (SQLException e){
			System.out.println("ERROR");
			e.printStackTrace();
		}finally {
			if (con != null) con.close();
		}
		
		return result;	
	}
	
	@Override
	public void setFlag(Integer tripId, String flag) throws SQLException{
		PreparedStatement pstmt = null;
		Connection con = h2connection.getConnection();
		
		try{
			pstmt = con.prepareStatement("update trip set flag= ? where trip_id = ?;");
			pstmt.setString(1, flag);
			pstmt.setInt(2, tripId);			
			
			pstmt.executeUpdate();
			
			//exists = false;			
			
		} catch (SQLException e){
			System.out.println("ERROR");
			e.printStackTrace();
		}finally {
			if (con != null) con.close();
		}	
	}
	
	/**
	 * This method get trips that has new flags by username
	 */
	@Override
	public Collection<Trip> getByFlagByUser(String username, String flag) throws SQLException {
		PreparedStatement pstmt = null;
		Connection con = h2connection.getConnection();
		Collection<Trip> trips = new ArrayList<Trip>();
		
		try {
			//pstmt = con.prepareStatement("select * from account_trip ti inner join trip t on ti.trip_id = t.trip_id;");
			pstmt = con.prepareStatement("select * from account_trip ti inner join trip t on ti.trip_id = t.trip_id where t.flag = ? and ti.username = ?;");
			System.out.println(pstmt.toString());
			pstmt.setString(1, flag);
			pstmt.setString(2, username);			
			
	        ResultSet rs = pstmt.executeQuery();
	        while (rs.next()) {	        	
	        	Integer tripId = rs.getInt("trip_id");
	            String dname = rs.getString("name");
	            String description = rs.getString("description");
	            Timestamp dtime = rs.getTimestamp("UPDATETIME");
	            
	            trips.add(new Trip(tripId, dname, description, dtime, flag));	            
	        }
	    } catch (SQLException e ) {
	        System.out.println("ERROR");
	        e.printStackTrace();
	    } finally {
	    	if (con != null) con.close();
	        if (pstmt != null) { pstmt.close(); }
	    }
		return trips;
	}	
}
