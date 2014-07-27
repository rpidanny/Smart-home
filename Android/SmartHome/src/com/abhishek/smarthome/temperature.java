package com.abhishek.smarthome;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class temperature extends Fragment {
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.temperature, container, false);
        String url = Room1Activity.ServerIpAddress+":8080/cgi-bin/mobilegraph.py";

        //WebView wv = new WebView(getActivity()); 
        // or 
         WebView wv = (WebView)rootView.findViewById(R.id.webView);
         //wv.setWebChromeClient(chromeClient);
         wv.getSettings().setJavaScriptEnabled(true);
        // wv.getSettings().setPluginsEnabled(true);
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
