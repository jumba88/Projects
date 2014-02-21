package com.honglang.lugang.home;

import java.util.ArrayList;
import java.util.List;

import com.honglang.lugang.R;
import com.honglang.lugang.SessionManager;
import com.honglang.lugang.office.DoneFragment;
import com.honglang.lugang.office.HandlingFragment;
import com.viewpagerindicator.TabPageIndicator;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class TabTwo extends Fragment {

	private TextView title;
	private TextView user;
	private TextView type;
	private Button city;
	private static String[] CONTENT;
	private List<Fragment> fragments;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.i(getActivity().getClass().getName(), "onCreateView");
		return inflater.inflate(R.layout.tab2, null);
	}
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		title = (TextView) view.findViewById(R.id.title);
		title.setText(R.string.tab2);
		city = (Button) view.findViewById(R.id.city);
		city.setText(SessionManager.getInstance().getCity());
		city.setVisibility(View.VISIBLE);
		user = (TextView) view.findViewById(R.id.user);
		user.setText(SessionManager.getInstance().getUsername());
		type = (TextView) view.findViewById(R.id.type);
		type.setText(SessionManager.getInstance().getUsertype());
		
		if (SessionManager.getInstance().getUsertype().equals("物流企业")) {
			CONTENT = new String[]{"待处理工单","已完成工单"};
		} else {
			CONTENT = new String[]{"待处理事项","已下单信息"};
		}
		
		fragments = new ArrayList<Fragment>();
		fragments.add(new HandlingFragment());
		fragments.add(new DoneFragment());
		Log.i(getActivity().getClass().getName(), "onViewCreated" + fragments.size());
		
		FragmentPagerAdapter adapter = new BillAdapter(this.getChildFragmentManager());
		ViewPager pager = (ViewPager)view.findViewById(R.id.pager);
        pager.setAdapter(adapter);

        TabPageIndicator indicator = (TabPageIndicator)view.findViewById(R.id.indicator);
        indicator.setViewPager(pager);
	}
	
	class BillAdapter extends FragmentPagerAdapter{

		public BillAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int arg0) {
			
			return fragments.get(arg0);
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return CONTENT[position % CONTENT.length].toUpperCase();
		}

		@Override
		public int getCount() {
			return CONTENT.length;
		}
		
	}
	
//	public static TabTwo newInstance(){
//		TabTwo tab = new TabTwo();
//		return tab;
//	}
}
