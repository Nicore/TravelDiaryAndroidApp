/**
 * This particular class enables the client to send requests to the server to get
 * information back about the contents of the remote database and also upload data to it,
 * hence enabling synchronisation.
 *  
 * @peerReview
 * @date 9/7/12
 * @name Syncer
 * @author Nick Comer
 */
package sync;

import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.io.HttpResponseParser;
import org.apache.http.io.HttpMessageParser;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.android.Constants;
import com.android.DBAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import domain.Account;
import domain.Media;
import domain.Trip;

import android.util.Log;

/*
 * Notes:
 * May require a 'last sync'd' or 'last up/downloaded' column(s) in the databases to remember when the last round of data was transferred.
 * This would mean we don't have to figure out what to up/download by way of differencing the remote and local database contents.
 */

public class Syncer { // idk about this, syncadapter extends some abstract
						// thread class, but we dont have the Account
						// functionality implemented atm iirc
	// should probably just leave integrating to the UI team, they should know
	// how to piece the parts together...
	public static final String SERVER_ADDRESS = Constants.SERVER_ADDRESS;
	public static final String TAG = "TravelDiarySyncer";

	HttpClient httpclient = new DefaultHttpClient();
	HttpPost httppost = new HttpPost(SERVER_ADDRESS + "/SyncerServlet"); // the
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

	public Syncer(DBAdapter dba) {
		db = dba;
		// DBAdapter dba = new DBAdapter(getApplicationContext());
		// would be the thing that needs to be passed
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
	public String uploadTrip(int tripid, String name, String descr,
			String updateTime) {
		int result = 0;
		HttpResponse response;
		String responseBody = null;
		HttpEntity entity;
		// String fixedUpdateTime = new SimpleDateFormat("MM/dd/yyyy").format()

		// db.open();
		// String[] acct = db.getAccountArray();
		List<NameValuePair> nameValPairs = new ArrayList<NameValuePair>(); // this
																			// is
																			// a
																			// name:value
																			// pair
																			// list
		nameValPairs.add(new BasicNameValuePair("reqtype", "downloadtrips")); // this
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
		nameValPairs.add(new BasicNameValuePair("trip_id", tripid + "")); // this
																			// is
																			// the
																			// data
																			// to
																			// act
																			// upon
		nameValPairs.add(new BasicNameValuePair("name", name));
		nameValPairs.add(new BasicNameValuePair("description", descr));
		// Log.e(TAG, "UpdateTime in Syncer = " + updateTime);
		nameValPairs.add(new BasicNameValuePair("updatetime", updateTime));

		Log.e(TAG, nameValPairs.toString());
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

			if (responseBody != null /* && responseBody.equals("1") */) {
				result = 1;
				Log.e(TAG, "not null ResponseBody = " + responseBody);
			} else { // upload failed for some reason
				result = 0;
				Log.e(TAG, "else ResponseBody = " + responseBody);
			}
			// responseBody = EntityUtils.toString(response.getEntity());
		} catch (ClientProtocolException e) { // oh noes the connection failed
			result = -1;
		} catch (IOException e) { // io broke maybe
			result = -1;
		}
		// db.close();
		return responseBody;
		// return responseBody;
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
	 * Upload a single row from the event table
	 * 
	 * @param eventid
	 * @param name
	 * @param descr
	 * @param lati
	 * @param longi
	 * @param datetime
	 * @param tripid
	 * @return 1 = success, 0 = server denies it or something, -1 = connection
	 *         fail
	 */
	public int uploadEvent(int eventid, String name, String descr, long lati,
			long longi, String updatetime, int tripid) {
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
		nameValPairs.add(new BasicNameValuePair("reqtype", "downloadevents")); // this
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
		nameValPairs.add(new BasicNameValuePair("event_id", eventid + ""));
		nameValPairs.add(new BasicNameValuePair("trip_id", tripid + "")); // this
																			// is
																			// the
																			// data
																			// to
																			// act
																			// upon
		nameValPairs.add(new BasicNameValuePair("longitude", longi + ""));
		nameValPairs.add(new BasicNameValuePair("latitude", lati + ""));
		nameValPairs.add(new BasicNameValuePair("name", name));
		nameValPairs.add(new BasicNameValuePair("description", descr));
		nameValPairs.add(new BasicNameValuePair("UPDATETIME", updatetime));

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

			if (responseBody != null && responseBody.equals("1")) {
				result = 1;
			} else { // upload failed for some reason
				result = 0;
				Log.w(TAG, responseBody);
			}
			// responseBody = EntityUtils.toString(response.getEntity());
		} catch (ClientProtocolException e) { // oh noes the connection failed
			result = -1;
		} catch (IOException e) { // io broke maybe
			result = -1;
		}
		// db.close();
		return result;

	}

	/**
	 * Uploads one row from the media table to the remote server.
	 * 
	 * @param mediaid
	 * @param name
	 * @param descr
	 * @param type
	 * @param blob
	 * @param eventid
	 * @return 1 = success, 0 = server denies it or something, -1 = connection
	 *         fail
	 */
	public int uploadMedia(int mediaid, String name, String descr, String type,
			String blob, int eventid, String updateTime) {
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
		nameValPairs.add(new BasicNameValuePair("reqtype", "uploadmedia")); // this
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
		nameValPairs.add(new BasicNameValuePair("media_id", mediaid + ""));
		nameValPairs.add(new BasicNameValuePair("event_id", eventid + ""));
		nameValPairs.add(new BasicNameValuePair("name", name));
		nameValPairs.add(new BasicNameValuePair("description", descr));
		nameValPairs.add(new BasicNameValuePair("type", type));
		nameValPairs.add(new BasicNameValuePair("blob", blob));
		nameValPairs.add(new BasicNameValuePair("updatetime", updateTime));

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

			if (responseBody != null && responseBody.equals("1")) {
				result = 1;
			} else { // upload failed for some reason
				result = 0;
				Log.w(TAG, responseBody);
			}
			// responseBody = EntityUtils.toString(response.getEntity());
		} catch (ClientProtocolException e) { // oh noes the connection failed
			result = -1;
		} catch (IOException e) { // io broke maybe
			result = -1;
		}
		// db.close();
		return result;

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
	public int uploadTag(String tagtype, int id, String text) {
		int result = 0;
		HttpResponse response;
		String responseBody = null;
		HttpEntity entity;

		List<NameValuePair> nameValPairs = new ArrayList<NameValuePair>(4); // this
																			// is
																			// a
																			// name:value
																			// pair
																			// list
																			// of
																			// size
																			// 2
		nameValPairs.add(new BasicNameValuePair("reqtype", "uploadtag")); // this
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
		nameValPairs.add(new BasicNameValuePair("id", "" + id)); // cheap string
																	// conversion
																	// hack
		nameValPairs.add(new BasicNameValuePair("text", text));

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

			if (responseBody != null && responseBody.equals("1")) {
				result = 1;
			} else { // upload failed for some reason
				result = 0;
				Log.w(TAG, responseBody);
			}
		} catch (ClientProtocolException e) { // oh noes the connection failed
			result = -1;
		} catch (IOException e) { // io broke maybe
			result = -1;
		}
		return result;

	}

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
	public Collection<Trip> downloadTrips(String username, String updatetime) {
		int result = 0;
		username = "bbbb";
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
		nameValPairs.add(new BasicNameValuePair("reqtype", "uploadtrips")); // this
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
		nameValPairs.add(new BasicNameValuePair("updatetime", updatetime)); // this
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
		Log.e(TAG, nameValPairs.toString());
		Collection<Trip> trips = null;

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

			if (responseBody != null && responseBody.equals("0")) { // failed to
																	// download
				result = 0;
			} else {
				try {
					Log.e(TAG, responseBody);
					Type collectionType = new TypeToken<Collection<Trip>>() {
					}.getType();
					trips = new Gson().fromJson(responseBody, collectionType);
				} catch (Exception e) {
					Log.e(TAG, e.toString());
				}
				// throw it into the database
				// parse the response string with magic
				// for every tag returned {
				// db.insertTag(whatever) }

				result = 1;
				Log.w(TAG, responseBody);
			}
		} catch (ClientProtocolException e) { // oh noes the connection failed
			result = -1;
		} catch (IOException e) { // io broke maybe
			result = -1;
		}
		return trips;

	}

	/**
	 * For downloading a Event from the remote Media table
	 * 
	 * @param startid
	 *            the event id to determine which row to start getting events
	 *            from in the remote database
	 * @param endid
	 *            the event to determine which row to stop getting events at
	 * @return 1 = true = event upload success, 0 = false = event upload
	 *         failure, -1 = omg the connection failed and everything fell apart
	 */
	public int downloadEvents(int startid, int endid) {
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
																			// 3
		nameValPairs.add(new BasicNameValuePair("reqtype", "downloadevents")); // this
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
		nameValPairs.add(new BasicNameValuePair("id", "" + startid)); // cheap
																		// string
																		// conversion
																		// hack
		nameValPairs.add(new BasicNameValuePair("id", "" + endid));

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

			if (responseBody != null && responseBody.equals("0")) { // failed to
																	// download
				result = 0;
			} else {
				try {
					DocumentBuilderFactory factory = DocumentBuilderFactory
							.newInstance();
					DocumentBuilder db = factory.newDocumentBuilder();
					InputSource inStream = new InputSource();
					inStream.setCharacterStream(new StringReader(responseBody));
					Document doc = db.parse(inStream);

					String playcount = "empty";
					NodeList nl = doc.getElementsByTagName("playcount");
					for (int i = 0; i < nl.getLength(); i++) {
						if (nl.item(i).getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
							org.w3c.dom.Element nameElement = (org.w3c.dom.Element) nl
									.item(i);
							playcount = nameElement.getFirstChild()
									.getNodeValue().trim();
						}
					}
				} catch (Exception e) {
					Log.e(TAG, e.toString());
				}
				// throw it into the database
				// parse the response string with magic
				// for every tag returned {
				// db.insertTag(whatever) }

				result = 1;
				Log.w(TAG, responseBody);
			}
		} catch (ClientProtocolException e) { // oh noes the connection failed
			result = -1;
		} catch (IOException e) { // io broke maybe
			result = -1;
		}
		return result;

	}

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
	public Collection<Media> downloadMedias() {
		int result = 0;
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
		nameValPairs.add(new BasicNameValuePair("reqtype", "uploadmedia")); // this
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
		Collection<Media> medias = null;

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

			if (responseBody != null && responseBody.equals("0")) { // failed to
																	// download
				result = 0;
			} else {
				try {
					Log.e(TAG, responseBody);
					Type collectionType = new TypeToken<Collection<Media>>() {
					}.getType();
					medias = new Gson().fromJson(responseBody, collectionType);
				} catch (Exception e) {
					Log.e(TAG, e.toString());
				}
				// throw it into the database
				// parse the response string with magic
				// for every tag returned {
				// db.insertTag(whatever) }

				result = 1;
				Log.w(TAG, responseBody);
			}
		} catch (ClientProtocolException e) { // oh noes the connection failed
			result = -1;
		} catch (IOException e) { // io broke maybe
			result = -1;
		}
		return medias;

	}

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
	public int downloadTags(String tagtype, int startid, int endid) {
		int result = 0;
		HttpResponse response;
		String responseBody = null;
		HttpEntity entity;

		List<NameValuePair> nameValPairs = new ArrayList<NameValuePair>(4); // this
																			// is
																			// a
																			// name:value
																			// pair
																			// list
																			// of
																			// size
																			// 2
		nameValPairs.add(new BasicNameValuePair("reqtype", "downloadtags")); // this
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
		nameValPairs.add(new BasicNameValuePair("id", "" + startid)); // cheap
																		// string
																		// conversion
																		// hack
		nameValPairs.add(new BasicNameValuePair("id", "" + endid));

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

			if (responseBody != null && responseBody.equals("0")) { // failed to
																	// download
				result = 0;
			} else { // throw it into the database
				// parse the response string with magic
				// for every tag returned {
				// db.insertTag(whatever) }

				result = 1;
				Log.w(TAG, responseBody);
			}
		} catch (ClientProtocolException e) { // oh noes the connection failed
			result = -1;
		} catch (IOException e) { // io broke maybe
			result = -1;
		}
		return result;

	}

}
