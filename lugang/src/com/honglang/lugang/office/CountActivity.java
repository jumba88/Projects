package com.honglang.lugang.office;

import java.text.DecimalFormat;

import com.honglang.lugang.Constant;
import com.honglang.lugang.R;
import com.honglang.lugang.SessionManager;
import com.honglang.lugang.R.id;
import com.honglang.lugang.R.layout;

import android.os.Bundle;
import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.content.Intent;
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
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class CountActivity extends Activity implements OnClickListener {

	private Order data;
	private TextView title;
	private Button back;
	private Button confirm;
//	private Switch isDsf;
	private RadioButton yes;
	private RadioButton no;
	private TextView type;
	private TextView name;
//	public TextView allUnit;
	public String allUnit;
	private TextView totalUnit;
	private TextView hint;
	private EditText total;
//	private EditText weight;
	public EditText weight;
	public EditText cubage;
	public Button calc;
	public Button calcWeight;
	public Button calcCubage;
	public EditText yf;
	public EditText bzf;
	public EditText thf;
	public EditText shf;
	public EditText bxf;
	public EditText zyf;
	private EditText tbjz;
	private EditText dk;
	
	public Spinner pack;
	public Spinner weightUnit;
	private static final String[] UNIT = new String[]{"t","kg"};
	private static String[] PACK_TYPE;
	private ArrayAdapter<String> adapter;
	private ArrayAdapter<String> pAdapter;
	
	private static boolean EDITABLE = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_count);
		
		init();
		
		if (SessionManager.getInstance().getUsertype().equals("物流企业")) {
			confirm.setVisibility(View.VISIBLE);
		}
		if (SessionManager.getInstance().getUsertype().equals("VIP会员")) {
			total.setFocusable(false);
			weight.setEnabled(false);
			cubage.setEnabled(false);
			calcWeight.setEnabled(false);
			calcCubage.setEnabled(false);
			calc.setEnabled(false);
			weightUnit.setEnabled(false);
			yf.setEnabled(false);
			bzf.setFocusable(false);
			thf.setFocusable(false);
			shf.setFocusable(false);
			tbjz.setEnabled(true);
			hint.setVisibility(View.VISIBLE);
			EDITABLE = true;
		}
	}
	
	private void init(){
		title = (TextView) this.findViewById(R.id.title);
		title.setText("费用计算");
		back = (Button) this.findViewById(R.id.back);
		back.setOnClickListener(this);
		confirm = (Button) this.findViewById(R.id.ok);
		confirm.setText("确 认");
		confirm.setOnClickListener(this);
		data = (Order) this.getIntent().getExtras().getSerializable("order");
		
		hint = (TextView) this.findViewById(R.id.hint);
		type = (TextView) this.findViewById(R.id.type);
		name = (TextView) this.findViewById(R.id.name);
		totalUnit = (TextView) this.findViewById(R.id.totalUnit);
		total = (EditText) this.findViewById(R.id.total);
		weight = (EditText) this.findViewById(R.id.weight);
		cubage = (EditText) this.findViewById(R.id.cubage);
		cubage.setOnClickListener(this);
		calc = (Button) this.findViewById(R.id.calc);
		calc.setOnClickListener(this);
		calcWeight = (Button) this.findViewById(R.id.calcWeight);
		calcWeight.setOnClickListener(this);
		calcCubage = (Button) this.findViewById(R.id.calcCubage);
		calcCubage.setOnClickListener(this);
		
		yf = (EditText) this.findViewById(R.id.yf);
//		allUnit = (TextView) this.findViewById(R.id.allUnit);
		
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
		weight.setText(data.getZl());
		weight.setInputType(InputType.TYPE_NULL);
//		weight.setOnTouchListener(new WeightTouchListener());
		weight.setOnClickListener(this);
		cubage.setText(data.getTiji());
		yf.setText(data.getYunfei());
		bzf.setText(data.getBaozhuangfei());
		thf.setText(data.getTihuofei());
		shf.setText(data.getSonghuofei());
		bxf.setText(data.getBaofei());
		zyf.setText(data.getZongyunfei());
		tbjz.setText(data.getTbjz());
		dk.setText(data.getHuok());
		allUnit = data.getZl_danwei();
		
		yf.addTextChangedListener(new CalcWatcher());
		bzf.addTextChangedListener(new CalcWatcher());
		shf.addTextChangedListener(new CalcWatcher());
		thf.addTextChangedListener(new CalcWatcher());
		bxf.addTextChangedListener(new CalcWatcher());
		tbjz.addTextChangedListener(new TbWatcher());
		
//		isDsf = (Switch) this.findViewById(R.id.dshk);
		yes = (RadioButton) this.findViewById(R.id.dshk_yes);
		no = (RadioButton) this.findViewById(R.id.dshk_no);
		if (data.getIsdsf().trim().equals("是")) {
			yes.setChecked(true);
		} else {
			no.setChecked(true);
		}
		
		pack = (Spinner) this.findViewById(R.id.pack);
		PACK_TYPE = this.getResources().getStringArray(R.array.pack_type);
		pAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, PACK_TYPE);
		pAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		pack.setAdapter(pAdapter);
		if (data.getBaozhuang().equals(PACK_TYPE[0])) {
			pack.setSelection(0, true);
		}
		if (data.getBaozhuang().equals(PACK_TYPE[1])) {
			pack.setSelection(1, true);
		}
		if (data.getBaozhuang().equals(PACK_TYPE[2])) {
			pack.setSelection(2, true);
		}
		if (data.getBaozhuang().equals(PACK_TYPE[3])) {
			pack.setSelection(3, true);
		}
		if (data.getBaozhuang().equals(PACK_TYPE[4])) {
			pack.setSelection(4, true);
		}
		if (data.getBaozhuang().equals(PACK_TYPE[5])) {
			pack.setSelection(5, true);
		}
		if (data.getBaozhuang().equals(PACK_TYPE[6])) {
			pack.setSelection(6, true);
		}
		
		weightUnit = (Spinner) this.findViewById(R.id.weightUnit);
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, UNIT);
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		weightUnit.setAdapter(adapter);
		if (data.getZl_danwei().equals("t")) {
			weightUnit.setSelection(0, true);
		} else {
			weightUnit.setSelection(1, true);
		}
		weightUnit.setOnItemSelectedListener(new SpinnerSelectedListener());
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			this.finish();
			break;
		case R.id.ok:
			ok();
			break;
		case R.id.calcWeight:
			showDialog();
			break;
		case R.id.calcCubage:
			DialogFragment frag = CalcCubDialog.newInstance(0);
			frag.show(getFragmentManager(), "cubage");
			break;
		case R.id.calc:
			DialogFragment f = CalcMoneyDialog.newInstance(0);
			f.show(getFragmentManager(), "money");
			break;
		}
		
	}
	//get the final result
	public void ok(){
		if (!Constant.isInt(total.getText().toString())) {
			Toast.makeText(this, "请输入货物数量!", Toast.LENGTH_LONG).show();
			return;
		}
		if (!Constant.isNumber(yf.getText().toString())) {
			Toast.makeText(CountActivity.this, "运费不能为空,至少为0或大于0", Toast.LENGTH_LONG).show();
			return;
		}
		if (!Constant.isNumber(bzf.getText().toString())) {
			Toast.makeText(CountActivity.this, "包装费不能为空,至少为0或大于0", Toast.LENGTH_LONG).show();
			return;
		}
		if (!Constant.isNumber(thf.getText().toString())) {
			Toast.makeText(CountActivity.this, "提货费不能为空,至少为0或大于0", Toast.LENGTH_LONG).show();
			return;
		}
		if (!Constant.isNumber(shf.getText().toString())) {
			Toast.makeText(CountActivity.this, "送货费不能为空,至少为0或大于0", Toast.LENGTH_LONG).show();
			return;
		}
		data.setSl(total.getText().toString());
		data.setZl(weight.getText().toString());
		data.setZl_danwei(UNIT[weightUnit.getSelectedItemPosition()]);
		data.setTiji(cubage.getText().toString());
		data.setYunfei(yf.getText().toString());
		data.setBaozhuangfei(bzf.getText().toString());
		data.setTihuofei(thf.getText().toString());
		data.setSonghuofei(shf.getText().toString());
		data.setBaofei(bxf.getText().toString());
		data.setZongyunfei(zyf.getText().toString());
		String str = tbjz.getText().toString().trim();
		if (EDITABLE) {
			if (!Constant.isNumber(str)) {
				Toast.makeText(this, "请输入投保价值!", Toast.LENGTH_LONG).show();
				return;
			}
			if (Double.parseDouble(str) < 2000) {
				Toast.makeText(this, "投保价值最低2000元!", Toast.LENGTH_LONG).show();
				return;
			}
		}
		data.setTbjz(str);
		
		data.setJl_danwei(allUnit);
		data.setBaozhuang(PACK_TYPE[pack.getSelectedItemPosition()]);
		Intent i = new Intent();
		i.putExtra("item", data);
		setResult(RESULT_OK, i);
		this.finish();
	}
	
	
	public void showDialog(){
		DialogFragment newFragment = CalcWeightDialog.newInstance(weightUnit.getSelectedItemPosition(),0);
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
			String yun = yf.getText().toString().trim();
			String bz = bzf.getText().toString().trim();
			String th = thf.getText().toString().trim();
			String sh = shf.getText().toString().trim();
			String bx = bxf.getText().toString().trim();
			
//			String tb = tbjz.getText().toString().trim();
			if (Constant.isNum(yun) && Constant.isNum(bz) && Constant.isNum(th) && Constant.isNum(sh) && Constant.isNum(bx)) {
				Double y = Double.parseDouble(yun);
				Double b = Double.parseDouble(bz);
				Double t = Double.parseDouble(th);
				Double ds = Double.parseDouble(sh);
				Double x = Double.parseDouble(bx);
				Double sum = y + b + t + ds + x;
				DecimalFormat f = new DecimalFormat("0.00");
//				bxf.setText(f.format(x));
				zyf.setText(f.format(sum));
			}
//			if (EDITABLE) {
//				if (Constant.isNum(tb)) {
//					if (Double.parseDouble(tb) != Double.parseDouble(data.getTbjz())) {
//						confirm.setVisibility(View.VISIBLE);
//					}else{
//						confirm.setVisibility(View.GONE);
//					}
//				}
//			}
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
	
	class TbWatcher implements TextWatcher{

		@Override
		public void afterTextChanged(Editable s) {
			if (EDITABLE) {
				if (Constant.isNum(s.toString())) {
					if (Double.parseDouble(s.toString()) != Double.parseDouble(data.getTbjz())) {
						confirm.setVisibility(View.VISIBLE);
						bxf.setText(Double.parseDouble(s.toString())*5/10000+"");
					}else{
						confirm.setVisibility(View.GONE);
					}
				}
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
