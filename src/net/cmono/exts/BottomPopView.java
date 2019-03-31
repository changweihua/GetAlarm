package net.cmono.exts;

import net.cmono.getalarm.R;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;

public abstract class BottomPopView {
	private Context mContext;
	private View anchor;
	private LayoutInflater mInflater;
	private TextView mTvTop;
	private TextView mTvBottom;
	private TextView mTvCancel;
	private PopupWindow mPopupWindow;
	LayoutParams params;
	WindowManager windowManager;
	Window window;

	/**
	 * @param context
	 * @param anchor
	 *            �������ĸ�View����
	 */
	public BottomPopView(Activity context, View anchor) {
		this.mContext = context;
		this.mInflater = LayoutInflater.from(context);
		this.anchor = anchor;
		windowManager = context.getWindowManager();
		window = context.getWindow();
		params = context.getWindow().getAttributes();
		init();
	}

	public void init() {
		View view = mInflater.inflate(R.layout.bottom_pop_window, null);
		params.dimAmount = 0.5f;
		window.addFlags(LayoutParams.FLAG_DIM_BEHIND);
		mTvBottom = (TextView) view.findViewById(R.id.tv_choose_photo);
		mTvTop = (TextView) view.findViewById(R.id.tv_take_photo);
		mTvCancel = (TextView) view.findViewById(R.id.tv_cancel);
		mTvTop.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				onTopButtonClick();
			}
		});
		mTvBottom.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				onBottomButtonClick();
			}
		});
		mTvCancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});

		mPopupWindow = new PopupWindow(view, 720,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		// ����PopupWindow��dismiss����dismissʱ��Ļ�ָ�����
		mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
			@Override
			public void onDismiss() {
				params.alpha = 1.0f;
				window.setAttributes(params);
			}
		});
		mPopupWindow.setWidth(android.view.ViewGroup.LayoutParams.MATCH_PARENT);
		mPopupWindow
				.setHeight(android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
		mPopupWindow.setTouchable(true);
		mPopupWindow.setFocusable(true);
		mPopupWindow.setOutsideTouchable(true);
		// ����Ч�� �ӵײ�����
		mPopupWindow.setAnimationStyle(R.style.AnimBottom);
	}

	/**
	 * ��ʾ�ײ��Ի���
	 */
	public void show() {
		mPopupWindow.showAtLocation(anchor, Gravity.BOTTOM
				| Gravity.CENTER_HORIZONTAL, 0, 0);
		params.alpha = 0.5f;
		window.setAttributes(params);
	}

	/**
	 * ��һ����ť������Ļص�
	 */
	public abstract void onTopButtonClick();

	/**
	 * �ڶ�����ť������Ļص�
	 */
	public abstract void onBottomButtonClick();

	public void setTopText(String text) {
		mTvTop.setText(text);
	}

	public void setBottomText(String text) {
		mTvBottom.setText(text);
	}

	public void dismiss() {
		if (mPopupWindow != null && mPopupWindow.isShowing()) {
			mPopupWindow.dismiss();
		}
	}
}
