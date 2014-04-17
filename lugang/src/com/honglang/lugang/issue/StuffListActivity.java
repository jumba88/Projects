package com.honglang.lugang.issue;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.honglang.lugang.Constant;
import com.honglang.lugang.R;
import com.honglang.lugang.SessionManager;
import com.honglang.lugang.R.layout;
import com.honglang.lugang.R.menu;
import com.honglang.lugang.company.CompanyActivity;
import com.honglang.lugang.office.Bill;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class StuffListActivity extends Activity implements OnClickListener {

	private TextView title;
	private Button back;
	private Button ok;
	
	private PullToRefreshListView mListView;
	private int pageSize;
	private int pageIndex;
	private int totalCount;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_stuff_list);
		
		init();
	}
	
	private void init(){
		title = (TextView) this.findViewById(R.id.title);
		title.setText("货物发布");
		back = (Button) this.findViewById(R.id.back);
		back.setOnClickListener(this);
		ok = (Button) this.findViewById(R.id.ok);
		ok.setText("新增");
		ok.setVisibility(View.VISIBLE);
		ok.setOnClickListener(this);
		
		pageSize = 40;
		pageIndex = 1;
		
		mListView = (PullToRefreshListView) findViewById(R.id.list_stuff);
		mListView.setMode(Mode.PULL_FROM_END);
		new LoadTask().execute((Void)null);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;
		case R.id.ok:
			break;
		}
	}

	class LoadTask extends AsyncTask<Void, Void, Boolean>{

		private String errMsg;
		@Override
		protected Boolean doInBackground(Void... params) {
			SoapObject rpc = new SoapObject(Constant.NAMESPACE, "MyPhInfo");
			rpc.addProperty("currentUserno", SessionManager.getInstance().getUsername());
			rpc.addProperty("token", SessionManager.getInstance().getTokene());
			rpc.addProperty("pageSize", pageSize);
			rpc.addProperty("pageIndex", pageIndex);
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(rpc);
			HttpTransportSE transport = new HttpTransportSE(Constant.SERVICE_URL);
			transport.debug = true;
			try {
				transport.call(Constant.NAMESPACE + "MyPhInfo", envelope);
				SoapObject response = (SoapObject) envelope.bodyIn;
				if(response != null){
					JSONTokener parser = new JSONTokener(response.getPropertyAsString("MyPhInfoResult"));
					JSONObject json = (JSONObject) parser.nextValue();
					if (json.getBoolean("result")) {
						JSONObject data = json.getJSONObject("data");
						totalCount = data.getInt("totalrowcount");
						JSONArray rows = data.getJSONArray("rows");
						
//						for (int i = 0; i < rows.length(); i++) {
//							JSONObject obj = rows.getJSONObject(i);
//							Bill item = new Bill();
//							item.setTitle(obj.getString("title"));
//							item.setTrun_time(obj.getString("trun_time"));
//							item.setDone_time(obj.getString("done_time"));
//							item.setCurrent_node_id(obj.getString("current_node_id"));
//							item.setForm_oid(obj.getString("form_oid"));
//							items.add(item);
//						}
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
			}
			return false;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
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
