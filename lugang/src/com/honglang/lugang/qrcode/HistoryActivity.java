package com.honglang.lugang.qrcode;

import java.util.ArrayList;
import java.util.List;

import com.honglang.lugang.R;
import com.honglang.lugang.R.layout;
import com.honglang.lugang.notice.Notice;
import com.honglang.lugang.notice.NoticeActivity;
import com.honglang.lugang.notice.NoticeAdapter;
import com.honglang.lugang.notice.NoticeDetailActivity;

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
import android.widget.AdapterView.OnItemClickListener;

public class HistoryActivity extends Activity implements OnClickListener {

	private int type;
	private TextView title;
	private Button back;
	private ListView mListView;
	private Notice item;
	private List<Notice> items;
	private NoticeAdapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_history);
		type = this.getIntent().getExtras().getInt("type", 0);
		init();
	}
	
	private void init(){
		back = (Button) this.findViewById(R.id.back);
		back.setOnClickListener(this);
		title = (TextView) this.findViewById(R.id.title);
		if(type == 1){
			title.setText(R.string.singlein);
		}else{
			title.setText(R.string.singleout);
		}
		
		
		mListView = (ListView) this.findViewById(R.id.list_notice);
		item = new Notice();
		items = new ArrayList<Notice>();
		adapter = new NoticeAdapter(items, this);
		
		for (int i = 0; i < 5; i++) {
			item.setTitle("2014012400000867");
			item.setAddtime("2014/01/13");
			items.add(item);
		}
		if (adapter != null) {
			mListView.setAdapter(adapter);
		}
		
//		mListView.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
//					long arg3) {
//				Intent intent = new Intent(HistoryActivity.this,NoticeDetailActivity.class);
//				HistoryActivity.this.startActivity(intent);
//			}
//		});
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
//		getMenuInflater().inflate(R.menu.history, menu);
//		return true;
//	}

}
