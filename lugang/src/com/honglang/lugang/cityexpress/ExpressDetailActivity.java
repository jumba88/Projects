package com.honglang.lugang.cityexpress;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import com.honglang.lugang.Constant;
import com.honglang.lugang.R;
import com.honglang.lugang.R.layout;
import com.honglang.lugang.R.menu;
import com.honglang.lugang.out.OutListActivity;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.util.Log;
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
	
	private ProgressDialog progress;
	private List<HashMap<String, String>> items;
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
		
		crosscity = (TextView) this.findViewById(R.id.crosscity);
		crosscity.setOnClickListener(this);
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
		
		items = new ArrayList<HashMap<String,String>>();
		new LoadTask().execute((Void)null);
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
		case R.id.crosscity:
			Intent i = new Intent(this, CrossActivity.class);
			i.putExtra("cross", (Serializable)items);
			startActivity(i);
			break;
		}
	}
	
	class LoadTask extends AsyncTask<Void, Void, Boolean>{

		private String cross = "";
		@Override
		protected void onPreExecute() {
			if (progress != null) {
				progress = null;
			}
			progress = ProgressDialog.show(ExpressDetailActivity.this, null, "加载中...", false, false);
			super.onPreExecute();
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			SoapObject rpc = new SoapObject(Constant.NAMESPACE, "TkzxTJDList");
			rpc.addProperty("zxid", data.getId());
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(rpc);
			HttpTransportSE transport = new HttpTransportSE(Constant.SERVICE_URL);
			transport.debug = true;
			try {
				transport.call(Constant.NAMESPACE + "TkzxTJDList", envelope);
				SoapObject response = (SoapObject) envelope.bodyIn;
				if (response != null) {
					JSONTokener parser = new JSONTokener(response.getPropertyAsString("TkzxTJDListResult"));
					JSONObject json = (JSONObject) parser.nextValue();
					if (json.getBoolean("result")) {
						JSONObject data = json.getJSONObject("data");
						JSONArray rows = data.getJSONArray("rows");
						for (int i = 0; i < rows.length(); i++) {
							HashMap<String, String> item = new HashMap<String, String>();
							JSONObject obj = rows.getJSONObject(i);
							item.put("city", obj.getString("city"));
							item.put("address", obj.getString("address"));
							items.add(item);
							if (i < (rows.length()-1)) {
								cross += obj.getString("city") + "->";
							} else {
								cross += obj.getString("city");
							}
							
						}
						return true;
					}
				}
			} catch (Exception e) {
				Log.i("suxoyo", e.toString());
			}
			return false;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			progress.dismiss();
			if (result) {
				if (items.size() > 0) {
					crosscity.setText(cross);
				}else {
					crosscity.setClickable(false);
				}
			}
			super.onPostExecute(result);
		}
		
	}

}
