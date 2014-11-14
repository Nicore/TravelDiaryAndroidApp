package sync;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;

import android.content.Context;
import android.database.SQLException;
import android.util.Log;

import com.android.DBAdapter;

import domain.AccountTrip;
import domain.Coordinate;
import domain.Event;
import domain.Media;
import domain.Tag;
import domain.Trip;

/**
 * This class is going to be used to coordinate all the sync calls. This way in
 * the mainmapactivity you can just call this class and the methods.
 * 
 * @author jonda741 flags: n = new, d = delete, e = edited, s = sync
 */
public class SyncHelper {
	private String username;
	private DBAdapter dba;
	private Context ctx;
	private SyncerTrip syncerTrip;
	private SyncerEvent syncerEvent;
	private SyncerMedia syncerMedia;
	private SyncerTag syncerTag;
	private SyncerCoord syncerCoord;
	private SyncerAccountTrip syncerAccountTrip;
	private final String TAG = "Sync Helper";
	private static final String PREFS_NAME = "MyPrefsFile";

	public SyncHelper(String username, Context ctx) {

		this.username = username;
		this.ctx = ctx;
		this.dba = new DBAdapter(ctx);
		this.syncerTrip = new SyncerTrip(this.dba);
		this.syncerEvent = new SyncerEvent(this.dba);
		this.syncerMedia = new SyncerMedia(this.dba);
		this.syncerTag = new SyncerTag(this.dba);
		this.syncerCoord = new SyncerCoord(this.dba);
		this.syncerAccountTrip = new SyncerAccountTrip(this.dba);
	}

	/**
	 * This method is just going to call each method in appropriate order.
	 */
	public void doSync() {

		//getDelMedias();
		//getDelEvents();
		//getDelTrips();

//		sendDelMedias();
//		sendDelEvents();
//		sendDelTrips();

		getNewTrips();
		sendNewTrips();
		getNewEvents();
		sendNewEvents();
		getNewCoordinates();
		sendNewCoordinates();
		getNewMedias();
		sendNewMedias();

		getNewAccountTrips();
		sendNewAccountTrips();
		// getNewTags();
		// sendNewTags();
	}

	public void getNewCoordinates() {
		Collection<Coordinate> coordinates = new ArrayList<Coordinate>();

		coordinates = syncerCoord.downloadNewCoords(username);

		Log.i("Sync Helper", coordinates.toString());

		for (Coordinate c : coordinates) {
			c.setFlag("s"); // g for good?
			try {
				dba.open();
				try {
					dba.insertCoord(c);
				} catch (SQLException e) {
					Log.e(TAG, e.toString());
				}
			} catch (SQLException e) {
				Log.e(TAG, e.toString());
			} finally {
				dba.close();
			}
		}
	}
	
	public void sendNewCoordinates() {
		dba.open();
		Collection<Coordinate> coordinates = new ArrayList<Coordinate>();
		coordinates = dba.getCoordinatesByFlagByUser(username, "n");

		for (Coordinate c : coordinates) {
			String newUpdateTime = syncerCoord.uploadNewCoord(c);
			c.setFlag("s");
			c.setDatetime(Timestamp.valueOf(newUpdateTime));

			dba.updateCoord(c);
		}

		dba.close();
	}
	/**
	 * This method here will iterate through each trip in local database with
	 * the new flag ('n') and send each of them to the servlet, then get back
	 * the updated trip and update the local DB's version.
	 */
	public void sendNewTrips() {
		dba.open();
		Collection<Trip> trips = new ArrayList<Trip>();
		trips = dba.getTripsByFlagByUser(username, "n");

		for (Trip t : trips) {
			String newUpdateTime = syncerTrip.uploadNewTrip(t);
			t.setFlag("s");
			t.setUpdatetime(Timestamp.valueOf(newUpdateTime));
			dba.updateTrip(t);
		}

		dba.close();
	}

	/**
	 * This method here will iterate through each account_trip in local database
	 * with the new flag ('n') and send each of them to the servlet, then get
	 * back the updated account_trip and update the local DB's version.
	 */
	public void sendNewAccountTrips() {
		dba.open();
		Collection<AccountTrip> accountTrips = new ArrayList<AccountTrip>();
		accountTrips = dba.getAccountTripsByFlag("n");

		for (AccountTrip t : accountTrips) {
			String newUpdateTime = syncerAccountTrip.uploadNewAccountTrip(t);
			t.setFlag("s");
			// t.setUpdatetime(Timestamp.valueOf(newUpdateTime));
			dba.updateAccountTrip(t);
		}

		dba.close();
	}

	/**
	 * This method is going to send a message to the database asking for all the
	 * account trips with the new flag ('n') that are associated to the user. It
	 * will recieve a collectin of accountTrips to be put iterated through and
	 * put into the database.
	 */
	public void getNewAccountTrips() {
		Collection<AccountTrip> accountTrips = new ArrayList<AccountTrip>();

		Log.i("Sync Helper", accountTrips.toString());
		accountTrips = syncerAccountTrip.downloadNewAccountTrips(username);

		Log.i("Sync Helper", accountTrips.toString());

		for (AccountTrip t : accountTrips) {
			t.setFlag("s"); // g for good?
			try {
				dba.open();
				try {
					dba.insertAccountTrip(t.getUsername(), t.getTripId(),
							t.getPermissions(), t.getFlag());
				} catch (SQLException e) {

				}
			} catch (SQLException e) {

			} finally {
				dba.close();
			}
		}
	}

	/**
	 * This method here will iterate through each trip in local database with
	 * the new flag ('n') and send each of them to the servlet, then get back
	 * the updated trip and update the local DB's version.
	 */
	public void sendDelTrips() {
		dba.open();
		Collection<Trip> trips = new ArrayList<Trip>();

		trips = dba.getTripsByFlagByUser(username, "d");

		for (Trip t : trips) {
			String newUpdateTime = syncerTrip.uploadDelTrip(t.getTripId());
			t.setUpdatetime(Timestamp.valueOf(newUpdateTime));
			dba.updateTrip(t);
		}

		dba.close();
	}

	/**
	 * This method is going to send a message to the database asking for all the
	 * trips with the new flag ('n') that are associated to the user. It will
	 * recieve a collectin of trips to be put iterated through and put into the
	 * database.
	 */
	public void getNewTrips() {
		Collection<Trip> trips = new ArrayList<Trip>();

		Log.i("Sync Helper", trips.toString());
		trips = syncerTrip.downloadNewTrips(username);

		Log.i("Sync Helper", trips.toString());

		for (Trip t : trips) {
			t.setFlag("s"); // g for good?
			try {
				dba.open();
				try {
					dba.insertTrip(t);
				} catch (SQLException e) {

				}
			} catch (SQLException e) {

			} finally {
				dba.close();
			}
		}
	}

	/**
	 * This method is going to send a message to the database asking for all the
	 * trips with the delete flag ('d') that are associated to the user. It will
	 * recieve a collectin of trips to be put iterated through and deleted from
	 * the database.
	 */
	public void getDelTrips() {
		Collection<Trip> trips = new ArrayList<Trip>();

		Log.i("Sync Helper", trips.toString());
		trips = syncerTrip.downloadDelTrips(username);

		Log.i("Sync Helper", trips.toString());

		for (Trip t : trips) {
			try {
				dba.open();
				try {
					// dba.insertTrip(t);
					// this method should fully delete the trip from the client
					// DB
					dba.deleteTrip(t.getTripId());
				} catch (SQLException e) {
				}
			} catch (SQLException e) {
			} finally {
				dba.close();
			}
		}
	}

	public void sendNewEvents() {
		dba.open();
		//Collection<Event> eEvents = new ArrayList<Event>();
		Collection<Event> eEvents = dba.getEventsByFlagByUser(username, "n");
		//eEvents = dba.getEventsByFlagByUser(username, "n"); // need one for
															
		Log.i(TAG, "Events: " + eEvents.toString());

		for (Event e : eEvents) {
			String newUpdateTime = syncerEvent.uploadNewEvent(e);
			e.setFlag("s");
			e.setUpdatetime(Timestamp.valueOf(newUpdateTime));
			dba.updateEvent(e);
		}

		dba.close();
	}

	public void sendDelEvents() {
		dba.open();
		Collection<Event> events = new ArrayList<Event>();
		events = dba.getEventsByFlagByUser(username, "n"); // need one for
															// events

		if (events.size() > 0) {

			for (Event e : events) {
				String newUpdateTime = syncerEvent.uploadDelEvent(e.getPoiId());
				// e.setUpdatetime(Timestamp.valueOf(newUpdateTime));
				dba.deleteEvent(e.getPoiId());
			}
		}

		dba.close();
	}

	public void getNewEvents() {
		Collection<Event> events = new ArrayList<Event>();

		Log.i("Sync Helper", events.toString());
		events = syncerEvent.downloadNewEvents(username);

		Log.i("Sync Helper", events.toString());

		dba.open();
		for (Event e : events) {
			e.setFlag("s"); // g for good?
			try {
				
				dba.insertEvent(e);
			} catch (SQLException s) {
				Log.e(TAG, e.toString());
			
			}
			
		}
		dba.close();
	}

	public void getDelEvents() {
		Collection<Event> events = new ArrayList<Event>();

		Log.i("Sync Helper", events.toString());
		events = syncerEvent.downloadDelEvents(username);

		Log.i("Sync Helper", events.toString());

		dba.open();
		for (Event e : events) {
			// e.setFlag("s"); //g for good?
			try {
				dba.deleteEvent(e.getPoiId());
			} catch (SQLException s) {

			} finally {

			}
		}
		dba.close();
	}

	/**
	 * 
	 */
	public void sendNewMedias() {
		dba.open();
		Collection<Media> medias = new ArrayList<Media>();
		medias = dba.getMediasByFlagByUser(username, "n"); // need one for
															// events
		Log.e(TAG, "GetNewMediaBy User: " + medias.toString());

		for (Media m : medias) {
			String newUpdateTime = syncerMedia.uploadNewMedia(m);
			m.setFlag("s");
			m.setUpdatetime(Timestamp.valueOf(newUpdateTime));
			dba.updateMedia(m);
		}
		dba.close();
	}

	public void sendDelMedias() {
		dba.open();
		Collection<Media> medias = new ArrayList<Media>();
		medias = dba.getMediasByFlagByUser(username, "d");
		Log.e(TAG, "GetNewMediaBy User: " + medias.toString());

		for (Media m : medias) {
			String newUpdateTime = syncerMedia.uploadDelMedia(m.getMediaId());
			m.setUpdatetime(Timestamp.valueOf(newUpdateTime));
			dba.updateMedia(m);
		}
		dba.close();
	}

	/* Getting from server to client */
	public void getNewMedias() {
		Collection<Media> medias = new ArrayList<Media>();

		Log.i("Sync Helper", medias.toString());
		medias = syncerMedia.downloadNewMedias(username);

		Log.i("Sync Helper", medias.toString());

		for (Media m : medias) {
			m.setFlag("s"); // g for good?
			try {
				dba.open();
				dba.insertMedia(m);
			} catch (SQLException e) {

			} finally {
				dba.close();
			}
		}
	}

	public void getDelMedias() {
		Collection<Media> medias = new ArrayList<Media>();

		Log.i("Sync Helper", medias.toString());
		medias = syncerMedia.downloadDelMedias(username);

		Log.i("Sync Helper", medias.toString());

		dba.open();
		for (Media m : medias) {
			// m.setFlag("s"); //g for good?
			try {
				dba.deleteMedia(m.getMediaId());
			} catch (SQLException e) {
				Log.e(TAG, "SQL exception in getDelMedias");
			} finally {

			}
		}
		dba.close();
	}

	/**
	 * This method here will iterate through each tag table in local database
	 * with the new flag ('n') and send each of them to the servlet, then get
	 * back the updated tags and update the local DB's version.
	 */
	public void sendNewTags() {
		dba.open();
		Collection<Tag> tags = new ArrayList<Tag>();
		tags.addAll(dba.getNewTripTagsByUser(username));
		tags.addAll(dba.getNewEventTagsByUser(username));
		tags.addAll(dba.getNewMediaTagsByUser(username));

		for (Tag t : tags) {
			String newUpdateTime = syncerTag.uploadNewTag(t);
			t.setFlag("s");
			t.setUpdatetime(Timestamp.valueOf(newUpdateTime));
			String type = t.getType();
			dba.updateTag(t, type);
		}

		dba.close();
	}

	/**
	 * This method is going to send a message to the database asking for all the
	 * tags with the new flag ('n') that are associated to the user. It will
	 * recieve a collectin of tags to be put iterated through and put into the
	 * database.
	 */
	public void getNewTags() {
		Collection<Tag> tags = new ArrayList<Tag>();

		Log.i("Sync Helper", tags.toString());
		tags = syncerTag.downloadNewTags(username, "trip");
		tags.addAll(syncerTag.downloadNewTags(username, "event"));
		tags.addAll(syncerTag.downloadNewTags(username, "media"));

		Log.i("Sync Helper", tags.toString());

		for (Tag t : tags) {
			t.setFlag("s"); // g for good?
			try {
				dba.open();
				try {
					dba.insertTags(t.getText(), t.getTagId(), t.getType(), t
							.getUpdatetime().toString(), t.getFlag());
				} catch (SQLException e) {

				}
			} catch (SQLException e) {

			} finally {
				dba.close();
			}
		}

	}

	public void sendNewCoords() {
		dba.open();
		Collection<Coordinate> coords = new ArrayList<Coordinate>();
		coords = dba.getCoordinatesByFlagByUser(username, "n"); // going to be
																// one of those
																// joins where
																// trip_id =
																// trip_id for
																// trip and
																// coordinates
		for (Coordinate c : coords) {
			String newUpdateTime = syncerCoord.uploadNewCoord(c);
			c.setFlag("s");
			// c.setUpdatetime(Timestamp.valueOf(newUpdateTime)); //Not used for
			// coordinates since they don't change ever.

			dba.updateCoord(c);
		}

		dba.close();
	}

	public void getNewCoords() {
		Collection<Coordinate> coords = new ArrayList<Coordinate>();

		Log.i("Sync Helper", coords.toString());
		coords = syncerCoord.downloadNewCoords(username);

		Log.i("Sync Helper", coords.toString());

		for (Coordinate c : coords) {
			c.setFlag("s"); // g for good?
			try {
				dba.open();
				dba.insertCoord(c);
			} catch (SQLException s) {

			} finally {
				dba.close();
			}
		}

	}

}
