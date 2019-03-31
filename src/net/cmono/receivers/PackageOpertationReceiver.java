package net.cmono.receivers;

import net.cmono.getalarm.MainActivity;
import net.cmono.getalarm.MainActivity.DataLoadTask;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class PackageOpertationReceiver extends BroadcastReceiver {

	// private ToastUtil toast = ToastUtil.createToastConfig();

	@Override
	public void onReceive(Context context, Intent intent) {

		// 接收广播：系统启动完成后运行程序
		// if
		// (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
		// Intent newIntent = new Intent(context, MainActivity.class);
		// newIntent.setAction("android.intent.action.MAIN");
		// newIntent.addCategory("android.intent.category.LAUNCHER");
		// newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		// context.startActivity(newIntent);
		// }

		// 接收广播：设备上新安装了一个应用程序包后自动启动新安装应用程序
		if (intent.getAction().equals("android.intent.action.PACKAGE_ADDED")) {
			// String packageName = intent.getDataString().substring(8);
			// Logger.d("PackageOpertationReceiver", "成功安装 " + packageName);
			// Toast.makeText(context,
			// "成功安装 " + packageName,
			// Toast.LENGTH_SHORT).show();
			// toast.ToastShow(context, (ViewGroup)(new
			// MainActivity()).findViewById(R.id.toast_layout_root), "成功安装",
			// packageName);

			// Intent newIntent = new Intent();
			// newIntent.setClassName(packageName, packageName +
			// ".MainActivity");
			// newIntent.setAction("android.intent.action.MAIN");
			// newIntent.addCategory("android.intent.category.LAUNCHER");
			// newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			// context.startActivity(newIntent);
		}
		// 接收广播：设备上删除了一个应用程序包。
		if (intent.getAction().equals("android.intent.action.PACKAGE_REMOVED")) {
			// String packageName = intent.getDataString().substring(8);
			// Logger.d("PackageOpertationReceiver", "成功卸载 " + packageName);
			// toast.ToastShow(context, (ViewGroup)(new
			// MainActivity()).findViewById(R.id.toast_layout_root), "成功卸载",
			// packageName);
			// Toast.makeText(context,
			// "成功卸载 " + packageName,
			// Toast.LENGTH_SHORT).show();
			// System.out.println("********************************");
			// DatabaseHelper dbhelper = new DatabaseHelper();
			// dbhelper.executeSql("delete from xxx");
		}
		
	}
}