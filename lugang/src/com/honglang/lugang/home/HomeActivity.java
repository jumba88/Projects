package com.honglang.lugang.home;

import com.honglang.lugang.HlApp;
import com.honglang.lugang.R;

import android.os.Bundle;
import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class HomeActivity extends  FragmentActivity implements OnClickListener{

	private FragmentManager fManager;
	private RadioGroup tabs;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		HlApp.getInstance().setHomeActivity(HomeActivity.this);
		tabs = (RadioGroup) this.findViewById(R.id.tabs);
		
		tabs.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				Fragment tab = null;
				switch (checkedId) {
				case R.id.one:
					tab = TabOne.newInstance();
					break;
				case R.id.two:
					tab = TabTwo.newInstance();
					break;
				case R.id.three:
					tab = TabThree.newInstance();
					break;
				case R.id.four:
					tab = TabFour.newInstance();
					break;
				}
				fManager = getSupportFragmentManager();
				FragmentTransaction transaction = fManager.beginTransaction();
				transaction.replace(R.id.content, tab).commit();
			}
		});
		((RadioButton)tabs.findViewById(R.id.one)).setChecked(true);
	}
	
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.home, menu);
//		return true;
//	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
	

}
