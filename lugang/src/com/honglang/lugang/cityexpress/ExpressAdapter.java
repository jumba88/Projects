package com.honglang.lugang.cityexpress;

import java.util.List;

import com.honglang.lugang.R;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ExpressAdapter extends BaseAdapter {

	private List<Express> items;
	private LayoutInflater inflater;
	private Activity activity;
	public ExpressAdapter(List<Express> items, Activity activity) {
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
			convertView = inflater.inflate(R.layout.express_item, null);
		}
		Express item = items.get(position);
		if(item == null){
			return null;
		}
		
		TextView num = (TextView) convertView.findViewById(R.id.number);
		TextView goal = (TextView) convertView.findViewById(R.id.go);
		TextView price = (TextView) convertView.findViewById(R.id.price);
		TextView heavy = (TextView) convertView.findViewById(R.id.heavy);
		TextView light = (TextView) convertView.findViewById(R.id.light);
		
		num.setText((position+1) + "");
		goal.setText(item.getGoal());
		price.setText(item.getPrice());
		heavy.setText(item.getHeavy());
		light.setText(item.getLight());
	
		return convertView;
	}

}
