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

import dao.TripDAO;
import dao.TripImpl;
import domain.Trip;

/**
 * Servlet implementation class TripServlet
 */
public class TripServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Gson gson = new Gson();
	private java.util.Date date= new java.util.Date();
	private TripDAO tripDAO = new TripImpl();
	private String strtype ="null";   
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public TripServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
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
		if (strtype.equals("sendnewtrips")){
			System.out.println("Send new trips from server to client");
			getNewTrips(request, response);
			
		} else if (strtype.equals("getnewtrip")){
			System.out.println("got new trip from client ");
			newTrip(request, response);
			
		} else if (strtype.equals("senddeletedtrips")){
			System.out.println("Send deleted trips");
			getDeletedTrips(request, response);
			
		} else if (strtype.equals("deletetrip")){
			System.out.println("Get deleted trips");
			deleteTrip(request, response);
		}
		}		
		strtype = "";
	}	
	
	/**
	 * This method receives a brand new trip as a json string in the request parameter, then
	 * converts it from json to a trip domain object. Then it sets the updatetime to the current 
	 * system time. Then it inserts the trip into the database using the tripDAO. 
	 * If successful it writes the updated trip object back to JSON then returns it via the the response 
	 * parameter. 
	 * If this fails, it returns the error.  
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ServletException
	 */
	public void newTrip(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		//Timestamp returnTime;
		System.out.println("=======================================================");
		System.out.println("TripServlet  -   new trip method ");
		
		String responseValue = "0"; //whether or not the method succeeds
		
		response.setContentType("text/plain");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		
		try {
			// Get the trip as a JSON string
			String newTripJson = request.getParameter("jsonobj");
		    System.out.println("Got trip JSON data: " + newTripJson);
			// Convert the trip from string back to a trip...hopefully
			Trip newTrip = gson.fromJson(newTripJson, Trip.class);
			//give the trip a timestamp with the current time
			System.out.println(newTrip.toString());
			System.out.println("Converted JSON");
			newTrip.setUpdatetime(new Timestamp(date.getTime()));		
			System.out.println("Setting updatetime");
			
			try {
				System.out.println("Adding Trip to DB");
				//add the trip into the database
				tripDAO.create(newTrip);
				System.out.println("Successfully added trip to DB");
				//if here the trip was successfully added to the database. Convert the trip back to JSON and send it back
				
				
				responseValue = newTrip.getUpdatetime().toString();
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
	public void getNewTrips(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {		
		System.out.println("=======================================================");
		System.out.println("TripServlet  -   send new trips method ");
		
		Collection<Trip> trips = new ArrayList<Trip>();
		Gson gson = new Gson();		
		
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
			
		try{
			String user = request.getParameter("username");
			System.out.println("Username: " + user);
			
			if (user != null){
				trips = tripDAO.getByFlagByUser(user, "n");
				System.out.println("Got collection of trips");
				
				System.out.println(trips.toString());
				String returnJson = gson.toJson(trips);
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
	
	/**
	 * This method receives a trip id which has a 'd' flag on the client db as a json string in the request parameter, then
	 * converts it from json to a trip domain object. Then it sets the updatetime to the current 
	 * system time. Then it change the flag of the trip on the server database using the tripDAO. 
	 * If successful it writes the updated trip_idt back to JSON then returns it via the the response 
	 * parameter. 
	 * If this fails, it returns the error.  
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ServletException
	 */
	public void deleteTrip(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		//Timestamp returnTime;
		System.out.println("=======================================================");
		System.out.println("TripServlet  -   delete trip method ");
		
		String responseValue = "0"; //whether or not the method succeeds
		
		response.setContentType("text/plain");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		
		try {

			// Get the trip as a JSON string
			String deleteTrip = request.getParameter("tripid");
		    System.out.println("Got trip ID: " + deleteTrip);

				
			System.out.println("Setting updatetime");
			
			try {
				System.out.println("Delete Trip to DB");
				
				Timestamp updatetime = new Timestamp(date.getTime());
				//add the trip into the database
				tripDAO.delete(Integer.valueOf(deleteTrip), updatetime);
				System.out.println("Successfully deleted trip in DB");
				//if here the trip was successfully added to the database. Convert the trip back to JSON and send it back

				
				
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
	
	public void getDeletedTrips(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {		
		System.out.println("=======================================================");
		System.out.println("TripServlet  -   get deleted trips method ");
		
		Collection<Trip> trips = new ArrayList<Trip>();
		Gson gson = new Gson();		
		
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		
		
		
		try{
			String user = request.getParameter("username");
			System.out.println("Username: " + user);
			
			if (user != null){
				trips = tripDAO.getByFlagByUser(user, "d");
				System.out.println("Got collection of trips");
				
				System.out.println(trips.toString());
				String returnJson = gson.toJson(trips);
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
