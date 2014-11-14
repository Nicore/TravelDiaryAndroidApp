/**
* @file CommentDAO.java
* Interface to show methods and parameters to be used for interacting with comment tables in DB
* @peerReview
* @date 15/09/2012
* @author Nick Comer
*/
package dao;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Collection;

/*import domain.Comment;*/


public interface CommentDAO {
	
	public void create(Integer id, Integer fid, String text, String type, String alias, Timestamp tstamp) throws SQLException;
	
	public void update(Integer id, Integer fid, String text, String type, String alias, Timestamp tstamp) throws SQLException ;
	
	public void delete(Integer id, String type) throws SQLException;
	
	/*public Collection<Comment> getAll(String type) throws SQLException;
	
	public Collection<Comment> getById(Integer Id, String type)throws SQLException;	
	
	public Collection<Comment> getByAlias(String alias, String type)throws SQLException;
	
	public Collection<Comment> getByDate(Timestamp date, String type) throws SQLException;*/
}
