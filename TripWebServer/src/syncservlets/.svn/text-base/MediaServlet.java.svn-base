package syncservlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import dao.MediaDAO;
import dao.MediaImpl;
import domain.Media;

/**
 * Servlet implementation class MediaServlet
 */
public class MediaServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Gson gson = new Gson();
	private java.util.Date date= new java.util.Date();
	private MediaDAO mediaDAO = new MediaImpl();
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MediaServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String strtype = "";
		try{
			strtype = request.getParameter("reqtype");
		} catch (Exception e){
			e.printStackTrace();
		}
		
		
		if (strtype != null){

		strtype = strtype.toLowerCase();
		if (strtype.equals("sendnewmedias")){
			System.out.println("Send new medias");
			getNewMedia(request, response);
			
		} else if (strtype.equals("getnewmedia")){
			System.out.println("Get new medias");
			newMedia(request, response);
			
		}else if (strtype.equals("senddeletedmedias")){
			System.out.println("Send deleted medias");
			getDeletedMedias(request, response);
		} else if (strtype.equals("deletemedia")){
			System.out.println("Get deleted medias");
			deleteMedia(request, response);
		} 
		}
	}
	
	
	/**
	 * This method receives a brand new media as a json string in the request parameter, then
	 * converts it from json to a media domain object. Then it sets the updatetime to the current 
	 * system time. Then it inserts the media into the database using the mediaDAO. 
	 * If successful it writes the updated media object back to JSON then returns it via the the response 
	 * parameter. 
	 * If this fails, it returns the error.  
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ServletException
	 */
	public void newMedia(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		//Timestamp returnTime;
		System.out.println("=======================================================");
		System.out.println("MediaServlet  -   new media method ");
		
		String responseValue = "0"; //whether or not the method succeeds
		
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		
		try {
			// Get the media as a JSON string
			String newMediaJson = request.getParameter("jsonobj");
			System.out.println("Got media JSON data: " + newMediaJson);
			// Convert the media from string back to a media...hopefully
			Media newMedia = gson.fromJson(newMediaJson, Media.class);
			System.out.println("Converted JSON");
			//give the media a timestamp with the current time
			newMedia.setUpdatetime(new Timestamp(date.getTime()));		
			System.out.println("Setting updatetime");
			
			try {
				//add the media into the database
				System.out.println("Adding Media to DB");
				mediaDAO.create(newMedia);
				System.out.println("Successfully added media to DB");
				
				//if here the media was successfully added to the database. Convert the media back to JSON and send it back
				responseValue = newMedia.getUpdatetime().toString();
				System.out.println("ResponseValue: " + responseValue);
				out.write(responseValue);		
				System.out.println("Wrote response successfully");			
			} catch (SQLException e) { //unless the database is broken, then throw some exceptions
				out.write("Exception in SQL "+e.toString());
				e.printStackTrace();
			} catch (Exception e){ //or maybe something else is broken
				out.write("Exception in entire operation "+ e.toString());
				e.printStackTrace();
			} finally {
				out.flush();
				out.close();
			} 			
		} catch (Exception e) { //unless something bad happened
			out.write("Exception in withdrawl "+responseValue); //in which case tell me what
			e.printStackTrace();
			out.flush();
			out.close();
		} 		
	}
	
	/**  
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ServletException
	 */
	public void getNewMedia(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {		
		System.out.println("=======================================================");
		System.out.println("MediaServlet  -   get new medias method ");
		
		Collection<Media> medias = new ArrayList<Media>();
		Gson gson = new Gson();		
		
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		
		try{
			String user = request.getParameter("username");
			System.out.println("Username: " + user);
			
			if (user != null){
				medias = mediaDAO.getByFlagByUser(user, "n");
				System.out.println("Got collection of media");
				
				System.out.println(medias.toString());
				String returnJson = gson.toJson(medias);
				System.out.println("Converted collection to JSON");
				
				out.write(returnJson);
				System.out.println("Successfully written to out");
				
			} else {
				out.write("null");
			}
			
		} catch (SQLException e) { //unless the database is broken, then throw some exceptions
			out.write("Exception in SQL "+e.toString());
			e.printStackTrace();
		} catch (Exception e){ //or maybe something else is broken
			out.write("Exception in entire operation "+ e.toString());
			e.printStackTrace();
		} finally {
			out.flush();
			out.close();
		}
	}

	/*
	 * This method receives a media id which has a 'd' flag on the client db as a json string in the request parameter, then
	 * converts it from json to a media domain object. Then it sets the updatetime to the current 
	 * system time. Then it change the flag of the media on the server database using the mediaDAO. 
	 * If successful it writes the updated media_id back to JSON then returns it via the the response 
	 * parameter. 
	 * If this fails, it returns the error.  
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ServletException
	 */
	public void deleteMedia(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		//Timestamp returnTime;
		System.out.println("=======================================================");
		System.out.println("MediaServlet  -   delete media method ");
		
		String responseValue = "0"; //whether or not the method succeeds
		
		response.setContentType("text/plain");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		
		try {

			// Get the media as a JSON string
			String deleteMedia = request.getParameter("mediaid");
		    System.out.println("Got media ID: " + deleteMedia);

				
			System.out.println("Setting updatetime");
			
			try {
				System.out.println("Delete Media to DB");
				
				Timestamp updatetime = new Timestamp(date.getTime());
				//add the media into the database
				mediaDAO.delete(Integer.valueOf(deleteMedia), updatetime);
				System.out.println("Successfully deleted media in DB");
				//if here the media was successfully added to the database. Convert the media back to JSON and send it back

				
				
				responseValue = (updatetime.toString());
				System.out.println("ResponseValue: " + responseValue);
				out.write(responseValue);		
				System.out.println("Wrote response successfully");
			} catch (SQLException e) { //unless the database is broken, then throw some exceptions
				out.write("Exception in SQL "+e.toString());
				e.printStackTrace();
			} catch (Exception e){ //or maybe something else is broken
				out.write("Exception in entire operation "+ e.toString());
				e.printStackTrace();
			} finally {
				out.flush();
				out.close();
			} 			
		} catch (Exception e) { //unless something bad happened
			out.write("Exception in withdrawl "+responseValue); //in which case tell me what
			e.printStackTrace();
			out.flush();
			out.close();
		} 		
	}
	
	public void getDeletedMedias(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {		
		System.out.println("=======================================================");
		System.out.println("MediaServlet  -   get deleted medias method ");
		
		Collection<Media> medias = new ArrayList<Media>();
		Gson gson = new Gson();		
		
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		
		
		
		try{
			String user = request.getParameter("username");
			System.out.println("Username: " + user);
			
			if (user != null){
				medias = mediaDAO.getByFlagByUser(user, "d");
				System.out.println("Got collection of medias");
				
				System.out.println(medias.toString());
				String returnJson = gson.toJson(medias);
				System.out.println("Converted collection to JSON");
				
				out.write(returnJson);
				
				System.out.println("Successfully written to out:  " + returnJson);
				
			} else {
				out.write("null");
			}
			
		} catch (SQLException e) { //unless the database is broken, then throw some exceptions
			out.write("Exception in SQL "+e.toString());
			e.printStackTrace();
		} catch (Exception e){ //or maybe something else is broken
			out.write("Exception in entire operation "+ e.toString());
			e.printStackTrace();
		} finally {
			out.flush();
			out.close();
		}
	}
	
}
