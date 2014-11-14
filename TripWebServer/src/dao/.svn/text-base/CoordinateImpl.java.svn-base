package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;

import domain.Coordinate;

public class CoordinateImpl implements CoordinateDAO{

	@Override
	public void create(Integer tripId, Double lat, Double longi, Timestamp dtime, String flag)
			throws SQLException {
		// TODO Auto-generated method stub
		Connection con = h2connection.getConnection();
		PreparedStatement pstmt = null;
		

		try {
            con.setAutoCommit(false);
            pstmt = con.prepareStatement("INSERT INTO Coordinates (latitude, longitude, trip_id, datetime, flag) VALUES (?, ?, ?, ?, ?);");
            pstmt.setDouble(1, lat);
            pstmt.setDouble (2, longi);
            pstmt.setInt (3, tripId);
            pstmt.setTimestamp(4, dtime);
            pstmt.setString(5, flag);
            
            pstmt.executeUpdate();

            con.commit();
            pstmt.close();

        } finally {
            if (con != null) con.close();
        }
	}

	@Override
	public Collection<Coordinate> getAll() throws SQLException {
		// TODO Auto-generated method stub
		Statement stmt = null;
		Connection con = h2connection.getConnection();
		Collection<Coordinate> coordinates = new ArrayList<Coordinate>();
		
		String query = "select * from coordinates";
		try {
	        stmt = con.createStatement();
	        ResultSet rs = stmt.executeQuery(query);
	        while (rs.next()) {

	            Integer tripId = rs.getInt("trip_id");
	            Double longitude = rs.getDouble("longitude");
	            Double lattitude = rs.getDouble("latitude");
	            Timestamp dtime = rs.getTimestamp("dtime");
	            String flag = rs.getString("flag");
	            
	            coordinates.add(new Coordinate(lattitude, longitude, dtime, tripId, flag));
	        }
	    } catch (SQLException e ) {
	        System.out.println("ERROR");
	        e.printStackTrace();
	    } finally {
	    	if (con != null) con.close();
	        if (stmt != null) { stmt.close(); }
	    }
		return coordinates;

	}

	@Override
	public Collection<Coordinate> getByTrip(Integer tripId) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<Coordinate> getByUser(String username)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<Coordinate> getByUserDate(String username,
			Timestamp updatetime) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public String getFlag(Integer tripId) throws SQLException
	{
		String result = null;
		PreparedStatement pstmt = null;
		Connection con = h2connection.getConnection();
		
		try{
			pstmt = con.prepareStatement("select flag FROM coordinates where trip_id = ?");
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
	public void setFlag(Integer tripId, String flag) throws SQLException
	{
		PreparedStatement pstmt = null;
		Connection con = h2connection.getConnection();
		
		try{
			pstmt = con.prepareStatement("update media set flag= ? where trip_id = ?;");
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

	@Override
	public void create(Coordinate coordinate) throws SQLException {
		Connection con = h2connection.getConnection();
		PreparedStatement pstmt = null;		

		try {
            con.setAutoCommit(false);
            pstmt = con.prepareStatement("INSERT INTO Coordinates (latitude, longitude, trip_id, datetime, flag) VALUES (?, ?, ?, ?, ?);");
            pstmt.setDouble(1, coordinate.getLat());
            pstmt.setDouble (2, coordinate.getLongi());
            pstmt.setInt (3, coordinate.getTripId());
            pstmt.setTimestamp(4, coordinate.getDatetime());
            pstmt.setString(5, coordinate.getFlag());
            
            pstmt.executeUpdate();

            con.commit();
            pstmt.close();

        } finally {
            if (con != null) con.close();
        }		
	}

	@Override
	public Collection<Coordinate> getNewByUser(String username)
			throws SQLException {
		PreparedStatement pstmt = null;
		Connection con = h2connection.getConnection();
		Collection<Coordinate> coordinates = new ArrayList<Coordinate>();
		
		try {
			//pstmt = con.prepareStatement("select * from account_trip ti inner join trip t on ti.trip_id = t.trip_id;");
			pstmt = con.prepareStatement("select * from account_trip ti inner join coordinates c on ti.trip_id = c.trip_id where c.flag = 'n' and ti.username = ?");
			pstmt.setString(1, username);
	        ResultSet rs = pstmt.executeQuery();
	        while (rs.next()) {	    	            
	        	Double la = rs.getDouble("latitude");
	        	Double lo = rs.getDouble("longitude");
	        	Timestamp dtime = rs.getTimestamp("datetime");
	        	Integer tripId = rs.getInt("trip_id");
	            String flag = rs.getString("flag");
	            
	            coordinates.add(new Coordinate(la, lo, dtime, tripId, flag));	            
	        }
	    } catch (SQLException e ) {
	        System.out.println("ERROR");
	        e.printStackTrace();
	    } finally {
	    	if (con != null) con.close();
	        if (pstmt != null) { pstmt.close(); }
	    }
		return coordinates;
	}
}
