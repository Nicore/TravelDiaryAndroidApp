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

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.*;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.*;

public class SearchFriendActivity extends Activity {
	Button send, cancel;
	EditText address, subject, message;
	private static final String PREFS_NAME = "MyPrefsFile";
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_friend);
		
		final TableLayout tLayout = (TableLayout) findViewById(R.id.table_friend_search);
        //Button searchButton = (Button) findViewById(R.id.btn_friend_search); 
        //final EditText searchText = (EditText) findViewById(R.id.txt_friend_search);
        
        DBAdapter db = new DBAdapter(this);
        Registrar rs = new Registrar(db);
        
        db.open();
        Collection<Account> resultList = rs.getAllAccounts();
        Collection<String> myFriends = db.getFriends();
        db.close();
        
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		final String theUser = settings.getString("user", "");
        
        try{
        for(Account account : resultList){
        	
        	if(!account.getUsername().equals(theUser)){
        		if(!myFriends.contains(account.getUsername())){
	        	TableRow tRow = new TableRow(this);
	        	final TextView accountTitle = new TextView(this);
	        	accountTitle.setText(account.getUsername());
	        	
	        	accountTitle.setOnClickListener(new View.OnClickListener() {
	        		public void onClick(View v) {
	        			DBAdapter db = new DBAdapter(v.getContext());
	        			db.open();
	        			
	        			db.insertFriend(theUser, accountTitle.getText().toString(), new Timestamp(new Date().getTime()).toString(), "n");
	        			
	        			Toast toast = Toast.makeText(v.getContext(), "Added Friend!", Toast.LENGTH_SHORT);
	        			toast.show();
	            		finish();
	        			db.close();
	        		}
	        		});
        	
        	tRow.addView(accountTitle);
        	tLayout.addView(tRow);
        	}}
        } 
        } catch (Exception e){
        	Toast toast = Toast.makeText(this, "Server issue, please retry!", Toast.LENGTH_SHORT);
    		toast.show();
        }
        
        /*searchButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				
				//clear table contents
				int count = tLayout.getChildCount();
				for (int i = 0; i < count; i++) {
				    View child = tLayout.getChildAt(i);
				    if (child instanceof TableRow) ((ViewGroup) child).removeAllViews();
				}
				
				//Get new instance of server accounts
				//resultList = server.getAccounts();
				try{
					for(int i = 0; i< resultList.size(); i++){
			        	
			        	if(resultList.get(i).getEmail().contains(searchText.getText().toString().toLowerCase()) ||
			        			resultList.get(i).getFirstName().contains(searchText.getText().toString().toLowerCase()) ||
			        				resultList.get(i).getLastName().contains(searchText.getText().toString().toLowerCase()) ||
			        					resultList.get(i).getUsername().contains(searchText.getText().toString().toLowerCase()) ||
			        						resultList.get(i).getEmail().contains(searchText.getText().toString().toUpperCase()) ||
			        		        			resultList.get(i).getFirstName().contains(searchText.getText().toString().toUpperCase()) ||
			        		        				resultList.get(i).getLastName().contains(searchText.getText().toString().toUpperCase()) ||
			        		        					resultList.get(i).getUsername().contains(searchText.getText().toString().toUpperCase()) 
			        			){
			        	TableRow tRow = new TableRow(v.getContext());
			        	tRow.setId(i);
			        	TextView accountTitle = new TextView(v.getContext());
			        	accountTitle.setText(resultList.get(i).getFirstName() + " " + resultList.get(i).getLastName() +" ("+resultList.get(i).getUsername()+")");
			        	
			        	tRow.addView(accountTitle);
			        	tLayout.addView(tRow);
			        	} else {
			        		Toast toast = Toast.makeText(v.getContext(), "No Friends Found!", Toast.LENGTH_SHORT);
			        		toast.show();
			        	}
		        	} 
				} catch (Exception e){
					Toast toast = Toast.makeText(v.getContext(), "Server issue, please retry!", Toast.LENGTH_SHORT);
		    		toast.show();
				}
			}
        });*/
	
       
        
	}
}
