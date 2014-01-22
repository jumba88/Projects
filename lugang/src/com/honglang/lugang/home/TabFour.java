package com.honglang.lugang.home;

import com.honglang.lugang.R;
import com.honglang.lugang.login.LoginActivity;

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
	private Button logout;
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
		
		logout = (Button) view.findViewById(R.id.logout);
		logout.setOnClickListener(this);
	}
	
	public void onLogout(){
		Intent intent = new Intent(getActivity(), LoginActivity.class);
		getActivity().startActivity(intent);
		getActivity().finish();
	}

	public static TabFour newInstance(){
		TabFour tab = new TabFour();
		return tab;
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.logout:
			onLogout();
			break;

		}
		
	}
}
