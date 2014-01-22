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
	public OfficeAdapter(List<Bill> items, Activity activity) {
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
		b.setText(item.getBill());
		time.setText(item.getTime());
		
		return convertView;
	}

}
