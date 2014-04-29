package com.honglang.lugang.out;

import java.util.HashMap;
import java.util.List;

import com.honglang.lugang.R;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class PreviewAdapter extends BaseAdapter {

	private List<HashMap<String, String>> items;
	private LayoutInflater inflater;
	private Activity activity;
	public PreviewAdapter(List<HashMap<String, String>> items, Activity activity) {
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
			convertView = inflater.inflate(R.layout.preview_item, null);
		}
		HashMap<String, String> item = items.get(position);
		if(item == null){
			return null;
		}
		
		TextView rkid = (TextView) convertView.findViewById(R.id.rkid);
		TextView fhcode = (TextView) convertView.findViewById(R.id.fhcode);
		TextView keycode = (TextView) convertView.findViewById(R.id.keycode);
		TextView name = (TextView) convertView.findViewById(R.id.name);
		TextView to = (TextView) convertView.findViewById(R.id.to);
		TextView total = (TextView) convertView.findViewById(R.id.total);
		TextView weight = (TextView) convertView.findViewById(R.id.weight);
		TextView cubage = (TextView) convertView.findViewById(R.id.cubage);
		
		rkid.setText(item.get("rkid"));
		fhcode.setText(item.get("fhcode"));
		keycode.setText(item.get("keycode"));
		name.setText(item.get("wpmc"));
		to.setText(item.get("tocity"));
		total.setText(item.get("sl")+item.get("huoh"));
		weight.setText(item.get("zongliang")+item.get("zongliangdanwei"));
		cubage.setText(item.get("tiji"));
		return convertView;
	}

}
