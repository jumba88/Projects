package com.honglang.lugang.issue;

import java.util.Calendar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import com.honglang.lugang.Constant;
import com.honglang.lugang.R;
import com.honglang.lugang.SessionManager;
import com.honglang.lugang.R.layout;
import com.honglang.lugang.qrcode.AreaDialog;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class IssueStuffActivity extends Activity implements OnClickListener {

	private TextView title;
	private Button back;
	private Button ok;
	
	private Button date;
	public Button to;
	public Button from;
	
	private EditText name;
	private EditText total;
	private EditText weight;
	private EditText cubage;
	private EditText contact;
	private EditText phone;
	private EditText remark;
	
	public Spinner pack;
	public Spinner weightUnit;
	public Spinner totalUnit;
	private static final String[] UNIT = new String[] { "t", "kg" };
	private static String[] PACK_TYPE;
	private static String[] COUNT_TYPE;
	private ArrayAdapter<String> pAdapter;
	private ArrayAdapter<String> uAdapter;
	private ArrayAdapter<String> cAdapter;
	
	private int mYear;
    private int mMonth;
    private int mDay;
    
    public boolean IS_TO = false;
	public boolean IS_FROM = false;
    
    static final int DATE_DIALOG_ID = 0;
	static final int TO_DIALOG_ID = 11;
	static final int FROM_DIALOG_ID = 12;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_issue_stuff);
		
		init();
		
		final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
	}

	private void init(){
		title = (TextView) this.findViewById(R.id.title);
		title.setText("货物详情");
		back = (Button) this.findViewById(R.id.back);
		back.setOnClickListener(this);
		ok = (Button) this.findViewById(R.id.ok);
		ok.setText("确定");
		ok.setVisibility(View.VISIBLE);
		ok.setOnClickListener(this);
		
		date = (Button) this.findViewById(R.id.ydqx);
		date.setOnClickListener(this);
		to = (Button) this.findViewById(R.id.to);
		to.setOnClickListener(this);
		from = (Button) this.findViewById(R.id.from);
		from.setOnClickListener(this);
		
		name = (EditText) this.findViewById(R.id.name);
		total = (EditText) this.findViewById(R.id.total);
		weight = (EditText) this.findViewById(R.id.weight);
		cubage = (EditText) this.findViewById(R.id.cubage);
		contact = (EditText) this.findViewById(R.id.contact);
		phone = (EditText) this.findViewById(R.id.phone);
		remark = (EditText) this.findViewById(R.id.remark);
		
		pack = (Spinner) this.findViewById(R.id.pack);
		weightUnit = (Spinner) this.findViewById(R.id.weightUnit);
		totalUnit = (Spinner) this.findViewById(R.id.totalUnit);
		
		PACK_TYPE = this.getResources().getStringArray(R.array.pack_type);
		COUNT_TYPE = this.getResources().getStringArray(R.array.count_type);
		
		cAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, COUNT_TYPE);
		cAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		totalUnit.setAdapter(cAdapter);
		
		pAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, PACK_TYPE);
		pAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		pack.setAdapter(pAdapter);
		
		uAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, UNIT);
		uAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		weightUnit.setAdapter(uAdapter);
		weightUnit.setSelection(0, true);
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DATE_DIALOG_ID:
			return new DatePickerDialog(this, mDateSetListener, mYear, mMonth, mDay);
		}
		return null;
	}

	@Override
	protected void onPrepareDialog(int id, Dialog dialog) {
		switch (id) {
		case DATE_DIALOG_ID:
            dialog.setTitle("运抵期限");
			((DatePickerDialog) dialog).updateDate(mYear, mMonth, mDay);
            break;
		}
	}
	
	private void updateDisplay(){
		date.setText(new StringBuilder().append(mYear).append("-").append(mMonth+1).append("-").append(mDay));
	}
	private OnDateSetListener mDateSetListener = new OnDateSetListener() {
		
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;
            updateDisplay();
		}
	};
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;
		case R.id.ok:
			new ConfirmTask().execute((Void)null);
			break;
		case R.id.ydqx:
			showDialog(DATE_DIALOG_ID);
			break;
		case R.id.from:
			DialogFragment frag = AreaDialog.newInstance(10);
			frag.show(getFragmentManager(), "area");
			break;
		case R.id.to:
			DialogFragment f = AreaDialog.newInstance(11);
			f.show(getFragmentManager(), "area");
			break;
		}
	}

	class ConfirmTask extends AsyncTask<Void, Void, Boolean>{

		@Override
		protected void onPreExecute() {
			
			super.onPreExecute();
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			JSONArray mxb = new JSONArray();
			JSONObject json = new JSONObject();
			try {
				json.put("name", name.getText());
				json.put("sl", total.getText());
				json.put("jl_danwei", COUNT_TYPE[totalUnit.getSelectedItemPosition()]);
				json.put("zzl", weight.getText());
				json.put("zldanwei", UNIT[weightUnit.getSelectedItemPosition()]);
				json.put("tiji", cubage.getText());
				json.put("tjdanwei", "m³");
				json.put("baozhuang", PACK_TYPE[pack.getSelectedItemPosition()]);
				json.put("startaddr", from.getText());
				json.put("endaddr", to.getText());
				json.put("usname", contact.getText());
				json.put("phone", phone.getText());
				json.put("details", remark.getText());
				json.put("ydqx", date.getText());
				mxb.put(json);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
			SoapObject rpc = new SoapObject(Constant.NAMESPACE, "PhInfoEdit");
			rpc.addProperty("id", "");
			rpc.addProperty("mode", "ADD");
			rpc.addProperty("jsonMxbArrayString", mxb.toString());
			rpc.addProperty("currentUserno", SessionManager.getInstance().getUsername());
			rpc.addProperty("token", SessionManager.getInstance().getTokene());
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(rpc);
			HttpTransportSE transport = new HttpTransportSE(Constant.SERVICE_URL);
			transport.debug = true;
			try {
				transport.call(Constant.NAMESPACE + "PhInfoEdit", envelope);
				SoapObject response = (SoapObject) envelope.bodyIn;
				if(response != null){
					JSONTokener parser = new JSONTokener(response.getPropertyAsString("PhInfoEditResult"));
					JSONObject obj = (JSONObject) parser.nextValue();
					Log.i("suxoyo", obj.toString());
					if (obj.getBoolean("result")) {
						
						return true;
					} else {
//						errMsg = json.getString("msg");
						return false;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
//				errMsg = e.toString();
//				errMsg = "操作失败，请稍候重试";
			}
			
			return false;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			
			super.onPostExecute(result);
		}
		
	}
	
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.issue_stuff, menu);
//		return true;
//	}

}
