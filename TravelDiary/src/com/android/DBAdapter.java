/**
 * The purpose of this program is to provide a database adapter to the
 * Travel Diary application. The adapter was created from best practices
 * found on the internet
 * 
 * @peerReview
 * @date 22/05/12
 * @name DBAdapter
 * @author Mostafa Alwash, Daniel Jonker, Nick Corner
 */
package com.android;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import domain.Coordinate;
import domain.Event;
import domain.Media;
import domain.Tag;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;
import domain.*;

public class DBAdapter {

	// Declaring all neccessary variables
	private DatabaseHelper mDbHelper;
	private SQLiteDatabase mDb;
	private final Context mCtx;
	private static final String DATABASE_NAME = "TravelDiaryDB";
	private static final int DATABASE_VERSION = 1;
	private static final String TAG = "TripDiaryDbAdapter";

	/* Creating tables for Travel Diary */
	private static final String TABLE_ACCOUNT = "CREATE TABLE account (username varchar2(15) primary key, password varchar2(60) not null,"
			+ " email varchar2(512) not null, first_name varchar2(20), last_name varchar2(20), UPDATETIME TIMESTAMP, flag char(1));";

	private static final String TABLE_FRIEND = "create table friend (username varchar2(15) not null, friend_id varchar2(15) not null, UPDATETIME TIMESTAMP, flag char(1),"
			+ " constraint friend_prim_key PRIMARY KEY (username, friend_id), foreign key(username) references account(username) ); ";

	private static final String TABLE_TRIP = "CREATE table trip (trip_id int primary key,  name varchar2(32) not null, description varchar2(512), UPDATETIME TIMESTAMP, flag char(1)); ";

	private static final String TABLE_TRIP_TAG = "create table trip_tag (text varchar2(25) not null, trip_id int not null, UPDATETIME TIMESTAMP, flag char(1),"
			+ "  foreign key(trip_id) references trip(trip_id), primary key (text, trip_id));";

	private static final String TABLE_EVENT = "create table event (event_id int primary key, trip_id int, longitude double,"
			+ " latitude double, dtime datetime not null, name varchar2(50), description varchar2(512), UPDATETIME TIMESTAMP, flag char(1),"
			+ " foreign key(trip_id) references trip(trip_id)); ";

	private static final String TABLE_EVENT_TAG = "create table event_tag (text varchar2(25) primary key, event_id int not null, UPDATETIME TIMESTAMP, flag char(1),"
			+ "  foreign key(event_id) references event(event_id)); ";

	private static final String TABLE_MEDIA = "create table media (media_id int primary key, event_id int not null, name varchar2(50),"
			+ " description varchar2(512), type varchar2(12) not null, blob BLOB not null, UPDATETIME TIMESTAMP, flag char(1), foreign key(event_id) references event(event_id),"
			+ " CONSTRAINT media_type_check CHECK (type in ('photo','video', 'audio', 'text')) ); ";

	private static final String TABLE_MEDIA_TAG = "create table media_tag (text varchar2(25) not null, media_id int not null, UPDATETIME TIMESTAMP, flag char(1),"
			+ " foreign key(media_id) references media(media_id), primary key (text, media_id)); ";

	private static final String TABLE_COORDINATES = "create table coordinates ( latitude double not null, longitude double not null, trip_id int not null, datetime timestamp,flag char(1), primary key (latitude, longitude, trip_id, datetime), foreign key (trip_id) references trip (trip_id) );";

	private static final String TABLE_ACCOUNT_TRIP = "create table account_trip (username varchar2(15), trip_id int, flag char(1), permissions int, foreign key(username) references account(username), foreign key(trip_id) references trip(trip_id));";

	private static class DatabaseHelper extends SQLiteOpenHelper {

		/*
		 * Constructor which takes a context and assigns it to the local
		 * context, assigns database name and version
		 */
		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		/**
		 * This method is initially called to construct the database for the
		 * Travel Diary Application
		 */
		@Override
		public void onCreate(SQLiteDatabase db) {
			try {

				Log.w(TAG, "creating DB");
				db.execSQL(TABLE_TRIP);
				db.execSQL(TABLE_ACCOUNT);
				db.execSQL(TABLE_FRIEND);
				db.execSQL(TABLE_TRIP_TAG);
				db.execSQL(TABLE_EVENT);
				db.execSQL(TABLE_EVENT_TAG);
				db.execSQL(TABLE_MEDIA);
				db.execSQL(TABLE_MEDIA_TAG);
				db.execSQL(TABLE_COORDINATES);
				db.execSQL(TABLE_ACCOUNT_TRIP);
				// For Demonstration I will use the below code as it works with
				// event features

				Log.w(TAG, "The Database successfully created");
			} catch (SQLException e) {
				Log.w(TAG, "The Database failed at creating: " + e.toString());
				e.printStackTrace();
			}
		}

		/**
		 * This method is used when upgrading the database, it firstly drops all
		 * tables then calls the onCreate method
		 */
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
					+ newVersion + ", which will destroy all old data");
			db.execSQL("DROP TABLE IF EXISTS account; DROP TABLE IF EXISTS friend;"
					+ " DROP TABLE IF EXISTS trip; DROP TABLE IF EXISTS event; DROP TABLE IF EXISTS media;"
					+ "DROP TABLE IF EXISTS trip_tag; DROP TABLE IF EXISTS event_tag; DROP TABLE IF EXISTS media_tag;");
			onCreate(db);
		}

		public void onOpen(SQLiteDatabase db) {
			// db.execSQL("DELETE FROM event; DELETE FROM media;");
		}
	}

	public DBAdapter(Context ctx) {
		this.mCtx = ctx;
	}

	/**
	 * This method acts as a facade to allow open connection with the database
	 */
	public DBAdapter open() throws SQLException {
		Log.w(TAG, "The open method is entered");
		mDbHelper = new DatabaseHelper(mCtx);
		Log.w(TAG, "The DatabaseHelper and context is ok");
		mDb = mDbHelper.getWritableDatabase();
		Log.w(TAG, "The writable database is ok");
		return this;
	}

	public void close() {
		mDbHelper.close();
	}

	/**
	 * This method inserts a Account object within the database
	 */
	public long insertAccount(String username, String password, String email,
			String firstName, String lastName, String updateTime, String flag) {
		ContentValues initialValues = new ContentValues();
		initialValues.put("username", username);
		initialValues.put("password", password);
		initialValues.put("email", email);
		initialValues.put("first_name", firstName);
		initialValues.put("last_name", lastName);
		initialValues.put("flag", flag);
		initialValues.put("UPDATETIME", updateTime);
		return mDb.insertOrThrow("account", null, initialValues);
	}

	/**
	 * This method inserts a Account_Trip object within the database
	 */
	public long insertAccountTrip(String username, int tripId, int permissions,
			String flag) {
		ContentValues initialValues = new ContentValues();
		initialValues.put("username", username);
		initialValues.put("trip_id", tripId);
		initialValues.put("flag", flag);
		initialValues.put("permissions", permissions);
		return mDb.insertOrThrow("account_trip", null, initialValues);
	}

	/**
	 * This method inserts a Account object as a friend within the database
	 */
	public long insertFriend(String username, String friendUsername,
			String updateTime, String flag) {
		ContentValues initialValues = new ContentValues();
		initialValues.put("username", username);
		initialValues.put("friend_id", friendUsername);
		initialValues.put("flag", flag);
		initialValues.put("UPDATETIME", updateTime);
		Log.w(TAG, username + "," + friendUsername + "," + updateTime + ","
				+ flag);
		return mDb.insertOrThrow("friend", null, initialValues);
	}

	/**
	 * This method inserts a Coordinates object within the database
	 */
	public long insertCoordinates(int tripId, double latitude,
			double longitude, String dateTime, String flag) {

		Log.w(TAG, tripId + "," + latitude + "," + longitude + "," + dateTime);
		ContentValues coordinateValues = new ContentValues();

		coordinateValues.put("trip_id", tripId);
		coordinateValues.put("latitude", latitude);
		coordinateValues.put("longitude", longitude);
		coordinateValues.put("flag", flag);
		coordinateValues.put("datetime", dateTime);
		return mDb.insertOrThrow("coordinates", null, coordinateValues);
	}

	/**
	 * This method inserts a Trips object within the database
	 */
	public long insertTrip(int tripId, String tripName, String tripDescription,
			String updateTime, String flag) {

		Log.w(TAG, tripId + "," + tripName + "," + tripDescription + ", "
				+ updateTime);
		ContentValues tripValues = new ContentValues();

		tripValues.put("trip_id", tripId);
		tripValues.put("name", tripName);
		tripValues.put("description", tripDescription);
		tripValues.put("flag", flag);
		tripValues.put("UPDATETIME", updateTime);
		return mDb.insertOrThrow("trip", null, tripValues);
	}

	public long insertTrip(Trip trip) {

		Log.i(TAG, "Inserting trip: " + trip.toString());
		ContentValues tripValues = new ContentValues();

		tripValues.put("trip_id", trip.getTripId());
		tripValues.put("name", trip.getName());
		tripValues.put("description", trip.getDescription());
		tripValues.put("UPDATETIME", trip.getUpdatetime().toString());
		tripValues.put("flag", trip.getFlag());
		return mDb.insertOrThrow("trip", null, tripValues);
	}

	/**
	 * This method retrieves all loose media within the database depending the
	 * passed in value Loose media means media not associate with any trips or
	 * events
	 */

	public ArrayList<Media> getLooseMedia(String type) {

		Cursor cursor = null;
		ArrayList<Media> media = new ArrayList<Media>();

		cursor = mDb.rawQuery("select * from media where type = '" + type
				+ "' AND event_id = -1 AND flag != 'd'", null);

		int iRow = cursor.getColumnIndex("media_id");
		int iEventId = cursor.getColumnIndex("event_id");
		int iName = cursor.getColumnIndex("name");
		int iDescription = cursor.getColumnIndex("description");
		int iType = cursor.getColumnIndex("type");
		int iBlob = cursor.getColumnIndex("blob");
		int iUpdateTime = cursor.getColumnIndex("UPDATETIME");
		int iFlag = cursor.getColumnIndex("flag");

		Media m = null;

		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			m = new Media(cursor.getInt(iRow), cursor.getString(iName),
					cursor.getString(iDescription), cursor.getString(iType),
					cursor.getBlob(iBlob), cursor.getInt(iEventId),
					Timestamp.valueOf(cursor.getString(iUpdateTime)),
					cursor.getString(iFlag));
			media.add(m);
		}

		Log.e(TAG, "Contents of all loose media: " + media.toString());
		cursor.close();
		return media;

	}

	/**
	 * This method retrieves all Coordinate points relating to a trip from
	 * within the database
	 */

	public ArrayList<Coordinate> getCoordinates(int tripId) {

		Cursor cursor = null;
		ArrayList<Coordinate> tripCoordinates = new ArrayList<Coordinate>();

		cursor = mDb.rawQuery("select * from coordinates where trip_id = "
				+ tripId, null);

		int iRow = cursor.getColumnIndex("trip_id");
		int iDateTime = cursor.getColumnIndex("datetime");
		int iLatitude = cursor.getColumnIndex("latitude");
		int iLongitude = cursor.getColumnIndex("longitude");
		int iFlag = cursor.getColumnIndex("flag");
		Coordinate c = null;

		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			c = new Coordinate(cursor.getDouble(iLatitude),
					cursor.getDouble(iLongitude), Timestamp.valueOf(cursor
							.getString(iDateTime)), cursor.getInt(iRow),
					cursor.getString(iFlag));
			Log.e(TAG, "Coordinate retrieved =" + c.toString());
			tripCoordinates.add(c);
		}

		Log.e(TAG,
				"Contents of all coordinates for a Trip: "
						+ tripCoordinates.toString());
		cursor.close();
		return tripCoordinates;
	}

	/**
	 * This method retrieves all accepted friends from the database
	 */

	public ArrayList<String> getFriends() {

		Cursor cursor = null;
		ArrayList<String> friends = new ArrayList<String>();

		cursor = mDb.rawQuery("select * from friend", null);

		int iRow = cursor.getColumnIndex("username");
		int iFriendId = cursor.getColumnIndex("friend_id");
		int iUpdateTime = cursor.getColumnIndex("UPDATETIME");
		int iFlag = cursor.getColumnIndex("flag");
		Friend f = null;

		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			// f = new Friend(cursor.getString(iRow), cursor.getInt(iFriendId),
			// Timestamp.valueOf(cursor.getString(iUpdateTime)),
			// cursor.getString(iFlag));
			friends.add(cursor.getString(iFriendId));
			Log.e(TAG, "Friend retrieved =" + cursor.getString(iFriendId));

		}

		Log.e(TAG, "Contents of all friends:" + friends.toString());
		cursor.close();
		return friends;
	}

	/**
	 * This method retrieves all Events relating to a trip from within the
	 * database
	 */

	public ArrayList<Event> getEvents(int tripId) {

		Cursor cursor = null;
		ArrayList<Event> tripEvents = new ArrayList<Event>();

		cursor = mDb.rawQuery("select * from event where trip_id = " + tripId,
				null);

		int iRow = cursor.getColumnIndex("event_id");
		int iTripId = cursor.getColumnIndex("trip_id");
		int iLatitude = cursor.getColumnIndex("latitude");
		int iLongitude = cursor.getColumnIndex("longitude");
		int iDateTime = cursor.getColumnIndex("dtime");
		int iName = cursor.getColumnIndex("name");
		int iDescription = cursor.getColumnIndex("description");
		int iUpdateTime = cursor.getColumnIndex("UPDATETIME");
		int iFlag = cursor.getColumnIndex("flag");
		Event e = null;

		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			e = new Event(cursor.getInt(iRow), cursor.getDouble(iLatitude),
					cursor.getDouble(iLongitude), Timestamp.valueOf(cursor
							.getString(iDateTime)), cursor.getString(iName),
					cursor.getString(iDescription), cursor.getInt(iTripId),
					Timestamp.valueOf(cursor.getString(iUpdateTime)),
					cursor.getString(iFlag));
			Log.e(TAG, "Event retrieved =" + e.toString());
			tripEvents.add(e);
		}

		Log.e(TAG,
				"Contents of all events for a Trip: " + tripEvents.toString());
		cursor.close();
		return tripEvents;
	}

	/**
	 * This method inserts a Event object within the database
	 */
	public long insertEvent(int eventId, int tripId, double longitude,
			double latitude, String dateTime, String eventName,
			String eventDescription, String updateTime, String flag) {

		Log.w(TAG, eventId + "," + tripId + "," + longitude + "," + latitude
				+ "," + dateTime + "," + eventName + "," + eventDescription
				+ ", " + updateTime + ", " + flag);
		ContentValues eventValues = new ContentValues();
		eventValues.put("event_id", eventId);
		eventValues.put("trip_id", tripId);
		eventValues.put("longitude", longitude);
		eventValues.put("latitude", latitude);
		eventValues.put("dtime", dateTime);
		eventValues.put("name", eventName);
		eventValues.put("description", eventDescription);
		eventValues.put("UPDATETIME", updateTime);
		eventValues.put("flag", flag);
		return mDb.insertOrThrow("event", null, eventValues);
	}

	/**
	 * This method inserts a tag object within the database
	 */
	public long insertTags(String text, int recordId, String tagType,
			String updateTime, String flag) {

		Log.w(TAG, "inserting Tag " + text + "," + recordId + "," + tagType
				+ ", " + updateTime + ", " + flag);
		String tableType = "";
		String foreignKeyId = "";

		if (tagType.equals("media")) {
			tableType = "media_tag";
			foreignKeyId = "media_id";
		} else if (tagType.equals("event")) {
			tableType = "event_tag";
			foreignKeyId = "event_id";
		} else if (tagType.equals("trip")) {
			tableType = "trip_tag";
			foreignKeyId = "trip_id";
		} else {
			return 0;
		}

		ContentValues tagValues = new ContentValues();
		tagValues.put("text", text);
		tagValues.put(foreignKeyId, recordId);
		tagValues.put("UPDATETIME", updateTime);
		tagValues.put("flag", flag);
		Log.e(TAG, tagValues.toString());
		return mDb.insertOrThrow(tableType, null, tagValues);
	}

	/**
	 * This method inserts a Media object within the database
	 */
	public long insertEventMedia(int mediaId, String mediaText,
			String mediaDescription, String mediaType, byte[] blob,
			int eventId, String updateTime, String flag) {

		Log.w(TAG, mediaId + "," + eventId + "," + mediaText + ","
				+ mediaDescription + "," + mediaType + "," + blob + ", "
				+ updateTime + ", " + flag);
		ContentValues mediaValues = new ContentValues();
		mediaValues.put("media_id", mediaId);
		mediaValues.put("event_id", eventId);
		mediaValues.put("name", mediaText);
		mediaValues.put("description", mediaDescription);
		mediaValues.put("type", mediaType);
		mediaValues.put("blob", blob);
		mediaValues.put("UPDATETIME", updateTime);
		mediaValues.put("flag", flag);
		return mDb.insertOrThrow("media", null, mediaValues);
	}

	/**
	 * This method removes a Media object within the database
	 */
	public int deleteMedia(int mediaId) {
		/*
		 * ContentValues values = new ContentValues(); values.put("flag", "d");
		 * // updating row return mDb.update("media", values, "media_id"+"= ?",
		 * new String[] { String.valueOf(mediaId) });
		 */

		return mDb.delete("media", "media_id = " + mediaId, null);
	}

	/**
	 * This method removes a Event object within the database
	 */
	public int deleteEvent(int eventId) {
		/*
		 * ContentValues values = new ContentValues(); values.put("flag", "d");
		 * // updating row return mDb.update("event", values, "event_id"+"= ?",
		 * new String[] { String.valueOf(eventId) });
		 */
		return mDb.delete("event", "event_id = " + eventId, null);
	}

	/**
	 * This method removes a Event object within the database
	 */
	public int deleteTrip(int tripId) {
		/*
		 * ContentValues values = new ContentValues(); values.put("flag", "d");
		 * // updating row return mDb.update("trip", values, "trip_id"+"= ?",
		 * new String[] { String.valueOf(tripId) });
		 */
		return mDb.delete("trip", "trip_id = " + tripId, null);
	}

	/**
	 * This method updates a Media object within the database
	 */
	public int updateMedia(Media media) {
		ContentValues values = new ContentValues();
		values.put("updatetime", String.valueOf(media.getUpdatetime()));
		values.put("flag", media.getFlag());

		// updating row
		return mDb.update("media", values, "media_id" + "= ?",
				new String[] { String.valueOf(media.getMediaId()) });
	}

	/**
	 * This method updates a Event object within the database
	 */
	public int updateEvent(Event event) {
		ContentValues values = new ContentValues();
		values.put("updatetime", String.valueOf(event.getUpdatetime()));
		values.put("flag", event.getFlag());
		return mDb.update("event", values, "event_id" + "= ?",
				new String[] { String.valueOf(event.getPoiId()) });
	}

	/**
	 * This method updates a Trip object within the database
	 */
	public int updateTrip(Trip trip) {
		ContentValues values = new ContentValues();
		values.put("updatetime", String.valueOf(trip.getUpdatetime()));
		values.put("flag", trip.getFlag());
		// updating row
		return mDb.update("trip", values, "trip_id" + "= ?",
				new String[] { String.valueOf(trip.getTripId()) });
	}

	/**
	 * This method updates a Account Trip object within the database
	 */
	public int updateAccountTrip(AccountTrip accountTrip) {
		ContentValues values = new ContentValues();
		values.put("flag", accountTrip.getFlag());
		// updating row
		return mDb.update("account_trip", values, "trip_id"
				+ "= ? and username = ?",
				new String[] { String.valueOf(accountTrip.getTripId()),
						accountTrip.getUsername() });
	}

	/**
	 * This method updates a Media object flag within the database
	 */
	public int setMediaFlag(int mediaId, String flag) {
		ContentValues values = new ContentValues();
		values.put("flag", flag);

		// updating row
		return mDb.update("media", values, "media_id" + "= ?",
				new String[] { String.valueOf(mediaId) });
	}

	/**
	 * This method updates a Event object flag within the database
	 */
	public int setEventFlag(int eventId, String flag) {
		ContentValues values = new ContentValues();
		values.put("flag", flag);
		return mDb.update("event", values, "event_id" + "= ?",
				new String[] { String.valueOf(eventId) });
	}

	/**
	 * This method updates a Trip object flag within the database
	 */
	public int setTripFlag(int tripId, String flag) {
		ContentValues values = new ContentValues();
		values.put("flag", flag);
		// updating row
		return mDb.update("trip", values, "trip_id" + "= ?",
				new String[] { String.valueOf(tripId) });
	}

	/**
	 * This method updates a Tag object within the database
	 */
	public int updateTag(Tag t, String type) {

		String tableType = "";

		if (type.equals("trip")) {
			tableType = "trip_tag";
		} else if (type.equals("event")) {
			tableType = "event_tag";
		} else if (type.equals("media")) {
			tableType = "media_tag";
		}

		ContentValues values = new ContentValues();
		values.put("updatetime", String.valueOf(t.getUpdatetime()));
		values.put("flag", t.getFlag());
		// updating row
		return mDb.update(tableType, values, tableType + "= ?",
				new String[] { String.valueOf(t.getTagId()) });
	}

	/**
	 * This method retrieves a Account object within the database
	 */
	public ArrayList<String> getAccount() {
		String[] columns = new String[] { "username", "password", "email",
				"first_name", "last_name", "UPDATETIME", "flag" };
		Cursor c = mDb.query("account", columns, null, null, null, null, null);
		ArrayList<String> accounts = new ArrayList<String>();

		int iRow = c.getColumnIndex("username");
		int iPassword = c.getColumnIndex("password");
		int iEmail = c.getColumnIndex("email");
		int iFirstName = c.getColumnIndex("first_name");
		int iLastName = c.getColumnIndex("last_name");
		int iUpdateTime = c.getColumnIndex("UPDATETIME");
		int iFlag = c.getColumnIndex("flag");

		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			accounts.add(c.getString(iRow));
		}
		c.close();
		return accounts;

	}

	/**
	 * This method retrieves all events within the database
	 */
	public ArrayList<Event> getEvent() {
		String[] columns = new String[] { "event_id", "trip_id", "longitude",
				"latitude", "dtime", "name", "description", "UPDATETIME",
				"flag" };
		Cursor c = mDb.query("event", columns, null, null, null, null, null);
		ArrayList<Event> list = new ArrayList<Event>();

		int iRow = c.getColumnIndex("event_id");
		int iTripId = c.getColumnIndex("trip_id");
		int iLongitude = c.getColumnIndex("longitude");
		int iLatitude = c.getColumnIndex("latitude");
		int iDtime = c.getColumnIndex("dtime");
		int iName = c.getColumnIndex("name");
		int iDescription = c.getColumnIndex("description");
		int iUpdateTime = c.getColumnIndex("UPDATETIME");
		int iFlag = c.getColumnIndex("flag");

		Event e = null;
		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			e = new Event(c.getInt(iRow), c.getDouble(iLongitude),
					c.getDouble(iLatitude), Timestamp.valueOf(c
							.getString(iDtime)), c.getString(iName),
					c.getString(iDescription), c.getInt(iTripId),
					Timestamp.valueOf(c.getString(iUpdateTime)),
					c.getString(iFlag));
			list.add(e);
		}
		c.close();
		return list;

	}

	/**
	 * This method retrieves all trips within the database
	 */
	public ArrayList<Trip> getTrips() {
		String[] columns = new String[] { "trip_id", "name", "description",
				"UPDATETIME", "flag" };
		Cursor c = mDb.query("trip", columns, null, null, null, null, null);
		ArrayList<Trip> list = new ArrayList<Trip>();

		int iRow = c.getColumnIndex("trip_id");
		int iTripName = c.getColumnIndex("name");
		int iTripDescription = c.getColumnIndex("description");
		int iUpdateTime = c.getColumnIndex("UPDATETIME");
		int iFlag = c.getColumnIndex("flag");
		Trip t = null;
		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			t = new Trip(c.getInt(iRow), c.getString(iTripName),
					c.getString(iTripDescription), Timestamp.valueOf(c
							.getString(iUpdateTime)), c.getString(iFlag));
			list.add(t);
		}
		c.close();
		return list;

	}

	/**
	 * This method retrieves all trips given a username from account_trip
	 */
	public ArrayList<Trip> getTripsByFlagByUser(String username, String pFlag) {
		Cursor cursor = null;
		ArrayList<Trip> list = new ArrayList<Trip>();
		// no prepared statement?
		cursor = mDb
				.rawQuery(
						"select t.trip_id, t.name, t.description, t.UPDATETIME, t.flag from account_trip as ti inner join trip as t on ti.trip_id = t.trip_id where t.flag = '"
								+ pFlag
								+ "' and ti.username = '"
								+ username
								+ "'", null);
		// cursor = mDb.rawQuery("select * from trip where flag = 'n'" , null);

		if (cursor != null) {
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				int tId = cursor.getInt(cursor.getColumnIndex("trip_id"));
				String name = cursor.getString(cursor.getColumnIndex("name"));
				String description = cursor.getString(cursor
						.getColumnIndex("description"));
				Timestamp updateTime = Timestamp.valueOf(cursor
						.getString(cursor.getColumnIndex("UPDATETIME")));
				String flag = cursor.getString(cursor.getColumnIndex("flag"));

				Trip t = new Trip(tId, name, description, updateTime, flag);
				list.add(t);
			}
			cursor.close();
		}
		return list;

	}

	/**
	 * This method retrieves all account_trips given a username
	 */
	public ArrayList<AccountTrip> getAccountTripsByFlagByUser(String username,
			String pFlag) {
		Cursor cursor = null;
		ArrayList<AccountTrip> list = new ArrayList<AccountTrip>();
		// no prepared statement?
		cursor = mDb.rawQuery("select * from account_trip where flag = '"
				+ pFlag + "' and username = '" + username + "'", null);
		// cursor = mDb.rawQuery("select * from trip where flag = 'n'" , null);

		if (cursor != null) {
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				String uname = cursor.getString(cursor
						.getColumnIndex("username"));
				int tId = cursor.getInt(cursor.getColumnIndex("trip_id"));
				int permissions = cursor.getInt(cursor
						.getColumnIndex("permissions"));
				String flag = cursor.getString(cursor.getColumnIndex("flag"));

				AccountTrip t = new AccountTrip(uname, tId, flag, permissions);
				list.add(t);
			}
			cursor.close();
		}
		return list;

	}

	/**
	 * This method retrieves all account_trips given a username
	 */
	public ArrayList<AccountTrip> getAccountTripsByFlag(String pFlag) {
		Cursor cursor = null;
		ArrayList<AccountTrip> list = new ArrayList<AccountTrip>();
		// no prepared statement?
		cursor = mDb.rawQuery("select * from account_trip where flag = '" + pFlag + "';" , null);
		// cursor = mDb.rawQuery("select * from trip where flag = 'n'" , null);

		if (cursor != null) {
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				String uname = cursor.getString(cursor
						.getColumnIndex("username"));
				int tId = cursor.getInt(cursor.getColumnIndex("trip_id"));
				int permissions = cursor.getInt(cursor
						.getColumnIndex("permissions"));
				String flag = cursor.getString(cursor.getColumnIndex("flag"));

				AccountTrip t = new AccountTrip(uname, tId, flag, permissions);
				list.add(t);
			}
			cursor.close();
		}
		return list;

	}
	/**
	 * This method retrieves all events given a username and flag from
	 * account_trip
	 */
	public ArrayList<Event> getEventsByFlagByUser(String username, String pFlag) {
		Cursor cursor = null;
		ArrayList<Event> eList = new ArrayList<Event>();

		cursor = mDb.rawQuery("select * from event where flag = '" + "n" + "'", null);
		// + pFlag + "' and ti.username = '" + username +"'" , null);
		
		/*cursor = mDb.rawQuery("select e.event_id, e.latitude, e.longitude, e.dtime, e.UPDATETIME, e.name, e.description, e.trip_id, e.flag from account_trip as ti inner join trip as t on ti.trip_id = t.trip_id inner join event as e on t.trip_id = e.trip_id where e.flag = '"
								+ pFlag
								+ "' and ti.username = '"
								+ username
								+ "'", null);*/
		if (cursor != null) {

			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				Log.i(TAG, "1");
				int eId = cursor.getInt(cursor.getColumnIndex("event_id"));
				Log.i(TAG, "2");
				double latitude = cursor.getDouble(cursor
						.getColumnIndex("latitude"));
				Log.i(TAG, "3");
				double longitude = cursor.getDouble(cursor
						.getColumnIndex("longitude"));
				Log.i(TAG, "4");
				Timestamp dtime = Timestamp.valueOf(cursor.getString(cursor
						.getColumnIndex("dtime")));
				Log.i(TAG, "5");
				Timestamp updateTime = Timestamp.valueOf(cursor
						.getString(cursor.getColumnIndex("UPDATETIME")));
				Log.i(TAG, "6");
				String name = cursor.getString(cursor.getColumnIndex("name"));
				Log.i(TAG, "7");
				String description = cursor.getString(cursor
						.getColumnIndex("description"));
				Log.i(TAG, "8");
				int tId = cursor.getInt(cursor.getColumnIndex("trip_id"));
				String flag = cursor.getString(cursor.getColumnIndex("flag"));

				Event e = new Event(eId, latitude, longitude, dtime, name,
						description, tId, updateTime, flag);
				
				eList.add(e);
				
				Log.i(TAG, "EVENT: " + e.toString());
			}
			cursor.close();
		}
		Log.i(TAG, "LIST: " + eList.toString());
		return eList;

	}

	/**
	 * This method retrieves all trips given a username and flag from
	 * account_trip
	 */
	public ArrayList<Media> getMediasByFlagByUser(String username, String pFlag) {
		Cursor cursor = null;
		ArrayList<Media> list = new ArrayList<Media>();
		// no prepared statement?
		cursor = mDb
				.rawQuery(
						"select m.media_id, m.event_id, m.name, m.description, m.type, m.blob, m.UPDATETIME, m.flag from account_trip as ti inner join trip as t on ti.trip_id = t.trip_id inner join event as e on t.trip_id = e.trip_id inner join media as m on e.event_id = m.event_id where m.flag = '"
								+ pFlag
								+ "' and ti.username = '"
								+ username
								+ "'", null);
		// cursor = mDb.rawQuery("select * from trip where flag = 'n'" , null);

		if (cursor != null) {
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				int mId = cursor.getInt(cursor.getColumnIndex("media_id"));
				int eventId = cursor.getInt(cursor.getColumnIndex("event_id"));
				String name = cursor.getString(cursor.getColumnIndex("name"));
				String description = cursor.getString(cursor
						.getColumnIndex("description"));
				String mediaType = cursor.getString(cursor
						.getColumnIndex("type"));
				byte[] mediaBlob = cursor
						.getBlob(cursor.getColumnIndex("blob"));
				Timestamp updateTime = Timestamp.valueOf(cursor
						.getString(cursor.getColumnIndex("UPDATETIME")));
				String flag = cursor.getString(cursor.getColumnIndex("flag"));

				Media media = new Media(mId, name, description, mediaType,
						mediaBlob, eventId, updateTime, flag);
				list.add(media);
			}
			cursor.close();
		}
		return list;

	}

	/**
	 * This method retrieves all tags given a username from account_trip
	 */
	public ArrayList<Tag> getNewTripTagsByUser(String username) {
		Cursor cursor = null;
		ArrayList<Tag> list = new ArrayList<Tag>();
		Tag t = null;
		cursor = mDb
				.rawQuery(
						"select ttag.trip_id, ttag.text, ttag.updatetime, ttag.flag from account_trip ti inner join trip t on ti.trip_id = t.trip_id inner join trip_tag ttag on t.trip_id = ttag.trip_id where ttag.flag = 'n' and ti.username = '"
								+ username + "'", null);
		// cursor = mDb.rawQuery("select * from trip_tag where flag = 'n'" ,
		// null);

		if (cursor != null) {
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				int tId = cursor.getInt(cursor.getColumnIndex("trip_id"));
				String text = cursor.getString(cursor.getColumnIndex("text"));
				String type = "trip";
				Timestamp updateTime = Timestamp.valueOf(cursor
						.getString(cursor.getColumnIndex("UPDATETIME")));
				String flag = cursor.getString(cursor.getColumnIndex("flag"));

				t = new Tag(tId, text, type, updateTime, flag);
				Log.e(TAG, "tag =" + t.toString());
				list.add(t);
			}
			cursor.close();
		}

		return list;

	}

	public ArrayList<Tag> getNewEventTagsByUser(String username) {
		Cursor cursor = null;
		ArrayList<Tag> list = new ArrayList<Tag>();
		Tag t = null;
		cursor = mDb
				.rawQuery(
						"select etag.event_id, etag.text, etag.updatetime, etag.flag from account_trip ti inner join trip t on ti.trip_id = t.trip_id inner join event e on t.trip_id = e.trip_id inner join event_tag etag on e.event_id = etag.event_id where etag.flag = 'n' and ti.username = '"
								+ username + "'", null);

		if (cursor != null) {
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				int tId = cursor.getInt(cursor.getColumnIndex("event_id"));
				String text = cursor.getString(cursor.getColumnIndex("text"));
				String type = "event";
				Timestamp updateTime = Timestamp.valueOf(cursor
						.getString(cursor.getColumnIndex("UPDATETIME")));
				String flag = cursor.getString(cursor.getColumnIndex("flag"));

				t = new Tag(tId, text, type, updateTime, flag);
				Log.e(TAG, "tag =" + t.toString());
				list.add(t);
			}
			cursor.close();
		}
		return list;
	}

	public ArrayList<Tag> getNewMediaTagsByUser(String username) {
		Cursor cursor = null;
		ArrayList<Tag> list = new ArrayList<Tag>();
		Tag t = null;
		cursor = mDb
				.rawQuery(
						"select mtag.media_id, mtag.text, mtag.updatetime, mtag.flag from account_trip ti inner join trip t on ti.trip_id = t.trip_id inner join event e on t.trip_id = e.trip_id inner join media m on e.event_id = m.event_id inner join media_tag mtag on m.media_id = mtag.media_id where mtag.flag = 'n' and ti.username = '"
								+ username + "'", null);

		if (cursor != null) {
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				int tId = cursor.getInt(cursor.getColumnIndex("media_id"));
				String text = cursor.getString(cursor.getColumnIndex("text"));
				String type = "media";
				Timestamp updateTime = Timestamp.valueOf(cursor
						.getString(cursor.getColumnIndex("UPDATETIME")));
				String flag = cursor.getString(cursor.getColumnIndex("flag"));

				t = new Tag(tId, text, type, updateTime, flag);
				Log.e(TAG, "tag =" + t.toString());
				list.add(t);
			}
			cursor.close();
		}

		return list;

	}

	/**
	 * This method retrieves a media within the database given an Id
	 */
	public Media getMediaById(int mediaId) {
		Cursor cursor = null;
		Media media = null;
		cursor = mDb.rawQuery("select * from media where " + "media_id " + "="
				+ mediaId, null);
		if (cursor != null) {
			if (cursor.moveToFirst()) {
				int mId = cursor.getInt(cursor.getColumnIndex("media_id"));
				int eventId = cursor.getInt(cursor.getColumnIndex("event_id"));
				String name = cursor.getString(cursor.getColumnIndex("name"));
				String description = cursor.getString(cursor
						.getColumnIndex("description"));
				String mediaType = cursor.getString(cursor
						.getColumnIndex("type"));
				byte[] mediaBlob = cursor
						.getBlob(cursor.getColumnIndex("blob"));
				Timestamp updateTime = Timestamp.valueOf(cursor
						.getString(cursor.getColumnIndex("UPDATETIME")));
				String flag = cursor.getString(cursor.getColumnIndex("flag"));

				media = new Media(mId, name, description, mediaType, mediaBlob,
						eventId, updateTime, flag);
			}
			cursor.close();
		}
		return media;
	}

	/**
	 * This method retrieves a event within the database given an Id
	 */
	public Event getEventById(int eventId) {
		Cursor cursor = null;
		Event event = null;
		cursor = mDb.rawQuery("select * from event where " + "event_id " + "="
				+ eventId, null);
		if (cursor != null) {
			if (cursor.moveToFirst()) {
				int eId = cursor.getInt(cursor.getColumnIndex("event_id"));
				int tripId = cursor.getInt(cursor.getColumnIndex("trip_id"));
				double lng = cursor.getDouble(cursor
						.getColumnIndex("longitude"));
				double lat = cursor
						.getDouble(cursor.getColumnIndex("latitude"));
				Timestamp dtime = Timestamp.valueOf(cursor.getString(cursor
						.getColumnIndex("dtime")));
				String name = cursor.getString(cursor.getColumnIndex("name"));
				String description = cursor.getString(cursor
						.getColumnIndex("description"));
				Timestamp updateTime = Timestamp.valueOf(cursor
						.getString(cursor.getColumnIndex("UPDATETIME")));
				String flag = cursor.getString(cursor.getColumnIndex("flag"));

				event = new Event(eId, lng, lat, dtime, name, description,
						tripId, updateTime, flag);

				// byte[] image =
				// cursor.getBlob(cursor.getColumnIndex(BanksTable.COL_IMAGE));
			}
			cursor.close();
		}
		return event;

	}

	/**
	 * This method retrieves the media associated to an event
	 */
	public ArrayList<Media> getMediasByEventId(int eventId) {
		Cursor cursor = null;
		Media m = null;
		ArrayList<Media> list = new ArrayList<Media>();
		cursor = mDb.rawQuery("select * from media where " + "event_id " + "="
				+ eventId, null);
		if (cursor != null) {
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				int mId = cursor.getInt(cursor.getColumnIndex("media_id"));
				int eId = cursor.getInt(cursor.getColumnIndex("event_id"));
				String name = cursor.getString(cursor.getColumnIndex("name"));
				String description = cursor.getString(cursor
						.getColumnIndex("description"));
				String type = cursor.getString(cursor.getColumnIndex("type"));
				byte[] blob = cursor.getBlob(cursor.getColumnIndex("blob"));
				Timestamp updateTime = Timestamp.valueOf(cursor
						.getString(cursor.getColumnIndex("UPDATETIME")));
				String flag = cursor.getString(cursor.getColumnIndex("flag"));

				m = new Media(mId, name, description, type, blob, eId,
						updateTime, flag);
				list.add(m);
				// byte[] image =
				// cursor.getBlob(cursor.getColumnIndex(BanksTable.COL_IMAGE));
			}
			cursor.close();
		}
		return list;

	}

	/**
	 * This method retrieves a trip within the database given an Id
	 */
	public Trip getTripById(int tripId) {
		Cursor cursor = null;
		Trip trip = null;

		cursor = mDb.rawQuery("select * from trip where " + "trip_id " + "="
				+ tripId, null);
		if (cursor != null) {
			if (cursor.moveToFirst()) {
				int tId = cursor.getInt(cursor.getColumnIndex("trip_id"));
				String tripName = cursor.getString(cursor
						.getColumnIndex("name"));
				String tripDescription = cursor.getString(cursor
						.getColumnIndex("description"));
				Timestamp updateTime = Timestamp.valueOf(cursor
						.getString(cursor.getColumnIndex("UPDATETIME")));
				String flag = cursor.getString(cursor.getColumnIndex("flag"));

				trip = new Trip(tId, tripName, tripDescription, updateTime,
						flag);
				Log.e(TAG, "DBADAPTER trip retrieved = " + updateTime);
				// byte[] image =
				// cursor.getBlob(cursor.getColumnIndex(BanksTable.COL_IMAGE));
			}
			cursor.close();
		}
		return trip;

	}

	/**
	 * This method retrieves a trip within the database given an name
	 */
	public Trip getTripByName(String tripN) {
		Cursor cursor = null;
		Trip trip = null;

		cursor = mDb.rawQuery("select * from trip where " + "name " + "="
				+ tripN, null);
		if (cursor != null) {
			if (cursor.moveToFirst()) {
				int tId = cursor.getInt(cursor.getColumnIndex("trip_id"));
				String tripName = cursor.getString(cursor
						.getColumnIndex("name"));
				String tripDescription = cursor.getString(cursor
						.getColumnIndex("description"));
				Timestamp updateTime = Timestamp.valueOf(cursor
						.getString(cursor.getColumnIndex("UPDATETIME")));
				String flag = cursor.getString(cursor.getColumnIndex("flag"));

				trip = new Trip(tId, tripName, tripDescription, updateTime,
						flag);
				Log.e(TAG, "DBADAPTER trip retrieved = " + updateTime);
				// byte[] image =
				// cursor.getBlob(cursor.getColumnIndex(BanksTable.COL_IMAGE));
			}
			cursor.close();
		}
		return trip;

	}

	/**
	 * This method retrieves all media within the database
	 */
	public ArrayList<Media> getMedia() {
		String[] columns = new String[] { "media_id", "event_id", "name",
				"description", "type", "blob", "UPDATETIME", "flag" };
		Cursor c = mDb.query("media", columns, null, null, null, null, null);
		ArrayList<Media> list = new ArrayList<Media>();

		int iRow = c.getColumnIndex("media_id");
		int iEventId = c.getColumnIndex("event_id");
		int iName = c.getColumnIndex("name");
		int iDescription = c.getColumnIndex("description");
		int iType = c.getColumnIndex("type");
		int iBlob = c.getColumnIndex("blob");
		int iUpdateTime = c.getColumnIndex("UPDATETIME");
		int iFlag = c.getColumnIndex("flag");

		Media m = null;

		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			m = new Media(c.getInt(iRow), c.getString(iName),
					c.getString(iDescription), c.getString(iType),
					c.getBlob(iBlob), c.getInt(iEventId), Timestamp.valueOf(c
							.getString(iUpdateTime)), c.getString(iFlag));
			Log.e(TAG, "Media retrieved =" + m.toString());
			list.add(m);
		}
		Log.e(TAG, "Contents of all Trips: " + list.toString());
		c.close();
		return list;
	}

	/**
	 * This method retrieves a count for all trips within the database
	 */
	public int getTripCount() {

		String sql = "SELECT COUNT(*) FROM trip";
		SQLiteStatement statement = mDb.compileStatement(sql);
		return (int) statement.simpleQueryForLong();
	}

	/**
	 * This method retrieves a count for all events within the database
	 */
	public int getEventCount() {
		String sql = "SELECT COUNT(*) FROM event";
		SQLiteStatement statement = mDb.compileStatement(sql);
		return (int) statement.simpleQueryForLong();
	}

	/**
	 * This method retrieves a count for all media within the database
	 */
	public int getMediaCount() {
		String sql = "SELECT COUNT(*) FROM media";
		SQLiteStatement statement = mDb.compileStatement(sql);
		return (int) statement.simpleQueryForLong();
	}

	/**
	 * This method retrieves all trips after a certain timestamp for a given
	 * user
	 */
	public ArrayList<Trip> getTripsByUpdateTime(Timestamp updateTime) {
		Cursor cursor = null;
		ArrayList<Trip> tList = new ArrayList<Trip>();
		cursor = mDb.rawQuery("select * from trip where UPDATETIME > "
				+ updateTime, null);
		if (cursor != null) {
			if (cursor.moveToFirst()) {
				int tripId = cursor.getInt(cursor.getColumnIndex("trip_id"));
				String name = cursor.getString(cursor.getColumnIndex("name"));
				String description = cursor.getString(cursor
						.getColumnIndex("description"));
				Timestamp tUpdateTime = Timestamp.valueOf(cursor
						.getString(cursor.getColumnIndex("UPDATETIME")));
				String flag = cursor.getString(cursor.getColumnIndex("flag"));

				Trip t = new Trip(tripId, name, description, tUpdateTime, flag);
				tList.add(t);
			}
			cursor.close();
		}
		return tList;

	}

	/**
	 * This method retrieves a Account object within the database, somehow
	 * calling values of -1, I have no idea why.
	 */
	public String[] getAccountArray() {
		String[] columns = new String[] { "username", "password", "email",
				"first_name", "last_name", "UPDATETIME", "flag" };
		Cursor c = mDb.query("account", columns, null, null, null, null, null);
		String[] result = new String[columns.length];

		int iRow = c.getColumnIndex("username");
		int iPassword = c.getColumnIndex("password");
		int iEmail = c.getColumnIndex("email");
		int iFirstName = c.getColumnIndex("first_name");
		int iLastName = c.getColumnIndex("last_name");
		int iUpdateTime = c.getColumnIndex("UPDATETIME");
		int iFlag = c.getColumnIndex("flag");

		/*
		 * for(c.moveToFirst(), i = 0; !c.isAfterLast(); c.moveToNext()){
		 * //result = result + c.getString(iRow) + " " + c.getString(iPassword)
		 * + " " + //c.getString(iEmail) + " " + c.getString(iFirstName) + " " +
		 * c.getString(iLastName) + "\n"; result[i] = iRow; c. }
		 */
		result[0] = c.getString(iRow);
		result[1] = c.getString(iPassword);
		result[2] = c.getString(iEmail);
		result[3] = c.getString(iFirstName);
		result[4] = c.getString(iLastName);
		result[5] = c.getString(iUpdateTime);
		result[6] = c.getString(iFlag);

		c.close();
		return result;

	}

	/**
	 * This method retrieves all tags of a media item
	 */
	public ArrayList<String> getTagsByMediaId(int mediaId) {
		String[] columns = new String[] { "text", "media_id", "UPDATETIME",
				"flag" };
		Cursor c = mDb
				.query("media_tag", columns, null, null, null, null, null);
		ArrayList<String> list = new ArrayList<String>();

		int iRow = c.getColumnIndex("text");
		int iMediaId = c.getColumnIndex("media_id");
		int iUpdateTime = c.getColumnIndex("UPDATETIME");
		int iFlag = c.getColumnIndex("flag");

		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			Log.e(TAG, "Media retrieved =" + c.getString(iRow));
			list.add(c.getString(iRow));
		}
		c.close();
		return list;
	}

	/**
	 * This method retrieves media ids based on a tag provided
	 */
	public ArrayList<Integer> getMediaIdsByTag(String mediaTag) {
		Cursor cursor = null;
		ArrayList<Integer> mediaIds = new ArrayList<Integer>();

		cursor = mDb.rawQuery("select * from media_tag where text LIKE '%"
				+ mediaTag + "%", null);
		if (cursor != null) {
			if (cursor.moveToFirst()) {
				String text = cursor.getString(cursor.getColumnIndex("text"));
				int mediaId = cursor.getInt(cursor.getColumnIndex("media_id"));
				Timestamp updateTime = Timestamp.valueOf(cursor
						.getString(cursor.getColumnIndex("UPDATETIME")));
				String flag = cursor.getString(cursor.getColumnIndex("flag"));

				mediaIds.add(mediaId);
			}
			cursor.close();
		}
		return mediaIds;

	}

	/**
	 * This method retrieves event ids based on a tag provided
	 */
	public int[] getEventIdsByTag(String eventTag) {
		Cursor cursor = null;
		ArrayList<Integer> eventIds = new ArrayList<Integer>();

		cursor = mDb.rawQuery("select * from event_tag where text ='"
				+ eventTag + "'", null);
		if (cursor != null) {
			if (cursor.moveToFirst()) {
				String text = cursor.getString(cursor.getColumnIndex("text"));
				int eventId = cursor.getInt(cursor.getColumnIndex("event_id"));
				Timestamp updateTime = Timestamp.valueOf(cursor
						.getString(cursor.getColumnIndex("UPDATETIME")));
				String flag = cursor.getString(cursor.getColumnIndex("flag"));

				eventIds.add(eventId);
			}
			cursor.close();
		}

		int[] result = new int[eventIds.size()];

		for (int i = 0; i < eventIds.size(); i++) {
			result[i] = eventIds.get(i);
		}
		return result;

	}

	public Collection<Event> getNewEventByUser(String username) {
		Cursor cursor = null;
		ArrayList<Event> list = new ArrayList<Event>();
		cursor = mDb
				.rawQuery(
						"select e.event_id, e.trip_id, e.latitude, e.longitude, e.name, e.dtime, e.description, e.UPDATETIME, e.flag from account_trip ti inner join trip t on ti.trip_id = t.trip_id inner join event e on t.trip_id = e.trip_id where e.flag = 'n' and ti.username = '"
								+ username + "'", null);
		// cursor = mDb.rawQuery("select * from event where flag = 'n'" , null);
		if (cursor != null) {
			if (cursor.moveToFirst()) {
				int eId = cursor.getInt(cursor.getColumnIndex("event_id"));
				int tId = cursor.getInt(cursor.getColumnIndex("trip_id"));
				double lat = cursor
						.getDouble(cursor.getColumnIndex("latitude"));
				double lon = cursor.getDouble(cursor
						.getColumnIndex("longitude"));
				String name = cursor.getString(cursor.getColumnIndex("name")); // due
																				// to
																				// duplicate
																				// values,
																				// may
																				// need
																				// to
																				// assign
																				// table
																				// aliases
																				// in
																				// query
				Timestamp datetime = Timestamp.valueOf(cursor.getString(cursor
						.getColumnIndex("dtime")));
				String description = cursor.getString(cursor
						.getColumnIndex("description"));
				Timestamp updateTime = Timestamp.valueOf(cursor
						.getString(cursor.getColumnIndex("UPDATETIME")));
				String flag = cursor.getString(cursor.getColumnIndex("flag"));

				Event e = new Event(eId, lat, lon, datetime, name, description,
						tId, updateTime, flag);
				list.add(e);
			}
			cursor.close();
		}
		return list;
	}

	public long insertEvent(Event e) {
		Log.i(TAG,
				e.getPoiId() + "," + e.getTripId() + "," + e.getLatitude()
						+ "," + e.getLongitude() + "," + e.getDtime() + ","
						+ e.getName() + "," + e.getDescription() + ", "
						+ e.getUpdatetime() + ", " + e.getFlag());
		ContentValues eventValues = new ContentValues();
		eventValues.put("event_id", e.getPoiId());
		eventValues.put("trip_id", e.getTripId());
		eventValues.put("longitude", e.getLongitude());
		eventValues.put("latitude", e.getLatitude());
		eventValues.put("dtime", e.getDtime() + "");
		eventValues.put("name", e.getName());
		eventValues.put("description", e.getDescription());
		eventValues.put("UPDATETIME", e.getUpdatetime() + "");
		eventValues.put("flag", e.getFlag());
		return mDb.insertOrThrow("event", null, eventValues);
	}

	public Collection<Media> getNewMediaByUser(String username) {
		Cursor cursor = null;
		Media m = null;
		Collection<Media> list = new ArrayList<Media>();
		cursor = mDb
				.rawQuery(
						"select m.media_id, m.event_id, m.name, m.description, m.type, m.blob, m.UPDATETIME, m.flag from account_trip ti "
								+ "inner join trip t on ti.trip_id = t.trip_id "
								+ "inner join event e on t.trip_id = e.trip_id "
								+ "inner join media m on e.event_id = m.event_id "
								+ "where m.flag = 'n' "
								+ "and ti.username = '"
								+ username + "'", null);
		if (cursor != null) {
			if (cursor.moveToFirst()) {
				int mId = cursor.getInt(cursor.getColumnIndex("media_id"));
				int eId = cursor.getInt(cursor.getColumnIndex("event_id"));
				String name = cursor.getString(cursor.getColumnIndex("name"));
				String description = cursor.getString(cursor
						.getColumnIndex("description"));
				String type = cursor.getString(cursor.getColumnIndex("type"));
				byte[] blob = cursor.getBlob(cursor.getColumnIndex("blob"));
				Timestamp updateTime = Timestamp.valueOf(cursor
						.getString(cursor.getColumnIndex("UPDATETIME")));
				String flag = cursor.getString(cursor.getColumnIndex("flag"));

				m = new Media(mId, name, description, type, blob, eId,
						updateTime, flag);
				list.add(m);
				// byte[] image =
				// cursor.getBlob(cursor.getColumnIndex(BanksTable.COL_IMAGE));
			}
			cursor.close();
		}
		return list;
	}

	public long insertMedia(Media m) {
		Log.w(TAG,
				m.getMediaId() + "," + m.getPoiId() + "," + m.getName() + ","
						+ m.getDescription() + "," + m.getType() + ","
						+ m.getBlob() + ", " + m.getUpdatetime() + ", "
						+ m.getFlag());
		ContentValues mediaValues = new ContentValues();
		mediaValues.put("media_id", m.getMediaId());
		mediaValues.put("event_id", m.getPoiId());
		mediaValues.put("name", m.getName());
		mediaValues.put("description", m.getDescription());
		mediaValues.put("type", m.getType());
		mediaValues.put("blob", m.getBlob());
		mediaValues.put("UPDATETIME", m.getUpdatetime() + "");
		mediaValues.put("flag", m.getFlag());
		return mDb.insertOrThrow("media", null, mediaValues);
	}

	public long insertCoord(Coordinate c) {
		Log.w(TAG, c.getTripId() + "," + c.getLat() + "," + c.getLongi() + ","
				+ c.getDatetime());
		ContentValues coordinateValues = new ContentValues();

		coordinateValues.put("trip_id", c.getTripId());
		coordinateValues.put("latitude", c.getLat());
		coordinateValues.put("longitude", c.getLongi());
		coordinateValues.put("flag", c.getFlag());
		coordinateValues.put("datetime", c.getDatetime() + "");
		return mDb.insertOrThrow("coordinates", null, coordinateValues);

	}

	/**
	 * Update the coordinate in the client database such that it's flag says
	 * it's been synced. Can't think of an appropriate query nor can I remember
	 * the exact name of the table :/
	 * 
	 * @param c
	 * @return
	 */
	public int updateCoord(Coordinate c) {
		ContentValues values = new ContentValues();
		// values.put("updatetime", String.valueOf(c.getUpdatetime()));
		values.put("flag", c.getFlag());

		return mDb.update("coordinate", values, "trip_id" + "= ?", new String[] { String.valueOf(c.getTripId()) }); 
	}

	public Collection<Coordinate> getCoordinatesByFlagByUser(String username,
			String flag) {
		Cursor cursor = null;
		ArrayList<Coordinate> tripCoordinates = new ArrayList<Coordinate>();

		cursor = mDb
				.rawQuery(
						"select c.trip_id, c.datetime, c.latitude, c.longitude, c.flag from account_trip ti "
								+ "inner join trip t on ti.trip_id = t.trip_id "
								+ "inner join coordinate c on t.trip_id = c.trip_id "
								+ "where c.flag = 'n' "
								+ "and ti.username = '"
								+ username + "'", null);

		int iRow = cursor.getColumnIndex("trip_id");
		int iDateTime = cursor.getColumnIndex("datetime");
		int iLatitude = cursor.getColumnIndex("latitude");
		int iLongitude = cursor.getColumnIndex("longitude");
		int iFlag = cursor.getColumnIndex("flag");
		Coordinate c = null;

		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			c = new Coordinate(cursor.getDouble(iLatitude),
					cursor.getDouble(iLongitude), Timestamp.valueOf(cursor
							.getString(iDateTime)), cursor.getInt(iRow),
					cursor.getString(iFlag));
			Log.e(TAG, "Coordinate retrieved =" + c.toString());
			tripCoordinates.add(c);
		}

		Log.e(TAG,
				"Contents of all coordinates for a Trip: "
						+ tripCoordinates.toString());
		cursor.close();
		
		
		return tripCoordinates;
	}

	public Collection<Tag> getAllTagsByFlagByUser(String username, String flag) {
		Cursor cursor = null;
		ArrayList<Tag> dbTags = new ArrayList<Tag>();

		// NOTE: NOT SURE ABOUT THIS QUERY :/
		cursor = mDb
				.rawQuery(
						"select ttag.text, ttag.trip_id, ttag.UPDATETIME, ttag.flag from account_trip ti "
								+ "inner join trip t on ti.trip_id = t.trip_id "
								+ "inner join trip_tag ttag on t.trip_id = ttag.trip_id "
								+ "where ttag.flag = 'n' "
								+ "and ti.username = '" + username + "'", null);

		int iRow = cursor.getColumnIndex("text");
		int iTripId = cursor.getColumnIndex("trip_id");
		int iUpdateTime = cursor.getColumnIndex("UPDATETIME");
		int iFlag = cursor.getColumnIndex("flag");
		Tag t = null;

		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			t = new Tag(cursor.getInt(iTripId), cursor.getString(iRow), "trip",
					Timestamp.valueOf(cursor.getString(iUpdateTime)),
					cursor.getString(iFlag));
			Log.e(TAG, "Tag retrieved =" + t.toString());
			dbTags.add(t);
		}

		cursor = mDb
				.rawQuery(
						"select etag.text, etag.event_id, etag.UPDATETIME, etag.flag from account_trip ti "
								+ "inner join trip t on ti.trip_id = t.trip_id "
								+ "inner join event e on t.trip_id = e.trip_id "
								+ "inner join event_tag etag on e.event_id = etag.event_id "
								+ "where etag.flag = 'n' "
								+ "and ti.username = '" + username + "'", null);

		iRow = cursor.getColumnIndex("text");
		iTripId = cursor.getColumnIndex("event_id");
		iUpdateTime = cursor.getColumnIndex("UPDATETIME");
		iFlag = cursor.getColumnIndex("flag");
		t = null;

		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			t = new Tag(cursor.getInt(iTripId), cursor.getString(iRow),
					"event", Timestamp.valueOf(cursor.getString(iUpdateTime)),
					cursor.getString(iFlag));
			Log.e(TAG, "Tag retrieved =" + t.toString());
			dbTags.add(t);
		}

		cursor = mDb
				.rawQuery(
						"select mtag.text, mtag.media_id, mtag.UPDATETIME, mtag.flag from account_trip ti "
								+ "inner join trip t on ti.trip_id = t.trip_id "
								+ "inner join event e on t.trip_id = e.trip_id "
								+ "inner join media m on e.event_id = m.event_id "
								+ "inner join media_tag mtag on m.media_id = mtag.media_id "
								+ "where mtag.flag = 'n' "
								+ "and ti.username = '" + username + "'", null);

		iRow = cursor.getColumnIndex("text");
		iTripId = cursor.getColumnIndex("media_id");
		iUpdateTime = cursor.getColumnIndex("UPDATETIME");
		iFlag = cursor.getColumnIndex("flag");
		t = null;

		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			t = new Tag(cursor.getInt(iTripId), cursor.getString(iRow),
					"Event", Timestamp.valueOf(cursor.getString(iUpdateTime)),
					cursor.getString(iFlag));
			Log.e(TAG, "Tag retrieved =" + t.toString());
			dbTags.add(t);
		}

		Log.e(TAG, "Contents of all tags: " + dbTags.toString());
		cursor.close();
		return dbTags;
	}

}
