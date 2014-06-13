package com.honglang.lugang.home;

import com.honglang.lugang.HlApp;
import com.honglang.lugang.R;
import com.honglang.lugang.R.id;
import com.honglang.lugang.R.layout;
import com.honglang.lugang.R.string;
import com.honglang.lugang.login.LoginActivity;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

public class SettingActivity extends Activity implements OnClickListener {

	private TextView title;
	private Button back;
	private Button logout;
	private Button feedback;
	private Button about;
	private Button version;
	
	private Switch switcher;
	private LinearLayout linear;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		init();
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
//		switch (id) {
//		case 1:
//			return new AlertDialog.Builder(SettingActivity.this)
//			.setIconAttribute(android.R.attr.alertDialogIcon)
//			.setTitle("")
//			.setMessage("").create();
//		case 2:
//			return new AlertDialog.Builder(SettingActivity.this)
//			.setIconAttribute(android.R.attr.alertDialogIcon)
//			.setMessage("").create();
//		}
		return new AlertDialog.Builder(SettingActivity.this)
		.setIconAttribute(android.R.attr.alertDialogIcon)
		.setTitle("关于")
		.setMessage(R.string.about_content).create();
	}

	private void init(){
		back = (Button) this.findViewById(R.id.back);
		back.setOnClickListener(this);
		title = (TextView) this.findViewById(R.id.title);
		title.setText(R.string.setting);
		logout = (Button) this.findViewById(R.id.logout);
		logout.setOnClickListener(this);
		feedback = (Button) this.findViewById(R.id.feedback);
		feedback.setOnClickListener(this);
		about = (Button) this.findViewById(R.id.about);
		about.setOnClickListener(this);
		version = (Button) this.findViewById(R.id.version);
		version.setOnClickListener(this);
		
		linear = (LinearLayout) findViewById(R.id.linear);
		switcher = (Switch) findViewById(R.id.switcher);
		switcher.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					linear.setVisibility(View.VISIBLE);
				}else {
					linear.setVisibility(View.INVISIBLE);
				}
				
			}
		});
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			this.finish();
			break;
		case R.id.feedback:
			startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+"07722801201")));
			break;
		case R.id.about:
			showDialog(1);
			break;
		case R.id.version:
			startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.6gang.com.cn/lugangclint.apk")));
			break;
		case R.id.logout:
			onLogout();
			break;
		}
		
	}
	
	public void onLogout(){
		Intent intent = new Intent(this, LoginActivity.class);
		intent.putExtra("dir", 0);
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
