package com.abhishek.smarthome;



import java.text.DateFormat;
import java.util.Date;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.FragmentActivity;
import android.telephony.SmsManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class StartUpActivity extends FragmentActivity {

	private SmsManager smsManager = SmsManager.getDefault();
	String sms ="";
	String SMSnumber ="";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_start_up);

		final Button btn1=(Button)findViewById(R.id.btnLiving);
		btn1.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Animation anim= AnimationUtils.loadAnimation(StartUpActivity.this, R.anim.animation);
				btn1.startAnimation(anim);
				Intent i = new Intent(getBaseContext(), Room1Activity.class);
				startActivityForResult(i, 0);
			}
			
			
			
		});
		
		
		final Button btn2=(Button)findViewById(R.id.btnDining);
		btn2.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Animation anim= AnimationUtils.loadAnimation(StartUpActivity.this, R.anim.animation);
				btn2.startAnimation(anim);
				Intent i = new Intent(getBaseContext(), Room2Activity.class);
				startActivityForResult(i, 0);
			}
		});
		
		final Button btn3=(Button)findViewById(R.id.btnMiscellaneous);
		btn3.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Animation anim= AnimationUtils.loadAnimation(StartUpActivity.this, R.anim.animation);
				btn3.startAnimation(anim);
				Intent i = new Intent(getBaseContext(), MiscellaneousActivity.class);
				startActivityForResult(i, 0);
			}
		});
		
		
		final ImageView sh =(ImageView)findViewById(R.id.imageView1);
		sh.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				Animation anim= AnimationUtils.loadAnimation(StartUpActivity.this, R.anim.animation);
				sh.startAnimation(anim);
				Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
	 	        v.vibrate(30);
			}
			
		});
		
		final TextView vote = (TextView)findViewById(R.id.voteClick);
		String currentDateTimeString = DateFormat.getDateInstance().format(new Date());
		//Toast.makeText(StartUpActivity.this,currentDateTimeString , Toast.LENGTH_LONG).show();
		
		
		if(currentDateTimeString.equals("Jun 16, 2014") || currentDateTimeString.equals("Jun 17, 2014") || currentDateTimeString.equals("Jun 18, 2014") || currentDateTimeString.equals("Jun 19, 2014") || currentDateTimeString.equals("Jun 20, 2014") || currentDateTimeString.equals("Jun 21, 2014") || currentDateTimeString.equals("Jun 22, 2014") ){
			
			vote.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Animation anim= AnimationUtils.loadAnimation(StartUpActivity.this, R.anim.vote_animation);
					vote.startAnimation(anim);
					
					Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		 	        v.vibrate(30);
							//.getDateTimeInstance().format(new Date());
					//Toast.makeText(StartUpActivity.this,"" , Toast.LENGTH_LONG).show();
				     
					AlertDialog.Builder builder = new AlertDialog.Builder(StartUpActivity.this);
					builder.setMessage("You sure we deserve your vote?");
					builder.setCancelable(true);
					builder.setPositiveButton("Hell Yeah!!", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							Toast.makeText(StartUpActivity.this,"Sending SMS..." , Toast.LENGTH_LONG).show();
							//smsManager.sendTextMessage(SMSnumber, null, sms, null, null);
						}
					});
					
					builder.setNegativeButton("Nahh", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							Toast.makeText(StartUpActivity.this,"Be a good lad and send us your feedback at abhishekmaharjan1993@gmail.com" , Toast.LENGTH_LONG).show();
							dialog.cancel();
						}
					});
					AlertDialog alert = builder.create();
					alert.show();
				}
				
			});
			
		}
		else
		{
			//vote.setVisibility(View.GONE);
			vote.setText("Powered By :DannyTech");
		}
		
		
		
		
	}

}
