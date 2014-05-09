package com.honglang.lugang.out;

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
import com.honglang.lugang.office.Bill;
import com.honglang.lugang.office.DealingActivity;
import com.honglang.lugang.office.DealingActivity.DealingTask;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class OutListActivity extends Activity implements OnClickListener {

	private TextView title;
	private Button back;
	
	private int pageSize;
	private int pageIndex;
	
	private boolean isFirst = true;
	
	private ProgressDialog progress;
	private MyAdapter adapter;
	
	private PullToRefreshListView mListView;
	private List<HashMap<String, String>> items;
	private LayoutInflater inflater;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_out_list);
		
		init();
	}

	private void init(){
		title = (TextView) this.findViewById(R.id.title);
		title.setText("出库单查询 ");
		back = (Button) this.findViewById(R.id.back);
		back.setOnClickListener(this);
		
		pageSize = 40;
		pageIndex = 1;
		
		items = new ArrayList<HashMap<String,String>>();
		inflater = this.getLayoutInflater();
		mListView = (PullToRefreshListView) this.findViewById(R.id.list);
		mListView.setMode(Mode.PULL_FROM_END);
		mListView.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				pageIndex++;
				new LoadTask().execute((Void)null);
			}
		});
		
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				HashMap<String, String> item = items.get(arg2-1);
				Intent intent = new Intent(OutListActivity.this, PreviewActivity.class);
				intent.putExtra("type", 0);
				intent.putExtra("keycode", item.get("keycode"));
				startActivity(intent);
			}
		});
		
		new LoadTask().execute((Void)null);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;
		}
	}

	class MyAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return items.size();
		}

		@Override
		public Object getItem(int position) {
			return items.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if(convertView == null){
				convertView = inflater.inflate(R.layout.out_list_item, null);
			}
			HashMap<String, String> item = items.get(position);
			if(item == null){
				return null;
			}
			
			TextView code = (TextView) convertView.findViewById(R.id.code);
			TextView time = (TextView) convertView.findViewById(R.id.time);
			
			code.setText(item.get("keycode"));
			time.setText(item.get("adddate"));
			
			return convertView;
		}
		
	}
	
	class LoadTask extends AsyncTask<Void, Void, Boolean>{

		private String errMsg;
		private int totalCount;
		@Override
		protected void onPreExecute() {
			if (isFirst) {
				progress = ProgressDialog.show(OutListActivity.this, null, "加载中...", false, false);
			}
			super.onPreExecute();
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			SoapObject rpc = new SoapObject(Constant.NAMESPACE, "ChuKuDanList");
			rpc.addProperty("currentUserno", SessionManager.getInstance().getUsername());
			rpc.addProperty("token", SessionManager.getInstance().getTokene());
			rpc.addProperty("pageSize", pageSize);
			rpc.addProperty("pageIndex", pageIndex);
			rpc.addProperty("keyCode", "");
			rpc.addProperty("startDate", "");
			rpc.addProperty("endDate", "");
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(rpc);
			HttpTransportSE transport = new HttpTransportSE(Constant.SERVICE_URL);
			transport.debug = true;
			try {
				transport.call(Constant.NAMESPACE + "ChuKuDanList", envelope);
				SoapObject response = (SoapObject) envelope.bodyIn;
				if(response != null){
					JSONTokener parser = new JSONTokener(response.getPropertyAsString("ChuKuDanListResult"));
					JSONObject json = (JSONObject) parser.nextValue();
					if (json.getBoolean("result")) {
						
						JSONObject data = json.getJSONObject("data");
						totalCount = data.getInt("totalrowcount");
						JSONArray rows = data.getJSONArray("rows");
						for (int i = 0; i < rows.length(); i++) {
							JSONObject obj = rows.getJSONObject(i);
							HashMap<String, String> item = new HashMap<String, String>();
							item.put("keycode", obj.getString("keycode"));
							item.put("adddate", obj.getString("adddate"));
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
			if (result) {
				if (isFirst) {
					adapter = new MyAdapter();
					mListView.setAdapter(adapter);
				} else {
					adapter.notifyDataSetChanged();
				}
				
				isFirst = false;
				
				if (pageIndex > 1) {
					mListView.onRefreshComplete();
				}
				if (totalCount == items.size()) {
					mListView.setMode(Mode.DISABLED);
					Toast.makeText(OutListActivity.this, "已加载完所有数据", Toast.LENGTH_SHORT).show();
				}
			} else {
				Toast.makeText(OutListActivity.this, errMsg, Toast.LENGTH_LONG).show();
				if (errMsg.equals("请先登录")) {
					Intent intent = new Intent(OutListActivity.this, LoginActivity.class);
					intent.putExtra("dir", 1);
					OutListActivity.this.startActivity(intent);
				}
				OutListActivity.this.finish();
			}
			super.onPostExecute(result);
		}
		
	}

}
