package com.abhishek.smarthome;

import java.io.IOException;
import java.net.URI;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;




import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class voice extends Fragment {
	
	   private static final String TAG = "MjpegActivity";

	
	    //String URL = "http://192.168.1.15:8070/videofeed";
		private MjpegView mv;
		boolean cameraflag=true;
	    DoRead DoRead1;
	    ImageView camera;
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.voice, container, false);
        mv = new MjpegView(getActivity());
        FrameLayout frame = (FrameLayout) rootView.findViewById(R.id.frame);
        camera= (ImageView) rootView.findViewById(R.id.camcover);
        
        frame.setOnClickListener(new OnClickListener() {
    	    
    				@Override
    				public void onClick(View arg0) {
    					
    				    if(cameraflag){
    				    	camera.setVisibility(View.GONE);
    				        FrameLayout frame = (FrameLayout)arg0.findViewById(R.id.frame);
    				        frame.removeAllViews();
    				        frame.addView(mv, 0);
    				        cameraflag=false;
    				       connectCam();
    				     
    				        
    				    }
    				    else
    				    {
    				    	destoryCam();
    				    	FrameLayout frame = (FrameLayout)arg0.findViewById(R.id.frame);
    				        //frame.removeView(mv);
    				        frame.removeAllViews();
    				        mv.stopPlayback();
    				        
    				    	camera.setVisibility(View.VISIBLE);
    				    	//cameraflag=true;
    				    	
    				    }
    				}
    	        });
        
        return rootView;
    }
	
	
	 public void connectCam()
	 {
		 DoRead1 = new DoRead();
	     DoRead1.execute(Room1Activity.videolink);
	     // new DoRead().execute(URL);
	      
	 }
	 
	 public void destoryCam()
	 {
		 //DoRead1.cancel(true);
		 if(DoRead1.cancel(true))
		 {
			 cameraflag=true;
		 }
		 else
		 {
			 cameraflag=false;
		 }
	 }
	 
	 public void onPause() {
	        super.onPause();
	       //mv.stopPlayback();
	    }
	
	public class DoRead extends AsyncTask<String, Void, MjpegInputStream> {
        protected MjpegInputStream doInBackground(String... url) {
            //TODO: if camera has authentication deal with it and don't just not work
            HttpResponse res = null;
            DefaultHttpClient httpclient = new DefaultHttpClient();     
            Log.d(TAG, "1. Sending http request");
            try {
                res = httpclient.execute(new HttpGet(URI.create(url[0])));
                Log.d(TAG, "2. Request finished, status = " + res.getStatusLine().getStatusCode());
                if(res.getStatusLine().getStatusCode()==401){
                    //You must turn off camera User Access Control before this will work
                    return null;
                }
                return new MjpegInputStream(res.getEntity().getContent());  
            } catch (ClientProtocolException e) {
                e.printStackTrace();
                Log.d(TAG, "Request failed-ClientProtocolException", e);
                //Error connecting to camera
            } catch (IOException e) {
                e.printStackTrace();
                Log.d(TAG, "Request failed-IOException", e);
                //Error connecting to camera
            }

            return null;
        }

        protected void onPostExecute(MjpegInputStream result) {
            mv.setSource(result);
            mv.setDisplayMode(MjpegView.SIZE_BEST_FIT);
            mv.showFps(true);
        }
    }

}
