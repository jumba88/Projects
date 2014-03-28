package com.honglang.lugang.office;

import java.util.ArrayList;
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
import com.honglang.lugang.login.LoginActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

public class DealingActivity extends Activity implements OnClickListener,OnScrollListener {

	private TextView title;
	private Button back;
	
	private List<Bill> items;
	private OfficeAdapter adapter;
	private ListView mListView;
	private View footerView;
	private ProgressBar pb;
	
	private int pageSize;
	private int pageIndex;
	private String action = "Dealing";
	private String currentUserNo;
	private String token;
	
	private int curCount;
	private int totalCount;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dealing);
		
		title = (TextView) this.findViewById(R.id.title);
		if (SessionManager.getInstance().getUsertype().equals("物流企业") || SessionManager.getInstance().getUsertype().equals("物流园")) {
			title.setText("待处理工单");
		}else{
			title.setText("待处理事项 ");
		}
		back = (Button) this.findViewById(R.id.back);
		back.setOnClickListener(this);
		
		pageSize = 40;
		pageIndex = 1;
		currentUserNo = SessionManager.getInstance().getUsername();
		token = SessionManager.getInstance().getTokene();
		
		items = new ArrayList<Bill>();
		new DealingTask().execute((Void)null);
		adapter = new OfficeAdapter(items, this, 0);
		mListView = (ListView) this.findViewById(R.id.list_handling);
		footerView = this.getLayoutInflater().inflate(R.layout.footer, null);
		pb = (ProgressBar) footerView.findViewById(R.id.pb);
		mListView.addFooterView(footerView);
		if(adapter != null){
			mListView.setAdapter(adapter);
		}
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Bill bill = items.get(arg2);
				if (bill.getCurrent_node_id().equals("7") || bill.getCurrent_node_id().equals("9")) {
					Intent intent = new Intent(DealingActivity.this, OrderActivity.class);
					intent.putExtra("bill", bill);
					startActivityForResult(intent, 111);
				}
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 111) {
			if (resultCode == RESULT_OK) {
				new DealingTask().execute((Void)null);
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

		default:
			break;
		}
		
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		
	}
	
	public class DealingTask extends AsyncTask<Void, Void, Boolean>{

		private String errMsg;
		@Override
		protected Boolean doInBackground(Void... params) {
			SoapObject rpc = new SoapObject(Constant.NAMESPACE, action);
			rpc.addProperty("currentUserNo", currentUserNo);
			rpc.addProperty("token", token);
			rpc.addProperty("pageSize", pageSize);
			rpc.addProperty("pageIndex", pageIndex);
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(rpc);
			HttpTransportSE transport = new HttpTransportSE(Constant.SERVICE_URL);
			transport.debug = true;
			try {
				transport.call(Constant.NAMESPACE + action, envelope);
				SoapObject response = (SoapObject) envelope.bodyIn;
				if(response != null){
					JSONTokener parser = new JSONTokener(response.getPropertyAsString("DealingResult"));
					JSONObject json = (JSONObject) parser.nextValue();
					Log.i("suxoyo", json.toString());
					if (json.getBoolean("result")) {
						items.clear();
						
						JSONObject data = json.getJSONObject("data");
						curCount = data.getInt("currentrowcount");
						totalCount = data.getInt("totalrowcount");
						JSONArray rows = data.getJSONArray("rows");
						for (int i = 0; i < rows.length(); i++) {
							JSONObject obj = rows.getJSONObject(i);
							Bill item = new Bill();
							item.setTitle(obj.getString("title"));
							item.setTrun_time(obj.getString("trun_time"));
							item.setCurrent_node_id(obj.getString("current_node_id"));
							item.setForm_oid(obj.getString("form_oid"));
							items.add(item);
						}
						return true;
					} else {
						errMsg = json.getString("msg");
						return false;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
//				errMsg = e.toString();
				errMsg = "操作失败，请稍候重试";
				return false;
			}
			return false;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			if (result) {
				adapter.notifyDataSetChanged();
				pb.setVisibility(View.GONE);
			}else{
				Toast.makeText(DealingActivity.this, errMsg, Toast.LENGTH_LONG).show();
				if (errMsg.equals("请先登录")) {
					Intent intent = new Intent(DealingActivity.this, LoginActivity.class);
					intent.putExtra("dir", 1);
					DealingActivity.this.startActivity(intent);
				}
				DealingActivity.this.finish();
			}
			super.onPostExecute(result);
		}
		
	}


//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.dealing, menu);
//		return true;
//	}

}
