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
 * ��ʾ��Ϣ�Ĺ�����
 * 
 * @author yang
 * 
 */
public class TipViewManager {
	@SuppressWarnings("unused")
	private static final String TAG = "TipViewManager";

	// ����ID:����Ͳ���R.layout.ac_show_tip��һ��
	private static final int CONTAINER_VIEW_ID = R.id.rl_tip;
	// ��ʾView�ĸ߶�: dp
	private static final int TIP_VIEW_HEIGHT = 40;

	// ��ʾ���˳�����Ч��
	private static final Effects DEFAULT_EFFECT = Effects.standard;
	// ���ʱ�� ms
	private static final int ANIMATION_DURATION = 700;

	/***
	 * ��ʾ��ʾ��Ϣ
	 * 
	 * @param act
	 *            ���ڵ�activity
	 * @param contentView
	 *            ��ʾ������View
	 * @param viewHeight
	 *            ��ʾView�ĸ߶�
	 * @param animEffect
	 *            ������˳��Ķ���Ч��
	 * @param animDuration
	 *            ������˳�����ʱ��
	 * @param showTime
	 *            ��ʾ����ʾʱ��:�����������˳�ʱ��
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

		// ��ʾView������
		Configuration cfg = new Configuration.Builder()
				.setAnimDuration(animDuration)// ����ʱ��
				.setDispalyDuration(showTime)// ������ɺ���ʾ�೤ʱ��
				.setViewHeight(viewHeight)// dp ��ʾView�ĸ߶�
				.build();
		// ������ʾView
		final NiftyNotificationView nnView = NiftyNotificationView.build(act,
				contentView, animEffect, CONTAINER_VIEW_ID, cfg);
		// TODO �ظ���ʾ����������������ٴΣ�����ʾ���ٴ�
		// �����show(),���ظ���ʾʱ�������ʾView������ʾ�У��������������������ʾ���
		nnView.show(true);
		return nnView;
	}

	/***
	 * ���������ʾ��View
	 */
	public static void clearAll() {
		Manager.getInstance().clearQueue();
	}

	/***
	 * �����ʾ��View
	 */
	public static void clear(Activity act) {
		Manager.getInstance().clear(act);
	}

	/***
	 * ��ʾ��ʾ��Ϣ�������ģ�
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
		// ��ʾ��ʾ��Ϣ
		return show(act, contentView, TIP_VIEW_HEIGHT, DEFAULT_EFFECT,
				ANIMATION_DURATION, showTime);
	}

	/***
	 * ��ʾ�������ʾ
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
		// ��ʾ��ʾ��Ϣ
		return show(act, contentView, TIP_VIEW_HEIGHT, DEFAULT_EFFECT,
				ANIMATION_DURATION, showTime);
	}

	/***
	 * ��ʾû�����ݵ���ʾ
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
		// ��ʾ��ʾ��Ϣ
		return show(act, contentView, TIP_VIEW_HEIGHT, DEFAULT_EFFECT,
				ANIMATION_DURATION, showTime);
	}

	/***
	 * ȡ������ʾ��ʾ�ĸ�View <br>
	 * Ҫ��Activity onCreate��setContentView()����Ϊ���صĸ�View
	 * 
	 * @param act
	 * @param layoutResId
	 *            activity�Ĳ���ID
	 * @return
	 */
	public static ViewGroup getTipRootView(Activity act, int layoutResId) {
		LayoutInflater inflater = LayoutInflater.from(act);
		RelativeLayout rlRootView = (RelativeLayout) inflater.inflate(
				R.layout.ac_show_tip, null);
		View contentView = inflater.inflate(layoutResId, null);
		// ���ϲ������ԣ���ֹlayoutResId��Ӧ��xml��View��scrollView��activity�����ݲ���ȫ��
		rlRootView.addView(contentView, 0, new LayoutParams(
				android.view.ViewGroup.LayoutParams.MATCH_PARENT,
				android.view.ViewGroup.LayoutParams.MATCH_PARENT));
		return rlRootView;
	}

}
