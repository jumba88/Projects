package com.honglang.lugang;

import com.honglang.lugang.office.CalcWeightDialog;
import com.honglang.lugang.office.Order;

import android.os.Bundle;
import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
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

public class CountActivity extends Activity implements OnClickListener {

	private Order data;
	private TextView title;
	private Button back;
	private Button confirm;
	private Switch isDsf;
	private TextView type;
	private TextView name;
	private TextView totalUnit;
	private EditText total;
	private EditText weight;
	private EditText cubage;
	private EditText yf;
	private EditText thf;
	private EditText shf;
	private EditText bxf;
	private EditText zyf;
	private EditText tbjz;
	private EditText dk;
	
	private Spinner weightUnit;
	private static final String[] UNIT = new String[]{"t","kg"};
	private ArrayAdapter<String> adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_count);
		data = (Order) this.getIntent().getExtras().getSerializable("order");
		init();
		
		if (savedInstanceState == null) {
			// First-time init; create fragment to embed in activity.
			FragmentTransaction ft = getFragmentManager().beginTransaction();
            DialogFragment newFragment = CalcWeightDialog.newInstance();
            ft.add(R.id.embedded, newFragment).commit();
		}
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
		weight = (EditText) this.findViewById(R.id.weight);
		cubage = (EditText) this.findViewById(R.id.cubage);
		yf = (EditText) this.findViewById(R.id.yf);
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
		weight.setOnTouchListener(new WeightTouchListener());
		cubage.setText(data.getTiji());
		yf.setText(data.getYunfei());
		thf.setText(data.getTihuofei());
		shf.setText(data.getSonghuofei());
		bxf.setText(data.getBaof());
		zyf.setText(data.getZongyunfei());
		tbjz.setText(data.getTbjz());
		dk.setText(data.getHuok());
		
		isDsf = (Switch) this.findViewById(R.id.dshk);
		if (data.getIsdsf().trim().equals("是")) {
			isDsf.setChecked(true);
		} else {
			isDsf.setChecked(false);
		}
		
		weightUnit = (Spinner) this.findViewById(R.id.weightUnit);
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, UNIT);
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		weightUnit.setAdapter(adapter);
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			this.finish();
			break;

		}
		
	}
	
	class SpinnerSelectedListener implements OnItemSelectedListener{

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	class WeightTouchListener implements OnTouchListener{

		int touch_flag=0;
		@Override
		public boolean onTouch(View v, MotionEvent event) {
            weight.setInputType(0);
            touch_flag++;
            if (touch_flag == 2) {
            	DialogFragment newFragment = CalcWeightDialog.newInstance();
//    			newFragment.getDialog().setTitle("总重量计算(最多6项)");
                newFragment.show(getFragmentManager(), "calc");
                touch_flag = 0;
			}
            return false;
		}
		
	}

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.count, menu);
//		return true;
//	}

}
