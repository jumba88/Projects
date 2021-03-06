package com.honglang.lugang.home;

import com.honglang.lugang.R;
import com.honglang.lugang.qrcode.HistoryActivity;
import com.honglang.zxing.CaptureActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class TabThree extends Fragment implements OnClickListener {

	private TextView title;
//	private Button history;
	private Button in;
	private Button blank;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		return inflater.inflate(R.layout.tab3, null);
	}
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		title = (TextView) view.findViewById(R.id.title);
		title.setText(R.string.tab3);
//		history = (Button) view.findViewById(R.id.history);
//		history.setOnClickListener(this);
		in = (Button) view.findViewById(R.id.in);
		in.setOnClickListener(this);
		blank = (Button) view.findViewById(R.id.blank);
		blank.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
//		case R.id.history:
//			Intent intent = new Intent(getActivity(),HistoryActivity.class);
//			intent.putExtra("type", 1);
//			this.startActivity(intent);
//			break;
		case R.id.in:
			Intent i = new Intent(getActivity(),CaptureActivity.class);
			i.putExtra("QRTYPE", 1);
			this.startActivity(i);
			break;
		case R.id.blank:
			Intent b = new Intent(getActivity(),CaptureActivity.class);
			b.putExtra("QRTYPE", 2);
			this.startActivity(b);
			break;
		}
		
	}

//	public static TabThree newInstance(){
//		TabThree tab = new TabThree();
//		return tab;
//	}
}
