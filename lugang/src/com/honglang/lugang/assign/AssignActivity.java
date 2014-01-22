package com.honglang.lugang.assign;

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
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class AssignActivity extends Activity implements OnClickListener {

	private TextView title;
	private Button back;
	private ListView mListView;
	private Assign item;
	private List<Assign> items;
	private AssignAdapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_assign);
		
		back = (Button) this.findViewById(R.id.back);
		back.setOnClickListener(this);
		init();
	}

	private void init(){
		title = (TextView) this.findViewById(R.id.title);
		title.setText(R.string.lastet);
		
		mListView = (ListView) this.findViewById(R.id.list_assign);
		item = new Assign();
		items = new ArrayList<Assign>();
		adapter = new AssignAdapter(items, this);
		
		for (int i = 0; i < 20; i++) {
			item.setStuff("电冰箱");
			item.setFrom("广西-柳州市-城中区");
			item.setTo("广东-深圳市-罗湖区");
			item.setTotal("20t");
			items.add(item);
		}
		if(adapter != null){
			mListView.setAdapter(adapter);
		}
		
		mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent(AssignActivity.this,StuffDetailActivity.class);
				AssignActivity.this.startActivity(intent);
				
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
//		getMenuInflater().inflate(R.menu.assign, menu);
//		return true;
//	}

}
