package com.abhishek.smarthome;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;



public class Room1Activity extends FragmentActivity {

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
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_room1);
		showUserSettings();
		viewpager=(ViewPager)findViewById(R.id.pager);
		FragmentManager fragmentmanager = getSupportFragmentManager();
		viewpager.setAdapter(new MyAdapter(fragmentmanager));
		
	}

	
	
	class MyAdapter extends FragmentStatePagerAdapter {

		public MyAdapter(FragmentManager fm) {
			super(fm);
			// TODO Auto-generated constructor stub
		}

		@Override
		public Fragment getItem(int arg0) {
			// TODO Auto-generated method stub
			Fragment fragment=null;
			if(arg0 ==0){
				
				fragment= new buttons();
				return fragment;
			}
			else if(arg0 ==1){
				
				fragment= new temperature();
				return fragment;
			}
			/*else if(arg0 ==2){
				fragment= new color();
				return fragment;
	
					}*/
			else if(arg0 ==2){
				fragment= new voice();
				return fragment;
	
					}
			return fragment;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return 3;
		}
		
		public CharSequence getPageTitle(int arg0){
			String title=new String();
			if(arg0 ==0){
				
			
				return "Devices";
			}
			else if(arg0 ==1){
				
				
				return "Temperature";
			}
			/*else if(arg0 ==2){
			
				return "Color Chooser";
	
					}*/
			else if(arg0 ==2){
				
				return "Video";
	
					}
			return null;
			
		}
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
	    	 Intent jarvis= new Intent(Room1Activity.this,Jarvis.class);
				
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
