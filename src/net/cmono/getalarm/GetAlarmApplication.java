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
		// ...����������strategy�����ԣ���bugly��ʼ��ʱ����

		UserStrategy strategy = new UserStrategy(mContext);

		// strategy.setAppChannel("myChannel"); // ��������
		// strategy.setAppVersion("1.0.1"); // App�İ汾

		// Bugly��������10s������ͬ�����ݡ��������ر����󣬿����޸����ʱ�䡣
		strategy.setAppReportDelay(5000); // ��Ϊ5s

		CrashReport.initCrashReport(mContext, "900009893", false);

		// ��ȷ��λ��ĳ���û����쳣
		CrashReport.setUserId("Weihua Chang"); // ������������쳣��־�û�ID������ Weihua Chang

		AlibabaSDK.asyncInit(this, new InitResultCallback() {
			@Override
			public void onSuccess() {
				// Toast.makeText(MainActivity.this, "��ʼ���ɹ�",
				// Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onFailure(int code, String message) {
				// Toast.makeText(MainActivity.this, "��ʼ���쳣",
				// Toast.LENGTH_SHORT).show();
			}
		});

		screenLocked = false;

		instance = this;
		Thread.setDefaultUncaughtExceptionHandler(restartHandler); // �������ʱ�����߳�
																	// �������������������쳣

	}

	// Exception Auto Restart

	protected static GetAlarmApplication instance;

	// �����������ڲ�������쳣
	private UncaughtExceptionHandler restartHandler = new UncaughtExceptionHandler() {
		public void uncaughtException(Thread thread, Throwable ex) {
			restartApp();// ���������쳣ʱ,����Ӧ��
		}
	};

	public void restartApp() {
		Intent intent = new Intent(instance, MainActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		instance.startActivity(intent);
		android.os.Process.killProcess(android.os.Process.myPid()); // ��������֮ǰ���԰�������ע�������˳����������δ���֮ǰ
	}

	//

	private static boolean session_enter = false; // Ĭ��Ϊfalse

	/**
	 * ����session״̬
	 * 
	 * @param bol
	 *            trueΪ�Ե�¼��falseδ��¼
	 */
	public static void putSession(boolean bol) {
		session_enter = bol;
	}

	/**
	 * �Ƿ��¼
	 * 
	 * @return true�ǣ�false��
	 */
	public static boolean isEnter() {

		return true;
		//return session_enter;
	}

	private static UserModel currentUser;

	// �Ƿ���ͨ��״̬
	public static Boolean callStatus = false;

	// �Ƿ�����Ϣ֪ͨ
	// ��Ļ�Ƿ����
	public static Boolean screenLocked = true;

	// �Ƿ�����
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

	// ������Ϣ
	public static Hashtable<String, NotiMessage> NotiMessages = new Hashtable<String, NotiMessage>();
	public static LinkedList<NotiMessage> NotiMessageList = new LinkedList<NotiMessage>();
}
