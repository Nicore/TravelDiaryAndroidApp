/**
 * This class is for registering the a user with the remote server.
 *  
 * @peerReview
 * @date 9/7/12
 * @name Registrar
 * @author Nick Comer
 */
package sync;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.util.Log;

import com.android.Constants;

import com.android.DBAdapter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import domain.Account;

/*
 * Notes:
 * May require a 'last sync'd' or 'last up/downloaded' column(s) in the databases to remember when the last round of data was transferred.
 * This would mean we don't have to figure out what to up/download by way of differencing the remote and local database contents.
 */

public class Registrar { // idk about this, syncadapter extends some abstract
							// thread class, but we dont have the Account
							// functionality implemented atm iirc
	// should probably just leave integrating to the UI team, they should know
	// how to piece the parts together...
	public static final String SERVER_ADDRESS = Constants.SERVER_ADDRESS;
	public static final String TAG = "TravelDiaryRegistrar";

	HttpClient httpclient = new DefaultHttpClient();
	HttpPost httppost = new HttpPost(SERVER_ADDRESS + "/RegisterLoginServlet"); // the
																				// address
																				// of
																				// some
																				// script
																				// on
																				// the
																				// host
																				// server
	// DBAdapter db = new DBAdapter(getApplicationContext());
	DBAdapter db;

	public Registrar(DBAdapter dba) {
		db = dba;

		// DBAdapter dba = new DBAdapter(getApplicationContext());
		// would be the thing that needs to be passed
	}

	/**
	 * For checking if the username a user wants to register with already
	 * exists.
	 * 
	 * @param Username
	 *            to be checked
	 * @return 1 = true = tag upload success, 0 = false = tag upload failure, -1
	 *         = omg the connection failed and everything fell apart
	 */
	public int checkUsername(String username) { // no button for this yet so
												// I'll leave it
		int result = 0; //
		HttpResponse response; // the response content from the post request
		String responseBody = null; // the string message that's returned
		HttpEntity entity;

		List<NameValuePair> nameValPairs = new ArrayList<NameValuePair>(2); // this
																			// is
																			// a
																			// name:value
																			// pair
																			// list
																			// of
																			// size
																			// 2
		nameValPairs.add(new BasicNameValuePair("reqtype", "usernamecheck")); // this
																				// one
																				// defines
																				// what
																				// we
																				// action
																				// we
																				// want
																				// the
																				// php
																				// script
																				// to
																				// take
		nameValPairs.add(new BasicNameValuePair("username", username)); // this
																		// is
																		// the
																		// data
																		// to
																		// act
																		// upon
		try {
			httppost.setEntity(new UrlEncodedFormEntity(nameValPairs)); // set
																		// proper
																		// encoding
																		// for
																		// the
																		// request

			response = httpclient.execute(httppost);

			entity = response.getEntity();
			if (entity != null) {
				responseBody = EntityUtils.toString(entity);
				Log.i("responsebody", responseBody);
			}

			// if (/*response turns up with a true value (so there is an equal
			// username)*/true) { //needs to be implemented by determining
			// response
			if (responseBody != null && responseBody.equals("true")) {
				result = 1;
				Log.w(TAG, responseBody);
			} else {
				result = 0;
			}

			// responseBody = EntityUtils.toString(response.getEntity()); //get
			// the string of the response if needed
		} catch (ClientProtocolException e) { // oh noes the connection failed
			result = -1;
		} catch (IOException e) { // io broke maybe
			result = -1;
		}

		return result;
	}

	/**
	 * For retrieving all accounts from the server.
	 * 
	 * @return 1 = true = tag upload success, 0 = false = tag upload failure, -1
	 *         = omg the connection failed and everything fell apart
	 */
	public Collection<Account> getAllAccounts() { // no button for this yet so
													// I'll leave it
		int result = 0; //
		HttpResponse response; // the response content from the post request
		String responseBody = null; // the string message that's returned
		HttpEntity entity;
		Collection<Account> accounts = null;
		// Account[] accounts = null;
		// Gson gson = new Gson();

		List<NameValuePair> nameValPairs = new ArrayList<NameValuePair>(); // this
																			// is
																			// a
																			// name:value
																			// pair
																			// list
																			// of
																			// size
																			// 2
		nameValPairs.add(new BasicNameValuePair("reqtype", "getaccounts")); // this
																			// one
																			// defines
																			// what
																			// we
																			// action
																			// we
																			// want
																			// the
																			// php
																			// script
																			// to
																			// take
		try {
			httppost.setEntity(new UrlEncodedFormEntity(nameValPairs)); // set
																		// proper
																		// encoding
																		// for
																		// the
																		// request

			// ResponseHandler<String> responseHandler = new
			// BasicResponseHandler();
			response = httpclient.execute(httppost);
			// responseBody = httpclient.execute(httppost, responseHandler);
			// //execute the request, retrieving the response
			// JSONObject responseJ = new JSONObject(responseBody);
			// responseJ.
			entity = response.getEntity();
			if (entity != null) {
				responseBody = EntityUtils.toString(entity);
				Log.w(TAG, responseBody);
			}

			// if (/*response turns up with a true value (so there is an equal
			// username)*/true) { //needs to be implemented by determining
			// response
			if (responseBody != null /* && responseBody.equals("true") */) {
				Log.w(TAG, "Response not null " + responseBody);
				Type collectionType = new TypeToken<Collection<Account>>() {
				}.getType();
				accounts = new Gson().fromJson(responseBody, collectionType);
				// accounts = new Gson().fromJson(responseBody,
				// Account[].class);
				/*
				 * for(Account deserialized : accounts){ Log.e(TAG,
				 * String.format("From JSON\n: %s", deserialized.toString())); }
				 */
			} else {
				result = 0;
			}

			// responseBody = EntityUtils.toString(response.getEntity()); //get
			// the string of the response if needed
		} catch (ClientProtocolException e) { // oh noes the connection failed
			result = -1;
		} catch (IOException e) { // io broke maybe
			result = -1;
		}
		return accounts;
	}

	/**
	 * For registering a user with the remote server.
	 * 
	 * @param Username
	 *            to register
	 * @param Password
	 *            to register
	 * @return 1 = true = tag upload success, 0 = false = tag upload failure, -1
	 *         = omg the connection failed and everything fell apart
	 */
	public int registerUser(String uname, String pwd, String email,
			String fname, String lname, String updateTime, String flag) {
		int result = 0;
		HttpResponse response;
		String responseBody = null;
		HttpEntity entity;

		// db.open();
		// String[] acct = db.getAccountArray();
		List<NameValuePair> nameValPairs = new ArrayList<NameValuePair>(); // this
																			// is
																			// a
																			// name:value
																			// pair
																			// list
		nameValPairs.add(new BasicNameValuePair("reqtype", "registeruser")); // this
																				// one
																				// defines
																				// what
																				// we
																				// action
																				// we
																				// want
																				// the
																				// php
																				// script
																				// to
																				// take
		nameValPairs.add(new BasicNameValuePair("username", uname/* acct[0] */)); // this
																					// is
																					// the
																					// data
																					// to
																					// act
																					// upon
		nameValPairs.add(new BasicNameValuePair("password", pwd)); // right now
																	// I'm not
																	// caring
																	// about
																	// encryption
		nameValPairs.add(new BasicNameValuePair("email", email));
		nameValPairs.add(new BasicNameValuePair("first_name", fname));
		nameValPairs.add(new BasicNameValuePair("last_name", lname));
		nameValPairs.add(new BasicNameValuePair("updatetime", updateTime));
		nameValPairs.add(new BasicNameValuePair("flag", flag));

		try {
			httppost.setEntity(new UrlEncodedFormEntity(nameValPairs)); // set
																		// proper
																		// encoding
																		// for
																		// the
																		// request
			response = httpclient.execute(httppost); // execute the request,
														// retrieving the
														// response

			entity = response.getEntity();
			if (entity != null) {
				responseBody = EntityUtils.toString(entity);
				Log.w(TAG, responseBody);
			}

			// if (/*response turns up with a true value (registration was
			// successful)*/true) { //needs to be implemented by determining
			// response
			if (responseBody != null && responseBody.equals("1")) {
				result = 1;
			} else { // registration failed for some reason (maybe another user
						// signed up with same name within time between check
						// and register
				result = 0;
			}
			// responseBody = EntityUtils.toString(response.getEntity());
		} catch (ClientProtocolException e) { // oh noes the connection failed
			result = -1;
		} catch (IOException e) { // io broke maybe
			result = -1;
		}
		// db.close();
		return result;
		// return responseBody;
	}

}
