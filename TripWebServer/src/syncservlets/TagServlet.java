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

import dao.TagDAO;
import dao.TagImpl;
import domain.Tag;

/**
 * Servlet implementation class TagServlet
 */
public class TagServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Gson gson = new Gson();
	private java.util.Date date= new java.util.Date();
	private TagDAO tagDAO = new TagImpl();
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public TagServlet() {
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
		if (strtype.equals("sendnewtags")){
			System.out.println("Send new tags");
			getNewTags(request, response);
			
		} else if (strtype.equals("getnewtag")){
			System.out.println("Get new tags");
			newTag(request, response);
		}else if (strtype.equals("senddeletedtags")){
			System.out.println("Send deleted tags");
			getDeletedTags(request, response);
			
		} else if (strtype.equals("deletetag")){
			System.out.println("Get deleted tags");
			deleteTag(request, response);
		
		} 
		}
	}
		
	/**
	 * This method receives a brand new tag as a json string in the request parameter, then
	 * converts it from json to a tag domain object. Then it sets the updatetime to the current 
	 * system time. Then it inserts the tag into the database using the tagDAO. 
	 * If successful it writes the updated tag object back to JSON then returns it via the the response 
	 * parameter. 
	 * If this fails, it returns the error.  
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ServletException
	 */
	public void newTag(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		//Timestamp returnTime;
		
		String responseValue = "0"; //whether or not the method succeeds
		
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		
		try {
			// Get the tag as a JSON string
			String newTagJson = request.getParameter("jsonobj");
			// Convert the tag from string back to a tag...hopefully
			Tag newTag = gson.fromJson(newTagJson, Tag.class);
			//give the tag a timestamp with the current time
			newTag.setUpdatetime(new Timestamp(date.getTime()));		
			
			try {
				//add the tag into the database
				tagDAO.create(newTag);
				//if here the tag was successfully added to the database. Convert the tag back to JSON and send it back
				String newTagJson2 = gson.toJson(newTag);
				responseValue = (newTagJson2);
				out.write(responseValue);				
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
	public void getNewTags(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {		
		Collection<Tag> tags = new ArrayList<Tag>();
		Gson gson = new Gson();		
		
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		
		try{
			String user = request.getParameter("username");
			String tagType = request.getParameter("tagtype");
			
			if (user != null){
				tags = tagDAO.getByFlagByUser(user, tagType, "n");
				
				String returnJson = gson.toJson(tags);
				
				out.write(returnJson);
				
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
	 * This method receives a tag id which has a 'd' flag on the client db as a json string in the request parameter, then
	 * converts it from json to a tag domain object. Then it sets the updatetime to the current 
	 * system time. Then it change the flag of the tag on the server database using the tagDAO. 
	 * If successful it writes the updated tag_idt back to JSON then returns it via the the response 
	 * parameter. 
	 * If this fails, it returns the error.  
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ServletException
	 */
	public void deleteTag(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		//Timestamp returnTime;
		System.out.println("=======================================================");
		System.out.println("TagServlet  -   delete tag method ");
		
		String responseValue = "0"; //whether or not the method succeeds
		
		response.setContentType("text/plain");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		
		try {

			// Get the tag as a JSON string
			String deleteTag = request.getParameter("tagid");
			String deleteTagType = request.getParameter("tagtype");
		    System.out.println("Got tag ID: " + deleteTag + "tag type: " + deleteTagType);

				
			System.out.println("Setting updatetime");
			
			try {
				System.out.println("Delete Tag from DB");
				
				Timestamp updatetime = new Timestamp(date.getTime());
				//delete the tag from the database
				tagDAO.delete(Integer.valueOf(deleteTag), deleteTagType, updatetime);
				System.out.println("Successfully deleted tag in DB");
				//if here the tag was successfully added to the database. Convert the tag back to JSON and send it back

				
				
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
	
	public void getDeletedTags(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {		
		System.out.println("=======================================================");
		System.out.println("TagServlet  -   get deleted tags method ");
		
		Collection<Tag> tags = new ArrayList<Tag>();
		Gson gson = new Gson();		
		
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
			
		try{
			String user = request.getParameter("username");
			String tagType = request.getParameter("tagtype");
			System.out.println("Username: " + user);
			
			if (user != null){
				tags = tagDAO.getByFlagByUser(user, tagType, "d");
				System.out.println("Got collection of tags");
				
				System.out.println(tags.toString());
				String returnJson = gson.toJson(tags);
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
