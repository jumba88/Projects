package com.honglang.lugang.billsearch;

import java.util.HashMap;
import java.util.List;

import com.honglang.lugang.R;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class StuffAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private List<HashMap<String, String>> items;
	private Activity activity;
	public StuffAdapter(List<HashMap<String, String>> items, Activity activity) {
		super();
		this.items = items;
		this.activity = activity;
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
			convertView = inflater.inflate(R.layout.stuff_item, null);
		}
		HashMap<String, String> item = items.get(position);
		if(item == null){
			return null;
		}
		
		TextView name = (TextView) convertView.findViewById(R.id.name);
		TextView unit = (TextView) convertView.findViewById(R.id.unit);
		TextView total = (TextView) convertView.findViewById(R.id.total);
		TextView send = (TextView) convertView.findViewById(R.id.send);
		TextView notSend = (TextView) convertView.findViewById(R.id.notSend);
		TextView sent = (TextView) convertView.findViewById(R.id.sent);
		
		name.setText(item.get("wpmc"));
		unit.setText(item.get("sl_danwei"));
		total.setText(item.get("sl"));
		send.setText(item.get("yfsl"));
		notSend.setText(item.get("wfsl"));
		sent.setText(item.get("yydsl"));
		return convertView;
	}

}
