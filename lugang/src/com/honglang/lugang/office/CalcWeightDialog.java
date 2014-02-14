package com.honglang.lugang.office;

import com.honglang.lugang.R;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class CalcWeightDialog extends DialogFragment {

	public static CalcWeightDialog newInstance(){
		return new CalcWeightDialog();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
//		getDialog().setTitle("总重量计算(最多6项)");
		return inflater.inflate(R.layout.calc_weight, null);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
	}

}
