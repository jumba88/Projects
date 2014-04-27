package com.honglang.lugang.home;

import com.honglang.lugang.R;
import com.honglang.lugang.login.LoginActivity;
import com.honglang.lugang.out.FormListActivity;
import com.honglang.lugang.qrcode.HistoryActivity;
import com.honglang.zxing.CaptureActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class TabFour extends Fragment implements OnClickListener {

	private TextView title;
//	private Button history;
	private Button another;
	private Button out;
	private Button list;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		return inflater.inflate(R.layout.tab4, null);
	}
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		title = (TextView) view.findViewById(R.id.title);
		title.setText(R.string.tab4);
//		history = (Button) view.findViewById(R.id.history);
//		history.setOnClickListener(this);
		
		list = (Button) view.findViewById(R.id.list);
		list.setOnClickListener(this);
		out = (Button) view.findViewById(R.id.out);
		out.setOnClickListener(this);
		another = (Button) view.findViewById(R.id.another);
		another.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.out:
			Intent i = new Intent(getActivity(),CaptureActivity.class);
			i.putExtra("QRTYPE", 0);
			this.startActivity(i);
			break;
		case R.id.another:
			Intent intent = new Intent(getActivity(),CaptureActivity.class);
			intent.putExtra("QRTYPE", 6);
			this.startActivity(intent);
			break;
		case R.id.list:
			Intent in = new Intent(getActivity(),FormListActivity.class);
			this.startActivity(in);
			break;
//		case R.id.history:
//			Intent intent = new Intent(getActivity(),HistoryActivity.class);
//			intent.putExtra("type", 2);
//			this.startActivity(intent);
//			break;
		}
		
	}
}
