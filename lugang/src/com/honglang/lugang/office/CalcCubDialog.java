package com.honglang.lugang.office;

import com.honglang.lugang.Constant;
import com.honglang.lugang.R;
import com.honglang.lugang.office.CalcWeightDialog.CalcWatcher;
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

public class CalcCubDialog extends DialogFragment implements OnClickListener {

	private EditText length;
	private EditText width;
	private EditText height;
	private EditText length1;
	private EditText width1;
	private EditText height1;
	private EditText length2;
	private EditText width2;
	private EditText height2;
	private EditText length3;
	private EditText width3;
	private EditText height3;
	private EditText length4;
	private EditText width4;
	private EditText height4;
	private EditText length5;
	private EditText width5;
	private EditText height5;
	
	private EditText count;
	private EditText count1;
	private EditText count2;
	private EditText count3;
	private EditText count4;
	private EditText count5;
	
	private EditText extra;
	
	private TextView result;
	private TextView result1;
	private TextView result2;
	private TextView result3;
	private TextView result4;
	private TextView result5;
	private TextView resultExtra;
	private TextView total;
	
	private View line1;
	private View line2;
	private View line3;
	private View line4;
	private View line5;
	
	private Button confirm;
	private Button cancel;
	
	public static CalcCubDialog newInstance(int from){
		CalcCubDialog frag =  new CalcCubDialog();
		Bundle args = new Bundle();
        args.putInt("from", from);
        frag.setArguments(args);
		return frag;
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		Dialog dialog = super.onCreateDialog(savedInstanceState);
		dialog.setTitle("总体积计算(最多6项)");
		dialog.setCanceledOnTouchOutside(false);
		return dialog;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		return inflater.inflate(R.layout.calc_cubage, null);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		confirm = (Button) view.findViewById(R.id.confirm);
		cancel = (Button) view.findViewById(R.id.cancel);
		confirm.setOnClickListener(this);
		cancel.setOnClickListener(this);
		
		length = (EditText) view.findViewById(R.id.length);
		width = (EditText) view.findViewById(R.id.width);
		height = (EditText) view.findViewById(R.id.height);
		length.addTextChangedListener(new CalcWatcher(0));
		width.addTextChangedListener(new CalcWatcher(0));
		height.addTextChangedListener(new CalcWatcher(0));
		
		length1 = (EditText) view.findViewById(R.id.length1);
		width1 = (EditText) view.findViewById(R.id.width1);
		height1 = (EditText) view.findViewById(R.id.height1);
		length1.addTextChangedListener(new CalcWatcher(1));
		width1.addTextChangedListener(new CalcWatcher(1));
		height1.addTextChangedListener(new CalcWatcher(1));
		
		length2 = (EditText) view.findViewById(R.id.length2);
		width2 = (EditText) view.findViewById(R.id.width2);
		height2 = (EditText) view.findViewById(R.id.height2);
		length2.addTextChangedListener(new CalcWatcher(2));
		width2.addTextChangedListener(new CalcWatcher(2));
		height2.addTextChangedListener(new CalcWatcher(2));
		
		length3 = (EditText) view.findViewById(R.id.length3);
		width3 = (EditText) view.findViewById(R.id.width3);
		height3 = (EditText) view.findViewById(R.id.height3);
		length3.addTextChangedListener(new CalcWatcher(3));
		width3.addTextChangedListener(new CalcWatcher(3));
		height3.addTextChangedListener(new CalcWatcher(3));
		
		length4 = (EditText) view.findViewById(R.id.length4);
		width4 = (EditText) view.findViewById(R.id.width4);
		height4 = (EditText) view.findViewById(R.id.height4);
		length4.addTextChangedListener(new CalcWatcher(4));
		width4.addTextChangedListener(new CalcWatcher(4));
		height4.addTextChangedListener(new CalcWatcher(4));
		
		length5 = (EditText) view.findViewById(R.id.length5);
		width5 = (EditText) view.findViewById(R.id.width5);
		height5 = (EditText) view.findViewById(R.id.height5);
		length5.addTextChangedListener(new CalcWatcher(5));
		width5.addTextChangedListener(new CalcWatcher(5));
		height5.addTextChangedListener(new CalcWatcher(5));
		
		count = (EditText) view.findViewById(R.id.count);
		count1 = (EditText) view.findViewById(R.id.count1);
		count2 = (EditText) view.findViewById(R.id.count2);
		count3 = (EditText) view.findViewById(R.id.count3);
		count4 = (EditText) view.findViewById(R.id.count4);
		count5 = (EditText) view.findViewById(R.id.count5);
		count.addTextChangedListener(new CalcWatcher(0));
		count1.addTextChangedListener(new CalcWatcher(1));
		count2.addTextChangedListener(new CalcWatcher(2));
		count3.addTextChangedListener(new CalcWatcher(3));
		count4.addTextChangedListener(new CalcWatcher(4));
		count5.addTextChangedListener(new CalcWatcher(5));
		
		extra = (EditText) view.findViewById(R.id.extra);
		extra.addTextChangedListener(new CalcWatcher(6));
		
		result = (TextView) view.findViewById(R.id.result);
		result1 = (TextView) view.findViewById(R.id.result1);
		result2 = (TextView) view.findViewById(R.id.result2);
		result3 = (TextView) view.findViewById(R.id.result3);
		result4 = (TextView) view.findViewById(R.id.result4);
		result5 = (TextView) view.findViewById(R.id.result5);
		resultExtra = (TextView) view.findViewById(R.id.extra_result);
		total = (TextView) view.findViewById(R.id.total);
		
		line1 = view.findViewById(R.id.line1);
		line2 = view.findViewById(R.id.line2);
		line3 = view.findViewById(R.id.line3);
		line4 = view.findViewById(R.id.line4);
		line5 = view.findViewById(R.id.line5);
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
			Double iLength = 0.00;
			Double iWidth = 0.00;
			Double iHeight = 0.00;
			Double iCount = 0.00;
			Double iLength1 = 0.00;
			Double iWidth1 = 0.00;
			Double iHeight1 = 0.00;
			Double iCount1 = 0.00;
			Double iLength2 = 0.00;
			Double iWidth2 = 0.00;
			Double iHeight2 = 0.00;
			Double iCount2 = 0.00;
			Double iLength3 = 0.00;
			Double iWidth3 = 0.00;
			Double iHeight3 = 0.00;
			Double iCount3 = 0.00;
			Double iLength4 = 0.00;
			Double iWidth4 = 0.00;
			Double iHeight4 = 0.00;
			Double iCount4 = 0.00;
			Double iLength5 = 0.00;
			Double iWidth5 = 0.00;
			Double iHeight5 = 0.00;
			Double iCount5 = 0.00;
			Double iExtra = 0.00;
			
			String sLength = length.getText().toString().trim();
			String sWidth = width.getText().toString().trim();
			String sHeight = height.getText().toString().trim();
			String sCount = count.getText().toString().trim();
			String sLength1 = length1.getText().toString().trim();
			String sWidth1 = width1.getText().toString().trim();
			String sHeight1 = height1.getText().toString().trim();
			String sCount1 = count1.getText().toString().trim();
			String sLength2 = length2.getText().toString().trim();
			String sWidth2 = width2.getText().toString().trim();
			String sHeight2 = height2.getText().toString().trim();
			String sCount2 = count2.getText().toString().trim();
			String sLength3 = length3.getText().toString().trim();
			String sWidth3 = width3.getText().toString().trim();
			String sHeight3 = height3.getText().toString().trim();
			String sCount3 = count3.getText().toString().trim();
			String sLength4 = length4.getText().toString().trim();
			String sWidth4 = width4.getText().toString().trim();
			String sHeight4 = height4.getText().toString().trim();
			String sCount4 = count4.getText().toString().trim();
			String sLength5 = length5.getText().toString().trim();
			String sWidth5 = width5.getText().toString().trim();
			String sHeight5 = height5.getText().toString().trim();
			String sCount5 = count5.getText().toString().trim();
			String sExtra = extra.getText().toString().trim();
			
			Double r0 = Double.parseDouble(result.getText().toString().trim());
			Double r1 = Double.parseDouble(result1.getText().toString().trim());
			Double r2 = Double.parseDouble(result2.getText().toString().trim());
			Double r3 = Double.parseDouble(result3.getText().toString().trim());
			Double r4 = Double.parseDouble(result4.getText().toString().trim());
			Double r5 = Double.parseDouble(result5.getText().toString().trim());
			Double r6 = Double.parseDouble(resultExtra.getText().toString().trim());
			switch (frag) {
			case 0:
				if (Constant.isNum(sLength) && Constant.isNum(sWidth) && Constant.isNum(sHeight) && Constant.isNum(sCount)) {
					iLength = Double.parseDouble(sLength);
					iWidth = Double.parseDouble(sWidth);
					iHeight = Double.parseDouble(sHeight);
					iCount = Double.parseDouble(sCount);
					Double sum = iLength*iWidth*iHeight*iCount;
					result.setText(sum+"");
					total.setText(""+(sum + r1 + r2 + r3 + r4 + r5 + r6));
				}else{
					result.setText(0+"");
					total.setText(""+(0 + r1 + r2 + r3 + r4 + r5 + r6));
				}
				break;
			case 1:
				if (Constant.isNum(sLength1) && Constant.isNum(sWidth1) && Constant.isNum(sHeight1) && Constant.isNum(sCount1)) {
					iLength1 = Double.parseDouble(sLength1);
					iWidth1 = Double.parseDouble(sWidth1);
					iHeight1 = Double.parseDouble(sHeight1);
					iCount1 = Double.parseDouble(sCount1);
					Double sum = iLength1*iWidth1*iHeight1*iCount1;
					result1.setText(sum+"");
					total.setText(""+(sum + r0 + r2 + r3 + r4 + r5 + r6));
				}else{
					result1.setText(0+"");
					total.setText(""+(0 + r0 + r2 + r3 + r4 + r5 + r6));
				}
				break;
			case 2:
				if (Constant.isNum(sLength2) && Constant.isNum(sWidth2) && Constant.isNum(sHeight2) && Constant.isNum(sCount2)) {
					iLength2 = Double.parseDouble(sLength2);
					iWidth2 = Double.parseDouble(sWidth2);
					iHeight2 = Double.parseDouble(sHeight2);
					iCount2 = Double.parseDouble(sCount2);
					Double sum = iLength2*iWidth2*iHeight2*iCount2;
					result2.setText(sum+"");
					total.setText(""+(sum + r1 + r0 + r3 + r4 + r5 + r6));
				}else{
					result2.setText(0+"");
					total.setText(""+(0 + r1 + r0 + r3 + r4 + r5 + r6));
				}
				break;
			case 3:
				if (Constant.isNum(sLength3) && Constant.isNum(sWidth3) && Constant.isNum(sHeight3) && Constant.isNum(sCount3)) {
					iLength3 = Double.parseDouble(sLength3);
					iWidth3 = Double.parseDouble(sWidth3);
					iHeight3 = Double.parseDouble(sHeight3);
					iCount3 = Double.parseDouble(sCount3);
					Double sum = iLength3*iWidth3*iHeight3*iCount3;
					result3.setText(sum+"");
					total.setText(""+(sum + r1 + r2 + r0 + r4 + r5 + r6));
				}else{
					result3.setText(0+"");
					total.setText(""+(0 + r1 + r2 + r0 + r4 + r5 + r6));
				}
				break;
			case 4:
				if (Constant.isNum(sLength4) && Constant.isNum(sWidth4) && Constant.isNum(sHeight4) && Constant.isNum(sCount4)) {
					iLength4 = Double.parseDouble(sLength4);
					iWidth4 = Double.parseDouble(sWidth4);
					iHeight4 = Double.parseDouble(sHeight4);
					iCount4 = Double.parseDouble(sCount4);
					Double sum = iLength4*iWidth4*iHeight4*iCount4;
					result4.setText(sum+"");
					total.setText(""+(sum + r1 + r2 + r3 + r0 + r5 + r6));
				}else{
					result4.setText(0+"");
					total.setText(""+(0 + r1 + r2 + r3 + r0 + r5 + r6));
				}
				break;
			case 5:
				if (Constant.isNum(sLength5) && Constant.isNum(sWidth5) && Constant.isNum(sHeight5) && Constant.isNum(sCount5)) {
					iLength5 = Double.parseDouble(sLength5);
					iWidth5 = Double.parseDouble(sWidth5);
					iHeight5 = Double.parseDouble(sHeight5);
					iCount5 = Double.parseDouble(sCount5);
					Double sum = iLength5*iWidth5*iHeight5*iCount5;
					result5.setText(sum+"");
					total.setText(""+(sum + r1 + r2 + r3 + r4 + r0 + r6));
				}else{
					result5.setText(0+"");
					total.setText(""+(0 + r1 + r2 + r3 + r4 + r0 + r6));
				}
				break;
			case 6:
				if (Constant.isNum(sExtra)) {
					iExtra = Double.parseDouble(sExtra);
					resultExtra.setText("" + iExtra);
					total.setText(""+(iExtra + r1 + r2 + r3 + r4 + r5 + r0));
				} else {
					resultExtra.setText("" + 0);
					total.setText(""+(0 + r1 + r2 + r3 + r4 + r5 + r0));
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
				((CountActivity)getActivity()).cubage.setText(total.getText());
			}
			if (getArguments().getInt("from") == 1) {
				((StuffActivity)getActivity()).cubage.setText(total.getText());
			}
			dismiss();
			break;

		case R.id.cancel:
			dismiss();
			break;
		}
		
	}

}
