package com.abhishek.smarthome;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class buttons extends Fragment {
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.buttons, container, false);
        String url = Room1Activity.ServerIpAddress +":8000/";
        
        //WebView wv = new WebView(getActivity()); 
        // or 
         WebView wv = (WebView)rootView.findViewById(R.id.webView1);
         wv.getSettings().setJavaScriptEnabled(true);
        wv.loadUrl(url);
        wv.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        return rootView;
    }

}
