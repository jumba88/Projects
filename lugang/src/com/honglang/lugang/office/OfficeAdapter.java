package com.honglang.lugang.office;

import java.util.List;

import com.honglang.lugang.R;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class OfficeAdapter extends BaseAdapter {

	private List<Bill> items;
	private LayoutInflater inflater;
	private Activity activity;
	private int type;
	public OfficeAdapter(List<Bill> items, Activity activity, int type) {
		super();
		this.items = items;
		this.activity = activity;
		this.type = type;
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
			convertView = inflater.inflate(R.layout.office_item, null);
		}
		Bill item = items.get(position);
		if(item == null){
			return null;
		}
		
		TextView num = (TextView) convertView.findViewById(R.id.number);
		TextView b = (TextView) convertView.findViewById(R.id.bill);
		TextView time = (TextView) convertView.findViewById(R.id.time);
		
		num.setText((position+1) + "");
		b.setText(item.getTitle());
		if (item.getCurrent_node_id().equals("7") || item.getCurrent_node_id().equals("9")) {
			b.setTextColor(activity.getResources().getColor(android.R.color.holo_red_dark));
		} else {
			b.setTextColor(activity.getResources().getColor(android.R.color.black));
		}
		if (type == 0) {
			time.setText(item.getTrun_time());
		} else {
			time.setText(item.getDone_time());
		}
		
		
		return convertView;
	}

}
