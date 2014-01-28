package com.honglang.lugang.home;

import com.honglang.lugang.R;
import com.honglang.lugang.qrcode.HistoryActivity;

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
	private Button history;
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
		history = (Button) view.findViewById(R.id.history);
		history.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.history:
			Intent intent = new Intent(getActivity(),HistoryActivity.class);
			intent.putExtra("type", 1);
			this.startActivity(intent);
			break;
		}
		
	}

//	public static TabThree newInstance(){
//		TabThree tab = new TabThree();
//		return tab;
//	}
}
