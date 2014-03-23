package com.honglang.lugang.out;

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
import android.widget.Toast;

public class OutAdapter extends BaseAdapter {

	private Activity activity;
	private List<HashMap<String, String>> items;
	private LayoutInflater inflater;
	
	public List<Integer> list;
	
//	private Boolean isChecked = false;
//	public List<Integer> selected;
//	public List<String> ids;
	public OutAdapter(Activity activity, List<HashMap<String, String>> items) {
		super();
		this.activity = activity;
		this.items = items;
		inflater = activity.getLayoutInflater();
		
		list = new ArrayList<Integer>();
		for (int i = 0; i < items.size(); i++) {
			list.add(Integer.parseInt(items.get(i).get("zongliang")));
		}
//		selected = new ArrayList<Integer>();
//		ids = new ArrayList<String>();
//		for (int i = 0; i < items.size(); i++) {
//			selected.add(0);
//			ids.add(items.get(i).get("oid"));
//		}
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
		final HashMap<String, String> item = items.get(position);
		if(item == null){
			return null;
		}
		
		TextView code = (TextView) convertView.findViewById(R.id.code);
		TextView city = (TextView) convertView.findViewById(R.id.city);
		TextView oid = (TextView) convertView.findViewById(R.id.rkid);
		TextView name = (TextView) convertView.findViewById(R.id.stuff_name);
		TextView weight = (TextView) convertView.findViewById(R.id.weight);
		TextView cubage = (TextView) convertView.findViewById(R.id.cubage);
		TextView count = (TextView) convertView.findViewById(R.id.count);
//		CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.checkBox);
		EditText edit = (EditText) convertView.findViewById(R.id.edit);
		
//		checkBox.setChecked(isChecked);
		
		final String number = item.get("yusl");
		code.setText(item.get("fhcode"));
		city.setText(item.get("tocity"));
		oid.setText(item.get("rkid"));
		name.setText(item.get("wpmc"));
		weight.setText(item.get("zongliang")+item.get("zongliangdanwei"));
		cubage.setText(item.get("tiji"));
		count.setText(number+item.get("huoh"));
		edit.setText(number);
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
				if (s != null && s.length() > 0) {
					if (Integer.parseInt(s.toString()) > Integer.parseInt(item.get("yusl"))) {
						Toast.makeText(activity, "不能大于预装数量"+number, Toast.LENGTH_LONG).show();
						s.replace(0, s.length(), item.get("yusl"));
						return;
					}else {
						list.set(pos, Integer.parseInt(s.toString()));
					}
				}
//				ids.set(pos, s.toString());
			}
		});
		
//		checkBox.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				if (isChecked) {
//					isChecked = false;
//					selected.set(pos, 0);
//				} else {
//					isChecked = true;
//					selected.set(pos, 1);
//				}
//			}
//		});
		
		return convertView;
	}

}
