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

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import sync.SyncHelper;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.provider.Settings.Secure;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.telephony.TelephonyManager;
import android.content.Context;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

import domain.Event;
import domain.Friend;

public class MainMapActivity extends MapActivity {
	/* Initially sets up the required Classes */

	private MapController mapController;
	private MapView myMapView;
	private LocationManager locationManager;
	private MyItemizedOverlay itemizedoverlay;
	private MyItemizedOverlay restrauantsitemizedoverlay;
	private MyItemizedOverlay banksitemizedoverlay;
	private MyItemizedOverlay cafeitemizedoverlay;
	private MyItemizedOverlay motelsitemizedoverlay;
	private MyItemizedOverlay servicestationsitemizedoverlay;
	private MyItemizedOverlay supermarketsitemizedoverlay;
	private List<Overlay> mapOverlays;
	private String myProvider;
	private File output;
	private TelephonyManager telephonyManager;

	protected String _path;
	protected boolean _taken;

	private static final String TAG = "MainMapActivity";
	private static final String PREFS_NAME = "MyPrefsFile";

	protected static final String PHOTO_TAKEN = "photo_taken";
	private Location myLocation;

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
		setContentView(R.layout.mainmap);

		// Assign all gui components to a variable
		TextView camButton = (TextView) findViewById(R.id.btnCamera);
		TextView micButton = (TextView) findViewById(R.id.btnMic);
		TextView noteButton = (TextView) findViewById(R.id.btnNote);
		TextView looseMedia = (TextView) findViewById(R.id.btnLooseMedia);
		final TextView trip = (TextView) findViewById(R.id.btnStartTrip);
		TextView tripLogButton = (TextView) findViewById(R.id.btnTripLog);
		final TextView eventButton = (TextView) findViewById(R.id.btnEvent);

		myMapView = (MapView) findViewById(R.id.mapview);
		myMapView.setBuiltInZoomControls(true);
		mapController = myMapView.getController();
		mapController.setZoom(14); // Zoom 1 is world view

		// Get all the overlays on the map view
		mapOverlays = myMapView.getOverlays();
		// create a marker
		Drawable drawable = this.getResources().getDrawable(
				R.drawable.point_user);
		// initialise a ItemizedOverlay object
		itemizedoverlay = new MyItemizedOverlay(drawable, this);

		// Assign images to different categories of sites
		Drawable restrauntdrawable = this.getResources().getDrawable(
				R.drawable.point_restaurant);
		restrauantsitemizedoverlay = new MyItemizedOverlay(restrauntdrawable,
				this);
		Drawable bankdrawable = this.getResources().getDrawable(
				R.drawable.point_bank);
		banksitemizedoverlay = new MyItemizedOverlay(bankdrawable, this);
		Drawable cafedrawable = this.getResources().getDrawable(
				R.drawable.point_cafe);
		cafeitemizedoverlay = new MyItemizedOverlay(cafedrawable, this);
		Drawable moteldrawable = this.getResources().getDrawable(
				R.drawable.point_motel);
		motelsitemizedoverlay = new MyItemizedOverlay(moteldrawable, this);
		Drawable servicestationdrawable = this.getResources().getDrawable(
				R.drawable.point_servicestation);
		servicestationsitemizedoverlay = new MyItemizedOverlay(
				servicestationdrawable, this);
		Drawable supermarketsdrawable = this.getResources().getDrawable(
				R.drawable.point_supermarket);
		supermarketsitemizedoverlay = new MyItemizedOverlay(
				supermarketsdrawable, this);

		// add hard coded coordinates to map
		/*
		 * String coordinates[] = {"-45.875300,170.502697,Kate,Princes Street",
		 * "-45.873118,170.354739,Kevin,High Street"}; for (int i = 0; i <
		 * coordinates.length; i++ ) { double lat =
		 * Double.parseDouble(coordinates[i].split(",")[0]); double lng =
		 * Double.parseDouble(coordinates[i].split(",")[1]); GeoPoint myPoint =
		 * new GeoPoint((int) (lat * 1E6), (int) (lng * 1E6)); OverlayItem
		 * overlayitem = new OverlayItem(myPoint, coordinates[i].split(",")[2],
		 * coordinates[i].split(",")[3]);
		 * itemizedoverlay.addOverlay(overlayitem); };
		 */

		// Use the location manager to get user's location and add my location
		// on the map
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		myProvider = locationManager.getBestProvider(new Criteria(), false);
		myLocation = locationManager.getLastKnownLocation(myProvider);

		// if my location is trackable then plot it on the Map
		if (myLocation != null) {
			double myLat = myLocation.getLatitude();
			double myLng = myLocation.getLongitude();
			GeoPoint myPoint = new GeoPoint((int) (myLat * 1E6),
					(int) (myLng * 1E6));
			OverlayItem overlayitem = new OverlayItem(myPoint, "Me",
					"Starts here");
			itemizedoverlay.addOverlay(overlayitem);
			mapController.animateTo(myPoint);
		} else {
			Toast toast = Toast.makeText(this, "No Location",
					Toast.LENGTH_SHORT);
			toast.show();
		}

		if (mapOverlays != null) {
			mapOverlays.add(itemizedoverlay);

		}

		/* Create a new locationListener to plot my movement */
		LocationListener locationListener = new LocationListener() {

			public void onLocationChanged(Location newLocation) {
				double myLat = newLocation.getLatitude();
				double myLng = newLocation.getLongitude();
				GeoPoint myPoint = new GeoPoint((int) (myLat * 1E6),
						(int) (myLng * 1E6));
				OverlayItem overlayitem = new OverlayItem(myPoint,
						"Current Coordinate", myLng + ", " + myLat);
				// Drawable drawable =
				// getResources().getDrawable(R.drawable.point_move);
				// overlayitem.setMarker(drawable);
				mapOverlays.clear();
				itemizedoverlay.addOverlay(overlayitem);
				mapOverlays.add(itemizedoverlay);

				// Adding to the coordinate Table
				DBAdapter db = new DBAdapter(getApplicationContext());

				SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
				SharedPreferences.Editor editor = settings.edit();
				boolean isTrip = settings.getBoolean("tripMode", false);
				editor.putString("eventLongitude",
						String.valueOf(newLocation.getLongitude()));
				editor.putString("eventLatitude",
						String.valueOf(newLocation.getLatitude()));
				editor.commit();

				Log.e(TAG, "istrip = " + isTrip);
				db.open();
				if (isTrip) {

					// get Latest Trip
					int latestTrip = settings.getInt("tripId", 0);
					String timestamp = new Timestamp(new Date().getTime())
							.toString();
					// Toast toast = Toast.makeText(getApplicationContext(),
					// "New Coordinate Saved {"+myLng+","+myLat+"}",
					// Toast.LENGTH_SHORT);
					// toast.show();
					db.insertCoordinates(latestTrip, myLat, myLng, timestamp,
							"n");

				}
				db.close();
			}

			// Non applicable neccessary methods
			public void onProviderDisabled(String arg0) {
				Log.w(TAG, "onProviderDisabled");
				startActivity(new Intent(
						android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
			}

			public void onProviderEnabled(String arg0) {
				Log.w(TAG, "onProviderEnabled");
			}

			public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
				Log.w(TAG, "onStatusChanged");
			}
		};
		/*
		 * Request the locationManager to update every 20 seconds or 10 metres
		 * using the previously declared locationListener
		 */
		locationManager.requestLocationUpdates(myProvider, 0, 0,
				locationListener);

		/**
		 * The purpose of this method is to capture a photo and save it to the
		 * phone database
		 */
		camButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// Get a time stamp to create the unique id
				long timestamp = Calendar.getInstance().getTimeInMillis();
				// String picId = "image"+Long.toString(timestamp);

				File dir = Environment
						.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
				output = new File(dir, "pic.jpg");

				// File file = new
				// File(Environment.getExternalStorageDirectory(),
				// picId+".jpg");

				// Uri imageUri = Uri.fromFile(file);

				// Start the bultin intent for capturing a phone
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

				// Add the output from intent as Extra Output so that it can be
				// retrieved
				intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(output));
				startActivityForResult(intent, 0);
			}
		});

		/**
		 * The purpose of this method is to capture a voice recording and save
		 * it to the phone database
		 */
		micButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(final View v) {

				String mFile = null;
				final String LOG_TAG = "AudioRecordDialog";
				final Dialog dialog = new Dialog(v.getContext());

				// Assign the xml of the dialog to be mic_dialog
				dialog.setContentView(R.layout.mic_dialog);

				// Assign xml components to workable variables
				final Button recButton = (Button) dialog
						.findViewById(R.id.recButton);
				final Button cancelButton = (Button) dialog
						.findViewById(R.id.cancelButton);

				dialog.setTitle("Record Audio Event");
				dialog.show();

				mFile = Environment.getExternalStorageDirectory()
						.getAbsolutePath();
				mFile += "/audiorecordtest.3gp";

				final String mFileName = mFile;

				recButton.setOnClickListener(new View.OnClickListener() {

					private MediaRecorder mRecorder = null;

					/*
					 * Once you click on the Start button, the recorder will
					 * begin to decide if it should stop or begin, the text is
					 * changed to show different states
					 */
					public void onClick(View v) {
						if (recButton.getText().equals("Start")) {
							onRecord(true);
						} else {
							onRecord(false);
						}
					}

					// If the button is called start then begin a recording,
					// otherwise stop it
					public void onRecord(boolean start) {
						if (start) {
							startRecording();
						} else {
							stopRecording();
						}
					}

					private void startRecording() {
						// Assign all neccessary components for recorder then
						// begin
						mRecorder = new MediaRecorder();
						mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
						mRecorder
								.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
						mRecorder
								.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
						mRecorder.setOutputFile(mFileName);

						recButton.setText("Finish");
						try {
							mRecorder.prepare();
						} catch (IOException e) {
							Log.e(LOG_TAG, "prepare() failed");
						}

						mRecorder.start();
					}

					private void stopRecording() {
						mRecorder.stop();
						mRecorder.release();
						mRecorder = null;

						DBAdapter db = null;

						byte[] audio = null;

						try {
							/*
							 * Open up a database instance and convert the audio
							 * recording to a byte stream
							 */
							db = new DBAdapter(getApplicationContext());
							File file = new File("/sdcard/audiorecordtest.3gp");

							FileInputStream fin = new FileInputStream(file);
							BufferedInputStream bis = new BufferedInputStream(
									fin);
							DataInputStream dis = new DataInputStream(bis);
							ByteArrayOutputStream out = new ByteArrayOutputStream();
							copy(dis, out);
							audio = out.toByteArray();
						} catch (Exception e) {
							Log.w(TAG, "ERROR in audio Conversion");
						}

						db.open();
						telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
						String imeistring = telephonyManager.getDeviceId();
						int mediaId = Integer.parseInt(imeistring.substring(0,
								imeistring.length() / 2)) + db.getMediaCount();

						SharedPreferences settings = getSharedPreferences(
								PREFS_NAME, 0);
						boolean isTrip = settings.getBoolean("tripMode", false);
						boolean isEvent = settings.getBoolean("eventMode",
								false);

						EditText audioName = (EditText) dialog
								.findViewById(R.id.editAudioName);
						EditText audioDescription = (EditText) dialog
								.findViewById(R.id.editAudioDescription);
						EditText audioTags = (EditText) dialog
								.findViewById(R.id.editAudioTags);

						if (isTrip && isEvent) {
							// Record the Event and connect it to the current
							// Trip
							int tripId = settings.getInt("tripId", 0);
							int eventId = settings.getInt("eventId", 0);
							String tripName = settings.getString("tripName",
									null);
							String tripDescription = settings.getString(
									"tripDescription", null);
							String user = settings.getString("user", "");

							double eventLng = Double.valueOf(settings
									.getString("eventLongitude", null));
							double eventLat = Double.valueOf(settings
									.getString("eventLatitude", null));
							String eventName = settings.getString("eventName",
									null);
							String eventDescription = settings.getString(
									"eventDescription", null);

							if (db.getTripById(tripId) == null) {
								Log.e(TAG, "Got into mic writing trip");
								db.insertTrip(tripId, tripName,
										tripDescription, new Timestamp(
												new Date().getTime())
												.toString(), "n");
								db.insertAccountTrip(user, tripId, 1, "n");
							}
							if (db.getEventById(eventId) == null) {
								db.insertEvent(eventId, tripId, eventLng,
										eventLat,
										new Timestamp(new Date().getTime())
												.toString(), eventName,
										eventDescription, new Timestamp(
												new Date().getTime())
												.toString(), "n");
							}

							String[] tagList = audioTags.getText().toString()
									.trim().split(",");

							for (int i = 0; i < tagList.length; i++) {
								db.insertTags(tagList[i], mediaId, "media",
										new Timestamp(new Date().getTime())
												.toString(), "n");
							}

							db.insertEventMedia(mediaId, audioName.getText()
									.toString(), audioDescription.getText()
									.toString(), "audio", audio, eventId,
									new Timestamp(new Date().getTime())
											.toString(), "n");
							Toast.makeText(getApplicationContext(),
									"Audio Successfully Saved",
									Toast.LENGTH_LONG).show();
						} else if (isTrip && !isEvent) {
							Toast toast = Toast.makeText(v.getContext(),
									"Please create an event for your trip!",
									Toast.LENGTH_SHORT);
							toast.show();
						} else {
							// Insert information into database

							/*
							 * int nextEventId = db.getEventCount(); double
							 * eventLng = myLocation.getLongitude(); double
							 * eventLat = myLocation.getLatitude();
							 * db.insertEvent(nextEventId, 0, eventLng,
							 * eventLat, new Timestamp(new
							 * Date().getTime()).toString(), "emptyEvent",
							 * "emptyDescription", new Timestamp(new
							 * Date().getTime()).toString());
							 */
							db.insertEventMedia(mediaId, audioName.getText()
									.toString(), audioDescription.getText()
									.toString(), "audio", audio, -1,
									new Timestamp(new Date().getTime())
											.toString(), "n");

							String[] tagList = audioTags.getText().toString()
									.trim().split(",");

							for (int i = 0; i < tagList.length; i++) {
								db.insertTags(tagList[i], mediaId, "media",
										new Timestamp(new Date().getTime())
												.toString(), "n");
							}

							Toast.makeText(getApplicationContext(),
									"Audio Successfully Saved",
									Toast.LENGTH_LONG).show();
						}
						db.close();

						dialog.dismiss();
					}
				});

				cancelButton.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						dialog.dismiss();
					}
				});
			}

		});

		/**
		 * The purpose of this method is to capture a text recording and save it
		 * to the phone database
		 */
		noteButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {

				final String LOG_TAG = "NoteRecordDialog";
				final Dialog dialog = new Dialog(v.getContext());

				dialog.setContentView(R.layout.note_dialog);
				// Assign xml components to workable variables
				final Button saveButton = (Button) dialog
						.findViewById(R.id.noteSave);
				final Button cancelButton = (Button) dialog
						.findViewById(R.id.noteCancel);

				dialog.setTitle("Record Note Event");
				dialog.show();

				// Listening to save button
				saveButton.setOnClickListener(new View.OnClickListener() {

					public void onClick(View v) {
						try {

							EditText noteName = (EditText) dialog
									.findViewById(R.id.editNoteName);
							EditText noteTags = (EditText) dialog
									.findViewById(R.id.editNoteTags);
							EditText noteDescription = (EditText) dialog
									.findViewById(R.id.editNoteDescription);

							// Open an instance of the database, get the
							// coordinates and save information
							DBAdapter db = new DBAdapter(v.getContext());
							db.open();
							telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
							String imeistring = telephonyManager.getDeviceId();
							int mediaId = Integer.parseInt(imeistring
									.substring(0, imeistring.length() / 2))
									+ db.getMediaCount();

							SharedPreferences settings = getSharedPreferences(
									PREFS_NAME, 0);
							boolean isTrip = settings.getBoolean("tripMode",
									false);
							boolean isEvent = settings.getBoolean("eventMode",
									false);

							if (isTrip && isEvent) {
								// Record the Event and connect it to the
								// current Trip

								int tripId = settings.getInt("tripId", 0);
								int eventId = settings.getInt("eventId", 0);
								String user = settings.getString("user", "");

								double eventLng = Double.valueOf(settings
										.getString("eventLongitude", null));
								double eventLat = Double.valueOf(settings
										.getString("eventLatitude", null));
								String tripName = settings.getString(
										"tripName", null);
								String tripDescription = settings.getString(
										"tripDescription", null);
								String eventName = settings.getString(
										"eventName", null);
								String eventDescription = settings.getString(
										"eventDescription", null);

								if (db.getTripById(tripId) == null) {
									Log.e(TAG, "Got into note writing trip");
									db.insertTrip(tripId, tripName,
											tripDescription, new Timestamp(
													new Date().getTime())
													.toString(), "n");
									db.insertAccountTrip(user, tripId, 1, "n");
								}
								if (db.getEventById(eventId) == null) {
									db.insertEvent(eventId, tripId, eventLng,
											eventLat,
											new Timestamp(new Date().getTime())
													.toString(), eventName,
											eventDescription, new Timestamp(
													new Date().getTime())
													.toString(), "n");
								}

								String[] tagList = noteTags.getText()
										.toString().trim().split(",");
								Log.e(TAG,
										"Result when split = "
												+ tagList.toString());
								for (int i = 0; i < tagList.length; i++) {
									db.insertTags(tagList[i], mediaId, "media",
											new Timestamp(new Date().getTime())
													.toString(), "n");
								}

								db.insertEventMedia(mediaId, noteName.getText()
										.toString(), noteDescription.getText()
										.toString(), "text", noteDescription
										.getText().toString().getBytes(),
										eventId,
										new Timestamp(new Date().getTime())
												.toString(), "n");

								Toast.makeText(getBaseContext(),
										"Note Successfully Saved",
										Toast.LENGTH_SHORT).show();

							} else if (isTrip && !isEvent) {
								Toast toast = Toast.makeText(
										v.getContext(),
										"Please create an event for your trip!",
										Toast.LENGTH_SHORT);
								toast.show();
							} else {
								// Record the Event as a Loose Media

								// int nextEventId = db.getEventCount();
								// double eventLng = myLocation.getLongitude();
								// double eventLat = myLocation.getLatitude();
								// db.insertEvent(nextEventId, 0, eventLng,
								// eventLat, new Timestamp(new
								// Date().getTime()).toString(), "emptyEvent",
								// "emptyDescription", new Timestamp(new
								// Date().getTime()).toString());

								db.insertEventMedia(mediaId, noteName.getText()
										.toString(), noteDescription.getText()
										.toString(), "text", noteDescription
										.getText().toString().getBytes(), -1,
										new Timestamp(new Date().getTime())
												.toString(), "n");

								String[] tagList = noteTags.getText()
										.toString().trim().split(",");
								Log.e(TAG,
										"Result when split = "
												+ tagList.toString());
								for (int i = 0; i < tagList.length; i++) {
									db.insertTags(tagList[i], mediaId, "media",
											new Timestamp(new Date().getTime())
													.toString(), "n");
								}

								Toast.makeText(getBaseContext(),
										"Note Successfully Saved",
										Toast.LENGTH_SHORT).show();
							}

							db.close();
						} catch (Exception e) {
							Toast.makeText(getBaseContext(), e.toString(),
									Toast.LENGTH_LONG).show();
							Log.w(TAG, e.toString());
						}
						dialog.dismiss();
					}
				});

				cancelButton.setOnClickListener(new View.OnClickListener() {

					public void onClick(View v) {
						dialog.dismiss();
					}
				});
			}

		});

		/**
		 * The purpose of this method is to allow users to view their loose
		 * media in a gallery
		 */
		looseMedia.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),
						LooseMedia.class);
				// startActivityForResult(intent, 1);
				startActivity(intent);
			}
		});

		/**
		 * The purpose of this method is to allow users to begin events
		 */
		eventButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				if (trip.getText().equals("Stop Recording")) {

					if (eventButton.getText().equals("Start Event")) {
						final Dialog dialog = new Dialog(v.getContext());
						final DBAdapter db = new DBAdapter(v.getContext());

						dialog.setContentView(R.layout.event_dialog);
						dialog.setTitle("Record Event");
						dialog.show();

						// Assign xml components to workable variables
						final Button saveButton = (Button) dialog
								.findViewById(R.id.eventSave);
						final Button cancelButton = (Button) dialog
								.findViewById(R.id.eventCancel);
						final EditText eventName = (EditText) dialog
								.findViewById(R.id.editEventName);
						final EditText eventDescription = (EditText) dialog
								.findViewById(R.id.editEventDescription);
						final double myLat = myLocation.getLatitude();
						final double myLng = myLocation.getLongitude();

						saveButton
								.setOnClickListener(new View.OnClickListener() {

									public void onClick(View v) {
										db.open();
										telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
										String imeistring = telephonyManager
												.getDeviceId();
										int eventId = Integer.parseInt(imeistring
												.substring(0,
														imeistring.length() / 2))
												+ db.getEventCount();
										db.close();

										SharedPreferences settings = getSharedPreferences(
												PREFS_NAME, 0);
										SharedPreferences.Editor editor = settings
												.edit();

										editor.putBoolean("eventMode", true);
										editor.putString("eventName", eventName
												.getText().toString());
										editor.putString("eventDescription",
												eventDescription.getText()
														.toString());
										editor.putInt("eventId", eventId);
										editor.putString("eventLongitude",
												String.valueOf(myLng));
										editor.putString("eventLatitude",
												String.valueOf(myLat));

										editor.commit();
										editor.apply();
										dialog.dismiss();

										Log.e(TAG,
												"Settings contents = "
														+ settings.getBoolean(
																"eventMode",
																false)
														+ ", "
														+ settings.getString(
																"eventName",
																"empty")
														+ ", "
														+ settings
																.getString(
																		"eventDescription",
																		"empty")
														+ ", "
														+ settings.getInt(
																"eventId", 0)
														+ ", "
														+ settings
																.getString(
																		"eventLongitude",
																		"0")
														+ ", "
														+ settings
																.getString(
																		"eventLatitude",
																		"0"));
										eventButton.setText("Stop Event");

										Toast.makeText(getBaseContext(),
												"Event Started",
												Toast.LENGTH_SHORT).show();
									}
								});

						cancelButton
								.setOnClickListener(new View.OnClickListener() {
									public void onClick(View v) {
										dialog.dismiss();
									}
								});

					} else if (eventButton.getText().equals("Stop Event")) {
						final Dialog dialog = new Dialog(v.getContext());

						dialog.setContentView(R.layout.event_option);
						dialog.setTitle("Event Options");
						dialog.show();

						final Button saveTagsButton = (Button) dialog
								.findViewById(R.id.eventTagSave);
						final Button eventCloseButton = (Button) dialog
								.findViewById(R.id.eventEnd);
						final EditText eventTagName = (EditText) dialog
								.findViewById(R.id.editEventTagName);

						eventCloseButton
								.setOnClickListener(new View.OnClickListener() {
									public void onClick(View v) {
										dialog.dismiss();
										eventButton.setText("Start Event");

										SharedPreferences settings = getSharedPreferences(
												PREFS_NAME, 0);
										settings.edit().remove("eventMode")
												.remove("eventName")
												.remove("eventDescription")
												.remove("eventId")
												.remove("eventLongitude")
												.remove("eventLatitude")
												.commit();
									}
								});

						saveTagsButton
								.setOnClickListener(new View.OnClickListener() {
									public void onClick(View v) {
										if (eventTagName.getText().toString()
												.equals("")) {
											Toast.makeText(v.getContext(),
													"No tags were entered",
													Toast.LENGTH_SHORT).show();
										} else {
											DBAdapter db = new DBAdapter(v
													.getContext());
											SharedPreferences settings = getSharedPreferences(
													PREFS_NAME, 0);
											int eventId = settings.getInt(
													"eventId", -1);
											String[] tagList = eventTagName
													.getText().toString()
													.trim().split(",");

											db.open();
											for (int i = 0; i < tagList.length; i++) {
												db.insertTags(
														tagList[i],
														eventId,
														"event",
														new Timestamp(
																new Date()
																		.getTime())
																.toString(),
														"n");
											}
											db.close();
										}

										dialog.dismiss();
									}
								});

					}
				} else if (trip.getText().equals("Start Trip")) {
					Toast.makeText(getBaseContext(), "Please start a Trip",
							Toast.LENGTH_SHORT).show();
				}

			}
		});

		trip.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				if (trip.getText().equals("Start Trip")) {

					final Dialog dialog = new Dialog(v.getContext());

					dialog.setContentView(R.layout.trip_start_dialog);
					// Assign xml components to workable variables
					final Button okButton = (Button) dialog
							.findViewById(R.id.tripStartOk);
					final Button cancelButton = (Button) dialog
							.findViewById(R.id.tripStartCancel);
					final EditText tripName = (EditText) dialog
							.findViewById(R.id.editTripName);
					final EditText tripDescription = (EditText) dialog
							.findViewById(R.id.editTripDescription);

					dialog.setTitle("Trip Start");
					dialog.show();

					// Listening to save button
					okButton.setOnClickListener(new View.OnClickListener() {

						public void onClick(View v) {
							trip.setText("Stop Recording");

							DBAdapter db = new DBAdapter(v.getContext());
							db.open();
							SharedPreferences settings = getSharedPreferences(
									PREFS_NAME, 0);
							SharedPreferences.Editor editor = settings.edit();
							editor.remove("tripMode").remove("tripName")
									.remove("tripId").remove("tripDescription");
							editor.commit();

							telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
							String imeistring = telephonyManager.getDeviceId();
							int tripId = Integer.parseInt(imeistring.substring(
									0, imeistring.length() / 2))
									+ db.getTripCount();

							db.close();
							Log.e(TAG, "DB count = " + tripId);
							editor.putBoolean("tripMode", true);
							editor.putString("tripName", tripName.getText()
									.toString());
							editor.putInt("tripId", tripId);
							editor.putString("tripDescription", tripDescription
									.getText().toString());

							Log.e(TAG, "Contents: "
									+ tripName.getText().toString() + ", "
									+ tripDescription.getText());

							editor.commit();
							editor.apply();
							dialog.dismiss();

							Toast.makeText(getBaseContext(), "Trip Started",
									Toast.LENGTH_SHORT).show();
						}
					});

					cancelButton.setOnClickListener(new View.OnClickListener() {
						public void onClick(View v) {
							dialog.dismiss();
						}
					});

				} else if (eventButton.getText().toString()
						.equals("Stop Event")) {
					Toast.makeText(getBaseContext(),
							"Please end Event before ending Trip",
							Toast.LENGTH_SHORT).show();
				} else if (trip.getText().toString().equals("Stop Recording")) {
					final Dialog dialog = new Dialog(v.getContext());

					final DBAdapter db = new DBAdapter(v.getContext());
					dialog.setContentView(R.layout.trip_option);
					dialog.setTitle("Trip Options");
					dialog.show();

					final Button saveTagsButton = (Button) dialog
							.findViewById(R.id.tripTagSave);
					final Button saveFriendButton = (Button) dialog
							.findViewById(R.id.tripFriendSave);
					final EditText tripTagName = (EditText) dialog
							.findViewById(R.id.editTripTagName);
					final Button tripCloseButton = (Button) dialog
							.findViewById(R.id.tripEnd);
					final Spinner tripFriend = (Spinner) dialog
							.findViewById(R.id.cmbFriends);
					final Spinner tripFriendPermissions = (Spinner) dialog
							.findViewById(R.id.cmbFriendsPermissions);
					try {
						db.open();
						List<String> friends = db.getFriends();
						Log.e(TAG, "*******friends List: " + friends);

						ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(
								v.getContext(),
								android.R.layout.simple_spinner_item, friends);
						dataAdapter
								.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
						tripFriend.setAdapter(dataAdapter);

						ArrayAdapter<String> adapter = new ArrayAdapter<String>(
								v.getContext(),
								android.R.layout.simple_spinner_item);

						adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
						adapter.add("2");
						adapter.add("3");
						tripFriendPermissions.setAdapter(adapter);

						db.close();
					} catch (Exception e) {
						Log.e(TAG, e.toString());
					}
					tripCloseButton
							.setOnClickListener(new View.OnClickListener() {
								public void onClick(View v) {
									dialog.dismiss();
									trip.setText("Start Trip");

									SharedPreferences settings = getSharedPreferences(
											PREFS_NAME, 0);
									settings.edit().remove("tripMode")
											.remove("tripName")
											.remove("tripId")
											.remove("tripDescription").commit();
								}
							});

					saveFriendButton
							.setOnClickListener(new View.OnClickListener() {
								public void onClick(View v) {

									if (tripFriend.getSelectedItem() != null
											&& tripFriendPermissions
													.getSelectedItem() != null) {

										SharedPreferences settings = getSharedPreferences(
												PREFS_NAME, 0);
										int tripId = settings.getInt("tripId",
												-1);
										Log.i(TAG,
												"*******Selected permission: "
														+ tripFriendPermissions
																.getSelectedItem()
																.toString());
										int permission = Integer
												.valueOf(tripFriendPermissions
														.getSelectedItem()
														.toString());
										db.open();
										Log.i(TAG,
												"*******Insert to Account_Trip table: \nFriend:"
														+ tripFriend
																.getSelectedItem()
																.toString()
														+ "\n tripId: "
														+ tripId
														+ "Permission: "
														+ permission);
										db.insertAccountTrip(tripFriend.getSelectedItem().toString(),tripId, permission, "n");
										
										db.close();

										dialog.dismiss();
									} else {
										Toast.makeText(v.getContext(),
												"No friend was selected",
												Toast.LENGTH_SHORT).show();
									}
								}
							});

					saveTagsButton
							.setOnClickListener(new View.OnClickListener() {
								public void onClick(View v) {
									if (tripTagName.getText().toString()
											.equals("")) {
										Toast.makeText(v.getContext(),
												"No tags were entered",
												Toast.LENGTH_SHORT).show();
									} else {
										DBAdapter db = new DBAdapter(v
												.getContext());
										SharedPreferences settings = getSharedPreferences(
												PREFS_NAME, 0);
										int tripId = settings.getInt("tripId",
												-1);
										String[] tagList = tripTagName
												.getText().toString().trim()
												.split(",");

										db.open();
										for (int i = 0; i < tagList.length; i++) {
											db.insertTags(
													tagList[i],
													tripId,
													"trip",
													new Timestamp(new Date()
															.getTime())
															.toString(), "n");
										}
										db.close();
									}

									dialog.dismiss();
								}
							});

				}
			}
		});

		/**
		 * The purpose of this method is to allow users to view their trips in a
		 * Log
		 */
		tripLogButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),
						TripLog.class);
				startActivity(intent);

			}
		});
	}

	public void eventPlot(int eventId) {
		DBAdapter db = new DBAdapter(this);

		db.open();
		Event e = db.getEventById(eventId);
		db.close();

		GeoPoint myPoint = new GeoPoint((int) (e.getLatitude() * 1E6),
				(int) (e.getLongitude() * 1E6));
		OverlayItem overlayitem = new OverlayItem(myPoint, e.getName(),
				e.getDescription());
		itemizedoverlay.addOverlay(overlayitem);
	}

	/*
	 * Method for copying contents from a database to a phones assets for quick
	 * use
	 */
	public void CopyDB(InputStream inputStream, OutputStream outputStream)
			throws IOException {
		byte[] buffer = new byte[1024];
		int length;
		while ((length = inputStream.read(buffer)) > 0) {
			outputStream.write(buffer, 0, length);
		}
		inputStream.close();
		outputStream.close();
	}

	/**
	 * The purpose of this method handle a captured image after the shutter is
	 * taken
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case (0): { // Capturing a photo
			if (resultCode == RESULT_OK) {
				byte[] tempImage = null;

				Uri imageUri = null;

				if (data == null) {
					imageUri = Uri.fromFile(output);
				} else {
					imageUri = data.getData();
				}

				Bitmap bitmap = null;
				ContentResolver cr = getContentResolver();

				// Convert the image into a byte stream
				try {
					bitmap = android.provider.MediaStore.Images.Media
							.getBitmap(cr, imageUri);
					Bitmap newBitmap = Bitmap.createScaledBitmap(bitmap, 640,
							480, false);
					ByteArrayOutputStream stream = new ByteArrayOutputStream();
					newBitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
					tempImage = stream.toByteArray();
					bitmap.recycle();
					newBitmap.recycle();
					System.gc();

				} catch (Exception e) {
					Log.e("Camera", e.toString());
				}

				final byte[] image = tempImage;
				final Dialog dialog = new Dialog(this);
				dialog.setTitle("Record Image Event");
				dialog.setContentView(R.layout.image_dialog);
				dialog.show();

				final Button okButton = (Button) dialog
						.findViewById(R.id.imageSave);
				final Button cancelButton = (Button) dialog
						.findViewById(R.id.imageCancel);
				final EditText imageName = (EditText) dialog
						.findViewById(R.id.editImageName);
				final EditText imageDescription = (EditText) dialog
						.findViewById(R.id.editImageDescription);
				final EditText imageTags = (EditText) dialog
						.findViewById(R.id.editImageTags);

				okButton.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {

						// Open database instance, get coordinates and save
						// information
						DBAdapter db = new DBAdapter(v.getContext());
						db.open();
						telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
						String imeistring = telephonyManager.getDeviceId();
						int mediaId = Integer.parseInt(imeistring.substring(0,
								imeistring.length() / 2)) + db.getMediaCount();

						SharedPreferences settings = getSharedPreferences(
								PREFS_NAME, 0);
						boolean isTrip = settings.getBoolean("tripMode", false);
						int tripId = settings.getInt("tripId", 0);
						boolean isEvent = settings.getBoolean("eventMode",
								false);
						int eventId = settings.getInt("eventId", 0);

						if (isTrip && isEvent) {
							// Record the Event and connect it to the current
							// Trip

							String user = settings.getString("user", "");
							String tripName = settings.getString("tripName",
									null);
							String tripDescription = settings.getString(
									"tripDescription", null);
							double eventLng = Double.valueOf(settings
									.getString("eventLongitude", null));
							double eventLat = Double.valueOf(settings
									.getString("eventLatitude", null));
							String eventName = settings.getString("eventName",
									null);
							String eventDescription = settings.getString(
									"eventDescription", null);

							if (db.getTripById(tripId) == null) {
								db.insertTrip(tripId, tripName,
										tripDescription, new Timestamp(
												new Date().getTime())
												.toString(), "n");
								db.insertAccountTrip(user, tripId, 1, "n");
							}
							if (db.getEventById(eventId) == null) {
								db.insertEvent(eventId, tripId, eventLng,
										eventLat,
										new Timestamp(new Date().getTime())
												.toString(), eventName,
										eventDescription, new Timestamp(
												new Date().getTime())
												.toString(), "n");
							}

							String[] tagList = imageTags.getText().toString()
									.trim().split(",");

							for (int i = 0; i < tagList.length; i++) {
								db.insertTags(tagList[i], mediaId, "media",
										new Timestamp(new Date().getTime())
												.toString(), "n");
							}

							db.insertEventMedia(mediaId, imageName.getText()
									.toString(), imageDescription.getText()
									.toString(), "photo", image, eventId,
									new Timestamp(new Date().getTime())
											.toString(), "n");

							Toast.makeText(getBaseContext(),
									"Image Successfully Saved",
									Toast.LENGTH_SHORT).show();
						} else if (isTrip && !isEvent) {
							Toast toast = Toast.makeText(v.getContext(),
									"Please create an event for your trip!",
									Toast.LENGTH_SHORT);
							toast.show();
						} else {

							/*
							 * int nextEventId = db.getEventCount(); double
							 * eventLng = myLocation.getLongitude(); double
							 * eventLat = myLocation.getLatitude();
							 * db.insertEvent(nextEventId, 0, eventLng,
							 * eventLat, new Timestamp(new
							 * Date().getTime()).toString(), "emptyEvent",
							 * "emptyDescription", new Timestamp(new
							 * Date().getTime()).toString());
							 */
							db.insertEventMedia(mediaId, imageName.getText()
									.toString(), imageDescription.getText()
									.toString(), "photo", image, -1,
									new Timestamp(new Date().getTime())
											.toString(), "n");

							String[] tagList = imageTags.getText().toString()
									.trim().split(",");

							for (int i = 0; i < tagList.length; i++) {
								db.insertTags(tagList[i], mediaId, "media",
										new Timestamp(new Date().getTime())
												.toString(), "n");
							}

							Toast toast = Toast.makeText(v.getContext(),
									"Image Successfully Saved",
									Toast.LENGTH_SHORT);
							toast.show();
						}
						db.close();
						dialog.dismiss();
					}
				});

				cancelButton.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						dialog.dismiss();
					}
				});
			}
			break;
		}
		}
	}

	/**
	 * The purpose of this method is to create a menu
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return true;
	}

	/**
	 * The purpose of this method is to determine the action of users on the
	 * menu
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.menu_friend:
			try {
				Intent intent = new Intent(getApplicationContext(),
						SearchFriendActivity.class);
				startActivityForResult(intent, 1);
			} catch (Exception e) {
				Log.w(TAG, e.toString());
			}
			return true;
		case R.id.menu_sync:
			String username = settings.getString("user", "");
			if (username.equals("")) {
				Log.e(TAG, "Tried to sync but no user in settings!!!");
				return false;
			}
			SyncHelper syncHelper = new SyncHelper(username,
					getApplicationContext());
			boolean success = true;
			try {
				Toast.makeText(this, "Sync in progress...", Toast.LENGTH_SHORT)
						.show();
				syncHelper.doSync();

			} catch (Exception e) {
				Log.e(TAG, e.toString());
				success = false;
			} finally {
				if (success) {
					Toast.makeText(this, "Sync successfull", Toast.LENGTH_SHORT)
							.show();
				} else {
					Toast.makeText(this, "Sync failed, please try again",
							Toast.LENGTH_SHORT).show();
				}
			}

			return true;
		case R.id.menu_email:
			try {
				startActivity(new Intent(this, EmailFriendActivity.class));
			} catch (Exception e) {
				Log.w(TAG, "Something wrong" + e.toString());
			}
			return true;
		case R.id.menu_tags:
			try {
				startActivity(new Intent(this, SearchTagsActivity.class));
			} catch (Exception e) {
				Log.w(TAG, "Something wrong" + e.toString());
			}
			return true;
		case R.id.menu_logout:
			settings.edit().remove("user").commit();
			finish();
			return true;
		case R.id.menu_lbs:

			double myLat = -45.868945;
			double myLng = 170.504959;
			GeoPoint myPoint = new GeoPoint((int) (myLat * 1E6),
					(int) (myLng * 1E6));
			OverlayItem overlayitem = new OverlayItem(myPoint,
					"The Rainforest Restaurant",
					"lovely food, excellent flavours");
			restrauantsitemizedoverlay.addOverlay(overlayitem);

			myLat = -45.868706;
			myLng = 170.506407;
			myPoint = new GeoPoint((int) (myLat * 1E6), (int) (myLng * 1E6));
			overlayitem = new OverlayItem(myPoint,
					"Tokyo Garden Japanese Restaurant",
					"quiet and comfortable environment");
			restrauantsitemizedoverlay.addOverlay(overlayitem);

			myLat = -45.866316;
			myLng = 170.508081;
			myPoint = new GeoPoint((int) (myLat * 1E6), (int) (myLng * 1E6));
			overlayitem = new OverlayItem(myPoint, "Vino Vena Restaurant",
					"nice simple but nothing stands out");
			restrauantsitemizedoverlay.addOverlay(overlayitem);

			myLat = -45.86958;
			myLng = 170.506246;
			myPoint = new GeoPoint((int) (myLat * 1E6), (int) (myLng * 1E6));
			overlayitem = new OverlayItem(myPoint,
					"Table Se7en Restaurant And Bar", "upstairs eatery");
			restrauantsitemizedoverlay.addOverlay(overlayitem);

			myLat = -45.868923;
			myLng = 170.506289;
			myPoint = new GeoPoint((int) (myLat * 1E6), (int) (myLng * 1E6));
			overlayitem = new OverlayItem(myPoint,
					"The Reef Seafood Restaurant & Bar",
					"quick service, reasonable price like $10 lunch");
			restrauantsitemizedoverlay.addOverlay(overlayitem);

			// Banks

			myLat = -45.872651;
			myLng = 170.504551;
			myPoint = new GeoPoint((int) (myLat * 1E6), (int) (myLng * 1E6));
			overlayitem = new OverlayItem(myPoint, "Westpac",
					"Personal Banking");
			banksitemizedoverlay.addOverlay(overlayitem);

			myLat = -45.8728;
			myLng = 170.504444;
			myPoint = new GeoPoint((int) (myLat * 1E6), (int) (myLng * 1E6));
			overlayitem = new OverlayItem(myPoint, "BNZ", "Bank of New Zealand");
			banksitemizedoverlay.addOverlay(overlayitem);

			myLat = -45.875041;
			myLng = 170.505463;
			myPoint = new GeoPoint((int) (myLat * 1E6), (int) (myLng * 1E6));
			overlayitem = new OverlayItem(myPoint, "Rabobank Dunedin",
					"Rabobank Dunedin Branch");
			banksitemizedoverlay.addOverlay(overlayitem);

			myLat = -45.871665;
			myLng = 170.503124;
			myPoint = new GeoPoint((int) (myLat * 1E6), (int) (myLng * 1E6));
			overlayitem = new OverlayItem(myPoint,
					"Credit Union South as NZCSR", "NZCSR South Branch");
			banksitemizedoverlay.addOverlay(overlayitem);

			myLat = -45.872718;
			myLng = 170.504154;
			myPoint = new GeoPoint((int) (myLat * 1E6), (int) (myLng * 1E6));
			overlayitem = new OverlayItem(myPoint, "ANZ",
					"ANZ Banking Group(New Zealand) Limited");
			banksitemizedoverlay.addOverlay(overlayitem);

			// Cafes

			myLat = -45.874981;
			myLng = 170.502877;
			myPoint = new GeoPoint((int) (myLat * 1E6), (int) (myLng * 1E6));
			overlayitem = new OverlayItem(myPoint, "Jizo Japanese Cafe & Bar",
					"teriyaki chicken -sushi");
			cafeitemizedoverlay.addOverlay(overlayitem);

			myLat = -45.874264;
			myLng = 170.509529;
			myPoint = new GeoPoint((int) (myLat * 1E6), (int) (myLng * 1E6));
			overlayitem = new OverlayItem(myPoint, "Ironic Cafe & Bar",
					"slice mushroom and flavours lamb");
			cafeitemizedoverlay.addOverlay(overlayitem);

			myLat = -45.868064;
			myLng = 170.511053;
			myPoint = new GeoPoint((int) (myLat * 1E6), (int) (myLng * 1E6));
			overlayitem = new OverlayItem(myPoint, "Ombrellos Cafe & Bar",
					"big plates food and good service");
			cafeitemizedoverlay.addOverlay(overlayitem);

			myLat = -45.871993;
			myLng = 170.506074;
			myPoint = new GeoPoint((int) (myLat * 1E6), (int) (myLng * 1E6));
			overlayitem = new OverlayItem(myPoint, "Stock X Change Bar",
					"Dunedin Stock Xchange Branch");
			cafeitemizedoverlay.addOverlay(overlayitem);

			myLat = -45.867526;
			myLng = 170.514722;
			myPoint = new GeoPoint((int) (myLat * 1E6), (int) (myLng * 1E6));
			overlayitem = new OverlayItem(myPoint, "Eureka Cafe & Bar",
					"warm and friendly atmosphere");
			cafeitemizedoverlay.addOverlay(overlayitem);

			// Motels

			myLat = -45.87525;
			myLng = 170.50292;
			myPoint = new GeoPoint((int) (myLat * 1E6), (int) (myLng * 1E6));
			overlayitem = new OverlayItem(myPoint, "Scenic Hotel Dunedin City",
					"helpful and friendly staff and nice rooms");
			motelsitemizedoverlay.addOverlay(overlayitem);

			myLat = -45.877939;
			myLng = 170.500946;
			myPoint = new GeoPoint((int) (myLat * 1E6), (int) (myLng * 1E6));
			overlayitem = new OverlayItem(myPoint, "Mercure Dunedin",
					"attentive staff most of the time, but mould in bathroom");
			motelsitemizedoverlay.addOverlay(overlayitem);

			myLat = -45.864074;
			myLng = 170.507898;
			myPoint = new GeoPoint((int) (myLat * 1E6), (int) (myLng * 1E6));
			overlayitem = new OverlayItem(myPoint, "Bluestone On George",
					"the best equipment and highest quality of all hotels");
			motelsitemizedoverlay.addOverlay(overlayitem);

			myLat = -45.861743;
			myLng = 170.510044;
			myPoint = new GeoPoint((int) (myLat * 1E6), (int) (myLng * 1E6));
			overlayitem = new OverlayItem(myPoint, "Quality Hotel Cargills",
					"friendly, helpful and informative staff");
			motelsitemizedoverlay.addOverlay(overlayitem);

			myLat = -45.870589;
			myLng = 170.509872;
			myPoint = new GeoPoint((int) (myLat * 1E6), (int) (myLng * 1E6));
			overlayitem = new OverlayItem(myPoint, "LivingSpace Dunedin",
					"pricey");
			motelsitemizedoverlay.addOverlay(overlayitem);

			// Service Stations

			myLat = -45.863238;
			myLng = 170.510902;
			myPoint = new GeoPoint((int) (myLat * 1E6), (int) (myLng * 1E6));
			overlayitem = new OverlayItem(myPoint, "Caltex City North",
					"Dunedin North Caltex");
			servicestationsitemizedoverlay.addOverlay(overlayitem);

			myLat = -45.861146;
			myLng = 170.510516;
			myPoint = new GeoPoint((int) (myLat * 1E6), (int) (myLng * 1E6));
			overlayitem = new OverlayItem(myPoint, "BP 2GO Regent",
					"BP Branch at Regent");
			servicestationsitemizedoverlay.addOverlay(overlayitem);

			myLat = -45.858157;
			myLng = 170.514851;
			myPoint = new GeoPoint((int) (myLat * 1E6), (int) (myLng * 1E6));
			overlayitem = new OverlayItem(myPoint, "BP 2GO Dunedin North",
					"BP Brach at Dunedin North");
			servicestationsitemizedoverlay.addOverlay(overlayitem);

			myLat = -45.870589;
			myLng = 170.510645;
			myPoint = new GeoPoint((int) (myLat * 1E6), (int) (myLng * 1E6));
			overlayitem = new OverlayItem(myPoint, "Instone Motors",
					"Local Service Station");
			servicestationsitemizedoverlay.addOverlay(overlayitem);

			myLat = -45.857051;
			myLng = 170.515838;
			myPoint = new GeoPoint((int) (myLat * 1E6), (int) (myLng * 1E6));
			overlayitem = new OverlayItem(myPoint, "Mobi North Gate",
					"Service Station at Mobi North");
			servicestationsitemizedoverlay.addOverlay(overlayitem);

			// Supermarkets

			myLat = -45.891442;
			myLng = 170.499229;
			myPoint = new GeoPoint((int) (myLat * 1E6), (int) (myLng * 1E6));
			overlayitem = new OverlayItem(myPoint, "PAK'nSAVE Dunedin",
					"Dunedin PAK'nSAVE Branch");
			supermarketsitemizedoverlay.addOverlay(overlayitem);

			myLat = -45.895146;
			myLng = 170.508842;
			myPoint = new GeoPoint((int) (myLat * 1E6), (int) (myLng * 1E6));
			overlayitem = new OverlayItem(myPoint, "Woolworths-Andersons Bay",
					"Woolworths Dunedin");
			supermarketsitemizedoverlay.addOverlay(overlayitem);

			myLat = -45.87531;
			myLng = 170.499573;
			myPoint = new GeoPoint((int) (myLat * 1E6), (int) (myLng * 1E6));
			overlayitem = new OverlayItem(myPoint, "The Warehouse",
					"Dunedin Branch of The Warehouse");
			supermarketsitemizedoverlay.addOverlay(overlayitem);

			myLat = -45.894668;
			myLng = 170.481377;
			myPoint = new GeoPoint((int) (myLat * 1E6), (int) (myLng * 1E6));
			overlayitem = new OverlayItem(myPoint, "Four Square Caversham",
					"Dunedin Branch of 4Square");
			supermarketsitemizedoverlay.addOverlay(overlayitem);

			myLat = -45.90733;
			myLng = 170.47245;
			myPoint = new GeoPoint((int) (myLat * 1E6), (int) (myLng * 1E6));
			overlayitem = new OverlayItem(myPoint,
					"Middleton Road Food Centre", "Middleton Food Centre");
			supermarketsitemizedoverlay.addOverlay(overlayitem);

			mapOverlays.add(restrauantsitemizedoverlay);
			mapOverlays.add(banksitemizedoverlay);
			mapOverlays.add(cafeitemizedoverlay);
			mapOverlays.add(motelsitemizedoverlay);
			mapOverlays.add(servicestationsitemizedoverlay);
			mapOverlays.add(supermarketsitemizedoverlay);

			return true;
		}
		return false;
	}

	public static long copy(InputStream from, OutputStream to)
			throws IOException {
		byte[] buf = new byte[0x1000];// 4K
		long total = 0;
		while (true) {
			int r = from.read(buf);
			if (r == -1) {
				break;
			}
			to.write(buf, 0, r);
			total += r;
		}
		return total;
	}

}