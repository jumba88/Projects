package com.honglang.lugang.home;

import com.honglang.lugang.Constant;
import com.honglang.lugang.HlApp;
import com.honglang.lugang.R;
import com.honglang.lugang.SessionManager;
import com.honglang.lugang.assign.AssignActivity;
import com.honglang.lugang.billsearch.BillDetailActivity;
import com.honglang.lugang.billsearch.SearchActivity;
import com.honglang.lugang.cityexpress.ExpressActivity;
import com.honglang.lugang.company.CompanyActivity;
import com.honglang.lugang.notice.NoticeActivity;
import com.honglang.lugang.truck.TruckActivity;
import com.honglang.lugang.ui.ClearEditText;
import com.honglang.zxing.CaptureActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class TabOne extends Fragment implements OnClickListener {

	private HlApp app;
	
	private TextView title;
	private Button qr;
	private Button city;
	private Button express;
	private Button company;
	private Button truck;
	private Button assign;
	private Button billsearch;
	private Button notice;
	private ClearEditText number;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		return inflater.inflate(R.layout.tab1, null);
	}
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		app = (HlApp) getActivity().getApplication();
		
		title = (TextView) view.findViewById(R.id.title);
		title.setText(R.string.tab1);
		city = (Button) view.findViewById(R.id.city);
		city.setText(SessionManager.getInstance().getCity());
		city.setVisibility(View.VISIBLE);
		
		qr = (Button) view.findViewById(R.id.qrsearch);
		express = (Button) view.findViewById(R.id.express);
		company = (Button) view.findViewById(R.id.company);
		truck = (Button) view.findViewById(R.id.truck);
		assign = (Button) view.findViewById(R.id.assign);
		billsearch = (Button) view.findViewById(R.id.btn);
		notice = (Button) view.findViewById(R.id.notice);
		number = (ClearEditText) view.findViewById(R.id.search);
		
		qr.setOnClickListener(this);
		express.setOnClickListener(this);
		company.setOnClickListener(this);
		truck.setOnClickListener(this);
		assign.setOnClickListener(this);
		billsearch.setOnClickListener(this);
		notice.setOnClickListener(this);
	}

//	public static TabOne newInstance(){
//		TabOne tab = new TabOne();
//		return tab;
//	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.city:
//			getActivity().startActivity(new Intent(getActivity(),SettingActivity.class));
			break;
		case R.id.qrsearch:
			if (!app.isNetworkConnected()) {
				Toast.makeText(getActivity(), "当前网络不可用，请检查网络设置", Toast.LENGTH_SHORT).show();
				return;
			}
			Intent i = new Intent(getActivity(),CaptureActivity.class);
			i.putExtra("QRTYPE", 3);
			getActivity().startActivity(i);
			break;
		case R.id.express:
			if (!app.isNetworkConnected()) {
				Toast.makeText(getActivity(), "当前网络不可用，请检查网络设置", Toast.LENGTH_SHORT).show();
				return;
			}
			getActivity().startActivity(new Intent(getActivity(),ExpressActivity.class));
			break;
		case R.id.company:
			if (!app.isNetworkConnected()) {
				Toast.makeText(getActivity(), "当前网络不可用，请检查网络设置", Toast.LENGTH_SHORT).show();
				return;
			}
			getActivity().startActivity(new Intent(getActivity(),CompanyActivity.class));
			break;
		case R.id.truck:
			if (!app.isNetworkConnected()) {
				Toast.makeText(getActivity(), "当前网络不可用，请检查网络设置", Toast.LENGTH_SHORT).show();
				return;
			}
			getActivity().startActivity(new Intent(getActivity(),TruckActivity.class));
			break;
		case R.id.assign:
			if (!app.isNetworkConnected()) {
				Toast.makeText(getActivity(), "当前网络不可用，请检查网络设置", Toast.LENGTH_SHORT).show();
				return;
			}
			getActivity().startActivity(new Intent(getActivity(),AssignActivity.class));
			break;
		case R.id.btn:
			if (!app.isNetworkConnected()) {
				Toast.makeText(getActivity(), "当前网络不可用，请检查网络设置", Toast.LENGTH_SHORT).show();
				return;
			}
			String code = number.getText().toString().trim();
			if (code != null && code.length() != 0) {
				Intent intent = new Intent(getActivity(),SearchActivity.class);
				intent.putExtra("number", code);
				getActivity().startActivity(intent);
			} else {
				Toast.makeText(getActivity(), "请输入运单号", Toast.LENGTH_SHORT).show();
				return;
			}
			break;
		case R.id.notice:
			if (!app.isNetworkConnected()) {
				Toast.makeText(getActivity(), "当前网络不可用，请检查网络设置", Toast.LENGTH_SHORT).show();
				return;
			}
			getActivity().startActivity(new Intent(getActivity(),NoticeActivity.class));
			break;
		}
		
	}
}
