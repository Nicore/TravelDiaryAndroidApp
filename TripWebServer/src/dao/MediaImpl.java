/**
 * 
 */
package dao;

import java.io.ByteArrayInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import domain.Media;

/**
 * @author jonda741
 *
 */
public class MediaImpl implements MediaDAO {

	@Override
	public void create(Integer mediaId, String name, String description,
			String type, byte[] blob, Integer eventId, Timestamp updatetime, String flag) throws SQLException {
		// TODO Auto-generated method stub.
		Connection con = h2connection.getConnection();
		PreparedStatement pstmt = null;
		
		try {
            con.setAutoCommit(false);
            pstmt = con.prepareStatement("INSERT INTO media (media_id, event_id, name, description, type, blob, UPDATETIME, flag) VALUES (?, ?, ?, ?, ?, ?, ?, ?);");
            pstmt.setInt(1, mediaId);
            pstmt.setInt(2, eventId);
            pstmt.setString(3, name);
            pstmt.setString(4, description);
            pstmt.setString(5, type);
            pstmt.setBinaryStream(6, new ByteArrayInputStream(blob),blob.length);
            pstmt.setTimestamp(7, updatetime);
            pstmt.setString(8, flag);
            
            pstmt.executeUpdate();

            con.commit();
            pstmt.close();

        } finally {
            if (con != null) con.close();
        }
	}

	@Override
	public void update(Integer mediaId, String name, String description,
			String type, byte[] blob, Integer eventId, Timestamp updatetime, String flag) throws SQLException {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub.
		Connection con = h2connection.getConnection();
		PreparedStatement pstmt = null;
		
		try {
            con.setAutoCommit(false);
            pstmt = con.prepareStatement("UPDATE MEDIA set event_id = ?, name = ?, description = ?, type = ?, blob = ?, UPDATETIME = ?, flag = ? VALUES (?, ?, ?, ?, ?, ?, ?);");
            pstmt.setInt(1, eventId);
            pstmt.setString(2, name);
            pstmt.setString(3, description);
            pstmt.setString(4, type);
            pstmt.setBinaryStream(5, new ByteArrayInputStream(blob),blob.length);
            pstmt.setTimestamp(6, updatetime);
            pstmt.setString(7, flag);
            pstmt.executeUpdate();

            con.commit();
            pstmt.close();

        } finally {
            if (con != null) con.close();
        }
	}

	@Override
	public void updateAudioToText(Integer currentId, Integer newId, byte[] audio)
			throws SQLException {
		
	}

	@Override
	public void delete(Integer mediaId, Timestamp updatetime) throws SQLException {
		Connection con = h2connection.getConnection();
		PreparedStatement pstmt = null;
		
		try {
            con.setAutoCommit(false);
            pstmt = con.prepareStatement("update media set flag = 'd', updatetime = ? where media_id = ?;");
            pstmt.setTimestamp(1, updatetime);
			pstmt.setInt(2, mediaId);

            pstmt.executeUpdate();

            con.commit();
            pstmt.close();
		}catch (SQLException e){
			System.out.println("ERROR");
			e.printStackTrace();		
		}finally {
            if (con != null) con.close();
        }		
	}

	@Override
	public Collection<Media> getAll() throws SQLException {
		// TODO Auto-generated method stub
		Statement stmt = null;
		Connection con = h2connection.getConnection();
		Collection<Media> medias = new ArrayList<Media>();
		
		String query = "select * from media";
		try {
	        stmt = con.createStatement();
	        ResultSet rs = stmt.executeQuery(query);
	        while (rs.next()) {
	        	
	            Integer mediaId = rs.getInt("media_id");
	            Integer eventId = rs.getInt("event_id");
	            String name = rs.getString("name");
	            String description = rs.getString("description");
	            String type = rs.getString("type");
	            byte[] blob = rs.getBytes("blob");
	            Timestamp updatetime = rs.getTimestamp("UPDATETIME");
	            String flag = rs.getString("flag");


	            medias.add(new Media(mediaId, name, description, type, blob, eventId, updatetime, flag));
	        }
	    } catch (SQLException e ) {
	        System.out.println("ERROR");
	        e.printStackTrace();
	    } finally {
	    	if (con != null) con.close();
	        if (stmt != null) { stmt.close(); }
	    }
		return medias;
	}

	@Override
	public Collection<Media> getByEvent(Integer eId) throws SQLException {
		// TODO Auto-generated method stub
		PreparedStatement pstmt = null;
		Connection con = h2connection.getConnection();
		Collection<Media> medias = new ArrayList<Media>();
		
		try {
			pstmt = con.prepareStatement("select * from media where event_id = ?");
			pstmt.setInt(1, eId);
	        ResultSet rs = pstmt.executeQuery();
	        while (rs.next()) {
	        	
	        	
	        	Integer mediaId = rs.getInt("media_id");
	            Integer eventId = rs.getInt("event_id");
	            String name = rs.getString("name");
	            String description = rs.getString("description");
	            String type = rs.getString("type");
	            byte[] blob = rs.getBytes("blob");
	            Timestamp updatetime = rs.getTimestamp("UPDATETIME");
	            String flag = rs.getString("flag");


	            medias.add(new Media(mediaId, name, description, type, blob, eventId, updatetime, flag));
	            
	        }
	    } catch (SQLException e ) {
	        System.out.println("ERROR");
	        e.printStackTrace();
	    } finally {
	    	if (con != null) con.close();
	        if (pstmt != null) { pstmt.close(); }
	    }
		return medias;
	}

	@Override
	public Collection<Media> getByName(String n) throws SQLException {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		PreparedStatement pstmt = null;
		Connection con = h2connection.getConnection();
		Collection<Media> medias = new ArrayList<Media>();
		
		try {
			pstmt = con.prepareStatement("select * from media where name = ?");
			pstmt.setString(1, n);
	        ResultSet rs = pstmt.executeQuery();
	        while (rs.next()) {
	        	
	        	
	        	Integer mediaId = rs.getInt("media_id");
	            Integer eventId = rs.getInt("event_id");
	            String name = rs.getString("name");
	            String description = rs.getString("description");
	            String type = rs.getString("type");
	            byte[] blob = rs.getBytes("blob");
	            Timestamp updatetime = rs.getTimestamp("UPDATETIME");
	            String flag = rs.getString("flag");

	            medias.add(new Media(mediaId, name, description, type, blob, eventId, updatetime, flag));
	            
	        }
	    } catch (SQLException e ) {
	        System.out.println("ERROR");
	        e.printStackTrace();
	    } finally {
	    	if (con != null) con.close();
	        if (pstmt != null) { pstmt.close(); }
	    }
		return medias;
		
	}

	@Override
	public Collection<Media> getByType(String t) throws SQLException {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		PreparedStatement pstmt = null;
		Connection con = h2connection.getConnection();
		Collection<Media> medias = new ArrayList<Media>();
		
		try {
			pstmt = con.prepareStatement("select * from media where type = ?");
			pstmt.setString(1, t);
	        ResultSet rs = pstmt.executeQuery();
	        while (rs.next()) {

	        	Integer mediaId = rs.getInt("media_id");
	            Integer eventId = rs.getInt("event_id");
	            String name = rs.getString("name");
	            String description = rs.getString("description");
	            String type = rs.getString("type");
	            byte[] blob = rs.getBytes("blob");
	            Timestamp updatetime = rs.getTimestamp("UPDATETIME");
	            String flag = rs.getString("flag");

	            medias.add(new Media(mediaId, name, description, type, blob, eventId, updatetime, flag));
	            
	        }
	    } catch (SQLException e ) {
	        System.out.println("ERROR");
	        e.printStackTrace();
	    } finally {
	    	if (con != null) con.close();
	        if (pstmt != null) { pstmt.close(); }
	    }
		return medias;
	}

	@Override
	public Collection<Media> getByUserDate(String user, Timestamp valueOf)
			throws SQLException {
		// TODO Auto-generated method stub
			
		
		
		return null;
	}

	@Override
	public Timestamp getUpdateTime(Integer mediaId) throws SQLException
	{
		Timestamp result = null;
		PreparedStatement pstmt = null;
		Connection con = h2connection.getConnection();
		
		try{
			pstmt = con.prepareStatement("select updatetime FROM media where media_id = ?");
			pstmt.setInt(1, mediaId);
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
	
	public void setUpdateTime(Integer mediaId, Timestamp t) throws SQLException
	{
		PreparedStatement pstmt = null;
		Connection con = h2connection.getConnection();
		
		try{
			pstmt = con.prepareStatement("update media set updatetime= ? where media_id = ?;");
			pstmt.setTimestamp(1, t);
			pstmt.setInt(2, mediaId);			
			
			pstmt.executeUpdate();
			
			//exists = false;
			
			
		} catch (SQLException e){
			System.out.println("ERROR");
			e.printStackTrace();
		}finally {
			if (con != null) con.close();
		}		
		
	}
	
	public String getFlag(Integer mediaId) throws SQLException
	{
		String result = null;
		PreparedStatement pstmt = null;
		Connection con = h2connection.getConnection();
		
		try{
			pstmt = con.prepareStatement("select flag FROM media where media_id = ?");
			pstmt.setInt(1, mediaId);
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
	
	public void setFlag(Integer mediaId, String flag) throws SQLException
	{
		PreparedStatement pstmt = null;
		Connection con = h2connection.getConnection();
		
		try{
			pstmt = con.prepareStatement("update media set flag= ? where media_id = ?;");
			pstmt.setString(1, flag);
			pstmt.setInt(2, mediaId);			
			
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
	public void create(Media media) throws SQLException {
		Connection con = h2connection.getConnection();
		PreparedStatement pstmt = null;
		
		try {
            con.setAutoCommit(false);
            pstmt = con.prepareStatement("INSERT INTO media (media_id, event_id, name, description, type, blob, UPDATETIME, flag) VALUES (?, ?, ?, ?, ?, ?, ?, ?);");
            pstmt.setInt(1, media.getMediaId());
            pstmt.setInt(2, media.getPoiId());
            pstmt.setString(3, media.getName());
            pstmt.setString(4, media.getDescription());
            pstmt.setString(5, media.getType());
            pstmt.setBinaryStream(6, new ByteArrayInputStream(media.getBlob()),media.getBlob().length);
            pstmt.setTimestamp(7, media.getUpdatetime());
            pstmt.setString(8, media.getFlag()  );
            
            pstmt.executeUpdate();

            con.commit();
            pstmt.close();

        } finally {
            if (con != null) con.close();
        }			
	}

	@Override
	public Collection<Media> getByFlagByUser(String username, String flag) throws SQLException {
		PreparedStatement pstmt = null;
		Connection con = h2connection.getConnection();
		Collection<Media> medias = new ArrayList<Media>();
		
		try {
			pstmt = con.prepareStatement("select * from account_trip ti inner join trip t on ti.trip_id = t.trip_id inner join event e on t.trip_id = e.trip_id inner join media m on e.event_id = m.event_id where m.flag = ? and ti.username = ?;");
			pstmt.setString(1, flag);
			pstmt.setString(2, username);
			
	        ResultSet rs = pstmt.executeQuery();
	        while (rs.next()) {
	        	Integer mediaId = rs.getInt("media_id");	      
	            String dname = rs.getString("name");
	            String description = rs.getString("description");
	            String type = rs.getString("type");
	            byte[] blob = rs.getBytes("blob");
	            Integer poiId = rs.getInt("event_id");
	            Timestamp dtime = rs.getTimestamp("UPDATETIME");
		            
	            medias.add(new Media(mediaId, dname, description, type, blob, poiId, dtime, flag));	            
	        }
	    } catch (SQLException e ) {
	        System.out.println("ERROR");
	        e.printStackTrace();
	    } finally {
	    	if (con != null) con.close();
	        if (pstmt != null) { pstmt.close(); }
	    }
		return medias;
	}
}
