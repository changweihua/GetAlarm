package net.cmono.receivers;

import net.cmono.getalarm.AlarmHandlerActivity;
import net.cmono.utils.Logger;
import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmHandlerReceiver extends BroadcastReceiver {

	static final String action_boot = "net.cmono.getalarm.shownoti";

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub

		if (intent.getAction().equals(action_boot)) {
			Logger.d(action_boot, "OOKK");
			KeyguardManager km = (KeyguardManager) context
					.getSystemService(Context.KEYGUARD_SERVICE);
			if (km.inKeyguardRestrictedInputMode()) {
				Intent alarmIntent = new Intent(context,
						AlarmHandlerActivity.class);
				alarmIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(alarmIntent);
			}
		}
	}

}
