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

import domain.AccountTrip;
import domain.Trip;

public class SyncerAccountTrip {

	public static final String SERVER_ADDRESS = Constants.SERVER_ADDRESS;
	public static final String TAG = " Account Trip Syncer";

	HttpClient httpclient = new DefaultHttpClient();
	// HttpPost httppost = new
	// HttpPost("http://139.80.75.191:8080/TripWebServer/TripServlet"); //the
	// address of some script on the host server
	HttpPost httppost = new HttpPost(SERVER_ADDRESS + "/AccountTripServlet"); // the
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

	public SyncerAccountTrip(DBAdapter dba) {
		db = dba;
		// DBAdapter dba = new DBAdapter(getApplicationContext());
		// would be the thing that needs to be passed
	}

	/**
	 * Uploads a single row from the account trip table in the database to the
	 * server.
	 * 
	 * @param accountTrip
	 *            , the permission record to be uploaded
	 * @return 1 = success, 0 = server denies it or something, -1 = connection
	 *         fail
	 */
	public String uploadNewAccountTrip(AccountTrip accountTrip) {
		int result = 0;
		HttpResponse response;
		String responseBody = null;
		HttpEntity entity;
		Gson gson = new Gson();

		// Convert trip to JSON
		String accountTripJSON = gson.toJson(accountTrip);

		List<NameValuePair> nameValPairs = new ArrayList<NameValuePair>(); 
		nameValPairs.add(new BasicNameValuePair("reqtype", "getnewaccounttrip")); 
		nameValPairs.add(new BasicNameValuePair("jsonobj", accountTripJSON)); 


		Log.d(TAG, nameValPairs.toString());
		try {
			httppost.setEntity(new UrlEncodedFormEntity(nameValPairs)); 
			response = httpclient.execute(httppost); 
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
			result = -1;
		} catch (IOException e) { // io broke maybe
			result = -1;
		}
		// db.close();
		return responseBody;
		// return responseBody;
	}// end uploadTrip

	/**
	 * For downloading all account Trips from the remote Account Trips table
	 * 
	 * @param username
	 *            , is the username of the account to withdraw all related
	 *            Account Trips
	 * @return 1 = true = trip upload success, 0 = false = trip upload failure,
	 *         -1 = omg the connection failed and everything fell apart
	 */
	public Collection<AccountTrip> downloadNewAccountTrips(String username) {
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
		nameValPairs.add(new BasicNameValuePair("reqtype",
				"sendnewaccounttrips")); // this one defines what we action we
											// want the php script to take
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
		Log.i(TAG, "DownloadNewTrips:" + nameValPairs.toString());
		Collection<AccountTrip> accountTrips = new ArrayList<AccountTrip>();

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
			Type collectionType = new TypeToken<Collection<AccountTrip>>() {
			}.getType();
			accountTrips = new Gson().fromJson(responseBody, collectionType);

			Log.i(TAG, "responsebody3: " + responseBody);

		} catch (ClientProtocolException e) { // oh noes the connection failed
			Log.e(TAG, e.toString());
		} catch (IOException e) { // io broke maybe
			Log.e(TAG, e.toString());
		}
		return accountTrips;

	}// end downloadtrips
}
