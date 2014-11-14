/**
 * The purpose of this program is to present a gallery for users
 * of the Travel Diary Application to view all events that they
 * have captured. It also allows them to manipulate these events
 * by deleting and editing them, it also allows for users to show
 * the content of the event or plot it on the map
 * 
 * @peerReview
 * @date 22/05/12
 * @name LooseEvents
 * @author Mostafa Alwash
 */
package com.android;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

import android.app.*;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TabHost.TabSpec;

public class LooseMedia extends TabActivity {

	private static final String TAG = "LooseMedia";

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.loose_media);

		try {

			Resources resources = getResources();// Resource object to get
													// Drawables
			TabHost tabHost = getTabHost();// The activity TabHost
			tabHost.setCurrentTab(0);// set the default tab to photo tab, this
										// is zero based property.

			// Photo category
			// Create an intent to launch an Activity for the photo tab
			Intent intentPhoto = new Intent().setClass(this,
					PhotoCategoryActivity.class);
			// Initialise a TabSpec for the photo tab
			TabSpec tabSpecPhoto = tabHost
					.newTabSpec("Photo")
					.setIndicator("",
							resources.getDrawable(R.drawable.cam_button))
					.setContent(intentPhoto);

			// Audio category
			// Create an intent to launch an Activity for the audio tab
			Intent intentAudio = new Intent().setClass(this,
					AudioCategoryActivity.class);
			// Initialise a TabSpec for the audio tab
			TabSpec tabSpecAudio = tabHost
					.newTabSpec("Audio")
					.setIndicator("",
							resources.getDrawable(R.drawable.mic_button))
					.setContent(intentAudio);

			// Note category
			// Create an intent to launch an Activity for the note tab
			Intent intentNote = new Intent().setClass(this,
					NoteCategoryActivity.class);
			// Initialise a TabSpec for the note tab
			TabSpec tabSpecNote = tabHost
					.newTabSpec("Note")
					.setIndicator("",
							resources.getDrawable(R.drawable.pad_button))
					.setContent(intentNote);

			// add all above tabs to the tabHost.
			tabHost.addTab(tabSpecPhoto);
			tabHost.addTab(tabSpecAudio);
			tabHost.addTab(tabSpecNote);

		} catch (Exception e) {
			Log.w(TAG, e.toString());
		}
	}

	/* Called when the back button is pressed */
	public void onBackPressed() {
		setResult(LooseMedia.RESULT_CANCELED, new Intent());
		finish();
	}
}
