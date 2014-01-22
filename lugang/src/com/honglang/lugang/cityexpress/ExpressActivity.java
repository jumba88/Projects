package com.honglang.lugang.cityexpress;

import java.util.ArrayList;
import java.util.List;

import com.honglang.lugang.R;
import com.honglang.lugang.R.layout;
import com.honglang.lugang.R.menu;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class ExpressActivity extends Activity implements OnClickListener {

	private TextView title;
	private Button back;
	private ListView mListView;
	private Express item;
	private List<Express> items;
	private ExpressAdapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_express);
		
		back = (Button) this.findViewById(R.id.back);
		back.setOnClickListener(this);
		init();
	}

	private void init(){
		title = (TextView) this.findViewById(R.id.title);
		title.setText(R.string.express);
		
		mListView = (ListView) this.findViewById(R.id.list_express);
		item = new Express();
		items = new ArrayList<Express>();
		adapter = new ExpressAdapter(items, this);
		
		for (int i = 0; i < 20; i++) {
			item.setGoal("柳州--南宁");
			item.setPrice("12元");
			item.setLight("24元/立方");
			item.setHeavy("18元/公斤");
			items.add(item);
		}
		if(adapter != null){
			mListView.setAdapter(adapter);
		}
		
		mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent(ExpressActivity.this, ExpressDetailActivity.class);
				ExpressActivity.this.startActivity(intent);
			}
		});
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			this.finish();
			break;
		}
		
	}

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.express, menu);
//		return true;
//	}

}
