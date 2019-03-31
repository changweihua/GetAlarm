package net.cmono.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.flyco.animation.BaseAnimatorSet;
import com.flyco.animation.BounceEnter.BounceTopEnter;
import com.flyco.animation.SlideExit.SlideBottomExit;
import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.widget.MaterialDialog;

import android.content.Context;

/**
 * 对话框帮助类
 * 
 * @author Changweihua
 * 
 */
public class D {
	private Context context = null;
	private BaseAnimatorSet bas_in;
	private BaseAnimatorSet bas_out;

	public D(Context ctx) {
		context = ctx;
		bas_in = new BounceTopEnter();
		bas_out = new SlideBottomExit();
	}

	public void setBasIn(BaseAnimatorSet bas_in) {
		this.bas_in = bas_in;
	}

	public void setBasOut(BaseAnimatorSet bas_out) {
		this.bas_out = bas_out;
	}

	/**
	 * 仅仅提供确认选项
	 * 
	 * @param text
	 */
	public void ShowMaterialSimpleConfirmDialog(String text) {
		final MaterialDialog dialog = new MaterialDialog(context);
		dialog.content(text).btnNum(1)//
				.btnText("确定")//
				.showAnim(bas_in)//
				.dismissAnim(bas_out)//
				.show();

		dialog.setOnBtnClickL(new OnBtnClickL() {
			@Override
			public void onBtnClick() {
				dialog.dismiss();
			}
		});

	}

	/**
	 * 提供选择
	 * 
	 * @param text
	 */
	public void ShowMaterialDialog(String text, String[] names, Method[] actions) {
		final MaterialDialog dialog = new MaterialDialog(context);
		dialog.content(text)//
				.btnText(names)//
				.showAnim(bas_in)//
				.dismissAnim(bas_out)//
				.show();

		OnBtnClickL[] methods = new OnBtnClickL[names.length];

		for (int i = 0; i < names.length; i++) {
			final Method action = actions[i];
			methods[i] = new OnBtnClickL() {
				@Override
				public void onBtnClick() {
					try {
						action.invoke(null, null);
					} catch (IllegalAccessException | IllegalArgumentException
							| InvocationTargetException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					dialog.dismiss();
				}
			};
		}

		dialog.setOnBtnClickL(methods);

	}

}
