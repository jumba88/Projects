package com.honglang.lugang.office;

import java.text.DecimalFormat;

import com.honglang.lugang.Constant;
import com.honglang.lugang.R;
import com.honglang.lugang.qrcode.StuffActivity;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
/**
 * 计算运费对话框
 * @author Administrator
 *
 */
public class CalcMoneyDialog extends DialogFragment implements OnClickListener {

	private int weight;
	private double cubage;
	
	//cache the weightUnit position
	private int position;
	public Spinner unit;
	private static String[] UNIT;
	private ArrayAdapter<String> adapter;
	
	private TextView zl;
	private TextView all;
	private String allStr;
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
	
	DecimalFormat f = new DecimalFormat("0.00");
	public static CalcMoneyDialog newInstance(int from){
		CalcMoneyDialog frag = new CalcMoneyDialog();
		Bundle args = new Bundle();
        args.putInt("from", from);
        frag.setArguments(args);
		return frag;
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
		unit = (Spinner) view.findViewById(R.id.unit);
		zl = (TextView) view.findViewById(R.id.zl);
		all = (TextView) view.findViewById(R.id.all);
		totalMoney = (TextView) view.findViewById(R.id.totalMoney);
		
		if (getArguments().getInt("from") == 0) {
			bzf = Double.parseDouble(((CountActivity)getActivity()).bzf.getText().toString());
			thf = Double.parseDouble(((CountActivity)getActivity()).thf.getText().toString());
			shf = Double.parseDouble(((CountActivity)getActivity()).shf.getText().toString());
			bxf = Double.parseDouble(((CountActivity)getActivity()).bxf.getText().toString());
			weight = Integer.parseInt(((CountActivity)getActivity()).weight.getText().toString());
			cubage = Double.parseDouble(((CountActivity)getActivity()).cubage.getText().toString());
			position = ((CountActivity)getActivity()).weightUnit.getSelectedItemPosition();
		}
		if (getArguments().getInt("from") == 1) {
			bzf = Double.parseDouble(((StuffActivity)getActivity()).bzf.getText().toString());
			thf = Double.parseDouble(((StuffActivity)getActivity()).thf.getText().toString());
			shf = Double.parseDouble(((StuffActivity)getActivity()).shf.getText().toString());
			bxf = Double.parseDouble(((StuffActivity)getActivity()).bxf.getText().toString());
			weight = Integer.parseInt(((StuffActivity)getActivity()).weight.getText().toString());
			cubage = Double.parseDouble(((StuffActivity)getActivity()).cubage.getText().toString());
			position = ((StuffActivity)getActivity()).weightUnit.getSelectedItemPosition();
		}
		
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
		if (weight == 0 && cubage > 0) {//calc by cubage
			unit.setSelection(1, true);
			zl.setText("总量(m3)");
			all.setText(f.format(cubage));
			allStr = String.valueOf(cubage);
		}else{
			unit.setSelection(0, true);
			all.setText(weight+"");
			allStr = String.valueOf(weight);
			if (position == 0) {
				zl.setText("总量(t)");
			} else {
				zl.setText("总量(kg)");
			}
		}
		unit.setOnItemSelectedListener(new SpinnerSelectedListener());
		
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
			Log.i("suxoyo", "arg2="+arg2);
			switch (position) {
			case 0:
				if (arg2 == 0) {
					zl.setText("总量(t)");
					allStr = String.valueOf(weight);
				} else {
					zl.setText("总量(m3)");
					allStr = String.valueOf(cubage);
				}
				all.setText(allStr);
				break;
			case 1:
				if (arg2 == 0) {
					zl.setText("总量(kg)");
					allStr = String.valueOf(weight);
				} else {
					zl.setText("总量(m3)");
					allStr = String.valueOf(cubage);
				}
				all.setText(allStr);
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
			Log.i("suxoyo", allStr);
			double iall = Double.parseDouble(allStr);
			String mStr = money.getText().toString().trim();
			String dStr = distance.getText().toString().trim();
			if (Constant.isNum(mStr) && Constant.isNum(dStr)) {
				double imoney = Double.parseDouble(mStr);
				double idis = Double.parseDouble(dStr);
				double sum = iall * imoney * idis;
				
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
			if (getArguments().getInt("from") == 0) {
				((CountActivity)getActivity()).yf.setText(totalMoney.getText().toString());
				((CountActivity)getActivity()).zyf.setText(zong);
				String allUnit = zl.getText().toString().trim();
				if (allUnit.contains("t")) {
					((CountActivity)getActivity()).allUnit = "t";
				}
				if (allUnit.contains("kg")) {
					((CountActivity)getActivity()).allUnit = "kg";
				}
				if (allUnit.contains("m")) {
					((CountActivity)getActivity()).allUnit = "立方米";
				}
			}
			if (getArguments().getInt("from") == 1) {
				((StuffActivity)getActivity()).yf.setText(totalMoney.getText().toString());
				((StuffActivity)getActivity()).zyf.setText(zong);
				String allUnit = zl.getText().toString().trim();
				if (allUnit.contains("t")) {
					((StuffActivity)getActivity()).allUnit = "t";
				}
				if (allUnit.contains("kg")) {
					((StuffActivity)getActivity()).allUnit = "kg";
				}
				if (allUnit.contains("m³")) {
					((StuffActivity)getActivity()).allUnit = "立方米";
				}
			}
			dismiss();
			break;
		case R.id.cancel:
			dismiss();
			break;
		}
		
	}

}
