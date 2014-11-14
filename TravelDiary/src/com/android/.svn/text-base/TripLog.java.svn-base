/**
 * The purpose of this program is to present a Log that displays all trips that
 * a user has recorded and allows them to edit and display them
 * 
 * @peerReview
 * @date 19/07/12
 * @name TripLog
 * @author Mostafa Alwash
 */

package com.android;

import java.util.ArrayList;

import sync.Syncer;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import domain.Trip;

public class TripLog extends Activity {

	private static final String TAG = "TripLog";

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.trip_log);

		TableLayout tLayout = (TableLayout) findViewById(R.id.table_trip_search);
		// Button searchButton = (Button) findViewById(R.id.btn_trip_search);
		// EditText searchText = (EditText) findViewById(R.id.txt_trip_search);

		final DBAdapter db = new DBAdapter(this);
		final Syncer sync = new Syncer(db);

		db.open();
		final ArrayList<Trip> resultList = db.getTrips();
		for (Trip t : resultList) {
			Log.i(TAG, t.toString());
		}

		db.close();

		for (int i = 0; i < resultList.size(); i++) {
			TableRow tRow = new TableRow(this);
			tRow.setId(resultList.get(i).getTripId());
			CheckBox tripCheck = new CheckBox(this);
			TextView tripTitle = new TextView(this);
			tripTitle.setText(resultList.get(i).getName());
			final Button viewButton = new Button(this);
			viewButton.setText("View");
			viewButton.setId(resultList.get(i).getTripId());
			Button editButton = new Button(this);
			editButton.setText("Edit");
			Button deleteButton = new Button(this);
			deleteButton.setText("Delete");

			/* Listening to view button */
			viewButton.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {

					int clickedTripId = viewButton.getId(); // Will pull from
															// View

					Intent intent = new Intent(getApplicationContext(),
							DisplayMapActivity.class);
					intent.putExtra("trip", clickedTripId);
					startActivity(intent);

					/*
					 * DBAdapter clickedDb = new DBAdapter(v.getContext());
					 * clickedDb.open(); ArrayList<Coordinate>
					 * resultCoordinateList =
					 * clickedDb.getCoordinates(clickedTripId); //Grab all
					 * coordinates related to trip clickedDb.close();
					 * 
					 * String addonCoordinates = ""; //KML Tags data for points
					 * in between start and end address String saddr =""; //KML
					 * Tags data for starting address String daddr =""; //KML
					 * Tags data for ending address
					 * 
					 * for(int i = 0; i < resultCoordinateList.size(); i++){
					 * if(i == 0){ // if it is the starting coordinate saddr =
					 * resultCoordinateList
					 * .get(i).getLat()+","+resultCoordinateList
					 * .get(i).getLongi(); } else if (i ==
					 * resultCoordinateList.size()-1){ //if it is the ending
					 * coordinate daddr =
					 * resultCoordinateList.get(i).getLat()+","
					 * +resultCoordinateList.get(i).getLongi(); } else { // if
					 * it is a coordinate inbetween start and end
					 * addonCoordinates +=
					 * "+to:"+resultCoordinateList.get(i).getLat
					 * ()+","+resultCoordinateList.get(i).getLongi(); } }
					 * 
					 * Intent intent = new
					 * Intent(android.content.Intent.ACTION_VIEW,
					 * Uri.parse("http://maps.google.com/maps?saddr="
					 * +saddr+"&daddr="+daddr+addonCoordinates));
					 * startActivity(intent);
					 */
				}
			});

			/* Listening to edit button */
			editButton.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					int clickedTripId = 0; // Will pull from View

					db.open();
					Trip t = db.getTripById(clickedTripId);
					Log.e(TAG, "Trip 2 be sent :" + t.toString());
					db.close();

					// int result = sync.uploadTrip(t.getTripId(),
					// t.getTripName(), t.getTripDescription(),
					// t.getUpdateTime(), "Alwmo");
					// Log.e(TAG, String.valueOf(result));
				}
			});

			tRow.addView(tripCheck);
			tRow.addView(tripTitle);
			tRow.addView(viewButton);
			tRow.addView(editButton);
			tRow.addView(deleteButton);

			tLayout.addView(tRow);
		}

		/* Listening to search button */
		/*
		 * searchButton.setOnClickListener(new View.OnClickListener() { public
		 * void onClick(View v) {
		 * 
		 * 
		 * } });
		 */

	}
}
