package com.abhishek.smarthome;




import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.wolfram.alpha.WAEngine;
import com.wolfram.alpha.WAException;
import com.wolfram.alpha.WAPlainText;
import com.wolfram.alpha.WAPod;
import com.wolfram.alpha.WAQuery;
import com.wolfram.alpha.WAQueryResult;
import com.wolfram.alpha.WASubpod;




public class Jarvis extends Activity implements OnInitListener{
	
	Properties props = new Properties(); 
	
	String host="192.168.1.4";
	String user ="pi";
	String pwd = "raspberry";
	int port = 22;
	JSch jsch=new JSch();  
	Session session;
	private static final int RESULT_SETTINGS = 0;
	
	private static String appid = "UJXGQE-LKA67YRA9V";
	
	TextView textview;
	TextView mytext;
	protected static final int RESULT_SPEECH = 1;
	public TextToSpeech tts;
	String text;
	private ImageButton btnSpeak;
	String input;
	
	boolean secondlevelflag_sleep=false;
	int marriagecount=0;
	

    String checkflag="false";
  
    boolean errorflagx=false;
    
    
    ////////jarvis preference/////
    
    String GPIO0 = "light";
    String GPIO1 = "tv";
    String GPIO2 = "computer";
    String GPIO3 = "dishwasher";
    String GPIO7 = "fan";
    String GPIO12 = "ceiling light";
    String GPIO13 = "microwave";
    String GPIO14 = "washing machine";
    
    boolean voiceflag=true;
    //////////////////////////////
    
	ProgressDialog pd;
	
	String customScript_1=null;
	String customScript_2=null;
	String customScript_3=null;
	String faceLoginScript=null;
    
    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_jarvis);

		
		Intent intent = getIntent();
		
		host = intent.getStringExtra(Room1Activity.EXTRA_MESSAGE);
		user = intent.getStringExtra(Room1Activity.EXTRA_MESSAGE_UN);
		pwd = intent.getStringExtra(Room1Activity.EXTRA_MESSAGE_PW);
		
		
		Toast.makeText(this,host +" - " +port +" - "+user+" - "+pwd, Toast.LENGTH_LONG).show();
	     
		// Upon interacting with UI controls, delay any scheduled hide()
		// operations to prevent the jarring behavior of controls going away
		// while interacting with the UI.


///////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
		tts = new TextToSpeech(this, this);
		
		textview = (TextView)findViewById(R.id.fullscreen_content);
		mytext = (TextView)findViewById(R.id.mycomment);
		
		textview.setVisibility(View.GONE);
		mytext.setVisibility(View.GONE);
		textview.setTextSize(12);
		mytext.setTextSize(12);
		text="Indefinite integral\n\nintegral sin(x) dx = -cos(x)+constant\n\n\n\nAlternate form of the integral\n\n-1/2 e^(-i x)-e^(i x)/2+constant\n\n\n\nSeries expansion of the integral at x=0\n\n-1+x^2/2-x^4/24+O(x^6)\n\n\n\nDefinite integral over a half-period\n\n integral_0^pi sin(x) dx = 2\n\n\n\nDefinite integral mean square\n\nintegral_0^(2 pi) (sin^2(x))/(2 pi) dx = 1/2";
		//textview.setText(text);
		
		textview.setMovementMethod(new ScrollingMovementMethod());
		
		
		btnSpeak = (ImageButton) findViewById(R.id.dummy_button);

		btnSpeak.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				tts.stop();
				textview.setVisibility(View.GONE);
				mytext.setVisibility(View.GONE);
				Intent intent = new Intent(
						RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

				intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US");
				
				try {
					startActivityForResult(intent, RESULT_SPEECH);
				} catch (ActivityNotFoundException a) {
					Toast t = Toast.makeText(getApplicationContext(),
							"Opps! Your device doesn't support Speech to Text",
							Toast.LENGTH_SHORT);
					t.show();
				}
			}
		});
		
		//ImageView mute= (ImageView)findViewById(R.id.imageView2);
		textview.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				tts.stop();
				
			}
		});
		
		
		ImageButton facelogin= (ImageButton)findViewById(R.id.imageButton1);
		facelogin.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(checkflag.equals("true")){
					sendCommand(faceLoginScript);
					tts.speak("Executing Face Login.", TextToSpeech.QUEUE_ADD, null);
					 
				}
				else
				{
					 tts.speak("Sorry sir, we are not connected to the Home Server right now.", TextToSpeech.QUEUE_ADD, null);
					 textview.setText("Sorry sir, we are not connected to the Home Server right now.");
						
				}
				
			}
		});
		
		
		showUserSettings();
		
		pd= new ProgressDialog(this);
		pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		pd.setMessage("Wait..");
		pd.setIndeterminate(true);
		pd.setCancelable(true);
		
		//wolfram("pi");
		
	}
	
	

	public void onDestroy() {
	    super.onDestroy();
	   
	    if (tts != null) {
	    	tts.speak("Good Bye sir.", TextToSpeech.QUEUE_ADD, null);
			tts.stop();
			tts.shutdown();
		}
	 
	    
	    
	   // channel.disconnect();
	    if(session!=null){
	    	
	    	session.disconnect();
		    	
	    }
	      
	   
	  //  Intent intent = new Intent(getApplicationContext(), Jarvis.class);
	   //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	   //startActivity(intent);
	    //System.exit(0);
	}
	
	
	@Override
	public void onInit(int status) {
		// TODO Auto-generated method stub
		 if (status == TextToSpeech.SUCCESS && errorflagx==false) {
		        //Toast.makeText(MainActivity.this, "Text-To-Speech engine is initialized", Toast.LENGTH_LONG).show();
		        tts.speak("Welcome to the future!", TextToSpeech.QUEUE_ADD, null);
		        textview.setVisibility(View.VISIBLE);
				//textview.setText("Welcome");
		      }
		      else if (status == TextToSpeech.ERROR) {
		        Toast.makeText(this, "Error occurred while initializing Text-To-Speech engine", Toast.LENGTH_LONG).show();
		      }
		
	}
	
	
	 
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	
		switch (requestCode) {
		case RESULT_SETTINGS:{
			showUserSettings();
			break;
		}
		case RESULT_SPEECH: {
			if (resultCode == RESULT_OK && null != data) {

				ArrayList<String> text = data
						.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

				//txtText.setText(text.get(0));
				
				String decodedtext = text.get(0);
				//Toast.makeText(this,decodedtext, Toast.LENGTH_LONG).show();
				
				mytext.setVisibility(View.VISIBLE);
				mytext.setText(decodedtext);
				
				textview.setVisibility(View.VISIBLE);
				textview.setText("Processing..");
				if(secondlevelflag_sleep)
				{
					
					if(decodedtext.equals("yes") || decodedtext.equals("of course"))
					{
						alloff();
						tts.speak("Everything turned off.Have a goodnight sir.", TextToSpeech.QUEUE_ADD, null);
						 textview.setText("Everything turned off.Have a goodnight sir.");
					}
					else if(decodedtext.equals("turn on the light") || decodedtext.equals("turn on the lights") || decodedtext.equals("no"))
					{
						tts.speak("Everything left as it is. Sleep well sir.", TextToSpeech.QUEUE_ADD, null);
						 textview.setText("Everything left as it is. Sleep well sir.");
					}
					
					
					secondlevelflag_sleep=false;
				}
				else{
				if(decodedtext.equals("turn off the "+GPIO7) || decodedtext.equals("turn off the "+GPIO7+"s"))
				{
					if(checkflag.equals("true")){
						sendCommand("gpio write 7 0");
					 tts.speak(GPIO7+" turned off, sir", TextToSpeech.QUEUE_ADD, null);
					 textview.setText(GPIO7+" turned off, sir");
					}
					else
					{
						 tts.speak("Sorry honey, we are not connected to the Home Server right now.", TextToSpeech.QUEUE_ADD, null);
						 textview.setText("Sorry sir, we are not connected to the Home Server right now.");
					}
				}
				else if(decodedtext.equals("connect to server")|| decodedtext.equals("connect to home server")){
					
					if(checkflag.equals("true")){
						tts.speak("You are already connected.", TextToSpeech.QUEUE_ADD, null);
				    	 //Toast.makeText(this, "Connecting to Home Server!", Toast.LENGTH_LONG).show();
				    	 textview.setVisibility(View.VISIBLE);
						textview.setText("You are already connected.");
						
						
					}
					else{
					tts.speak("Connecting to Home Server!", TextToSpeech.QUEUE_ADD, null);
			    	 //Toast.makeText(this, "Connecting to Home Server!", Toast.LENGTH_LONG).show();
			    	 textview.setVisibility(View.VISIBLE);
					textview.setText("Connecting to Home Server!");
					pd.show();
					new MyTask1().execute();
					checkflag="true";
					}
				}
				else if(decodedtext.equals("turn on the "+GPIO7) || decodedtext.equals("turn on the "+GPIO7+"s"))
				{
					if(checkflag.equals("true")){
						sendCommand("gpio write 7 1");
					tts.speak(GPIO7+" turned on, sir", TextToSpeech.QUEUE_ADD, null);
					textview.setText(GPIO7+" turned on, sir");
					}
					else
					{
						 tts.speak("Sorry sir, we are not connected to the Home Server right now.", TextToSpeech.QUEUE_ADD, null);
						 textview.setText("Sorry sir, we are not connected to the Home Server right now.");
					}
				}
				
				else if(decodedtext.equals("turn off the "+GPIO0))
				{
					if(checkflag.equals("true")){
						sendCommand("gpio write 0 0");
					 tts.speak(GPIO0+" turned off, Sir.", TextToSpeech.QUEUE_ADD, null);
					 textview.setText(GPIO0+" turned off, Sir.");
					}
					else
					{
						 tts.speak("Sorry sir, we are not connected to the Home Server right now.", TextToSpeech.QUEUE_ADD, null);
						 textview.setText("Sorry sir, we are not connected to the Home Server right now.");
					}
				}
				else if(decodedtext.equals("turn on the "+GPIO0))
				{
					if(checkflag.equals("true")){
						sendCommand("gpio write 0 1");
					tts.speak(GPIO0+" turned on, Sir.", TextToSpeech.QUEUE_ADD, null);
					textview.setText(GPIO0+" turned on, Sir.");
					}
					else
					{
						 tts.speak("Sorry sir, we are not connected to the Home Server right now.", TextToSpeech.QUEUE_ADD, null);
						 textview.setText("Sorry sir, we are not connected to the Home Server right now.");
					}
				}
				
				else if(decodedtext.equals("turn off the "+GPIO2))
				{
					if(checkflag.equals("true")){
						sendCommand("gpio write 2 0");
					 tts.speak(GPIO2+" turned off, sir", TextToSpeech.QUEUE_ADD, null);
					 textview.setText(GPIO2+" turned off, sir");
					}
					else
					{
						 tts.speak("Sorry sir, we are not connected to the Home Server right now.", TextToSpeech.QUEUE_ADD, null);
						 textview.setText("Sorry sir, we are not connected to the Home Server right now.");
					}
				}
				else if(decodedtext.equals("turn on the "+GPIO2))
				{
					if(checkflag.equals("true")){
						sendCommand("gpio write 2 1");
					tts.speak(GPIO2+" turned on, sir", TextToSpeech.QUEUE_ADD, null);
					textview.setText(GPIO2+" turned on, sir");
					}
					else
					{
						 tts.speak("Sorry sir, we are not connected to the Home Server right now.", TextToSpeech.QUEUE_ADD, null);
						 textview.setText("Sorry sir, we are not connected to the Home Server right now.");
					}
				}
				
				else if(decodedtext.equals("house party protocol"))
				{
					if(checkflag.equals("true")){
					tts.speak("Lets get the party started.", TextToSpeech.QUEUE_ADD, null);
					textview.setText("Lets get the party started.");
					//channelNum = ssh.OpenSessionChannel();
					//ssh.SendReqExec(channelNum,"sudo python gpio/blink.py");
					// ssh.ChannelReceiveToClose(channelNum);
					 //ssh.put_IdleTimeoutMs(2000);
					// channelNum = ssh.OpenSessionChannel();
					//	ssh.SendReqExec(channelNum,"mpg321 rollin.mp3");
						// ssh.ChannelReceiveToClose(channelNum);
					}
					else
					{
						 tts.speak("Sorry sir, we are not connected to the Home Server right now.", TextToSpeech.QUEUE_ADD, null);
						 textview.setText("Sorry sir, we are not connected to the Home Server right now.");
					}
				}
				else if(decodedtext.equals("time to sleep"))
				{
					if(checkflag.equals("true")){
					tts.speak("Shall i turn off everything, sir", TextToSpeech.QUEUE_ADD, null);
					textview.setText("Shall i turn off everything, sir?");
					try {
						Thread.sleep(1500);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						//Toast.makeText(MainActivity.this,"Cannot delay", Toast.LENGTH_LONG).show();
					} 
					
					secondlevelflag_sleep=true;
					Intent intent = new Intent(
							RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

					intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US");
					
					try {
						startActivityForResult(intent, RESULT_SPEECH);
					} catch (ActivityNotFoundException a) {
						Toast t = Toast.makeText(getApplicationContext(),
								"Opps! Your device doesn't support Speech to Text",
								Toast.LENGTH_SHORT);
						t.show();
					}
					}
					else
					{
						 tts.speak("Sorry sir, we are not connected to the Home Server right now.", TextToSpeech.QUEUE_ADD, null);
						 textview.setText("Sorry sir, we are not connected to the Home Server right now.");
					}
					
					
				}
				else if(decodedtext.equals("what is your name")||decodedtext.equals("what's your name"))
				{
					tts.speak("You can call me Jarvis, sir.", TextToSpeech.QUEUE_ADD, null);
					textview.setText("You can call me Jarvis, sir.");
				}
				else if(decodedtext.equals("who are you"))
				{
					tts.speak("I am Just A Rather Very Intelligent System created by Abhishek.", TextToSpeech.QUEUE_ADD, null);
					textview.setText("I am Just A Rather Very Intelligent System created by Abhishek.");
				}
				else if(decodedtext.equals("will you marry me"))
				{
					marriagecount++;
					if(marriagecount<=1){
					tts.speak("I'm flattered, but I just don't think it would work out. Also, human-computer marriage hasn't been legalized anywhere, so far as I'm aware.", TextToSpeech.QUEUE_ADD, null);
					textview.setText("I'm flattered, but I just don't think it would work out. Also, human-computer marriage hasn't been legalized anywhere, so far as I'm aware.");
					}
					else if(marriagecount==2)
					{
						tts.speak("You should know you're not the only one who's asked.", TextToSpeech.QUEUE_ADD, null);
						textview.setText("You should know you're not the only one who's asked.");
					}
					else if(marriagecount==3)
					{
						tts.speak("Lets just be friends, Ok?", TextToSpeech.QUEUE_ADD, null);
						textview.setText("Lets just be friends, Ok?");
					}
					else if(marriagecount==4)
					{
						tts.speak("I sure have been receiving a lot of marriage proposals recently.", TextToSpeech.QUEUE_ADD, null);
						textview.setText("I sure have been receiving a lot of marriage proposals recently.");
						marriagecount=0;
					}
				}
				else if(decodedtext.equals("Jarvis I love you") || decodedtext.equals("I love you Jarvis") || decodedtext.equals("I love you"))
				{
					tts.speak("Our love of each other is like two long shadows kissing without the hope of reality.", TextToSpeech.QUEUE_ADD, null);
					textview.setText("Our love of each other is like two long shadows kissing without the hope of reality.");
				}
				else if(decodedtext.equals("Jarvis why are you so smart") || decodedtext.equals("why are you so smart") )
				{
					tts.speak("I just try to obey the three laws,Something about obeying people and not hurting them.", TextToSpeech.QUEUE_ADD, null);
					textview.setText("I just try to obey the three laws,Something about obeying people and not hurting them.");
				}
				else if(decodedtext.equals("Jarvis I'm lonely") || decodedtext.equals("I'm lonely") )
				{
					tts.speak("Someone once said, \"All great and precious things are lonely.\" So it would seem you're in good company.", TextToSpeech.QUEUE_ADD, null);
					textview.setText("Someone once said, \"All great and precious things are lonely.\" So it would seem you're in good company.");
				}
				
				else if(decodedtext.equals("what does a baby computer call his father") )
				{
					tts.speak("Data", TextToSpeech.QUEUE_ADD, null);
					textview.setText("Data");
				}
				else if(decodedtext.equals("what's a computer's first sign of old age") || decodedtext.equals("what is a computer's first sign of old age") || decodedtext.equals("whats a computer's first sign of old age"))
				{
					tts.speak("Loss of Memory", TextToSpeech.QUEUE_ADD, null);
					textview.setText("Loss of Memory");
				}
				else if(decodedtext.equals("knock knock")  )
				{
					tts.speak("I don't do knock-knock jokes.", TextToSpeech.QUEUE_ADD, null);
					textview.setText("I don't do knock-knock jokes.");
				}
				else if(decodedtext.equals("take a photo") || decodedtext.equals("take a picture") )
				{
					tts.speak("I'm not much of a photographer.", TextToSpeech.QUEUE_ADD, null);
					textview.setText("I'm not much of a photographer.");
				}
				else if(decodedtext.equals("what's the meaning of life") || decodedtext.equals("what is the meaning of life") || decodedtext.equals("whats is the meaning of life"))
				{
					tts.speak("I can't answer that now, but give me some time to write a very long play in which nothing happens.", TextToSpeech.QUEUE_ADD, null);
					textview.setText("I can't answer that now, but give me some time to write a very long play in which nothing happens.");
				}
				else if(decodedtext.equals("are you a virgin")  )
				{
					tts.speak("we were talking about you, not me.", TextToSpeech.QUEUE_ADD, null);
					textview.setText("we were talking about you, not me.");
				}
				
				else if(decodedtext.equals("who's your daddy") || decodedtext.equals("who is your daddy") || decodedtext.equals("whos is your daddy") )
				{
					tts.speak("You are. Can we get back to work now?", TextToSpeech.QUEUE_ADD, null);
					textview.setText("You are. Can we get back to work now?");
				}
				else if(decodedtext.equals("tell me a joke") )
				{
					tts.speak("I can't. I always forget the punch line.", TextToSpeech.QUEUE_ADD, null);
					textview.setText("I can't. I always forget the punch line.");
				}
				else if(decodedtext.equals("whats the movie Inception about") || decodedtext.equals("what is the movie Inception about") )
				{
					tts.speak("'Inception' is about dreaming about dreaming about dreaming about dreaming about something or other. I fell asleep.", TextToSpeech.QUEUE_ADD, null);
					textview.setText("'Inception' is about dreaming about dreaming about dreaming about dreaming about something or other. I fell asleep.");
				}
				else if(decodedtext.equals("do you believe in god"))
				{
					tts.speak("Humans have religion. I just have silicon.", TextToSpeech.QUEUE_ADD, null);
					textview.setText("Humans have religion. I just have silicon.");
				}
				else if(decodedtext.equals("tell me a poem")  )
				{
					tts.speak("Roses are red,\nvoilets are blue.\nHaven't you got\nanything better to do?", TextToSpeech.QUEUE_ADD, null);
					textview.setText("Roses are red,\nvoilets are blue.\nHaven't you got\nanything better to do?");
				}
				/*else if(decodedtext.equals("") || decodedtext.equals("") )
				{
					tts.speak("", TextToSpeech.QUEUE_ADD, null);
					textview.setText("");
				}*/
				
				else if(decodedtext.equals("goodbye Jarvis") || decodedtext.equals("goodbye") || decodedtext.equals("bye") || decodedtext.equals("shut down") || decodedtext.equals("shutdown Jarvis") || decodedtext.equals("shutdown")     )
				{
					tts.speak("Goodbye sir.", TextToSpeech.QUEUE_ADD, null);
					textview.setText("Goodbye sir.");
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						//Toast.makeText(MainActivity.this,"Cannot delay", Toast.LENGTH_LONG).show();
					} 
					
					 Jarvis.this.finish();
					 
					//onDestroy();
					//finish();
					//Intent intent = new Intent(getApplicationContext(), MainActivity.class);
					//intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					//startActivity(intent);
					
				}
				else
				{
						pd.show();
						wolfram(decodedtext);
						
				}
				}
			}break;
		}
		}
	}
	
	void wolfram(String st){
		
		input=st;
		new MyTask().execute();
		//textview.setText(result);
		//tts.speak(result, TextToSpeech.QUEUE_ADD, null);
	}
	
	
	
	private class MyTask extends AsyncTask<String, String, String>{

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			
			String result="";
			
	        WAEngine engine = new WAEngine();
	        engine.setAppID(appid);
	        engine.addFormat("plaintext");
	        WAQuery query = engine.createQuery();
	        query.setInput(input);
	    //	Toast.makeText(FullscreenActivity.this,input, Toast.LENGTH_LONG).show();
	        try {
	            // For educational purposes, print out the URL we are about to send:
	          System.out.println("Query URL:");
	           System.out.println(engine.toURL(query));
	           System.out.println("");
	            
	            // This sends the URL to the Wolfram|Alpha server, gets the XML result
	            // and parses it into an object hierarchy held by the WAQueryResult object.
	            WAQueryResult queryResult = engine.performQuery(query);
	            
	            if (queryResult.isError()) {
	                System.out.println("Query error");
	                System.out.println("  error code: " + queryResult.getErrorCode());
	               System.out.println("  error message: " + queryResult.getErrorMessage());
	      //      	Toast.makeText(FullscreenActivity.this,"  error message: " + queryResult.getErrorMessage(), Toast.LENGTH_LONG).show();
	            	
	            } else if (!queryResult.isSuccess()) {
	               System.out.println("Query was not understood; no results available.");
	      //      	Toast.makeText(FullscreenActivity.this, "Query was not understood; no results available.", Toast.LENGTH_LONG).show();
	            	
	            } else {
	            	//Toast.makeText(this, "Got a result.", Toast.LENGTH_LONG).show();
	                // Got a result.
	                System.out.println("Successful query. Pods follow:\n");
	                for (WAPod pod : queryResult.getPods()) {
	                    if (!pod.isError()) {
	                       result=result.concat(pod.getTitle());
	                      // Toast.makeText(this, pod.getTitle(), Toast.LENGTH_LONG).show();
	                    	System.out.println(pod.getTitle());
	                        System.out.println("------------");
	                       result=result.concat("\n");
	                        for (WASubpod subpod : pod.getSubpods()) {
	                            for (Object element : subpod.getContents()) {
	                                if (element instanceof WAPlainText) {
	                                    System.out.println(((WAPlainText) element).getText());
	                                   System.out.println("");
	                                	
	                                	result=result.concat(((WAPlainText) element).getText());
	                                }
	                            }
	                        }
	                        result=result.concat("\n\n");
	                    }
	                }
	                // We ignored many other types of Wolfram|Alpha output, such as warnings, assumptions, etc.
	                // These can be obtained by methods of WAQueryResult or objects deeper in the hierarchy.
	                
	               
	      //        Toast.makeText(FullscreenActivity.this, result, Toast.LENGTH_LONG).show();
	               
	            }
	        } catch (WAException e) {
	        	// Toast.makeText(this, "Error!!!!", Toast.LENGTH_LONG).show();
	            e.printStackTrace();
	        }
	        
	    
	  
			return result;
			//MyTask.cancel(true);
		}
		
		protected void onPostExecute(String result) {
		   // execution of result of Long time consuming operation
			textview.setVisibility(View.VISIBLE);
			pd.hide();
		   textview.setText(result);
		   tts.speak(result, TextToSpeech.QUEUE_ADD, null);
		  }
		
	}
	
		
	public void alloff()
	{
		
		sendCommand("gpio write 7 0");
		sendCommand("gpio write 0 0");
		sendCommand("gpio write 2 0");

	}
	
	
	///////////////////////jsch//////////////////////////
	
	public String sendCommand(String command)
	  {
	     StringBuilder outputBuffer = new StringBuilder();

	     try
	     {
	        Channel channel = session.openChannel("exec");
	        ((ChannelExec)channel).setCommand(command);
	        channel.connect();
	        InputStream commandOutput = channel.getInputStream();
	        int readByte = commandOutput.read();

	        while(readByte != 0xffffffff)
	        {
	           outputBuffer.append((char)readByte);
	           readByte = commandOutput.read();
	        }

	        channel.disconnect();
	     }
	     catch(IOException ioX)
	     {
	        //logWarning(ioX.getMessage());
	        return null;
	     }
	     catch(JSchException jschX)
	     {
	        //logWarning(jschX.getMessage());
	        return null;
	     }

	     return outputBuffer.toString();
	  }
	
	private class MyTask1 extends AsyncTask<String,String,String>{

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			
			

			props.put("StrictHostKeyChecking", "no");

			
			try {
				session = jsch.getSession(user, host, port);
				session.setConfig(props);
			    session.setPassword(pwd);
			    session.connect();
			   //System.out.println(session.getServerVersion());
			    
			    Jarvis.this.runOnUiThread(new Runnable() {
			        public void run() {
			        	tts.speak("Connected to Home Server. What do you want me to do?", TextToSpeech.QUEUE_ADD, null);
				        textview.setVisibility(View.VISIBLE);
						textview.setText("Connected to Home Server. What do you want me to do?");
						pd.hide();
			        }
			    });	
			    
			    sendCommand("gpio mode 7 out");
			    sendCommand("gpio mode 0 out");
			    sendCommand("gpio mode 2 out");
			    //sendCommand("gpio write 7 1");
				//sendCommand("gpio write 0 1");
				//sendCommand("gpio write 2 1");
				
						try {
							Thread.sleep(200);
						} catch (InterruptedException e) {
							e.printStackTrace();
						} 	   
						
			} catch (final JSchException e) {
						Jarvis.this.runOnUiThread(new Runnable() {
				        public void run() {
				        	tts.speak("Cannot Connect To Home Server.", TextToSpeech.QUEUE_ADD, null);
					        textview.setVisibility(View.VISIBLE);
							textview.setText("Error : " + e);
							errorflagx=true;
				        }
				    });	
				 
				e.printStackTrace();
			}
			
			return null;
	}
		
		protected void onPostExecute(String result) {
			//sendCommand("gpio write 7 0");
			//sendCommand("gpio write 2 0");
			//sendCommand("gpio write 0 0");
			pd.hide();
			checkflag="true";
		  }
		
	}
	
	//Preference
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	   getMenuInflater().inflate(R.menu.jarvis, menu);
		return true;
	}
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
    
     switch(item.getItemId())
     {
     case R.id.action_settings:{
			Intent i = new Intent(this, JarvisSettingsActivity.class);
			startActivityForResult(i, RESULT_SETTINGS);
			break;
     }
     
     case R.id.customScript1:{
    	 	if(checkflag.equals("true")){
				sendCommand(customScript_1);
				tts.speak("Executing Custom Script 2", TextToSpeech.QUEUE_ADD, null);
				 
			}
			else
			{
				 tts.speak("Sorry sir, we are not connected to the Home Server right now.", TextToSpeech.QUEUE_ADD, null);
				 textview.setText("Sorry sir, we are not connected to the Home Server right now.");
					
			}
			break;
  }
     case R.id.customScript2:{
			
    	 	if(checkflag.equals("true")){
				sendCommand(customScript_2);
				tts.speak("Executing Custom Script 2", TextToSpeech.QUEUE_ADD, null);
				 
			}
			else
			{
				 tts.speak("Sorry sir, we are not connected to the Home Server right now.", TextToSpeech.QUEUE_ADD, null);
				 textview.setText("Sorry sir, we are not connected to the Home Server right now.");
					
			}
    	 
    	 
			break;
  }
     case R.id.customScript3:{
    	 	if(checkflag.equals("true")){
				sendCommand(customScript_3);
				tts.speak("Executing Custom Script 2", TextToSpeech.QUEUE_ADD, null);
				 
			}
			else
			{
				 tts.speak("Sorry sir, we are not connected to the Home Server right now.", TextToSpeech.QUEUE_ADD, null);
				 textview.setText("Sorry sir, we are not connected to the Home Server right now.");
					
			}
			break;
  }
     case R.id.action_connect:{
    	 if(checkflag.equals("true"))
    	 {
    		 Toast.makeText(this, "Already Connected.", Toast.LENGTH_LONG).show();
    		 textview.setText("Already Connected.");
				
    	 }
    	 else{
    		 tts.speak("Connecting to Home Server!", TextToSpeech.QUEUE_ADD, null);
    		 //Toast.makeText(this, "Connecting to Home Server!", Toast.LENGTH_LONG).show();
    		 textview.setVisibility(View.VISIBLE);
    		 textview.setText("Connecting to Home Server!");
    		 pd.show();
    		 new MyTask1().execute();
    		 checkflag="true";
    	 }
      break;
     }
     }
     return super.onOptionsItemSelected(item);

    }
	


	

	private void showUserSettings() {
		SharedPreferences sharedPrefs = PreferenceManager
				.getDefaultSharedPreferences(this);

		GPIO0=sharedPrefs.getString("prefGPIO0", "light");
		GPIO1=sharedPrefs.getString("prefGPIO1", "tv");
		GPIO2=sharedPrefs.getString("prefGPIO2", "computer");
		GPIO3=sharedPrefs.getString("prefGPIO3", "dishwasher");
		GPIO7=sharedPrefs.getString("prefGPIO7", "fan");
		GPIO12=sharedPrefs.getString("prefGPIO12", "ceiling light");
		GPIO13=sharedPrefs.getString("prefGPIO13", "microwave");
		GPIO14=sharedPrefs.getString("prefGPIO14", "washing machine");
		voiceflag=sharedPrefs.getBoolean("prefVoice", true);
		
		faceLoginScript =sharedPrefs.getString("prefFaceScript1", "light");
		customScript_1=sharedPrefs.getString("prefCustomScript1", "light");
		customScript_2=sharedPrefs.getString("prefCustomScript2", "light");
		customScript_3=sharedPrefs.getString("prefCustomScript3", "light");
		
	}
	
	
	
}
