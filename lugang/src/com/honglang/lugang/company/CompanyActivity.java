package com.honglang.lugang.company;

import java.util.ArrayList;
import java.util.List;

import com.honglang.lugang.R;
import com.honglang.lugang.R.layout;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class CompanyActivity extends Activity implements OnClickListener {

	private TextView title;
	private Button back;
	private ListView mListView;
	private Company item;
	private List<Company> items;
	private CompanyAdapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_company);
		
		back = (Button) this.findViewById(R.id.back);
		back.setOnClickListener(this);
		init();
	}

	private void init(){
		title = (TextView) this.findViewById(R.id.title);
		title.setText(R.string.company);
		
		mListView = (ListView) this.findViewById(R.id.list_company);
		item = new Company();
		items = new  ArrayList<Company>();
		adapter = new CompanyAdapter(items, this);
		
		for (int i = 0; i < 20; i++) {
			item.setName("新概念物流");
			item.setPhone("0772-1010110");
			item.setAddress("广西柳州市新概念物流中心");
			items.add(item);
		}
		
		if(adapter != null){
			mListView.setAdapter(adapter);
		}
		
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent(CompanyActivity.this,CompanyDetailActivity.class);
				CompanyActivity.this.startActivity(intent);
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
//		getMenuInflater().inflate(R.menu.company, menu);
//		return true;
//	}

}
