package com.honglang.lugang.issue;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.honglang.lugang.R;
import com.honglang.lugang.R.layout;
import com.honglang.lugang.R.menu;
import com.honglang.lugang.company.CompanyActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class IssueStuffActivity extends Activity implements OnClickListener {

	private TextView title;
	private Button back;
	private Button ok;
	
	private PullToRefreshListView mListView;
	private int pageSize;
	private int pageIndex;
	private int totalCount;
	private ProgressDialog progress;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_issue_stuff);
		
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

		@Override
		protected void onPreExecute() {
			progress = ProgressDialog.show(IssueStuffActivity.this, null, "加载中...", false, false);
			super.onPreExecute();
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			// TODO Auto-generated method stub
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
