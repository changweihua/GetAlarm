package net.cmono.utils;

import net.cmono.getalarm.R;
import android.app.Activity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.gitonway.lee.niftynotification.lib.Configuration;
import com.gitonway.lee.niftynotification.lib.Effects;
import com.gitonway.lee.niftynotification.lib.Manager;
import com.gitonway.lee.niftynotification.lib.NiftyNotificationView;

/***
 * 提示消息的管理类
 * 
 * @author yang
 * 
 */
public class TipViewManager {
	@SuppressWarnings("unused")
	private static final String TAG = "TipViewManager";

	// 容器ID:必须和布局R.layout.ac_show_tip中一致
	private static final int CONTAINER_VIEW_ID = R.id.rl_tip;
	// 提示View的高度: dp
	private static final int TIP_VIEW_HEIGHT = 40;

	// 显示和退出动画效果
	private static final Effects DEFAULT_EFFECT = Effects.standard;
	// 入出时间 ms
	private static final int ANIMATION_DURATION = 700;

	/***
	 * 显示提示消息
	 * 
	 * @param act
	 *            所在的activity
	 * @param contentView
	 *            提示的内容View
	 * @param viewHeight
	 *            提示View的高度
	 * @param animEffect
	 *            进入和退出的动画效果
	 * @param animDuration
	 *            进入和退出动画时间
	 * @param showTime
	 *            提示的显示时间:不包括进入退出时间
	 * @return
	 */
	public static NiftyNotificationView show(Activity act,
			ViewGroup contentView, int viewHeight, Effects animEffect,
			long animDuration, long showTime) {
		if (act == null || contentView == null) {
			return null;
		}

		if (viewHeight < 0) {
			viewHeight = TIP_VIEW_HEIGHT;
		}
		if (animEffect == null) {
			animEffect = DEFAULT_EFFECT;
		}
		if (animDuration < 0) {
			animDuration = ANIMATION_DURATION;
		}

		// 提示View的配置
		Configuration cfg = new Configuration.Builder()
				.setAnimDuration(animDuration)// 动画时间
				.setDispalyDuration(showTime)// 动画完成后，显示多长时间
				.setViewHeight(viewHeight)// dp 提示View的高度
				.build();
		// 建立提示View
		final NiftyNotificationView nnView = NiftyNotificationView.build(act,
				contentView, animEffect, CONTAINER_VIEW_ID, cfg);
		// TODO 重复显示，调用这个方法多少次，就显示多少次
		// 如果是show(),不重复显示时，如果提示View正在显示中，调用这个方法，不会显示多次
		nnView.show(true);
		return nnView;
	}

	/***
	 * 清空所有显示的View
	 */
	public static void clearAll() {
		Manager.getInstance().clearQueue();
	}

	/***
	 * 清空显示的View
	 */
	public static void clear(Activity act) {
		Manager.getInstance().clear(act);
	}

	/***
	 * 显示提示消息（正常的）
	 * 
	 * @param act
	 * @param msg
	 * @param showTime
	 * @return
	 */
	public static NiftyNotificationView showNormalMsg(Activity act, String msg,
			long showTime) {
		if (act == null) {
			return null;
		}
		if (TextUtils.isEmpty(msg)) {
			return null;
		}
		LinearLayout contentView = (LinearLayout) LayoutInflater.from(act)
				.inflate(R.layout.show_normal_tip, null);
		((TextView) contentView.findViewById(R.id.tip_msg)).setText(msg);
		// 显示提示消息
		return show(act, contentView, TIP_VIEW_HEIGHT, DEFAULT_EFFECT,
				ANIMATION_DURATION, showTime);
	}

	/***
	 * 显示错误的提示
	 * 
	 * @param act
	 * @param msg
	 * @param showTime
	 * @return
	 */
	public static NiftyNotificationView showErrorMsg(Activity act, String msg,
			long showTime) {
		if (act == null) {
			return null;
		}
		if (TextUtils.isEmpty(msg)) {
			msg = act.getString(R.string.tip_error);
		}
		LinearLayout contentView = (LinearLayout) LayoutInflater.from(act)
				.inflate(R.layout.show_error_tip, null);
		((TextView) contentView.findViewById(R.id.tip_msg)).setText(msg);
		// 显示提示消息
		return show(act, contentView, TIP_VIEW_HEIGHT, DEFAULT_EFFECT,
				ANIMATION_DURATION, showTime);
	}

	/***
	 * 显示没有数据的提示
	 * 
	 * @param act
	 * @param msg
	 * @param showTime
	 * @return
	 */
	public static NiftyNotificationView showNoDataMsg(Activity act, String msg,
			long showTime) {
		if (act == null) {
			return null;
		}
		if (TextUtils.isEmpty(msg)) {
			msg = act.getString(R.string.tip_no_data);
		}
		LinearLayout contentView = (LinearLayout) LayoutInflater.from(act)
				.inflate(R.layout.show_no_data_tip, null);
		((TextView) contentView.findViewById(R.id.tip_msg)).setText(msg);
		// 显示提示消息
		return show(act, contentView, TIP_VIEW_HEIGHT, DEFAULT_EFFECT,
				ANIMATION_DURATION, showTime);
	}

	/***
	 * 取得能显示提示的根View <br>
	 * 要在Activity onCreate中setContentView()设置为返回的根View
	 * 
	 * @param act
	 * @param layoutResId
	 *            activity的布局ID
	 * @return
	 */
	public static ViewGroup getTipRootView(Activity act, int layoutResId) {
		LayoutInflater inflater = LayoutInflater.from(act);
		RelativeLayout rlRootView = (RelativeLayout) inflater.inflate(
				R.layout.ac_show_tip, null);
		View contentView = inflater.inflate(layoutResId, null);
		// 加上布局属性，防止layoutResId对应的xml根View是scrollView，activity的内容不会全屏
		rlRootView.addView(contentView, 0, new LayoutParams(
				android.view.ViewGroup.LayoutParams.MATCH_PARENT,
				android.view.ViewGroup.LayoutParams.MATCH_PARENT));
		return rlRootView;
	}

}
