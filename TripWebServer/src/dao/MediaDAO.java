package dao;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Collection;

import domain.Media;

public interface MediaDAO {
	
		public void create(Integer mediaId, String name, String description, String type, byte[] blob, Integer eventId, Timestamp updatetime, String flag) throws SQLException;
		
		public void update(Integer mediaId, String name, String description, String type, byte[] blob, Integer eventId, Timestamp updatetime, String flag) throws SQLException;
		
		public void updateAudioToText(Integer currentId, Integer newId, byte[] audio)throws SQLException;
		
		public void delete(Integer mediaId, Timestamp updatetime)throws SQLException;
		
		public Collection<Media> getAll()throws SQLException;
		
		public Collection<Media> getByEvent(Integer eventId)throws SQLException;
		
		public Collection<Media> getByName(String name)throws SQLException;
		
		public Collection<Media> getByType(String type)throws SQLException;

		public Collection<Media> getByUserDate(String user, Timestamp valueOf) throws SQLException;

		public Timestamp getUpdateTime(Integer mediaId) throws SQLException;
		
		public void setUpdateTime(Integer mediaId, Timestamp t) throws SQLException;
		
		public String getFlag(Integer mediaId) throws SQLException;
		
		public void setFlag(Integer mediaId, String flag) throws SQLException;
		
		public void create(Media media) throws SQLException;
		
		public Collection<Media> getByFlagByUser(String username, String flag) throws SQLException;
	}


