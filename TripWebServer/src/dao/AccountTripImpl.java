package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import domain.AccountTrip;
import domain.Trip;

public class AccountTripImpl implements AccountTripDAO {

	@Override
	public Collection<AccountTrip> getAll() throws SQLException {
		Statement stmt = null;
		Connection con = h2connection.getConnection();
		Collection<AccountTrip> accountTrips = new ArrayList<AccountTrip>();
		
		String query = "select * from account_trip";
		try {
	        stmt = con.createStatement();
	        ResultSet rs = stmt.executeQuery(query);
	        while (rs.next()) {	        	
	        	String uname = rs.getString("username");
	        	Integer tripId = rs.getInt("trip_id");
	        	Integer permissions = rs.getInt("permissions");
	            String flag = rs.getString("flag");
	            accountTrips.add(new AccountTrip(uname, tripId, flag, permissions));
	        }
	    } catch (SQLException e ) {
	        System.out.println("ERROR");
	        e.printStackTrace();
	    } finally {
	    	if (con != null) con.close();
	        if (stmt != null) { stmt.close(); }
	    }
		return accountTrips;
	}

	@Override
	public void create(AccountTrip accountTrip) throws SQLException {
		Connection con = h2connection.getConnection();
		PreparedStatement pstmt = null;
		
		try {
            con.setAutoCommit(false);
            pstmt = con.prepareStatement("INSERT INTO account_trip (username, trip_id, permissions, flag) VALUES (?, ?, ?, ?);");
            pstmt.setString(1, accountTrip.getUsername());
            pstmt.setInt(2, accountTrip.getTripId());
            pstmt.setInt(3, accountTrip.getPermissions());
            pstmt.setString(4, accountTrip.getFlag());
            
            pstmt.executeUpdate();

            con.commit();
            pstmt.close();

        } finally {
            if (con != null) con.close();
        }
		
	}

	@Override
	public Collection<AccountTrip> getByFlagByUser(String username, String flag)
			throws SQLException {
		PreparedStatement pstmt = null;
		Connection con = h2connection.getConnection();
		Collection<AccountTrip> accountTrips = new ArrayList<AccountTrip>();
		
		try {
			//pstmt = con.prepareStatement("select * from account_trip ti inner join trip t on ti.trip_id = t.trip_id;");
			pstmt = con.prepareStatement("select * from account_trip where flag = ? and username = ?;");
			pstmt.setString(1, flag);
			pstmt.setString(2, username);			
			
	        ResultSet rs = pstmt.executeQuery();
	        while (rs.next()) {	        	
	        	String uname = rs.getString("username");
	        	Integer tripId = rs.getInt("trip_id");
	        	Integer permissions = rs.getInt("permissions");
	        	String acflag = rs.getString("flag");
	            
	            accountTrips.add(new AccountTrip(uname, tripId, acflag, permissions));	            
	        }
	    } catch (SQLException e ) {
	        System.out.println("ERROR");
	        e.printStackTrace();
	    } finally {
	    	if (con != null) con.close();
	        if (pstmt != null) { pstmt.close(); }
	    }
		return accountTrips;
	}

	@Override
	public void delete(String username, int tripId) throws SQLException {
		PreparedStatement pstmt = null;
		Connection con = h2connection.getConnection();
		
		try{
			con.setAutoCommit(false);
			pstmt = con.prepareStatement("update account_trip set flag = 'd' where trip_id = ? and username = ?;");
			pstmt.setInt(1, tripId);
			pstmt.setString(2, username);			
			
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

	

}
