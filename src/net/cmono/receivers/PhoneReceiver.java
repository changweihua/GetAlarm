package net.cmono.receivers;

import org.litepal.LitePalApplication;

import net.cmono.consts.ConstValue;
import net.cmono.getalarm.GetAlarmApplication;
import net.cmono.utils.Logger;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.Ringtone;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

public class PhoneReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		System.out.println("action" + intent.getAction());
		// 如果是去电
		if (intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)) {
			String phoneNumber = intent
					.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
			Logger.d("PhoneReceiver", "call OUT:" + phoneNumber);
			Editor editor = prefs.edit();
			editor.putBoolean("enableMsg", false);
			GetAlarmApplication.screenLocked = true;
			editor.commit();
		} else if (intent.getAction().equals(Intent.ACTION_ANSWER)) {
			String phoneNumber = intent
					.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
			Logger.d("PhoneReceiver", "call In:" + phoneNumber);
			Editor editor = prefs.edit();
			editor.putBoolean("enableMsg", false);
			GetAlarmApplication.screenLocked = false;
			editor.commit();
		} else {
			// 查了下android文档，貌似没有专门用于接收来电的action,所以，非去电即来电.
			// 如果我们想要监听电话的拨打状况，需要这么几步 :
			/*
			 * 第一：获取电话服务管理器TelephonyManager manager =
			 * this.getSystemService(TELEPHONY_SERVICE);
			 * 第二：通过TelephonyManager注册我们要监听的电话状态改变事件。manager.listen(new
			 * MyPhoneStateListener(),
			 * PhoneStateListener.LISTEN_CALL_STATE);这里的PhoneStateListener
			 * .LISTEN_CALL_STATE就是我们想要 监听的状态改变事件，初次之外，还有很多其他事件哦。 第三步：通过extends
			 * PhoneStateListener来定制自己的规则。将其对象传递给第二步作为参数。
			 * 第四步：这一步很重要，那就是给应用添加权限。android.permission.READ_PHONE_STATE
			 */

			TelephonyManager tm = (TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);
			tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
			// 设置一个监听器
		}
	}

	SharedPreferences prefs = GetAlarmApplication.getContext()
			.getSharedPreferences(ConstValue.INCALL_PF, Context.MODE_PRIVATE);

	private SensorManager manager = null;
	private SensorEventListener sensorEventListener = null;
	private Sensor sensor = null;

	private boolean isRegisted = false;// 是否已经注册服务
	private boolean isUnregisted = true;// 是否已经取消服务

	private int volume;
	private int mode;

	/**
	 * 获取系统当前情景模式
	 * 
	 * @param audio
	 */
	private void getInitring(AudioManager audio) {
		// 取得手机的初始音量，并初始化进度条
		volume = audio.getStreamVolume(AudioManager.STREAM_RING); // 取得初始音量
		// 取得初始模式，并分别设置图标
		mode = audio.getRingerMode(); // 取得初始模式
	}

	private AudioManager audioManager = (AudioManager) GetAlarmApplication
			.getContext().getSystemService(Context.AUDIO_SERVICE);

	boolean mGoUp = false;
	Ringtone mRinger = null;

	private void initSensor() {

		getInitring(audioManager);

		sensorEventListener = new SensorEventListener() {
			@Override
			public void onAccuracyChanged(Sensor s, int accuracy) {
			}

			@Override
			public void onSensorChanged(SensorEvent event) {
				float x = event.values[SensorManager.DATA_X];
				float y = event.values[SensorManager.DATA_Y];
				float z = event.values[SensorManager.DATA_Z];

				if (x < 1 && x > -1 && y < 1 && y > -1) {

					if (z > 0) {
						mGoUp = true;
					} else {
						mGoUp = false;
					}
				} else {
					// if (x > 1 || x < -1 || y > 1 || y < -1 ) {

					if (z > 0 && !mGoUp) {
						audioManager.setRingerMode(mode);
					} else if (z < 0 && mGoUp) {
						audioManager
								.setRingerMode(AudioManager.RINGER_MODE_SILENT);
					} else {
						audioManager.setRingerMode(mode);
					}

					// }

				}

			}
		};

		manager = (SensorManager) LitePalApplication.getContext()
				.getSystemService(Context.SENSOR_SERVICE);
		sensor = manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
	}

	PhoneStateListener listener = new PhoneStateListener() {

		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			// 注意，方法必须写在super方法后面，否则incomingNumber无法获取到值。
			super.onCallStateChanged(state, incomingNumber);
			Editor editor = prefs.edit();
			switch (state) {
			case TelephonyManager.CALL_STATE_IDLE:
				// System.out.println("挂断");
				editor.putBoolean("enableMsg", true);
				GetAlarmApplication.screenLocked = true;
			case TelephonyManager.CALL_STATE_OFFHOOK:
				// System.out.println("接听");
				editor.putBoolean("enableMsg", false);
				GetAlarmApplication.screenLocked = true;
				if (!isUnregisted && isRegisted) {
					manager.unregisterListener(sensorEventListener);
					isUnregisted = true;
					isRegisted = false;
				}
				break;
			case TelephonyManager.CALL_STATE_RINGING:
				// System.out.println("响铃:来电号码" + incomingNumber);
				editor.putBoolean("enableMsg", false);
				GetAlarmApplication.screenLocked = true;
				if (isUnregisted && prefs.getBoolean("enableFlipSlient", false)) {
					initSensor();
					manager.registerListener(sensorEventListener, sensor,
							SensorManager.SENSOR_DELAY_GAME);
					isRegisted = true;
					isUnregisted = false;
				}
				// 输出来电号码
				break;
			}
			editor.commit();
		}
	};
}