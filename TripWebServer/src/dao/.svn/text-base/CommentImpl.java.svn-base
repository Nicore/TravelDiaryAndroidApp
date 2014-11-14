/**
 * @file CommentImpl.java
 * @peerReview
 * @date 15/09/2012
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

/*import domain.Comment;*/
import domain.Tag;

public class CommentImpl implements CommentDAO {
	DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
	

	@Override
	public void create(Integer id, Integer fid, String text, String type, String alias, Timestamp tstamp) throws SQLException{
		
		Connection con = h2connection.getConnection(); //connect to database
		PreparedStatement pstmt = null;
		
//		java.util.Date parsedDate = null;
//		try {
//			parsedDate = df.parse(tstamp);
//		} catch (ParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		//java.sql.Timestamp timestamp = new java.sql.Timestamp(parsedDate.getTime()); //set timestamp

		try {
            con.setAutoCommit(false);
            pstmt = con.prepareStatement("INSERT INTO "+type+"_comment (id, text, alias, deflag, "+type+"_id, tstamp) " +
            		"VALUES (?, ?, ?, ?, ?, ?);");
            pstmt.setInt(1, id);
            pstmt.setString(2, text);
            pstmt.setString(3, alias);
            pstmt.setInt(4, 0);
            pstmt.setInt(5, fid);
            pstmt.setTimestamp(6, tstamp);
                        
            
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
	public void update(Integer id, Integer fid, String text, String type, String alias, Timestamp tstamp)
			throws SQLException {
	
		Connection con = h2connection.getConnection();
		PreparedStatement pstmt = null;
		
		try {
            con.setAutoCommit(false);
            pstmt = con.prepareStatement("UPDATE "+type+"_comment set "+type+"_id = ?, tstamp = ?, text = ?, alias = ?" +
            		" where id = ?;");
            pstmt.setInt(1, fid);
            pstmt.setTimestamp(2, tstamp);
            pstmt.setString(3, text);
            pstmt.setString(4, alias);
            pstmt.setInt(5, id);
            
            pstmt.executeUpdate();

            con.commit();
            pstmt.close();

        } finally {
            if (con != null) con.close();
        }
		
	}


	/**
	 * Delete a specified comment
	 * 
	 * @param id The comment ID to delete
	 * @param type The table to delete from
	 */
	@Override
	public void delete(Integer id, String type) throws SQLException{
		Connection con = h2connection.getConnection();
		PreparedStatement pstmt = null;
		
		try {
            con.setAutoCommit(false);
            pstmt = con.prepareStatement("DELETE FROM "+type+"_comment where id = ?");
            pstmt.setInt(1, id);

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
	/*@Override
	public Collection<Comment> getAll(String type) throws SQLException{
		Statement stmt = null;
		Connection con = h2connection.getConnection();
		Collection<Comment> comments = new ArrayList<Comment>();
		
		String query = "select * from "+type+"_comment";
		try {
	        stmt = con.createStatement();
	        ResultSet rs = stmt.executeQuery(query);
	        while (rs.next()) {
	        	
	        	Integer id = rs.getInt("id");
	        	Integer fid = rs.getInt(type+"_id");
	            String text = rs.getString("text");
	            String alias = rs.getString("alias");
	            Timestamp dtime = rs.getTimestamp("TSTAMP");
	            
	            comments.add(new Comment(id, fid, text, type, alias, dtime));
	        }
	    } catch (SQLException e ) {
	        System.out.println("ERROR");
	        e.printStackTrace();
	    } finally {
	    	if (con != null) con.close();
	        if (stmt != null) { stmt.close(); }
	    }
		return comments;
	}*/

	/**
	 * Get the tags for a specific event/trip/media
	 * 
	 * @param id The media/trip/event ID
	 * @param type media/trip/event
	 */
	/*@Override
	public Collection<Comment> getById(Integer id, String type) throws SQLException{
		PreparedStatement pstmt = null;
		Connection con = h2connection.getConnection();
		Collection<Comment> comments = new ArrayList<Comment>();
		
		try {
			pstmt = con.prepareStatement("select * from "+type+"_comment where id = ?");
			pstmt.setInt(1, id);
	        ResultSet rs = pstmt.executeQuery();
	        while (rs.next()) {
	        	
	        	//Integer pid = rs.getInt("id");
	        	Integer fid = rs.getInt(type+"_id");
	            String text = rs.getString("text");
	            String alias = rs.getString("alias");
	            Timestamp dtime = rs.getTimestamp("TSTAMP");
	            
	            comments.add(new Comment(id, fid, text, type, alias, dtime));
	            
	        }
	    } catch (SQLException e ) {
	        System.out.println("ERROR");
	        e.printStackTrace();
	    } finally {
	    	if (con != null) con.close();
	        if (pstmt != null) { pstmt.close(); }
	    }
		return comments;
	}

	@Override
	public Collection<Comment> getByAlias(String alias, String type) throws SQLException {
		PreparedStatement pstmt = null;
		Connection con = h2connection.getConnection();
		Collection<Comment> comments = new ArrayList<Comment>();
		
		try {
			pstmt = con.prepareStatement("select * from "+type+"_comment where id = ?");
			pstmt.setString(1, alias);
	        ResultSet rs = pstmt.executeQuery();
	        while (rs.next()) {
	        	
	        	Integer id = rs.getInt("id");
	        	Integer fid = rs.getInt(type+"_id");
	            String text = rs.getString("text");
	            //String alias = rs.getString("alias");
	            Timestamp dtime = rs.getTimestamp("TSTAMP");
	            
	            comments.add(new Comment(id, fid, text, type, alias, dtime));
	            
	        }
	    } catch (SQLException e ) {
	        System.out.println("ERROR");
	        e.printStackTrace();
	    } finally {
	    	if (con != null) con.close();
	        if (pstmt != null) { pstmt.close(); }
	    }
		return comments;
	}

	@Override
	public Collection<Comment> getByDate(Timestamp date, String type) throws SQLException {
		PreparedStatement pstmt = null;
		Connection con = h2connection.getConnection();
		Collection<Comment> comments = new ArrayList<Comment>();
		
		try {
			pstmt = con.prepareStatement("select * from "+type+"_comment where TSTAMP > ?");
			pstmt.setTimestamp(1, date);
	        ResultSet rs = pstmt.executeQuery();
	        while (rs.next()) {
	        	
	        	Integer id = rs.getInt("id");
	        	Integer fid = rs.getInt(type+"_id");
	            String text = rs.getString("text");
	            String alias = rs.getString("alias");
	            //Timestamp dtime = rs.getTimestamp("TSTAMP");
	            
	            comments.add(new Comment(id, fid, text, type, alias, date));
	            
	        }
	    } catch (SQLException e ) {
	        System.out.println("ERROR");
	        e.printStackTrace();
	    } finally {
	    	if (con != null) con.close();
	        if (pstmt != null) { pstmt.close(); }
	    }
		return comments;
	}

	
	/*@Override
	public Timestamp getUpdateTime() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setUpdateTime(Timestamp t) {
		// TODO Auto-generated method stub
		
	}*/


	

}
