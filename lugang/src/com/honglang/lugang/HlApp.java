package com.honglang.lugang;

import java.util.LinkedList;
import java.util.List;







import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class HlApp extends Application {

	private List<Activity> activityList = new LinkedList<Activity>();
	private Activity homeActivity;
	public static HlApp instance;
	@Override
	public void onCreate() {
		super.onCreate();
		
	}
	
	public void addActivity(Activity activity) {
		activityList.add(activity);
	}

	public void exit() {
		if (activityList != null) {
			Activity activity;

			for (int i = 0; i < activityList.size(); i++) {
				activity = activityList.get(i);

				if (activity != null) {
					if (!activity.isFinishing()) {
						activity.finish();
					}

					activity = null;
				}

				activityList.remove(i);
				i--;
			}
		}
	}
	
	public Activity getHomeActivity() {
		return homeActivity;
	}

	public void setHomeActivity(Activity homeActivity) {
		this.homeActivity = homeActivity;
	}
	
	public static HlApp getInstance() {
		if (null == instance) {
			instance = new HlApp();
		}
		return instance;
	}
	
	/**
	 * 检测网络是否可用
	 * @return
	 */
	public boolean isNetworkConnected() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		return ni != null && ni.isConnectedOrConnecting();
	}

}
