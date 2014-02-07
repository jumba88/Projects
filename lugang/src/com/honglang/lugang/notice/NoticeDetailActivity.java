package com.honglang.lugang.notice;

import com.honglang.lugang.R;
import com.honglang.lugang.R.layout;
import com.honglang.lugang.R.menu;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class NoticeDetailActivity extends Activity implements OnClickListener {

	private Notice data;
	private TextView title;
	private Button back;
	private TextView newstitle;
	private TextView addtime;
	private TextView content;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notice_detail);
		data = (Notice) this.getIntent().getSerializableExtra("Notice");
		init();
	}

	private void init(){
		title = (TextView) this.findViewById(R.id.title);
		title.setText("ͨ新闻公告");
		back = (Button) this.findViewById(R.id.back);
		back.setOnClickListener(this);
		
		newstitle = (TextView) this.findViewById(R.id.newstitle);
		newstitle.setText(data.getTitle());
		addtime = (TextView) this.findViewById(R.id.time);
		addtime.setText(data.getAddtime());
		content = (TextView) this.findViewById(R.id.content);
		content.setText(data.getContent());
	}
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.notice_detail, menu);
//		return true;
//	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			this.finish();
			break;
		}		
	}

}
