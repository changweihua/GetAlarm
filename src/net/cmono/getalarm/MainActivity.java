package net.cmono.getalarm;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import net.cmono.adapters.BorderedGridAdapter;
import net.cmono.adapters.CardArrayAdapter;
import net.cmono.adapters.IconAdapter;
import net.cmono.consts.ConstValue;
import net.cmono.dialogs.AppSettingDialog;
import net.cmono.dialogs.IncallSettingDialog;
import net.cmono.dialogs.NotificationSettingDialog;
import net.cmono.dialogs.SpecialDayDialog;
import net.cmono.dtos.AppInfo;
import net.cmono.dtos.ArtistFilenameFilter;
import net.cmono.dtos.Card;
import net.cmono.dtos.Icon;
import net.cmono.dtos.NotificationInfo;
import net.cmono.explosionfield.ExplosionField;
import net.cmono.exts.BorderedGridView;
import net.cmono.exts.BottomPopView;
import net.cmono.exts.LazyFragment;
import net.cmono.obvs.ScreenObserver;
import net.cmono.receivers.HomeWatcherReceiver;
import net.cmono.receivers.NetworkChangedReceiver;
import net.cmono.svrs.AutoWordService;
import net.cmono.utils.AMapUtil;
import net.cmono.utils.APPGCUtil;
import net.cmono.utils.D;
import net.cmono.utils.FileUtil;
import net.cmono.utils.ImageUtil;
import net.cmono.utils.Logger;
import net.cmono.utils.NotiRuleUtil;
import net.cmono.utils.PackageDetailUtil;
import net.cmono.utils.PathUtil;
import net.cmono.utils.SystemBarTintManager;
import net.cmono.utils.TelephonyStatusUtil;
import net.cmono.utils.UpdateUtil;
import net.cmono.utils.WifiStatusUtil;
import net.cmono.wheelview.LoopListener;
import net.cmono.wheelview.LoopView;
import net.steamcrafted.loadtoast.LoadToast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.LitePalApplication;
import org.litepal.crud.DataSupport;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.app.ProgressDialog;
import android.app.WallpaperManager;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.CalendarView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.network.connectionclass.ConnectionQuality;
import com.facebook.network.connectionclass.ConnectionClassManager;
import com.flyco.animation.BaseAnimatorSet;
import com.flyco.animation.BounceEnter.BounceTopEnter;
import com.flyco.animation.SlideExit.SlideBottomExit;
import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.ActionSheetDialog;
import com.flyco.dialog.widget.MaterialDialog;
import com.flyco.dialog.widget.NormalDialog;
import com.google.gson.Gson;
import com.mingle.headsUp.HeadsUp;
import com.mingle.headsUp.HeadsUpManager;
import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;
import com.tencent.bugly.crashreport.CrashReport;
import com.yunos.sdk.account.SsoClient;

public class MainActivity extends BaseActivity {

	@TargetApi(19)
	protected void setTranslucentStatus(boolean on) {
		Window win = getWindow();
		WindowManager.LayoutParams winParams = win.getAttributes();
		final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
		if (on) {
			winParams.flags |= bits;
		} else {
			winParams.flags &= ~bits;
		}
		win.setAttributes(winParams);
	}

	/** 沉浸式状态栏管理类 **/
	protected SystemBarTintManager mTintManager;// 生命一个变量

	// Fragments
	protected PlaceholderFragment mainFragment = new PlaceholderFragment();
	protected AboutPlaceholderFragment aboutFragment = new AboutPlaceholderFragment();
	protected ConfigPlaceholderFragment configFragement = new ConfigPlaceholderFragment();
	protected BasicSettingPreferenceFragment basicSettingPrefFragment = new BasicSettingPreferenceFragment();
	protected IncallSettingPreferenceFragment incallSettingPreferenceFragment = new IncallSettingPreferenceFragment();
	protected RunningStatusFragment runningStatusFragment = new RunningStatusFragment();
	protected ActionPlaceholderFragment actionPlaceholderFragment = new ActionPlaceholderFragment();
	protected RemoteHelpFragment remoteHelpFragment = new RemoteHelpFragment();

	protected NetworkChangedReceiver networkChangedReceiver;// = new
															// NetworkChangedReceiver();

	// 当前显示的 Fragment
	protected Fragment mContent;

	// 返回按钮
	protected ImageButton backButton;
	// 返回按钮
	protected TextView tvBack;
	// 当前 Fragment 名称
	protected TextView tvActionName;
	// 应用名
	protected TextView tvAppName;

	protected LinearLayout llGrid;// = (LinearLayout)findViewById(
									// R.id.ll_grid);

//	private AMapLocationClient mlocationClient = null;
//	private AMapLocationClientOption mLocationOption = null;

	private AlarmManager alarmManager = (AlarmManager) GetAlarmApplication
			.getAppContext().getSystemService(Context.ALARM_SERVICE);;

	// 返回按钮点击事件
	public void btnBackClick(View v) {
		switchContent(mContent, mainFragment);
	}

	private static final String ENABLED_NOTIFICATION_LISTENERS = "enabled_notification_listeners";
	private static final String ACTION_NOTIFICATION_LISTENER_SETTINGS = "android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS";

	private boolean isEnabled() {
		String pkgName = getPackageName();
		final String flat = Settings.Secure.getString(getContentResolver(),
				ENABLED_NOTIFICATION_LISTENERS);
		if (!TextUtils.isEmpty(flat)) {
			final String[] names = flat.split(":");
			for (int i = 0; i < names.length; i++) {
				final ComponentName cn = ComponentName
						.unflattenFromString(names[i]);
				if (cn != null) {
					if (TextUtils.equals(pkgName, cn.getPackageName())) {
						return true;
					}
				}
			}
		}
		return false;
	}

	private void openNotificationAccess() {
		startActivity(new Intent(ACTION_NOTIFICATION_LISTENER_SETTINGS));
	}

	// 爆炸区域
	private ExplosionField mExplosionField;

	protected D d = null;

	// 给需要爆炸的视图添加到爆炸区域中
	private void addListener(View root) {

		// 如果是view group 类型 就把它的子视图添加到区域中
		if (root instanceof ViewGroup) {
			ViewGroup parent = (ViewGroup) root;
			for (int i = 0; i < parent.getChildCount(); i++) {
				addListener(parent.getChildAt(i));
			}
		}

		// 这里是View 类型的视图
		else {

			// 设置它为可点击的
			root.setClickable(true);

			// 添加监听器
			root.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {

					// 爆炸该视图
					mExplosionField.explode(v);
					// 取消注册其点击事件
					v.setOnClickListener(null);
				}
			});
		}
	}

	// 对话框动画
	private BaseAnimatorSet bas_in;
	private BaseAnimatorSet bas_out;

	private ScreenObserver mScreenObserver;

	/**
	 * YUNOS 配置
	 */

	private static final int AUTH_REQUEST_CODE = 5;
	private SsoClient ssoClient = null;

	String LOGOUT_ACTION = "com.aliyun.xiaoyunmi.action.DELETE_ACCOUNT";
	private final BroadcastReceiver logoutReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context ctx, Intent intent) {
			String action = intent.getAction();
			if (LOGOUT_ACTION.equals(action)) {
				// Logger.d("LOGOUT", "user logout");
				// 由于yunos是单账号系统，收到此广播，就可以把授权登录的账号退出了
			}
		}
	};

	/**
	 * YUNOS 配置
	 */

	class GetUserTask extends AsyncTask<String, Integer, String> {
		private Context context;

		GetUserTask(Context context) {
			this.context = context;
		}

		/**
		 * 运行在UI线程中，在调用doInBackground()之前执行
		 */
		@Override
		protected void onPreExecute() {
			Toast.makeText(context, "开始执行", Toast.LENGTH_SHORT).show();
		}

		/**
		 * 后台运行的方法，可以运行非UI线程，可以执行耗时的方法
		 */
		@Override
		protected String doInBackground(String... params) {

			String authcode = params[0];
			// rediret地址要跟入住open.yunos.com时的域名要保持一致

			HttpPost httpPost = new HttpPost(ConstValue.YUNOS_TOKEN_URL);
			List<NameValuePair> props = new ArrayList<NameValuePair>();
			props.add(new BasicNameValuePair("grant_type",
					ConstValue.YUNOS_GRANT_TYPE));
			props.add(new BasicNameValuePair("code", authcode));
			props.add(new BasicNameValuePair("client_id",
					ConstValue.YUNOS_APPKEY));
			props.add(new BasicNameValuePair("client_secret",
					ConstValue.YUNOS_SECRET));
			props.add(new BasicNameValuePair("redirect_uri",
					ConstValue.YUNOS_REDIRECT));
			try {
				httpPost.setEntity(new UrlEncodedFormEntity(props, HTTP.UTF_8));
				HttpResponse httpResponse = null;
				try {
					httpResponse = new DefaultHttpClient().execute(httpPost);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (httpResponse.getStatusLine().getStatusCode() == 200) {
					String result = null;
					try {
						// 创建一个JSON对象
						JSONObject jsonObject = new JSONObject(
								EntityUtils.toString(httpResponse.getEntity()));
						result = URLDecoder.decode(
								jsonObject.getString("taobao_user_nick"),
								"UTF-8");
						return result;
					} catch (ParseException | IOException | JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return null;
		}

		/**
		 * 运行在ui线程中，在doInBackground()执行完毕后执行
		 */
		@Override
		protected void onPostExecute(String result) {

			d.ShowMaterialSimpleConfirmDialog("欢迎你，" + result);
		}

		/**
		 * 在publishProgress()被调用以后执行，publishProgress()用于更新进度
		 */
		@Override
		protected void onProgressUpdate(Integer... values) {
		}
	}

	private Bitmap getImageFromAssetsFile(String fileName) {
		Bitmap image = null;
		AssetManager am = getResources().getAssets();
		try {
			InputStream is = am.open(fileName);
			image = BitmapFactory.decodeStream(is);
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return image;

	}

//	Handler mLocatingHandler = new Handler() {
//		@Override
//		public void dispatchMessage(android.os.Message msg) {
//			switch (msg.what) {
//			case AMapUtil.MSG_LOCATION_START:
//				break;
//			// 定位完成
//			case AMapUtil.MSG_LOCATION_FINISH:
//				AMapLocation loc = (AMapLocation) msg.obj;
//				String result = AMapUtil.getLocationStr(loc);
//				mlocationClient.stopLocation();// 停止定位
//				break;
//			case AMapUtil.MSG_LOCATION_STOP:
//				break;
//			default:
//				break;
//			}
//		};
//	};

//	// 定位监听
//	@Override
//	public void onLocationChanged(AMapLocation loc) {
//		if (null != loc) {
//			Message msg = mLocatingHandler.obtainMessage();
//			msg.obj = loc;
//			msg.what = AMapUtil.MSG_LOCATION_FINISH;
//			mLocatingHandler.sendMessage(msg);
//		}
//	}

	public static MainActivity mActivity;

	private static final String LOG_TAG = "HomeReceiver";
	private static HomeWatcherReceiver mHomeKeyReceiver = null;

	private static void registerHomeKeyReceiver(Context context) {
		Logger.d(LOG_TAG, "registerHomeKeyReceiver");
		mHomeKeyReceiver = new HomeWatcherReceiver();
		final IntentFilter homeFilter = new IntentFilter(
				Intent.ACTION_CLOSE_SYSTEM_DIALOGS);

		context.registerReceiver(mHomeKeyReceiver, homeFilter);
	}

	private static void unregisterHomeKeyReceiver(Context context) {
		Logger.d(LOG_TAG, "unregisterHomeKeyReceiver");
		if (null != mHomeKeyReceiver) {
			context.unregisterReceiver(mHomeKeyReceiver);
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		// 判断操作系统的版本
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			// 状态栏透明 需要在创建SystemBarTintManager之前调用。
			setTranslucentStatus(true);// 方法如下
		}
		mTintManager = new SystemBarTintManager(this);
		// 设置状态栏背景
		mTintManager.setStatusBarTintEnabled(true);
		mTintManager.setStatusBarTintColor(Color.argb(255, 16, 179, 178));// 颜色可以随便修改
		// mTintManager.setStatusBarTintColor(R.color.statusBarBGColor);//
		// 颜色可以随便修改
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		if (savedInstanceState == null) {
			mContent = mainFragment;
			getFragmentManager().beginTransaction()
					.add(R.id.container, mContent).commit();
		}

		CrashReport.setUserSceneTag(MainActivity.this, 3341); // 上报后的Crash会显示该标签
		d = new D(MainActivity.this);

		// 添加爆炸效果
		// mExplosionField = ExplosionField.attach2Window(this);
		// addListener(findViewById(R.id.container));

		try {

			mActivity = this;

			// 读取Provider
			/*
			 * Cursor c = getContentResolver() .query(Uri .parse(
			 * "com.ali.music.music.publicservice.provider.PublicMediaContentProvider"
			 * ), null, null, null, null);
			 * 
			 * if (c.getCount() > 0) { while (c.moveToNext()) { String mail =
			 * ""; for (int i = 0; i < c.getColumnCount(); i++) { int mailindex
			 * = c.getInt(0); mail += c.getString(mailindex) + "; "; }
			 * 
			 * mActivity.d.ShowMaterialSimpleConfirmDialog("" + mail); } }
			 * c.close();
			 */
			// 切换语言
			SharedPreferences appSettingPrefs = GetAlarmApplication
					.getContext().getSharedPreferences(ConstValue.APP_PF,
							Context.MODE_PRIVATE);

			int language = appSettingPrefs.getInt("Language", 0);
			Locale locale = Locale.SIMPLIFIED_CHINESE;
			switch (language) {
			case 1:
				locale = Locale.TRADITIONAL_CHINESE;
				break;
			case 2:
				locale = Locale.US;
				break;
			case 3:
				locale = Locale.UK;
				break;
			case 4:
				locale = Locale.JAPANESE;
				break;
			default:
				locale = Locale.SIMPLIFIED_CHINESE;
				break;
			}
			Configuration config = getResources().getConfiguration();
			config.locale = locale;
			getResources().updateConfiguration(config,
					getResources().getDisplayMetrics());

			// 判断是否有开启Notification access
			boolean isEnabledNLS = isEnabled();
			if (!isEnabledNLS) {
				openNotificationAccess();
			}

//			// AMap Begin
//			mlocationClient = new AMapLocationClient(
//					this.getApplicationContext());
//			// 初始化定位参数
//			mLocationOption = new AMapLocationClientOption();
//			// 设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
//			mLocationOption.setLocationMode(AMapLocationMode.Battery_Saving);
//			// 设置是否返回地址信息（默认返回地址信息）
//			mLocationOption.setNeedAddress(true);
//			// 设置是否只定位一次,默认为false
//			mLocationOption.setOnceLocation(false);
//			// 设置是否强制刷新WIFI，默认为强制刷新
//			mLocationOption.setWifiActiveScan(true);
//			// 设置是否允许模拟位置,默认为false，不允许模拟位置
//			mLocationOption.setMockEnable(false);
//			// 设置定位间隔,单位毫秒,默认为2000ms
//			mLocationOption.setInterval(2000);
//			// 给定位客户端对象设置定位参数
//			mlocationClient.setLocationOption(mLocationOption);
//			// 启动定位
//			// mlocationClient.startLocation();
//
//			// AMap End

			bas_in = new BounceTopEnter();
			bas_out = new SlideBottomExit();

			backButton = (ImageButton) findViewById(R.id.about_us_back);
			tvActionName = (TextView) findViewById(R.id.tv_actionName);
			tvAppName = (TextView) findViewById(R.id.tv_appName);

			tvActionName.setVisibility(View.GONE);
			backButton.setVisibility(View.GONE);
			Typeface font = Typeface.createFromAsset(getAssets(),
					"fontawesome-webfont.ttf");

			// 加载背景图
			// Calendar c = Calendar.getInstance();
			// int month = c.get(Calendar.MONTH);
			// String bg =
			// "bg/bg_month_"+String.format("%1$02d",month+1)+".png";
			//
			// @SuppressWarnings("deprecation")
			// Drawable d= new BitmapDrawable(getImageFromAssetsFile(bg));
			// llGrid = (LinearLayout)findViewById( R.id.ll_grid);
			// llGrid.setBackground(d);

			IntentFilter intentFilter = new IntentFilter(
					ConnectivityManager.CONNECTIVITY_ACTION);
			this.registerReceiver(networkChangedReceiver, intentFilter);

			(new DataLoadTask()).execute();
			
			// registerHomeKeyReceiver(this);

			// ConnectionClassManager.getInstance()
			// .register(connectionChangedListener);
			// DeviceBandwidthSampler.getInstance().startSampling();

			// 添加定时更新每日历史

			// Calendar c=Calendar.getInstance();//获取日期对象
			// c.setTimeInMillis(System.currentTimeMillis()); //设置Calendar对象
			// c.set(Calendar.HOUR, 9); //设置闹钟小时数
			// c.set(Calendar.MINUTE, 0); //设置闹钟的分钟数
			// c.set(Calendar.SECOND, 0); //设置闹钟的秒数
			// c.set(Calendar.MILLISECOND, 0); //设置闹钟的毫秒数
			// Intent intent = new Intent(MainActivity.this,
			// TodayHistoryAlarmReceiver.class); //创建Intent对象
			// PendingIntent pi = PendingIntent.getBroadcast(MainActivity.this,
			// 0, intent, 0); //创建PendingIntent
			// //alarmManager.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(),
			// pi); //设置闹钟
			// alarmManager.set(AlarmManager.RTC_WAKEUP,
			// System.currentTimeMillis(), pi); //设置闹钟，当前时间就唤醒
			// d.ShowMaterialSimpleConfirmDialog("每天9点自动刷新");//提示用户

			// 注册解锁通知
			registerScreenActionReceiver();

			// Logger.d("Length", Integer.toString(
			// "第二次世界大战中土耳其正式对纳粹德国、日本".length()));

			// // 常驻通知栏
			// NotificationManager nm = (NotificationManager)
			// getSystemService(Context.NOTIFICATION_SERVICE);
			// Notification n = new Notification(R.drawable.app_aligame,
			// "Hello,there!", System.currentTimeMillis());
			// n.flags = Notification.FLAG_NO_CLEAR
			// & Notification.FLAG_ONGOING_EVENT;
			// Intent i = new Intent(GetAlarmApplication.getContext(),
			// MainActivity.class);
			// i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
			// | Intent.FLAG_ACTIVITY_NEW_TASK);
			// // PendingIntent
			// PendingIntent contentIntent = PendingIntent.getActivity(
			// GetAlarmApplication.getContext(), R.string.app_name, i,
			// PendingIntent.FLAG_UPDATE_CURRENT);
			//
			// n.setLatestEventInfo(GetAlarmApplication.getContext(), "常伟华",
			// "Hello,there,I'm 常伟华.", contentIntent);
			// nm.notify(R.string.app_name, n);

			/*
			 * 
			 * YunosAuthService service = AlibabaSDK
			 * .getService(YunosAuthService.class); ssoClient = new
			 * SsoClient(MainActivity.this, ConstValue.YUNOS_APPKEY);
			 * service.authorize(ssoClient, AUTH_REQUEST_CODE, new
			 * ResultCallback<Result<AccessToken>>() {
			 * 
			 * @Override public void onSuccess(final Result<AccessToken> result)
			 * { AccessToken data = result.data;//
			 * 这里的AccessToken是一个Java类，并不是OAuth中的访问授权码AccessToken.data.get
			 * 
			 * Logger.d("TestAuth", "authcode:" + data.getAuthCode());//
			 * data.getAuthCode() // 返回就是授权码 Logger.d("TestAuth", "message:" +
			 * result.message);
			 * 
			 * // 根据 AuthCode去获取用户信息 GetUserTask getUserTask = new GetUserTask(
			 * MainActivity.this); getUserTask.execute(data.getAuthCode());
			 * 
			 * }
			 * 
			 * @Override public void onFailure(final int code, final String msg)
			 * { Logger.d("TestAuth", "code:" + code + ",msg:" + msg); } });
			 * 
			 * // 监听系统账号改变 // 由于yunos是单账号系统，收到此广播，就可以把授权登录的账号退出了 String
			 * permission = "com.aliyun.account.permission.SEND_MANAGE_DATA";
			 * IntentFilter intentFilter = new IntentFilter(LOGOUT_ACTION);
			 * LitePalApplication.getContext().registerReceiver(logoutReceiver,
			 * intentFilter, permission, null);
			 */

		} catch (Throwable thr) {
			CrashReport.postCatchedException(thr); // bugly会将这个throwable上报
		}

	}

	private void registerScreenActionReceiver() {
		final IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_SCREEN_OFF);
		filter.addAction(Intent.ACTION_SCREEN_ON);
		filter.addAction(Intent.ACTION_USER_PRESENT);
		registerReceiver(screenStateChangedReceiver, filter);

		IntentFilter filter2 = new IntentFilter();
		filter2.addAction(ConstValue.HEADD_NOTI);
		registerReceiver(headNotiReceiver, filter2);
	}

	// 获取悬浮通知
	private final BroadcastReceiver headNotiReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(final Context context, final Intent intent) {
			if (intent.getAction().equals(ConstValue.HEADD_NOTI)) {

				View view = getLayoutInflater().inflate(R.layout.notification,
						null);

				HeadsUpManager manage = HeadsUpManager
						.getInstant(getApplication());

				Bundle bundle = intent.getExtras();
				PendingIntent pIntent = bundle.getParcelable("PI");
				int id = bundle.getInt("id");
				int icon = bundle.getInt("icon");
				String title = bundle.getString("title");
				String pkg = bundle.getString("pkg");
				String text = bundle.getString("text");
				PendingIntent pendingIntent = PendingIntent.getActivity(
						MainActivity.this, 11, new Intent(MainActivity.this,
								MainActivity.class),
						PendingIntent.FLAG_UPDATE_CURRENT);

				HeadsUp.Builder builder = new HeadsUp.Builder(MainActivity.this);
				builder.setContentTitle(title)
						.setDefaults(Notification.DEFAULT_LIGHTS)
						// 要显示通知栏通知,这个一定要设置
						// .setSmallIcon(icon)
						// 2.3 一定要设置这个参数,负责会报错
						.setContentIntent(pIntent)
						.setFullScreenIntent(pendingIntent, false)
						.setContentText(text);

				// int notiId = (int) System.currentTimeMillis();
				int notiId = GetAlarmApplication.iNotiIdList.indexOfValue(pkg);

				if ((title != null && title.contains("[微信红包]"))
						|| (text != null && text.contains("[微信红包]") || (text != null && text
								.contains("[微信红包]")))) {
					try {
						pIntent.send();
					} catch (CanceledException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						CrashReport.postCatchedException(e);
					}
				}

				try {
					// 根据APP Package Name 查询 Logo
					Drawable iconDrawable = null;

					if(GetAlarmApplication.AppInfos.size() == 0){
						(new DataLoadTask()).execute();
					}
					
					for (AppInfo app : GetAlarmApplication.AppInfos) {
						if (app.getPkgName().equals(pkg)) {
							iconDrawable = app.getAppIcon();
							break;
						}
					}

					NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
					notificationManager.cancel(id);

					HeadsUp headsUp = builder.buildHeadUp();
					headsUp.setIconDrawable(iconDrawable);
					headsUp.setPkgName(pkg);
					headsUp.setSticky(false);
					headsUp.setActivateStatusBar(false);// 默认 true ,设置 false
														// 表示只显示
														// heads-up ,而不会发送到通知栏上
					headsUp.setInterval(6);
					manage.notify(notiId, headsUp);
				} catch (Exception e) {
					// TODO: handle exception
					CrashReport.postCatchedException(e);
				}

			}
		}
	};

	private final BroadcastReceiver screenStateChangedReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(final Context context, final Intent intent) {
			// Do your action here
			if (Intent.ACTION_SCREEN_ON.equals(intent.getAction())) {
				GetAlarmApplication.screenLocked = true;
			} else if (Intent.ACTION_SCREEN_OFF.equals(intent.getAction())) {
				GetAlarmApplication.screenLocked = true;
				// GetAlarmApplication.NotiMessageList.clear();
			} else {
				GetAlarmApplication.screenLocked = false;
				// GetAlarmApplication.NotiMessageList.clear();
			}
		}

	};

	// 切换 Fragment
	public void switchContent(Fragment from, Fragment to) {
		if (mContent != to) {
			mContent = to;
			backButton.setVisibility(View.VISIBLE);
			tvAppName.setVisibility(View.VISIBLE);
			tvActionName.setVisibility(View.VISIBLE);
			if (to == mainFragment) {
				backButton.setVisibility(View.GONE);
				tvActionName.setVisibility(View.GONE);
			} else {
				tvAppName.setVisibility(View.GONE);
				if (to == aboutFragment) {
					tvActionName.setText("关于");
				}
			}

			FragmentTransaction transaction = getFragmentManager()
					.beginTransaction();
			if (!to.isAdded()) { // 先判断是否被add过
				transaction.hide(from).add(R.id.container, to).commit(); // 隐藏当前的fragment，add下一个到Activity中
			} else {
				transaction.hide(from).show(to).commit(); // 隐藏当前的fragment，显示下一个
			}
		}
	}

	@Override
	public void onBackPressed() {
		if (mContent == mainFragment) {
			super.onBackPressed();
		} else {
			switchContent(mContent, mainFragment);
		}

	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		final String[] stringItems = { "升级", "关于", "退出" };
		final ActionSheetDialog dialog = new ActionSheetDialog(
				MainActivity.this, stringItems, null);
		dialog.title("操作\r\n(-------------------------------)")
				.isTitleShow(false)//
				.titleTextSize_SP(12.5f)//
				.show();

		dialog.setOnOperItemClickL(new OnOperItemClickL() {
			@Override
			public void onOperItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				switch (position) {
				case 0:

					try {

						mThread = new Thread(runnable);
						mThread.start();// 线程启动

					} catch (Exception e) {
						d.ShowMaterialSimpleConfirmDialog("异常信息\r\n"
								+ e.getMessage());
						CrashReport.postCatchedException(e);
					}

					break;
				case 1:
					switchContent(mContent, aboutFragment);
					break;
				case 2:
					final MaterialDialog dialog = new MaterialDialog(
							MainActivity.this);
					dialog.isTitleShow(false)//
							.btnNum(2).content("确定要退出吗？")//
							.btnText("取消", "确定")//
							.showAnim(bas_in)//
							.dismissAnim(bas_out)//
							.show();

					dialog.setOnBtnClickL(new OnBtnClickL() {
						@Override
						public void onBtnClick() {
							dialog.dismiss();
						}
					}, new OnBtnClickL() {
						@Override
						public void onBtnClick() {
							Exit();
							dialog.dismiss();
						}
					});
					break;
				default:
					break;
				}
				// showShort(MainActivity.this, stringItems[position]);
				dialog.dismiss();
			}
		});

		return false;

		// Inflate the menu; this adds items to the action bar if it is present.
		// getMenuInflater().inflate(R.menu.main, menu);
		// return true;
	}

	// 网速监听
	private ConnectionChangedListener connectionChangedListener = new ConnectionChangedListener();

	private class ConnectionChangedListener implements
			ConnectionClassManager.ConnectionClassStateChangeListener {
		@Override
		public void onBandwidthStateChange(ConnectionQuality bandwidthState) {
			PendingIntent pendingIntent = PendingIntent.getActivity(
					MainActivity.this, 11, new Intent(MainActivity.this,
							MainActivity.class),
					PendingIntent.FLAG_UPDATE_CURRENT);
			double downloadKBitsPerSecond = ConnectionClassManager
					.getInstance().getDownloadKBitsPerSecond();
			if (bandwidthState == ConnectionQuality.POOR) {
				Intent i = new Intent("net.cmono.getalarm.headnoti");
				// 绑定数据
				i.putExtra("id", 1);
				i.putExtra("icon", R.drawable.ic_traffic_recharge);
				i.putExtra("title", "网络提醒");
				i.putExtra("text", "当前网速过低，可能影响使用");
				i.putExtra("PI", pendingIntent);

				sendBroadcast(i);
			} else if (bandwidthState == ConnectionQuality.UNKNOWN) {
				Intent i = new Intent("net.cmono.getalarm.headnoti");
				// 绑定数据
				i.putExtra("id", 2);
				i.putExtra("icon", R.drawable.ic_traffic_recharge);
				i.putExtra("title", "网络提醒");
				i.putExtra("text", "未知网络，请检查设置");
				i.putExtra("PI", pendingIntent);

				sendBroadcast(i);
			}

		}
	}

	// 关闭APP ---
	// public BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
	//
	// @Override
	// public void onReceive(Context context, Intent intent) {
	// // TODO Auto-generated method stub
	// finish();
	// }
	// };
	//
	// @Override
	// public void onResume() {
	// super.onResume();
	//
	// // 在当前的activity中注册广播
	// IntentFilter filter = new IntentFilter();
	// filter.addAction(ConstValue.EXIT_ACTION);
	// registerReceiver(this.broadcastReceiver, filter); // 注册
	// }
	//
	// @Override
	// protected void onDestroy() {
	// // TODO Auto-generated method stub
	// super.onDestroy();
	// this.unregisterReceiver(broadcastReceiver);
	// }

	@Override
	public void onResume() {
		super.onResume();
		ConnectionClassManager.getInstance()
				.register(connectionChangedListener);
		GetAlarmApplication.screenLocked = false;

		NotiRuleUtil.ReloadNotis();
		NotiRuleUtil.ReloadiGnores();
		NotiRuleUtil.ReloadNotiIds();
		
		// 清理一周前的日志文件
		(new LogCleanTask(MainActivity.this)).execute();
	}

	@Override
	protected void onPause() {
		super.onPause();
		ConnectionClassManager.getInstance().remove(connectionChangedListener);
	}

	@Override
	protected void onDestroy() {

		if (this.networkChangedReceiver != null) {
			this.unregisterReceiver(networkChangedReceiver);
		}
		if (this.headNotiReceiver != null) {
			this.unregisterReceiver(headNotiReceiver);
		}
//		if (null != mlocationClient) {
//			/**
//			 * 如果AMapLocationClient是在当前Activity实例化的，
//			 * 在Activity的onDestroy中一定要执行AMapLocationClient的onDestroy
//			 */
//			mlocationClient.onDestroy();
//			mlocationClient = null;
//			mLocationOption = null;
//		}

		// unregisterHomeKeyReceiver(this);

		super.onDestroy();
		// DeviceBandwidthSampler.getInstance().stopSampling();
		// ConnectionClassManager.getInstance().remove(connectionChangedListener);
	};

	protected void Exit() {
		Intent intent = new Intent();
		intent.setAction(ConstValue.EXIT_ACTION);
		this.sendBroadcast(intent);
		super.finish();
	}

	// 关闭APP ---

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_about) {

			switchContent(mContent, aboutFragment);

			return true;
		} else if (id == R.id.action_update) {
			try {
				// OkHttpClient client = new OkHttpClient();
				// Request.Builder builder = new Request.Builder();
				// Request request =
				// builder.url("http://www.baidu.com").build();
				//
				//
				// DeviceBandwidthSampler.getInstance().startSampling();
				// client.newCall(request).enqueue(new Callback() {
				// @Override
				// public void onFailure(Call call, IOException e) {
				// DeviceBandwidthSampler.getInstance().stopSampling();
				// Log.e("TAG","onFailure:"+e);
				// }
				//
				// @Override
				// public void onResponse(Call call, Response response) throws
				// IOException {
				// DeviceBandwidthSampler.getInstance().stopSampling();
				// Log.e("TAG","onResponse:"+response);
				// final ConnectionQuality connectionQuality =
				// ConnectionClassManager.getInstance().getCurrentBandwidthQuality();
				// final double downloadKBitsPerSecond =
				// ConnectionClassManager.getInstance().getDownloadKBitsPerSecond();
				// Log.e("TAG","connectionQuality:"+connectionQuality+" downloadKBitsPerSecond:"+downloadKBitsPerSecond+" kb/s");
				//
				// tv.post(new Runnable() {
				// @Override
				// public void run() {
				// tv.setText("connectionQuality:"+connectionQuality+"\n"+"downloadKBitsPerSecond:"+downloadKBitsPerSecond+" kb/s");
				// }
				// });
				// }
				// });
				mThread = new Thread(runnable);
				mThread.start();// 线程启动
				// UpdateHelper manager = new UpdateHelper(MainActivity.this);
				// // 检查软件更新
				// manager.checkUpdate();

				// //显示ProgressDialog
				// progressDialog = ProgressDialog.show(MainActivity.this,
				// "Loading...", "Please wait...", true, false);
				//
				// //新建线程
				// new Thread(){
				//
				// @Override
				// public void run() {
				// //需要花时间计算的方法
				// Calculation.calculate(4);
				//
				// //向handler发消息
				// progressHandler.sendEmptyMessage(0);
				// }}.start();
				//
			} catch (Exception e) {
				// TODO: handle exception
				new AlertDialog.Builder(this).setTitle("异常信息")
						.setMessage(e.getMessage())
						.setPositiveButton("OK", null).show();
				CrashReport.postCatchedException(e);
			}

			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private ProgressDialog progressDialog;
	/**
	 * 用Handler来更新UI
	 */
	private Handler progressHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {

			// 关闭ProgressDialog
			progressDialog.dismiss();

			// 更新UI
			// statusTextView.setText("Completed!");
		}
	};

	Thread mThread;

	static Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			Bundle data = msg.getData();

			// TODO
			// UI界面的更新等相关操作
		}
	};

	Runnable runnable = new Runnable() {

		@Override
		public void run() {// run()在新的线程中运行
			Looper.prepare();
			UpdateUtil manager = new UpdateUtil(MainActivity.this);
			// 检查软件更新
			manager.checkUpdate();
			Looper.loop();
			handler.sendEmptyMessage(0);
		}
	};
	
	public class DataLoadTask extends AsyncTask<String, Integer, String> {

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			NotiRuleUtil.ReloadAppInfos();
			return null;
		}
		
	}
	
	

	// 清理一周之前的日志文件
	class LogCleanTask extends AsyncTask<Calendar, Integer, String> {
		private Context context;
		Calendar date;

		LogCleanTask(Context context) {
			this.context = context;
		}

		/**
		 * 运行在UI线程中，在调用doInBackground()之前执行
		 */
		@Override
		protected void onPreExecute() {
			// Toast.makeText(context, "开始执行获取", Toast.LENGTH_SHORT).show();

		}

		private void RecursionDeleteFile(File file) {
			if (file.isFile()) {
				file.delete();
				return;
			}
			if (file.isDirectory()) {
				File[] childFile = file.listFiles();
				if (childFile == null || childFile.length == 0) {
					file.delete();
					return;
				}
				for (File f : childFile) {
					RecursionDeleteFile(f);
				}
				file.delete();
			}
		}

		/**
		 * 删除文件或文件夹
		 * 
		 * @param f
		 */
		private boolean delFile(File f) {
			boolean flag = false;
			if (!f.exists()) {
				System.out.println("文件或文件夹不存在！");
				flag = false;
			} else {
				if (f.isFile()) {
					flag = f.delete();
				} else {
					// 先删除文件夹下的文件或子文件夹
					String[] files = f.list();
					for (int i = 0; i < files.length; i++) {
						String p = f.getPath() + "/" + files[i];
						delFile(new File(p));
					}
					// 再删除文件夹
					flag = f.delete();
				}
			}
			return flag;
		}

		/**
		 * 后台运行的方法，可以运行非UI线程，可以执行耗时的方法
		 */
		@Override
		protected String doInBackground(Calendar... params) {

			try {
				Date cDate = new Date();
				String fName = ConstValue.PATHFORMATE.format(cDate);
				String root = ConstValue.ROOTFOLDER;
				File directory = new File(root);
				File files[] = directory.listFiles();
				Boolean flag = false;

				if (files != null && files.length > 0) {
					for (File f : files) {

						if (f.isDirectory()) {
							if (Integer.parseInt(fName)
									- Integer.parseInt(f.getName()) >= 7) {
								flag = true;
								RecursionDeleteFile(f);
							}
						}

					}
				}

				// 同步头像
				// File folder = new File(ConstValue.TCoverPath);
				// File artists[] = folder.listFiles();
				// if (artists != null && artists.length > 0) {
				// for (File f : artists) {
				// if (f.isDirectory()) {
				// File dir = new File(ConstValue.TArtistPath
				// + f.getName());
				// Boolean isNew = false;
				// if (!dir.exists()) {
				// dir.mkdirs();
				// isNew = true;
				// }
				// if (isNew) {
				// File covers[] = f
				// .listFiles(new ArtistFilenameFilter());
				// if (covers != null && covers.length > 0) {
				// File firstFile = covers[0];
				// String newFile = ConstValue.TArtistPath
				// + dir.getName() + File.separator
				// + firstFile.getName();
				// Logger.d("A", firstFile.getPath() + "|"
				// + newFile);
				// FileUtil.copyFile(firstFile.getPath(),
				// newFile);
				// }
				// }
				// }
				// }
				// }

				if (flag) {
					return "成功删除指定文件夹";
				}

				return "";
			} catch (Exception e) {
				// TODO: handle exception
				CrashReport.postCatchedException(e);
			}

			return "未能成功删除";

		}

		/**
		 * 运行在ui线程中，在doInBackground()执行完毕后执行
		 */
		@Override
		protected void onPostExecute(String result) {
			// Logger.d("结果", result);
			// mainActivity.d
			// .ShowMaterialSimpleConfirmDialog(Integer.toString(cards.size()));
			if (result.length() > 0) {
				Toast.makeText(context, result, Toast.LENGTH_SHORT).show();
			}

		}

		/**
		 * 在publishProgress()被调用以后执行，publishProgress()用于更新进度
		 */
		@Override
		protected void onProgressUpdate(Integer... values) {
		}
	}

	private void startPhotoZoom(Uri uri, int size) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// crop为true是设置在开启的intent中设置显示的view可以剪裁
		intent.putExtra("crop", "true");
		intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
		intent.putExtra("noFaceDetection", true);
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);

		// outputX,outputY 是剪裁图片的宽高
		intent.putExtra("outputX", size);
		intent.putExtra("outputY", size);
		intent.putExtra("return-data", true);

		startActivityForResult(intent, PHOTO_REQUEST_CUT);
	}

	public File mFile;// 存储图片的文件
	private static final int PHOTO_REQUEST_TAKEPHOTO = 1;// 拍照
	private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
	private static final int PHOTO_REQUEST_CUT = 3;// 结果

	private void getImageAfterCut(Intent data) {
		Bundle extras = data.getExtras();
		if (extras != null) {
			Bitmap photo = extras.getParcelable("data");
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			photo.compress(Bitmap.CompressFormat.JPEG, 75, stream);
			// 将流写入文件或者直接使用
			try {
				FileOutputStream fos = new FileOutputStream(
						ConstValue.ROOTFOLDER + "avator.png");
				photo.compress(Bitmap.CompressFormat.JPEG, 90, fos);
				fos.flush();
				fos.close();
				aboutFragment.imageView.setImageURI(Uri.fromFile(new File(
						ConstValue.ROOTFOLDER + "avator.png")));
			} catch (IOException e) {
				e.printStackTrace();
				CrashReport.postCatchedException(e);
			}
			photo.recycle();
			photo = null;
			System.gc();
		}
	}

	/**
	 * 从本地相册中选择并得到相片
	 */
	private void getImageFromGallery(Intent data) {
		Uri selectedImage = data.getData();
		// 用一个String数组存储相册所有图片
		String[] filePathColumn = { MediaColumns.DATA };
		// 用一个Cursor对象的到相册的所有内容
		Cursor cursor = getContentResolver().query(selectedImage,
				filePathColumn, null, null, null);
		cursor.moveToFirst();
		// 得到选中图片下标
		int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
		// 得到所选的相片路径
		String picturePath = cursor.getString(columnIndex);
		// 关闭Cursor，以免占用资源
		cursor.close();

		ImageUtil
				.zipImage(picturePath, ConstValue.ROOTFOLDER + "avator.png");// 一般相册中图片太大，不能直接显示，需要压缩图片
		aboutFragment.imageView.setImageURI(Uri.fromFile(new File(
				ConstValue.ROOTFOLDER + "avator.png")));
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
		case PHOTO_REQUEST_TAKEPHOTO:
			if (resultCode == RESULT_OK) {
				startPhotoZoom(Uri.fromFile(new File(mFile.getAbsolutePath())),
						150);
				/*
				 * String fName = rootFolder + File.separator + "avator.png";
				 * ImageZipHelper.zipImage(mFile.getAbsolutePath(), fName);
				 * aboutFragment.imageView.setImageURI(Uri .fromFile(new
				 * File(fName)));
				 */
			}
			break;

		case PHOTO_REQUEST_GALLERY:
			if (resultCode == RESULT_OK) {
				// getImageFromGallery(data);
				startPhotoZoom(data.getData(), 150);
			}
			break;

		case PHOTO_REQUEST_CUT:

			if (resultCode == RESULT_OK) {
				// ImageZipHelper.zipImage("", mFile.getAbsolutePath());// 压缩图片
				getImageAfterCut(data);
			}
			break;

		case AUTH_REQUEST_CODE:
			if (ssoClient != null) {
				// status=>200
				// message=>SUCCESS
				// authcode=>abc
				Bundle bunlde = data.getExtras();
				Object[] keys = bunlde.keySet().toArray();
				String nick = "Key=" + keys[0] + "Value="
						+ (String) bunlde.get((String) keys[0]) + "Key="
						+ keys[1] + "Value="
						+ (String) bunlde.get((String) keys[1]) + "Key="
						+ keys[2] + "Value="
						+ (String) bunlde.get((String) keys[2]);

				// new AlertDialog.Builder(this).setTitle("名称").setMessage(nick)
				// .setPositiveButton("OK", null).show();
				ssoClient.authorizeCallBack(requestCode, resultCode, data);
			}
			break;

		}

	}

	/**
	 * Activity Main Fragment
	 * 
	 * @author Changweihua
	 * 
	 */
	public static class PlaceholderFragment extends Fragment {

		class TodayHistoryTask extends AsyncTask<Calendar, Integer, List<Card>> {
			private Context context;
			Calendar date;

			TodayHistoryTask(Context context) {
				this.context = context;
			}

			/**
			 * 运行在UI线程中，在调用doInBackground()之前执行
			 */
			@Override
			protected void onPreExecute() {
				// Toast.makeText(context, "开始执行获取", Toast.LENGTH_SHORT).show();
			}

			/**
			 * 后台运行的方法，可以运行非UI线程，可以执行耗时的方法
			 */
			@Override
			protected List<Card> doInBackground(Calendar... params) {

				date = params[0];
				SharedPreferences sp = GetAlarmApplication.getAppContext()
						.getSharedPreferences(ConstValue.SETTING_PF,
								Context.MODE_PRIVATE);
				Date cDate = new Date();
				String fName = ConstValue.PATHFORMATE.format(cDate);
				String ExternalStorePath = ConstValue.ROOTFOLDER + fName
						+ File.separator;

				String FileName = fName + ".json";
				String FileName0 = fName + "0" + ".json";
				String FileName1 = fName + "1" + ".json";
				File file = new File(ExternalStorePath + FileName);
				File file0 = new File(ExternalStorePath + FileName0);
				File file1 = new File(ExternalStorePath + FileName1);
				// 得到文件最新修改时间
				long time = file.lastModified();
				Calendar mCalendar = Calendar.getInstance();
				mCalendar.setTimeInMillis(time);

				Calendar cCalendar = Calendar.getInstance();
				cCalendar.set(date.get(Calendar.YEAR),
						date.get(Calendar.MONTH) + 1,
						date.get(Calendar.DAY_OF_MONTH), 9, 0);

				if ((file0.exists() && date.get(Calendar.HOUR_OF_DAY) < 9)
						|| (file1.exists() && date.get(Calendar.HOUR_OF_DAY) >= 9)) {

					String source = "{}";
					List<Card> cards = new ArrayList<Card>();
					try {
						if (file1.exists()) {
							source = FileUtil.readFile(ExternalStorePath
									+ FileName1);
						} else if (file0.exists()) {
							source = FileUtil.readFile(ExternalStorePath
									+ FileName0);
						}
						// 创建一个JSON对象
						JSONObject jsonObject = new JSONObject(source);
						JSONArray array = jsonObject.getJSONArray("result");
						int length = array.length();
						for (int i = 0; i < length; i++) {// 遍历JSONArray
							JSONObject oj = array.getJSONObject(i);
							String line1 = "公元 " + oj.getString("year").trim()
									+ " 年";
							String line2 = URLDecoder.decode(
									oj.getString("title"), "UTF-8").trim();
							Card card = new Card(line1, line2);
							cards.add(card);
						}
						return cards;
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						CrashReport.postCatchedException(e);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						CrashReport.postCatchedException(e);
					}
					return null;
				} else {

					String url = "http://www.ipip5.com/today/api.php?type=json";
					List<Card> cards = new ArrayList<Card>();
					HttpGet httpGet = new HttpGet(url);
					HttpResponse httpResponse = null;
					try {
						httpResponse = new DefaultHttpClient().execute(httpGet);
						if (httpResponse.getStatusLine().getStatusCode() == 200) {

							try {
								String source = EntityUtils
										.toString(httpResponse.getEntity());

								if (date.get(Calendar.HOUR_OF_DAY) >= 9) {
									FileUtil.saveFile(source, ExternalStorePath
											+ FileName1);
									if (file0.exists()) {
										file0.delete();
									}
								} else {
									FileUtil.saveFile(source, ExternalStorePath
											+ FileName0);
								}

								// 创建一个JSON对象
								JSONObject jsonObject = new JSONObject(source);
								JSONArray array = jsonObject
										.getJSONArray("result");
								int length = array.length();
								for (int i = 0; i < length; i++) {// 遍历JSONArray
									JSONObject oj = array.getJSONObject(i);
									String line1 = "公元 "
											+ oj.getString("year").trim()
											+ " 年";
									String line2 = URLDecoder.decode(
											oj.getString("title"), "UTF-8")
											.trim();
									Card card = new Card(line1, line2);
									cards.add(card);
								}

								return cards;
							} catch (ParseException | IOException
									| JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								CrashReport.postCatchedException(e);
							}
						}

					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						CrashReport.postCatchedException(e);
					}

					return null;
				}

			}

			/**
			 * 运行在ui线程中，在doInBackground()执行完毕后执行
			 */
			@Override
			protected void onPostExecute(List<Card> cards) {
				// mainActivity.d
				// .ShowMaterialSimpleConfirmDialog(Integer.toString(cards.size()));
				cardArrayAdapter.clear();
				cardArrayAdapter.notifyDataSetChanged();
				// listView.setAdapter(cardArrayAdapter);
				mainActivity.mainFragment.listView.refreshDrawableState();
				cardArrayAdapter = new CardArrayAdapter(mainActivity,
						R.layout.list_item_card);
				SharedPreferences sp = mActivity.getSharedPreferences(
						ConstValue.SETTING_PF, Context.MODE_PRIVATE);
				String nickName = sp.getString("nickName", "新用户");
				Card nameCard = new Card("欢迎你", nickName);
				cards.add(0, nameCard);
				for (int i = 0; i < cards.size(); i++) {
					cardArrayAdapter.add(cards.get(i));
				}

				listView.setAdapter(null);

				listView.setAdapter(cardArrayAdapter);
			}

			/**
			 * 在publishProgress()被调用以后执行，publishProgress()用于更新进度
			 */
			@Override
			protected void onProgressUpdate(Integer... values) {
			}
		}

		MainActivity mainActivity;
		private Context mContext;
		private GridView grid_photo;
		private BaseAdapter mAdapter = null;
		private ArrayList<Icon> mData = null;
		CardArrayAdapter cardArrayAdapter;
		ListView listView;

		CardArrayAdapter notiArrayAdapter;
		ListView notiListView;

		private boolean isHistoryCreateLoaded = false;

		public PlaceholderFragment() {

		}

		/**
		 * 设置列表框高度，解决滚屏问题
		 */
		private void initListViewHeight(ListView list) {
			CardArrayAdapter listAdapter = (CardArrayAdapter) list.getAdapter();
			if (listAdapter == null || listAdapter.getCount() == 0) {
				ViewGroup.LayoutParams params = list.getLayoutParams();
				params.height = 0;
				list.setLayoutParams(params);
				return;
			}
			int totalHeight = 0;
			for (int i = 0; i < listAdapter.getCount(); i++) {
				View listItem = listAdapter.getView(i, null, list);
				listItem.measure(0, 0);
				totalHeight += listItem.getMeasuredHeight();
			}
			ViewGroup.LayoutParams params = list.getLayoutParams();
			params.height = totalHeight
					+ (list.getDividerHeight() * (listAdapter.getCount() - 1));
			params.height += 32 * (listAdapter.getItem(0).getLine2().length() / 21 + 1);
			list.setLayoutParams(params);
		}

		@Override
		public void onResume() {
			super.onResume();
			try {

				// 判断是否开启自动更新，并且是否已经更新
				SharedPreferences sp = mActivity.getSharedPreferences(
						ConstValue.SETTING_PF, Context.MODE_PRIVATE);
				boolean autoWord = sp.getBoolean("autoWord", false);
				String wordDate = sp.getString("wordDate", "");
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				String date = format.format(new Date());
				if (GetAlarmApplication.isOnline && autoWord
						&& !wordDate.equals(date)) {
					mainActivity.startService(new Intent(mainActivity,
							AutoWordService.class));
				}

				// Add Cus

				List<Card> cards = new ArrayList<Card>();
				notiArrayAdapter = new CardArrayAdapter(mainActivity,
						R.layout.list_item_card);

				String cnWord = sp.getString("cnWord", "励志，名人名言");
				cards.add(0, new Card("每日一句", cnWord));

				for (int i = 0; i < cards.size(); i++) {
					notiArrayAdapter.add(cards.get(i));
				}

				notiListView.setAdapter(notiArrayAdapter);

				initListViewHeight(notiListView);

				// if (!(cardArrayAdapter.getItem(0).getLine1().equals("欢迎你")))
				// {
				// Card nameCard = new Card("欢迎你", "常伟华 Lance Chang");
				// cardArrayAdapter.add(nameCard);
				// cardArrayAdapter.notifyDataSetChanged();
				// listView.refreshDrawableState();
				// }

				// /

				Calendar calendar = Calendar.getInstance();

				Date cDate = new Date();
				String fName = ConstValue.PATHFORMATE.format(cDate);
				String ExternalStorePath = ConstValue.ROOTFOLDER + fName
						+ File.separator;

				String FileName0 = ExternalStorePath + fName + "0" + ".json";
				String FileName1 = ExternalStorePath + fName + "1" + ".json";

				// 如果当前时间小于九点，且已经加载过0，则跳过
				// 如果当前时间大于九点，没有加载过1，执行更新
				/*
				 * Logger.d("CTIME",
				 * Integer.toString(calendar.get(Calendar.HOUR_OF_DAY)));
				 */
				if (isHistoryCreateLoaded) {
					isHistoryCreateLoaded = false;
					return;
				}

				if (calendar.get(Calendar.HOUR_OF_DAY) < 9
						&& new File(FileName0).exists()) {
					return;
				}

				if (calendar.get(Calendar.HOUR_OF_DAY) >= 9
						&& new File(FileName1).exists()) {
					isHistoryCreateLoaded = false;
					return;
				}
				TodayHistoryTask task = new TodayHistoryTask(mainActivity);
				task.execute(calendar);
			} catch (Throwable thr) {
				CrashReport.postCatchedException(thr); // bugly会将这个throwable上报
			}
		};

		private void ItemOnLongClick1() {
			// 注：setOnCreateContextMenuListener是与下面onContextItemSelected配套使用的
			listView.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {

				@Override
				public void onCreateContextMenu(ContextMenu menu, View v,
						ContextMenuInfo menuInfo) {
					menu.add(0, 0, 0, "购买");
					menu.add(0, 1, 0, "收藏");
					menu.add(0, 2, 0, "对比");

				}

			});
		}

		// 长按菜单响应函数
		public boolean onContextItemSelected(MenuItem item) {

			AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
					.getMenuInfo();
			// MID = (int) info.id;// 这里的info.id对应的就是数据库中_id的值

			switch (item.getItemId()) {
			case 0:
				// 添加操作
				Toast.makeText(mainActivity, "添加", Toast.LENGTH_SHORT).show();
				break;

			case 1:
				// 删除操作
				break;

			case 2:
				// 删除ALL操作
				break;

			default:
				break;
			}

			return super.onContextItemSelected(item);

		}

		private void ItemOnLongClick2() {

			listView.setOnItemLongClickListener(new OnItemLongClickListener() {

				@Override
				public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
						final int arg2, long arg3) {
					// list.remove(arg2);

					TextView tv = (TextView) arg1.findViewById(R.id.line2);

					// Gets a handle to the clipboard service.
					if (null == mClipboard) {
						mClipboard = (ClipboardManager) GetAlarmApplication
								.getAppContext().getSystemService(
										Context.CLIPBOARD_SERVICE);

					}
					// Creates a new text clip to put on the clipboard
					ClipData clip = ClipData.newPlainText("simple text",
							tv.getText());

					// Set the clipboard's primary clip.
					mClipboard.setPrimaryClip(clip);

					Toast.makeText(mainActivity, "已经复制到剪贴板", Toast.LENGTH_SHORT)
							.show();

					// new AlertDialog.Builder(mainActivity)
					// .setTitle("对Item进行操作")
					// .setItems(R.array.card_opts,
					// new DialogInterface.OnClickListener() {
					// public void onClick(
					// DialogInterface dialog,
					// int which) {
					// String[] PK = getResources()
					// .getStringArray(
					// R.array.card_opts);
					// Toast.makeText(mainActivity,
					// PK[which],
					// Toast.LENGTH_LONG).show();
					// // if (PK[which].equals("删除")) {
					// // //
					// // 按照这种方式做删除操作，这个if内的代码有bug，实际代码中按需操作
					// // list.remove(arg2);
					// // adapter = (SimpleAdapter)
					// // listView
					// // .getAdapter();
					// // if (!adapter.isEmpty()) {
					// // adapter.notifyDataSetChanged();
					// // // 实现数据的实时刷新
					// // }
					// // }
					// }
					// })
					// .setNegativeButton("取消",
					// new DialogInterface.OnClickListener() {
					// public void onClick(
					// DialogInterface dialog,
					// int which) {
					// // TODO Auto-generated method stub
					//
					// }
					// }).show();
					return true;
				}
			});

		}

		private ClipboardManager mClipboard = null;

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			final View rootView = inflater.inflate(R.layout.fragment_main,
					container, false);

			mainActivity = (MainActivity) getActivity();
			mContext = rootView.getContext();

			// Card Message Start
			listView = (ListView) rootView.findViewById(R.id.card_listView);
			notiListView = (ListView) rootView.findViewById(R.id.noti_listView);

			ItemOnLongClick2();

			notiArrayAdapter = new CardArrayAdapter(mainActivity,
					R.layout.list_item_card);

			listView.addHeaderView(new View(mainActivity));
			listView.addFooterView(new View(mainActivity));

			cardArrayAdapter = new CardArrayAdapter(mainActivity,
					R.layout.list_item_card);

			try {

				// if (!date.equals(sp.getString("todayHistoryDate", ""))) {
				TodayHistoryTask task = new TodayHistoryTask(mainActivity);
				task.execute(Calendar.getInstance());
				isHistoryCreateLoaded = true;
				// }

			} catch (Throwable thr) {
				CrashReport.postCatchedException(thr); // bugly会将这个throwable上报
			}

			notiListView.setAdapter(notiArrayAdapter);
			listView.setAdapter(cardArrayAdapter);

			// for (int i = 0; i < 10; i++) {
			// Card card = new Card("Card " + (i + 1) + " Line 1", "Card "
			// + (i + 1) + " Line 2");
			// cardArrayAdapter.add(card);
			// }
			// listView.setAdapter(cardArrayAdapter);
			// Card Message End

			grid_photo = (GridView) rootView.findViewById(R.id.grid_photo);

			mData = new ArrayList<Icon>();

			SharedPreferences appSettingPrefs = GetAlarmApplication
					.getContext().getSharedPreferences(ConstValue.APP_PF,
							Context.MODE_PRIVATE);

			int style = appSettingPrefs.getInt("IconStyle", 0);
			if (style == 4) {
				mData.add(new Icon(R.mipmap.iv_icon_1111, "设置"));
				mData.add(new Icon(R.mipmap.iv_icon_2222, "状态"));
				mData.add(new Icon(R.mipmap.iv_icon_3333, "命令"));
				mData.add(new Icon(R.mipmap.iv_icon_4444, "帮助"));
				mData.add(new Icon(R.mipmap.iv_icon_5555, "反馈"));
				mData.add(new Icon(R.mipmap.iv_icon_6666, "壁纸"));
			} else if (style == 3) {
				mData.add(new Icon(R.mipmap.iv_icon_111, "设置"));
				mData.add(new Icon(R.mipmap.iv_icon_222, "状态"));
				mData.add(new Icon(R.mipmap.iv_icon_333, "命令"));
				mData.add(new Icon(R.mipmap.iv_icon_444, "帮助"));
				mData.add(new Icon(R.mipmap.iv_icon_555, "反馈"));
				mData.add(new Icon(R.mipmap.iv_icon_666, "壁纸"));
			} else if (style == 1) {
				mData.add(new Icon(R.mipmap.iv_icon_1, "设置"));
				mData.add(new Icon(R.mipmap.iv_icon_2, "状态"));
				mData.add(new Icon(R.mipmap.iv_icon_3, "命令"));
				mData.add(new Icon(R.mipmap.iv_icon_4, "帮助"));
				mData.add(new Icon(R.mipmap.iv_icon_5, "反馈"));
				mData.add(new Icon(R.mipmap.iv_icon_6, "壁纸"));
			} else {
				mData.add(new Icon(R.mipmap.iv_icon_11, "设置"));
				mData.add(new Icon(R.mipmap.iv_icon_22, "状态"));
				mData.add(new Icon(R.mipmap.iv_icon_33, "命令"));
				mData.add(new Icon(R.mipmap.iv_icon_44, "帮助"));
				mData.add(new Icon(R.mipmap.iv_icon_55, "反馈"));
				mData.add(new Icon(R.mipmap.iv_icon_66, "壁纸"));
			}

			mAdapter = new IconAdapter<Icon>(mData, R.layout.item_grid_icon) {
				@Override
				public void bindView(ViewHolder holder, Icon obj) {
					holder.setImageResource(R.id.img_icon, obj.getiId());
					holder.setText(R.id.txt_icon, obj.getiName());
				}
			};

			grid_photo.setAdapter(mAdapter);

			/*
			 * grid_photo.setOnTouchListener(new AdapterView.OnTouchListener() {
			 * 
			 * @Override public boolean onTouch(View v, MotionEvent event) { //
			 * TODO Auto-generated method stub TextView tView = (TextView)
			 * v.findViewById(R.id.txt_icon); tView.setTextColor(Color.argb(255,
			 * 16, 179, 178)); return false; } });
			 */
			grid_photo
					.setOnItemClickListener(new AdapterView.OnItemClickListener() {
						@Override
						public void onItemClick(AdapterView<?> parent,
								View view, int position, long id) {

							if (position == 5) {

								final String[] stringItems = { "动态壁纸", "静态壁纸",
										"静态壁纸" };
								final ActionSheetDialog dialog = new ActionSheetDialog(
										mainActivity, stringItems, null);
								dialog.title(
										"操作\r\n(-------------------------------)")
										.isTitleShow(false)//
										.titleTextSize_SP(12.5f)//
										.show();

								dialog.setOnOperItemClickL(new OnOperItemClickL() {
									@Override
									public void onOperItemClick(
											AdapterView<?> parent, View view,
											int position, long id) {
										switch (position) {
										case 0:
											Intent intents = new Intent();
											intents.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
											intents.setAction(WallpaperManager.ACTION_LIVE_WALLPAPER_CHOOSER);
											startActivity(intents);
											break;
										case 1:

											// Effects effect = Effects.scale;
											// TipViewManager.getTipRootView(
											// mainActivity,
											// R.layout.activity_splash);
											// String msg =
											// "Today we would like to share a couple of simple styles and effects for android notifications.";
											// // 显示的内容View
											// LinearLayout contentView =
											// (LinearLayout) LayoutInflater
											// .from(mainActivity)
											// .inflate(
											// R.layout.show_normal_tip,
											// null);
											// ((TextView) contentView
											// .findViewById(R.id.tip_msg))
											// .setText(msg);
											// // 显示提示消息
											// TipViewManager.show(mainActivity,
											// contentView, 60, effect,
											// 500, 2000);
											break;
										case 2:
											Intent chooseIntent = new Intent(
													Intent.ACTION_SET_WALLPAPER);
											// 启动系统选择应用
											Intent intent = new Intent(
													Intent.ACTION_CHOOSER);
											intent.putExtra(
													Intent.EXTRA_INTENT,
													chooseIntent);
											intent.putExtra(Intent.EXTRA_TITLE,
													"选择壁纸");
											startActivity(intent);
										default:
											break;
										}
										dialog.dismiss();
									}
								});

								return;
							} else if (position == 0) {
								mainActivity.switchContent(
										mainActivity.mContent,
										mainActivity.basicSettingPrefFragment);
								mainActivity.tvActionName.setText("设置");
							} else if (position == 1) {
								mainActivity.tvActionName.setText("运行状态");
								mainActivity.switchContent(
										mainActivity.mContent,
										mainActivity.runningStatusFragment);
							} else if (position == 2) {
								mainActivity.tvActionName.setText("命令");
								mainActivity.switchContent(
										mainActivity.mContent,
										mainActivity.actionPlaceholderFragment);
							} else if (position == 3) {

								final String[] stringItems = { "软件说明", "应用信息",
										"云端帮助" };
								final ActionSheetDialog dialog = new ActionSheetDialog(
										mainActivity, stringItems, null);
								dialog.title(
										"操作\r\n(-------------------------------)")
										.isTitleShow(false)//
										.titleTextSize_SP(12.5f)//
										.show();

								dialog.setOnOperItemClickL(new OnOperItemClickL() {
									@Override
									public void onOperItemClick(
											AdapterView<?> parent, View view,
											int position, long id) {
										switch (position) {
										case 0:
											Intent intent = new Intent();
											intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
											intent.setClass(mainActivity,
													GuideActivity.class);
											// Bundle类用作携带数据，它类似于Map，用于存放key-value名值对形式的值
											Bundle b = new Bundle();
											b.putString("FuncName", "Guidance");
											// 此处使用putExtras，接受方就响应的使用getExtra
											intent.putExtras(b);
											startActivity(intent);
											break;
										case 1:
											/*
											 * mainActivity.d
											 * .ShowMaterialSimpleConfirmDialog
											 * ("还未开放，敬请期待!");
											 */
											PackageDetailUtil
													.showInstalledAppDetails(
															mainActivity,
															"net.cmono.getalarm");
											break;
										case 2:
											mainActivity.tvActionName
													.setText("远程帮助");
											mainActivity
													.switchContent(
															mainActivity.mContent,
															mainActivity.remoteHelpFragment);
										default:
											break;
										}
										dialog.dismiss();
									}
								});

							} else if (position == 4) {
								// 必须明确使用mailto前缀来修饰邮件地址,如果使用
								// intent.putExtra(Intent.EXTRA_EMAIL,
								// email)，结果将匹配不到任何应用
								Uri uri = Uri
										.parse("mailto:changweihua@outlook.com");
								// String[] email = { "changweihua@outlook.com"
								// };
								Intent intent = new Intent(
										Intent.ACTION_SENDTO, uri);
								// intent.putExtra(Intent.EXTRA_CC, email); //
								// 抄送人
								intent.putExtra(Intent.EXTRA_SUBJECT,
										"GetAlarm 意见反馈和咨询"); // 主题
								intent.putExtra(Intent.EXTRA_TEXT, "请填写内容"); // 正文
								startActivity(Intent.createChooser(intent,
										"请选择邮件类应用"));
							} else {
								Toast.makeText(mContext,
										"你点击了~" + position + "~项",
										Toast.LENGTH_SHORT).show();

							}

						}
					});

			return rootView;
		}

		public static Bitmap toRoundCorner(Bitmap bitmap, int pixels) {

			Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
					bitmap.getHeight(), Config.ARGB_8888);

			Canvas canvas = new Canvas(output);

			final int color = 0xff424242;

			final Paint paint = new Paint();

			final Rect rect = new Rect(0, 0, bitmap.getWidth(),
					bitmap.getHeight());

			final RectF rectF = new RectF(rect);

			final float roundPx = pixels;

			paint.setAntiAlias(true);

			canvas.drawARGB(0, 0, 0, 0);

			paint.setColor(color);

			canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

			paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));

			canvas.drawBitmap(bitmap, rect, rect, paint);
			return output;

		}

	}

	public static class RemoteHelpFragment extends LazyFragment {
		// 标志位，标志已经初始化完成。
		private boolean isPrepared;
		MainActivity mainActivity;
		WebView webView;
		View view;
		String loadingText = "";

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			view = inflater.inflate(R.layout.fragment_remote_help, container,
					false);
			mainActivity = (MainActivity) getActivity();
			loadingText = mainActivity.tvActionName.getText().toString();
			// XXX初始化view的各控件
			isPrepared = true;
			lazyLoad();
			return view;
		}

		@Override
		protected void lazyLoad() {
			if (isPrepared && isVisible) {
				return;
			}
			// mainActivity.d
			// .ShowMaterialSimpleConfirmDialog("开始执行");

			try {

				// 填充各控件的数据
				webView = (WebView) view.findViewById(R.id.webView);
				// 启用支持javascript
				WebSettings settings = webView.getSettings();
				settings.setJavaScriptEnabled(true);
				// 优先使用缓存
				webView.getSettings().setCacheMode(
						WebSettings.LOAD_CACHE_ELSE_NETWORK);

				// 覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
				webView.setWebViewClient(new WebViewClient() {
					@Override
					public boolean shouldOverrideUrlLoading(WebView wv,
							String url) {
						// TODO Auto-generated method stub
						// 返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
						wv.loadUrl(url);
						return true;
					}
				});

				webView.setWebChromeClient(new WebChromeClient() {
					@Override
					public void onProgressChanged(WebView wv, int newProgress) {
						// TODO Auto-generated method stub
						CrashReport.setJavascriptMonitor(wv, true);
						if (newProgress == 100) {
							// 网页加载完成
							mainActivity.tvActionName.setText(loadingText);
						} else {
							// 加载中
							mainActivity.tvActionName.setText(loadingText
									+ "  " + Integer.toString(newProgress)
									+ " %");
						}

					}
				});

				// WebView加载web资源
				webView.loadUrl("http://www.cmono.net/index.php/post/read/186");

			} catch (Throwable thr) {
				CrashReport.postCatchedException(thr); // bugly会将这个throwable上报
			}

		}

	}

	public static class AboutPlaceholderFragment extends Fragment {

		MainActivity mainActivity;

		LayoutParams params;
		WindowManager windowManager;
		Window window;
		BottomPopView bottomPopView;
		ImageView imageView;

		private static final int PHOTO_REQUEST_TAKEPHOTO = 1;// 拍照
		private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
		private static final int PHOTO_REQUEST_CUT = 3;// 结果
		// 创建一个以当前时间为名称的文件
		File tempFile = new File(Environment.getExternalStorageDirectory(),
				getPhotoFileName());

		// 使用系统当前日期加以调整作为照片的名称
		private String getPhotoFileName() {
			Date date = new Date(System.currentTimeMillis());
			SimpleDateFormat dateFormat = new SimpleDateFormat(
					"'IMG'_yyyyMMdd_HHmmss");
			return dateFormat.format(date) + ".jpg";
		}

		public AboutPlaceholderFragment() {
		}

		// private File mFile;// 存储图片的文件

		/**
		 * 向onActivityResult发出请求，的到拍摄生成的图片
		 */
		private void getPictureFromCamera() {
			Intent intent = new Intent();
			intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
			// 确定存储拍照得到的图片文件路径
			File temp = new File(ConstValue.ROOTFOLDER,
					System.currentTimeMillis() + ".jpg");
			mainActivity.mFile = temp;
			try {
				mainActivity.mFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
				CrashReport.postCatchedException(e);
			}
			// 加载Uri型的文件路径
			intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(temp));
			// 向onActivityResult发送intent，requestCode为GET_PIC_FROM_CAMERA
			mainActivity
					.startActivityForResult(intent, PHOTO_REQUEST_TAKEPHOTO);
		}

		private ShimmerTextView shimmerTv;
		private Shimmer shimmer;

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			final View rootView = inflater.inflate(R.layout.about_author,
					container, false);
			final View actionSheet = inflater.inflate(
					R.layout.bottom_pop_window, null);
			mainActivity = (MainActivity) getActivity();
			windowManager = mainActivity.getWindowManager();
			window = mainActivity.getWindow();
			params = mainActivity.getWindow().getAttributes();
			//hide the status bar
			//window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
	        //hide the title bar
			//window.requestFeature(Window.FEATURE_NO_TITLE);
			shimmerTv = (ShimmerTextView) rootView
					.findViewById(R.id.shimmer_tv);
			if (shimmer != null && shimmer.isAnimating()) {
				// shimmer.cancel();
			} else {
				shimmer = new Shimmer();
				shimmer.start(shimmerTv);
			}

			// 选择头像
			imageView = (ImageView) rootView.findViewById(R.id.imageView2);
			// imageView.setVisibility(ImageView.GONE);
			imageView.setOnClickListener(new ImageView.OnClickListener() {
				@Override
				public void onClick(View v) {

					// 底部弹出的布局 照相和选择图片
					bottomPopView = new BottomPopView(mainActivity, rootView) {
						@Override
						public void onTopButtonClick() {
							// 拍照
							// takePhoto();
							bottomPopView.dismiss();
							// // 调用系统的拍照功能
							// Intent intent = new Intent(
							// MediaStore.ACTION_IMAGE_CAPTURE);
							// // 指定调用相机拍照后照片的储存路径
							// intent.putExtra(MediaStore.EXTRA_OUTPUT,
							// Uri.fromFile(tempFile));
							// intent.putExtra("tempFile", tempFile);
							// mainActivity.startActivityForResult(intent,
							// PHOTO_REQUEST_TAKEPHOTO);

							getPictureFromCamera();

						}

						@Override
						public void onBottomButtonClick() {
							bottomPopView.dismiss();
							// // 选择本地图片
							// Intent intent = new Intent();
							// /* 开启Pictures画面Type设定为image */
							// intent.setType("image/*");
							// /* 使用Intent.ACTION_GET_CONTENT这个Action */
							// intent.setAction(Intent.ACTION_GET_CONTENT);
							// /* 取得相片后返回本画面 */
							// mainActivity.startActivityForResult(intent,
							// PHOTO_REQUEST_GALLERY);

							// 左起参数：选择行为权限，系统本地相册URI路径
							Intent i = new Intent(
									Intent.ACTION_PICK,
									android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
							// 向onActivityResult发送intent，requestCode为GET_PIC_FROM_GALLERY
							mainActivity.startActivityForResult(i,
									PHOTO_REQUEST_GALLERY);
						}
					};
					bottomPopView.setTopText("拍照");
					bottomPopView.setBottomText("选择图片");
					// 显示底部菜单
					bottomPopView.show();

					/*
					 * PopupWindow mPopupWindow = new PopupWindow(actionSheet,
					 * 720, LinearLayout.LayoutParams.WRAP_CONTENT);
					 * //监听PopupWindow的dismiss，当dismiss时屏幕恢复亮度
					 * mPopupWindow.setOnDismissListener(new
					 * PopupWindow.OnDismissListener() {
					 * 
					 * @Override public void onDismiss() {
					 * 
					 * params.alpha = 1.0f; window.setAttributes(params); } });
					 * mPopupWindow.setWidth(LayoutParams.MATCH_PARENT);
					 * mPopupWindow.setHeight(LayoutParams.WRAP_CONTENT);
					 * mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
					 * mPopupWindow.setTouchable(true);
					 * mPopupWindow.setFocusable(true);
					 * mPopupWindow.setOutsideTouchable(true); // 动画效果 从底部弹起
					 * mPopupWindow.setAnimationStyle(R.style.Anim_push); //显示窗口
					 * mPopupWindow.showAtLocation(rootView, Gravity.BOTTOM |
					 * Gravity.CENTER_HORIZONTAL, 0, 0);
					 * //设置layout在PopupWindow中显示的位置
					 */
					// startActivity(new
					// Intent(mainActivity.getBaseContext(),SelectPicPopupWindow.class));

				}

			});

			// 如果已经修改过头像

			String fileName = ConstValue.ROOTFOLDER + "avator.png";
			File file = new File(fileName);
			if (file.exists()) {
				FileInputStream fis;
				try {
					fis = new FileInputStream(fileName);
					Bitmap bitmap = BitmapFactory.decodeStream(fis);
					imageView.setImageBitmap(bitmap);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block new
					CrashReport.postCatchedException(e);
				}

			}

			TextView tvNickName = (TextView) rootView
					.findViewById(R.id.tvNickName);
			TextView tvpkgsV = (TextView) rootView
					.findViewById(R.id.tvpkgsV);
			TextView tvigsV = (TextView) rootView
					.findViewById(R.id.tvigsV);

			SharedPreferences sp = GetAlarmApplication.getAppContext()
					.getSharedPreferences(ConstValue.SETTING_PF,
							Context.MODE_PRIVATE);
			String nickName = sp.getString("nickName", "Lance Chang");
			tvNickName.setText(nickName);

			FrameLayout fLayout = (FrameLayout) rootView.findViewById(R.id.flm);

			Calendar c = Calendar.getInstance();
			int month = c.get(Calendar.MONTH);
			String bg = "bg/bg_month_" + String.format("%1$02d", month + 1)
					+ ".png";

			@SuppressWarnings("deprecation")
			Drawable d = new BitmapDrawable(
					mainActivity.getImageFromAssetsFile(bg));
			fLayout.setBackground(d);
			
			SharedPreferences sharedPreferences = GetAlarmApplication.getAppContext().getSharedPreferences(ConstValue.APP_PF, MODE_PRIVATE);
			
			String pkgsVersion = "通知展示列表版本: " + sharedPreferences.getString("pkgsV", "未知版本");
			String igsVersion = "通知忽略列表版本: " + sharedPreferences.getString("igsV", "未知版本");
			tvpkgsV.setText(pkgsVersion);
			tvigsV.setText(igsVersion);
			/*
			 * WebView wView = (WebView)
			 * rootView.findViewById(R.id.about_author);
			 * 
			 * wView.loadUrl("http://www.cmono.net/about.php"); WebSettings
			 * webSettings = wView.getSettings();
			 * webSettings.setJavaScriptEnabled(true);
			 */

			return rootView;
		}

	}

	public static class RunningStatusFragment extends Fragment {

		Activity mainActivity;

		/**
		 * 获取手机信息
		 * 
		 * @return
		 */
		@SuppressWarnings("deprecation")
		private String getPhoneInfo() {
			String phoneInfo = "产品名称: " + android.os.Build.PRODUCT;
			phoneInfo += "\nCPU型号: " + android.os.Build.CPU_ABI;
			phoneInfo += "\n标签: " + android.os.Build.TAGS;
			phoneInfo += "\n手机型号: " + android.os.Build.MODEL;
			phoneInfo += "\nSDK版本: " + android.os.Build.VERSION.SDK;
			phoneInfo += "\n系统版本: " + android.os.Build.VERSION.RELEASE;
			phoneInfo += "\n设备驱动: " + android.os.Build.DEVICE;
			phoneInfo += "\n显示: " + android.os.Build.DISPLAY;
			phoneInfo += "\n品牌: " + android.os.Build.BRAND;
			phoneInfo += "\n主板: " + android.os.Build.BOARD;
			phoneInfo += "\n指纹: " + android.os.Build.FINGERPRINT;
			phoneInfo += "\nID: " + android.os.Build.ID;
			phoneInfo += "\n制造商: " + android.os.Build.MANUFACTURER;
			phoneInfo += "\n用户组: " + android.os.Build.USER;

			return phoneInfo;
		}

		private String getWifiInfo() {
			WifiManager wifi = (WifiManager) mainActivity
					.getSystemService(Context.WIFI_SERVICE);
			WifiInfo info = wifi.getConnectionInfo();
			String maxText = info.getMacAddress();
			String ipText = intToIp(info.getIpAddress());
			String status = "";
			if (wifi.getWifiState() == WifiManager.WIFI_STATE_ENABLED) {
				status = "WIFI_STATE_ENABLED";
			}
			String ssid = info.getSSID();
			int networkID = info.getNetworkId();
			int speed = info.getLinkSpeed();
			return " mac：" + maxText + "\n\r" + "ip：" + ipText + "\n\r"
					+ "wifi status :" + status + "\n\r" + "ssid :" + ssid
					+ "\n\r" + "net work id :" + networkID + "\n\r"
					+ "connection speed:" + speed + "\n\r";
		}

		private String intToIp(int ip) {
			return (ip & 0xFF) + "." + ((ip >> 8) & 0xFF) + "."
					+ ((ip >> 16) & 0xFF) + "." + ((ip >> 24) & 0xFF);
		}

		public RunningStatusFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_running_status,
					container, false);

			mainActivity = getActivity();
			LoopView loopView = (LoopView) rootView.findViewById(R.id.loopView);
			loopView.setNotLoop();// 设置是否循环播放

			loopView.setPosition(5);// 设置初始位置

			loopView.setTextSize(30);// 设置字体大小

			ArrayList<String> list = new ArrayList();

			for (int i = 0; i < 15; i++) {

				list.add("item " + i);

			}

			loopView.setArrayList(list);

			// 滚动监听

			loopView.setListener(new LoopListener() {// 滚动停止后所在的Item

				@Override
				public void onItemSelect(int item) {
					final LoadToast lt = new LoadToast(mainActivity).setText(
							Integer.toString(item)).setTranslationY(100);
					lt.show();
					// lt.success();
				}

			});

			// 得到TabHost对象实例
			TabHost tabhost = (TabHost) rootView.findViewById(R.id.mtabhost);
			// 调用 TabHost.setup()
			tabhost.setup();

			final TabWidget tabWidget = tabhost.getTabWidget();
			tabWidget.setBackgroundColor(Color.WHITE);

			tabhost.setOnTabChangedListener(new OnTabChangeListener() {

				@Override
				public void onTabChanged(String tabId) {

					for (int i = 0; i < tabWidget.getChildCount(); i++) {

						TextView tv = (TextView) tabWidget.getChildAt(i)
								.findViewById(android.R.id.title);
						tv.setTextColor(Color.argb(255, 16, 179, 178));// 设置字体的颜色；

					}

					if (tabId.equalsIgnoreCase("one")) {
						((TextView) tabWidget.getChildAt(0).findViewById(
								android.R.id.title)).setTextColor(Color.argb(
								255, 142, 9, 80));
					} else if (tabId.equalsIgnoreCase("two")) {
						((TextView) tabWidget.getChildAt(1).findViewById(
								android.R.id.title)).setTextColor(Color.argb(
								255, 142, 9, 80));
					} else if (tabId.equalsIgnoreCase("three")) {
						((TextView) tabWidget.getChildAt(2).findViewById(
								android.R.id.title)).setTextColor(Color.argb(
								255, 142, 9, 80));
					}

				}
			});

			// 创建Tab标签
			tabhost.addTab(tabhost.newTabSpec("one").setIndicator("系统信息")
					.setContent(R.id.Tab1));
			tabhost.addTab(tabhost.newTabSpec("two").setIndicator("WIFI")
					.setContent(R.id.Tab2));
			tabhost.addTab(tabhost.newTabSpec("three").setIndicator("SIM")
					.setContent(R.id.Tab3));

			for (int i = 0; i < tabWidget.getChildCount(); i++) {
				// ImageView imageView =
				// (ImageView)tabWidget.getChildAt(i).findViewById(android.R.id.secondaryProgress);//获取控件imageView
				TextView tv = (TextView) tabWidget.getChildAt(i).findViewById(
						android.R.id.title);
				tv.setGravity(BIND_AUTO_CREATE);
				tv.setPadding(10, 10, 10, 10);

				// tv.setTextSize(16);// 设置字体的大小；
				tv.setTextColor(Color.argb(255, 16, 179, 178));// 设置字体的颜色；
				if (i == 0) {
					tv.setTextColor(Color.argb(255, 142, 9, 80));
				}
				// 修改背景
				tabWidget.getChildAt(i).setBackgroundResource(
						R.drawable.ab_transparent_light_holo);
				// 获取tabs图片；
				ImageView iv = (ImageView) tabWidget.getChildAt(i)
						.findViewById(android.R.id.icon);
			}

			String phoneInfo = "Product: " + android.os.Build.PRODUCT;

			TextView tvPhoneInfo = (TextView) rootView
					.findViewById(R.id.main_phoneinfo);
			tvPhoneInfo.setText(getPhoneInfo());

			// TextView tvWifiInfo = (TextView) rootView
			// .findViewById(R.id.wifi_info);
			// tvWifiInfo.setText(getWifiInfo());

			List<Map<String, Object>> listItems1 = (new WifiStatusUtil())
					.GetStatus();
			SimpleAdapter adapter1 = new SimpleAdapter(mainActivity,
					listItems1, R.layout.item_sim_info, new String[] { "name",
							"value" }, new int[] { R.id.SimInfoTitle,
							R.id.SimInfoValue });

			ListView lvWifiInfo = (ListView) rootView
					.findViewById(R.id.wifi_info);

			// 为listShow设置Adapter
			lvWifiInfo.setAdapter(adapter1);

			List<Map<String, Object>> listItems = (new TelephonyStatusUtil())
					.GetStatus();
			SimpleAdapter adapter = new SimpleAdapter(mainActivity, listItems,
					R.layout.item_sim_info, new String[] { "name", "value" },
					new int[] { R.id.SimInfoTitle, R.id.SimInfoValue });

			ListView lvSimInfo = (ListView) rootView
					.findViewById(R.id.sim_info);

			// 为listShow设置Adapter
			lvSimInfo.setAdapter(adapter);

			return rootView;
		}
	}

	public static class ConfigPlaceholderFragment extends Fragment {

		Activity mainActivity;

		public ConfigPlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragement_config,
					container, false);

			mainActivity = getActivity();

			return rootView;
		}
	}

	public static class ActionPlaceholderFragment extends Fragment {

		/**
		 * 网络操作相关的子线程
		 */
		Runnable networkTask = new Runnable() {

			@Override
			public void run() {
				// TODO
				// 在这里进行 http request.网络请求相关操作

				String m_szDevIDShort = "35"
						+ // we make this look like a valid IMEI

						Build.BOARD.length() % 10 + Build.BRAND.length() % 10
						+ Build.CPU_ABI.length() % 10 + Build.DEVICE.length()
						% 10 + Build.DISPLAY.length() % 10
						+ Build.HOST.length() % 10 + Build.ID.length() % 10
						+ Build.MANUFACTURER.length() % 10
						+ Build.MODEL.length() % 10 + Build.PRODUCT.length()
						% 10 + Build.TAGS.length() % 10 + Build.TYPE.length()
						% 10 + Build.USER.length() % 10; // 13 digits

				JSONObject json = new JSONObject();
				try {
					json.put("username", "test2");
					json.put("userpwd", "1234567");
					json.put("userid", m_szDevIDShort);

					HttpParams httpParams = new BasicHttpParams();
					HttpConnectionParams
							.setConnectionTimeout(httpParams, 10000);
					HttpConnectionParams.setSoTimeout(httpParams, 10000);
					HttpClient client = new DefaultHttpClient(httpParams);
					String url = "http://www.cmono.net/index.php/guser/create";

					HttpPost request = new HttpPost(url);
					request.setEntity(new ByteArrayEntity(json.toString()
							.getBytes("UTF8")));
					request.setHeader("json", json.toString());
					HttpResponse response = client.execute(request);
					HttpEntity entity = response.getEntity();
					String result = "";
					// 创建一个JSON对象
					JSONObject jsonObject = new JSONObject(
							EntityUtils.toString(entity));
					result = URLDecoder.decode(jsonObject.getString("msg"),
							"UTF-8");

					Message msg = new Message();
					Bundle data = new Bundle();
					data.putString("value", result);
					msg.setData(data);
					handler.sendMessage(msg);
				} catch (JSONException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					CrashReport.postCatchedException(e);
				}
			}
		};

		public String convert(String utfString) {
			StringBuilder sb = new StringBuilder();
			int i = -1;
			int pos = 0;

			while ((i = utfString.indexOf("\\u", pos)) != -1) {
				sb.append(utfString.substring(pos, i));
				if (i + 5 < utfString.length()) {
					pos = i + 6;
					sb.append((char) Integer.parseInt(
							utfString.substring(i + 2, i + 6), 16));
				}
			}

			return sb.toString();
		}

		Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				Bundle data = msg.getData();
				String val = data.getString("value");
				// TODO
				// UI界面的更新等相关操作
				mainActivity.d.ShowMaterialSimpleConfirmDialog("" + val);
			}
		};

		private static String convertStreamToString(InputStream is) {
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(is));
			StringBuilder sb = new StringBuilder();
			String line = null;
			try {
				while ((line = reader.readLine()) != null) {
					sb.append((line + "\n"));
				}
			} catch (IOException e) {
				e.printStackTrace();
				CrashReport.postCatchedException(e);
			} finally {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
					CrashReport.postCatchedException(e);
				}
			}
			return sb.toString();
		}

		MainActivity mainActivity;

		private BorderedGridView gridview;
		private CalendarView calendarView;

		final String[] arrayCfgs = new String[] { "辅助功能设置", "选择活动", "APN设置",
				"应用程序设置", "设置GSM/UMTS波段", "电池信息", "日期和坝上旅游网时间设置", "日期和时间设置",
				"应用程序设置=》开发设置", "设备管理器", "关于手机", "显示——设置显示字体大小及预览", "显示设置",
				"底座设置", "SIM卡锁定设置", "语言和键盘设置", "语言和键盘设置", "选择手机语言", "选择手机语言",
				"已下载（安装）软件列表", "恢复出厂设置", "格式化手机闪存", "设置键盘", "隐私设置", "代理设置",
				"手机信息", "正在运行的程序（服务）", " 位置和安全设置", "系统设置", "安全信息", "声音设置",
				"测试——显示手机信息、电池信息、使用情况统计、Wifi information、服务信息", "绑定与便携式热点",
				"文字转语音设置", "使用情况统计", "用户词典", "语音输入与输出设置", "无线和网络设置" };

		final String[] arrayCfgPaths = new String[] {
				"com.android.settings.AccessibilitySettings",
				"com.android.settings.ActivityPicker",
				"com.android.settings.ApnSettings",
				"com.android.settings.ApplicationSettings",
				"com.android.settings.BandMode",
				"com.android.settings.BatteryInfo",
				"com.android.settings.DateTimeSettings",
				"com.android.settings.DateTimeSettingsSetupWizard",
				"com.android.settings.DevelopmentSettings",
				"com.android.settings.DeviceAdminSettings",
				"com.android.settings.DeviceInfoSettings",
				"com.android.settings.Display",
				"com.android.settings.DisplaySettings",
				"com.android.settings.DockSettings",
				"com.android.settings.IccLockSettings",
				"com.android.settings.InstalledAppDetails",
				"com.android.settings.LanguageSettings",
				"com.android.settings.LocalePicker",
				"com.android.settings.LocalePickerInSetupWizard",
				"com.android.settings.ManageApplications",
				"com.android.settings.MasterClear",
				"com.android.settings.MediaFormat",
				"com.android.settings.PhysicalKeyboardSettings",
				"com.android.settings.PrivacySettings",
				"com.android.settings.ProxySelector",
				"com.android.settings.RadioInfo",
				"com.android.settings.RunningServices",
				"com.android.settings.SecuritySettings",
				"com.android.settings.Settings",
				"com.android.settings.SettingsSafetyLegalActivity",
				"com.android.settings.SoundSettings",
				"com.android.settings.TestingSettings",
				"com.android.settings.TetherSettings",
				"com.android.settings.TextToSpeechSettings",
				"com.android.settings.UsageStats",
				"com.android.settings.UserDictionarySettings",
				"com.android.settings.VoiceInputOutputSettings",
				"com.android.settings.WirelessSettings" };

		public ActionPlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_action,
					container, false);

			mainActivity = (MainActivity) getActivity();

			gridview = (BorderedGridView) rootView.findViewById(R.id.gridview);
			gridview.setAdapter(new BorderedGridAdapter(mainActivity));
			calendarView = (CalendarView) rootView
					.findViewById(R.id.calendarView1);
			calendarView.setShowWeekNumber(false);
			Resources res = GetAlarmApplication.getAppContext().getResources();
			Drawable bg = res.getDrawable(R.drawable.calendar_bg);
			calendarView.setBackground(bg);
			calendarView.setFirstDayOfWeek(Calendar.MONDAY);
			calendarView.setSelectedWeekBackgroundColor(Color.TRANSPARENT);
			calendarView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					mainActivity.d
							.ShowMaterialSimpleConfirmDialog("Long Click");
				}

			});

			gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					Intent intents = new Intent();
					switch (position) {
					case 0:
						(new SpecialDayDialog(mainActivity)).show();
						break;
					case 1:
						// 开启一个子线程，进行网络操作，等待有返回结果，使用handler通知UI

						// TelephonyManager TelephonyMgr =
						// (TelephonyManager)mainActivity.getSystemService(TELEPHONY_SERVICE);
						// String m_szImei = TelephonyMgr.getDeviceId();
						//
						// mainActivity.d.ShowMaterialSimpleConfirmDialog(""
						// + m_szDevIDShort+"\r\n"+m_szImei);

						new Thread(networkTask).start();
						break;
					case 2:
						// mainActivity.tvActionName
						// .setText("来电设置");
						// mainActivity
						// .switchContent(
						// mainActivity.mContent,
						// mainActivity.incallSettingPreferenceFragment);
						(new IncallSettingDialog(mainActivity)).show();
						// Intent intent = new Intent();
						// intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
						// intent.setClass(mainActivity,
						// CustomerPreferenceActivity.class);
						// startActivity(intent);
						break;

					case 3:
						// Intent intents = new Intent();
						// intents.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						// intents.setAction(Settings.ACTION_ACCESSIBILITY_SETTINGS);
						// startActivity(intents);
						Dialog alertDialog = new AlertDialog.Builder(
								mainActivity)
								.setTitle("选择设置")
								.setIcon(R.drawable.ic_feedback_settings)
								.setItems(arrayCfgs,
										new DialogInterface.OnClickListener() {

											@Override
											public void onClick(
													DialogInterface dialog,
													int which) {
												// Toast.makeText(mainActivity,
												// arrayCfgPaths[which],
												// Toast.LENGTH_SHORT)
												// .show();

												Intent cfgIntent = new Intent(
														"/");
												ComponentName cm = new ComponentName(
														"com.android.settings",
														arrayCfgPaths[which]);
												cfgIntent.setComponent(cm);
												cfgIntent
														.setAction("android.intent.action.VIEW");
												startActivity(cfgIntent);
											}
										})
								.setNegativeButton("取消",
										new DialogInterface.OnClickListener() {

											@Override
											public void onClick(
													DialogInterface dialog,
													int which) {
												// TODO Auto-generated method
												// stub
											}
										}).create();
						alertDialog.show();
						break;
					case 4:
						intents = new Intent();
						intents.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						intents.setAction(Settings.ACTION_SYNC_SETTINGS);
						startActivity(intents);
						break;
					case 5:
						intents = new Intent();
						intents.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						intents.setAction(Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS);
						startActivity(intents);
						break;
					case 6:
						(new NotificationSettingDialog(mainActivity)).show();
						break;
					case 7:
						// intents = new Intent();
						// intents.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						// intents.setAction(Settings.ACTION_ACCESSIBILITY_SETTINGS);
						// startActivity(intents);
						NormalDialog dialog = new NormalDialog(mainActivity);
						dialog.setCancelable(false);
						dialog.isTitleShow(false)
								//
								.bgColor(Color.parseColor("#383838"))
								.cornerRadius(5)
								.content("是否确定退出程序?")
								.contentGravity(Gravity.CENTER)
								.contentTextColor(Color.parseColor("#ffffff"))
								.dividerColor(Color.parseColor("#222222"))
								.btnTextSize(15.5f, 15.5f)
								.btnTextColor(Color.parseColor("#ffffff"),
										Color.parseColor("#ffffff"))//
								.btnPressColor(Color.parseColor("#2B2B2B"))//
								.widthScale(0.85f)//
								.show();
						List<NotificationInfo> unReadNotis = DataSupport.where(
								"status = ? order by date desc", "0").find(
								NotificationInfo.class);
						// List<NotificationInfo> notis = DataSupport
						// .findAll(NotificationInfo.class);
						List<NotificationInfo> notis = DataSupport.order(
								"date desc").find(NotificationInfo.class);
						Gson gson = new Gson();
						String json = gson.toJson(notis);
						String fName = ConstValue.DATEFORMAT.format(new Date());
						String FileName = ConstValue.ROOTFOLDER + fName
								+ ".json";
						// 结果保存到文件中
						FileUtil.saveFile(json, FileName);
						break;
					case 8:

						AppSettingDialog appSettingDialog = new AppSettingDialog(
								mainActivity);
						appSettingDialog.setCancelable(false);
						appSettingDialog.show();
						break;
					default:
						mainActivity.d.ShowMaterialSimpleConfirmDialog(""
								+ position);
						break;
					}

				}
			});

			return rootView;
		}
	}
}
