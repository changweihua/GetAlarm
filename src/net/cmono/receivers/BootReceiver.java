package net.cmono.receivers;

import java.text.SimpleDateFormat;
import java.util.Date;

import net.cmono.consts.ConstValue;
import net.cmono.getalarm.GetAlarmApplication;
import net.cmono.svrs.AutoWordService;
import net.cmono.svrs.NotificationLockScreenMonitor;
import net.cmono.utils.Logger;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class BootReceiver extends BroadcastReceiver {

	static final String action_boot = "android.intent.action.BOOT_COMPLETED";

	public BootReceiver() {
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals(action_boot)) {
			/*
			 * Intent ootStartIntent = new Intent(context, MainActivity.class);
			 * ootStartIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			 * context.startActivity(ootStartIntent);
			 */

			Logger.d(intent.getAction(), "开机启动成功");
			Intent lc = new Intent();
			lc.setClass(context, NotificationLockScreenMonitor.class);
			context.startService(lc);

			SharedPreferences sp = context.getSharedPreferences(
					ConstValue.SETTING_PF, Context.MODE_PRIVATE);
			Boolean autoWord = sp.getBoolean("autoWord", true);
			String wordDate = sp.getString("wordDate", "");
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			String date = format.format(new Date());
			if (GetAlarmApplication.isOnline && autoWord && !wordDate.equals(date)) {
				Intent autoWordService = new Intent();
				autoWordService.setClass(context, AutoWordService.class);
				// 启动Service
				context.startService(autoWordService);

				// Logger.d(intent.getAction(), "启动服务成功");
			}

		} else {
			// Log.i("ddddd====", "" + intent.getAction());
			Logger.d(intent.getAction(), "开机启动失败");
		}

		// throw new UnsupportedOperationException("Not yet implemented");
	}
}
