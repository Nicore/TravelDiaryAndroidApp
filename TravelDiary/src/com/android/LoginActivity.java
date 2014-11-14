/**
 * The purpose of this program is to present the Login Screen Activity
 * to user to allow them to Login to he system and Register if they have not
 * 
 * @peerReview
 * @date 22/05/12
 * @name LoginActivity
 * @author June Cui
 */
package com.android;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import sync.Registrar;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity {

	private static final String PREFS_NAME = "MyPrefsFile";

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);

		TextView registerScreen = (TextView) findViewById(R.id.link_to_register);
		TextView mainMapScreen = (TextView) findViewById(R.id.btnLogin);
		final EditText username = (EditText) findViewById(R.id.txt_login_username);

		/* Listening to login button */
		mainMapScreen.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// Switch to Main Map screen
				DBAdapter db = new DBAdapter(v.getContext());
				db.open();
				Registrar rs = new Registrar(db);

				ArrayList<String> accounts = db.getAccount();

				if (rs.checkUsername(username.getText().toString()) == 1
						|| accounts.contains(username.getText().toString())) {
					SharedPreferences settings = getSharedPreferences(
							PREFS_NAME, 0);
					SharedPreferences.Editor editor = settings.edit();
					editor.remove("user");
					editor.commit();
					editor.putString("user", username.getText().toString());
					editor.commit();

					db.close();
					Intent i = new Intent(getApplicationContext(),
							MainMapActivity.class);
					startActivity(i);
				} else {
					Toast.makeText(v.getContext(),
							"Please enter valid credentials", Toast.LENGTH_LONG)
							.show();
				}
			}
		});

		/* Listening to register new account link */
		registerScreen.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// Switching to Register screen
				Intent i = new Intent(getApplicationContext(),
						RegisterActivity.class);
				startActivity(i);
			}
		});
	}

}