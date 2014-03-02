package com.honglang.lugang.qrcode;

import java.util.HashMap;
import java.util.List;

import com.honglang.lugang.Constant;
import com.honglang.lugang.R;
import com.honglang.lugang.R.layout;
import com.honglang.lugang.R.menu;

import android.os.Bundle;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class FormActivity extends Activity implements OnClickListener {

	private TextView title;
	private Button back;
	private Form form;
	
	private TextView stuffCode;
	private TextView form_id;
	private TextView sender;
	private TextView start;
	private TextView getter;
	private TextView end;
	
	private ListView mListView;
	private List<HashMap<String, String>> items;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_form);
		
		init();
	}

	private void init(){
		title = (TextView) this.findViewById(R.id.title);
		title.setText("订单详情");
		back = (Button) this.findViewById(R.id.back);
		back.setOnClickListener(this);
		form = (Form) this.getIntent().getExtras().getSerializable("form");
		
		stuffCode = (TextView) findViewById(R.id.stuffCode);
		form_id = (TextView) findViewById(R.id.form_id);
		sender = (TextView) findViewById(R.id.sender);
		start = (TextView) findViewById(R.id.start);
		getter = (TextView) findViewById(R.id.getter);
		end = (TextView) findViewById(R.id.end);
		
		stuffCode.setText(form.getInfo().getFhcode());
		form_id.setText(form.getInfo().getFormoid());
		sender.setText(form.getInfo().getFhrxingming());
		start.setText(form.getInfo().getFromcity());
		getter.setText(form.getInfo().getTyxingming());
		end.setText(form.getInfo().getTocity());
		
		mListView = (ListView) findViewById(R.id.stuffList);
		items = form.getStuff();
		mListView.setAdapter(new StuffAdapter());
		Constant.setListViewHeightBasedOnChildren(mListView);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;
		default:
			break;
		}
	}

	class StuffAdapter extends BaseAdapter{

		LayoutInflater inflater = FormActivity.this.getLayoutInflater();
		@Override
		public int getCount() {
			return items.size();
		}

		@Override
		public Object getItem(int position) {
			return items.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.form_item, null);
			}
			HashMap<String, String> item = items.get(position);
			if (item == null) {
				return null;
			}
			
			TextView type = (TextView) convertView.findViewById(R.id.stuffType);
			TextView name = (TextView) convertView.findViewById(R.id.stuffName);
			TextView count = (TextView) convertView.findViewById(R.id.count);
			TextView money = (TextView) convertView.findViewById(R.id.money);
			TextView truck = (TextView) convertView.findViewById(R.id.truck);
			
			type.setText(item.get("wplx"));
			name.setText(item.get("wpmc"));
			count.setText(item.get("sl")+item.get("sl_danwei"));
			money.setText(item.get("yunf"));
			truck.setText(item.get("chep"));
			return convertView;
		}
		
	}
	
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.form, menu);
//		return true;
//	}

}
