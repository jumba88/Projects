package com.honglang.lugang.qrcode;

import java.text.DecimalFormat;

import com.honglang.lugang.Constant;
import com.honglang.lugang.R;
import com.honglang.lugang.R.array;
import com.honglang.lugang.R.id;
import com.honglang.lugang.R.layout;
import com.honglang.lugang.office.CalcCubDialog;
import com.honglang.lugang.office.CalcMoneyDialog;
import com.honglang.lugang.office.CalcWeightDialog;
import com.honglang.lugang.office.Order;

import android.os.Bundle;
import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class StuffActivity extends Activity implements OnClickListener {

	private TextView title;
	private Button back;
	private Button confirm;

	public Button weight;
	public Button cubage;
	public Button calc;
	public EditText yf;
	public EditText name;
	public EditText total;
	public EditText bzf;
	public EditText thf;
	public EditText shf;
	public EditText bxf;
	public EditText zyf;
	public EditText tbjz;
	public EditText dk;

	private RadioButton yes;
	private RadioButton no;

	public Spinner type;
	public Spinner pack;
	public Spinner weightUnit;
	public Spinner totalUnit;
	private static final String[] UNIT = new String[] { "t", "kg" };
	private static String[] STUFF_TYPE;
	private static String[] PACK_TYPE;
	private static String[] COUNT_TYPE;
	private ArrayAdapter<String> tAdapter;
	private ArrayAdapter<String> pAdapter;
	private ArrayAdapter<String> uAdapter;
	private ArrayAdapter<String> cAdapter;
	
	public String allUnit;

	private Order stuff;
	private int action;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_stuff);

		init();
	}

	private void init() {
		title = (TextView) this.findViewById(R.id.title);
		title.setText("货物明细");
		back = (Button) this.findViewById(R.id.back);
		back.setOnClickListener(this);
		confirm = (Button) this.findViewById(R.id.ok);
		confirm.setText("确 认");
		confirm.setVisibility(View.VISIBLE);
		confirm.setOnClickListener(this);

		weight = (Button) this.findViewById(R.id.weight);
		weight.setOnClickListener(this);
		cubage = (Button) this.findViewById(R.id.cubage);
		cubage.setOnClickListener(this);
		calc = (Button) this.findViewById(R.id.calc);
		calc.setOnClickListener(this);
		yf = (EditText) this.findViewById(R.id.yf);
//		yf.setOnClickListener(this);

		name = (EditText) this.findViewById(R.id.name);
		total = (EditText) this.findViewById(R.id.total);
		bzf = (EditText) this.findViewById(R.id.bzf);
		thf = (EditText) this.findViewById(R.id.thf);
		shf = (EditText) this.findViewById(R.id.shf);
		bxf = (EditText) this.findViewById(R.id.bxf);
		zyf = (EditText) this.findViewById(R.id.zyf);
		tbjz = (EditText) this.findViewById(R.id.tbjz);
		dk = (EditText) this.findViewById(R.id.dk);
		
		yf.addTextChangedListener(new CalcWatcher());
		bzf.addTextChangedListener(new CalcWatcher());
		shf.addTextChangedListener(new CalcWatcher());
		thf.addTextChangedListener(new CalcWatcher());
		tbjz.addTextChangedListener(new CalcWatcher());

		yes = (RadioButton) findViewById(R.id.dshk_yes);
		no = (RadioButton) findViewById(R.id.dshk_no);

		STUFF_TYPE = this.getResources().getStringArray(R.array.stuff_type);
		PACK_TYPE = this.getResources().getStringArray(R.array.pack_type);
		COUNT_TYPE = this.getResources().getStringArray(R.array.count_type);
		
		type = (Spinner) this.findViewById(R.id.type);
		pack = (Spinner) this.findViewById(R.id.pack);
		weightUnit = (Spinner) this.findViewById(R.id.weightUnit);
		totalUnit = (Spinner) this.findViewById(R.id.totalUnit);
		
		cAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, COUNT_TYPE);
		cAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		totalUnit.setAdapter(cAdapter);
		
		tAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, STUFF_TYPE);
		tAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		type.setAdapter(tAdapter);
		
		pAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, PACK_TYPE);
		pAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		pack.setAdapter(pAdapter);
		
		uAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, UNIT);
		uAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		weightUnit.setAdapter(uAdapter);
		weightUnit.setSelection(0, true);

		action = getIntent().getIntExtra("action", 0);
		if (action == 100) {
			stuff = new Order();
			allUnit = "t";
		} else {
			stuff = (Order) getIntent().getSerializableExtra("stuff");

			String typeStr = stuff.getWplx();
			if (typeStr.equals(STUFF_TYPE[0])) {
				type.setSelection(0);
			} else if (typeStr.equals(STUFF_TYPE[1])) {
				type.setSelection(1);
			} else if (typeStr.equals(STUFF_TYPE[2])) {
				type.setSelection(2);
			} else if (typeStr.equals(STUFF_TYPE[3])) {
				type.setSelection(3);
			} else if (typeStr.equals(STUFF_TYPE[4])) {
				type.setSelection(4);
			} else if (typeStr.equals(STUFF_TYPE[5])) {
				type.setSelection(5);
			} else if (typeStr.equals(STUFF_TYPE[6])) {
				type.setSelection(6);
			} else if (typeStr.equals(STUFF_TYPE[7])) {
				type.setSelection(7);
			} else if (typeStr.equals(STUFF_TYPE[8])) {
				type.setSelection(8);
			} else if (typeStr.equals(STUFF_TYPE[9])) {
				type.setSelection(9);
			} else if (typeStr.equals(STUFF_TYPE[10])) {
				type.setSelection(10);
			} else if (typeStr.equals(STUFF_TYPE[11])) {
				type.setSelection(11);
			} else if (typeStr.equals(STUFF_TYPE[12])) {
				type.setSelection(12);
			} else if (typeStr.equals(STUFF_TYPE[13])) {
				type.setSelection(13);
			} else if (typeStr.equals(STUFF_TYPE[14])) {
				type.setSelection(14);
			} else if (typeStr.equals(STUFF_TYPE[15])) {
				type.setSelection(15);
			} else if (typeStr.equals(STUFF_TYPE[16])) {
				type.setSelection(16);
			}
			
			String packStr = stuff.getBaozhuang();
			if (packStr.equals(PACK_TYPE[0])) {
				pack.setSelection(0);
			} else if (packStr.equals(PACK_TYPE[1])) {
				pack.setSelection(1);
			} else if (packStr.equals(PACK_TYPE[2])) {
				pack.setSelection(2);
			} else if (packStr.equals(PACK_TYPE[3])) {
				pack.setSelection(3);
			} else if (packStr.equals(PACK_TYPE[4])) {
				pack.setSelection(4);
			} else if (packStr.equals(PACK_TYPE[5])) {
				pack.setSelection(5);
			} else if (packStr.equals(PACK_TYPE[6])) {
				pack.setSelection(6);
			}
			
			String unitStr = stuff.getSl_danwei();
			if (unitStr.equals(COUNT_TYPE[0])) {
				totalUnit.setSelection(0);
			} else if (unitStr.equals(COUNT_TYPE[1])) {
				totalUnit.setSelection(1);
			} else if (unitStr.equals(COUNT_TYPE[2])) {
				totalUnit.setSelection(2);
			} else if (unitStr.equals(COUNT_TYPE[3])) {
				totalUnit.setSelection(3);
			}
			
			String zl = stuff.getZl_danwei();
			if (zl.equals(UNIT[0])) {
				weightUnit.setSelection(0);
			} else {
				weightUnit.setSelection(1);
			}
			
			if (stuff.getIsdsf().equals("是")) {
				yes.setChecked(true);
			} else {
				no.setChecked(true);
			}
			
			name.setText(stuff.getWpmc());
			total.setText(stuff.getSl());
			weight.setText(stuff.getZl());
			cubage.setText(stuff.getTiji());
			yf.setText(stuff.getYunfei());
			bzf.setText(stuff.getBaozhuangfei());
			thf.setText(stuff.getTihuofei());
			bxf.setText(stuff.getBaofei());
			zyf.setText(stuff.getZongyunfei());
			tbjz.setText(stuff.getTbjz());
			dk.setText(stuff.getHuok());
			
			allUnit = stuff.getJl_danwei();

		}
		weightUnit.setOnItemSelectedListener(new SpinnerSelectedListener());
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;
		case R.id.ok:
			confirm();
			break;
		case R.id.weight:
			showDialog();
			break;
		case R.id.cubage:
			DialogFragment frag = CalcCubDialog.newInstance(1);
			frag.show(getFragmentManager(), "cubage");
			break;
		case R.id.calc:
			DialogFragment f = CalcMoneyDialog.newInstance(1);
			f.show(getFragmentManager(), "money");
			break;
		}

	}

	private void confirm() {
		String stf = name.getText().toString().trim();
		String count = total.getText().toString().trim();
		String yfStr = yf.getText().toString().trim();
		String bzfStr = bzf.getText().toString().trim();
		String thfStr = thf.getText().toString().trim();
		String shfStr = shf.getText().toString().trim();
		String bxfStr = bxf.getText().toString().trim();
		String tbjzStr = tbjz.getText().toString().trim();
		String hkStr = dk.getText().toString().trim();

		if (stf == null || stf.equals("")) {
			Toast.makeText(this, "请输入货物名称!", Toast.LENGTH_LONG).show();
			return;
		}
		if (!Constant.isNum(count)) {
			Toast.makeText(this, "请输入货物数量!", Toast.LENGTH_LONG).show();
			return;
		}
		if (!Constant.isNum(yfStr)) {
			Toast.makeText(this, "请输入运费!", Toast.LENGTH_LONG).show();
			return;
		}
		if (!Constant.isNum(bzfStr)) {
			Toast.makeText(this, "请输入保险费!", Toast.LENGTH_LONG).show();
			return;
		}
		if (!Constant.isNum(thfStr)) {
			Toast.makeText(this, "请输入提货费!", Toast.LENGTH_LONG).show();
			return;
		}
		if (!Constant.isNum(shfStr)) {
			Toast.makeText(this, "请输入送货费!", Toast.LENGTH_LONG).show();
			return;
		}
		if (!Constant.isNum(bxfStr)) {
			Toast.makeText(this, "请输入保险费!", Toast.LENGTH_LONG).show();
			return;
		}
		if (!Constant.isNum(tbjzStr)) {
			Toast.makeText(this, "请输入投保价值!", Toast.LENGTH_LONG).show();
			return;
		}
		if (!Constant.isNum(hkStr)) {
			Toast.makeText(this, "请输入货款!", Toast.LENGTH_LONG).show();
			return;
		}

		stuff.setWplx(STUFF_TYPE[type.getSelectedItemPosition()]);
		stuff.setWpmc(stf);
		stuff.setSl(count);
		stuff.setBaozhuang(PACK_TYPE[pack.getSelectedItemPosition()]);
		stuff.setZl(weight.getText().toString());
		stuff.setSl_danwei(COUNT_TYPE[totalUnit.getSelectedItemPosition()]);
		stuff.setZl_danwei(UNIT[weightUnit.getSelectedItemPosition()]);
		stuff.setTiji(cubage.getText().toString());
		stuff.setTiji_danwei("m³");
		stuff.setYunfei(yfStr);
		stuff.setBaozhuangfei(bzfStr);
		stuff.setTihuofei(thfStr);
		stuff.setSonghuofei(shfStr);
		stuff.setBaofei(bxfStr);
		stuff.setZongyunfei(zyf.getText().toString());
		stuff.setTbjz(tbjzStr);
		stuff.setHuok(hkStr);
		if (yes.isChecked()) {
			stuff.setIsdsf("是");
		} else {
			stuff.setIsdsf("否");
		}
		stuff.setJl_danwei(allUnit);
		
		Intent data = new Intent();
		data.putExtra("stuff", stuff);
		setResult(RESULT_OK, data);
		this.finish();
	}

	public void showDialog() {
		DialogFragment newFragment = CalcWeightDialog.newInstance(
				weightUnit.getSelectedItemPosition(), 1);
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
			String tb = tbjz.getText().toString().trim();
			
			if (Constant.isNum(yun) && Constant.isNum(bz) && Constant.isNum(th) && Constant.isNum(sh) && Constant.isNum(tb)) {
				Double y = Double.parseDouble(yun);
				Double b = Double.parseDouble(bz);
				Double t = Double.parseDouble(th);
				Double ds = Double.parseDouble(sh);
				Double x = Double.parseDouble(tb)*2/1000;
				Double sum = y + b + t + ds + x;
				DecimalFormat f = new DecimalFormat("0.00");
				bxf.setText(f.format(x));
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
	// @Override
	// public boolean onCreateOptionsMenu(Menu menu) {
	// // Inflate the menu; this adds items to the action bar if it is present.
	// getMenuInflater().inflate(R.menu.stuff, menu);
	// return true;
	// }

}
