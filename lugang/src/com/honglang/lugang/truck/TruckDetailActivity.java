package com.honglang.lugang.truck;

import com.honglang.lugang.R;
import com.honglang.lugang.R.layout;
import com.honglang.lugang.R.menu;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class TruckDetailActivity extends Activity implements OnClickListener {

	private Truck data;
	private TextView title;
	private Button back;
	private Button dial;
	private TextView name;
	private TextView model;
	private TextView brand;
	private TextView cubage;
	private TextView price;
	private TextView start;
	private TextView end;
	private TextView contact;
	private TextView phone;
	private TextView time;
	private TextView remark;
	private String number;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_truck_detail);
		data = (Truck) this.getIntent().getSerializableExtra("Truck");
		init();
	}

	private void init(){
		title = (TextView) this.findViewById(R.id.title);
		title.setText("空车详情");
		back = (Button) this.findViewById(R.id.back);
		back.setOnClickListener(this);
		
		name = (TextView) this.findViewById(R.id.name);
		name.setText(data.getChep());
		model = (TextView) this.findViewById(R.id.model);
		model.setText(data.getChex());
		brand = (TextView) this.findViewById(R.id.brand);
		brand.setText(data.getPingp());
		cubage = (TextView) this.findViewById(R.id.cubage);
		cubage.setText(data.getRongl());
		price = (TextView) this.findViewById(R.id.price);
		price.setText(data.getPrice());
		start = (TextView) this.findViewById(R.id.start);
		start.setText(data.getStartaddr());
		end = (TextView) this.findViewById(R.id.end);
		end.setText(data.getEndaddr());
		contact = (TextView) this.findViewById(R.id.contact);
		contact.setText(data.getRealname());
		phone = (TextView) this.findViewById(R.id.phone);
		number = data.getPhone();
		phone.setText(number);
		time = (TextView) this.findViewById(R.id.time);
		time.setText(data.getAdddate());
		remark = (TextView) this.findViewById(R.id.remark);
		remark.setText(data.getRemark());
		dial = (Button) this.findViewById(R.id.dial);
		dial.setOnClickListener(this);
	}
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.truck_detail, menu);
//		return true;
//	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			this.finish();
			break;
		case R.id.dial:
			Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + number.replace("-", "")));
			this.startActivity(intent);
			break;
		}		
	}

}
