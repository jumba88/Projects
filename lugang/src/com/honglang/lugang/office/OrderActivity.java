package com.honglang.lugang.office;

import java.util.ArrayList;
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
import com.honglang.lugang.billsearch.BillDetailActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class OrderActivity extends Activity implements OnClickListener {

	private TextView title;
	private Button back;
	private Button confirm;
	private View sureGrid;
	private View yesNo;
	private TextView stuffCode;
	private TextView end;
	private TextView transport;
	private EditText sure;
	private EditText surePhone;
	private EditText suggest;
	private Button yes;
	private Button no;
	private ListView mListView;
	private List<Order> items;
	private List<Order> cache;
	private OrderAdapter adapter;
	private ProgressDialog progress;
	private String action = "GetFormInfoByFormOid";
	private String confirmAction = "PaiCheShouHuoRuKu";
	private Bill bill;
	private HashMap<String, String> infoMap;
	public static int position;
	public List<Integer> submit;
	// Definition of the one requestCode we use for receiving results.
	static final private int GET_CODE = 0;
	
	private static boolean checked = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order);
		
		init();
	}

	private void init(){
		title = (TextView) this.findViewById(R.id.title);
		
		back = (Button) this.findViewById(R.id.back);
		back.setOnClickListener(this);
		
		sureGrid = this.findViewById(R.id.sureGrid);
		yesNo = this.findViewById(R.id.yesno);
		
		bill = (Bill) this.getIntent().getExtras().getSerializable("bill");
		if (bill.getCurrent_node_id().equals("7")) {
			title.setText("等待货物入库");
			confirm = (Button) this.findViewById(R.id.ok);
			confirm.setText("确 认");
			confirm.setVisibility(View.VISIBLE);
			confirm.setOnClickListener(this);
		}
		if (bill.getCurrent_node_id().equals("9")) {
			title.setText("托运 确认");
			sureGrid.setVisibility(View.VISIBLE);
			yesNo.setVisibility(View.VISIBLE);
		}
		
		sure = (EditText) this.findViewById(R.id.sure);
		surePhone = (EditText) this.findViewById(R.id.surePhone);
		suggest = (EditText) this.findViewById(R.id.suggest);
		sure.setText(SessionManager.getInstance().getAccount().getName()+"");
		surePhone.setText(SessionManager.getInstance().getAccount().getPhone()+"");
		yes = (Button) findViewById(R.id.yes);
		no = (Button) findViewById(R.id.no);
		yes.setOnClickListener(this);
		no.setOnClickListener(this);
		
		stuffCode = (TextView) this.findViewById(R.id.stuffCode);
		end = (TextView) this.findViewById(R.id.end);
		transport = (TextView) this.findViewById(R.id.transport);
		
		mListView = (ListView) this.findViewById(R.id.stuffList);
		items = new ArrayList<Order>();
		cache = new ArrayList<Order>();
		submit = new ArrayList<Integer>();
		new LoadTask().execute((Void)null);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				position = arg2;
				Order order = items.get(arg2);
				Intent intent = new Intent(OrderActivity.this, CountActivity.class);
				intent.putExtra("order", order);
				OrderActivity.this.startActivityForResult(intent, GET_CODE);
				checked = true;
			}
		});
	}
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.order, menu);
//		return true;
//	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			this.finish();
			break;
		case R.id.ok:
			ok();
			break;
		case R.id.yes:
			yes();
			break;
		case R.id.no:
			no();
			break;
		}
	}
	
	public void ok(){
		for (int i = 0; i < items.size(); i++) {
			if (Integer.parseInt(items.get(i).getSl()) == 0) {
				Toast.makeText(OrderActivity.this, items.get(i).getWpmc()+"没有填写数量", Toast.LENGTH_SHORT).show();
				return;
			}
			if (Double.parseDouble(items.get(i).getYunfei()) == 0) {
				Toast.makeText(OrderActivity.this, items.get(i).getWpmc()+"没有填写运费", Toast.LENGTH_SHORT).show();
				return;
			}
		}
		new ConfirmTask().execute((Void)null);
	}
	public void yes(){
		if (checked) {
			if (sure.getText().toString().isEmpty()) {
				Toast.makeText(OrderActivity.this, "确认人必须填写！", Toast.LENGTH_SHORT).show();
				return;
			}
			if (surePhone.getText().toString().isEmpty()) {
				Toast.makeText(OrderActivity.this, "确认人电话必须填写！", Toast.LENGTH_SHORT).show();
				return;
			}
			new YesTask().execute((Void)null);
		} else {
			Toast.makeText(OrderActivity.this, "请先查看核对货物信息再确认", Toast.LENGTH_SHORT).show();
		}
	}
	public void no(){
		if (checked) {
			if (sure.getText().toString().isEmpty()) {
				Toast.makeText(OrderActivity.this, "必须填写确认人！", Toast.LENGTH_SHORT).show();
				return;
			}
			if (surePhone.getText().toString().isEmpty()) {
				Toast.makeText(OrderActivity.this, "必须填写确认人电话！", Toast.LENGTH_SHORT).show();
				return;
			}
			if (suggest.getText().toString().isEmpty()) {
				Toast.makeText(OrderActivity.this, "必须填写意见！", Toast.LENGTH_SHORT).show();
				return;
			}
			new NoTask().execute((Void)null);
		} else {
			Toast.makeText(OrderActivity.this, "请先查看核对货物信息再确认", Toast.LENGTH_SHORT).show();
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == GET_CODE) {
			if (resultCode == RESULT_OK) {
				Order item = new Order();
				item = (Order) data.getExtras().getSerializable("item");
				Log.i("suxoyo", item.getTbjz() +"/" + cache.get(position).getTbjz());
				if (Double.parseDouble(item.getTbjz()) != Double
						.parseDouble(cache.get(position).getTbjz())) {
					submit.set(position, 0);
				} else {
					submit.set(position, 1);
				}
				items.set(position, item);
			}
			int sum = 0;
			for (int i = 0; i < submit.size(); i++) {
				sum += submit.get(i);
			}
			if (submit.size() != sum) {
				yes.setEnabled(false);
			} else {
				yes.setEnabled(true);
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	class LoadTask extends AsyncTask<Void, Void, Boolean>{

		private String errMsg;
		@Override
		protected void onPreExecute() {
			progress = ProgressDialog.show(OrderActivity.this, null, "加载中...", false, false);
			super.onPreExecute();
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			SoapObject rpc = new SoapObject(Constant.NAMESPACE, action);
			rpc.addProperty("formOid", Long.parseLong(bill.getForm_oid()));
			rpc.addProperty("nodeid", Long.parseLong(bill.getCurrent_node_id()));
			rpc.addProperty("currentUserno", SessionManager.getInstance().getUsername());
			rpc.addProperty("token", SessionManager.getInstance().getTokene());
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(rpc);
			HttpTransportSE transport = new HttpTransportSE(Constant.SERVICE_URL);
			transport.debug = true;
			try {
				transport.call(Constant.NAMESPACE + action, envelope);
				SoapObject response = (SoapObject) envelope.bodyIn;
				if(response != null){
					JSONTokener parser = new JSONTokener(response.getPropertyAsString("GetFormInfoByFormOidResult"));
					JSONObject json = (JSONObject) parser.nextValue();
					Log.i("suxoyo", json.toString());
					if (json.getBoolean("result")) {
						items.clear();
						JSONObject data = json.getJSONObject("data");
						JSONObject form = data.getJSONObject("form");
						JSONObject info = form.getJSONObject("row");
						infoMap = new HashMap<String, String>();
						infoMap.put("fhcode", info.getString("fhcode"));
						infoMap.put("tocity", info.getString("tocity"));
						infoMap.put("wuliu", info.getString("wuliu"));
						
						JSONObject mxb = data.getJSONObject("mxb");
						JSONArray rows = mxb.getJSONArray("rows");
						for (int i = 0; i < rows.length(); i++) {
							JSONObject obj = rows.getJSONObject(i);
//							Log.i("suxoyo", obj.toString());
							Order item = new Order();
							item.setOid(obj.getString("oid"));
							item.setWplx(obj.getString("wplx"));
							item.setWpmc(obj.getString("wpmc"));
							item.setJl_danwei(obj.getString("jl_danwei"));
							item.setSl(obj.getString("sl"));
							item.setSl_danwei(obj.getString("sl_danwei"));
							item.setZl(obj.getString("zl"));
							item.setZl_danwei(obj.getString("zl_danwei").trim());
							item.setTiji(obj.getString("tiji"));
							item.setTiji_danwei(obj.getString("tiji_danwei"));
							item.setYunfei(obj.getString("yunfei"));
							item.setBaozhuangfei(obj.getString("baozhuangfei"));
							item.setTihuofei(obj.getString("tihuofei"));
							item.setSonghuofei(obj.getString("songhuofei"));
							item.setBaofei(obj.getString("baofei"));
							item.setZongyunfei(obj.getString("zongyunfei"));
							item.setTbjz(obj.getString("tbjz"));
							item.setIsdsf(obj.getString("isdsf"));
							item.setHuok(obj.getString("huok"));
							items.add(item);
							cache.add(item);
							submit.add(1);
						}
						return true;
					} else {
						errMsg = json.getString("msg");
						return false;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				errMsg = "加载工单失败,请检查您的网络是否正常";
			}
			return false;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			progress.dismiss();
			if (result) {
				stuffCode.setText(infoMap.get("fhcode"));
				end.setText(infoMap.get("tocity"));
				transport.setText(infoMap.get("wuliu"));
				
				adapter = new OrderAdapter(items, OrderActivity.this);
				if(adapter != null){
					mListView.setAdapter(adapter);
					mListView.setCacheColorHint(0);
					Constant.setListViewHeightBasedOnChildren(mListView);
				}
			} else {
				Toast.makeText(OrderActivity.this, errMsg, Toast.LENGTH_SHORT).show();
				OrderActivity.this.finish();
			}
			super.onPostExecute(result);
		}
		
	}

	class ConfirmTask extends AsyncTask<Void, Void, Boolean>{

		private String errMsg;
		private List<JSONObject> orderList;
		@Override
		protected void onPreExecute() {
			progress = ProgressDialog.show(OrderActivity.this, null, "货物正在入库...", false, false);
			super.onPreExecute();
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			orderList = new ArrayList<JSONObject>();
			for (int i = 0; i < items.size(); i++) {
				Order item = new Order();
				JSONObject orderMap = new JSONObject();
				item = items.get(i);
				try {
					orderMap.put("oid", item.getOid());
					orderMap.put("sl", item.getSl());
					orderMap.put("jl_danwei", item.getSl_danwei());
					orderMap.put("tiji", item.getTiji());
					orderMap.put("tiji_danwei", item.getTiji_danwei());
					orderMap.put("zl", item.getZl());
					orderMap.put("zl_danwei", item.getZl_danwei());
					orderMap.put("yunfei", item.getYunfei());
					orderMap.put("baozhuangfei", item.getBaozhuangfei());
					orderMap.put("tihuofei", item.getTihuofei());
					orderMap.put("songhuofei", item.getSonghuofei());
					orderMap.put("baofei", item.getBaofei());
					orderMap.put("zongyunfei", item.getZongyunfei());
					orderMap.put("tbjz", item.getTbjz());
					orderList.add(orderMap);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
//			Log.i("suxoyo", orderList.toString());
			SoapObject rpc = new SoapObject(Constant.NAMESPACE, confirmAction);
			rpc.addProperty("formOid", Long.parseLong(bill.getForm_oid()));
			rpc.addProperty("jsonMxbArrayString", orderList.toString());
			rpc.addProperty("currentUserno", SessionManager.getInstance().getUsername());
			rpc.addProperty("token", SessionManager.getInstance().getTokene());
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(rpc);
			HttpTransportSE transport = new HttpTransportSE(Constant.SERVICE_URL);
			transport.debug = true;
			try {
				transport.call(Constant.NAMESPACE + action, envelope);
				SoapObject response = (SoapObject) envelope.bodyIn;
				if(response != null){
					JSONTokener parser = new JSONTokener(response.getPropertyAsString("PaiCheShouHuoRuKuResult"));
					JSONObject json = (JSONObject) parser.nextValue();
//					Log.i("suxoyo", json.toString());
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
				Toast.makeText(OrderActivity.this, "操作成功,信息已发送到下单人!", Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(OrderActivity.this, "操作失败,"+errMsg, Toast.LENGTH_SHORT).show();
			}
			super.onPostExecute(result);
		}
		
	}
	
	class YesTask extends AsyncTask<Void, Void, Boolean>{

		private String errMsg;
		@Override
		protected void onPreExecute() {
			progress = ProgressDialog.show(OrderActivity.this, null, "正在确认...", false, false);
			super.onPreExecute();
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			SoapObject rpc = new SoapObject(Constant.NAMESPACE, "TuoYunQueRenYes");
			rpc.addProperty("formOid", Long.parseLong(bill.getForm_oid()));
			rpc.addProperty("tuoyunren", SessionManager.getInstance().getAccount().getName()+"");
			rpc.addProperty("currentUserno", SessionManager.getInstance().getUsername());
			rpc.addProperty("token", SessionManager.getInstance().getTokene());
			rpc.addProperty("yijian", suggest.getText().toString()+"");
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(rpc);
			HttpTransportSE transport = new HttpTransportSE(Constant.SERVICE_URL);
			transport.debug = true;
			try {
				transport.call(Constant.NAMESPACE + "TuoYunQueRenYes", envelope);
				SoapObject response = (SoapObject) envelope.bodyIn;
				Log.i("suxoyo", response.toString());
				if(response != null){
					JSONTokener parser = new JSONTokener(response.getPropertyAsString("TuoYunQueRenYesResult"));
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
				Toast.makeText(OrderActivity.this, "您已同意托运", Toast.LENGTH_SHORT).show();
				OrderActivity.this.finish();
			} else {
				Toast.makeText(OrderActivity.this, "确认出错," + errMsg, Toast.LENGTH_SHORT).show();
			}
			super.onPostExecute(result);
		}
		
	}
	class NoTask extends AsyncTask<Void, Void, Boolean>{
		private String errMsg;
		@Override
		protected void onPreExecute() {
			progress = ProgressDialog.show(OrderActivity.this, null, "正在确认...", false, false);
			super.onPreExecute();
		}
		
		@Override
		protected Boolean doInBackground(Void... params) {
			List<JSONObject> noList = new ArrayList<JSONObject>();
			for (int i = 0; i < items.size(); i++) {
				Order item = new Order();
				JSONObject orderMap = new JSONObject();
				item = items.get(i);
				try {
					orderMap.put("oid", item.getOid());
					orderMap.put("tbjz", item.getTbjz());
					noList.add(orderMap);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			
			SoapObject rpc = new SoapObject(Constant.NAMESPACE, "TuoYunQueRenNo");
			rpc.addProperty("formOid", Long.parseLong(bill.getForm_oid()));
			rpc.addProperty("tuoyunren", SessionManager.getInstance().getAccount().getName()+"");
			rpc.addProperty("currentUserno", SessionManager.getInstance().getUsername());
			rpc.addProperty("token", SessionManager.getInstance().getTokene());
			rpc.addProperty("jsonMxbArrayString", noList.toString());
			rpc.addProperty("yijian", suggest.getText().toString()+"");
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(rpc);
			HttpTransportSE transport = new HttpTransportSE(Constant.SERVICE_URL);
			transport.debug = true;
			try {
				transport.call(Constant.NAMESPACE + "TuoYunQueRenNo", envelope);
				SoapObject response = (SoapObject) envelope.bodyIn;
				if(response != null){
					JSONTokener parser = new JSONTokener(response.getPropertyAsString("TuoYunQueRenNoResult"));
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
			return null;
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			progress.dismiss();
			if (result) {
				Toast.makeText(OrderActivity.this, "您已不同意托运", Toast.LENGTH_SHORT).show();
				OrderActivity.this.finish();
			} else {
				Toast.makeText(OrderActivity.this, "确认出错," + errMsg, Toast.LENGTH_SHORT).show();
			}
			super.onPostExecute(result);
		}
		
	}
}
