package net.cmono.getalarm;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

import net.cmono.dtos.AppInfo;
import net.cmono.dtos.AppJsonInfo;
import net.cmono.dtos.NotiMessage;
import net.cmono.dtos.UserModel;

import java.lang.Thread.UncaughtExceptionHandler;

import org.litepal.LitePalApplication;

import com.alibaba.sdk.android.AlibabaSDK;
import com.alibaba.sdk.android.callback.InitResultCallback;
import com.tencent.bugly.crashreport.CrashReport;
import com.tencent.bugly.crashreport.CrashReport.UserStrategy;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.util.SparseArray;

public class GetAlarmApplication extends LitePalApplication {

	private static Context mContext;

	@Override
	public void onCreate() {
		super.onCreate();
		mContext = getApplicationContext();
		// CrashHandler crashHandler = CrashHandler.getInstance();
		// crashHandler.init(getApplicationContext());
		// ...在这里设置strategy的属性，在bugly初始化时传入

		UserStrategy strategy = new UserStrategy(mContext);

		// strategy.setAppChannel("myChannel"); // 设置渠道
		// strategy.setAppVersion("1.0.1"); // App的版本

		// Bugly会在启动10s后联网同步数据。若您有特别需求，可以修改这个时间。
		strategy.setAppReportDelay(5000); // 改为5s

		CrashReport.initCrashReport(mContext, "900009893", false);

		// 精确定位到某个用户的异常
		CrashReport.setUserId("Weihua Chang"); // 本次启动后的异常日志用户ID都将是 Weihua Chang

		AlibabaSDK.asyncInit(this, new InitResultCallback() {
			@Override
			public void onSuccess() {
				// Toast.makeText(MainActivity.this, "初始化成功",
				// Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onFailure(int code, String message) {
				// Toast.makeText(MainActivity.this, "初始化异常",
				// Toast.LENGTH_SHORT).show();
			}
		});

		screenLocked = false;

		instance = this;
		Thread.setDefaultUncaughtExceptionHandler(restartHandler); // 程序崩溃时触发线程
																	// 以下用来捕获程序崩溃异常

	}

	// Exception Auto Restart

	protected static GetAlarmApplication instance;

	// 创建服务用于捕获崩溃异常
	private UncaughtExceptionHandler restartHandler = new UncaughtExceptionHandler() {
		public void uncaughtException(Thread thread, Throwable ex) {
			restartApp();// 发生崩溃异常时,重启应用
		}
	};

	public void restartApp() {
		Intent intent = new Intent(instance, MainActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		instance.startActivity(intent);
		android.os.Process.killProcess(android.os.Process.myPid()); // 结束进程之前可以把你程序的注销或者退出代码放在这段代码之前
	}

	//

	private static boolean session_enter = false; // 默认为false

	/**
	 * 设置session状态
	 * 
	 * @param bol
	 *            true为以登录，false未登录
	 */
	public static void putSession(boolean bol) {
		session_enter = bol;
	}

	/**
	 * 是否登录
	 * 
	 * @return true是，false否
	 */
	public static boolean isEnter() {

		return true;
		//return session_enter;
	}

	private static UserModel currentUser;

	// 是否处于通话状态
	public static Boolean callStatus = false;

	// 是否开启消息通知
	// 屏幕是否解锁
	public static Boolean screenLocked = true;

	// 是否在线
	public static Boolean isOnline = true;

	public static UserModel getCurrentUser() {
		return currentUser;
	}

	public static ArrayList<String> packageNames = new ArrayList<String>();
	public static ArrayList<String> iGnoreTitleList = new ArrayList<String>();
	public static ArrayList<String> iGnoreTextList = new ArrayList<String>();
	public static ArrayList<String> iGnorePkgList = new ArrayList<String>();
	public static List<AppInfo> AppInfos = new ArrayList<AppInfo>();
	public static ArrayList<AppJsonInfo> appJsonInfoList = new ArrayList<AppJsonInfo>();

	public static SparseArray<String> iNotiIdList = new SparseArray<String>();

	public static String TTPod_Artist = "SNH48";

	public static void setCurrentUser(UserModel currUser) {
		currentUser = currUser;
	}

	public static Context getAppContext() {
		return mContext;
	}

	public static Context getContextObject() {
		return mContext;
	}

	public static Resources getAppResources() {
		return getAppResources();
	}

	// 锁屏消息
	public static Hashtable<String, NotiMessage> NotiMessages = new Hashtable<String, NotiMessage>();
	public static LinkedList<NotiMessage> NotiMessageList = new LinkedList<NotiMessage>();
}
