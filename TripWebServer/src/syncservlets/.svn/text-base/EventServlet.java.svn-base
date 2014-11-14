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

import dao.EventDAO;
import dao.EventImpl;
import domain.Event;

/**
 * Servlet implementation class EventServlet
 */
public class EventServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Gson gson = new Gson();
	private java.util.Date date= new java.util.Date();
	private EventDAO eventDAO = new EventImpl();
	private String strtype ="null";
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EventServlet() {
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
		// TODO Auto-generated method stub
		//strtype = "null";
		try{
			strtype = request.getParameter("reqtype");
		} catch (Exception e){
			e.printStackTrace();
		}
		
		
		if (strtype != null){

		strtype = strtype.toLowerCase();
		if (strtype.equals("sendnewevents")){
			System.out.println("Send new events");
			getNewEvents(request, response);
			
		} else if (strtype.equals("getnewevent")){
			System.out.println("Get new events");
			newEvent(request, response);
			
		}else if (strtype.equals("senddeletedevents")){
			System.out.println("Send deleted events");
			getDeletedEvents(request, response);
			
		} else if (strtype.equals("deleteevent")){
			System.out.println("Get deleted events");
			deleteEvent(request, response);			
		} 
		}
	}
	
	
	/**
	 * This method receives a brand new event as a json string in the request parameter, then
	 * converts it from json to a event domain object. Then it sets the updatetime to the current 
	 * system time. Then it inserts the event into the database using the eventDAO. 
	 * If successful it writes the updated event object back to JSON then returns it via the the response 
	 * parameter. 
	 * If this fails, it returns the error.  
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ServletException
	 */
	public void newEvent(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		//Timestamp returnTime;
		System.out.println("=======================================================");
		System.out.println("EventServlet  -   new event method ");
		String responseValue = "0"; //whether or not the method succeeds
		//SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS");
		
		
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		
		try {
			// Get the event as a JSON string
			String newEventJson = request.getParameter("jsonobj");
			System.out.println("Got event JSON data: " + newEventJson);
			// Convert the event from string back to a event...hopefully
			Event newEvent = gson.fromJson(newEventJson, Event.class);
			System.out.println("Converted JSON");
			//give the event a timestamp with the current time
			newEvent.setUpdatetime(new Timestamp(date.getTime()));		
			System.out.println("Setting updatetime");
			
			try {
				System.out.println("Adding Event to DB");
				//add the event into the database
				eventDAO.create(newEvent);
				System.out.println("Successfully added event to DB");

				responseValue = newEvent.getUpdatetime().toString();
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
	public void getNewEvents(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {		
		System.out.println("=======================================================");
		System.out.println("EventServlet  -   get new events method ");
		
		Collection<Event> events = new ArrayList<Event>();
		Gson gson = new Gson();		
		
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		
		try{
			String user = request.getParameter("username");
			System.out.println("Username: " + user);
			
			if (user != null){
				events = eventDAO.getByFlagByUser(user, "n");
				System.out.println("Got collection of events");
				
				System.out.println(events.toString());
				
				String returnJson = gson.toJson(events);
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

	/* 
	 * This method receives a event id which has a 'd' flag on the client db as a json string in the request parameter, then
	 * converts it from json to a event domain object. Then it sets the updatetime to the current 
	 * system time. Then it change the flag of the event on the server database using the eventDAO. 
	 * If successful it writes the updated event_id back to JSON then returns it via the the response 
	 * parameter. 
	 * If this fails, it returns the error.  
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ServletException
	 */
	public void deleteEvent(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		//Timestamp returnTime;
		System.out.println("=======================================================");
		System.out.println("EventServlet  -   delete event method ");
		
		String responseValue = "0"; //whether or not the method succeeds
		
		response.setContentType("text/plain");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		
		try {

			// Get the event as a JSON string
			String deleteEvent = request.getParameter("eventId");
		    System.out.println("Got event ID: " + deleteEvent);

		    if (deleteEvent != null){
		    	
		    
				
			System.out.println("Setting updatetime");
			
			try {
				System.out.println("Delete Event to DB");
				
				Timestamp updatetime = new Timestamp(date.getTime());
				//add the event into the database
				eventDAO.delete(Integer.valueOf(deleteEvent), updatetime);
				System.out.println("Successfully deleted event in DB");
				//if here the event was successfully added to the database. Convert the event back to JSON and send it back
			
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
			
		    }
		    
		} catch (Exception e) { //unless something bad happened
			out.write("Exception in withdrawl "+responseValue); //in which case tell me what
			e.printStackTrace();
			out.flush();
			out.close();
		} 		
	}
	
	public void getDeletedEvents(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {		
		System.out.println("=======================================================");
		System.out.println("EventServlet  -   get deleted events method ");
		
		Collection<Event> events = new ArrayList<Event>();
		Gson gson = new Gson();		
		
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
				
		try{
			String user = request.getParameter("username");
			System.out.println("Username: " + user);
			
			if (user != null){
				events = eventDAO.getByFlagByUser(user, "d");
				System.out.println("Got collection of events");
				
				System.out.println(events.toString());
				String returnJson = gson.toJson(events);
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
