package com.honglang.lugang.cityexpress;

import com.honglang.lugang.R;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

public class SearchExpressDialog extends Dialog {

	private Context context;
	public SearchExpressDialog(Context context, int theme) {
		super(context, theme);
		this.context = context;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_express);
	}

}
