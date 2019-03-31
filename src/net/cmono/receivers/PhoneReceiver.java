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
		// �����ȥ��
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
			// ������android�ĵ���ò��û��ר�����ڽ��������action,���ԣ���ȥ�缴����.
			// ���������Ҫ�����绰�Ĳ���״������Ҫ��ô���� :
			/*
			 * ��һ����ȡ�绰���������TelephonyManager manager =
			 * this.getSystemService(TELEPHONY_SERVICE);
			 * �ڶ���ͨ��TelephonyManagerע������Ҫ�����ĵ绰״̬�ı��¼���manager.listen(new
			 * MyPhoneStateListener(),
			 * PhoneStateListener.LISTEN_CALL_STATE);�����PhoneStateListener
			 * .LISTEN_CALL_STATE����������Ҫ ������״̬�ı��¼�������֮�⣬���кܶ������¼�Ŷ�� ��������ͨ��extends
			 * PhoneStateListener�������Լ��Ĺ��򡣽�����󴫵ݸ��ڶ�����Ϊ������
			 * ���Ĳ�����һ������Ҫ���Ǿ��Ǹ�Ӧ�����Ȩ�ޡ�android.permission.READ_PHONE_STATE
			 */

			TelephonyManager tm = (TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);
			tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
			// ����һ��������
		}
	}

	SharedPreferences prefs = GetAlarmApplication.getContext()
			.getSharedPreferences(ConstValue.INCALL_PF, Context.MODE_PRIVATE);

	private SensorManager manager = null;
	private SensorEventListener sensorEventListener = null;
	private Sensor sensor = null;

	private boolean isRegisted = false;// �Ƿ��Ѿ�ע�����
	private boolean isUnregisted = true;// �Ƿ��Ѿ�ȡ������

	private int volume;
	private int mode;

	/**
	 * ��ȡϵͳ��ǰ�龰ģʽ
	 * 
	 * @param audio
	 */
	private void getInitring(AudioManager audio) {
		// ȡ���ֻ��ĳ�ʼ����������ʼ��������
		volume = audio.getStreamVolume(AudioManager.STREAM_RING); // ȡ�ó�ʼ����
		// ȡ�ó�ʼģʽ�����ֱ�����ͼ��
		mode = audio.getRingerMode(); // ȡ�ó�ʼģʽ
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
			// ע�⣬��������д��super�������棬����incomingNumber�޷���ȡ��ֵ��
			super.onCallStateChanged(state, incomingNumber);
			Editor editor = prefs.edit();
			switch (state) {
			case TelephonyManager.CALL_STATE_IDLE:
				// System.out.println("�Ҷ�");
				editor.putBoolean("enableMsg", true);
				GetAlarmApplication.screenLocked = true;
			case TelephonyManager.CALL_STATE_OFFHOOK:
				// System.out.println("����");
				editor.putBoolean("enableMsg", false);
				GetAlarmApplication.screenLocked = true;
				if (!isUnregisted && isRegisted) {
					manager.unregisterListener(sensorEventListener);
					isUnregisted = true;
					isRegisted = false;
				}
				break;
			case TelephonyManager.CALL_STATE_RINGING:
				// System.out.println("����:�������" + incomingNumber);
				editor.putBoolean("enableMsg", false);
				GetAlarmApplication.screenLocked = true;
				if (isUnregisted && prefs.getBoolean("enableFlipSlient", false)) {
					initSensor();
					manager.registerListener(sensorEventListener, sensor,
							SensorManager.SENSOR_DELAY_GAME);
					isRegisted = true;
					isUnregisted = false;
				}
				// ����������
				break;
			}
			editor.commit();
		}
	};
}