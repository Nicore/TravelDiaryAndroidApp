package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;



import domain.Account;

public class AccountImpl implements AccountDAO{
	
	
	@Override
	public void create(String username, String password, String email, String firstName, String lastName, Timestamp updateTime, String flag) throws SQLException {
		Connection con = h2connection.getConnection();
		PreparedStatement pstmt = null;
		
		try {
            con.setAutoCommit(false);
            pstmt = con.prepareStatement("INSERT INTO ACCOUNT (username, password, email, first_name, last_name, UPDATETIME, flag) VALUES (?, ?, ?, ?, ?, ?, ?);");
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.setString(3, email);
            pstmt.setString(4, firstName);
            pstmt.setString(5, lastName);
            pstmt.setTimestamp(6, updateTime);
            pstmt.setString(7, flag);
            pstmt.executeUpdate();

            con.commit();
            pstmt.close();

        } finally {
            if (con != null) con.close();
        }
		
	}

	@Override
	public void update(String username, String email, String firstName, String lastName, Timestamp updateTime, String flag) throws SQLException {
		Connection con = h2connection.getConnection();
		PreparedStatement pstmt = null;
		
		try {
            con.setAutoCommit(false);
            pstmt = con.prepareStatement("UPDATE account set email = ?, first_name = ?, last_name = ?, UPDATETIME = ?, flag = ? where username = ?;");
            pstmt.setString(1, email);
            pstmt.setString(2, firstName);
            pstmt.setString(3, lastName);
            pstmt.setTimestamp(4, updateTime);
            pstmt.setString(5, flag);
            pstmt.setString(6, username);
            
            pstmt.executeUpdate();

            con.commit();
            pstmt.close();

        } finally {
            if (con != null) con.close();
        }
		
	}

	@Override
	public void changePassword(String username, String oldPassword, String newPassword) throws SQLException {
		Connection con = h2connection.getConnection();
		PreparedStatement pstmt = null;
		
		try {
            con.setAutoCommit(false);
            pstmt = con.prepareStatement("UPDATE account set password = ? where username = ? and password = ?;");
            pstmt.setString(1, newPassword);
            pstmt.setString(2, username);
            pstmt.setString(3, oldPassword);
            pstmt.executeUpdate();

            con.commit();
            pstmt.close();

        } finally {
            if (con != null) con.close();
        }
		
		
		
		
	}

	@Override
	public void delete(String username) throws SQLException {
		Connection con = h2connection.getConnection();
		PreparedStatement pstmt = null;
		
		try {
            con.setAutoCommit(false);
            pstmt = con.prepareStatement("DELETE FROM account where username = ?");
            pstmt.setString(1, username);

            pstmt.executeUpdate();

            con.commit();
            pstmt.close();

		}  finally {
            if (con != null) con.close();
        }
		
	}

	@Override
	public Account getByUsername(String username) throws SQLException {
		Account account = null;
		PreparedStatement pstmt = null;
		Connection con = h2connection.getConnection();
		
		try{
			pstmt = con.prepareStatement("select * FROM account where username = ?");
			pstmt.setString(1, username);
			ResultSet rs = pstmt.executeQuery();
			rs.first();
			
			account = new Account (rs.getString("USERNAME"), rs.getString("PASSWORD"), rs.getString("EMAIL"), rs.getString("FIRST_NAME"), rs.getString("LAST_NAME"), rs.getTimestamp("UPDATETIME"), rs.getString("FLAG"));
			
		} catch (SQLException e){
			System.out.println("ERROR");
			e.printStackTrace();
		}finally {
			if (con != null) con.close();
		}
		
		return account;
	}

	@Override
	public Account getByEmail(String email) throws SQLException {
		Account account = null;
		PreparedStatement pstmt = null;
		Connection con = h2connection.getConnection();
		
		try{
			pstmt = con.prepareStatement("select * FROM account where email = ?");
			pstmt.setString(1, email);
			ResultSet rs = pstmt.executeQuery();
			rs.first();
			
			account = new Account (rs.getString("USERNAME"), rs.getString("PASSWORD"), rs.getString("EMAIL"), rs.getString("FIRST_NAME"), rs.getString("LAST_NAME"), rs.getTimestamp("UPDATETIME"), rs.getString("FLAG"));
			
		} catch (SQLException e){
			System.out.println("ERROR");
			e.printStackTrace();
		}finally {
			if (con != null) con.close();
		}
		
		return account;
	}

	@Override
	public Collection<Account> getAll() throws SQLException {
		Statement stmt = null;
		Connection con = h2connection.getConnection();
		Collection<Account> accounts = new ArrayList<Account>();
		
		String query = "select * from account";
		try {
	        stmt = con.createStatement();
	        ResultSet rs = stmt.executeQuery(query);
	        while (rs.next()) {
	            String username = rs.getString("USERNAME");
	            String password = rs.getString("PASSWORD");
	            String email = rs.getString("EMAIL");
	            String firstName = rs.getString("FIRST_NAME");
	            String lastName = rs.getString("LAST_NAME");
	            Timestamp updateTime = rs.getTimestamp("UPDATETIME");
	            String flag = rs.getString("FLAG");
	            
	            accounts.add(new Account(username, password, email, firstName, lastName, updateTime, flag));
	            
	            System.out.println(username + "\t" + password + "\t" + email + "\t" + firstName + "\t" + lastName + "\t" + updateTime + "\t" + flag);               
	        }
	    } catch (SQLException e ) {
	        System.out.println("ERROR");
	        e.printStackTrace();
	    } finally {
	    	if (con != null) con.close();
	        if (stmt != null) { stmt.close(); }
	    }	
		
		return accounts;
	}

	@Override
	public boolean usernameExists(String username) throws SQLException {
		//boolean exists = true;
		PreparedStatement pstmt = null;
		Connection con = h2connection.getConnection();
		
		try{
			pstmt = con.prepareStatement("select count(*) FROM account where username = ?");
			pstmt.setString(1, username);
			ResultSet rs = pstmt.executeQuery();
			rs.first();
			
			int result = rs.getInt(1);
			if (result != 0){
				return true;
			} else {
				return false;
			}
			//exists = false;
			
			
		} catch (SQLException e){
			System.out.println("ERROR");
			e.printStackTrace();
		}finally {
			if (con != null) con.close();
		}
		
		return true;
		//return exists;
	}

	
	/*
	 * (non-Javadoc)
	 * @see dao.AccountDAO#getUpdateTime(java.lang.String)
	 * timestamp format is: yyyy-MM-dd hh:mm:ss
	 */
	@Override
	public Timestamp getUpdateTime(String username) throws SQLException {
		Timestamp result = null;
		PreparedStatement pstmt = null;
		Connection con = h2connection.getConnection();
		
		try{
			pstmt = con.prepareStatement("select updatetime FROM account where username = ?");
			pstmt.setString(1, username);
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
	public void setUpdateTime(String username, Timestamp t) throws SQLException {
		PreparedStatement pstmt = null;
		Connection con = h2connection.getConnection();
		
		try{
			pstmt = con.prepareStatement("update account set updatetime= ? where username = ?;");
			pstmt.setTimestamp(1, t);
			pstmt.setString(2, username);			
			
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
	public String getFlag(String username) throws SQLException {
		String result = null;
		PreparedStatement pstmt = null;
		Connection con = h2connection.getConnection();
		
		try{
			pstmt = con.prepareStatement("select flag FROM account where username = ?");
			pstmt.setString(1, username);
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
	public void setFlag(String username, String flag) throws SQLException {
		PreparedStatement pstmt = null;
		Connection con = h2connection.getConnection();
		
		try{
			pstmt = con.prepareStatement("update account set flag= ? where username = ?;");
			pstmt.setString(1, flag);
			pstmt.setString(2, username);			
			
			pstmt.executeUpdate();
			
			//exists = false;			
			
		} catch (SQLException e){
			System.out.println("ERROR");
			e.printStackTrace();
		}finally {
			if (con != null) con.close();
		}		
	}
}
