package com.honglang.lugang;

import java.util.LinkedList;
import java.util.List;






import android.app.Activity;
import android.app.Application;

public class HlApp extends Application {

	private List<Activity> activityList = new LinkedList<Activity>();
	private Activity homeActivity;
	private Object obj;
	private static HlApp instance;
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

}
