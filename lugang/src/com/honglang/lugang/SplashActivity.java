package com.honglang.lugang;

import com.honglang.lugang.login.LoginActivity;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Intent;

public class SplashActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				Intent i = new Intent(SplashActivity.this,LoginActivity.class);
				i.putExtra("dir", 0);
				SplashActivity.this.startActivity(i);
				SplashActivity.this.finish();
			}
		}, 1000);
		
	}


}
