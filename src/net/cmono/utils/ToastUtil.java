package net.cmono.utils;

import net.cmono.getalarm.R;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ToastUtil {

	private static ToastUtil toastCommom;

	private Toast toast;

	private ToastUtil() {
	}

	public static ToastUtil createToastConfig() {
		if (toastCommom == null) {
			toastCommom = new ToastUtil();
		}
		return toastCommom;
	}

	/**
	 * œ‘ æToast
	 * 
	 * @param context
	 * @param root
	 * @param tvString
	 */

	public void ToastShow(Context context, ViewGroup root, String title,
			String msg) {
		View layout = LayoutInflater.from(context).inflate(R.layout.toast_xml,
				root);
		TextView tvTitle = (TextView) layout.findViewById(R.id.toastTitle);
		TextView tvMsg = (TextView) layout.findViewById(R.id.toastMsg);
		tvTitle.setText(title);
		tvMsg.setText(msg);
		toast = new Toast(context);
		// toast.setGravity(Gravity.BOTTOM, 0, 0);
		toast.setDuration(Toast.LENGTH_LONG);
		toast.setView(layout);
		toast.show();
	}

}