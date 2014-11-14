package com.android;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.android.AudioCategoryActivity.ImageAdapter;

import domain.Media;
import domain.Trip;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class AudioCategoryActivity extends Activity {
	private static final String TAG = "AudioCategory";

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.audio_grid);

		try {
			// Construct a gridView
			GridView gridView = (GridView) findViewById(R.id.audiogridview);

			// Assign the adapter for the grid view to be ImageAdapter (defined
			// below)
			gridView.setAdapter(new ImageAdapter(this));

			gridView.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> parent, View v,
						int position, long id) {
					// If an item within the grid is chosen then create a dialog
					// and provide some options
					final Dialog d = new Dialog(v.getContext());
					final DBAdapter db = new DBAdapter(v.getContext());

					final int mediaId = (int) v.getId();
					Log.e(TAG, "DB media ID = " + mediaId);

					d.setContentView(R.layout.loose_media_dialog_extended);

					Button showButton = (Button) d.findViewById(R.id.mediaShow);
					Button editButton = (Button) d.findViewById(R.id.mediaEdit);
					Button sharingButton = (Button) d
							.findViewById(R.id.mediaShare);
					Button deleteButton = (Button) d
							.findViewById(R.id.mediaDelete);
					Button associateButton = (Button) d
							.findViewById(R.id.mediaAssociate);

					d.setTitle("Loose Audio Options");
					d.show();

					// display the note in full screen
					showButton.setOnClickListener(new View.OnClickListener() {

						public void onClick(View v) {

							db.open();
							final Media mediaClicked = db.getMediaById(mediaId);
							db.close();

							final Dialog displayDialog = new Dialog(v
									.getContext());
							displayDialog
									.setContentView(R.layout.mic_playback_display);
							displayDialog.setTitle(mediaClicked.getName()
									.toString());
							displayDialog.show();

							Button playButton = (Button) displayDialog
									.findViewById(R.id.btn_mic_playback_display_play);
							Button closeButton = (Button) displayDialog
									.findViewById(R.id.btn_mic_playback_display_close);

							playButton
									.setOnClickListener(new View.OnClickListener() {
										public void onClick(View v) {
											playMp3(mediaClicked.getBlob());
										}
									});

							closeButton
									.setOnClickListener(new View.OnClickListener() {
										public void onClick(View v) {
											displayDialog.dismiss();
										}
									});
						}
					});

					// edit a media's properties such as name, description and
					// tags
					editButton.setOnClickListener(new View.OnClickListener() {
						public void onClick(View v) {
							final Dialog editD = new Dialog(v.getContext());
							editD.setContentView(R.layout.mic_edit_dialog);
							editD.show();
							db.open();
							Media m = null;

							Button saveButton = (Button) editD
									.findViewById(R.id.audioSave);
							Button cancelButton = (Button) editD
									.findViewById(R.id.audioCancel);

							final EditText noteName = (EditText) editD
									.findViewById(R.id.editAudioName);
							final EditText noteDescription = (EditText) editD
									.findViewById(R.id.editAudioDescription);
							final EditText noteTag = (EditText) editD
									.findViewById(R.id.editAudioTags);

							try {
								m = db.getMediaById(mediaId);
								Log.e(TAG,
										m.getMediaId() + " " + m.getPoiId()
												+ "" + m.getName() + " "
												+ m.getDescription() + " "
												+ m.getType() + " "
												+ m.getUpdatetime());
							} catch (Exception error) {
								Log.e(TAG, "DB media error," + error.toString());
							}

							// display existing value in the edit text box
							noteName.setText(m.getName().toString());
							noteDescription.setText(m.getDescription()
									.toString());

							ArrayList<String> tags = db.getTagsByMediaId(m
									.getMediaId());
							String allTags = "";
							// if there are tags for this media, display them in
							// the tags edit box
							if (tags.size() != 0) {
								// Concatenate all strings in the Tags array
								// list to a single string with comma separated
								for (int i = 0; i < tags.size(); i++) {
									if (i < tags.size() - 1)
										allTags += tags.get(i).toString()
												+ ", ";
									else
										allTags += tags.get(i);
								}
							}
							noteTag.setText(allTags);

							final Media tempMedia = m;

							// save changes made to the media
							saveButton
									.setOnClickListener(new View.OnClickListener() {
										public void onClick(View v) {
											// Get values out of dialog
											Media newM = new Media(tempMedia
													.getMediaId(), noteName
													.getText().toString(),
													noteDescription.getText()
															.toString(),
													tempMedia.getType(),
													tempMedia.getBlob(),
													tempMedia.getPoiId(),
													new Timestamp(new Date()
															.getTime()), "U");
											// Update the Media table with name,
											// description, tags and updatetime
											int mediaResult = db
													.updateMedia(newM);

											Toast toast;
											if (mediaResult > 0)
												toast = Toast.makeText(
														v.getContext(),
														"Audio updated successfully",
														Toast.LENGTH_LONG);
											else
												toast = Toast.makeText(
														v.getContext(),
														"Audio update failed, try again.",
														Toast.LENGTH_LONG);
											toast.show();

											db.close();
											editD.dismiss();
										}
									});

							// cancel editing
							cancelButton
									.setOnClickListener(new View.OnClickListener() {
										public void onClick(View v) {
											db.close();
											editD.dismiss();
										}
									});
						}
					});

					// delete a media from the media table
					deleteButton.setOnClickListener(new View.OnClickListener() {
						public void onClick(View v) {
							db.open();
							Log.e(TAG, "Media ID = " + mediaId);
							int mediaResult = db.deleteMedia(mediaId);

							Toast toast;
							if (mediaResult > 0)
								toast = Toast.makeText(v.getContext(),
										"Audio deleted successfully",
										Toast.LENGTH_LONG);
							else
								toast = Toast.makeText(v.getContext(),
										"Delete audio failed, try again.",
										Toast.LENGTH_LONG);
							toast.show();

							db.close();
							d.dismiss();
						}
					});

					// associate media to event and/or trip
					associateButton
							.setOnClickListener(new View.OnClickListener() {
								public void onClick(View v) {

									final Dialog dialog = new Dialog(v
											.getContext());
									dialog.setContentView(R.layout.media_associate_option);
									dialog.show();

									db.open();
									Log.e(TAG, "Media ID = " + mediaId);

									List<Trip> trips = db.getTrips();
									List<String> tripTitles = new ArrayList<String>();
									db.close();

									for (Trip t : trips) {
										tripTitles.add(t.getName());
									}

									final Spinner tripSpinner = (Spinner) dialog
											.findViewById(R.id.cmbTrips);
									Button associateAccept = (Button) dialog
											.findViewById(R.id.btnAssociateMediaAccept);
									Button associateCancel = (Button) dialog
											.findViewById(R.id.btnAssociateMediaCancel);
									final EditText eventName = (EditText) dialog
											.findViewById(R.id.editTxtEventName);
									final EditText eventDescription = (EditText) dialog
											.findViewById(R.id.editTxtEventDescription);

									ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(
											v.getContext(),
											android.R.layout.simple_spinner_item,
											tripTitles);
									dataAdapter
											.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

									tripSpinner.setAdapter(dataAdapter);

									associateAccept
											.setOnClickListener(new View.OnClickListener() {
												public void onClick(View v) {
													if (tripSpinner
															.getSelectedItem() != null) {

														db.open();
														TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
														String imeistring = telephonyManager
																.getDeviceId();
														int eventId = Integer
																.parseInt(imeistring
																		.substring(
																				0,
																				imeistring
																						.length() / 2))
																+ db.getEventCount();
														Trip trip = db
																.getTripByName((String) tripSpinner
																		.getSelectedItem());
														db.close();

														LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
														String myProvider = locationManager
																.getBestProvider(
																		new Criteria(),
																		false);
														Location myLocation = locationManager
																.getLastKnownLocation(myProvider);

														db.insertEvent(
																eventId,
																trip.getTripId(),
																myLocation
																		.getLongitude(),
																myLocation
																		.getLatitude(),
																new Timestamp(
																		new Date()
																				.getTime())
																		.toString(),
																eventName
																		.getText()
																		.toString(),
																eventDescription
																		.getText()
																		.toString(),
																new Timestamp(
																		new Date()
																				.getTime())
																		.toString(),
																"n");
														Media mediaResult = db
																.getMediaById(mediaId);
														// mediaResult.setFlag("n");
														mediaResult
																.setPoiId(eventId);

														db.updateMedia(mediaResult);

														dialog.dismiss();
													} else {
														Toast.makeText(
																v.getContext(),
																"No Trip was selected",
																Toast.LENGTH_SHORT)
																.show();
													}
												}
											});

									associateCancel
											.setOnClickListener(new View.OnClickListener() {
												public void onClick(View v) {

													dialog.dismiss();
												}
											});

									d.dismiss();
								}
							});

					// delete a media from the media table
					sharingButton
							.setOnClickListener(new View.OnClickListener() {
								public void onClick(View v) {
									db.open();
									Media mediaClicked = db
											.getMediaById(mediaId);
									db.close();

									// create a jpg file that can be used to
									// store the current image byte array
									File audioFile = new File("/sdcard/DCIM/"
											+ "display"
											+ String.valueOf(mediaClicked
													.getMediaId()) + ".mp3");
									Log.e(TAG, "File = " + audioFile.toString());
									try {
										audioFile.createNewFile();
										// write the byte array to a
										// FileOutputStream which gets saved to
										// the imageFile
										FileOutputStream fos = new FileOutputStream(
												audioFile);
										fos.write(mediaClicked.getBlob());
										fos.flush();
										fos.close();
									} catch (IOException ex) {
										Log.e(TAG, "IO problem Yo");
									} catch (Exception e) {
										Log.e(TAG,
												"Converting from array to file has problem");
									}

									// Call the decode method to convert a jpg
									// file to a bitmap
									// Bitmap bmp = decodeFile(imageFile);
									// Scale down the bitmap to be a thumbnail
									// bmp = Bitmap.createScaledBitmap(bmp, 640,
									// 480, false);

									shareIt(Uri.fromFile(audioFile));
								}
							});
				}
			});

		} catch (Exception e) {
			Log.w(TAG, e.toString());
		}
	}

	// share it method
	private void shareIt(Uri uri) {
		// sharing implementation here
		Intent intent = new Intent();
		intent.setAction(android.content.Intent.ACTION_VIEW);
		Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
		sharingIntent.setType("audio/mp3");
		String shareBody = "Hay, here is some of the cool files I can share using the Travel Diary System";
		sharingIntent.putExtra(android.content.Intent.EXTRA_STREAM, uri);
		sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
				"Travel Diary Share");
		sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
		intent.setDataAndType(uri, "audio/*");
		startActivity(intent);
		startActivity(Intent.createChooser(sharingIntent, "Share via"));
	}

	private void playMp3(byte[] mp3SoundByteArray) {
		try {
			// create temp file that will hold byte array
			File tempMp3 = File
					.createTempFile("kurchina", "mp3", getCacheDir());
			tempMp3.deleteOnExit();
			FileOutputStream fos = new FileOutputStream(tempMp3);
			fos.write(mp3SoundByteArray);
			fos.close();

			// Tried reusing instance of media player
			// but that resulted in system crashes...
			MediaPlayer mediaPlayer = new MediaPlayer();

			// Tried passing path directly, but kept getting
			// "Prepare failed.: status=0x1"
			// so using file descriptor instead
			FileInputStream fis = new FileInputStream(tempMp3);
			mediaPlayer.reset();
			mediaPlayer.setDataSource(fis.getFD());

			mediaPlayer.prepare();
			mediaPlayer.start();
		} catch (IOException ex) {
			String s = ex.toString();
			ex.printStackTrace();
		}
	}

	/**
	 * The purpose of this program is to develop an image adapter based on
	 * BaseAdapter which will present Notes that then can be used by a grid view
	 * 
	 * @peerReview
	 * @date 06/09/12
	 * @name ImageAdapter
	 * @author Mostafa Alwash, June Cui
	 */
	public class ImageAdapter extends BaseAdapter {
		private Context mContext;
		private Integer[] mThumbTypes;
		private Integer[] mThumbIds;// variable to store a note's ID

		public ImageAdapter(Context c) {
			mContext = c;

			// Create a database instance and retrieve all media
			DBAdapter db = new DBAdapter(mContext);
			db.open();

			ArrayList<Media> resultList = new ArrayList<Media>();
			// get array of photo objects
			resultList = db.getLooseMedia("audio");
			Log.e(TAG, resultList.size() + " loose audio retrieved!~!~!~");

			// create a Bitmap array to store thumbnail bitmaps
			mThumbTypes = new Integer[resultList.size()];
			// create a integer array to store all photo IDs
			mThumbIds = new Integer[resultList.size()];

			// Iterate through each object and convert the blob data to bitmap
			// thumbnails
			for (int i = 0; i < resultList.size(); i++) {

				// get the media ID of the current photo
				mThumbIds[i] = resultList.get(i).getMediaId();
				mThumbTypes[i] = R.drawable.point_sound;
			}

			db.close();
		}

		// To get the total number of photos
		public int getCount() {
			return mThumbIds.length;
		}

		// To get a note
		public Object getItem(int position) {
			return mThumbIds[position];
		}

		// To get the note ID
		public long getItemId(int position) {
			return mThumbIds[position];
		}

		// create a new ImageView for each text item referenced by the Adapter
		public View getView(int position, View convertView, ViewGroup parent) {
			ImageView imageView;
			if (convertView == null) { // if it's not recycled, initialize some
										// attributes
				imageView = new ImageView(mContext);
				// set the size of the image to display on the grid
				imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
				imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
				imageView.setPadding(8, 8, 8, 8);
			} else {
				imageView = (ImageView) convertView;
			}

			imageView.setImageResource(mThumbTypes[position]);
			imageView.setId(mThumbIds[position]);
			return imageView;
		}
	}
}
