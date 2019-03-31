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
 * ���ܣ�ʹ��ViewPagerʵ�ֳ��ν���Ӧ��ʱ������ҳ
 * 
 * (1)�ж��Ƿ����״μ���Ӧ��--��ȡ��ȡSharedPreferences�ķ��� (2)�ǣ����������activity���������MainActivity
 * (3)5s��ִ��(2)����
 * 
 * 
 */
public class SplashActivity extends Activity {

	// ���ض���
	private static int progress;
	private static LoadingProgressView loadingProgressView;
	private static final int DOWNLOAD_UPDATE = 0x99;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// ���ر�����
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// ����״̬��
		// ����ȫ������
		int flag = WindowManager.LayoutParams.FLAG_FULLSCREEN;
		// ��õ�ǰ�������
		Window window = SplashActivity.this.getWindow();
		// ���õ�ǰ����Ϊȫ����ʾ
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

			// �Ѿ���¼
			if (GetAlarmApplication.isEnter()) {
				Intent mIntent = new Intent();
				mIntent.setClass(SplashActivity.this, MainActivity.class);
				SplashActivity.this.startActivity(mIntent);
				SplashActivity.this.finish();
			} else {
				// ���Ĳ���һ��Ҫ�ͷ��ͷ�����ͬ������õ���ֵ
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
					// ��handler����һ����ʱ����Ϣ��1000�������
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
			// ��progress����maxProgressʱ�����ٵ���һ�·���
			if (progress <= loadingProgressView.getMaxProgress()) {
				loadingProgressView.setProgress(progress);// �����µĽ���
				loadingHandler.sendEmptyMessageDelayed(DOWNLOAD_UPDATE, 10);// ÿ��100���뷢��һ��handler
				return;
			}
			message.what = HANDLER_SUCCESS;
			progress = 0;
			loadingHandler.sendMessageDelayed(message, 0);
		}
	};
	private final Thread mThread = new Thread(mRunnable);

	// ģ������
	private Handler mLoadingHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			// ����msg
			switch (msg.what) {
			case DOWNLOAD_UPDATE:
				progress += 1;
				// ��progress����maxProgressʱ�����ٵ���һ�·���
				if (progress <= loadingProgressView.getMaxProgress()) {
					loadingProgressView.setProgress(progress);// �����µĽ���
					sendEmptyMessageDelayed(DOWNLOAD_UPDATE, 10);// ÿ��10���뷢��һ��handler
				} else {
					mHandler.sendEmptyMessageDelayed(SWITCH_MAINACTIVITY, 100);
				}
				break;
			}
		}
	};
	// --------------------------------------------

	// ****************************************************************
	// �ж�Ӧ���Ƿ���μ��أ���ȡSharedPreferences�е�guide_activity�ֶ�
	// ****************************************************************
	private static final String SHAREDPREFERENCES_NAME = ConstValue.INIT_PF;
	private static final String KEY_GUIDE_ACTIVITY = "activity_guide";

	private boolean isFirstEnter(Context context, String className) {
		if (context == null || className == null
				|| "".equalsIgnoreCase(className))
			return false;
		String mResultStr = context.getSharedPreferences(
				SHAREDPREFERENCES_NAME, Context.MODE_WORLD_READABLE).getString(
				KEY_GUIDE_ACTIVITY, "");// ȡ���������� �� com.my.MainActivity
		if (mResultStr.equalsIgnoreCase("false"))
			return false;
		else
			return true;
	}

	// *************************************************
	// Handler:��ת����ͬҳ��
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