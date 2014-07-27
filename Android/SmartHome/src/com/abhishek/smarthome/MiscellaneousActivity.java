package com.abhishek.smarthome;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;


import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;


public class MiscellaneousActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_miscellaneous);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.miscellaneous, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_miscellaneous,
					container, false);
			
			 Button btnLights=(Button)rootView.findViewById(R.id.btnLightsX);
				btnLights.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View arg0) {
					
						Intent i = new Intent(getActivity(), OtherLights.class);
						startActivityForResult(i, 0);
					}
					
					
					
				});
				
				
				
				 Button btnDoorOpen=(Button)rootView.findViewById(R.id.btnMainGateOpen);
				 btnDoorOpen.setOnClickListener(new OnClickListener(){

						@Override
						public void onClick(View arg0) {
						
							OpenDoor();
							 Toast.makeText(getActivity(), "Door Open", Toast.LENGTH_LONG).show();
						}
						
						
						
					});
					
					
					 Button btnDoorClose=(Button)rootView.findViewById(R.id.btnMainGateClose);
					 btnDoorClose.setOnClickListener(new OnClickListener(){

							@Override
							public void onClick(View arg0) {
								CloseDoor();
								 Toast.makeText(getActivity(),"Door Closed", Toast.LENGTH_LONG).show();
							}
							
							
							
						});
			
			return rootView;
		}
		
		void CloseDoor(){
			
				String URL = Room2Activity.ServerIpAddress+":8080/cgi-bin/doorControl.py?Door=Close";
				new RetrieveFeedTask().execute(URL);    
		}
		void OpenDoor(){
			
			String URL = Room2Activity.ServerIpAddress+":8080/cgi-bin/doorControl.py?Door=Open";
			new RetrieveFeedTask().execute(URL);    
	}
		
		class RetrieveFeedTask extends AsyncTask<String, Void, Void> {

			

		    protected Void doInBackground(String... urls) {
		        try {
		        	HttpClient Client = new DefaultHttpClient();
					try
		            {
					 	String SetServerString = "";
		                // Create Request to server and get response
	                    HttpGet httpget = new HttpGet(urls[0]);
	                    ResponseHandler<String> responseHandler = new BasicResponseHandler();
	                    SetServerString = Client.execute(httpget, responseHandler);
	                    Toast.makeText(getActivity(), SetServerString, Toast.LENGTH_LONG).show();
		       		     
		                      // Show response on activity 

		                       
		             }
		           catch(Exception ex)
		              {
		        	   Toast.makeText(getActivity(), ex.toString(), Toast.LENGTH_LONG).show();
		     		     
		               }
		        } catch (Exception e) {
		           
		        
		        }
				return null;
		      
		    }

		    protected void onPostExecute(String r) {
		        // TODO: check this.exception 
		        // TODO: do something with the feed
		    	 Toast.makeText(getActivity(),"Successful", Toast.LENGTH_LONG).show();
	     		   
		    }
		}
	}
	
	
	
	

}
