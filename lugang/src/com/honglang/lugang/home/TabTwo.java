package com.honglang.lugang.home;


import com.honglang.lugang.HlApp;
import com.honglang.lugang.R;
import com.honglang.lugang.SessionManager;
import com.honglang.lugang.issue.StuffListActivity;
import com.honglang.lugang.issue.TruckListActivity;
import com.honglang.lugang.login.UserActivity;
import com.honglang.lugang.office.DealingActivity;
import com.honglang.lugang.office.DoneActivity;
import com.honglang.zxing.CaptureActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class TabTwo extends Fragment implements OnClickListener {

	private HlApp app;
	
	private TextView title;
	private TextView user;
	private TextView type;
	private Button city;
	private Button dealing;
	private Button done;
	private Button blank;
	private Button stuff;
	private Button truck;
	
	private View info;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.i(getActivity().getClass().getName(), "onCreateView");
		return inflater.inflate(R.layout.tab2, null);
	}
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		app = (HlApp) getActivity().getApplication();
		
		title = (TextView) view.findViewById(R.id.title);
		title.setText(R.string.tab2);
		city = (Button) view.findViewById(R.id.city);
		city.setText(R.string.setting);
		city.setOnClickListener(this);
		city.setVisibility(View.VISIBLE);
		
		info = view.findViewById(R.id.info);
		info.setOnClickListener(this);
		
		user = (TextView) view.findViewById(R.id.user);
		user.setText(SessionManager.getInstance().getUsername());
		type = (TextView) view.findViewById(R.id.type);
		type.setText(SessionManager.getInstance().getUsertype());
		
		dealing = (Button) view.findViewById(R.id.dealing);
		dealing.setOnClickListener(this);
		done = (Button) view.findViewById(R.id.done);
		done.setOnClickListener(this);
		blank = (Button) view.findViewById(R.id.blank);
		blank.setOnClickListener(this);
		stuff = (Button) view.findViewById(R.id.stuff);
		stuff.setOnClickListener(this);
		truck = (Button) view.findViewById(R.id.truck);
		truck.setOnClickListener(this);
		
		if (SessionManager.getInstance().getUsertype().equals("物流企业")) {
			dealing.setText("待处理工单");
			done.setText("已完成工单");
			blank.setVisibility(View.VISIBLE);
		} else if(SessionManager.getInstance().getUsertype().equals("VIP会员")){
			dealing.setText("待处理事项");
			done.setText("已下单信息");
		}else {
			dealing.setVisibility(View.GONE);
			done.setVisibility(View.GONE);
		}
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.city:
			getActivity().startActivity(new Intent(getActivity(),SettingActivity.class));
			break;
		case R.id.dealing:
			if (!app.isNetworkConnected()) {
				Toast.makeText(getActivity(), "当前网络不可用，请检查网络设置", Toast.LENGTH_SHORT).show();
				return;
			}
			getActivity().startActivity(new Intent(getActivity(),DealingActivity.class));
			break;
		case R.id.done:
			if (!app.isNetworkConnected()) {
				Toast.makeText(getActivity(), "当前网络不可用，请检查网络设置", Toast.LENGTH_SHORT).show();
				return;
			}
			getActivity().startActivity(new Intent(getActivity(),DoneActivity.class));
			break;
		case R.id.info:
			if (!app.isNetworkConnected()) {
				Toast.makeText(getActivity(), "当前网络不可用，请检查网络设置", Toast.LENGTH_SHORT).show();
				return;
			}
			getActivity().startActivity(new Intent(getActivity(),UserActivity.class));
			break;
		case R.id.stuff:
			getActivity().startActivity(new Intent(getActivity(),StuffListActivity.class));
			break;
		case R.id.truck:
			getActivity().startActivity(new Intent(getActivity(),TruckListActivity.class));
			break;
		case R.id.blank:
			Intent i = new Intent(getActivity(),CaptureActivity.class);
			i.putExtra("QRTYPE", 4);
			getActivity().startActivity(i);
			break;
		}
	}
	
//	public static TabTwo newInstance(){
//		TabTwo tab = new TabTwo();
//		return tab;
//	}
}
