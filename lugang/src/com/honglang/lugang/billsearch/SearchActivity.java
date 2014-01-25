package com.honglang.lugang.billsearch;

import java.util.ArrayList;
import java.util.List;

import com.honglang.lugang.R;
import com.honglang.lugang.R.layout;
import com.honglang.lugang.R.menu;
import com.honglang.lugang.office.Bill;

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

public class SearchActivity extends Activity implements OnClickListener {

	private TextView title;
	private Button back;
	private ListView mListView;
	private Bill item;
	private List<Bill> items;
	private SearchAdapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		
		back = (Button) this.findViewById(R.id.back);
		back.setOnClickListener(this);
		init();
	}
	
	private void init(){
		title = (TextView) this.findViewById(R.id.title);
		title.setText(R.string.billsearch);
		
		mListView = (ListView) this.findViewById(R.id.list_search);
		item = new Bill();
		items = new ArrayList<Bill>();
		adapter = new SearchAdapter(items, this);
		
		for (int i = 0; i < 7; i++) {
			item.setCode("077219527");
			item.setSender("张三");
			item.setGetter("李四");
			items.add(item);
		}
		if (adapter != null) {
			mListView.setAdapter(adapter);
		}
		
		mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent(SearchActivity.this, BillDetailActivity.class);
				SearchActivity.this.startActivity(intent);
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
//		getMenuInflater().inflate(R.menu.search, menu);
//		return true;
//	}

}
