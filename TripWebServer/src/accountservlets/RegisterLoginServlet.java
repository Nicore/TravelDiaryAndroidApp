package accountservlets;

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

import dao.AccountDAO;
import dao.AccountImpl;
import domain.Account;

/**
 * This class is used to register a new account, check whether an account exists or not currently and to login.
 * For more information: read method comments.
 * Servlet implementation class RegisterLoginServlet
 * @author Daniel Jonker
 */
public class RegisterLoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private String strtype;   
    private AccountDAO accountDAO = new AccountImpl();
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RegisterLoginServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//doPost(request, response);
	}

	/**
	 * This is what will be called by syncer from android app. Syncer should maybe change, because this isn't syncing.
	 * What this is doing is just getting the type of request with request.getParameter("reqtype") then comparing that to expected values.
	 * If it is a correct request type it will call the apropriate method.
	 * It is split up like it is to make it easier to follow.
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("text/plain");
		response.setCharacterEncoding("UTF-8");
		
		strtype = "null";
		try{
			strtype = request.getParameter("reqtype");
		} catch (Exception e){
			e.printStackTrace();
		}
		
		strtype = strtype.toLowerCase();
		
		if (strtype.equals("login")){
			login(request, response);
		} else if (strtype.equals("registeruser")){
			register(request, response);
			//call register method
		} else if (strtype.equals("usernamecheck")){
			usernameCheck(request, response);
		} else if (strtype.equals("getaccounts")){
			getFriends(request, response);
		}
	}
	
	/*
	 * this method is basically just to validate the password and username provided. It takes in username and password parameters, gets an
	 * account object from the username, checks the password provided against that account object then returns an int.
	 * @return: this method will return one of 4 ints. 1 = password correct, 0 = incorrect, 2 = error in servlet method, 
	 * 3= error in account.checkpassword method
	 * all errors numbers will be the first value of a string, some, such as exceptions may be followed by error message as well. So if you just
	 * want number just take first value.
	 */
	public void login (HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException{
		response.setContentType("text/plain");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		
		String user = null;
		String password = null;
		Account account;
		
		Integer passwordValid;
		
		try{
			user = request.getParameter("username");
			password = request.getParameter("password");
		} catch (Exception e){
			out.write("2 \t" + e.toString());
		}
		
		try{
			account = accountDAO.getByUsername(user);
			passwordValid = account.checkPassword(password);
			
			out.write(passwordValid.toString());
		}catch (Exception e){
			out.write("2 \t" + e.toString());
		}
		
	}
	
	/*
	 * This method gets all the parameters passed in by the client, then creates a new account in the server DB.
	 * @return: if successful returns 1, if failed returns: 0 + tab + error message.
	 */
	public void register (HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException{
		response.setContentType("text/plain");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		String user = null;
		String password = null;
		String email = null;
		String firstName = null;
		String lastName = null;
		String flag = null;
		Timestamp updateTime = null;
		
		try{
			user = request.getParameter("username");
			password = request.getParameter("password");
			email = request.getParameter("email");
			firstName = request.getParameter("first_name");
			lastName = request.getParameter("last_name");
			updateTime = Timestamp.valueOf(request.getParameter("updatetime"));
			flag = request.getParameter("flag");

		} catch (Exception e){
			out.write("0 \t" + e.toString());
		}
		try{
			accountDAO.create(user, password, email, firstName, lastName, updateTime, flag);
			out.write("1");
		} catch (Exception e){
			out.write("Create an account: 0 \t" + e.toString());
		}
		
	}
	public void getFriends(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException{
		Gson gson = new Gson();
		
		Collection<Account> allAccounts = new ArrayList<Account>();
	
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		
		try{
			allAccounts = accountDAO.getAll();
			String returnJson = gson.toJson(allAccounts);
			
			out.write(returnJson);
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			out.write("0");
			e.printStackTrace();
		} catch (Exception e){
			out.write("0");
			e.printStackTrace();
		}
		finally {
			out.flush();
			out.close();
		}
	}
		
		
	
	/*
	 *  This method gets the username passed in by client, checks if it exists by calling the accountDAO method usernameExists
	 *  @return: This returns the string version of the result of username exists, so true or false. otherwise 0 for error.
	 */
	public void usernameCheck(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException{
		Boolean exists = true;
		
		response.setContentType("text/plain");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		
		try{
			String user = request.getParameter("username");
			
			System.out.println(user);
			if (user != null){
				exists = accountDAO.usernameExists(user);
				out.write(exists.toString());
				System.out.println(exists.toString());
			} else {
				out.write("null");
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			out.write("0");
			e.printStackTrace();
		} catch (Exception e){
			out.write("0");
			e.printStackTrace();
		}
		finally {
			out.flush();
			out.close();
		}
	}

}
