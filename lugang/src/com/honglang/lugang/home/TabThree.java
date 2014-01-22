package com.honglang.lugang.home;

import com.honglang.lugang.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class TabThree extends Fragment {

	private TextView title;
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
	}

	public static TabThree newInstance(){
		TabThree tab = new TabThree();
		return tab;
	}
}
