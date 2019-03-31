package net.cmono.getalarm;

import java.util.List;

import net.cmono.consts.ConstValue;
import net.cmono.receivers.HomeWatcherReceiver;
import net.cmono.utils.Logger;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

public class BaseActivity extends Activity {
	protected BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			finish();
		}
	};
	
	

    
    
	@Override
	public void onResume() {
		super.onResume();
//		Logger.d("GetAlarm BaseActivity", "切换到前台");
		// 在当前的activity中注册广播
		IntentFilter filter = new IntentFilter();
		filter.addAction(ConstValue.EXIT_ACTION);
		this.registerReceiver(this.broadcastReceiver, filter);
//		 
	}

	@Override
	protected void onStop() {
		
		
		
		// TODO Auto-generated method stub
		super.onStop();

		if (!isAppOnForeground()) {
			// app 进入后台
//			Logger.d("GetAlarm BaseActivity", "切换到后台");
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		this.unregisterReceiver(this.broadcastReceiver);
	}

	/**
	 * 程序是否在前台运行
	 * 
	 * @return
	 */
	public boolean isAppOnForeground() {
		// Returns a list of application processes that are running on the
		// device

		ActivityManager activityManager = (ActivityManager) getApplicationContext()
				.getSystemService(Context.ACTIVITY_SERVICE);
		String packageName = getApplicationContext().getPackageName();

		List<RunningAppProcessInfo> appProcesses = activityManager
				.getRunningAppProcesses();
		if (appProcesses == null)
			return false;

		for (RunningAppProcessInfo appProcess : appProcesses) {
			// The name of the process that this object is associated with.
			if (appProcess.processName.equals(packageName)
					&& appProcess.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
				return true;
			}
		}

		return false;
	}
}
