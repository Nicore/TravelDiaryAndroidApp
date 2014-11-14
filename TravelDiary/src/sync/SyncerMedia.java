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

import domain.Media;
import domain.Trip;

public class SyncerMedia {
	public static final String SERVER_ADDRESS = Constants.SERVER_ADDRESS;
	public static final String TAG = "TravelDiary Media Syncer";

	HttpClient httpclient = new DefaultHttpClient();
	HttpPost httppost = new HttpPost(SERVER_ADDRESS + "/MediaServlet"); // the
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

	public SyncerMedia(DBAdapter dba) {
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
	 * 
	 * @param mediaid
	 * @return
	 */
	public String uploadDelMedia(Integer mediaid) {
		HttpResponse response;
		String responseBody = null;
		HttpEntity entity;

		List<NameValuePair> nameValPairs = new ArrayList<NameValuePair>(); // this
																			// is
																			// a
																			// name:value
																			// pair
																			// list
		nameValPairs.add(new BasicNameValuePair("reqtype", "getnewmedia")); // this
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
		nameValPairs.add(new BasicNameValuePair("mediaid", String
				.valueOf(mediaid))); // this is the data to act upon

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
			Log.e(TAG, e.toString());
		} catch (IOException e) { // io broke maybe
			Log.e(TAG, e.toString());
		}
		// db.close();
		return responseBody;

	}// end uploadmedia

	public String uploadNewMedia(Media media) {
		int result = 0;
		HttpResponse response;
		String responseBody = null;
		HttpEntity entity;
		Gson gson = new Gson();

		// Convert trip to JSON
		String mediaJSON = gson.toJson(media);

		List<NameValuePair> nameValPairs = new ArrayList<NameValuePair>(); 
		nameValPairs.add(new BasicNameValuePair("reqtype", "getnewmedia")); 
		nameValPairs.add(new BasicNameValuePair("jsonobj", mediaJSON)); 

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

			/*
			 * if (responseBody != null && responseBody.equals("1")) { result =
			 * 1; Log.e(TAG, "not null ResponseBody = "+responseBody); } else {
			 * //upload failed for some reason result = 0; Log.e(TAG,
			 * "else ResponseBody = "+responseBody); }
			 */
			// responseBody = EntityUtils.toString(response.getEntity());
		} catch (ClientProtocolException e) { // oh noes the connection failed
			Log.e(TAG, e.toString());
		} catch (IOException e) { // io broke maybe
			Log.e(TAG, e.toString());
		}
		// db.close();
		return responseBody;

	}// end uploadmedia

	/**
	 * For downloading a Media from the remote Media table
	 * 
	 * @param startid
	 *            the trip id to determine which row to start getting events
	 *            from in the remote database
	 * @param endid
	 *            the trip id to determine which row to stop getting events at
	 * @return 1 = true = trip upload success, 0 = false = trip upload failure,
	 *         -1 = omg the connection failed and everything fell apart
	 */
	public Collection<Media> downloadNewMedias(String username) {
		int result = 0;
		// username = "bbbb";
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
		nameValPairs.add(new BasicNameValuePair("reqtype", "sendnewmedias")); // this
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
		// nameValPairs.add(new BasicNameValuePair("updatetime", updatetime));
		// //this one defines what we action we want the php script to take
		Log.i(TAG, nameValPairs.toString());
		Collection<Media> medias = new ArrayList<Media>();

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
				Log.i(TAG, responseBody);
			}

			Log.i(TAG, responseBody);
			Type collectionType = new TypeToken<Collection<Media>>() {
			}.getType();
			medias = new Gson().fromJson(responseBody, collectionType);

			Log.i(TAG, responseBody);

		} catch (ClientProtocolException e) { // oh noes the connection failed
			Log.e(TAG, e.toString());
		} catch (IOException e) { // io broke maybe
			Log.e(TAG, e.toString());
		}
		return medias;

	}// end downloadmedias

	public Collection<Media> downloadDelMedias(String username) {
		int result = 0;
		HttpResponse response;
		String responseBody = null;
		HttpEntity entity;

		List<NameValuePair> nameValPairs = new ArrayList<NameValuePair>();
		nameValPairs
				.add(new BasicNameValuePair("reqtype", "senddeletedmedias"));
		nameValPairs.add(new BasicNameValuePair("username", username));

		Log.i(TAG, nameValPairs.toString());
		Collection<Media> medias = new ArrayList<Media>();

		try {
			httppost.setEntity(new UrlEncodedFormEntity(nameValPairs));
			response = httpclient.execute(httppost);
			entity = response.getEntity();

			if (entity != null) {
				responseBody = EntityUtils.toString(entity);
				Log.i(TAG, responseBody);
			}

			Log.i(TAG, responseBody);
			Type collectionType = new TypeToken<Collection<Media>>() {
			}.getType();
			medias = new Gson().fromJson(responseBody, collectionType);

			Log.i(TAG, responseBody);

		} catch (ClientProtocolException e) { // oh noes the connection failed
			Log.e(TAG, e.toString());
		} catch (IOException e) { // io broke maybe
			Log.e(TAG, e.toString());
		}
		return medias;

	}// end downloadmedias
}
