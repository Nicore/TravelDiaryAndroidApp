package com.android;

import java.sql.Timestamp;
import java.util.Date;

import sync.Registrar;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterActivity extends Activity {

	/*
	 * public static final String ACCOUNT_TYPE = Constants.ACCOUNT_TYPE; public
	 * static final String AUTHTOKEN_TYPE = Constants.AUTHTOKEN_TYPE; public
	 * static final String AUTH_PROVIDER = "com.android.accprovider"; public
	 * static final String PARAM_AUTHTOKEN_TYPE = "auth.token";
	 */public static final String TAG = "RegisterActivity";

	// public AccountManager accMgr;

	/** Called when the activity is first created. */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);

		// accMgr = AccountManager.get(this);
		TextView loginScreen = (TextView) findViewById(R.id.link_to_login);
		Button registerButton = (Button) findViewById(R.id.btnRegister);
		final TextView txtUsername = (TextView) findViewById(R.id.txt_login_username);

		// Listening to Login Screen link
		loginScreen.setOnClickListener(new View.OnClickListener() {

			public void onClick(View arg0) {
				DBAdapter db = new DBAdapter(getApplicationContext());
				Registrar regis = new Registrar(db);

				int userCheck = regis.checkUsername(txtUsername.getText()
						.toString());

				if (userCheck == 0) {
					// Switching to Login Screen
					Intent i = new Intent(getApplicationContext(),
							LoginActivity.class);
					startActivity(i);
				} else if (userCheck == 1) {
					// Switching to Login Screen
					Intent i = new Intent(getApplicationContext(),
							LoginActivity.class);
					startActivity(i);
				}
			}
		});

		// Listening to Register button

		registerButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View arg0) {

				TextView registerFirstName = (TextView) findViewById(R.id.reg_firstname);
				TextView registerLastName = (TextView) findViewById(R.id.reg_lastname);
				TextView registerEmail = (TextView) findViewById(R.id.reg_email);
				TextView registerUsername = (TextView) findViewById(R.id.reg_username);
				TextView registerPassword = (TextView) findViewById(R.id.reg_password);

				String fname = registerFirstName.getText().toString();
				String lname = registerLastName.getText().toString();
				String email = registerEmail.getText().toString();
				String uname = registerUsername.getText().toString();
				String pword = registerPassword.getText().toString();

				if (fname.equals("")
						|| // if there's something missing
						lname.equals("") || email.equals("")
						|| uname.equals("") || pword.equals("")) {
					Toast toast = Toast.makeText(getApplicationContext(),
							"Please fill in all information",
							Toast.LENGTH_SHORT);
					toast.show();
				} else { // otherwise everything is filled in
					DBAdapter db = new DBAdapter(getApplicationContext());

					Registrar regis = new Registrar(db); // register online,
															// comment anything
															// with 'regis' out
															// if things start
															// failing

					int regisResult = regis.registerUser(uname, pword, email,
							fname, lname,
							new Timestamp(new Date().getTime()).toString(), "n");

					if (regisResult == 0) {
						Toast toast = Toast.makeText(getApplicationContext(),
								"This account has already been created",
								Toast.LENGTH_SHORT);
						toast.show();
					} else if (regisResult == -1) {
						Toast toast = Toast
								.makeText(
										getApplicationContext(),
										"Network Problem, please ensure that you are connected and retry",
										Toast.LENGTH_SHORT);
						toast.show();
					} else if (regisResult == 1) {
						db.open();
						// Need to pretty this up, just want simple strings to
						// be input

						db.insertAccount(uname, pword, email, fname, lname,
								new Timestamp(new Date().getTime()).toString(),
								"n");
						db.close();
						Toast toast = Toast.makeText(getApplicationContext(),
								"Your account has been created"/*
																 * +
																 * regisResult+
																 * "::"
																 * +regisString
																 */,
								Toast.LENGTH_SHORT);
						toast.show();
						finish();
					}
				}
			}
		});

	}
}