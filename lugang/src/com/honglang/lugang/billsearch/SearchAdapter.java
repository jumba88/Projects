package com.honglang.lugang.billsearch;

import java.util.List;

import com.honglang.lugang.R;
import com.honglang.lugang.office.Bill;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SearchAdapter extends BaseAdapter {

	private List<String> items;
	private LayoutInflater inflater;
	private Activity activity;
	
	public SearchAdapter(List<String> items, Activity activity) {
		super();
		this.items = items;
		this.activity = activity;
		inflater = activity.getLayoutInflater();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return items.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return items.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		if(arg1 == null){
			arg1 = inflater.inflate(R.layout.search_item, null);
		}
		String item = items.get(arg0);
		if(item == null){
			return null;
		}
		TextView num = (TextView) arg1.findViewById(R.id.text);
		
		num.setText(item.toString());
		return arg1;
	}

}
