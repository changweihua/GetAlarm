package net.cmono.receivers;

import org.litepal.crud.DataSupport;

import net.cmono.dtos.NotificationInfo;
import net.cmono.getalarm.GetAlarmApplication;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;

public class ScreenUnlockReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context arg0, Intent arg1) {

		// TODO Auto-generated method stub

		GetAlarmApplication.screenLocked = false;
//		GetAlarmApplication.NotiMessageList.clear();
		ContentValues values = new ContentValues();
		values.put("status", "1");
		DataSupport.updateAll(NotificationInfo.class, values,
				"status = ?", "0");
	}
}
