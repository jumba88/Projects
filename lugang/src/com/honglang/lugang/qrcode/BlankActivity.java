package com.honglang.lugang.qrcode;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

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
import com.honglang.lugang.R.menu;
import com.honglang.lugang.office.CountActivity;
import com.honglang.lugang.office.Order;
import com.honglang.lugang.office.OrderActivity;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
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
	
	public boolean IS_TO = false;
	public boolean IS_FROM = false;
	public boolean IS_ADD = false;
	
	private RadioButton istb;
	private RadioButton istx;
	private RadioButton sh;
	
	private RadioButton xj;
	private RadioButton tf;
	private RadioButton jz;
	
	private EditText name_sh;
	private EditText phone_sh;
	private EditText address_sh;
	private EditText code_sh;
	
	private EditText name_ty;
	private EditText phone_ty;
	private EditText address_ty;
	private EditText code_ty;
	
	private EditText name_kd;
	private EditText phone_kd;
	private EditText remark;
	
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
	
	public static int position;
	
	String nameTy;
	String phoneTy;
	String addressTy;
	String codeTy;
	
	String nameSh;
	String phoneSh;
	String addressSh;
	String codeSh;
	
	String nameKd;
	String phoneKd;
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
		
		istb = (RadioButton) findViewById(R.id.istb_yes);
		istx = (RadioButton) findViewById(R.id.istx_yes);
		sh = (RadioButton) findViewById(R.id.sh);
		
		xj = (RadioButton) findViewById(R.id.xj);
		tf = (RadioButton) findViewById(R.id.tf);
		jz = (RadioButton) findViewById(R.id.jz);
		
		name_kd = (EditText) this.findViewById(R.id.name_kd);
		nameKd = SessionManager.getInstance().getAccount().getName();
		name_kd.setText(nameKd+"");
		phone_kd = (EditText) this.findViewById(R.id.phone_kd);
		remark = (EditText) this.findViewById(R.id.remark);
		
		name_ty = (EditText) this.findViewById(R.id.name_ty);
		phone_ty = (EditText) this.findViewById(R.id.phone_ty);
		address_ty = (EditText) this.findViewById(R.id.address_ty);
		code_ty = (EditText) this.findViewById(R.id.code_ty);
		
		name_sh = (EditText) this.findViewById(R.id.name_sh);
		phone_sh = (EditText) this.findViewById(R.id.phone_sh);
		address_sh = (EditText) this.findViewById(R.id.address_sh);
		code_sh = (EditText) this.findViewById(R.id.code_sh);
		
		mListView = (ListView) this.findViewById(R.id.stuffList);
		items = new ArrayList<Order>();
		adapter = new OrderAdapter(items, this);
		mListView.setAdapter(adapter);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				position = arg2;
				Order order = items.get(arg2);
				Intent intent = new Intent(BlankActivity.this, StuffActivity.class);
				intent.putExtra("action", EDIT_CODE);
				intent.putExtra("stuff", order);
				startActivityForResult(intent, EDIT_CODE);
			}
		});
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
			confirm();
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
	
	private void confirm(){
		nameTy = name_ty.getText().toString().trim();
		phoneTy = phone_ty.getText().toString().trim();
		addressTy = address_ty.getText().toString().trim();
		codeTy = code_ty.getText().toString().trim();
		
		nameSh = name_sh.getText().toString().trim();
		phoneSh = phone_sh.getText().toString().trim();
		addressSh = address_sh.getText().toString().trim();
		codeSh = code_sh.getText().toString().trim();
		
		phoneKd = phone_kd.getText().toString().trim(); 
		
		if (nameSh == null || nameSh.equals("")) {
			Toast.makeText(this, "请填写收货人姓名", Toast.LENGTH_SHORT).show();
			return;
		}
		if (phoneSh == null || phoneSh.equals("")) {
			Toast.makeText(this, "请填写收货人电话", Toast.LENGTH_SHORT).show();
			return;
		}
		if (codeSh == null || codeSh.equals("")) {
			Toast.makeText(this, "请填写收货人身份证号码", Toast.LENGTH_SHORT).show();
			return;
		}
		
		if (nameTy == null || nameTy.equals("")) {
			Toast.makeText(this, "请填写托运人姓名", Toast.LENGTH_SHORT).show();
			return;
		}
		if (phoneTy == null || phoneTy.equals("")) {
			Toast.makeText(this, "请填写托运人电话", Toast.LENGTH_SHORT).show();
			return;
		}
		if (codeTy == null || codeTy.equals("")) {
			Toast.makeText(this, "请填写托运人身份证号码货企业代码", Toast.LENGTH_SHORT).show();
			return;
		}
		
		if (!IS_TO) {
			Toast.makeText(this, "请填写收货人目标城市", Toast.LENGTH_SHORT).show();
			return;
		}
		if (!IS_FROM) {
			Toast.makeText(this, "请填写托运人始发城市", Toast.LENGTH_SHORT).show();
			return;
		}
		
		if (phoneKd == null || phoneKd.equals("")) {
			Toast.makeText(this, "请填写开单人电话", Toast.LENGTH_SHORT).show();
			return;
		}
		
		if (!IS_ADD) {
			Toast.makeText(this, "您还没有填写要托运的货物", Toast.LENGTH_SHORT).show();
			return;
		}
		new ConfirmTask().execute((Void)null);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case ADD_CODE:
			if (resultCode == RESULT_OK) {
				items.add((Order) data.getSerializableExtra("stuff"));
				adapter.notifyDataSetChanged();
				Constant.setListViewHeightBasedOnChildren(mListView);
				IS_ADD = true;
			}
			break;

		case EDIT_CODE:
			if (resultCode == RESULT_OK) {
				items.set(position, (Order) data.getSerializableExtra("stuff"));
				adapter.notifyDataSetChanged();
			}
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
	
	class ConfirmTask extends AsyncTask<Void, Void, Boolean>{

		private String errMsg;
		@Override
		protected void onPreExecute() {
			progress = ProgressDialog.show(BlankActivity.this, null, "货物正在入库...", false, false);
			super.onPreExecute();
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			JSONObject str = new JSONObject();
			JSONObject form = new JSONObject();
			JSONArray mxb = new JSONArray();

			try {
				if (istb.isChecked()) {
					form.put("istoub", "是");
				} else {
					form.put("istoub", "否");
				}
				if (istx.isChecked()) {
					form.put("istx", "是");
				} else {
					form.put("istx", "否");
				}
				if (sh.isChecked()) {
					form.put("shfs", 0);
				} else {
					form.put("shfs", 1);
				}
				if (xj.isChecked()) {
					form.put("zffs0", 0);
				}
				if (tf.isChecked()) {
					form.put("zffs0", 1);
				}
				if (jz.isChecked()) {
					form.put("zffs0", 2);
				}
				form.put("ydqx", date.getText());
				form.put("fhrxingming", nameSh);
				form.put("fhrdianh", phoneSh);
				form.put("tocity", to.getText());
				form.put("fhrdizhi", addressSh+"");
				form.put("fhrsfz", codeSh);
				form.put("tyxingming", nameTy);
				form.put("tydianh", phoneTy);
				form.put("fromcity", from.getText());
				form.put("tydizhi", addressTy+"");
				form.put("tysfz", codeTy+"");
				form.put("kdr", nameKd);
				form.put("kdrdianh", phoneKd);
				form.put("beizhu", remark.getText()+"");
				
				for (int i = 0; i < items.size(); i++) {
					Order item = items.get(i);
					JSONObject obj = new JSONObject();
					obj.put("wplx", item.getWplx());
					obj.put("wpmc", item.getWpmc());
					obj.put("sl", item.getSl());
					obj.put("sl_danwei", item.getSl_danwei());
					obj.put("baozhuang", item.getBaozhuang());
					obj.put("jl_danwei", item.getJl_danwei());
					obj.put("zl", item.getZl());
					obj.put("zl_danwei", item.getZl_danwei());
					obj.put("tiji", item.getTiji());
					obj.put("tiji_danwei", item.getTiji_danwei());
					obj.put("yunfei", item.getYunfei());
					obj.put("baozhuangfei", item.getBaozhuangfei());
					obj.put("tihuofei", item.getTihuofei());
					obj.put("songhuofei", item.getSonghuofei());
					obj.put("baofei", item.getBaofei());
					obj.put("zongyunfei", item.getZongyunfei());
					obj.put("tbjz", item.getTbjz());
					obj.put("isdsf", item.getIsdsf());
					obj.put("huok", item.getHuok());
					mxb.put(obj);
				}
				
				str.put("form", form);
				str.put("mxb", mxb);
			} catch (JSONException e1) {
				
				e1.printStackTrace();
			}
			SoapObject rpc = new SoapObject(Constant.NAMESPACE, "WuLiuZhiJieRuKu");
			rpc.addProperty("fhCode", fhCode);
			rpc.addProperty("jsonFormMxbString", str.toString());
			rpc.addProperty("currentUserNo", SessionManager.getInstance().getUsername());
			rpc.addProperty("token", SessionManager.getInstance().getTokene());
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(rpc);
			HttpTransportSE transport = new HttpTransportSE(Constant.SERVICE_URL);
			transport.debug = true;
			try {
				transport.call(Constant.NAMESPACE + "WuLiuZhiJieRuKu", envelope);
				SoapObject response = (SoapObject) envelope.bodyIn;
				if(response != null){
					JSONTokener parser = new JSONTokener(response.getPropertyAsString("WuLiuZhiJieRuKuResult"));
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
			if (result) {
				Toast.makeText(BlankActivity.this, "发货码"+fhCode+"入库成功", Toast.LENGTH_SHORT).show();
				confirm.setEnabled(false);
			} else {
				Toast.makeText(BlankActivity.this, "操作失败"+errMsg, Toast.LENGTH_SHORT).show();
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
