package com.inventorydelivery;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;

public class SplashActivity extends Activity {

	private final int DELAY = 2500;
	
	Context mContext=SplashActivity.this;
    SharedPreferences appPreferences;
    boolean isAppInstalled = false;



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		startLoginActivty(DELAY);
		
		
		   appPreferences = PreferenceManager.getDefaultSharedPreferences(this);
           isAppInstalled = appPreferences.getBoolean("isAppInstalled",false);  
           if(isAppInstalled==false){
          
           Intent shortcutIntent = new Intent(getApplicationContext(),SplashActivity.class);
           shortcutIntent.setAction(Intent.ACTION_MAIN);
           Intent intent = new Intent();
           intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
           intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "Invent Delivery");
           intent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,Intent.ShortcutIconResource.fromContext(getApplicationContext(), R.drawable.ic_launcher));
           intent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
           getApplicationContext().sendBroadcast(intent);
          
           SharedPreferences.Editor editor = appPreferences.edit();
           editor.putBoolean("isAppInstalled", true);
           editor.commit();
    }


		
		
	}

	private void startLoginActivty(int delay) {

		Runnable runnable = new Runnable() {

			@Override
			public void run() {
				finish();
				Intent loginIntent = new Intent(SplashActivity.this,
						LoginActivity.class);
				startActivity(loginIntent);

			}
		};

		Handler handler = new Handler();
		handler.postDelayed(runnable, delay);

	}

}
