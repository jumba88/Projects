package com.honglang.lugang.office;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
/**
 * 计算重量对话框
 * @author Administrator
 *
 */
public class CalcWeightDialog extends DialogFragment implements OnClickListener {

	private EditText let01;
	private EditText let02;
	private EditText let11;
	private EditText let12;
	private EditText let21;
	private EditText let22;
	private EditText let31;
	private EditText let32;
	private EditText let41;
	private EditText let42;
	private EditText let51;
	private EditText let52;
	private EditText extra;
	
	private View line1;
	private View line2;
	private View line3;
	private View line4;
	private View line5;
	
	private TextView line_result;
	private TextView line1_result;
	private TextView line2_result;
	private TextView line3_result;
	private TextView line4_result;
	private TextView line5_result;
	private TextView extra_result;
	private TextView total;
	
	private TextView unitText;
	private TextView unitExtra;
	private TextView unit01;
	private TextView unit02;
	private TextView unit11;
	private TextView unit12;
	private TextView unit21;
	private TextView unit22;
	private TextView unit31;
	private TextView unit32;
	private TextView unit41;
	private TextView unit42;
	private TextView unit51;
	private TextView unit52;
	
	private Button confirm;
	private Button cancel;
	
	private int unit;
	public static CalcWeightDialog newInstance(int unit, int from){
		CalcWeightDialog frag = new CalcWeightDialog();
		Bundle args = new Bundle();
        args.putInt("unit", unit);
        args.putInt("from", from);
        frag.setArguments(args);
		return frag;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		Dialog dialog = super.onCreateDialog(savedInstanceState);
		dialog.setTitle("总重量计算(最多6项)");
		dialog.setCanceledOnTouchOutside(false);
		return dialog;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.calc_weight, null);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		unit = getArguments().getInt("unit");
		unit01 = (TextView) view.findViewById(R.id.unit01);
		unit02 = (TextView) view.findViewById(R.id.unit02);
		unit11 = (TextView) view.findViewById(R.id.unit11);
		unit12 = (TextView) view.findViewById(R.id.unit12);
		unit21 = (TextView) view.findViewById(R.id.unit21);
		unit22 = (TextView) view.findViewById(R.id.unit22);
		unit31 = (TextView) view.findViewById(R.id.unit31);
		unit32 = (TextView) view.findViewById(R.id.unit32);
		unit41 = (TextView) view.findViewById(R.id.unit41);
		unit42 = (TextView) view.findViewById(R.id.unit42);
		unit51 = (TextView) view.findViewById(R.id.unit51);
		unit52 = (TextView) view.findViewById(R.id.unit52);
		unitExtra = (TextView) view.findViewById(R.id.unit_extra);
		unitText = (TextView) view.findViewById(R.id.unit_total);
		switch (unit) {
		case 0:
			unit01.setText("t");
			unit02.setText("t");
			unit11.setText("t");
			unit12.setText("t");
			unit21.setText("t");
			unit22.setText("t");
			unit31.setText("t");
			unit32.setText("t");
			unit41.setText("t");
			unit42.setText("t");
			unit51.setText("t");
			unit52.setText("t");
			unitExtra.setText("t");
			unitText.setText("t");
			break;
		case 1:
			unit01.setText("kg");
			unit02.setText("kg");
			unit11.setText("kg");
			unit12.setText("kg");
			unit21.setText("kg");
			unit22.setText("kg");
			unit31.setText("kg");
			unit32.setText("kg");
			unit41.setText("kg");
			unit42.setText("kg");
			unit51.setText("kg");
			unit52.setText("kg");
			unitExtra.setText("kg");
			unitText.setText("kg");
			break;
		default:
			break;
		}
		
		let01 = (EditText) view.findViewById(R.id.let01);
		let01.addTextChangedListener(new CalcWatcher(0));
		let02 = (EditText) view.findViewById(R.id.let02);
		let02.addTextChangedListener(new CalcWatcher(0));
		
		let11 = (EditText) view.findViewById(R.id.let11);
		let11.addTextChangedListener(new CalcWatcher(1));
		let12 = (EditText) view.findViewById(R.id.let12);
		let12.addTextChangedListener(new CalcWatcher(1));
		
		let21 = (EditText) view.findViewById(R.id.let21);
		let21.addTextChangedListener(new CalcWatcher(2));
		let22 = (EditText) view.findViewById(R.id.let22);
		let22.addTextChangedListener(new CalcWatcher(2));
		
		let31 = (EditText) view.findViewById(R.id.let31);
		let31.addTextChangedListener(new CalcWatcher(3));
		let32 = (EditText) view.findViewById(R.id.let32);
		let32.addTextChangedListener(new CalcWatcher(3));
		
		let41 = (EditText) view.findViewById(R.id.let41);
		let41.addTextChangedListener(new CalcWatcher(4));
		let42 = (EditText) view.findViewById(R.id.let42);
		let42.addTextChangedListener(new CalcWatcher(4));
		
		let51 = (EditText) view.findViewById(R.id.let51);
		let51.addTextChangedListener(new CalcWatcher(5));
		let52 = (EditText) view.findViewById(R.id.let52);
		let52.addTextChangedListener(new CalcWatcher(5));
		
		extra = (EditText) view.findViewById(R.id.extra);
		extra.addTextChangedListener(new CalcWatcher(6));
		
		line1 = view.findViewById(R.id.line1);
		line2 = view.findViewById(R.id.line2);
		line3 = view.findViewById(R.id.line3);
		line4 = view.findViewById(R.id.line4);
		line5 = view.findViewById(R.id.line5);
		
		line_result = (TextView) view.findViewById(R.id.line_result);
		line1_result = (TextView) view.findViewById(R.id.line1_result);
		line2_result = (TextView) view.findViewById(R.id.line2_result);
		line3_result = (TextView) view.findViewById(R.id.line3_result);
		line4_result = (TextView) view.findViewById(R.id.line4_result);
		line5_result = (TextView) view.findViewById(R.id.line5_result);
		extra_result = (TextView) view.findViewById(R.id.extra_result);
		total = (TextView) view.findViewById(R.id.total);
		
		confirm = (Button) view.findViewById(R.id.confirm);
		confirm.setOnClickListener(this);
		cancel = (Button) view.findViewById(R.id.cancel);
		cancel.setOnClickListener(this);
		super.onViewCreated(view, savedInstanceState);
	}
	
	class CalcWatcher implements TextWatcher{

		private int FRAG;
		public CalcWatcher(int FRAG) {
			super();
			this.FRAG = FRAG;
		}

		@Override
		public void afterTextChanged(Editable s) {
			getResult(FRAG);
			
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			
			switch (FRAG) {
			case 0:
				line1.setVisibility(View.VISIBLE);
				break;
			case 1:
				line2.setVisibility(View.VISIBLE);
				break;
			case 2:
				line3.setVisibility(View.VISIBLE);
				break;
			case 3:
				line4.setVisibility(View.VISIBLE);
				break;
			case 4:
				line5.setVisibility(View.VISIBLE);
				break;
			}
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			
		}
		
		public void getResult(int frag){
			int i01 = 0;
			int i02 = 0;
			int i11 = 0;
			int i12 = 0;
			int i21 = 0;
			int i22 = 0;
			int i31 = 0;
			int i32 = 0;
			int i41 = 0;
			int i42 = 0;
			int i51 = 0;
			int i52 = 0;
			int iextra = 0;
			String s01 = let01.getText().toString().trim();
			String s02 = let02.getText().toString().trim();
			String s11 = let11.getText().toString().trim();
			String s12 = let12.getText().toString().trim();
			String s21 = let21.getText().toString().trim();
			String s22 = let22.getText().toString().trim();
			String s31 = let31.getText().toString().trim();
			String s32 = let32.getText().toString().trim();
			String s41 = let41.getText().toString().trim();
			String s42 = let42.getText().toString().trim();
			String s51 = let51.getText().toString().trim();
			String s52 = let52.getText().toString().trim();
			String ext = extra.getText().toString().trim();
			int r1 = Integer.parseInt(line_result.getText().toString().trim());
			int r2 = Integer.parseInt(line1_result.getText().toString().trim());
			int r3 = Integer.parseInt(line2_result.getText().toString().trim());
			int r4 = Integer.parseInt(line3_result.getText().toString().trim());
			int r5 = Integer.parseInt(line4_result.getText().toString().trim());
			int r6 = Integer.parseInt(line5_result.getText().toString().trim());
			int r7 = Integer.parseInt(extra_result.getText().toString().trim());
			
			switch (frag) {
			case 0:
				if (Constant.isNum(s01) && Constant.isNum(s02)) {
					i01 = Integer.parseInt(s01);
					i02 = Integer.parseInt(s02);
					int pro = i01*i02;
					line_result.setText(""+pro);
					total.setText(""+(pro + r2 + r3 + r4 + r5 + r6 + r7));
				} else {
					line_result.setText(""+0);
					total.setText(""+(0 + r2 + r3 + r4 + r5 + r6 + r7));
				}
				break;

			case 1:
				if (Constant.isNum(s11) && Constant.isNum(s12)) {
					i11 = Integer.parseInt(s11);
					i12 = Integer.parseInt(s12);
					int pro = i11*i12;
					line1_result.setText(""+pro);
					total.setText(""+(pro + r1 + r3 + r4 + r5 + r6 + r7));
				} else {
					line1_result.setText(""+0);
					total.setText(""+(0 + r1 + r3 + r4 + r5 + r6 + r7));
				}
				break;
			case 2:
				if (Constant.isNum(s21) && Constant.isNum(s22)) {
					i21 = Integer.parseInt(s21);
					i22 = Integer.parseInt(s22);
					int pro = i21*i22;
					line2_result.setText(""+pro);
					total.setText(""+(pro + r2 + r1 + r4 + r5 + r6 + r7));
				} else {
					line2_result.setText(""+0);
					total.setText(""+(0 + r2 + r1 + r4 + r5 + r6 + r7));
				}
				break;
			case 3:
				if (Constant.isNum(s31) && Constant.isNum(s32)) {
					i31 = Integer.parseInt(s31);
					i32 = Integer.parseInt(s32);
					int pro = i31*i32;
					line3_result.setText(""+pro);
					total.setText(""+(pro + r2 + r3 + r1 + r5 + r6 + r7));
				} else {
					line3_result.setText(""+0);
					total.setText(""+(0 + r2 + r3 + r1 + r5 + r6 + r7));
				}
				break;
			case 4:
				if (Constant.isNum(s41) && Constant.isNum(s42)) {
					i41 = Integer.parseInt(s41);
					i42 = Integer.parseInt(s42);
					int pro = i41*i42;
					line4_result.setText(""+pro);
					total.setText(""+(pro + r2 + r3 + r4 + r1 + r6 + r7));
				} else {
					line4_result.setText(""+0);
					total.setText(""+(0 + r2 + r3 + r4 + r1 + r6 + r7));
				}
				break;
			case 5:
				if (Constant.isNum(s51) && Constant.isNum(s52)) {
					i51 = Integer.parseInt(s51);
					i52 = Integer.parseInt(s52);
					int pro = i51*i52;
					line5_result.setText(""+pro);
					total.setText(""+(pro + r2 + r3 + r4 + r5 + r1 + r7));
				} else {
					line5_result.setText(""+0);
					total.setText(""+(0 + r2 + r3 + r4 + r5 + r1 + r7));
				}
				break;
			case 6:
				if (Constant.isNum(ext)) {
					iextra = Integer.parseInt(ext);
					extra_result.setText(""+iextra);
					total.setText(""+(iextra + r2 + r3 + r4 + r5 + r6 + r1));
				}else{
					extra_result.setText(""+0);
					total.setText(""+(0 + r2 + r3 + r4 + r5 + r6 + r1));
				}
				break;
			}
		}
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.confirm:
			if (getArguments().getInt("from") == 0) {
				((CountActivity)getActivity()).weight.setText(total.getText());
			}
			if (getArguments().getInt("from") == 1) {
				((StuffActivity)getActivity()).weight.setText(total.getText());
			}
			dismiss();
			break;
		case R.id.cancel:
			dismiss();
			break;
		}
		
	}

}
