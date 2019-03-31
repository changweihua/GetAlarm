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
		// �õ��Ƿ�û���������ӳɹ�
		boolean isNotConnected = intent.getBooleanExtra(
				ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);

		if (!isNotConnected || judgeNetIsConnected(context)) {
			Toast.makeText(context, "�������ӳɹ���", Toast.LENGTH_LONG).show();
		} else {
			Toast.makeText(context, "�������粻�������������磡", Toast.LENGTH_LONG).show();
		}
	}

	/**
	 * �ж����������Ƿ�ɹ�
	 * 
	 * @param context
	 *            �����Ķ���
	 * @return ���������Ƿ�ɹ�
	 */
	public static boolean judgeNetIsConnected(Context context) {
		// �õ����ӹ���������
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
		if (networkInfo == null) {
			return false;
		}

		return networkInfo.isConnected();
	}

}