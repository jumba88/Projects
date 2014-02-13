package com.honglang.lugang.office;

import java.util.List;

import com.honglang.lugang.R;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class OrderAdapter extends BaseAdapter {

	private List<Order> items;
	private Activity activity;
	private LayoutInflater inflater;
	public OrderAdapter(List<Order> items, Activity activity) {
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
			convertView = inflater.inflate(R.layout.order_item, null);
		}
		Order item = items.get(position);
		if(item == null){
			return null;
		}
		TextView type = (TextView) convertView.findViewById(R.id.stuffType);
		TextView name = (TextView) convertView.findViewById(R.id.stuffName);
		type.setText(item.getWplx());
		name.setText(item.getWpmc());
		return convertView;
	}

}
