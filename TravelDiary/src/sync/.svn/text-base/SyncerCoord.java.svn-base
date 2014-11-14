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

import domain.Coordinate;
import domain.Event;

public class SyncerCoord {
	public static final String SERVER_ADDRESS = Constants.SERVER_ADDRESS;
	public static final String TAG = "SyncCoord";

	HttpClient httpclient = new DefaultHttpClient();
	HttpPost httppost = new HttpPost(SERVER_ADDRESS + "/CoordinateServlet"); 
	// DBAdapter db = new DBAdapter(getApplicationContext());
	DBAdapter db;

	public SyncerCoord(DBAdapter dba) {
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
		List<NameValuePair> nameValPairs = new ArrayList<NameValuePair>(); 
		nameValPairs.add(new BasicNameValuePair("reqtype", "getupdatetime")); 
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
	 * Upload a single Coordinate. Side note: could probably get all coords
	 * associated with a trip and just hurl them at the server?
	 * 
	 * @param coord
	 *            The coordinate to upload.
	 */
	public String uploadNewCoord(Coordinate coord) {
		int result = 0;
		HttpResponse response;
		String responseBody = null;
		HttpEntity entity;
		Gson gson = new Gson();

		// Convert trip to JSON
		String coordJSON = gson.toJson(coord);

		// db.open();
		// String[] acct = db.getAccountArray();
		List<NameValuePair> nameValPairs = new ArrayList<NameValuePair>(); 
		nameValPairs.add(new BasicNameValuePair("reqtype", "getnewcoord"));
		nameValPairs.add(new BasicNameValuePair("jsonobj", coordJSON)); 

		Log.d(TAG, nameValPairs.toString());
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

			/*SimpleDateFormat format = new java.text.SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss.SSSSSS");

			try {
				format.parse(responseBody);
			} catch (ParseException e) {
				Log.e(TAG, "Did not receve a valid DATE, response was: "
						+ responseBody);
			}*/

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

	public Collection<Coordinate> downloadNewCoords(String uname) {
		int result = 0;
		HttpResponse response;
		String responseBody = null;
		HttpEntity entity;

		List<NameValuePair> nameValPairs = new ArrayList<NameValuePair>();
		nameValPairs.add(new BasicNameValuePair("reqtype", "sendnewcoords")); 
		nameValPairs.add(new BasicNameValuePair("username", uname)); 

		Log.i(TAG, nameValPairs.toString());
		Collection<Coordinate> coords = new ArrayList<Coordinate>();

		try {
			httppost.setEntity(new UrlEncodedFormEntity(nameValPairs)); 
			response = httpclient.execute(httppost); 
			
			entity = response.getEntity();

			if (entity != null) {
				responseBody = EntityUtils.toString(entity);
				Log.w(TAG, responseBody);
			}

			Log.i(TAG, responseBody);
			Type collectionType = new TypeToken<Collection<Coordinate>>() {
			}.getType();
			coords = new Gson().fromJson(responseBody, collectionType);

			Log.i(TAG, responseBody);

			/*
			 * if (responseBody != null && responseBody.equals("0")) { //failed
			 * to download result = 0; } else { try{ DocumentBuilderFactory
			 * factory = DocumentBuilderFactory.newInstance(); DocumentBuilder
			 * db = factory.newDocumentBuilder(); InputSource inStream = new
			 * InputSource(); inStream.setCharacterStream(new
			 * StringReader(responseBody)); Document doc = db.parse(inStream);
			 * 
			 * String playcount = "empty"; NodeList nl =
			 * doc.getElementsByTagName("playcount"); for(int i = 0; i <
			 * nl.getLength(); i++) { if (nl.item(i).getNodeType() ==
			 * org.w3c.dom.Node.ELEMENT_NODE) { org.w3c.dom.Element nameElement
			 * = (org.w3c.dom.Element) nl.item(i); playcount =
			 * nameElement.getFirstChild().getNodeValue().trim(); } } } catch
			 * (Exception e) {Log.e(TAG, e.toString());} //throw it into the
			 * database //parse the response string with magic //for every tag
			 * returned { //db.insertTag(whatever) }
			 * 
			 * result = 1; Log.w(TAG, responseBody); }
			 */
		} catch (ClientProtocolException e) { // oh noes the connection failed
			// result = -1;
			Log.e(TAG, e.toString());
		} catch (IOException e) { // io broke maybe
			// result = -1;
			Log.e(TAG, e.toString());
		}
		return coords;

	}// end downloadevents
}
