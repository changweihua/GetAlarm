package net.cmono.getalarm;

import java.lang.ref.WeakReference;

import net.cmono.consts.ConstValue;
import net.cmono.exts.LoadingProgressView;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;
import android.view.WindowManager;

/**
 * 功能：使用ViewPager实现初次进入应用时的引导页
 * 
 * (1)判断是否是首次加载应用--采取读取SharedPreferences的方法 (2)是，则进入引导activity；否，则进入MainActivity
 * (3)5s后执行(2)操作
 * 
 * 
 */
public class SplashActivity extends Activity {

	// 加载动画
	private static int progress;
	private static LoadingProgressView loadingProgressView;
	private static final int DOWNLOAD_UPDATE = 0x99;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// 隐藏标题栏
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 隐藏状态栏
		// 定义全屏参数
		int flag = WindowManager.LayoutParams.FLAG_FULLSCREEN;
		// 获得当前窗体对象
		Window window = SplashActivity.this.getWindow();
		// 设置当前窗体为全屏显示
		window.setFlags(flag, flag);

		boolean mFirst = isFirstEnter(SplashActivity.this, SplashActivity.this
				.getClass().getName());

		// Toast.makeText(this, mFirst + "", Toast.LENGTH_SHORT).show();
		if (mFirst) {
			mHandler.sendEmptyMessageDelayed(SWITCH_GUIDACTIVITY, 100);
			SharedPreferences sharedPreferences = this.getSharedPreferences(
					ConstValue.INIT_PF, MODE_PRIVATE);
			sharedPreferences.edit().putString("activity_guide", "false")
					.commit();
		} else {

			// 已经登录
			if (GetAlarmApplication.isEnter()) {
				Intent mIntent = new Intent();
				mIntent.setClass(SplashActivity.this, MainActivity.class);
				SplashActivity.this.startActivity(mIntent);
				SplashActivity.this.finish();
			} else {
				// 最后的参数一定要和发送方的相同，否则得到空值
				Boolean loginResult = getIntent().getExtras() == null ? false
						: getIntent().getExtras().getBoolean("LoignResult",
								false);

				if (!loginResult) {
					Intent mIntent = new Intent();
					mIntent.setClass(SplashActivity.this, LoginActivity.class);
					SplashActivity.this.startActivity(mIntent);
					SplashActivity.this.finish();
				} else {
					setContentView(R.layout.activity_splash);
					loadingProgressView = (LoadingProgressView) findViewById(R.id.progress_view_first);
					// 向handler发送一个延时空消息，1000毫秒后发送
					mLoadingHandler
							.sendEmptyMessageDelayed(DOWNLOAD_UPDATE, 10);
					GetAlarmApplication.putSession(true);
				}
			}

		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {

		}
	}

	// -------------------------------------

	private static final int HANDLER_SUCCESS = 206;

	private static class LoadingHandler extends Handler {
		private final WeakReference<SplashActivity> mActivity;

		public LoadingHandler(SplashActivity activity) {
			mActivity = new WeakReference<>(activity);
		}

		/**
		 * Subclasses must implement this to receive messages.
		 * 
		 * @param msg
		 */
		@Override
		public void handleMessage(Message msg) {
			SplashActivity activity = this.mActivity.get();
			if (activity != null) {
				switch (msg.what) {
				case HANDLER_SUCCESS: {
					
					break;
				}
				default: {

				}
				}
			}
		}
	}

	private final LoadingHandler loadingHandler = new LoadingHandler(
			SplashActivity.this);

	private final Runnable mRunnable = new Runnable() {
		@Override
		public void run() {
			Message message = loadingHandler.obtainMessage();
			progress += 1;
			// 当progress大于maxProgress时，不再调用一下方法
			if (progress <= loadingProgressView.getMaxProgress()) {
				loadingProgressView.setProgress(progress);// 设置新的进度
				loadingHandler.sendEmptyMessageDelayed(DOWNLOAD_UPDATE, 10);// 每隔100毫秒发送一次handler
				return;
			}
			message.what = HANDLER_SUCCESS;
			progress = 0;
			loadingHandler.sendMessageDelayed(message, 0);
		}
	};
	private final Thread mThread = new Thread(mRunnable);

	// 模拟下载
	private Handler mLoadingHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			// 处理msg
			switch (msg.what) {
			case DOWNLOAD_UPDATE:
				progress += 1;
				// 当progress大于maxProgress时，不再调用一下方法
				if (progress <= loadingProgressView.getMaxProgress()) {
					loadingProgressView.setProgress(progress);// 设置新的进度
					sendEmptyMessageDelayed(DOWNLOAD_UPDATE, 10);// 每隔10毫秒发送一次handler
				} else {
					mHandler.sendEmptyMessageDelayed(SWITCH_MAINACTIVITY, 100);
				}
				break;
			}
		}
	};
	// --------------------------------------------

	// ****************************************************************
	// 判断应用是否初次加载，读取SharedPreferences中的guide_activity字段
	// ****************************************************************
	private static final String SHAREDPREFERENCES_NAME = ConstValue.INIT_PF;
	private static final String KEY_GUIDE_ACTIVITY = "activity_guide";

	private boolean isFirstEnter(Context context, String className) {
		if (context == null || className == null
				|| "".equalsIgnoreCase(className))
			return false;
		String mResultStr = context.getSharedPreferences(
				SHAREDPREFERENCES_NAME, Context.MODE_WORLD_READABLE).getString(
				KEY_GUIDE_ACTIVITY, "");// 取得所有类名 如 com.my.MainActivity
		if (mResultStr.equalsIgnoreCase("false"))
			return false;
		else
			return true;
	}

	// *************************************************
	// Handler:跳转至不同页面
	// *************************************************
	private final static int SWITCH_MAINACTIVITY = 1000;
	private final static int SWITCH_GUIDACTIVITY = 1001;
	public Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SWITCH_MAINACTIVITY:
				Intent mIntent = new Intent();
				mIntent.setClass(SplashActivity.this, MainActivity.class);
				SplashActivity.this.startActivity(mIntent);
				SplashActivity.this.finish();
				break;
			case SWITCH_GUIDACTIVITY:
				mIntent = new Intent();
				Bundle b = new Bundle();
				b.putString("FuncName", "FirstLoad");
				mIntent.putExtras(b);
				mIntent.setClass(SplashActivity.this, GuideActivity.class);
				SplashActivity.this.startActivity(mIntent);
				SplashActivity.this.finish();
				break;
			}
			super.handleMessage(msg);
		}
	};
}