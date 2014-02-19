package com.honglang.lugang;

import java.text.DecimalFormat;

import com.honglang.lugang.office.CalcCubDialog;
import com.honglang.lugang.office.CalcMoneyDialog;
import com.honglang.lugang.office.CalcWeightDialog;
import com.honglang.lugang.office.Order;

import android.os.Bundle;
import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class CountActivity extends Activity implements OnClickListener {

	private Order data;
	private TextView title;
	private Button back;
	private Button confirm;
	private Switch isDsf;
	private TextView type;
	private TextView name;
	public TextView allUnit;
	private TextView totalUnit;
	private EditText total;
//	private EditText weight;
	public Button weight;
	public Button cubage;
	public Button yf;
	public EditText bzf;
	public EditText thf;
	public EditText shf;
	public EditText bxf;
	public EditText zyf;
	private EditText tbjz;
	private EditText dk;
	
	public Spinner weightUnit;
	private static final String[] UNIT = new String[]{"t","kg"};
	private ArrayAdapter<String> adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_count);
		data = (Order) this.getIntent().getExtras().getSerializable("order");
		init();
		
//		if (savedInstanceState == null) {
//			// First-time init; create fragment to embed in activity.
//			FragmentTransaction ft = getFragmentManager().beginTransaction();
//            DialogFragment newFragment = CalcWeightDialog.newInstance();
//            ft.add(R.id.embedded, newFragment).commit();
//		}
	}
	
	private void init(){
		title = (TextView) this.findViewById(R.id.title);
		title.setText("费用计算");
		back = (Button) this.findViewById(R.id.back);
		back.setOnClickListener(this);
		confirm = (Button) this.findViewById(R.id.confirm);
		confirm.setText("确 认");
		confirm.setVisibility(View.VISIBLE);
		
		type = (TextView) this.findViewById(R.id.type);
		name = (TextView) this.findViewById(R.id.name);
		totalUnit = (TextView) this.findViewById(R.id.totalUnit);
		total = (EditText) this.findViewById(R.id.total);
		weight = (Button) this.findViewById(R.id.weight);
		cubage = (Button) this.findViewById(R.id.cubage);
		yf = (Button) this.findViewById(R.id.yf);
		allUnit = (TextView) this.findViewById(R.id.allUnit);
		
		bzf = (EditText) this.findViewById(R.id.bzf);
		thf = (EditText) this.findViewById(R.id.thf);
		shf = (EditText) this.findViewById(R.id.shf);
		bxf = (EditText) this.findViewById(R.id.bxf);
		zyf = (EditText) this.findViewById(R.id.zyf);
		tbjz = (EditText) this.findViewById(R.id.tbjz);
		dk = (EditText) this.findViewById(R.id.dk);
		
		type.setText(data.getWplx());
		name.setText(data.getWpmc());
		total.setText(data.getSl());
		totalUnit.setText(data.getSl_danwei());
		weight.setText(data.getZongliang());
		weight.setInputType(InputType.TYPE_NULL);
//		weight.setOnTouchListener(new WeightTouchListener());
		weight.setOnClickListener(this);
		cubage.setText(data.getTiji());
		cubage.setOnClickListener(this);
		yf.setText(data.getYunfei());
		yf.setOnClickListener(this);
		bzf.setText(data.getBaozhuangfei());
		thf.setText(data.getTihuofei());
		shf.setText(data.getSonghuofei());
		bxf.setText(data.getBaof());
		zyf.setText(data.getZongyunfei());
		tbjz.setText(data.getTbjz());
		dk.setText(data.getHuok());
		allUnit.setText(data.getZl_danwei());
		
		bzf.addTextChangedListener(new CalcWatcher());
		shf.addTextChangedListener(new CalcWatcher());
		thf.addTextChangedListener(new CalcWatcher());
		bxf.addTextChangedListener(new CalcWatcher());
		
		isDsf = (Switch) this.findViewById(R.id.dshk);
		if (data.getIsdsf().trim().equals("是")) {
			isDsf.setChecked(true);
		} else {
			isDsf.setChecked(false);
		}
		
		weightUnit = (Spinner) this.findViewById(R.id.weightUnit);
//		weightUnit.setOnItemSelectedListener(new SpinnerSelectedListener());
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, UNIT);
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		weightUnit.setAdapter(adapter);
		if (data.getZl_danwei().equals("t")) {
			weightUnit.setSelection(0, true);
			weightUnit.setOnItemSelectedListener(new SpinnerSelectedListener());
		} else {
			weightUnit.setSelection(1, true);
			weightUnit.setOnItemSelectedListener(new SpinnerSelectedListener());
		}
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			this.finish();
			break;
		case R.id.weight:
			showDialog();
			break;
		case R.id.cubage:
			DialogFragment frag = CalcCubDialog.newInstance();
			frag.show(getFragmentManager(), "cubage");
		case R.id.yf:
			DialogFragment f = CalcMoneyDialog.newInstance();
			f.show(getFragmentManager(), "money");
		}
		
	}
	
	public void showDialog(){
		DialogFragment newFragment = CalcWeightDialog.newInstance(weightUnit.getSelectedItemPosition());
        newFragment.show(getFragmentManager(), "weight");
	}
	
	class SpinnerSelectedListener implements OnItemSelectedListener{

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			showDialog();
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			
		}
		
	}
	
	class CalcWatcher implements TextWatcher{

		@Override
		public void afterTextChanged(Editable s) {
			Double yun = Double.parseDouble(yf.getText().toString());
			String bz = bzf.getText().toString().trim();
			String th = thf.getText().toString().trim();
			String sh = shf.getText().toString().trim();
			String bx = bxf.getText().toString().trim();
			if (Constant.isNum(bz) && Constant.isNum(th) && Constant.isNum(sh) && Constant.isNum(bx)) {
				Double b = Double.parseDouble(bz);
				Double t = Double.parseDouble(th);
				Double ds = Double.parseDouble(sh);
				Double x = Double.parseDouble(bx);
				Double sum = yun + b + t + ds + x;
				DecimalFormat f = new DecimalFormat("0.00");
				zyf.setText(f.format(sum));
			}
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	/*class WeightTouchListener implements OnTouchListener{

		int touch_flag=0;
		@Override
		public boolean onTouch(View v, MotionEvent event) {
            weight.setInputType(0);
            Toast.makeText(CountActivity.this, "touch_flag="+touch_flag, Toast.LENGTH_SHORT).show();
            touch_flag++;
            if (touch_flag == 2) {
            	DialogFragment newFragment = CalcWeightDialog.newInstance();
                newFragment.show(getFragmentManager(), "calc");
                touch_flag=1;
                return true;
			}
            Toast.makeText(CountActivity.this, "flag="+touch_flag, Toast.LENGTH_SHORT).show();
            return false;
		}
		
	}*/

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.count, menu);
//		return true;
//	}

}
