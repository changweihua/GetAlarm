package net.cmono.receivers;

import net.cmono.getalarm.GetAlarmApplication;
import net.cmono.utils.Logger;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;

public class BatteryInfoReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		String action = intent.getAction();
		Logger.d("Battery Event", action);
		if (Intent.ACTION_BATTERY_CHANGED.equals(action)) {
			onBatteryInfo(intent);
		}
	}

	StringBuffer strBuffer = new StringBuffer(50);
	int level;
	int scale;

	private WakeLock mWakelock;

	// 亮屏
	@SuppressWarnings("deprecation")
	private void LightScreen() {
		PowerManager pm = (PowerManager) GetAlarmApplication.getAppContext()
				.getSystemService(Context.POWER_SERVICE);

		// 屏幕“亮”，表示有两种状态：a、未锁屏 b、目前正处于解锁状态 。这两种状态屏幕都是亮的
		// 屏幕“暗”，表示目前屏幕是黑的
		boolean isScreenOn = pm.isScreenOn();// 如果为true，则表示屏幕“亮”了，否则屏幕“暗”了。

		if (isScreenOn) {
			return;
		}
		mWakelock = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP
				| PowerManager.SCREEN_DIM_WAKE_LOCK, "SimpleTimer");
		mWakelock.acquire();
		mWakelock.release();
	}

	public void onBatteryInfo(Intent intent) {

		// String action = intent.getAction();

		// if (!Intent.ACTION_BATTERY_CHANGED.equals(action) ||
		// !Intent.ACTION_BATTERY_LOW.equals(action))
		// return;

		level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
		scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, 100);

		if (level == scale) {
			LightScreen();
		}

	}

}
