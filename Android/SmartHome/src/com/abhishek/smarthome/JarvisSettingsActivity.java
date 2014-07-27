package com.abhishek.smarthome;


import android.os.Bundle;
import android.preference.PreferenceActivity;

public class JarvisSettingsActivity extends PreferenceActivity {

	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		addPreferencesFromResource(R.xml.pref_general);

	}
}