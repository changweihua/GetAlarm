package net.cmono.getalarm;

import net.cmono.consts.ConstValue;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.Preference.OnPreferenceChangeListener;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class IncallSettingPreferenceFragment extends PreferenceFragment
		implements OnPreferenceChangeListener {

	SharedPreferences prefs;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		final View rootView = super.onCreateView(inflater, container,
				savedInstanceState);
		addPreferencesFromResource(R.xml.incall_setting);
		prefs = rootView.getContext().getSharedPreferences(
				ConstValue.SETTING_PF, Context.MODE_PRIVATE);

		rootView.setPadding(0,
				(int) convertDpToPixel(50, rootView.getContext()), 0, 0);
		return rootView;
	}

	@Override
	public boolean onPreferenceChange(Preference preference, Object newValue) {
		// TODO Auto-generated method stub
		String key = preference.getKey();

		Editor editor = prefs.edit();

		editor.putBoolean(key, (Boolean) newValue);

		editor.commit();
		return true;
	}

	public static float convertDpToPixel(float dp, Context context) {
		Resources resources = context.getResources();
		DisplayMetrics metrics = resources.getDisplayMetrics();
		float px = dp * (metrics.densityDpi / 160f);
		return px;
	}

	public static float convertPixelsToDp(float px, Context context) {
		Resources resources = context.getResources();
		DisplayMetrics metrics = resources.getDisplayMetrics();
		float dp = px / (metrics.densityDpi / 160f);
		return dp;
	}

}
