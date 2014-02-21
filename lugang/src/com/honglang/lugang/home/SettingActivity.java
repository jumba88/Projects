package com.honglang.lugang.home;

import com.honglang.lugang.HlApp;
import com.honglang.lugang.R;
import com.honglang.lugang.R.id;
import com.honglang.lugang.R.layout;
import com.honglang.lugang.R.string;
import com.honglang.lugang.login.LoginActivity;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class SettingActivity extends Activity implements OnClickListener {

	private TextView title;
	private Button back;
	private Button logout;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		init();
	}
	
	private void init(){
		back = (Button) this.findViewById(R.id.back);
		back.setOnClickListener(this);
		title = (TextView) this.findViewById(R.id.title);
		title.setText(R.string.setting);
		logout = (Button) this.findViewById(R.id.logout);
		logout.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			this.finish();
			break;
		case R.id.logout:
			onLogout();
			break;
		}
		
	}
	
	public void onLogout(){
		Intent intent = new Intent(this, LoginActivity.class);
		this.startActivity(intent);
		HlApp.getInstance().getHomeActivity().finish();
		this.finish();
	}

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.setting, menu);
//		return true;
//	}

}
