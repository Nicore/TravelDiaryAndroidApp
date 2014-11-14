/**
 * @file TagImpl.java
 * @peerReview
 * @date 15/08/2012
 * @author Nick Comer
 */
package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;

import domain.Tag;

public class TagImpl implements TagDAO {
	DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
	

	@Override
	public void create(Integer tagid, String dateTime, String text, String type, String flag) throws SQLException{
		
		Connection con = h2connection.getConnection(); //connect to database
		PreparedStatement pstmt = null;
		
		java.util.Date parsedDate = null;
		try {
			parsedDate = df.parse(dateTime);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		java.sql.Timestamp timestamp = new java.sql.Timestamp(parsedDate.getTime()); //set timestamp

		try {
            con.setAutoCommit(false);
            pstmt = con.prepareStatement("INSERT INTO "+type+"_tag (text, "+type+"_id, updatetime, flag) VALUES (?, ?, ?, ?);");
            pstmt.setString(1, text);
            pstmt.setInt(2, tagid);
            pstmt.setTimestamp(3, timestamp);
            pstmt.setString(4, flag);            
            
            pstmt.executeUpdate();

            con.commit();
            pstmt.close();

        } finally {
            if (con != null) con.close();
        }

	}

	/**
	 * Update a tag in the database
	 * 
	 * @param tagid The unique identifier for the tag
	 * @param dateTime The new date to set
	 * @param text The text to set
	 * @param type The type identifier
	 */
	@Override
	public void update(Integer tagid, String dateTime, String text, String type, String flag)
			throws SQLException {
	
		Connection con = h2connection.getConnection();
		PreparedStatement pstmt = null;
		java.util.Date parsedDate = null;
		try {
			parsedDate = df.parse(dateTime);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		java.sql.Timestamp timestamp = new java.sql.Timestamp(parsedDate.getTime());
		
		try {
            con.setAutoCommit(false);
            pstmt = con.prepareStatement("UPDATE "+type+"_tag set "+type+"_id = ?, updatetime = ?, text = ?, flag = ? where "+type+"_id = ?;");
            pstmt.setInt(1, tagid);
            pstmt.setString(3, text);
            pstmt.setTimestamp(2, timestamp);
            pstmt.setString(4, flag);
            
            pstmt.executeUpdate();

            con.commit();
            pstmt.close();

        } finally {
            if (con != null) con.close();
        }
		
	}


	/**
	 * Delete a specified tag
	 * 
	 * @param tagid The tag ID to delete
	 * @param type The table to delete
	 */
	@Override
	public void delete(Integer tagid, String type, Timestamp updatetime) throws SQLException{
		Connection con = h2connection.getConnection();
		PreparedStatement pstmt = null;
		
		try {
            con.setAutoCommit(false);
            pstmt = con.prepareStatement("update "+type+"_tag set flag = 'd', updatetime = ? where "+type+"_id = ?;");
            pstmt.setTimestamp(1, updatetime);
			pstmt.setInt(2, tagid);

            pstmt.executeUpdate();

            con.commit();
            pstmt.close();

		}  finally {
            if (con != null) con.close();
        }
	}

	/**
	 * Get the entire set of tags for one object
	 */
	@Override
	public Collection<Tag> getAll(String type) throws SQLException{
		Statement stmt = null;
		Connection con = h2connection.getConnection();
		Collection<Tag> tags = new ArrayList<Tag>();
		
		String query = "select * from "+type+"_tag";
		try {
	        stmt = con.createStatement();
	        ResultSet rs = stmt.executeQuery(query);
	        while (rs.next()) {
	        	
	            Integer tagid = rs.getInt(type+"_id");
	            Timestamp dtime = rs.getTimestamp("UPDATETIME");
	            String text = rs.getString("text");
	            String flag = rs.getString("flag");	            
	            
	            tags.add(new Tag(tagid, text, type, dtime, flag));
	        }
	    } catch (SQLException e ) {
	        System.out.println("ERROR");
	        e.printStackTrace();
	    } finally {
	    	if (con != null) con.close();
	        if (stmt != null) { stmt.close(); }
	    }
		return tags;
	}

	/**
	 * Get the tags for a specific event/trip/media
	 * 
	 * @param id The media/trip/event ID
	 * @param type media/trip/event
	 */
	@Override
	public Collection<Tag> getById(Integer id, String type) throws SQLException{
		PreparedStatement pstmt = null;
		Connection con = h2connection.getConnection();
		Collection<Tag> tags = new ArrayList<Tag>();
		
		try {
			pstmt = con.prepareStatement("select * from "+type+"_tag where "+type+"_id = ?");
			pstmt.setInt(1, id);
	        ResultSet rs = pstmt.executeQuery();
	        while (rs.next()) {
	        	
	        	Integer tagid = rs.getInt(type+"_id");
	            Timestamp dtime = rs.getTimestamp("UPDATETIME");
	            String text = rs.getString("text");
	            String flag = rs.getString("flag");	
	            
	            tags.add(new Tag(tagid, text, type, dtime, flag));
	            
	        }
	    } catch (SQLException e ) {
	        System.out.println("ERROR");
	        e.printStackTrace();
	    } finally {
	    	if (con != null) con.close();
	        if (pstmt != null) { pstmt.close(); }
	    }
		return tags;
	}

	@Override
	public Timestamp getUpdateTime(Integer tagId, String type) throws SQLException
	{
		Timestamp result = null;
		PreparedStatement pstmt = null;
		Connection con = h2connection.getConnection();
		
		try{
			pstmt = con.prepareStatement("select updatetime FROM " + type + "_tag where " + type + "_id = ?");
			pstmt.setInt(1, tagId);
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
	public void setUpdateTime(Integer tagId, String type, Timestamp t) throws SQLException
	{
		PreparedStatement pstmt = null;
		Connection con = h2connection.getConnection();
		
		try{
			pstmt = con.prepareStatement("update "+type+"_tag set updatetime= ? where "+type+"_id = ?;");
			pstmt.setTimestamp(1, t);
			pstmt.setInt(2, tagId);			
			
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
	public String getFlag(Integer tagId, String type) throws SQLException
	{
		String result = null;
		PreparedStatement pstmt = null;
		Connection con = h2connection.getConnection();
		
		try{
			pstmt = con.prepareStatement("select flag FROM " + type + "_tag where " + type + "_id = ?");
			pstmt.setInt(1, tagId);
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
	public void setFlag(Integer tagId, String flag, String type) throws SQLException
	{
		PreparedStatement pstmt = null;
		Connection con = h2connection.getConnection();
		
		try{
			pstmt = con.prepareStatement("update "+type+"_tag set flag= ? where "+type+"_id = ?;");
			pstmt.setString(1, flag);
			pstmt.setInt(2, tagId);			
			
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
	public void create(Tag tag) throws SQLException {
		Connection con = h2connection.getConnection(); //connect to database
		PreparedStatement pstmt = null;
		
		java.util.Date parsedDate = null;
		try {
			parsedDate = df.parse(tag.getUpdatetime().toString());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		java.sql.Timestamp timestamp = new java.sql.Timestamp(parsedDate.getTime()); //set timestamp

		try {
            con.setAutoCommit(false);
            String type = tag.getType();
            pstmt = con.prepareStatement("INSERT INTO "+type+"_tag (text, "+type+"_id, updatetime, flag) VALUES (?, ?, ?, ?);");
            pstmt.setString(1, tag.getText());
            pstmt.setInt(2, tag.getTagid());
            pstmt.setTimestamp(3, timestamp);
            pstmt.setString(4, tag.getFlag());            
            
            pstmt.executeUpdate();

            con.commit();
            pstmt.close();

        } finally {
            if (con != null) con.close();
        }		
	}

	@Override
	public Collection<Tag> getByFlagByUser(String username, String type, String flag) throws SQLException {
		PreparedStatement pstmt = null;
		Connection con = h2connection.getConnection();
		Collection<Tag> tags = new ArrayList<Tag>();
		
		int check = 0;
		
		if(type.equals("trip")){
			check = 1;
		} else if(type.equals("event")){
			check = 2;
		} else if(type.equals("media")){
			check = 3;
		}
		
		try {
			switch(check){
				case (1) : {
					//pstmt = con.prepareStatement("select * from account_trip ti inner join trip t on ti.trip_id = t.trip_id;");
					pstmt = con.prepareStatement("select * from account_trip ti inner join trip t on ti.trip_id = t.trip_id inner join trip_tag tt on t.trip_id = tt.trip_id where tt.flag = ? and ti.username = ?;");
					pstmt.setString(1, flag);
					pstmt.setString(2, username);
					
			        ResultSet rs = pstmt.executeQuery();
			        while (rs.next()) {	            
			        	Integer tripId = rs.getInt("trip_id");
			        	String text = rs.getString("text");	    
			        	Timestamp dtime = rs.getTimestamp("updatetime");	
			            tags.add(new Tag(tripId, text, type, dtime, flag));	            
			        }
			        break;
				}
				case (2): {
					pstmt = con.prepareStatement("select * from account_trip ti inner join event e on ti.trip_id = e.trip_id inner join event_tag et on e.event_id = et.event_id where et.flag = ? and ti.username = ?;");
					pstmt.setString(1, flag);
					pstmt.setString(2, username);
					
			        ResultSet rs = pstmt.executeQuery();
			        while (rs.next()) {	            
			        	Integer tripId = rs.getInt("trip_id");
			        	String text = rs.getString("text");	    
			        	Timestamp dtime = rs.getTimestamp("updatetime");
			            tags.add(new Tag(tripId, text, type, dtime, flag));	            
			        }
					break;
				}
				case (3): {
					pstmt = con.prepareStatement("select * from account_trip ti inner join event e on ti.trip_id = e.trip_id inner join media m on e.event_id = m.event_id inner join media_tag mt on m.media_id = mt.media_id where mt.flag = ? and ti.username = ?");
					pstmt.setString(1, flag);
					pstmt.setString(2, username);
					
			        ResultSet rs = pstmt.executeQuery();
			        while (rs.next()) {	            
			        	Integer tripId = rs.getInt("trip_id");
			        	String text = rs.getString("text");	    
			        	Timestamp dtime = rs.getTimestamp("updatetime");		
			            tags.add(new Tag(tripId, text, type, dtime, flag));	            
			        }
					break;
				}
			}
	    } catch (SQLException e ) {
	        System.out.println("ERROR");
	        e.printStackTrace();
	    } finally {
	    	if (con != null) con.close();
	        if (pstmt != null) { pstmt.close(); }
	    }
		return tags;
	}
}
