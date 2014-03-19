package com.honglang.lugang.qrcode;

import java.util.ArrayList;
import java.util.Calendar;
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
import com.honglang.lugang.SessionManager;
import com.honglang.lugang.R.layout;
import com.honglang.lugang.R.menu;
import com.honglang.lugang.office.CountActivity;
import com.honglang.lugang.office.Order;
import com.honglang.lugang.office.OrderAdapter;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class BlankActivity extends Activity implements OnClickListener {

	private TextView title;
	private Button back;
	private Button confirm;
	private String fhCode;
	
	private ProgressDialog progress;
	
	private Button date;
	public Button to;
	public Button from;
	public Button add;
	
	private ListView mListView;
	private List<Order> items;
	private OrderAdapter adapter;
	
	private int mYear;
    private int mMonth;
    private int mDay;
    
	static final int DATE_DIALOG_ID = 0;
	static final int TO_DIALOG_ID = 1;
	static final int FROM_DIALOG_ID = 2;
	public static final int ADD_CODE = 100;
	public static final int EDIT_CODE = 101;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_blank);
		
		init();
		
		final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        updateDisplay();
	}

	private void init(){
		title = (TextView) this.findViewById(R.id.title);
		title.setText("空白托运单");
		back = (Button) this.findViewById(R.id.back);
		back.setOnClickListener(this);
		confirm = (Button) this.findViewById(R.id.ok);
		confirm.setText("确 认");
		confirm.setVisibility(View.VISIBLE);
		confirm.setOnClickListener(this);
		fhCode = this.getIntent().getStringExtra("fhCode");
		
		new LoadTask().execute((Void)null);
		
		date = (Button) this.findViewById(R.id.ydqx);
		date.setOnClickListener(this);
		add = (Button) this.findViewById(R.id.add);
		add.setOnClickListener(this);
		to = (Button) this.findViewById(R.id.to);
		to.setOnClickListener(this);
		from = (Button) this.findViewById(R.id.from);
		from.setOnClickListener(this);
		
		mListView = (ListView) this.findViewById(R.id.stuffList);
		items = new ArrayList<Order>();
		adapter = new OrderAdapter(items, this);
		mListView.setAdapter(adapter);
		Constant.setListViewHeightBasedOnChildren(mListView);
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
		case R.id.ydqx:
			showDialog(DATE_DIALOG_ID);
			break;
		case R.id.add:
			Intent intent = new Intent(this, StuffActivity.class);
			intent.putExtra("action", ADD_CODE);
			this.startActivityForResult(intent, ADD_CODE);
			break;
		case R.id.ok:
//			ok();
			break;
		case R.id.from:
			DialogFragment frag = AreaDialog.newInstance(0);
			frag.show(getFragmentManager(), "area");
			break;
		case R.id.to:
			DialogFragment f = AreaDialog.newInstance(1);
			f.show(getFragmentManager(), "area");
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case ADD_CODE:
			if (resultCode == RESULT_OK) {
				Order item = new Order();
				item = (Order) data.getSerializableExtra("stuff");
				items.add(item);
				adapter.notifyDataSetChanged();
				Constant.setListViewHeightBasedOnChildren(mListView);
				Log.i("suxoyo", "onActivityResult"+item.getWplx());
			}
			break;

		default:
			break;
		}
	}

	class LoadTask extends AsyncTask<Void, Void, Boolean>{

		private String errMsg;
		@Override
		protected void onPreExecute() {
			progress = ProgressDialog.show(BlankActivity.this, null, "正在验证发货码...", false, false);
			super.onPreExecute();
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			SoapObject rpc = new SoapObject(Constant.NAMESPACE, "YanZhengTuoYunDan");
			rpc.addProperty("currentUserNo", SessionManager.getInstance().getUsername());
			rpc.addProperty("token", SessionManager.getInstance().getTokene());
			rpc.addProperty("fhCode", fhCode);
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(rpc);
			HttpTransportSE transport = new HttpTransportSE(Constant.SERVICE_URL);
			transport.debug = true;
			try {
				transport.call(Constant.NAMESPACE + "YanZhengTuoYunDan", envelope);
				SoapObject response = (SoapObject) envelope.bodyIn;
				if(response != null){
					JSONTokener parser = new JSONTokener(response.getPropertyAsString("YanZhengTuoYunDanResult"));
					JSONObject json = (JSONObject) parser.nextValue();
					Log.i("suxoyo", json.toString());
					if (json.getBoolean("result")) {
						return true;
					} else {
						errMsg = json.getString("msg");
						return false;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				errMsg = e.toString();
			}
			return false;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			progress.dismiss();
			if (!result) {
				Toast.makeText(BlankActivity.this, errMsg, Toast.LENGTH_SHORT).show();
				BlankActivity.this.finish();
			}
			super.onPostExecute(result);
		}
		
	}
	
	
	
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.blank, menu);
//		return true;
//	}

}
