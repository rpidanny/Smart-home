package com.abhishek.smarthome;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;
public class OtherLights extends Activity {

	
	ViewPager viewpager=null;
	private static final int RESULT_SETTINGS = 0;
	
	public String jarvisIP = null;
	public static String ServerIpAddress= null;
	public static String videolink=null;
	public  String user=null;
	public  String pwd=null;
	
	public final static String EXTRA_MESSAGE = "com.example.jarvis.MESSAGE";
	public final static String EXTRA_MESSAGE_UN = "com.example.jarvis.MESSAGEUN";
	public final static String EXTRA_MESSAGE_PW = "com.example.jarvis.MESSAGEPW";
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_other_lights);

		showUserSettings();
		
	    String url = ServerIpAddress +":8000/misc_lights.html";
	        
	    WebView wv = (WebView)findViewById(R.id.webViewLights);
	    wv.getSettings().setJavaScriptEnabled(true);
	    wv.loadUrl(url);
	    wv.setWebViewClient(new WebViewClient() {
	    @Override
	    public boolean shouldOverrideUrlLoading(WebView view, String url) {
	         view.loadUrl(url);
	         return true;
	         }
	    });
	        
	        
	}
	
	
	//Preference
			@Override
			public boolean onCreateOptionsMenu(Menu menu) {
			   getMenuInflater().inflate(R.menu.room1, menu);
			     //menu.add(1, 1, 0, "Connect");
			     //menu.add(1, 2, 1, "Exit");
				return true;
			}
			
			@Override
		    public boolean onOptionsItemSelected(MenuItem item)
		    {
		    
		     switch(item.getItemId())
		     {
		     case R.id.menu_jarvis:
		    	 Intent jarvis= new Intent(OtherLights.this,Jarvis.class);
					
			    	//tts.speak("Connecting to "+ message, TextToSpeech.QUEUE_ADD, null);
			    	jarvis.putExtra(EXTRA_MESSAGE, jarvisIP);
			    	jarvis.putExtra(EXTRA_MESSAGE_UN, user);
			    	jarvis.putExtra(EXTRA_MESSAGE_PW, pwd);
					startActivity(jarvis);
		      return true;
		     case R.id.action_settings:
					Intent i = new Intent(this, UserSettingActivity.class);
					startActivityForResult(i, RESULT_SETTINGS);
					break;

		     }
		     return super.onOptionsItemSelected(item);

		    }
			
			@Override
			protected void onActivityResult(int requestCode, int resultCode, Intent data) {
				super.onActivityResult(requestCode, resultCode, data);

				switch (requestCode) {
				case RESULT_SETTINGS:
					showUserSettings();
					break;

				}
			}

			private void showUserSettings() {
				SharedPreferences sharedPrefs = PreferenceManager
						.getDefaultSharedPreferences(this);
				jarvisIP= sharedPrefs.getString("prefIpaddress", "NULL");
				ServerIpAddress = "http://"+jarvisIP;
				videolink = "http://"+sharedPrefs.getString("prefVideo", "NULL");
				
				//port=Integer.parseInt(sharedPrefs.getString("prefPort", "22"));
				//host="192.168.1.10";
				user =sharedPrefs.getString("prefUser", "NULL");
				pwd = sharedPrefs.getString("prefPassword", "NULL");
				

			}

	
}
