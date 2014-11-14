package com.android;

import java.io.ByteArrayInputStream;
import java.io.File;

import android.telephony.TelephonyManager;
import android.util.Base64;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.sql.Timestamp;
import java.util.ArrayList;

import domain.Media;
import domain.Trip;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

public class PhotoCategoryActivity extends Activity {
	private static final String TAG = "PhotoCategory";
	private static final int THUMBNAIL_SIZE = 85;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.photo_grid);

		try {
			// Construct a gridView
			GridView gridView = (GridView) findViewById(R.id.photogridview);
			// Assign the adapter for the grid view to be ImageAdapter (defined
			// below)
			gridView.setAdapter(new ImageAdapter(this));

			gridView.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> parent, View v,
						int position, long id) {
					/*
					 * If an item within the grid is chosen then create a dialog
					 * and provide some options
					 */
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

					d.setTitle("Loose Photo Options");
					d.show();

					// display the photo in full screen
					showButton.setOnClickListener(new View.OnClickListener() {

						public void onClick(View v) {
							db.open();
							Media mediaClicked = db.getMediaById(mediaId);
							db.close();

							Dialog displayDialog = new Dialog(v.getContext());
							displayDialog
									.setContentView(R.layout.photo_display);

							displayDialog.setTitle(mediaClicked.getName()
									.toString());
							displayDialog.show();
							ImageView image = (ImageView) displayDialog
									.findViewById(R.id.photoimageview);

							// create a jpg file that can be used to store the
							// current image byte array
							File imageFile = new File("/sdcard/DCIM/"
									+ "display"
									+ String.valueOf(mediaClicked.getMediaId())
									+ ".jpg");
							Log.e(TAG, "File = " + imageFile.toString());
							try {
								imageFile.createNewFile();
								// write the byte array to a FileOutputStream
								// which gets saved to the imageFile
								FileOutputStream fos = new FileOutputStream(
										imageFile);
								fos.write(mediaClicked.getBlob());
								fos.flush();
								fos.close();
							} catch (IOException ex) {
								Log.e(TAG, "IO problem Yo");
							} catch (Exception e) {
								Log.e(TAG,
										"Converting from array to file has problem");
							}

							// Call the decode method to convert a jpg file to a
							// bitmap
							Bitmap bmp = decodeFile(imageFile);
							// Scale down the bitmap to be a thumbnail
							bmp = Bitmap.createScaledBitmap(bmp, 640, 480,
									false);
							image.setImageBitmap(bmp);

							System.gc();
						}
					});

					// edit a media's properties such as name, description and
					// tags
					editButton.setOnClickListener(new View.OnClickListener() {
						public void onClick(View v) {
							final Dialog editD = new Dialog(v.getContext());
							editD.setContentView(R.layout.photo_edit_dialog);
							editD.show();
							db.open();
							Media m = null;

							Button saveButton = (Button) editD
									.findViewById(R.id.photoSave);
							Button cancelButton = (Button) editD
									.findViewById(R.id.photoCancel);

							final EditText noteName = (EditText) editD
									.findViewById(R.id.editPhotoName);
							final EditText noteDescription = (EditText) editD
									.findViewById(R.id.editPhotoDescription);
							final EditText noteTag = (EditText) editD
									.findViewById(R.id.editPhotoTags);

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
														"Photo updated successfully",
														Toast.LENGTH_LONG);
											else
												toast = Toast.makeText(
														v.getContext(),
														"Photo update failed, try again.",
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
										"Photo deleted successfully",
										Toast.LENGTH_LONG);
							else
								toast = Toast.makeText(v.getContext(),
										"Delete photo failed, try again.",
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
									File imageFile = new File("/sdcard/DCIM/"
											+ "display"
											+ String.valueOf(mediaClicked
													.getMediaId()) + ".jpg");
									Log.e(TAG, "File = " + imageFile.toString());
									try {
										imageFile.createNewFile();
										// write the byte array to a
										// FileOutputStream which gets saved to
										// the imageFile
										FileOutputStream fos = new FileOutputStream(
												imageFile);
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

									shareIt(Uri.fromFile(imageFile));
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
		Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
		// sharingIntent.setType("text/plain");
		sharingIntent.setType("image/jpeg");
		String shareBody = "Hay, here is some of the cool files I can share using the Travel Diary System";
		sharingIntent.putExtra(android.content.Intent.EXTRA_STREAM, uri);
		sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
				"Travel Diary Share");
		sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
		startActivity(Intent.createChooser(sharingIntent, "Share via"));
	}

	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {
			if (width > height) {
				inSampleSize = Math.round((float) height / (float) reqHeight);
			} else {
				inSampleSize = Math.round((float) width / (float) reqWidth);
			}
		}
		return inSampleSize;
	}

	public static Bitmap decodeSampledBitmapFromByteArray(byte[] b,
			int reqWidth, int reqHeight) {

		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeByteArray(b, 0, b.length, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth,
				reqHeight);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeByteArray(b, 0, b.length, options);
	}

	// public void onBackPressed(){
	// setResult(PhotoCategoryActivity.RESULT_CANCELED, new Intent());
	// finish();
	// }

	/**
	 * The purpose of this program is to develop an image adapter based on
	 * BaseAdapter which will present photos that then can be used by a grid
	 * view
	 * 
	 * @peerReview
	 * @date 06/09/12
	 * @name ImageAdapter
	 * @author Mostafa Alwash, June Cui
	 */
	public class ImageAdapter extends BaseAdapter {
		private Context mContext;
		private Bitmap[] imageList;
		private Integer[] mThumbIds;// variable to store a photo's ID

		public ImageAdapter(Context c) {
			mContext = c;

			// Create a database instance and retrieve all media
			DBAdapter db = new DBAdapter(mContext);
			db.open();

			ArrayList<Media> resultList = new ArrayList<Media>();
			// get array of photo objects
			resultList = db.getLooseMedia("photo");
			Log.e(TAG, resultList.size() + " loose photos retrieved!~!~!~");

			// create a Bitmap array to store thumbnail bitmaps
			imageList = new Bitmap[resultList.size()];
			// create a integer array to store all photo IDs
			mThumbIds = new Integer[resultList.size()];

			// Iterate through each object and convert the blob data to bitmap
			// thumbnails
			for (int i = 0; i < resultList.size(); i++) {
				// get the blob data of the photo
				byte[] imageByte = resultList.get(i).getBlob();
				// get the media ID of the current photo
				mThumbIds[i] = resultList.get(i).getMediaId();

				// create a jpg file that can be used to store the current image
				// byte array
				File imageFile = new File("/sdcard/DCIM/" + String.valueOf(i)
						+ ".jpg");
				try {
					imageFile.createNewFile();
					// write the byte array to a FileOutputStream which gets
					// saved to the imageFile
					FileOutputStream fos = new FileOutputStream(imageFile);
					fos.write(imageByte);
					fos.flush();
					fos.close();
				} catch (IOException ex) {
					Log.e(TAG, "IO problem Yo");
				} catch (Exception e) {
					Log.e(TAG, "Converting from array to file has problem");
				}

				// Call the decode method to convert a jpg file to a bitmap
				Bitmap bmp = decodeFile(imageFile);
				// Scale down the bitmap to be a thumbnail
				bmp = Bitmap.createScaledBitmap(bmp, THUMBNAIL_SIZE,
						THUMBNAIL_SIZE, false);
				// add the thumbnail to the imageList
				imageList[i] = bmp;
			}

			db.close();
		}

		// To get the total number of photos
		public int getCount() {
			return imageList.length;
		}

		// To get a photo (A Bitmap item)
		public Object getItem(int position) {
			return imageList[position];
		}

		// To get the photo ID
		public long getItemId(int position) {
			return mThumbIds[position];
		}

		// create a new ImageView for each photo item referenced by the Adapter
		public View getView(int position, View convertView, ViewGroup parent) {
			ImageView imageView;
			if (convertView == null) { // if it's not recycled, initialize some
										// attributes
				imageView = new ImageView(mContext);
				// set the size of the image to display on the grid
				imageView.setLayoutParams(new GridView.LayoutParams(
						THUMBNAIL_SIZE, THUMBNAIL_SIZE));
				imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
				imageView.setPadding(8, 8, 8, 8);
			} else {
				imageView = (ImageView) convertView;
			}

			imageView.setImageBitmap(imageList[position]);
			imageView.setId(mThumbIds[position]);
			return imageView;
		}

	}

	// convert a File item to Bitmap format
	private Bitmap decodeFile(File f) {
		try {
			// Decode image size
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(new FileInputStream(f), null, o);

			// The new size we want to scale to
			final int REQUIRED_SIZE = 70;

			// Find the correct scale value. It should be the power of 2.
			int scale = 1;
			while (o.outWidth / scale / 2 >= REQUIRED_SIZE
					&& o.outHeight / scale / 2 >= REQUIRED_SIZE)
				scale *= 2;

			// Decode with inSampleSize
			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;
			return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
		} catch (FileNotFoundException e) {
		}
		return null;
	}
}
