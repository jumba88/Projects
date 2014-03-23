package com.honglang.lugang.out;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.honglang.lugang.R;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class FormAdapter extends BaseAdapter {

	private Activity activity;
	private List<HashMap<String, String>> items;
	private LayoutInflater inflater;
	
	public FormAdapter(Activity activity, List<HashMap<String, String>> items) {
		super();
		this.activity = activity;
		this.items = items;
		
		inflater = activity.getLayoutInflater();
	}

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
			convertView = inflater.inflate(R.layout.form_list_item, null);
		}
		final HashMap<String, String> item = items.get(position);
		if(item == null){
			return null;
		}
		
		TextView code = (TextView) convertView.findViewById(R.id.code);
		TextView truck = (TextView) convertView.findViewById(R.id.truck);
		TextView driver = (TextView) convertView.findViewById(R.id.driver);
		TextView tel = (TextView) convertView.findViewById(R.id.tel);
		TextView user = (TextView) convertView.findViewById(R.id.user);
		TextView date = (TextView) convertView.findViewById(R.id.date);
		
		code.setText(item.get("keycode"));
		truck.setText(item.get("chep"));
		driver.setText(item.get("sij"));
		tel.setText(item.get("sjdh"));
		user.setText(item.get("usid"));
		date.setText(item.get("adddate"));
		
		return convertView;
	}

}
