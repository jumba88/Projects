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
import com.honglang.lugang.truck.Truck;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class HandlingFragment extends Fragment {

	private List<Bill> items;
	private OfficeAdapter adapter;
	private ListView mListView;
	private int pageSize;
	private int pageIndex;
	private String action = "Dealing";
	private String currentUserNo;
	private String token;
	
	DealingTask task;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.handling_content, null);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		pageSize = 40;
		pageIndex = 1;
		currentUserNo = SessionManager.getInstance().getUsername();
		token = SessionManager.getInstance().getTokene();
		items = new ArrayList<Bill>();
//		new DealingTask().execute((Void)null);
		adapter = new OfficeAdapter(items, getActivity(), 0);
		mListView = (ListView) view.findViewById(R.id.list_handling);
		if(adapter != null){
			mListView.setAdapter(adapter);
		}
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Bill bill = items.get(arg2);
				if (bill.getCurrent_node_id().equals("7") || bill.getCurrent_node_id().equals("9")) {
					Intent intent = new Intent(getActivity(), OrderActivity.class);
					intent.putExtra("bill", bill);
					getActivity().startActivity(intent);
				}
			}
		});
	}
	
	@Override
	public void onResume() {
		if (task == null) {
			task = new DealingTask();
			task.execute((Void)null);
		}else {
			task = null;
			task = new DealingTask();
			task.execute((Void)null);
		}
//		new DealingTask().execute((Void)null);
		Log.i("suxoyo", "onResume");
		super.onResume();
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
				errMsg = e.toString();
				return false;
			}
			return false;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			if (result) {
				adapter.notifyDataSetChanged();
			}
			super.onPostExecute(result);
		}
		
	}

}
