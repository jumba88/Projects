package com.honglang.lugang.office;

import java.util.ArrayList;
import java.util.List;

import com.honglang.lugang.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class DoneFragment extends Fragment {

	private List<Bill> bills;
	private Bill bill;
	private OfficeAdapter adapter;
	private ListView mListView;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.done_content, null);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		bills = new ArrayList<Bill>();
		bill = new Bill();
		for(int i=0; i<5; i++){
			bill.setBill("·¢»õÂë" + (20140116+i));
			bill.setTime("2014");
			bills.add(bill);
		}
		
		adapter = new OfficeAdapter(bills, getActivity());
		mListView = (ListView) view.findViewById(R.id.list_handling);
		if(adapter != null){
			mListView.setAdapter(adapter);
		}
	}

}
