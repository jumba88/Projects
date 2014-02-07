package com.honglang.lugang.company;

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

public class CompanyDetailActivity extends Activity implements OnClickListener {

	private Company data;
	private TextView title;
	private Button back;
	private TextView name;
	private TextView address;
	private TextView phone;
	private TextView fax;
	private TextView credit;
	private TextView scope;
	private TextView path;
	private Button dial;
	private String number;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_company_detail);
		data = (Company) this.getIntent().getSerializableExtra("Company");
		init();
	}

	private void init(){
		title = (TextView) this.findViewById(R.id.title);
		title.setText("物流企业详情");
		back = (Button) this.findViewById(R.id.back);
		back.setOnClickListener(this);
		
		name = (TextView) this.findViewById(R.id.name);
		name.setText(data.getName());
		address = (TextView) this.findViewById(R.id.address);
		address.setText(data.getAddress());
		credit = (TextView) this.findViewById(R.id.credit);
		credit.setText(data.getQiyexinyu());
		scope = (TextView) this.findViewById(R.id.scope);
		scope.setText(data.getQyjingyinfanwei());
		path = (TextView) this.findViewById(R.id.path);
		path.setText(data.getQywanlaixianlu());
		phone = (TextView) this.findViewById(R.id.phone);
		number = data.getPhone();
		phone.setText(number);
		fax = (TextView) this.findViewById(R.id.fax);
		fax.setText(data.getJychuanz());
		dial = (Button) this.findViewById(R.id.dial);
		dial.setOnClickListener(this);
	}
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.company_detail, menu);
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
