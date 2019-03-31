package net.cmono.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

public class NetworkChangedReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// 得到是否没有网络连接成功
		boolean isNotConnected = intent.getBooleanExtra(
				ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);

		if (!isNotConnected || judgeNetIsConnected(context)) {
			Toast.makeText(context, "网络连接成功！", Toast.LENGTH_LONG).show();
		} else {
			Toast.makeText(context, "您的网络不给力，请检查网络！", Toast.LENGTH_LONG).show();
		}
	}

	/**
	 * 判断网络连接是否成功
	 * 
	 * @param context
	 *            上下文对象
	 * @return 网络连接是否成功
	 */
	public static boolean judgeNetIsConnected(Context context) {
		// 得到连接管理器对象
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
		if (networkInfo == null) {
			return false;
		}

		return networkInfo.isConnected();
	}

}