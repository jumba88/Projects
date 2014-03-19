package com.honglang.lugang.qrcode;

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
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class StuffActivity extends Activity implements OnClickListener {

	private TextView title;
	private Button back;
	private Button confirm;
	
	public Button weight;
	public Button cubage;
	public Button yf;
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
	private static final String[] UNIT = new String[]{"t","kg"};
	private static String[] STUFF_TYPE;
	private static String[] PACK_TYPE;
	private ArrayAdapter<String> tAdapter;
	private ArrayAdapter<String> pAdapter;
	private ArrayAdapter<String> uAdapter;
	
	private Order stuff;
	private int action;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_stuff);
		
		init();
	}
	
	private void init(){
		title = (TextView) this.findViewById(R.id.title);
		title.setText("货物明细");
		back = (Button) this.findViewById(R.id.back);
		back.setOnClickListener(this);
		confirm = (Button) this.findViewById(R.id.ok);
		confirm.setText("确 认");
		confirm.setVisibility(View.VISIBLE);
		confirm.setOnClickListener(this);
		
		action = getIntent().getIntExtra("action", 0);
		if (action == 100) {
			stuff = new Order();
		} else {
			stuff = (Order) getIntent().getSerializableExtra("stuff");
		}
		
		weight = (Button) this.findViewById(R.id.weight);
		weight.setOnClickListener(this);
		cubage = (Button) this.findViewById(R.id.cubage);
		cubage.setOnClickListener(this);
		yf = (Button) this.findViewById(R.id.yf);
		yf.setOnClickListener(this);
		
		name = (EditText) this.findViewById(R.id.name);
		total = (EditText) this.findViewById(R.id.total);
		bzf = (EditText) this.findViewById(R.id.bzf);
		thf = (EditText) this.findViewById(R.id.thf);
		shf = (EditText) this.findViewById(R.id.shf);
		bxf = (EditText) this.findViewById(R.id.bxf);
		zyf = (EditText) this.findViewById(R.id.zyf);
		tbjz = (EditText) this.findViewById(R.id.tbjz);
		dk = (EditText) this.findViewById(R.id.dk);
		
		yes = (RadioButton) findViewById(R.id.dshk_yes);
		no = (RadioButton) findViewById(R.id.dshk_no);
		
		STUFF_TYPE = this.getResources().getStringArray(R.array.stuff_type);
		PACK_TYPE = this.getResources().getStringArray(R.array.pack_type);
		type = (Spinner) this.findViewById(R.id.type);
		pack = (Spinner) this.findViewById(R.id.pack);
		weightUnit = (Spinner) this.findViewById(R.id.weightUnit);
		tAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, STUFF_TYPE);
		tAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		type.setAdapter(tAdapter);
		pAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, PACK_TYPE);
		pAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		pack.setAdapter(pAdapter);
		uAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, UNIT);
		uAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		weightUnit.setAdapter(uAdapter);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;
		case R.id.ok:
			confirm();
			Intent data = new Intent();
			data.putExtra("stuff", stuff);
			setResult(RESULT_OK, data);
			this.finish();
			break;
		case R.id.weight:
			showDialog();
			break;
		case R.id.cubage:
			DialogFragment frag = CalcCubDialog.newInstance(1);
			frag.show(getFragmentManager(), "cubage");
			break;
		case R.id.yf:
			DialogFragment f = CalcMoneyDialog.newInstance(1);
			f.show(getFragmentManager(), "money");
			break;
		}
		
	}
	
	private void confirm(){
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
			Toast.makeText(this, "请输入货物名称!", Toast.LENGTH_SHORT).show();
			return;
		}
		if (!Constant.isNum(count)) {
			Toast.makeText(this, "请输入货物数量!", Toast.LENGTH_SHORT).show();
			return;
		}
		if (!Constant.isNum(yfStr)) {
			Toast.makeText(this, "请输入运费!", Toast.LENGTH_SHORT).show();
			return;
		}
		if (!Constant.isNum(bzfStr)) {
			Toast.makeText(this, "请输入保险费!", Toast.LENGTH_SHORT).show();
			return;
		}
		if (!Constant.isNum(thfStr)) {
			Toast.makeText(this, "请输入提货费!", Toast.LENGTH_SHORT).show();
			return;
		}
		if (!Constant.isNum(shfStr)) {
			Toast.makeText(this, "请输入送货费!", Toast.LENGTH_SHORT).show();
			return;
		}
		if (!Constant.isNum(bxfStr)) {
			Toast.makeText(this, "请输入保险费!", Toast.LENGTH_SHORT).show();
			return;
		}
		if (!Constant.isNum(tbjzStr)) {
			Toast.makeText(this, "请输入投保价值!", Toast.LENGTH_SHORT).show();
			return;
		}
		if (!Constant.isNum(hkStr)) {
			Toast.makeText(this, "请输入货款!", Toast.LENGTH_SHORT).show();
			return;
		}
		
		stuff.setWplx(STUFF_TYPE[type.getSelectedItemPosition()]);
		stuff.setWpmc(stf);
		stuff.setSl(count);
		stuff.setBaozhuang(PACK_TYPE[pack.getSelectedItemPosition()]);
		stuff.setZl(weight.getText().toString());
		stuff.setSl_danwei("件");
		stuff.setZl_danwei(UNIT[weightUnit.getSelectedItemPosition()]);
		stuff.setTiji(cubage.getText().toString());
		stuff.setTiji_danwei("m³");
		stuff.setYunfei(yfStr);
		stuff.setBaozhuangfei(bzfStr);
		stuff.setTihuofei(thfStr);
		stuff.setSonghuofei(shfStr);
		stuff.setBaozhuangfei(bxfStr);
		stuff.setZongyunfei(zyf.getText().toString());
		stuff.setTbjz(tbjzStr);
		stuff.setHuok(hkStr);
		if (yes.isChecked()) {
			stuff.setIsdsf("是");
		} else {
			stuff.setIsdsf("否");
		}
	}

	public void showDialog(){
		DialogFragment newFragment = CalcWeightDialog.newInstance(weightUnit.getSelectedItemPosition(),1);
        newFragment.show(getFragmentManager(), "weight");
	}
	
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.stuff, menu);
//		return true;
//	}

}
