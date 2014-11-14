/**
 * The purpose of this program is to provide the operation for
 * the current user to send an email to someone he know to invite 
 * the person to use the Travel Diary System
 * 
 * @peerReview
 * @date 18/07/12
 * @name EmailFriendActivity
 * @author June Cui
 */
package com.android;

import android.app.Activity;
import android.content.*;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;

public class EmailFriendActivity extends Activity {
	Button send, cancel;
	EditText address, subject, message;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.email_friend);
		send = (Button) findViewById(R.id.buttonSend);
		cancel = (Button) findViewById(R.id.buttonCancel);
		address = (EditText) findViewById(R.id.editTextTo);
		subject = (EditText) findViewById(R.id.editTextSubject);
		message = (EditText) findViewById(R.id.editTextMessage);

		// when click on the send button, send the email
		send.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub
				final Intent emailIntent = new Intent(
						android.content.Intent.ACTION_SEND);
				emailIntent.setType("plain/text");
				emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,
						new String[] { address.getText().toString() });
				emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
						subject.getText());
				emailIntent.putExtra(android.content.Intent.EXTRA_TEXT,
						message.getText());
				startActivity(Intent.createChooser(emailIntent,
						"Email to Friend"));
			}
		});

		// Listening to the cancel button, so it go back to the main map screen
		cancel.setOnClickListener(new View.OnClickListener() {

			public void onClick(View arg0) {
				// Switching to Login Screen
				Intent i = new Intent(getApplicationContext(),
						MainMapActivity.class);
				startActivity(i);
			}
		});
	}
}
