package net.cmono.svrs;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.LitePalApplication;
import org.litepal.crud.DataSupport;

import com.tencent.bugly.crashreport.CrashReport;

import net.cmono.consts.ConstValue;
import net.cmono.dtos.NotiMessage;
import net.cmono.dtos.NotificationInfo;
import net.cmono.getalarm.GetAlarmApplication;
import net.cmono.getalarm.MainActivity;
import net.cmono.getalarm.R;
import net.cmono.providers.Configs;
import net.cmono.utils.FileUtil;
import net.cmono.utils.Logger;
import net.cmono.utils.NotiRuleUtil;
import net.cmono.utils.VibratorUtil;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.text.TextUtils;
import android.widget.RemoteViews;

public class NotificationLockScreenMonitor extends NotificationListenerService {

	// NotificationManager manger = (NotificationManager)
	// getSystemService(Context.NOTIFICATION_SERVICE);
	private SoundPool pool = new SoundPool(16, AudioManager.STREAM_MUSIC, 0);
	private int noti = 0;
	private static final String TAG = "NotificationLockScreenMonitor";
	private static final String TAG_PRE = "["
			+ NotificationLockScreenMonitor.class.getSimpleName() + "] ";
	private static final int EVENT_UPDATE_CURRENT_NOS = 0;
	public static final String ACTION_NLS_CONTROL = "net.cmono.getalarm.NLSCONTROL";

	private NotificationLockScreenMonitor monitor = null;

	// ���ڴ洢��ǰ���е�Notification��StatusBarNotification��������
	public static List<StatusBarNotification[]> mCurrentNotifications = new ArrayList<StatusBarNotification[]>();
	public static int mCurrentNotificationsCounts = 0;
	// �յ���֪ͨ��֪ͨ��StatusBarNotification����ֵ��mPostedNotification
	public static StatusBarNotification mPostedNotification;
	// ɾ��һ��֪ͨ��֪ͨ��StatusBarNotification����ֵ��mRemovedNotification
	public static StatusBarNotification mRemovedNotification;
	private CancelNotificationReceiver mReceiver = new CancelNotificationReceiver();
	// String a;
	private Handler mMonitorHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case EVENT_UPDATE_CURRENT_NOS:
				updateCurrentNotifications();
				break;
			default:
				break;
			}
		}
	};

	class CancelNotificationReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			try {
				String action;
				if (intent != null && intent.getAction() != null
						&& intent.hasExtra("command")) {
					action = intent.getAction();
					if (action.equals(ACTION_NLS_CONTROL)) {
						String command = intent.getStringExtra("command");
						if (TextUtils.equals(command, "cancel_last")) {
							if (mCurrentNotifications != null
									&& mCurrentNotificationsCounts >= 1) {
								// ÿ��ɾ��֪ͨ���һ��
								StatusBarNotification sbnn = getCurrentNotifications()[mCurrentNotificationsCounts - 1];
								cancelNotification(sbnn.getPackageName(),
										sbnn.getTag(), sbnn.getId());
							}
						} else if (TextUtils.equals(command, "cancel_all")) {
							// ɾ������֪ͨ
							cancelAllNotifications();
						}
					}
				}
			} catch (Exception e) {
				// TODO: handle exception
				String info = getErrorInfoFromException(e);
				CrashReport.postCatchedException(e);
			}

		}

	}

	@Override
	public void onCreate() {
		super.onCreate();
		IntentFilter filter = new IntentFilter();
		filter.addAction(ACTION_NLS_CONTROL);
		registerReceiver(mReceiver, filter);
		// ��onCreateʱ��һ�ε���getActiveNotifications()
		mMonitorHandler.sendMessage(mMonitorHandler
				.obtainMessage(EVENT_UPDATE_CURRENT_NOS));

		monitor = NotificationLockScreenMonitor.this;

		noti = pool.load(this, R.raw.titan, 1);

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		unregisterReceiver(mReceiver);
		if (this.mReceiver != null) {
			this.unregisterReceiver(mReceiver);
		}
		// ���ٵ�ʱ���ͷ�SoundPool��Դ
		if (pool != null) {
			pool.release();
			pool = null;
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		// a.equals("b");
		if (intent.getAction().equals("custom_intent")) {
			return null;
		} else {
			return super.onBind(intent);
		}
	}

	private WakeLock mWakelock;

	// �жϵ�ǰϵͳ����״̬
	private boolean isScreenLocked() {

		boolean flag = GetAlarmApplication.screenLocked;
		Logger.d("EnableMsg", Boolean.toString(flag));
		return flag;
	}

	// ����
	@SuppressWarnings("deprecation")
	private void LightScreen() {
		PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);

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

	public static String getErrorInfoFromException(Exception e) {
		try {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			return "\r\n" + sw.toString() + "\r\n";
		} catch (Exception e2) {
			CrashReport.postCatchedException(e2);
			return "bad getErrorInfoFromException";
		}
	}

	private boolean filterMsg(String source, String pattern) {
		Pattern p = Pattern.compile(pattern);
		Matcher m = p.matcher(source);
		return m.matches();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.service.notification.NotificationListenerService#onNotificationPosted
	 * (android.service.notification.StatusBarNotification)
	 * 
	 * ��ϵͳ�յ��µ�֪ͨ������ص�
	 */
	@Override
	public void onNotificationPosted(StatusBarNotification sbn) {
		try {
			// ���ϵͳ���ڽ���״̬��ͨ��״̬���������κβ���
			// ���������������Ϣ
			SharedPreferences sp = GetAlarmApplication.getAppContext()
					.getSharedPreferences(ConstValue.SETTING_PF,
							Context.MODE_PRIVATE);

			// ��ϵͳ�յ��µ�֪ͨ�󣬸���mCurrentNotifications�б�
			// updateCurrentNotifications();
			mPostedNotification = sbn;
			int notificationId = sbn.getId();
			PendingIntent pIntent = sbn.getNotification().contentIntent;
			
			
			// ͨ�����·�ʽ���Ի�ȡNotification����ϸ��Ϣ
			Bundle extras = sbn.getNotification().extras;
			String notificationTitle = extras
					.getString(Notification.EXTRA_TITLE);
			CharSequence tickerText = sbn.getNotification().tickerText;
			Bitmap notificationLargeIcon = ((Bitmap) extras
					.getParcelable(Notification.EXTRA_LARGE_ICON));
			Bitmap notificationSmallIcon = ((Bitmap) extras
					.getParcelable(Notification.EXTRA_SMALL_ICON));
			CharSequence notificationText = extras
					.getCharSequence(Notification.EXTRA_TEXT);
			CharSequence notificationSubText = extras
					.getCharSequence(Notification.EXTRA_SUB_TEXT);
			String pkgName = sbn.getPackageName();
			int notificationIcon = R.drawable.ic_notification;
			if(GetAlarmApplication.packageNames.size() == 0){
				NotiRuleUtil.ReloadNotis();
			}
			
			if (GetAlarmApplication.packageNames.contains(pkgName)) {
				// NotificationManager notificationManager =
				// (NotificationManager)
				// this.getSystemService(android.content.Context.NOTIFICATION_SERVICE);
				// notificationManager.cancel(sbn.getId());

				// Notification notification = new Notification();

				// �Զ������� �����ļ�����ramĿ¼�£�û�д�Ŀ¼�Լ�����һ��
				// notification.sound = Uri.parse("android.resource://"
				// + getPackageName() + "/" + R.raw.noti);

				// ʹ��ϵͳĬ����������������
				// notification.defaults = Notification.DEFAULT_SOUND;
				// manger.notify(1, notification);

				// Notification notification = new Notification();
				// notification.defaults = Notification.DEFAULT_VIBRATE;
				// long[] vibrate = {0,1000,2000,3000};
				// notification.vibrate = vibrate;
				// notification.ledARGB=Color.GREEN;
				// notification.ledOnMS=1000;
				// notification.ledOffMS=1000;
				// notification.flags=Notification.FLAG_SHOW_LIGHTS;

				String icon = "";
				String title = notificationTitle == null ? ""
						: notificationTitle.toString();
				String text = notificationText == null ? "" : notificationText
						.toString();
				String subtext = notificationSubText == null ? ""
						: notificationSubText.toString();
				
				// ��ȡ���춯���Զ�����ͼ
				if (pkgName.equals("com.sds.android.ttpod")) {
					/*RemoteViews remoteViews = sbn.getNotification().contentView;
					if (remoteViews == null) {
						remoteViews = sbn.getNotification().tickerView;
						if (remoteViews == null) {
							remoteViews = sbn.getNotification().bigContentView;
						}
					}

					if (remoteViews != null) {
						title = "��������";
						title = remoteViews.getPackage();
						text = sbn.getNotification().tickerText == null ? "NoUse"
								: sbn.getNotification().tickerText.toString();
					}*/
					title = "������������������";
					text = sbn.getNotification().tickerText == null ? "EmptyValue"
							: sbn.getNotification().tickerText.toString();
					
					if (!text.equals("EmptyValue")) {
						GetAlarmApplication.TTPod_Artist = text.split("-").length > 0 ? text
								.split("-")[0].trim() : "SNH48";
						
						//���� ContentProvider
								ContentResolver resolver = GetAlarmApplication.getAppContext().getContentResolver();  
						Uri uri = Uri.parse("content://"+Configs.TTPODAUTHORTY+"/covers");	
						resolver.update(uri, null, null, null);
					}

				}
				//��ȡϺ������
				if (pkgName.equals("fm.xiami.main")) {
					RemoteViews remoteViews = sbn.getNotification().contentView;
					if (remoteViews == null) {
						remoteViews = sbn.getNotification().tickerView;
						if (remoteViews == null) {
							remoteViews = sbn.getNotification().bigContentView;
						}
					}

					if (remoteViews != null) {
						title = "Ϻ������";
						title = remoteViews.getPackage();
						Logger.d("Title", title);
						Logger.d("Text", text);
						text = sbn.getNotification().tickerText == null ? "NoUse"
								: sbn.getNotification().tickerText.toString();
					}
					
					title = "Ϻ��������������";
					text = sbn.getNotification().tickerText == null ? "EmptyValue"
							: sbn.getNotification().tickerText.toString();
					
					if (!text.equals("EmptyValue")) {
						GetAlarmApplication.TTPod_Artist = text.split("-").length > 0 ? text
								.split("-")[0].trim() : "";
						
						//���� ContentProvider
								ContentResolver resolver = GetAlarmApplication.getAppContext().getContentResolver();  
						Uri uri = Uri.parse("content://"+Configs.XIAMIAUTHORTY+"/covers");	
						resolver.update(uri, null, null, null);
					}

				}
				
				// ����Ϣ���������ļ�
				// �����ʼ�
				if (pkgName.equals("com.smartisan.email")) {

					icon = "ic_lockscreen_smartisan_email.png";
					notificationIcon = R.drawable.ic_lockscreen_smartisan_email;

				} else if (pkgName.equals("com.tencent.mobileqq")
						|| pkgName.equals("com.tencent.qqlite")
						|| pkgName.equals("com.tencent.mobileqqi")
						|| pkgName.equals("com.tencent.mobileqq2")) {// QQ

					icon = "ic_lockscreen_qq.png";
					notificationIcon = R.drawable.ic_lockscreen_qq3;

				} else if (pkgName.equals("com.android.calendar")
						|| pkgName.equals("com.android.providers.calendar")) {

					icon = "ic_lockscreen_calendar.png";
					notificationIcon = R.drawable.ic_lockscreen_calendar;

				} else if (pkgName.equals("com.aliyun.video")
						|| pkgName.equals("com.aliyun.video.youku")) {

					icon = "ic_lockscreen_youtube.png";
					notificationIcon = R.drawable.ic_lockscreen_youtube;

				} else if (pkgName.equals("com.kingsoft.email")) {

					icon = "ic_lockscreen_email.png";
					notificationIcon = R.drawable.ic_lockscreen_email;

				} else if (pkgName.equals("com.alipay.android.app")
						|| pkgName.equals("com.eg.android.AlipayGphone")) {

					icon = "ic_lockscreen_alipay.png";
					notificationIcon = R.drawable.ic_lockscreen_alipay;

				} else if (pkgName.equals("com.tencent.mm")) {

					icon = "ic_lockscreen_wechat.png";
					notificationIcon = R.drawable.ic_lockscreen_wechat;

				} else if (pkgName.equals("com.aliyun.wireless.vos.appstore")) {

					icon = "ic_lockscreen_appstore.png";
					notificationIcon = R.drawable.ic_lockscreen_appstore;

				} else if (pkgName.equals("com.yunos.baseservice.cmns_client")) {

					icon = "ic_lockscreen_baseservice.png";
					notificationIcon = R.drawable.ic_lockscreen_baseservice;

				} else if (pkgName.equals("com.aliyun.SecurityCenter")
						|| pkgName.equals("com.aliyun.SecurityService")) {

					icon = "ic_lockscreen_security.png";
					notificationIcon = R.drawable.ic_lockscreen_security;

				} else if (pkgName.equals("com.baidu.tieba")) {

					icon = "ic_lockscreen_tieba.png";
					notificationIcon = R.drawable.ic_lockscreen_tieba;

				} else if (pkgName.equals("com.tencent.mtt")) {
					if (text == "�������") {
						icon = "ic_lockscreen_download.png";
						notificationIcon = R.drawable.ic_lockscreen_download3;
					}

				} else if (pkgName.equals("com.android.providers.downloads")) {
					title = new String(
							notificationTitle.getBytes("ISO-8859-1"), "utf-8");
					icon = "ic_lockscreen_download.png";
					notificationIcon = R.drawable.ic_lockscreen_download;

				} else if (pkgName.equals("com.coolapk.market")
						|| pkgName.equals("cn.com.shouji.market")
						|| pkgName.equals("com.tencent.android.qqdownloader")) {
					if (title == null || title.length() == 0) {
						title = "��������";
					}
					icon = "ic_lockscreen_download.png";
					notificationIcon = R.drawable.ic_lockscreen_download;

				} else {

					icon = "ic_lockscreen_timeline.png";
					notificationIcon = R.drawable.ic_notification;

				}

				// �������ݿ� Begin
				List<NotificationInfo> list = new ArrayList<NotificationInfo>();

				int id = DataSupport.max(NotificationInfo.class, "id",
						int.class) + 1;
				Calendar dob = Calendar.getInstance();

				NotificationInfo notificationInfo = new NotificationInfo(id,
						title, text, subtext, icon, 0, dob.getTime(), pkgName);
				list.add(notificationInfo);
				// ����
				DataSupport.saveAll(list);

				// �������ݿ� End

				// NotificationManager notificationManager =
				// (NotificationManager)
				// GetAlarmApplication.getAppContext().getSystemService(NOTIFICATION_SERVICE);
				// notificationManager.cancel(notificationId);

				if (title.length() == 0 && text.length() == 0
						&& subtext.length() == 0) {
					return;
				}

				if(GetAlarmApplication.iGnorePkgList.size() == 0||GetAlarmApplication.iGnoreTitleList.size() == 0||GetAlarmApplication.iGnoreTextList.size() == 0){
					NotiRuleUtil.ReloadiGnores();
				}
				// �����Ϣ��������������ʾ������
				for (int i = 0; i < GetAlarmApplication.iGnorePkgList.size(); i++) {
					if (GetAlarmApplication.iGnorePkgList.get(i)
							.equals(pkgName)) {
						if ((GetAlarmApplication.iGnoreTitleList.get(i)
								.length() > 0 && title
								.contains(GetAlarmApplication.iGnoreTitleList
										.get(i)))
								|| (GetAlarmApplication.iGnoreTextList.get(i)
										.length() > 0 && text
										.contains(GetAlarmApplication.iGnoreTextList
												.get(i)))) {
							return;
						}
					}
				}

				boolean showMsg = sp.getBoolean("showMsg", true);

				if (showMsg) {
					if (!isScreenLocked()) {
						Intent i = new Intent("net.cmono.getalarm.headnoti");
						// ������
						i.putExtra("id", notificationId);
						i.putExtra("icon", notificationIcon);
						i.putExtra("title", title);
						i.putExtra("text", text);
						i.putExtra("pkg", pkgName);
						i.putExtra("PI", pIntent);

						/*
						 * ���߰󶨳�һ������ Bundle data = new Bundle();
						 * data.putString("username1",username);
						 * data.putString("userpwd1",userpwd);
						 * intent.putExtras(data);
						 */
						sendBroadcast(i);

						// Intent i2 = new Intent(ACTION_NLS_CONTROL);
						// i.putExtra("command", "cancel_last");
						// sendBroadcast(i2);
					} else {

						// �浽ȫ�ֱ���
						NotiMessage message = new NotiMessage();
						message.setPkgName(pkgName);
						message.setNotificationInfo(notificationInfo);
						// GetAlarmApplication.NotiMessages.put(pkgName,
						// message);
						boolean addFlag = true;
						for (int i = 0; i < GetAlarmApplication.NotiMessageList
								.size(); i++) {
							NotiMessage msg = GetAlarmApplication.NotiMessageList
									.get(i);
							if (msg.getPkgName().equals(pkgName)) {
								msg.setNotificationInfo(notificationInfo);
								addFlag = false;
								break;
							}
						}
						if (addFlag) {
							if (GetAlarmApplication.NotiMessageList.size() >= 2) {
								GetAlarmApplication.NotiMessageList.pollLast();
							}
							GetAlarmApplication.NotiMessageList
									.offerFirst(message);
						}

						Intent i = new Intent("net.cmono.getalarm.noti");
						sendBroadcast(i);
						LightScreen();
						if (pool != null) {
							AudioManager mgr = (AudioManager) GetAlarmApplication
									.getAppContext().getSystemService(
											Context.AUDIO_SERVICE);
							float streamVolumeCurrent = mgr
									.getStreamVolume(AudioManager.STREAM_NOTIFICATION);
							float streamVolumeMax = mgr
									.getStreamMaxVolume(AudioManager.STREAM_NOTIFICATION);
							float volume = streamVolumeCurrent
									/ streamVolumeMax;
							pool.play(noti, volume, volume, 1, 0, 1.0f);
						}
						// ��
						// long[] vibrates = new long[] { 0, 1000, 2000, 1000 };
						// VibratorHelper.Vibrate(GetAlarmApplication.getAppContext(),vibrates,
						// false);
						VibratorUtil.Vibrate(
								GetAlarmApplication.getAppContext(), 300);
					}
				}
			}

		} catch (Exception e) {
			// TODO: handle exception
			// String info = getErrorInfoFromException(e);
			CrashReport.postCatchedException(e);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.service.notification.NotificationListenerService#
	 * onNotificationRemoved(android.service.notification.StatusBarNotification)
	 * 
	 * ��ϵͳ֪ͨ��ɾ��������ص�
	 */
	@Override
	public void onNotificationRemoved(StatusBarNotification sbn) {
		// ����֪ͨ��ɾ���󣬸���mCurrentNotifications�б�
		updateCurrentNotifications();
		String pkgName = sbn.getPackageName();
		if (GetAlarmApplication.packageNames.contains(pkgName)) {

		}
		GetAlarmApplication.NotiMessageList.clear();
		mRemovedNotification = sbn;
	}

	private void updateCurrentNotifications() {
		try {
			StatusBarNotification[] activeNos = monitor
					.getActiveNotifications();
			if (mCurrentNotifications.size() == 0) {
				mCurrentNotifications.add(null);
			}
			mCurrentNotifications.set(0, activeNos);
			mCurrentNotificationsCounts = activeNos.length;
		} catch (Exception e) {
			// String info = getErrorInfoFromException(e);
			CrashReport.postCatchedException(e);
		}
	}

	// ��ȡ��ǰ״̬����ʾ֪ͨ����
	public static StatusBarNotification[] getCurrentNotifications() {
		if (mCurrentNotifications.size() == 0) {
			return null;
		}
		return mCurrentNotifications.get(0);
	}

	private static void logNLS(Object object) {
		Logger.d(TAG, TAG_PRE + object);
	}

	 @Override
	 public int onStartCommand(Intent intent, int flags, int startId) {
	 // TODO Auto-generated method stub
	 return START_STICKY;
	 }

	// @Override
	// public int onStartCommand(Intent intent, int flags, int startId) {
	// // TODO Auto-generated method stub
	// Log.v("TrafficService","startCommand");
	//
	// flags = START_STICKY;
	// return super.onStartCommand(intent, flags, startId);
	// // return START_REDELIVER_INTENT;
	// }

}