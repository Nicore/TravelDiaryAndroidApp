package sync;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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

import domain.Trip;

public class SyncerTrip {

	public static final String SERVER_ADDRESS = Constants.SERVER_ADDRESS;
	public static final String TAG = " Trip Syncer";

	HttpClient httpclient = new DefaultHttpClient();
	// HttpPost httppost = new
	// HttpPost("http://139.80.75.191:8080/TripWebServer/TripServlet"); //the
	// address of some script on the host server
	HttpPost httppost = new HttpPost(SERVER_ADDRESS + "/TripServlet"); // the
	
	// DBAdapter db = new DBAdapter(getApplicationContext());
	DBAdapter db;

	public SyncerTrip(DBAdapter dba) {
		db = dba;
		// DBAdapter dba = new DBAdapter(getApplicationContext());
		// would be the thing that needs to be passed
	}

	/**
	 * Get the updateTime of a certain user in the server database
	 * 
	 * @param uname
	 *            username the trip is associated with
	 * @return the updateTime for a given user
	 */
	public String getUpdateTime(String uname) {
		String result = "";
		HttpResponse response;
		String responseBody = null;
		HttpEntity entity;

		// db.open();
		// String[] acct = db.getAccountArray();
		List<NameValuePair> nameValPairs = new ArrayList<NameValuePair>(); // this

		nameValPairs.add(new BasicNameValuePair("reqtype", "getupdatetime")); // this

		nameValPairs.add(new BasicNameValuePair("username", uname));

		try {
			httppost.setEntity(new UrlEncodedFormEntity(nameValPairs)); // set

			response = httpclient.execute(httppost); // execute the request,


			entity = response.getEntity();
			if (entity != null) {
				responseBody = EntityUtils.toString(entity);
			}

			if (responseBody != null /* && responseBody.equals("1") */) {
				result = responseBody;
				// result = 1;
			} else { // upload failed for some reason
				// result = 0;
				// Log.e(TAG, responseBody);
			}
			// responseBody = EntityUtils.toString(response.getEntity());
		} catch (ClientProtocolException e) { // oh noes the connection failed
			result = "";
		} catch (IOException e) { // io broke maybe
			result = "";
		}
		// db.close();
		return result;
		// return responseBody;
	}

	/**
	 * Uploads a single row from the trip table in the database to the server.
	 * 
	 * @param tripid
	 *            trip id for a trip
	 * @param name
	 *            name of the trip
	 * @param descr
	 *            the description of a trip
	 * @param uname
	 *            username the trip is associated with
	 * @return 1 = success, 0 = server denies it or something, -1 = connection
	 *         fail
	 */
	public String uploadNewTrip(Trip trip) {
		int result = 0;
		HttpResponse response;
		String responseBody = null;
		HttpEntity entity;
		Gson gson = new Gson();

		// Convert trip to JSON
		String tripJSON = gson.toJson(trip);

		List<NameValuePair> nameValPairs = new ArrayList<NameValuePair>(); // this
																			// is
																			// a
																			// name:value
																			// pair
																			// list
		nameValPairs.add(new BasicNameValuePair("reqtype", "getnewtrip")); // this
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
		nameValPairs.add(new BasicNameValuePair("jsonobj", tripJSON)); // this
																		// is
																		// the
																		// data
																		// to
																		// act
																		// upon

		Log.d(TAG, nameValPairs.toString());
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
				Log.e(TAG, "Entity =" + entity + ", response=" + response);
			}

			SimpleDateFormat format = new java.text.SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss.SSSSSS");

			try {
				format.parse(responseBody);
			} catch (ParseException e) {
				Log.e(TAG, "Did not receve a valid DATE, response was: "
						+ responseBody);
			}

		} catch (ClientProtocolException e) { // oh noes the connection failed
			Log.e(TAG, "client protocol exception: " + e.toString());

		} catch (IOException e) { // io broke maybe
			Log.e(TAG, "IO exception: " + e.toString());
		}

		return responseBody;
	}// end uploadTrip

	/**
	 * This method is used to pass deleted trip ID's to the server so that they
	 * can be flagged as deleted in the server
	 * 
	 * @param tripId
	 * @returns the response value of the servlet, which will be the update time
	 *          if the method is successful
	 */
	public String uploadDelTrip(Integer tripId) {
		HttpResponse response;
		String responseBody = null;
		HttpEntity entity;

		List<NameValuePair> nameValPairs = new ArrayList<NameValuePair>(); // this

		nameValPairs.add(new BasicNameValuePair("reqtype", "deletetrip")); // this

		nameValPairs.add(new BasicNameValuePair("tripid", String
				.valueOf(tripId))); // this is the data to act upon

		Log.d(TAG, nameValPairs.toString());
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
				Log.e(TAG, "Entity =" + entity + ", response=" + response);
			}

			SimpleDateFormat format = new java.text.SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss.SSSSSS");

			try {
				format.parse(responseBody);
			} catch (ParseException e) {
				Log.e(TAG, "Did not receve a valid DATE, response was: "
						+ responseBody);
			}

		} catch (ClientProtocolException e) { // oh noes the connection failed
			Log.e(TAG, "client protocol exception: " + e.toString());

		} catch (IOException e) { // io broke maybe
			Log.e(TAG, "IO exception: " + e.toString());
		}

		return responseBody;
	}// end uploadTrip

	/**
	 * For downloading a Trip from the remote Media table
	 * 
	 * @param startid
	 *            the trip id to determine which row to start getting events
	 *            from in the remote database
	 * @param endid
	 *            the trip id to determine which row to stop getting events at
	 * @return 1 = true = trip upload success, 0 = false = trip upload failure,
	 *         -1 = omg the connection failed and everything fell apart
	 */
	public Collection<Trip> downloadNewTrips(String username) {
		HttpResponse response;
		String responseBody = null;
		HttpEntity entity;

		List<NameValuePair> nameValPairs = new ArrayList<NameValuePair>();
		nameValPairs.add(new BasicNameValuePair("reqtype", "sendnewtrips"));
		nameValPairs.add(new BasicNameValuePair("username", username));

		Log.i(TAG, "DownloadNewTrips:" + nameValPairs.toString());
		Collection<Trip> trips = new ArrayList<Trip>();

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
				Log.i(TAG, "responsebody1: " + responseBody);
			}

			Log.i(TAG, "responsebody2: " + responseBody);
			Type collectionType = new TypeToken<Collection<Trip>>() {
			}.getType();
			trips = new Gson().fromJson(responseBody, collectionType);

			Log.i(TAG, "responsebody3: " + responseBody);

		} catch (ClientProtocolException e) { // oh noes the connection failed
			Log.e(TAG, e.toString());
		} catch (IOException e) { // io broke maybe
			Log.e(TAG, e.toString());
		}
		return trips;

	}// end downloadtrips

	/**
	 * This method is used to pull all of the trips that need to be deleted
	 * locally from the server
	 * 
	 * @param username
	 * @return the collection of trips with the deleted flag(d) from the server
	 *         that are associated to the user
	 */
	public Collection<Trip> downloadDelTrips(String username) {

		HttpResponse response;
		String responseBody = null;
		HttpEntity entity;

		List<NameValuePair> nameValPairs = new ArrayList<NameValuePair>(); 

		nameValPairs.add(new BasicNameValuePair("reqtype", "senddeletedtrips")); 

		nameValPairs.add(new BasicNameValuePair("username", username)); 
	

		Log.d(TAG, "DownloadDelTrips:" + nameValPairs.toString());
		Collection<Trip> trips = new ArrayList<Trip>();

		try {
			httppost.setEntity(new UrlEncodedFormEntity(nameValPairs)); 
			response = httpclient.execute(httppost); 
			
			entity = response.getEntity();

			if (entity != null) {
				responseBody = EntityUtils.toString(entity);
				Log.i(TAG, "responsebody1: " + responseBody);
			}

			Log.i(TAG, "responsebody2: " + responseBody);
			Type collectionType = new TypeToken<Collection<Trip>>() {
			}.getType();
			trips = new Gson().fromJson(responseBody, collectionType);

			Log.i(TAG, "responsebody3: " + responseBody);

		} catch (ClientProtocolException e) { // oh noes the connection failed
			Log.e(TAG, "Client protocol exception: " + e.toString());
		} catch (IOException e) { // io broke maybe
			Log.e(TAG, "IO exceptions: " + e.toString());
		}
		return trips;

	}// end downloadtrips
}
