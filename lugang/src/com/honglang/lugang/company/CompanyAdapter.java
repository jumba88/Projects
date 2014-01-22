package com.honglang.lugang.company;

import java.util.List;

import com.honglang.lugang.R;
import com.honglang.lugang.cityexpress.Express;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CompanyAdapter extends BaseAdapter {

	private List<Company> items;
	private LayoutInflater inflater;
	private Activity activity;
	public CompanyAdapter(List<Company> items, Activity activity) {
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
			convertView = inflater.inflate(R.layout.company_item, null);
		}
		Company item = items.get(position);
		if(item == null){
			return null;
		}
		
		TextView num = (TextView) convertView.findViewById(R.id.number);
		TextView goal = (TextView) convertView.findViewById(R.id.name);
		TextView price = (TextView) convertView.findViewById(R.id.phone);
		TextView heavy = (TextView) convertView.findViewById(R.id.address);
		
		num.setText((position+1) + "");
		goal.setText(item.getName());
		price.setText(item.getPhone());
		heavy.setText(item.getAddress());
	
		return convertView;
	}

}
