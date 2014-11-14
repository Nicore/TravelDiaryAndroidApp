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

import dao.CoordinateDAO;
import dao.CoordinateImpl;
import domain.Coordinate;

/**
 * Servlet implementation class CoordinateServlet
 */
public class CoordinateServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Gson gson = new Gson();
	private java.util.Date date= new java.util.Date();
	private CoordinateDAO coordinateDAO = new CoordinateImpl();
	private String strtype = "";
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CoordinateServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try{
			strtype = request.getParameter("reqtype");
		} catch (Exception e){
			e.printStackTrace();
		}
		
		
		if (strtype != null){

			strtype = strtype.toLowerCase();
			if (strtype.equals("sendnewcoords")){
				System.out.println("Send new coords from server to client");
				getNewCoordinates(request, response);
				
				
			} else if (strtype.equals("getnewcoord")){
				System.out.println("got new coords from client ");
				newCoordinate(request, response);
				
	
			}
		}
		strtype = "";
		
		// TODO Auto-generated method stub
	}
	
	
	/**
	 * This method receives a brand new coordinate as a json string in the request parameter, then
	 * converts it from json to a coordinate domain object. Then it sets the updatetime to the current 
	 * system time. Then it inserts the coordinate into the database using the coordinateDAO. 
	 * If successful it writes the updated coordinate object back to JSON then returns it via the the response 
	 * parameter. 
	 * If this fails, it returns the error.  
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ServletException
	 */
	public void newCoordinate(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		//Timestamp returnTime;
		
		String responseValue = "0"; //whether or not the method succeeds
		
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		
		try {
			// Get the coordinate as a JSON string
			String newCoordinateJson = request.getParameter("jsonobj");
			// Convert the coordinate from string back to a coordinate...hopefully
			System.out.println("-=-=-=-=-=-=-=-=-=-=-=--=-=");
			System.out.println(newCoordinateJson);
			Coordinate newCoordinate = gson.fromJson(newCoordinateJson, Coordinate.class);
			
			//give the coordinate a timestamp with the current time
			newCoordinate.setDatetime(new Timestamp(date.getTime()));		
			
			try {
				//add the coordinate into the database
				coordinateDAO.create(newCoordinate);
				//if here the coordinate was successfully added to the database. Convert the coordinate back to JSON and send it back
				//String newCoordinateJson2 = gson.toJson(newCoordinate);
				responseValue = newCoordinate.getDatetime().toString();
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
	public void getNewCoordinates(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {		
		Collection<Coordinate> coordinates = new ArrayList<Coordinate>();
		Gson gson = new Gson();		
		
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		
		try{
			String user = request.getParameter("username");
			
			System.out.println(user);
			
			if (user != null){
				coordinates = coordinateDAO.getNewByUser(user);
				
				String returnJson = gson.toJson(coordinates);
				
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
}
