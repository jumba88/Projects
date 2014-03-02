package com.honglang.lugang.qrcode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.honglang.lugang.R;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

public class InAdapter extends BaseAdapter {

	private Activity activity;
	private List<Form> items;
	private LayoutInflater inflater;
	private Boolean isChecked = false;
	
	public List<Integer> selected;
	public InAdapter(Activity activity, List<Form> items) {
		super();
		this.activity = activity;
		this.items = items;
		inflater = activity.getLayoutInflater();
		
		selected = new ArrayList<Integer>();
		for (int i = 0; i < items.size(); i++) {
			selected.add(0);
		}
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
			convertView = inflater.inflate(R.layout.qr_item, null);
		}
		Form item = items.get(position);
		if(item == null){
			return null;
		}
		CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.checkBox);
		checkBox.setChecked(isChecked);
		final int pos = position;
		checkBox.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (isChecked) {
					isChecked = false;
					selected.set(pos, 0);
				} else {
					isChecked = true;
					selected.set(pos, 1);
				}
			}
		});
		
		TextView id = (TextView) convertView.findViewById(R.id.form_id);
		TextView name = (TextView) convertView.findViewById(R.id.stuff_name);
		Info info = item.getInfo();
		List<HashMap<String, String>> list = item.getStuff();
		String str = null;
		for (int i = 0; i < list.size(); i++) {
			if (str == null) {
				str = list.get(i).get("wpmc");
			} else {
				str = str + "\n" + list.get(i).get("wpmc");
			}
		}
		id.setText(info.getFormoid());
		name.setText(str);
		return convertView;
	}

}
