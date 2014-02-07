package com.honglang.lugang.notice;

import java.util.List;

import com.honglang.lugang.R;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class NoticeAdapter extends BaseAdapter {

	private List<Notice> items;
	private LayoutInflater inflater;
	private Activity activity;
	public NoticeAdapter(List<Notice> items, Activity activity) {
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
		if (arg1 == null) {
			arg1 = inflater.inflate(R.layout.notice_item, null);
		}
		Notice item = items.get(arg0);
		if (item == null) {
			return null;
		}
		TextView notice = (TextView) arg1.findViewById(R.id.notice);
		TextView date = (TextView) arg1.findViewById(R.id.date);
		
		notice.setText(item.getTitle());
		date.setText(item.getAddtime());
		return arg1;
	}

}
