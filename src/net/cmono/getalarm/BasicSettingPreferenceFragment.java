package net.cmono.getalarm;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import net.cmono.consts.ConstValue;
import net.cmono.utils.Logger;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceFragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class BasicSettingPreferenceFragment extends PreferenceFragment {

	SharedPreferences prefs;
	EditTextPreference editTextPreferenceNickName;
	CheckBoxPreference checkBoxPreferenceShowSIMInfo;
	CheckBoxPreference checkBoxPreferenceShowNickName;
	EditTextPreference editTextPreferenceWord;
	CheckBoxPreference checkBoxPreferenceShowWord;
	CheckBoxPreference checkBoxPreferenceAutoWord;
	CheckBoxPreference checkBoxPreferenceShowSpecialDay;
	CheckBoxPreference checkBoxPreferenceShowMsg;
	CheckBoxPreference checkBoxPreferenceShowBatterCharging;
	CheckBoxPreference checkBoxPreferenceShowBatteryLosing;
	CheckBoxPreference checkBoxPreferenceShowWeekDay;
	CheckBoxPreference checkBoxPreferenceWordMode;
	CheckBoxPreference checkBoxPreferenceShowUnlockInfo;
	CheckBoxPreference checkBoxPreferenceShowHoroscope;
	CheckBoxPreference checkBoxPreferenceShowFish;
	CheckBoxPreference checkBoxPreferenceShowKJGJP;
	CheckBoxPreference checkBoxPreferenceShowTraditionalDayCover;
	ListPreference listPreferenceMyLocation;
	ListPreference listPreferenceMyHoroscope;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		final View rootView = super.onCreateView(inflater, container,
				savedInstanceState);
		addPreferencesFromResource(R.xml.basic_setting);

		Resources res = getResources();
		String[] horoscopes = res.getStringArray(R.array.horoscopes);
		String[] horoscopeENs = res.getStringArray(R.array.horoscopeENs);

		final HashMap<String, String> horoscopeMaps = new HashMap<String, String>();

		for (int i = 0; i < horoscopeENs.length; i++) {
			horoscopeMaps.put(horoscopeENs[i], horoscopes[i]);
		}

		prefs = rootView.getContext().getSharedPreferences(
				ConstValue.SETTING_PF, Context.MODE_PRIVATE);// PreferenceManager.getDefaultSharedPreferences(rootView.getContext());
		// 昵称
		editTextPreferenceNickName = (EditTextPreference) findPreference("nickName");
		// 是否显示SIM卡信息
		checkBoxPreferenceShowSIMInfo = (CheckBoxPreference) findPreference("showSIMInfo");
		// 是否显示昵称
		checkBoxPreferenceShowNickName = (CheckBoxPreference) findPreference("showNickName");
		// 是否显示每日一句
		checkBoxPreferenceShowWord = (CheckBoxPreference) findPreference("showWord");
		// 自动获取每日一句
		checkBoxPreferenceAutoWord = (CheckBoxPreference) findPreference("autoWord");
		// 是否显示特殊节日
		checkBoxPreferenceShowSpecialDay = (CheckBoxPreference) findPreference("showSpecialDay");
		// 是否显示充电动画
		checkBoxPreferenceShowBatterCharging = (CheckBoxPreference) findPreference("showBatterCharging");
		// 是否显示耗电动画
		checkBoxPreferenceShowBatteryLosing = (CheckBoxPreference) findPreference("showBatteryLosing");
		// 第三方消息
		checkBoxPreferenceShowMsg = (CheckBoxPreference) findPreference("showMsg");
		// 第三方消息
		checkBoxPreferenceShowFish = (CheckBoxPreference) findPreference("showFish");
		// 强调周几
		checkBoxPreferenceShowWeekDay = (CheckBoxPreference) findPreference("showWeekDay");
		// 每日一句语言类型
		checkBoxPreferenceWordMode = (CheckBoxPreference) findPreference("wordMode");
		checkBoxPreferenceShowUnlockInfo = (CheckBoxPreference) findPreference("showUnlockInfo");
		checkBoxPreferenceShowTraditionalDayCover = (CheckBoxPreference) findPreference("showTraditionalDayCover");
		// 我的位置
		listPreferenceMyLocation = (ListPreference) findPreference("myLocation");
		// 星座
		listPreferenceMyHoroscope = (ListPreference) findPreference("horoscope");
		checkBoxPreferenceShowHoroscope = (CheckBoxPreference) findPreference("showHoroscope");
		checkBoxPreferenceShowKJGJP = (CheckBoxPreference) findPreference("showKJGJP");

		editTextPreferenceNickName.setSummary(prefs.getString("nickName",
				"Chang"));
		editTextPreferenceWord = (EditTextPreference) findPreference("word");
		editTextPreferenceWord
				.setSummary(prefs
						.getString("word",
								"Early to bed and early to rise makes a man healthy, wealthy, and wise. "));

		editTextPreferenceWord.setEnabled(!prefs.getBoolean("autoWord", false));
		checkBoxPreferenceShowSIMInfo.setChecked(prefs.getBoolean(
				"showSIMInfo", true));
		checkBoxPreferenceShowTraditionalDayCover.setChecked(prefs.getBoolean(
				"showTraditionalDayCover", true));
		checkBoxPreferenceShowKJGJP.setChecked(prefs.getBoolean("showKJGJP",
				true));
		checkBoxPreferenceShowHoroscope.setChecked(prefs.getBoolean(
				"showHoroscope", true));
		checkBoxPreferenceShowNickName.setChecked(prefs.getBoolean(
				"showNickName", true));
		checkBoxPreferenceShowFish.setChecked(prefs
				.getBoolean("showFish", true));
		checkBoxPreferenceShowWord.setChecked(prefs
				.getBoolean("showWord", true));
		checkBoxPreferenceShowMsg.setChecked(prefs.getBoolean("showMsg", true));
		checkBoxPreferenceAutoWord.setChecked(prefs.getBoolean("autoWord",
				false));
		checkBoxPreferenceShowSpecialDay.setChecked(prefs.getBoolean(
				"showSpecialDay", true));
		checkBoxPreferenceShowBatterCharging.setChecked(prefs.getBoolean(
				"showBatterCharging", true));
		checkBoxPreferenceShowBatteryLosing.setChecked(prefs.getBoolean(
				"showBatteryLosing", true));
		checkBoxPreferenceShowWeekDay.setChecked(prefs.getBoolean(
				"showWeekDay", true));
		checkBoxPreferenceWordMode.setChecked(prefs
				.getBoolean("wordMode", true));
		checkBoxPreferenceShowUnlockInfo.setChecked(prefs.getBoolean(
				"showUnlockInfo", true));
		checkBoxPreferenceShowKJGJP.setChecked(prefs.getBoolean("showKJGJP",
				true));
		listPreferenceMyLocation.setSummary(prefs.getString("myLocation",
				"点击弹出城市列表"));

		String horoscope = "水瓶座";
		String horoscopeTip = "gn_day_ic_aquarius";
		if (!prefs.getString("horoscope", horoscopeTip).equals(horoscopeTip)) {
			horoscope = horoscopeMaps.get(horoscopeTip);
		}
		listPreferenceMyHoroscope.setSummary(horoscope);

		editTextPreferenceNickName
				.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {

					@Override
					public boolean onPreferenceChange(Preference preference,
							Object newValue) {
						// TODO Auto-generated method stub
						editTextPreferenceNickName.setSummary(newValue
								.toString());
						editTextPreferenceNickName.setDefaultValue(newValue);
						editTextPreferenceNickName.setText(newValue.toString());
						Editor editor = prefs.edit();

						editor.putString("nickName", newValue.toString());

						editor.commit();
						return true;
					}
				});

		checkBoxPreferenceShowKJGJP
				.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
					@Override
					public boolean onPreferenceChange(Preference arg0,
							Object newValue) {

						Editor editor = prefs.edit();

						editor.putBoolean("showKJGJP", (Boolean) newValue);

						editor.commit();
						return true;

					}
				});

		checkBoxPreferenceShowTraditionalDayCover
		.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			@Override
			public boolean onPreferenceChange(Preference arg0,
					Object newValue) {

				Editor editor = prefs.edit();

				editor.putBoolean("showTraditionalDayCover", (Boolean) newValue);

				editor.commit();
				return true;

			}
		});
		
		checkBoxPreferenceShowHoroscope
				.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
					@Override
					public boolean onPreferenceChange(Preference arg0,
							Object newValue) {

						Editor editor = prefs.edit();

						editor.putBoolean("showHoroscope", (Boolean) newValue);

						editor.commit();
						return true;

					}
				});

		checkBoxPreferenceShowFish
				.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
					@Override
					public boolean onPreferenceChange(Preference arg0,
							Object newValue) {

						Editor editor = prefs.edit();

						editor.putBoolean("showFish", (Boolean) newValue);

						editor.commit();
						return true;

					}
				});

		checkBoxPreferenceShowUnlockInfo
				.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
					@Override
					public boolean onPreferenceChange(Preference arg0,
							Object newValue) {

						Editor editor = prefs.edit();

						editor.putBoolean("showUnlockInfo", (Boolean) newValue);

						editor.commit();
						return true;

					}
				});

		checkBoxPreferenceShowSIMInfo
				.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
					@Override
					public boolean onPreferenceChange(Preference arg0,
							Object newValue) {

						Editor editor = prefs.edit();

						editor.putBoolean("showSIMInfo", (Boolean) newValue);

						editor.commit();
						return true;

					}
				});

		checkBoxPreferenceShowWeekDay
				.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
					@Override
					public boolean onPreferenceChange(Preference arg0,
							Object newValue) {

						Editor editor = prefs.edit();

						editor.putBoolean("showWeekDay", (Boolean) newValue);

						editor.commit();
						return true;

					}
				});

		checkBoxPreferenceShowNickName
				.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
					@Override
					public boolean onPreferenceChange(Preference arg0,
							Object newValue) {

						Editor editor = prefs.edit();

						editor.putBoolean("showNickName", (Boolean) newValue);

						editor.commit();
						return true;

					}
				});

		editTextPreferenceWord
				.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {

					@Override
					public boolean onPreferenceChange(Preference preference,
							Object newValue) {
						// TODO Auto-generated method stub
						editTextPreferenceWord.setSummary(newValue.toString());
						editTextPreferenceWord.setDefaultValue(newValue);
						editTextPreferenceWord.setText(newValue.toString());
						Editor editor = prefs.edit();

						editor.putString("word", newValue.toString());

						editor.commit();
						return true;
					}
				});

		checkBoxPreferenceShowWord
				.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
					@Override
					public boolean onPreferenceChange(Preference arg0,
							Object newValue) {

						Editor editor = prefs.edit();

						editor.putBoolean("showWord", (Boolean) newValue);

						editor.commit();
						return true;

					}
				});

		checkBoxPreferenceShowMsg
				.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
					@Override
					public boolean onPreferenceChange(Preference arg0,
							Object newValue) {

						Editor editor = prefs.edit();

						editor.putBoolean("showMsg", (Boolean) newValue);

						editor.commit();
						return true;

					}
				});
		checkBoxPreferenceShowSpecialDay
				.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
					@Override
					public boolean onPreferenceChange(Preference arg0,
							Object newValue) {

						Editor editor = prefs.edit();

						editor.putBoolean("showSpecialDay", (Boolean) newValue);

						editor.commit();
						return true;

					}
				});
		checkBoxPreferenceShowBatterCharging
				.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
					@Override
					public boolean onPreferenceChange(Preference arg0,
							Object newValue) {

						Editor editor = prefs.edit();

						editor.putBoolean("showBatteryCharging",
								(Boolean) newValue);

						editor.commit();
						return true;

					}
				});
		checkBoxPreferenceShowBatteryLosing
				.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
					@Override
					public boolean onPreferenceChange(Preference arg0,
							Object newValue) {

						Editor editor = prefs.edit();

						editor.putBoolean("showBatteryLosing",
								(Boolean) newValue);

						editor.commit();
						return true;

					}
				});

		checkBoxPreferenceWordMode
				.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
					@Override
					public boolean onPreferenceChange(Preference arg0,
							Object newValue) {

						Editor editor = prefs.edit();

						editor.putBoolean("wordMode", (Boolean) newValue);
						editor.putString("wordDate", "");

						editor.commit();
						return true;

					}
				});

		listPreferenceMyLocation
				.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
					@Override
					public boolean onPreferenceChange(Preference arg0,
							Object newValue) {

						Editor editor = prefs.edit();

						editor.putString("myLocation", newValue.toString());

						editor.commit();
						listPreferenceMyLocation.setSummary(newValue.toString());
						return true;

					}
				});

		listPreferenceMyHoroscope
				.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
					@Override
					public boolean onPreferenceChange(Preference arg0,
							Object newValue) {

						Editor editor = prefs.edit();

						editor.putString("horoscope", newValue.toString());

						editor.commit();
						String horoscope = "选择星座";
						String horoscopeTip = newValue.toString();
						// if(!prefs.getString("horoscope",
						// horoscopeTip).equals(newValue.toString())){
						// horoscope = horoscopeMaps.get(horoscopeTip);
						// }
						horoscope = horoscopeMaps.get(horoscopeTip);
						listPreferenceMyHoroscope.setSummary(horoscope);
						return true;

					}
				});

		checkBoxPreferenceAutoWord
				.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
					@Override
					public boolean onPreferenceChange(Preference arg0,
							Object newValue) {

						Editor editor = prefs.edit();

						editor.putBoolean("autoWord", (Boolean) newValue);

						editTextPreferenceWord.setEnabled(!(Boolean) newValue);

						editor.commit();

						try {
							if ((Boolean) newValue) {
								// ScheduledExecutorService executor = Executors
								// .newScheduledThreadPool(1);
								// long oneDay = 24 * 60 * 60 * 1000;
								// long initDelay = getTimeMillis("06:00:00")
								// - System.currentTimeMillis();
								// initDelay = initDelay > 0 ? initDelay :
								// oneDay
								// + initDelay;
								//
								// executor.scheduleAtFixedRate(new
								// EchoServer(),
								// initDelay, oneDay,
								// TimeUnit.MILLISECONDS);
								// Intent autoWordService = new Intent();
								// autoWordService.setClass(rootView.getContext(),
								// AutoWordService.class);
								// // 启动Service
								// rootView.getContext().startService(
								// autoWordService);
							} else {
								// Intent autoWordService = new Intent();
								// autoWordService.setClass(rootView.getContext(),
								// AutoWordService.class);
								// rootView.getContext().stopService(
								// autoWordService);
							}

						} catch (Exception e) {
							// TODO: handle exception
							Logger.d("BasicSettingPreferenceFragment",
									e.getMessage());
						}

						return true;

					}
				});

		rootView.setPadding(0,
				(int) convertDpToPixel(50, rootView.getContext()), 0, 0);
		return rootView;
	}

	class EchoServer implements Runnable {
		@Override
		public void run() {
			try {
				Editor editor = prefs.edit();

				editor.putString("word", "自动获取每日一句还未实现，请勿使用!");

				editor.commit();
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 获取指定时间对应的毫秒数
	 * 
	 * @param time
	 *            "HH:mm:ss"
	 * @return
	 */
	private static long getTimeMillis(String time) {
		try {
			DateFormat dateFormat = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
			DateFormat dayFormat = new SimpleDateFormat("yy-MM-dd");
			Date curDate = dateFormat.parse(dayFormat.format(new Date()) + " "
					+ time);
			return curDate.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
			Logger.d("BasicSettingPreferenceFragment", e.getMessage());
		}
		return 0;
	}

	/**
	 * This method converts dp unit to equivalent pixels, depending on device
	 * density.
	 * 
	 * @param dp
	 *            A value in dp (density independent pixels) unit. Which we need
	 *            to convert into pixels
	 * @param context
	 *            Context to get resources and device specific display metrics
	 * @return A float value to represent px equivalent to dp depending on
	 *         device density
	 */
	public static float convertDpToPixel(float dp, Context context) {
		Resources resources = context.getResources();
		DisplayMetrics metrics = resources.getDisplayMetrics();
		float px = dp * (metrics.densityDpi / 160f);
		return px;
	}

	/**
	 * This method converts device specific pixels to density independent
	 * pixels.
	 * 
	 * @param px
	 *            A value in px (pixels) unit. Which we need to convert into db
	 * @param context
	 *            Context to get resources and device specific display metrics
	 * @return A float value to represent dp equivalent to px value
	 */
	public static float convertPixelsToDp(float px, Context context) {
		Resources resources = context.getResources();
		DisplayMetrics metrics = resources.getDisplayMetrics();
		float dp = px / (metrics.densityDpi / 160f);
		return dp;
	}
}