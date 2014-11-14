package com.android;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

import domain.Event;
import domain.Media;

public class MyItemizedOverlay extends ItemizedOverlay<OverlayItem> {
	private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();
	private Context mContext;
	private static final String TAG = "MyItemizedOverlay";

	public MyItemizedOverlay(Drawable defaultMarker, Context context) {
		super(boundCenterBottom(defaultMarker));
		mContext = context;
		// TODO Auto-generated constructor stub
	}

	// add an overlayItem to the Overlay
	public void addOverlay(OverlayItem overlay) {
		mOverlays.add(overlay);
		populate();
	}

	@Override
	protected OverlayItem createItem(int i) {
		// TODO Auto-generated method stub
		return mOverlays.get(i);
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return mOverlays.size();
	}

	/*
	 * When user tap the marker on the map, a dialog displays some message of
	 * the marker
	 */
	@Override
	protected boolean onTap(int index) {
		// Log.e("MyItemizedOverlay", "Tap index= "+index);
		OverlayItem item = mOverlays.get(index);
		// Log.e("MyItemizedOverlay", "Tap index= "+item.getTitle());

		try {
			if (item.getTitle().equals("Current Coordinate")
					|| item.getTitle().contains("Coordinate")
					|| item.getTitle().equals("Me")) {

			} else if (item.getTitle().equals("The Rainforest Restaurant")
					|| item.getTitle().equals(
							"Tokyo Garden Japanese Restaurant")
					|| item.getTitle().equals("Vino Vena Restaurant")
					|| item.getTitle().equals("Table Se7en Restaurant And Bar")
					|| item.getTitle().equals(
							"The Reef Seafood Restaurant & Bar")
					|| item.getTitle().equals("Westpac")
					|| item.getTitle().equals("BNZ")
					|| item.getTitle().equals("Rabobank Dunedin")
					|| item.getTitle().equals("Credit Union Sounth as NZCSR")
					|| item.getTitle().equals("ANZ")
					|| item.getTitle().equals("Jizo Japanese Cafe & Bar")
					|| item.getTitle().equals("Ironic Cafe & Bar")
					|| item.getTitle().equals("Ombrellos Cafe & Bar")
					|| item.getTitle().equals("Stock X Change Bar")
					|| item.getTitle().equals("Eureka Cafe & Bar")
					|| item.getTitle().equals("Scenic Hotel Dunedin City")
					|| item.getTitle().equals("Mercure Dunedin")
					|| item.getTitle().equals("Bluestone On George")
					|| item.getTitle().equals("Quality Hotel Cargills")
					|| item.getTitle().equals("LivingSpace Dunedin")
					|| item.getTitle().equals("Caltex City North")
					|| item.getTitle().equals("BP 2GO Regent")
					|| item.getTitle().equals("BP 2GO Dunedin North")
					|| item.getTitle().equals("Instone Motors")
					|| item.getTitle().equals("Mobi North Gate")
					|| item.getTitle().equals("PAK'nSAVE Dunedin")
					|| item.getTitle().equals("Woolworths-Andersons Bay")
					|| item.getTitle().equals("The Warehouse")
					|| item.getTitle().equals("Four Square Caversham")
					|| item.getTitle().equals("Middleton Road Food Centre")) {
				AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
				dialog.setTitle(item.getTitle());
				dialog.setMessage(item.getSnippet());
				dialog.show();
				return true;

			} else {
				int eventId = Integer.parseInt(item.getTitle());

				Dialog dialog = new Dialog(mContext);
				dialog.setContentView(R.layout.overlayitem_option);
				TableLayout list = (TableLayout) dialog
						.findViewById(R.id.overlayitemlist);
				dialog.setTitle(item.getTitle());
				dialog.show();
				final DBAdapter db = new DBAdapter(mContext);
				db.open();
				ArrayList<Media> resultRetrieved = db
						.getMediasByEventId(eventId);
				db.close();
				Log.e("MyItemizedOverlay", "resultRetrieved Size = "
						+ resultRetrieved.size());

				for (int i = 0; i < resultRetrieved.size(); i++) {
					final TableRow r = new TableRow(mContext);
					r.setId(resultRetrieved.get(i).getMediaId());
					TextView mediaTitle = new TextView(mContext);
					mediaTitle.setText(resultRetrieved.get(i).getName()
							.toString());
					TextView mediaType = new TextView(mContext);
					mediaType.setText(" (" + resultRetrieved.get(i).getType()
							+ ") ");

					mediaTitle.setOnClickListener(new View.OnClickListener() {
						public void onClick(View v) {
							Dialog d = new Dialog(v.getContext());

							d.setContentView(R.layout.loose_media_dialog);
							d.setTitle("Media Options");
							d.show();

							Button showButton = (Button) d
									.findViewById(R.id.mediaShow);
							Button editButton = (Button) d
									.findViewById(R.id.mediaEdit);
							Button sharingButton = (Button) d
									.findViewById(R.id.mediaShare);
							Button deleteButton = (Button) d
									.findViewById(R.id.mediaDelete);

							// display the photo in full screen
							showButton
									.setOnClickListener(new View.OnClickListener() {

										public void onClick(View v) {

											int mediaId = r.getId();

											db.open();
											final Media mediaClicked = db
													.getMediaById(mediaId);
											db.close();

											if (mediaClicked.getType().equals(
													"photo")) {

												Dialog displayDialog = new Dialog(
														v.getContext());
												displayDialog
														.setContentView(R.layout.photo_display);

												displayDialog
														.setTitle(mediaClicked
																.getName()
																.toString());
												displayDialog.show();
												ImageView image = (ImageView) displayDialog
														.findViewById(R.id.photoimageview);

												// create a jpg file that can be
												// used to store the current
												// image byte array
												File imageFile = new File(
														"/sdcard/DCIM/"
																+ "display"
																+ String.valueOf(mediaClicked
																		.getMediaId())
																+ ".jpg");
												Log.e(TAG, "File = "
														+ imageFile.toString());
												try {
													imageFile.createNewFile();
													// write the byte array to a
													// FileOutputStream which
													// gets saved to the
													// imageFile
													FileOutputStream fos = new FileOutputStream(
															imageFile);
													fos.write(mediaClicked
															.getBlob());
													fos.flush();
													fos.close();
												} catch (IOException ex) {
													Log.e(TAG, "IO problem Yo");
												} catch (Exception e) {
													Log.e(TAG,
															"Converting from array to file has problem");
												}

												// Call the decode method to
												// convert a jpg file to a
												// bitmap
												Bitmap bmp = decodeFile(imageFile);
												// Scale down the bitmap to be a
												// thumbnail
												bmp = Bitmap
														.createScaledBitmap(
																bmp, 640, 480,
																false);
												image.setImageBitmap(bmp);

												System.gc();
											} else if (mediaClicked.getType()
													.equals("audio")) {

												final Dialog displayDialog = new Dialog(
														v.getContext());
												displayDialog
														.setContentView(R.layout.mic_playback_display);
												displayDialog
														.setTitle(mediaClicked
																.getName()
																.toString());
												displayDialog.show();

												Button playButton = (Button) displayDialog
														.findViewById(R.id.btn_mic_playback_display_play);
												Button closeButton = (Button) displayDialog
														.findViewById(R.id.btn_mic_playback_display_close);

												playButton
														.setOnClickListener(new View.OnClickListener() {
															public void onClick(
																	View v) {
																playMp3(mediaClicked
																		.getBlob());
															}
														});

												closeButton
														.setOnClickListener(new View.OnClickListener() {
															public void onClick(
																	View v) {
																displayDialog
																		.dismiss();
															}
														});

											} else if (mediaClicked.getType()
													.equals("text")) {
												final Dialog displayDialog = new Dialog(
														v.getContext());
												displayDialog
														.setContentView(R.layout.note_display);

												EditText noteName = (EditText) displayDialog
														.findViewById(R.id.txt_note_display_name);
												EditText noteDescription = (EditText) displayDialog
														.findViewById(R.id.txt_note_display_description);
												Button closeButton = (Button) displayDialog
														.findViewById(R.id.btn_note_display_close);

												noteName.setText(mediaClicked
														.getName());
												// Dont know why type is
												// description but it works
												noteDescription.setText(mediaClicked
														.getDescription());

												displayDialog
														.setTitle(mediaClicked
																.getName()
																.toString());
												displayDialog.show();

												closeButton
														.setOnClickListener(new View.OnClickListener() {
															public void onClick(
																	View v) {
																displayDialog
																		.dismiss();
															}
														});
											}
										}
									});

							sharingButton
									.setOnClickListener(new View.OnClickListener() {
										public void onClick(View v) {
										}
									});

							deleteButton
									.setOnClickListener(new View.OnClickListener() {
										public void onClick(View v) {

											int mediaId = r.getId();

											db.open();
											int mediaResult = db
													.deleteMedia(mediaId);

											Toast toast;
											if (mediaResult > 0)
												toast = Toast.makeText(
														v.getContext(),
														"Media deleted successfully",
														Toast.LENGTH_LONG);
											else
												toast = Toast.makeText(
														v.getContext(),
														"Delete media failed, try again.",
														Toast.LENGTH_LONG);
											toast.show();

											db.close();
										}
									});

							editButton
									.setOnClickListener(new View.OnClickListener() {
										public void onClick(View v) {

											final Dialog editD = new Dialog(v
													.getContext());
											editD.setContentView(R.layout.photo_edit_dialog);
											editD.show();
											db.open();
											Media m = null;

											int mediaId = r.getId();

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
														m.getMediaId()
																+ " "
																+ m.getPoiId()
																+ ""
																+ m.getName()
																+ " "
																+ m.getDescription()
																+ " "
																+ m.getType()
																+ " "
																+ m.getUpdatetime());
											} catch (Exception error) {
												Log.e(TAG, "DB media error,"
														+ error.toString());
											}

											// display existing value in the
											// edit text box
											noteName.setText(m.getName()
													.toString());
											noteDescription.setText(m
													.getDescription()
													.toString());

											ArrayList<String> tags = db
													.getTagsByMediaId(m
															.getMediaId());
											String allTags = "";
											// if there are tags for this media,
											// display them in the tags edit box
											if (tags.size() != 0) {
												// Concatenate all strings in
												// the Tags array list to a
												// single string with comma
												// separated
												for (int i = 0; i < tags.size(); i++) {
													if (i < tags.size() - 1)
														allTags += tags.get(i)
																.toString()
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
														public void onClick(
																View v) {
															// Get values out of
															// dialog
															Media newM = new Media(
																	tempMedia
																			.getMediaId(),
																	noteName.getText()
																			.toString(),
																	noteDescription
																			.getText()
																			.toString(),
																	tempMedia
																			.getType(),
																	tempMedia
																			.getBlob(),
																	tempMedia
																			.getPoiId(),
																	new Timestamp(
																			new Date()
																					.getTime()),
																	"U");
															// Update the Media
															// table with name,
															// description, tags
															// and updatetime
															int mediaResult = db
																	.updateMedia(newM);

															Toast toast;
															if (mediaResult > 0)
																toast = Toast
																		.makeText(
																				v.getContext(),
																				"Media updated successfully",
																				Toast.LENGTH_LONG);
															else
																toast = Toast
																		.makeText(
																				v.getContext(),
																				"Media update failed, try again.",
																				Toast.LENGTH_LONG);
															toast.show();

															db.close();
															editD.dismiss();
														}
													});

											// cancel editing
											cancelButton
													.setOnClickListener(new View.OnClickListener() {
														public void onClick(
																View v) {
															db.close();
															editD.dismiss();
														}
													});
										}
									});

						}
					});

					r.addView(mediaTitle);
					r.addView(mediaType);
					list.addView(r);

				}

				// dialog.setMessage(item.getSnippet());
				dialog.show();
			}
		} catch (Exception e) {

		}
		return true;
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

	private void playMp3(byte[] mp3SoundByteArray) {
		try {
			// create temp file that will hold byte array
			File tempMp3 = File.createTempFile("kurchina", "mp3",
					mContext.getCacheDir());
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
}