package com.honglang.lugang.office;

import java.text.DecimalFormat;

import com.honglang.lugang.Constant;
import com.honglang.lugang.R;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.TextView;

public class CalcMoneyDialog extends DialogFragment implements OnClickListener {

	private int weight;
	private int cubage;
	private int position;
	public Spinner unit;
	private static String[] UNIT = new String[]{"t","kg"};
	private ArrayAdapter<String> adapter;
	
	private TextView zl;
	private TextView all;
	private TextView totalMoney;
	
	private EditText money;
	private EditText distance;
	
	private Button confirm;
	private Button cancel;
	
	private Double bzf;
	private Double thf;
	private Double shf;
	private Double bxf;
	private String zong;
	public static CalcMoneyDialog newInstance(){
		return new CalcMoneyDialog();
	}
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		Dialog dialog = super.onCreateDialog(savedInstanceState);
		dialog.setTitle("运费=单价×总量×公里数");
		dialog.setCanceledOnTouchOutside(false);
		return dialog;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		return inflater.inflate(R.layout.calc_money, null);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		bzf = Double.parseDouble(((CountActivity)getActivity()).bzf.getText().toString());
		thf = Double.parseDouble(((CountActivity)getActivity()).thf.getText().toString());
		shf = Double.parseDouble(((CountActivity)getActivity()).shf.getText().toString());
		bxf = Double.parseDouble(((CountActivity)getActivity()).bxf.getText().toString());
		
		unit = (Spinner) view.findViewById(R.id.unit);
		weight = Integer.parseInt(((CountActivity)getActivity()).weight.getText().toString());
		cubage = Integer.parseInt(((CountActivity)getActivity()).cubage.getText().toString());
		position = ((CountActivity)getActivity()).weightUnit.getSelectedItemPosition();
		switch (position) {
		case 0:
			UNIT = new String[]{"重量(t)","体积(m3)"};
			break;
		case 1:
			UNIT = new String[]{"重量(kg)","体积(m3)"};
			break;
		}
		adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, UNIT);
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		unit.setAdapter(adapter);
		if (weight == 0 && cubage > 0) {
			unit.setSelection(1, true);
		}
		unit.setOnItemSelectedListener(new SpinnerSelectedListener());
		
		zl = (TextView) view.findViewById(R.id.zl);
		all = (TextView) view.findViewById(R.id.all);
		totalMoney = (TextView) view.findViewById(R.id.totalMoney);
		
		money = (EditText) view.findViewById(R.id.money);
		distance = (EditText) view.findViewById(R.id.distance);
		money.addTextChangedListener(new CalcWatcher());
		distance.addTextChangedListener(new CalcWatcher());
		
		confirm = (Button) view.findViewById(R.id.confirm);
		cancel = (Button) view.findViewById(R.id.cancel);
		confirm.setOnClickListener(this);
		cancel.setOnClickListener(this);
		super.onViewCreated(view, savedInstanceState);
	}
	
	class SpinnerSelectedListener implements OnItemSelectedListener{

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			switch (position) {
			case 0:
				if (arg2 == 0) {
					zl.setText("总量(t)");
					all.setText(String.valueOf(weight));
				} else {
					zl.setText("总量(m3)");
					all.setText(String.valueOf(cubage));
				}
				break;
			case 1:
				if (arg2 == 0) {
					zl.setText("总量(kg)");
					all.setText(String.valueOf(weight));
				} else {
					zl.setText("总量(m3)");
					all.setText(String.valueOf(cubage));
				}
				break;
			}
		}
		
		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			
		}
		
	}
	
	class CalcWatcher implements TextWatcher{

		@Override
		public void afterTextChanged(Editable s) {
			int iall = Integer.parseInt(all.getText().toString());
			String mStr = money.getText().toString().trim();
			String dStr = distance.getText().toString().trim();
			if (Constant.isNum(mStr) && Constant.isNum(dStr)) {
				double imoney = Double.parseDouble(mStr);
				double idis = Double.parseDouble(dStr);
				double sum = iall * imoney * idis;
				DecimalFormat f = new DecimalFormat("0.00");
				totalMoney.setText(f.format(sum));
				zong = f.format(sum + bzf + thf +shf + bxf);
			} else {
				totalMoney.setText("0");
			}
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			
		}
		
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.confirm:
			((CountActivity)getActivity()).yf.setText(totalMoney.getText().toString());
			((CountActivity)getActivity()).zyf.setText(zong);
			String allUnit = zl.getText().toString().trim();
			if (allUnit.contains("t")) {
				((CountActivity)getActivity()).allUnit.setText("t");
			}
			if (allUnit.contains("kg")) {
				((CountActivity)getActivity()).allUnit.setText("kg");
			}
			if (allUnit.contains("m3")) {
				((CountActivity)getActivity()).allUnit.setText("立方米");
			}
			dismiss();
			break;
		case R.id.cancel:
			dismiss();
			break;
		}
		
	}

}
