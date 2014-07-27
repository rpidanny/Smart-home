package com.abhishek.smarthome;



import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Toast;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;


public class color1 extends Fragment implements OnTouchListener, OnSeekBarChangeListener, View.OnClickListener{
	
	
	public final static String TAG = "AndroidColor";
	public ColorPickerView colorPicker;
	private TextView text1;
	private static final int blueStart = 100;
	SeekBar seek;
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.color, container, false);
        
        ///////////////////////////////////////////////////////////////
        
        LinearLayout layout = (LinearLayout) rootView.findViewById(R.id.color_picker_layout);
        final int width = layout.getWidth();
        //get the display density
        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        colorPicker = new ColorPickerView(getActivity(),blueStart,metrics.densityDpi);
        layout.setMinimumHeight(width);
        layout.addView(colorPicker);
        layout.setOnTouchListener(this);
        
        text1 = (TextView) rootView.findViewById(R.id.result1_textview);
	    text1.setText("Tap a color!");
                
		seek = (SeekBar) rootView.findViewById(R.id.seekBar1);
		seek.setProgress(blueStart);
		seek.setMax(255);
		seek.setOnSeekBarChangeListener(this);
        
		Button BtnRandom = (Button)rootView.findViewById(R.id.randomButton);
		BtnRandom.setOnClickListener(this);
        
        /****************************************************************************/
        
        
        
        return rootView;
    }
	
	
	
	 @Override
	public void onPause() {
	        super.onPause();
	       
	    }

	    @Override
		public void onResume() {
	        super.onResume();
	       
	    }
		    
		@Override
		public void onDestroy() {
			super.onDestroy();
		}
		
		// sends color data to a Serial device as {R, G, B, 0x0A}
		private void sendToArduino(int color){
			byte[] dataToSend = {(byte)Color.red(color),(byte)Color.green(color),(byte)Color.blue(color), 0x0A};
			//remove spurious line endings from color bytes so the serial device doesn't get confused
			for (int i=0; i<dataToSend.length-1; i++){
				if (dataToSend[i] == 0x0A){
					dataToSend[i] = 0x0B;
				}
			}
			
			try{ 
				 
                // URLEncode user defined data
				
			String RED    = URLEncoder.encode(Float.toString(map_to_float(Color.red(color),0,255,0,1)), "UTF-8");
            String GREEN  = URLEncoder.encode(Float.toString(map_to_float(Color.green(color),0,255,0,1)), "UTF-8");
            String BLUE   = URLEncoder.encode(Float.toString(map_to_float(Color.blue(color),0,255,0,1)), "UTF-8");
            
			
			String URL = Room2Activity.ServerIpAddress+":8080/cgi-bin/setRGB.py?R="+RED+"&G="+GREEN+"&B="+BLUE;
			new RetrieveFeedTask().execute(URL);
			 }
            catch(UnsupportedEncodingException ex)
             {
                  //   content.setText("Fail");
            	
            	 Toast.makeText(getActivity(), "ENCODING Failed! with "+ex.toString(), Toast.LENGTH_LONG).show();
       		     
              }    
			//send the color to the serial device
			/*if (device != null){
				try{
					device.write(dataToSend, 500);
				}
				catch (IOException e){
					Log.e(TAG, "couldn't write color bytes to serial device");
				}
			} */
		}
		
	    // sets the text boxes' text and color background.
		private void updateTextAreas(int col) {
			int[] colBits = {Color.red(col),Color.green(col),Color.blue(col)};
			//set the text & color backgrounds
			text1.setText("You picked #" + String.format("%02X", Color.red(col)) + String.format("%02X", Color.green(col)) + String.format("%02X", Color.blue(col)));
			text1.setBackgroundColor(col);
			
			if (isDarkColor(colBits)) {
				text1.setTextColor(Color.WHITE);
			} else {
				text1.setTextColor(Color.BLACK);
			}
		}
		
		// returns true if the color is dark.  useful for picking a font color.
	    public boolean isDarkColor(int[] color) {
	    	if (color[0]*.3 + color[1]*.59 + color[2]*.11 > 150) return false;
	    	return true;
	    }
	    
	    @Override
	    //called when the user touches the color palette
		public boolean onTouch(View view, MotionEvent event) {
	    	int color = 0;
			color = colorPicker.getColor(event.getX(),event.getY(),true);
			colorPicker.invalidate();
			//re-draw the selected colors text
			updateTextAreas(color);
			//send data to arduino
			sendToArduino(color);
			
			//Vibrtion
			 Vibrator v = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
 	        v.vibrate(30);
			return true;
		}
		
	    @Override
		public void onProgressChanged(SeekBar seek, int progress, boolean fromUser) {
			int amt = seek.getProgress();
			int col = colorPicker.updateShade(amt);
			updateTextAreas(col);
			sendToArduino(col);
			colorPicker.invalidate();
			//Vibrtion
			 Vibrator v = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
	        v.vibrate(100);
		}

		@Override
		public void onStartTrackingTouch(SeekBar arg0) {
			
		}

		@Override
		public void onStopTrackingTouch(SeekBar arg0) {
			
		}



		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
			int z = (int) (Math.random()*255);
	    	int x = (int) (Math.random()*255);
	    	int y = (int) (Math.random()*255);
	    	colorPicker.setColor(x,y,z);
			//SeekBar seek = (SeekBar) v.findViewById(R.id.seekBar1);
			seek.setProgress(z);
			
			//Vibrtion
			 Vibrator v1 = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
	        v1.vibrate(100);
			
		}
	
		
		
		
		float map_to_float(float x, float a, float b, float c, float d) // input,lower,upper,lower,upper
		{
		      float f=x/(b-a)*(d-c)+c;
		      return f;
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