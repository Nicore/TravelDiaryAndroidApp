package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;

import org.h2.jdbc.JdbcSQLException;

import domain.Event;

public class EventImpl implements EventDAO {
	DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
	

	@Override
	public void create(Integer eventId, double latitude, double longitude,
			Timestamp dateTime, String name, String description, Integer tripId, Timestamp updateTime, String flag) throws SQLException{
		
		Connection con = h2connection.getConnection();
		PreparedStatement pstmt = null;
		
		/*java.util.Date parsedDate = null;
		try {
			parsedDate = df.parse(dateTime);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		java.sql.Timestamp timestamp = new java.sql.Timestamp(parsedDate.getTime());
		 */
		try {
            con.setAutoCommit(false);
            pstmt = con.prepareStatement("INSERT INTO Event (Event_id, trip_id, longitude, latitude, dtime, name, description, updatetime, flag) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);");
            pstmt.setInt(1, eventId);
            pstmt.setInt(2, tripId);
            pstmt.setDouble(3, longitude);
            pstmt.setDouble(4, latitude);
            pstmt.setTimestamp(5, dateTime);
            pstmt.setString(6, name);
            pstmt.setString(7, description);
            pstmt.setTimestamp(8, updateTime);
            pstmt.setString(9, flag);
            
            pstmt.executeUpdate();

            con.commit();
            pstmt.close();

        } finally {
            if (con != null) con.close();
        }

	}

	@Override
	public void update(Integer eventId, double lattitude, double longitude,
			Timestamp dateTime, String name, String description, Integer tripId, Timestamp updateTime, String flag)
			throws SQLException {
	
		Connection con = h2connection.getConnection();
		PreparedStatement pstmt = null;
		/*java.util.Date parsedDate = null;
		try {
			parsedDate = df.parse(dateTime);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		java.sql.Timestamp timestamp = new java.sql.Timestamp(parsedDate.getTime());
		*/
		try {
            con.setAutoCommit(false);
            pstmt = con.prepareStatement("UPDATE Event set trip_Id = ?, longitude = ?, latitude = ?, dtime = ?, Name = ?, description = ?, updatetime = ?, flag = ? where eventId = ?;");
            pstmt.setInt(1, tripId);
            pstmt.setDouble(2, longitude);
            pstmt.setDouble(3, lattitude);
            pstmt.setTimestamp(4, dateTime);
            pstmt.setString(5, name);
            pstmt.setString(6, description);
            pstmt.setTimestamp(7, updateTime);
            pstmt.setString(8, flag);
            pstmt.setInt(9, eventId);
            pstmt.executeUpdate();

            con.commit();
            pstmt.close();

        } finally {
            if (con != null) con.close();
        }
		
	}

	@Override
	public void delete(Integer eventId, Timestamp updatetime) throws SQLException{
		PreparedStatement pstmt = null;
		Connection con = h2connection.getConnection();
		
		try {
            con.setAutoCommit(false);
            pstmt = con.prepareStatement("Update event set flag = 'd', updatetime = ? where event_id = ?");
            pstmt.setTimestamp(1, updatetime);
            pstmt.setInt(2, eventId);

            pstmt.executeUpdate();

            con.commit();
            pstmt.close();

		}  finally {
            if (con != null) con.close();
        }
	}
	
	@Override
	public Collection<Event> getAll() throws SQLException{
		Statement stmt = null;
		Connection con = h2connection.getConnection();
		Collection<Event> events = new ArrayList<Event>();
		
		String query = "select * from event";
		try {
	        stmt = con.createStatement();
	        ResultSet rs = stmt.executeQuery(query);
	        while (rs.next()) {
	        	
	            Integer eventId = rs.getInt("event_id");
	            Integer tripId = rs.getInt("trip_id");
	            Double longitude = rs.getDouble("longitude");
	            Double lattitude = rs.getDouble("latitude");
	            Timestamp dtime = rs.getTimestamp("dtime");
	            String name = rs.getString("name");
	            String description = rs.getString("description");
	            Timestamp updateTime = rs.getTimestamp("UPDATETIME");
	            String flag = rs.getString("FLAG");
	            
	            events.add(new Event(eventId, longitude, lattitude, dtime, name, description, tripId, updateTime, flag));
	        }
	    } catch (SQLException e ) {
	        System.out.println("ERROR");
	        e.printStackTrace();
	    } finally {
	    	if (con != null) con.close();
	        if (stmt != null) { stmt.close(); }
	    }
		return events;
	}

	@Override
	public Collection<Event> getById(Integer id) throws SQLException{
		PreparedStatement pstmt = null;
		Connection con = h2connection.getConnection();
		Collection<Event> events = new ArrayList<Event>();
		
		try {
			pstmt = con.prepareStatement("select * from event where event_id = ?");
			pstmt.setInt(1, id);
	        ResultSet rs = pstmt.executeQuery();
	        while (rs.next()) {
	        	
	            Integer eventId = rs.getInt("event_id");
	            Integer tripId = rs.getInt("trip_id");
	            Double longitude = rs.getDouble("longitude");
	            Double latitude = rs.getDouble("latitude");
	            Timestamp dtime = rs.getTimestamp("dtime");
	            String name = rs.getString("name");
	            String description = rs.getString("description");
	            Timestamp updateTime = rs.getTimestamp("UPDATETIME");
	            String flag = rs.getString("flag");
	            
	            events.add(new Event(eventId, longitude, latitude, dtime, name, description, tripId, updateTime, flag));
	            
	        }
	    } catch (SQLException e ) {
	        System.out.println("ERROR");
	        e.printStackTrace();
	    } finally {
	    	if (con != null) con.close();
	        if (pstmt != null) { pstmt.close(); }
	    }
		return events;
	}

	@Override
	public Collection<Event> getByName(String evName) throws SQLException{
		PreparedStatement pstmt = null;
		Connection con = h2connection.getConnection();
		Collection<Event> events = new ArrayList<Event>();
		
		try {
			pstmt = con.prepareStatement("select * from event where event_id = ?");
			pstmt.setString(1, evName);
	        ResultSet rs = pstmt.executeQuery();
	        while (rs.next()) {
	        	
	            Integer eventId = rs.getInt("event_id");
	            Integer tripId = rs.getInt("trip_id");
	            Double longitude = rs.getDouble("longitude");
	            Double lattitude = rs.getDouble("latitude");
	            Timestamp dtime = rs.getTimestamp("dtime");
	            String name = rs.getString("name");
	            String description = rs.getString("description");
	            Timestamp updateTime = rs.getTimestamp("UPDATETIME");
	            String flag = rs.getString("flag");
	            
	            events.add(new Event(eventId, longitude, lattitude, dtime, name, description, tripId, updateTime, flag));	            
	        }
	    } catch (SQLException e ) {
	        System.out.println("ERROR");
	        e.printStackTrace();
	    } finally {
	    	if (con != null) con.close();
	        if (pstmt != null) { pstmt.close(); }
	    }
		return events;
	}

	@Override
	public Collection<Event> getByTripId(Integer tripId) throws SQLException{
		PreparedStatement pstmt = null;
		Connection con = h2connection.getConnection();
		Collection<Event> events = new ArrayList<Event>();
		
		try {
			pstmt = con.prepareStatement("select * from event where event_id = ?");
			pstmt.setInt(1, tripId);
	        ResultSet rs = pstmt.executeQuery();
	        while (rs.next()) {
	        	
	            Integer eventId = rs.getInt("event_id");
	            Integer tId = rs.getInt("trip_id");
	            Double longitude = rs.getDouble("longitude");
	            Double lattitude = rs.getDouble("latitude");
	            Timestamp dtime = rs.getTimestamp("dtime");
	            String name = rs.getString("name");
	            String description = rs.getString("description");
	            Timestamp updateTime = rs.getTimestamp("UPDATETIME");
	            String flag = rs.getString("flag");
	            
	            events.add(new Event(eventId, longitude, lattitude, dtime, name, description, tId, updateTime, flag));
	            
	        }
	    } catch (SQLException e ) {
	        System.out.println("ERROR");
	        e.printStackTrace();
	    } finally {
	    	if (con != null) con.close();
	        if (pstmt != null) { pstmt.close(); }
	    }
		return events;
	}

	@Override
	public Collection<Event> getByUserDate(String username, Timestamp date)
			throws SQLException {
		
		PreparedStatement pstmt = null;
		Connection con = h2connection.getConnection();
		Collection<Event> events = new ArrayList<Event>();
		
		try {
			pstmt = con.prepareStatement("select * from event e inner join trip t on e.trip_id = t.trip_id where e.updatetime > ? and t.username = ?");

			pstmt.setString(2, username);
			pstmt.setTimestamp (1, date);
	        ResultSet rs = pstmt.executeQuery();
	        while (rs.next()) {
	        	
	        	Integer eventId = rs.getInt("event_id");
	            Integer tripId = rs.getInt("trip_id");
	            Double longi = rs.getDouble("longitude");
	            Double lat = rs.getDouble("latitude");
	            Timestamp dtime = rs.getTimestamp("dtime");
	            String name = rs.getString("name");
	            String description = rs.getString("name");
	            Timestamp updateTime = rs.getTimestamp("UPDATETIME");
	            String flag = rs.getString("flag");
	            
	            events.add(new Event(eventId, lat, longi, dtime, name, description, tripId, updateTime, flag));
	            
	        }
	    } catch (SQLException e ) {
	        System.out.println("ERROR");
	        e.printStackTrace();
	    } finally {
	    	if (con != null) con.close();
	        if (pstmt != null) { pstmt.close(); }
	    }
		return events;
	}
	
	@Override
	public Timestamp getUpdateTime(Integer eventId) throws SQLException {
		Timestamp result = null;
		PreparedStatement pstmt = null;
		Connection con = h2connection.getConnection();
		
		try{
			pstmt = con.prepareStatement("select updatetime FROM event where event_id = ?");
			pstmt.setInt(1, eventId);
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
	public void setUpdateTime(Integer eventId, Timestamp t) throws SQLException {
		PreparedStatement pstmt = null;
		Connection con = h2connection.getConnection();
		
		try{
			pstmt = con.prepareStatement("update event set updatetime= ? where event_id = ?;");
			pstmt.setTimestamp(1, t);
			pstmt.setInt(2, eventId);			
			
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
	public String getFlag(Integer eventId) throws SQLException {
		String result = null;
		PreparedStatement pstmt = null;
		Connection con = h2connection.getConnection();
		
		try{
			pstmt = con.prepareStatement("select flag FROM event where event_id = ?");
			pstmt.setInt(1, eventId);
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
	public void setFlag(Integer eventId, String flag) throws SQLException {
		PreparedStatement pstmt = null;
		Connection con = h2connection.getConnection();
		
		try{
			pstmt = con.prepareStatement("update event set flag= ? where event_id = ?;");
			pstmt.setString(1, flag);
			pstmt.setInt(2, eventId);			
			
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
	public void create(Event event) throws SQLException {
		Connection con = h2connection.getConnection();
		PreparedStatement pstmt = null;
		
		try {
            con.setAutoCommit(false);
            pstmt = con.prepareStatement("INSERT INTO Event (Event_id, trip_id, longitude, latitude, dtime, name, description, updatetime, flag) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);");
            pstmt.setInt(1, event.getPoiId());
            pstmt.setInt(2, event.getTripId());
            pstmt.setDouble(3, event.getLongitude());
            pstmt.setDouble(4, event.getLatitude());
            pstmt.setTimestamp(5, event.getDtime());
            pstmt.setString(6, event.getName());
            pstmt.setString(7, event.getDescription());
            pstmt.setTimestamp(8, event.getUpdatetime());            
            pstmt.setString(9, event.getFlag());
            
            pstmt.executeUpdate();

            con.commit();
            pstmt.close();
            
            //primary key violation, ignore
		} catch (JdbcSQLException e) {
			System.out.println("Primary Key violation in sync, ignoring.");
        } finally {
            if (con != null) con.close();
        }	
	}

	@Override
	public Collection<Event> getByFlagByUser(String username, String flag) throws SQLException {
		PreparedStatement pstmt = null;
		Connection con = h2connection.getConnection();
		Collection<Event> events = new ArrayList<Event>();
		
		try {
			//pstmt = con.prepareStatement("select * from account_trip ti inner join trip t on ti.trip_id = t.trip_id;");
			pstmt = con.prepareStatement("select * from (select ti.username as username, ti.trip_id as trip_id from account_trip ti inner join trip t on ti.trip_id = t.trip_id inner join event e on t.trip_id = e.trip_id where ti.flag = ? and ti.username = ?) eti inner join event e on eti.trip_id = e.trip_id;");
			pstmt.setString(1, flag);
			pstmt.setString(2, username);
			
	        ResultSet rs = pstmt.executeQuery();
	        while (rs.next()) {	            
	        	Integer eventId = rs.getInt("event_id");
	        	Integer tripId = rs.getInt("trip_id");
	        	Double lo = rs.getDouble("longitude");
	        	Double la = rs.getDouble("latitude");
	        	Timestamp dtime = rs.getTimestamp("dtime");
	            String name = rs.getString("name");	            
	            String description = rs.getString("description");	            
	            Timestamp uptime = rs.getTimestamp("UPDATETIME");
	            
	            events.add(new Event(eventId, lo, la, dtime, name, description, tripId, uptime, flag));	            
	        }
	    } catch (SQLException e ) {
	        System.out.println("ERROR");
	        e.printStackTrace();
	    } finally {
	    	if (con != null) con.close();
	        if (pstmt != null) { pstmt.close(); }
	    }
		
		System.out.println(events.toString());
		return events;
	}
}
