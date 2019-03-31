package net.cmono.svrs;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

public class SensorFunctionServiceIncall extends Service {

	private static final String LOG_TAG = "InCallActivity/SensorFunctionServiceIncall";
	private static final float CRITICAL_DOWN_ANGLE = -5.0f;
	private static final float CRITICAL_UP_ANGLE = 5.0f;
	private static final int Z_ORATIATION = 2;

	private SensorManager mSensorManager;
	private Sensor mGsensor;
	private SensorEventListener mGsensorListener;
	private PowerManager pm;
	private int mReverseDownFlg = -1;

	private int previousMuteMode = -1;

	private boolean mActFlag = false;

	@Override
	public void onCreate() {
		super.onCreate();
		pm = (PowerManager) getSystemService(Context.POWER_SERVICE);

		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		mGsensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);// TYPE_GRAVITY);

		Log.d(LOG_TAG, "onCreate()...  this = " + this);

		mGsensorListener = new SensorEventListener() {

			@Override
			public void onAccuracyChanged(Sensor sensor, int accuracy) {
			}

			@Override
			public void onSensorChanged(SensorEvent event) {

				// Log.d(LOG_TAG, "onSensorChanged()...  event = " + event);

				Log.d(LOG_TAG,
						"onSensorChanged()...  event.values[SensorManager.DATA_Z] = "
								+ event.values[SensorManager.DATA_Z]);

				if (event.values[SensorManager.DATA_Z] >= CRITICAL_UP_ANGLE) { // screen
																				// up
																				// first
					mReverseDownFlg = 0;

				} else if (event.values[SensorManager.DATA_Z] <= CRITICAL_DOWN_ANGLE
						&& mReverseDownFlg == 0) { // screen down next
					mReverseDownFlg = 1;
				}

				if (mReverseDownFlg == 1) { // screen reverse from up to down
					if (mActFlag == false) {
						mActFlag = true;
						/*
						 * AudioManager audioManager = (AudioManager)
						 * getSystemService(Context.AUDIO_SERVICE);
						 * audioManager.
						 * setRingerMode(AudioManager.RINGER_MODE_SILENT);
						 */

						AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
						if (previousMuteMode == -1) {
							previousMuteMode = am.getRingerMode();
							am.setRingerMode(0);
						}
						am.setRingerMode(previousMuteMode);
						previousMuteMode = -1;

						Log.d(LOG_TAG, "onSensorChanged()...  mActFlag = "
								+ mActFlag);
					}
				}
			}
		};

		mSensorManager.registerListener(mGsensorListener, mGsensor,
				SensorManager.SENSOR_DELAY_GAME);
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

}