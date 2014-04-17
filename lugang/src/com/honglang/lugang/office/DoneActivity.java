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

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.honglang.lugang.Constant;
import com.honglang.lugang.R;
import com.honglang.lugang.SessionManager;
import com.honglang.lugang.R.layout;
import com.honglang.lugang.R.menu;
import com.honglang.lugang.login.LoginActivity;
import com.honglang.lugang.office.DoneFragment.DealingTask;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class DoneActivity extends Activity implements OnClickListener {

	private TextView title;
	private Button back;
	
	private TextView text;
	private TextView time;
	private List<Bill> items;
	private OfficeAdapter adapter;
	private PullToRefreshListView mListView;
	private int pageSize;
	private int pageIndex;
	private int totalCount;
	private String action = "DealtDone";
	private ProgressDialog progress;
	private boolean ISFIRST = true;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_done);
		
		title = (TextView) this.findViewById(R.id.title);
		if (SessionManager.getInstance().getUsertype().equals("物流企业") || SessionManager.getInstance().getUsertype().equals("物流园")) {
			title.setText("已完成工单");
		}else{
			title.setText("已下单信息");
		}
		back = (Button) this.findViewById(R.id.back);
		back.setOnClickListener(this);
		
		text = (TextView) this.findViewById(R.id.txt);
		text.setText("已完成事项 ");
		time = (TextView) this.findViewById(R.id.time);
		time.setText("处理时间");
		
		pageSize = 40;
		pageIndex = 1;
		items = new ArrayList<Bill>();
		new DoneTask().execute((Void)null);
		adapter = new OfficeAdapter(items, this, 1);
		mListView = (PullToRefreshListView) this.findViewById(R.id.list_handling);
		mListView.setMode(Mode.PULL_FROM_END);
		mListView.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				pageIndex++;
				new DoneTask().execute((Void)null);
			}
		});
		
		if(adapter != null){
			mListView.setAdapter(adapter);
		}
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
	
	class DoneTask extends AsyncTask<Void, Void, Boolean>{

		private String errMsg;
		
		@Override
		protected void onPreExecute() {
			if (ISFIRST) {
				progress = ProgressDialog.show(DoneActivity.this, null, "加载中...", false, false);
			}
			super.onPreExecute();
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			SoapObject rpc = new SoapObject(Constant.NAMESPACE, action);
			rpc.addProperty("currentUserNo", SessionManager.getInstance().getUsername());
			rpc.addProperty("token", SessionManager.getInstance().getTokene());
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
					JSONTokener parser = new JSONTokener(response.getPropertyAsString("DealtDoneResult"));
					JSONObject json = (JSONObject) parser.nextValue();
					if (json.getBoolean("result")) {
						JSONObject data = json.getJSONObject("data");
						totalCount = data.getInt("totalrowcount");
						JSONArray rows = data.getJSONArray("rows");
						
						for (int i = 0; i < rows.length(); i++) {
							JSONObject obj = rows.getJSONObject(i);
							Bill item = new Bill();
							item.setTitle(obj.getString("title"));
							item.setTrun_time(obj.getString("trun_time"));
							item.setDone_time(obj.getString("done_time"));
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
			}
			return false;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			progress.dismiss();
			ISFIRST = false;
			if (result) {
				adapter.notifyDataSetChanged();
				
				if (pageIndex > 1) {
					mListView.onRefreshComplete();
				}
				if (totalCount == items.size()) {
					mListView.setMode(Mode.DISABLED);
					Toast.makeText(DoneActivity.this, "已加载完所有数据", Toast.LENGTH_SHORT).show();
				}
			}else {
				Toast.makeText(DoneActivity.this, errMsg, Toast.LENGTH_LONG).show();
				if (errMsg.equals("请先登录")) {
					Intent i = new Intent(DoneActivity.this, LoginActivity.class);
					i.putExtra("dir", 1);
					startActivity(i);
				}
				if (pageIndex > 1) {
					pageIndex--;
				}
				DoneActivity.this.finish();
			}
			super.onPostExecute(result);
		}
		
	}


//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.done, menu);
//		return true;
//	}

}
