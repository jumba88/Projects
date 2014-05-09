package com.honglang.lugang.cityexpress;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.honglang.lugang.R;
import com.honglang.lugang.R.layout;
import com.honglang.lugang.R.menu;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class CrossActivity extends Activity implements OnClickListener {

	private TextView title;
	private Button back;
	
	private ListView mListView;
	private List<HashMap<String, String>> items;
	private LayoutInflater inflater;
	private CrossAdapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cross);
		
		init();
	}

	private void init(){
		title = (TextView) this.findViewById(R.id.title);
		title.setText("途经城市");
		back = (Button) this.findViewById(R.id.back);
		back.setOnClickListener(this);
		
		mListView = (ListView) findViewById(R.id.list);
		items = new ArrayList<HashMap<String,String>>();
		items = (List<HashMap<String, String>>) getIntent().getSerializableExtra("cross");
		Log.i("suxoyo", items.size()+"");
		inflater = this.getLayoutInflater();
		adapter = new CrossAdapter();
		if (adapter != null) {
			mListView.setAdapter(adapter);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;
		}
	}
	
	class CrossAdapter extends BaseAdapter{

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
			if(convertView == null){
				convertView = inflater.inflate(R.layout.crosscity, null);
			}
			HashMap<String, String> item = items.get(position);
			if(item == null){
				return null;
			}
			TextView city = (TextView) convertView.findViewById(R.id.city);
			TextView address = (TextView) convertView.findViewById(R.id.time);
			
			city.setText(item.get("city"));
			address.setText(item.get("address"));
			return convertView;
		}
		
	}

}
