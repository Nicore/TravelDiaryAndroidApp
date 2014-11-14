/**
 * The purpose of this program is to present the main Activity
 * screen which the user will interact with. it provides clickable
 * buttons which are linked to their associated functionality
 * 
 * @peerReview
 * @date 22/05/12
 * @name MainMapActivity
 * @author Mostafa Alwash, June Cui, Daniel Jonker, Lu Zhao
 */
package com.android;

import java.io.*;
import java.sql.Timestamp;
import java.util.*;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.location.*;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.*;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;

import com.google.android.maps.*;

import domain.Coordinate;
import domain.Event;
import domain.Media;
import domain.Trip;

public class DisplayMapActivity extends MapActivity {
	/* Initially sets up the required Classes */

	private MapController mapController;
	private MapView myMapView;
	private MyItemizedOverlay coordinateitemizedoverlay;
	private MyItemizedOverlay eventitemizedoverlay;
	private List<Overlay> mapOverlays;

	private static final String TAG = "DisplayMapActivity";
	private static final String PREFS_NAME = "MyPrefsFile";

	/* isRouteDisplayed() is required for all MapActivity */
	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

	/**
	 * This method binds the GUI components from the XML file manipulative
	 * objects which are clickable. An overlay is then layered over the MapView.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.displaytripmap);

		myMapView = (MapView) findViewById(R.id.displaymapview);
		myMapView.setBuiltInZoomControls(true);
		mapController = myMapView.getController();
		mapController.setZoom(14); // Zoom 1 is world view

		// Get all the overlays on the map view
		mapOverlays = myMapView.getOverlays();
		// create a marker
		Drawable coordinatedrawable = this.getResources().getDrawable(
				R.drawable.point_coordinate);
		// initialise a ItemizedOverlay object
		coordinateitemizedoverlay = new MyItemizedOverlay(coordinatedrawable,
				this);

		// create a marker
		Drawable eventdrawable = this.getResources().getDrawable(
				R.drawable.point);
		// initialise a ItemizedOverlay object
		eventitemizedoverlay = new MyItemizedOverlay(eventdrawable, this);

		if (mapOverlays != null) {
			mapOverlays.add(coordinateitemizedoverlay);
			mapOverlays.add(eventitemizedoverlay);
		}

		try {
			Bundle extras = getIntent().getExtras();
			int clickedTripId = extras.getInt("trip");
			Log.e(TAG, "ClickedTrip =" + clickedTripId);

			DBAdapter db = new DBAdapter(this);

			db.open();
			ArrayList<Coordinate> resultCoordinateList = db
					.getCoordinates(clickedTripId); // Grab all coordinates
													// related to trip
			ArrayList<Event> resultEventList = db.getEvents(clickedTripId); // Grab
																			// all
																			// coordinates
																			// related
																			// to
																			// trip
			db.close();

			for (int i = 0; i < resultCoordinateList.size(); i++) {
				GeoPoint myPoint = new GeoPoint((int) (resultCoordinateList
						.get(i).getLat() * 1E6), (int) (resultCoordinateList
						.get(i).getLongi() * 1E6));
				OverlayItem overlayitem = new OverlayItem(myPoint,
						"Coordinate " + i, "Was here");
				coordinateitemizedoverlay.addOverlay(overlayitem);
				if (i == 0) {
					mapController.animateTo(myPoint);
				}
			}

			for (int i = 0; i < resultEventList.size(); i++) {
				GeoPoint myPoint = new GeoPoint((int) (resultEventList.get(i)
						.getLatitude() * 1E6), (int) (resultEventList.get(i)
						.getLongitude() * 1E6));
				OverlayItem overlayitem = new OverlayItem(myPoint,
						String.valueOf(resultEventList.get(i).getPoiId()),
						resultEventList.get(i).getName());
				eventitemizedoverlay.addOverlay(overlayitem);

			}
		} catch (Exception e) {
			Toast.makeText(this, "Trip contains a problem", Toast.LENGTH_SHORT)
					.show();
		}
	}

	/**
	 * The purpose of this method handle a captured image after the shutter is
	 * taken
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case (0): { // Capturing a photo

			break;
		}
		case (1): { // Method for handling adding points to a map

		}
			break;
		}
	}
}