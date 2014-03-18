package com.honglang.lugang.qrcode;

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
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class StuffActivity extends Activity implements OnClickListener {

	private TextView title;
	private Button back;
	private Button confirm;
	
	public Button weight;
	public Button cubage;
	public Button yf;
	public EditText bzf;
	public EditText thf;
	public EditText shf;
	public EditText bxf;
	public EditText zyf;
	
	public Spinner type;
	public Spinner weightUnit;
	private static final String[] UNIT = new String[]{"t","kg"};
	private static String[] STUFF_TYPE;
	private ArrayAdapter<String> tAdapter;
	private ArrayAdapter<String> uAdapter;
	
	private Order stuff;
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
		
		weight = (Button) this.findViewById(R.id.weight);
		weight.setOnClickListener(this);
		cubage = (Button) this.findViewById(R.id.cubage);
		cubage.setOnClickListener(this);
		yf = (Button) this.findViewById(R.id.yf);
		yf.setOnClickListener(this);
		
		bzf = (EditText) this.findViewById(R.id.bzf);
		thf = (EditText) this.findViewById(R.id.thf);
		shf = (EditText) this.findViewById(R.id.shf);
		bxf = (EditText) this.findViewById(R.id.bxf);
		zyf = (EditText) this.findViewById(R.id.zyf);
		
		STUFF_TYPE = this.getResources().getStringArray(R.array.stuff_type);
		type = (Spinner) this.findViewById(R.id.type);
		weightUnit = (Spinner) this.findViewById(R.id.weightUnit);
		tAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, STUFF_TYPE);
		tAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		type.setAdapter(tAdapter);
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
//			ok();
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
