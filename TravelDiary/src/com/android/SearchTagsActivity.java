/**
 * The purpose of this program is to provide the operation for
 * the current user to search for a friend registered on the Travel Diary System
 * 
 * @peerReview
 * @date 18/07/12
 * @name SearchFriendActivity
 * @author Mostafa Alwash
 */
package com.android;

import java.util.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;

import sync.Registrar;

import domain.Account;
import domain.Tag;

import android.annotation.SuppressLint;
import android.app.*;
import android.content.*;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.*;
import android.widget.TabHost.TabSpec;

public class SearchTagsActivity extends Activity {

	private static final String TAG = "SearchTagsActivity";
	private static final String PREFS_NAME = "MyPrefsFile";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_tags);

		Button searchButton = (Button) findViewById(R.id.btn_tag_search);

		searchButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				TableLayout tLayout = (TableLayout) findViewById(R.id.table_tag_search);

				EditText searchText = (EditText) findViewById(R.id.txt_tag_search);
				DBAdapter db = new DBAdapter(v.getContext());

				SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
				String user = settings.getString("user", "");

				db.open();
				ArrayList<Tag> resultList = new ArrayList<Tag>();
				ArrayList<Tag> tripList = db.getNewTripTagsByUser(user);
				ArrayList<Tag> eventList = db.getNewEventTagsByUser(user);
				ArrayList<Tag> mediaList = db.getNewMediaTagsByUser(user);
				Log.e(TAG, "MEDIALIST=" + mediaList.toString());
				resultList.addAll(tripList);
				resultList.addAll(eventList);
				resultList.addAll(mediaList);
				Log.e(TAG, "RESULTLIST=" + resultList.toString());
				db.close();

				try {
					for (int i = 0; i < resultList.size(); i++) {
						TableRow tRow = null;
						final TextView tagTitle = new TextView(v.getContext());
						;
						if (resultList.get(i).getText()
								.contains(searchText.getText())) {
							tagTitle.setId(resultList.get(i).getTagId());
							tRow = new TableRow(v.getContext());
							tagTitle.setText(resultList.get(i).getText() + " ("
									+ resultList.get(i).getType() + ")");
							tRow.addView(tagTitle);
							tLayout.addView(tRow);
						}
					}
				} catch (Exception e) {
					Log.e(TAG, e.toString());
					Toast toast = Toast.makeText(v.getContext(),
							"Server issue, please retry!", Toast.LENGTH_SHORT);
					toast.show();
				}
			}
		});

	}

	/* Called when the back button is pressed */
	public void onBackPressed() {
		setResult(SearchTagsActivity.RESULT_CANCELED, new Intent());
		finish();
	}
}
