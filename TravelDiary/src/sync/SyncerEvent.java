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

import domain.Event;

public class SyncerEvent {
	public static final String SERVER_ADDRESS = Constants.SERVER_ADDRESS;
	public static final String TAG = "Event Syncer";

	HttpClient httpclient = new DefaultHttpClient();
	HttpPost httppost = new HttpPost(SERVER_ADDRESS + "/EventServlet"); // the
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

	public SyncerEvent(DBAdapter dba) {
		db = dba;
		// DBAdapter dba = new DBAdapter(getApplicationContext());
		// would be the thing that needs to be passed
	}

	/**
	 * Get the updateTime of a certain user in the server database
	 * 
	 * @param uname
	 *            username the event is associated with
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
																			// is
																			// a
																			// name:value
																			// pair
																			// list
		nameValPairs.add(new BasicNameValuePair("reqtype", "getupdatetime")); // this
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
		nameValPairs.add(new BasicNameValuePair("username", uname));

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
	 * Upload a single row from the event table
	 * 
	 * @param eventid
	 * @param name
	 * @param descr
	 * @param lati
	 * @param longi
	 * @param datetime
	 * @param eventid
	 * @return 1 = success, 0 = server denies it or something, -1 = connection
	 *         fail
	 */
	public String uploadNewEvent(Event event) {
		int result = 0;
		HttpResponse response;
		String responseBody = null;
		HttpEntity entity;
		Gson gson = new Gson();

		// Convert event to JSON
		String eventJSON = gson.toJson(event);

		// db.open();
		// String[] acct = db.getAccountArray();
		List<NameValuePair> nameValPairs = new ArrayList<NameValuePair>(); // this
																			// is
																			// a
																			// name:value
																			// pair
																			// list
		nameValPairs.add(new BasicNameValuePair("reqtype", "getnewevent")); // this
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
		nameValPairs.add(new BasicNameValuePair("jsonobj", eventJSON)); // this
																		// is
																		// the
																		// data
																		// to
																		// act
																		// upon
		Log.i(TAG, nameValPairs.toString());
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

			SimpleDateFormat format = new java.text.SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss.SSSSSS");

			try {
				format.parse(responseBody);
			} catch (ParseException e) {
				Log.e(TAG, "Did not receve a valid DATE, response was: "
						+ responseBody);
			}

			/*
			 * if (responseBody != null && responseBody.equals("1")) { result =
			 * 1; } else { //upload failed for some reason result = 0;
			 * Log.w(TAG, responseBody); }
			 */
			// responseBody = EntityUtils.toString(response.getEntity());
		} catch (ClientProtocolException e) { // oh noes the connection failed
			result = -1;
		} catch (IOException e) { // io broke maybe
			result = -1;
		}
		// db.close();
		// return result;
		return responseBody;

	}// end uploadevent

	public Collection<Event> downloadNewEvents(String uname) {
		HttpResponse response;
		String responseBody = null;
		HttpEntity entity;

		List<NameValuePair> nameValPairs = new ArrayList<NameValuePair>(); 
		nameValPairs.add(new BasicNameValuePair("reqtype", "sendnewevents")); 
		nameValPairs.add(new BasicNameValuePair("username", uname)); // cheap
																		// string
																		// conversion
																		// hack

		Log.i(TAG, nameValPairs.toString());
		Collection<Event> events = new ArrayList<Event>();

		try {
			httppost.setEntity(new UrlEncodedFormEntity(nameValPairs)); 
			response = httpclient.execute(httppost); // execute the request,
														// retrieving the
														// response
			entity = response.getEntity();

			if (entity != null) {
				responseBody = EntityUtils.toString(entity);
				Log.i(TAG, responseBody);
			}

			Log.i(TAG, responseBody);
			Type collectionType = new TypeToken<Collection<Event>>() {
			}.getType();
			events = new Gson().fromJson(responseBody, collectionType);

			Log.i(TAG, responseBody);

		} catch (ClientProtocolException e) { // oh noes the connection failed
			// result = -1;
			Log.e(TAG, e.toString());
		} catch (IOException e) { // io broke maybe
			// result = -1;
			Log.e(TAG, e.toString());
		}
		
		Log.i(TAG, "EVents: " + events.toString());
		return events;

	}// end downloadevents

	/**
	 * This method is used to pass deleted event ID's to the server so that they
	 * can be flagged as deleted in the server
	 * 
	 * @param eventId
	 * @returns the response value of the servlet, which will be the update time
	 *          if the method is successful
	 */
	public String uploadDelEvent(Integer eventId) {
		HttpResponse response;
		String responseBody = null;
		HttpEntity entity;

		List<NameValuePair> nameValPairs = new ArrayList<NameValuePair>(); // this
																			// is
																			// a
																			// name:value
																			// pair
																			// list
		nameValPairs.add(new BasicNameValuePair("reqtype", "deleteevent")); // this
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
		nameValPairs.add(new BasicNameValuePair("eventid", String
				.valueOf(eventId))); // this is the data to act upon

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
	}// end uploadEvent

	/**
	 * This method is used to pull all of the events that need to be deleted
	 * locally from the server
	 * 
	 * @param username
	 * @return the collection of events with the deleted flag(d) from the server
	 *         that are associated to the user
	 */
	public Collection<Event> downloadDelEvents(String username) {

		HttpResponse response;
		String responseBody = null;
		HttpEntity entity;

		List<NameValuePair> nameValPairs = new ArrayList<NameValuePair>(); // this
																			// is
																			// a
																			// name:value
																			// pair
																			// list
																			// of
																			// size
																			// 3
		nameValPairs
				.add(new BasicNameValuePair("reqtype", "senddeletedevents")); // this
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

		Log.d(TAG, "DownloadDelEvents:" + nameValPairs.toString());
		Collection<Event> events = new ArrayList<Event>();

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
			Type collectionType = new TypeToken<Collection<Event>>() {
			}.getType();
			events = new Gson().fromJson(responseBody, collectionType);

			Log.i(TAG, "responsebody3: " + responseBody);

		} catch (ClientProtocolException e) { // oh noes the connection failed
			Log.e(TAG, "Client protocol exception: " + e.toString());
		} catch (IOException e) { // io broke maybe
			Log.e(TAG, "IO exceptions: " + e.toString());
		}
		return events;

	}// end downloadevents
}
