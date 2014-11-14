/**
* h2Connection
* Class to connect to h2 database
* @peerReview
* 24/04/2012
* Author: Daniel Jonker
*/
package dao;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.PropertyResourceBundle;


public class h2connection {
	
	/**
	* Connects to DB
	* @throws SQLException       
	* @return Connection object
	*/
	public static Connection getConnection() throws SQLException{
		Connection conn = null;
		FileInputStream pFile = null;
		try {
			// Load properties file with username and password details.
			//System.out.println("Working Directory = " + System.getProperty("user.dir"));
			//pFile = new FileInputStream( "DBConnection.properties" );
			
			Properties properties = new Properties();
			//try {
			  //properties.load(new FileInputStream("WEB-INF/DBConnection.properties"));
			  
			  Class.forName("org.h2.Driver");
			  //conn = DriverManager.getConnection(properties.getProperty("DBURL"), properties.getProperty("USERNAME"), properties.getProperty("PASSWORD"));
			  conn = DriverManager.getConnection("jdbc:h2:tcp://info-nts-12.otago.ac.nz/projects/info401", "info401user", "401");
			  
			  
			//} catch (IOException e) {
			  //e.printStackTrace();
			//} 
		

		      //ResourceBundle ldProp = ResourceBundle.getBundle("dao.resources.DBConnection.properties");
		      
		      
		    

		} //try
		catch (ClassNotFoundException e) {
			System.out.println("Error");
			e.printStackTrace();
		}  finally {
            try {
            	if (pFile != null){
            		pFile.close();
            	}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
      }
		return conn;
	} // getConnection

	
	
	
	public static void main(String [] args) throws SQLException {

		
	} //main
	
} //class h2Connection
