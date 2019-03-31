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

	// ����
	@SuppressWarnings("deprecation")
	private void LightScreen() {
		PowerManager pm = (PowerManager) GetAlarmApplication.getAppContext()
				.getSystemService(Context.POWER_SERVICE);

		// ��Ļ����������ʾ������״̬��a��δ���� b��Ŀǰ�����ڽ���״̬ ��������״̬��Ļ��������
		// ��Ļ����������ʾĿǰ��Ļ�Ǻڵ�
		boolean isScreenOn = pm.isScreenOn();// ���Ϊtrue�����ʾ��Ļ�������ˣ�������Ļ�������ˡ�

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
