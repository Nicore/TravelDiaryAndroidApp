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

import domain.Tag;
import domain.Trip;

public class SyncerTag {
	public static final String SERVER_ADDRESS = Constants.SERVER_ADDRESS;
	public static final String TAG = "TravelDiarySyncer";

	HttpClient httpclient = new DefaultHttpClient();
	HttpPost httppost = new HttpPost(SERVER_ADDRESS + "/TagServlet"); // the
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

	public SyncerTag(DBAdapter dba) {
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
	 * For uploading a row for a tag table
	 * 
	 * @param tagtype
	 *            defines whether it's a tag for trip, event or media
	 * @param id
	 *            the unique id for that tag
	 * @param text
	 *            the content of the tag
	 * @return 1 = true = tag upload success, 0 = false = tag upload failure, -1
	 *         = omg the connection failed and everything fell apart
	 */
	public String uploadNewTag(Tag tag) {
		int result = 0;
		HttpResponse response;
		String responseBody = null;
		HttpEntity entity;
		Gson gson = new Gson();

		// Convert trip to JSON
		String tagJSON = gson.toJson(tag);

		List<NameValuePair> nameValPairs = new ArrayList<NameValuePair>(); // this
																			// is
																			// a
																			// name:value
																			// pair
																			// list
		nameValPairs.add(new BasicNameValuePair("reqtype", "getnewtag")); // this
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
		nameValPairs.add(new BasicNameValuePair("jsonobj", tagJSON)); // this is
																		// the
																		// data
																		// to
																		// act
																		// upon
		// nameValPairs.add(new BasicNameValuePair("name", trip.getName()));
		// nameValPairs.add(new BasicNameValuePair("description",
		// trip.getDescription()));
		// nameValPairs.add(new BasicNameValuePair("username",
		// trip.getUsername()));
		// Log.e(TAG, "UpdateTime in Syncer = " + updateTime);
		// nameValPairs.add(new BasicNameValuePair("updatetime",
		// trip.getUpdatetime()+""));

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
			result = -1;
		} catch (IOException e) { // io broke maybe
			result = -1;
		}
		// db.close();
		return responseBody;
		// return responseBody;
	}// end uploadtag

	/**
	 * For downloading a set of tags from the remote tag table
	 * 
	 * @param tagtype
	 *            defines whether it's a tag for trip, event or media
	 * @param startid
	 *            the tag id to determine which row to start getting tag from in
	 *            the remote database
	 * @param endid
	 *            the tag to determine which row to stop getting tags at
	 * @return 1 = true = tag upload success, 0 = false = tag upload failure, -1
	 *         = omg the connection failed and everything fell apart
	 */
	public Collection<Tag> downloadNewTags(String tagtype, String username) {
		int result = 0;
		HttpResponse response;
		String responseBody = null;
		HttpEntity entity;

		List<NameValuePair> nameValPairs = new ArrayList<NameValuePair>(3); // this
																			// is
																			// a
																			// name:value
																			// pair
																			// list
																			// of
																			// size
																			// 2
		nameValPairs.add(new BasicNameValuePair("reqtype", "sendnewtags")); // this
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
		nameValPairs.add(new BasicNameValuePair("tagtype", tagtype)); // this is
																		// the
																		// data
																		// to
																		// act
																		// upon
		nameValPairs.add(new BasicNameValuePair("username", username));

		Collection<Tag> tags = new ArrayList<Tag>();
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
			Type collectionType = new TypeToken<Collection<Tag>>() {
			}.getType();
			tags = new Gson().fromJson(responseBody, collectionType);

			Log.i(TAG, responseBody);

		} catch (ClientProtocolException e) { // oh noes the connection failed
			Log.e(TAG, e.toString());
		} catch (IOException e) { // io broke maybe
			Log.e(TAG, e.toString());
		}
		return tags;

	}// end downloadtags
}
