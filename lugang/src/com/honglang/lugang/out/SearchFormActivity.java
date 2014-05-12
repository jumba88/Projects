package com.honglang.lugang.out;

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
import com.honglang.lugang.billsearch.SearchActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class SearchFormActivity extends Activity implements OnClickListener {

	private int mYear;
    private int mMonth;
    private int mDay;
    
    static final int START_DIALOG_ID = 0;
    static final int END_DIALOG_ID = 1;
    
	private Button to;
	private Button from;
	private EditText number;
	private Button search;
	private TextView zero;
	
	private ListView mListView;
	private int pageSize;
	private int pageIndex;
	private List<HashMap<String, String>> items;
	private MyAdapter adapter;
	private LayoutInflater inflater;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_form);
		
		final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
		
		to = (Button) findViewById(R.id.to);
		to.setOnClickListener(this);
		from = (Button) findViewById(R.id.from);
		from.setOnClickListener(this);
		number = (EditText) findViewById(R.id.number);
		search = (Button) findViewById(R.id.search);
		search.setOnClickListener(this);
		
		zero = (TextView) findViewById(R.id.zero);
		
		pageSize = 40;
		pageIndex = 1;
		items = new ArrayList<HashMap<String,String>>();
		inflater = this.getLayoutInflater();
		adapter = new MyAdapter();
		mListView = (ListView) findViewById(R.id.list_form);
		if(adapter != null){
			mListView.setAdapter(adapter);
		}
		
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				HashMap<String, String> item = items.get(arg2);
				Intent intent = new Intent(SearchFormActivity.this, PreviewActivity.class);
				intent.putExtra("type", 0);
				intent.putExtra("keycode", item.get("keycode"));
				startActivity(intent);
			}
		});
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case START_DIALOG_ID:
			return new DatePickerDialog(this, mDateSetListener, mYear, mMonth, mDay);
//			break;
		case END_DIALOG_ID:
			return new DatePickerDialog(this, mDateListener, mYear, mMonth, mDay);
		}
		return null;
	}

	@Override
	protected void onPrepareDialog(int id, Dialog dialog) {
		switch (id) {
		case START_DIALOG_ID:
            dialog.setTitle("开始期限");
			((DatePickerDialog) dialog).updateDate(mYear, mMonth, mDay);
            break;
		case END_DIALOG_ID:
			dialog.setTitle("截止期限");
			((DatePickerDialog) dialog).updateDate(mYear, mMonth, mDay);
			break;
		}
	}
	
	private void updateDisplay(){
		from.setText(new StringBuilder().append(mYear).append("-").append(mMonth+1).append("-").append(mDay));
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
	
	private void update(){
		to.setText(new StringBuilder().append(mYear).append("-").append(mMonth+1).append("-").append(mDay));
	}
	private OnDateSetListener mDateListener = new OnDateSetListener() {
		
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			mYear = year;
			mMonth = monthOfYear;
			mDay = dayOfMonth;
			update();
		}
	};
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.search:
			if (items.size() > 0) {
				items.clear();
				adapter.notifyDataSetChanged();
			}else {
				zero.setVisibility(View.GONE);
			}
			new SearchTask().execute((Void)null);
			break;
		case R.id.from:
			showDialog(START_DIALOG_ID);
			break;
		case R.id.to:
			showDialog(END_DIALOG_ID);
			break;
		}
	}
	
	class SearchTask extends AsyncTask<Void, Void, Boolean>{

		private String errMsg;
		private int totalCount;
		@Override
		protected Boolean doInBackground(Void... params) {
			SoapObject rpc = new SoapObject(Constant.NAMESPACE, "ChuKuDanList");
			rpc.addProperty("currentUserno", SessionManager.getInstance().getUsername());
			rpc.addProperty("token", SessionManager.getInstance().getTokene());
			rpc.addProperty("pageSize", pageSize);
			rpc.addProperty("pageIndex", pageIndex);
			rpc.addProperty("keyCode", number.getText()+"");
			rpc.addProperty("startDate", from.getText()+"");
			rpc.addProperty("endDate", to.getText()+"");
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
			if (result) {
				if (items.size() == 0) {
					zero.setVisibility(View.VISIBLE);
				}else {
					adapter.notifyDataSetChanged();
				}
			}
			super.onPostExecute(result);
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

}
