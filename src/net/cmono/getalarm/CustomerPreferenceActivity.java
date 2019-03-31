package net.cmono.getalarm;

import net.cmono.utils.SystemBarTintManager;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;

public class CustomerPreferenceActivity extends PreferenceActivity implements
		OnPreferenceChangeListener {

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

	SharedPreferences prefs;

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

		// requestWindowFeature(Window.FEATURE_NO_TITLE);

		super.onCreate(savedInstanceState);

		addPreferencesFromResource(R.xml.incall_setting);
		this.getListView().setPadding(
				(int) convertDpToPixel(5, getWindow().getContext()),
				(int) convertDpToPixel(25, getWindow().getContext()),
				(int) convertDpToPixel(5, getWindow().getContext()), 0);

	};

	private float convertDpToPixel(float dp, Context context) {
		Resources resources = context.getResources();
		DisplayMetrics metrics = resources.getDisplayMetrics();
		float px = dp * (metrics.densityDpi / 160f);
		return px;
	}

	@Override
	public boolean onPreferenceChange(Preference preference, Object newValue) {
		// TODO Auto-generated method stub
		return false;
	}

}
