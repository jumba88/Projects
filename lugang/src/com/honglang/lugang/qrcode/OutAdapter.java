package com.honglang.lugang.qrcode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.honglang.lugang.R;

import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

public class OutAdapter extends BaseAdapter {

	private Activity activity;
	private List<HashMap<String, String>> items;
	private LayoutInflater inflater;
	
	private Boolean isChecked = false;
	public List<Integer> selected;
	public List<String> ids;
	public OutAdapter(Activity activity, List<HashMap<String, String>> items) {
		super();
		this.activity = activity;
		this.items = items;
		inflater = activity.getLayoutInflater();
		
		selected = new ArrayList<Integer>();
		ids = new ArrayList<String>();
		for (int i = 0; i < items.size(); i++) {
			selected.add(0);
			ids.add(items.get(i).get("OID"));
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
		final int pos = position;
		if(convertView == null){
			convertView = inflater.inflate(R.layout.out_item, null);
		}
		HashMap<String, String> item = items.get(position);
		if(item == null){
			return null;
		}
		
		TextView oid = (TextView) convertView.findViewById(R.id.form_id);
		TextView name = (TextView) convertView.findViewById(R.id.stuff_name);
		TextView weight = (TextView) convertView.findViewById(R.id.weight);
		TextView cubage = (TextView) convertView.findViewById(R.id.cubage);
		TextView count = (TextView) convertView.findViewById(R.id.count);
		CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.checkBox);
		EditText edit = (EditText) convertView.findViewById(R.id.edit);
		
		checkBox.setChecked(isChecked);
		
		oid.setText(item.get("OID"));
		name.setText(item.get("wpmc"));
		weight.setText(item.get("zl")+item.get("zl_danwei"));
		cubage.setText(item.get("tiji")+item.get("tiji_danwei"));
		count.setText(item.get("sl")+item.get("sl_danwei"));
		edit.setText(item.get("sl"));
		edit.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				ids.set(pos, s.toString());
			}
		});
		
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
		
		return convertView;
	}

}
