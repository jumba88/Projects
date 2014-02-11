package com.honglang.lugang.billsearch;

import java.util.ArrayList;
import java.util.List;

import com.honglang.lugang.R;
import com.honglang.lugang.R.layout;
import com.honglang.lugang.R.menu;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
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
	private List<String> items;
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
		title.setText(R.string.record);
		
		mListView = (ListView) this.findViewById(R.id.list_search);
		items = new ArrayList<String>();
		items = this.getIntent().getExtras().getStringArrayList("record");
//		Log.i("suxoyo", ""+items.toString());
		adapter = new SearchAdapter(items, this);
		if (adapter != null) {
			mListView.setAdapter(adapter);
		}
		
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
