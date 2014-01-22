package com.honglang.lugang.truck;

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

public class TruckActivity extends Activity implements OnClickListener {

	private TextView title;
	private Button back;
	private ListView mListView;
	private Truck item;
	private List<Truck> items;
	private TruckAdapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_truck);
		
		back = (Button) this.findViewById(R.id.back);
		back.setOnClickListener(this);
		init();
	}

	private void init(){
		title = (TextView) this.findViewById(R.id.title);
		title.setText(R.string.carinfo);
		
		mListView = (ListView) this.findViewById(R.id.list_truck);
		item = new Truck();
		items = new ArrayList<Truck>();
		adapter = new TruckAdapter(items, this);
		
		for (int i = 0; i < 20; i++) {
			item.setTruck("桂B9527");
			item.setLocation("柳州市");
			item.setPark("新概念停车场");
			item.setUpdate("2014/1/13");
			items.add(item);
		}
		
		if(adapter != null){
			mListView.setAdapter(adapter);
		}
		
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent(TruckActivity.this,TruckDetailActivity.class);
				TruckActivity.this.startActivity(intent);
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
//		getMenuInflater().inflate(R.menu.truck, menu);
//		return true;
//	}

}
