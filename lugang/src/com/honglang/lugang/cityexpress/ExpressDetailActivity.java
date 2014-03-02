package com.honglang.lugang.cityexpress;

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

public class ExpressDetailActivity extends Activity implements OnClickListener {

	private TextView title;
	private Button back;
	private Express data;
	private TextView fromcity;
	private TextView tocity;
	private TextView fromaddress;
	private TextView crosscity;
	private TextView toaddress;
	private TextView garden;
	private TextView tel;
	private Button dial;
	private TextView minprice;
	private TextView lightprice;
	private TextView haevyprice;
	private TextView details;
	private String number;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_express_detail);
		data = (Express) this.getIntent().getSerializableExtra("Express");
		init();
	}

	private void init(){
		title = (TextView) this.findViewById(R.id.title);
		title.setText("城际快线详情");
		back = (Button) this.findViewById(R.id.back);
		back.setOnClickListener(this);
		
		fromcity = (TextView) this.findViewById(R.id.fromcity);
		fromcity.setText(data.getFromcity());
		tocity = (TextView) this.findViewById(R.id.tocity);
		tocity.setText(data.getTocity());
		fromaddress = (TextView) this.findViewById(R.id.fromaddress);
		fromaddress.setText(data.getFromaddress());
		toaddress = (TextView) this.findViewById(R.id.toaddress);
		toaddress.setText(data.getToaddress());
		garden = (TextView) this.findViewById(R.id.garden);
		garden.setText(data.getWly_name());
		tel = (TextView) this.findViewById(R.id.tel);
		number = data.getWly_phone();
		tel.setText(number);
		minprice = (TextView) this.findViewById(R.id.minprice);
		if (Double.parseDouble(data.getMinprice()) == 0) {
			minprice.setText("--元");
		} else {
			minprice.setText(data.getMinprice()+"元");
		}
		
		lightprice = (TextView) this.findViewById(R.id.lightprice);
		if (Double.parseDouble(data.getLightprice()) == 0) {
			lightprice.setText("--元/立方");
		} else {
			lightprice.setText(data.getLightprice()+"元/立方");
		}
		
		haevyprice = (TextView) this.findViewById(R.id.haevyprice);
		if (Double.parseDouble(data.getHaevyprice()) == 0) {
			haevyprice.setText("--元/公斤");
		} else {
			haevyprice.setText(data.getHaevyprice()+"元/公斤");
		}
		
		details = (TextView) this.findViewById(R.id.details);
		details.setText(data.getDetails());
		dial = (Button) this.findViewById(R.id.dial);
		dial.setOnClickListener(this);
	}
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.express_detail, menu);
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
