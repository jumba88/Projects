package com.honglang.lugang.home;

import com.honglang.lugang.R;
import com.honglang.lugang.assign.AssignActivity;
import com.honglang.lugang.billsearch.SearchActivity;
import com.honglang.lugang.cityexpress.ExpressActivity;
import com.honglang.lugang.company.CompanyActivity;
import com.honglang.lugang.notice.NoticeActivity;
import com.honglang.lugang.truck.TruckActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class TabOne extends Fragment implements OnClickListener {

	private TextView title;
	private Button express;
	private Button company;
	private Button truck;
	private Button assign;
	private Button billsearch;
	private Button notice;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		return inflater.inflate(R.layout.tab1, null);
	}
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		title = (TextView) view.findViewById(R.id.title);
		title.setText(R.string.tab1);
		
		express = (Button) view.findViewById(R.id.express);
		company = (Button) view.findViewById(R.id.company);
		truck = (Button) view.findViewById(R.id.truck);
		assign = (Button) view.findViewById(R.id.assign);
		billsearch = (Button) view.findViewById(R.id.billsearch);
		notice = (Button) view.findViewById(R.id.notice);
		
		express.setOnClickListener(this);
		company.setOnClickListener(this);
		truck.setOnClickListener(this);
		assign.setOnClickListener(this);
		billsearch.setOnClickListener(this);
		notice.setOnClickListener(this);
	}

	public static TabOne newInstance(){
		TabOne tab = new TabOne();
		return tab;
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.express:
			getActivity().startActivity(new Intent(getActivity(),ExpressActivity.class));
			break;
		case R.id.company:
			getActivity().startActivity(new Intent(getActivity(),CompanyActivity.class));
			break;
		case R.id.truck:
			getActivity().startActivity(new Intent(getActivity(),TruckActivity.class));
			break;
		case R.id.assign:
			getActivity().startActivity(new Intent(getActivity(),AssignActivity.class));
			break;
		case R.id.billsearch:
			getActivity().startActivity(new Intent(getActivity(),SearchActivity.class));
			break;
		case R.id.notice:
			getActivity().startActivity(new Intent(getActivity(),NoticeActivity.class));
			break;
		}
		
	}
}
