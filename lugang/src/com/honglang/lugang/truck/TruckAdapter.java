package com.honglang.lugang.truck;

import java.util.List;

import com.honglang.lugang.R;
import com.honglang.lugang.cityexpress.Express;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class TruckAdapter extends BaseAdapter {

	private List<Truck> items;
	private LayoutInflater inflater;
	private Activity activity;
	public TruckAdapter(List<Truck> items, Activity activity) {
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
			convertView = inflater.inflate(R.layout.truck_item, null);
		}
		Truck item = items.get(position);
		if(item == null){
			return null;
		}
		
		TextView num = (TextView) convertView.findViewById(R.id.number);
		TextView name = (TextView) convertView.findViewById(R.id.truck);
		TextView price = (TextView) convertView.findViewById(R.id.loation);
		TextView heavy = (TextView) convertView.findViewById(R.id.park);
		TextView light = (TextView) convertView.findViewById(R.id.update);
		
		num.setText((position+1) + "");
		name.setText(item.getChep());
		price.setText(item.getStartaddr());
		heavy.setText(item.getEndaddr());
		light.setText(item.getAdddate());
	
		return convertView;
	}

}
