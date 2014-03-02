package com.honglang.lugang.assign;

import com.honglang.lugang.R;
import com.honglang.lugang.R.layout;
import com.honglang.lugang.R.menu;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class StuffDetailActivity extends Activity implements OnClickListener {

	private TextView title;
	private Button back;
	private Button dial;
	private Assign data;
	private TextView name;
	private TextView start;
	private TextView end;
	private TextView arriveTime;
	private TextView totalCount;
	private TextView totalWeight;
	private TextView cubage;
	private TextView contact;
	private TextView phone;
	private TextView detail;
	private String unit;
	private String number;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_stuff_detail);
		data = (Assign) this.getIntent().getSerializableExtra("Assign");
		init();
	}

	private void init(){
		title = (TextView) this.findViewById(R.id.title);
		title.setText("配货详情");
		back = (Button) this.findViewById(R.id.back);
		back.setOnClickListener(this);
		dial = (Button) this.findViewById(R.id.dial);
		dial.setOnClickListener(this);
		
		name = (TextView) this.findViewById(R.id.name);
		name.setText(data.getName());
		start = (TextView) this.findViewById(R.id.start);
		start.setText(data.getStartaddr());
		end = (TextView) this.findViewById(R.id.end);
		end.setText(data.getEndaddr());
		arriveTime = (TextView) this.findViewById(R.id.arriveTime);
		arriveTime.setText(data.getYdqx());
		totalCount = (TextView) this.findViewById(R.id.totalCount);
		totalCount.setText(data.getSl());
		cubage = (TextView) this.findViewById(R.id.cubage);
		if (data.getDanwei().equals("m&sup3;")) {
//			unit = "立方";
			Drawable drawable= getResources().getDrawable(R.drawable.m3);
			drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
//			totalCount.setCompoundDrawables(null, null, drawable, null);
			cubage.setCompoundDrawables(null, null, drawable, null);
//			totalCount.setText(data.getSl());
			cubage.setText(data.getRj());
		} else {
			unit = data.getDanwei();
//			totalCount.setText(data.getSl() + unit);
			cubage.setText(data.getRj() + unit);
		}
		
		totalWeight = (TextView) this.findViewById(R.id.totalWeight);
		totalWeight.setText(data.getZzl() + data.getZldanwei());
		
		contact = (TextView) this.findViewById(R.id.contact);
		contact.setText(data.getUsname());
		number = data.getPhone();
		phone = (TextView) this.findViewById(R.id.phone);
		phone.setText(number);
		detail = (TextView) this.findViewById(R.id.detail);
		detail.setText(data.getDetails());
	}
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.stuff_detail, menu);
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
