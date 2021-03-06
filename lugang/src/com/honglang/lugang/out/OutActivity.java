package com.honglang.lugang.out;

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
import com.honglang.lugang.R.array;
import com.honglang.lugang.R.id;
import com.honglang.lugang.R.layout;
import com.honglang.lugang.qrcode.DriverActivity;
import com.honglang.zxing.CaptureActivity;

import android.media.AudioManager;
import android.media.SoundPool;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class OutActivity extends Activity implements OnClickListener {

	private TextView title;
	private Button back;
	private Button confirm;
	private Button again;
	private Button go;

	private String fhCode;

	private SoundPool soundPool;

	// private EditText number;
	// private EditText driver;
	// private EditText driverId;
	// private EditText driverNumber;
	//
	// private Button choose;
	//
	// private Spinner province;
	// private Spinner code;
	// private String[] provinces;
	// private String[] codes;
	// private ArrayAdapter<String> proAdapter;
	// private ArrayAdapter<String> codeAdapter;

	private ListView mListView;
	private List<HashMap<String, String>> items;
	private OutAdapter adapter;

	private List<JSONObject> outList;
	private ProgressDialog progress;

	// private String truck;
	// String num;
	// String driName;
	// String driId;
	// String driNum;

	String pcId;

	static final private int GET_CODE = 0;

	private static int FROM;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_out);

		init();
	}

	private void init() {
		title = (TextView) this.findViewById(R.id.title);
		title.setText("配车单货物明细表");
		back = (Button) this.findViewById(R.id.back);
		back.setOnClickListener(this);
		confirm = (Button) this.findViewById(R.id.ok);
		confirm.setText("确认数量 ");
		confirm.setVisibility(View.VISIBLE);
		confirm.setOnClickListener(this);
		again = (Button) this.findViewById(R.id.again);
		go = (Button) this.findViewById(R.id.go);

		soundPool = new SoundPool(2, AudioManager.STREAM_SYSTEM, 5);
		soundPool.load(this, R.raw.success, 1);
		soundPool.load(this, R.raw.failed, 2);

		// province = (Spinner) this.findViewById(R.id.province);
		// code = (Spinner) this.findViewById(R.id.code);
		// provinces = this.getResources().getStringArray(R.array.province);
		// codes = this.getResources().getStringArray(R.array.province_codes);
		// proAdapter = new ArrayAdapter<String>(this,
		// android.R.layout.simple_spinner_item, provinces);
		// codeAdapter = new ArrayAdapter<String>(this,
		// android.R.layout.simple_spinner_item, codes);
		// proAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// codeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// province.setAdapter(proAdapter);
		// code.setAdapter(codeAdapter);
		//
		// number = (EditText) this.findViewById(R.id.number);
		// driver = (EditText) this.findViewById(R.id.driver);
		// driverId = (EditText) this.findViewById(R.id.driver_id);
		// driverNumber = (EditText) this.findViewById(R.id.driver_number);
		//
		// choose = (Button) this.findViewById(R.id.choose);
		// choose.setOnClickListener(this);

		mListView = (ListView) this.findViewById(R.id.stuffList);
		items = new ArrayList<HashMap<String, String>>();
		outList = new ArrayList<JSONObject>();

		pcId = getIntent().getStringExtra("pcId");
		FROM = getIntent().getIntExtra("from", 0);
		switch (FROM) {
		case 0:
			fhCode = this.getIntent().getExtras().getString("fhCode");
			new LoadTask().execute((Void) null);
			again.setVisibility(View.VISIBLE);
			again.setOnClickListener(this);
			break;
		case 1:
			go.setVisibility(View.VISIBLE);
			go.setOnClickListener(this);
			progress = ProgressDialog.show(OutActivity.this, null, "加载中...",
					false, false);
			new LoadInfoTask().execute((Void) null);
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// if (requestCode == GET_CODE) {
		// if (resultCode == RESULT_OK) {
		// driver.setText(data.getStringExtra("sj"));
		// ;
		// driverNumber.setText(data.getStringExtra("dh"));
		// ;
		// driverId.setText(data.getStringExtra("sfz"));
		// ;
		// }
		// }
		if (requestCode == 300) {
			if (resultCode == RESULT_OK) {
				fhCode = data.getStringExtra("fhCode");
				new LoadTask().execute((Void) null);
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;
		case R.id.ok:
			// ok();
			new SubmitTask().execute((Void) null);
			break;
		case R.id.again:
			Intent data = new Intent();
			data.putExtra("pcid", pcId);
			Log.i("suxoyo", "2-pcid=" + pcId);
			setResult(RESULT_OK, data);
			finish();
			break;
		case R.id.go:
			Intent intent = new Intent(this, CaptureActivity.class);
			intent.putExtra("QRTYPE", 5);
			intent.putExtra("pcId", pcId);
			this.startActivityForResult(intent, 300);
			break;
		// case R.id.choose:
		// Intent intent = new Intent(this, DriverActivity.class);
		// this.startActivityForResult(intent, GET_CODE);
		// break;
		}

	}

	// private void ok(){
	// num = number.getText().toString().trim();
	// driName = driver.getText().toString().trim();
	// driId = driverId.getText().toString().trim();
	// driNum = driverNumber.getText().toString().trim();
	// truck = provinces[province.getSelectedItemPosition()] +
	// codes[code.getSelectedItemPosition()] + num;
	// if (num == null || num.equals("")) {
	// Toast.makeText(OutActivity.this, "请输入车牌号", Toast.LENGTH_SHORT).show();
	// return;
	// }
	//
	// if (driName == null || driName.equals("")) {
	// Toast.makeText(OutActivity.this, "请输入司机姓名", Toast.LENGTH_SHORT).show();
	// return;
	// }
	//
	// if (driId == null || driId.equals("")) {
	// Toast.makeText(OutActivity.this, "请输入司机身份证号码",
	// Toast.LENGTH_SHORT).show();
	// return;
	// }
	//
	// if (driNum == null || driNum.equals("")) {
	// Toast.makeText(OutActivity.this, "请输入司机电话号码", Toast.LENGTH_SHORT).show();
	// return;
	// }
	//
	// outList.clear();
	// for (int i = 0; i < items.size(); i++) {
	// if (adapter.selected.get(i) == 1) {
	// if (!Constant.isNum(adapter.ids.get(i))) {
	// Toast.makeText(OutActivity.this,
	// "请输入货物"+items.get(i).get("wpmc")+"的预装数量", Toast.LENGTH_SHORT).show();
	// return;
	// } else {
	// JSONObject json = new JSONObject();
	// try {
	// json.put("oid", items.get(i).get("OID"));
	// json.put("sl", items.get(i).get(""));
	// } catch (JSONException e) {
	// e.printStackTrace();
	// }
	// outList.add(json);
	// }
	// }
	// }
	// Log.i("suxoyo", outList.toString());
	// if (outList.size() > 0) {
	// new ConfirmTask().execute((Void)null);
	// } else {
	// Toast.makeText(this, "请选择订单!", Toast.LENGTH_SHORT).show();
	// return;
	// }
	//
	// }
	//
	class LoadTask extends AsyncTask<Void, Void, Boolean> {

		private String errMsg;

		@Override
		protected void onPreExecute() {
			progress = ProgressDialog.show(OutActivity.this, null,
					"订单货物正在加入明细表...", false, false);
			super.onPreExecute();
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			// SoapObject rpc = new SoapObject(Constant.NAMESPACE,
			// "GetMxbByFhcodeForChuKu");
			// rpc.addProperty("fhCode", fhCode);
			// rpc.addProperty("currentUserNo",
			// SessionManager.getInstance().getUsername());
			// rpc.addProperty("token",
			// SessionManager.getInstance().getTokene());

			SoapObject rpc = new SoapObject(Constant.NAMESPACE,
					"PeiCheChuKuAdd");
			rpc.addProperty("fhCode", fhCode);
			rpc.addProperty("pcid", pcId);
			Log.i("suxoyo", "1-pcid=" + pcId);
			rpc.addProperty("currentUserno", SessionManager.getInstance()
					.getUsername());
			rpc.addProperty("token", SessionManager.getInstance().getTokene());
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER12);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(rpc);
			HttpTransportSE transport = new HttpTransportSE(
					Constant.SERVICE_URL);
			transport.debug = true;
			try {
				// transport.call(Constant.NAMESPACE + "GetMxbByFhcodeForChuKu",
				// envelope);
				transport.call(Constant.NAMESPACE + "PeiCheChuKuAdd", envelope);
				SoapObject response = (SoapObject) envelope.bodyIn;
				if (response != null) {
					// JSONTokener parser = new
					// JSONTokener(response.getPropertyAsString("GetMxbByFhcodeForChuKuResult"));
					JSONTokener parser = new JSONTokener(
							response.getPropertyAsString("PeiCheChuKuAddResult"));
					JSONObject json = (JSONObject) parser.nextValue();
					Log.i("suxoyo", json.toString());

					if (json.getBoolean("result")) {
						JSONObject data = json.getJSONObject("data");
						pcId = data.getString("pcid");

					} else {
						errMsg = json.getString("msg");
						return false;
					}
					return true;
				}
			} catch (Exception e) {
				e.printStackTrace();
				errMsg = e.toString();
				errMsg = "操作失败，请稍候重试";
			}
			return false;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			if (result) {

				new LoadInfoTask().execute((Void) null);
			} else {
				progress.dismiss();
				Toast.makeText(OutActivity.this, errMsg, Toast.LENGTH_LONG)
						.show();
				Log.i("suxoyo", errMsg);
				soundPool.play(2, 1, 1, 2, 0, 1);
				if (FROM == 0) {
					OutActivity.this.finish();
				}

			}
			super.onPostExecute(result);
		}

	}

	class LoadInfoTask extends AsyncTask<Void, Void, Boolean> {

		private String errMsg;

		@Override
		protected Boolean doInBackground(Void... params) {
			SoapObject rpc = new SoapObject(Constant.NAMESPACE, "GetPCmxb");
			rpc.addProperty("PCID", Long.parseLong(pcId));
			rpc.addProperty("currentUserno", SessionManager.getInstance()
					.getUsername());
			rpc.addProperty("token", SessionManager.getInstance().getTokene());
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER12);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(rpc);
			HttpTransportSE transport = new HttpTransportSE(
					Constant.SERVICE_URL);
			transport.debug = true;
			try {
				// transport.call(Constant.NAMESPACE + "GetMxbByFhcodeForChuKu",
				// envelope);
				transport.call(Constant.NAMESPACE + "GetPCmxb", envelope);
				SoapObject response = (SoapObject) envelope.bodyIn;
				if (response != null) {
					// JSONTokener parser = new
					// JSONTokener(response.getPropertyAsString("GetMxbByFhcodeForChuKuResult"));
					JSONTokener parser = new JSONTokener(
							response.getPropertyAsString("GetPCmxbResult"));
					JSONObject json = (JSONObject) parser.nextValue();
					Log.i("suxoyo", json.toString());

					if (json.getBoolean("result")) {
						JSONObject data = json.getJSONObject("data");

						if (data.getInt("currentrowcount") > 0) {
							items.clear();
							JSONArray rows = data.getJSONArray("rows");

							for (int i = 0; i < rows.length(); i++) {
								JSONObject obj = rows.getJSONObject(i);
								HashMap<String, String> map = new HashMap<String, String>();
								map.put("rkid", obj.getString("rkid"));
								map.put("oid", obj.getString("oid"));
								map.put("wpmc", obj.getString("wpmc"));
								map.put("zongliang", obj.getString("zongliang"));
								map.put("yusl", obj.getString("yusl"));
								map.put("tiji", obj.getString("tiji"));
								map.put("zongliangdanwei",
										obj.getString("zongliangdanwei"));
								map.put("huoh", obj.getString("huoh"));
								map.put("fhcode", obj.getString("fhcode"));
								map.put("tocity", obj.getString("tocity"));
								items.add(map);
							}
							return true;
						} else {
							errMsg = "没有对应订单货物";
							return false;
						}

					} else {
						errMsg = json.getString("msg");
						return false;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				// errMsg = e.toString();
				errMsg = "操作失败，请稍候重试";
			}
			return false;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			progress.dismiss();
			if (result) {
				adapter = new OutAdapter(OutActivity.this, items);
				if (adapter != null) {
					mListView.setAdapter(adapter);
					mListView.setCacheColorHint(0);
					Constant.setListViewHeightBasedOnChildren(mListView);
				}
				soundPool.play(1, 1, 1, 1, 0, 1);
			} else {
				Toast.makeText(OutActivity.this, errMsg, Toast.LENGTH_LONG)
						.show();
				soundPool.play(2, 1, 1, 1, 0, 1);
				if (FROM == 0) {
					OutActivity.this.finish();
				} 
			}
			super.onPostExecute(result);
		}

	}

	class SubmitTask extends AsyncTask<Void, Void, Boolean> {

		private String errMsg;

		@Override
		protected void onPreExecute() {
			progress = ProgressDialog.show(OutActivity.this, null, "正在提交...",
					false, false);
			super.onPreExecute();
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			JSONArray array = new JSONArray();

			try {
				for (int i = 0; i < items.size(); i++) {
					JSONObject obj = new JSONObject();
					obj.put("oid", items.get(i).get("oid"));
					obj.put("sl", adapter.list.get(i));
					array.put(obj);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

			Log.i("suxoyo", "array=" + array.toString());
			// Log.i("suxoyo", pcId);

			SoapObject rpc = new SoapObject(Constant.NAMESPACE,
					"PeiCheChuKuRemove");
			rpc.addProperty("jsonOidSlArrayString", array.toString());
			rpc.addProperty("currentUserno", SessionManager.getInstance()
					.getUsername());
			rpc.addProperty("token", SessionManager.getInstance().getTokene());
			rpc.addProperty("pcid", Long.parseLong(pcId));
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER12);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(rpc);
			HttpTransportSE transport = new HttpTransportSE(
					Constant.SERVICE_URL);
			transport.debug = true;
			try {
				transport.call(Constant.NAMESPACE + "PeiCheChuKuRemove",
						envelope);
				SoapObject response = (SoapObject) envelope.bodyIn;
				if (response != null) {
					JSONTokener parser = new JSONTokener(
							response.getPropertyAsString("PeiCheChuKuRemoveResult"));
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
				// errMsg = e.toString();
				errMsg = "操作失败，请稍候重试";
				e.printStackTrace();
			}
			return false;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			progress.dismiss();
			if (result) {
				Toast.makeText(OutActivity.this, "操作成功", Toast.LENGTH_LONG)
						.show();
				soundPool.play(1, 1, 1, 1, 0, 1);
				new LoadInfoTask().execute((Void) null);
			} else {
				Toast.makeText(OutActivity.this, errMsg, Toast.LENGTH_LONG)
						.show();
				soundPool.play(2, 1, 1, 1, 0, 1);
				OutActivity.this.finish();
			}
			super.onPostExecute(result);
		}

	}

	class ConfirmTask extends AsyncTask<Void, Void, Boolean> {

		private String errMsg;

		@Override
		protected void onPreExecute() {
			progress = ProgressDialog.show(OutActivity.this, null, "正在提交...",
					false, false);
			super.onPreExecute();
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			SoapObject rpc = new SoapObject(Constant.NAMESPACE, "YuPeiCheChuKu");
			rpc.addProperty("jsonMxbArrayString", outList.toString());
			rpc.addProperty("currentUserno", SessionManager.getInstance()
					.getUsername());
			rpc.addProperty("token", SessionManager.getInstance().getTokene());
			// rpc.addProperty("chepai", truck);
			// rpc.addProperty("siji", driName);
			// rpc.addProperty("sijidh", driNum);
			// rpc.addProperty("sjsfz", driId);
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER12);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(rpc);
			HttpTransportSE transport = new HttpTransportSE(
					Constant.SERVICE_URL);
			transport.debug = true;
			try {
				transport.call(Constant.NAMESPACE + "YuPeiCheChuKu", envelope);
				SoapObject response = (SoapObject) envelope.bodyIn;
				if (response != null) {
					JSONTokener parser = new JSONTokener(
							response.getPropertyAsString("YuPeiCheChuKuResult"));
					JSONObject json = (JSONObject) parser.nextValue();
					if (json.getBoolean("result")) {
						return true;
					} else {
						errMsg = json.getString("msg");
						return false;
					}
				}
			} catch (Exception e) {
				errMsg = e.toString();
				e.printStackTrace();
			}
			return false;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			progress.dismiss();
			if (result) {
				Toast.makeText(OutActivity.this, "操作成功", Toast.LENGTH_LONG)
						.show();
				// for (int i = 0; i < items.size(); i++) {
				// if (adapter.selected.get(i) == 1) {
				// items.remove(i);
				// }
				// }
				adapter.notifyDataSetChanged();
			} else {
				Toast.makeText(OutActivity.this, "操作失败," + errMsg,
						Toast.LENGTH_LONG).show();
				Log.i("suxoyo", errMsg);
			}
			super.onPostExecute(result);
		}

	}

	// @Override
	// public boolean onCreateOptionsMenu(Menu menu) {
	// // Inflate the menu; this adds items to the action bar if it is present.
	// getMenuInflater().inflate(R.menu.out, menu);
	// return true;
	// }

}
