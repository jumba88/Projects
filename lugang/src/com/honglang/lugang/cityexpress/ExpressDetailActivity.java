package com.honglang.lugang.cityexpress;

import com.honglang.lugang.R;
import com.honglang.lugang.R.layout;
import com.honglang.lugang.R.menu;

import android.os.Bundle;
import android.app.Activity;
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
		tel = (TextView) this.findViewById(R.id.tel);
		minprice = (TextView) this.findViewById(R.id.minprice);
		minprice.setText(data.getMinprice());
		lightprice = (TextView) this.findViewById(R.id.lightprice);
		lightprice.setText(data.getLightprice());
		haevyprice = (TextView) this.findViewById(R.id.haevyprice);
		haevyprice.setText(data.getHaevyprice());
		details = (TextView) this.findViewById(R.id.details);
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
		}
		
	}

}
