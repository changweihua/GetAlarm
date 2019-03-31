package net.cmono.dialogs;

import org.litepal.LitePalApplication;

import net.cmono.consts.ConstValue;
import net.cmono.getalarm.R;
import net.cmono.utils.DisplayUtil;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.Gravity;
import android.view.animation.TranslateAnimation;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView.ScaleType;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.ToggleButton;

/**
 * 
 * ״̬��ť�ļ����¼�
 * 
 * @author Changweihua
 * 
 */
public class ToggleListener implements OnCheckedChangeListener {
	private Context context;
	private String settingName;
	private ToggleButton toggle;
	private ImageButton toggle_Button;

	SharedPreferences prefs;

	public ToggleListener(Context context, String settingName,
			ToggleButton toggle, ImageButton toggle_Button) {
		this.context = context;
		this.settingName = settingName;
		this.toggle = toggle;
		this.toggle_Button = toggle_Button;

		prefs = LitePalApplication.getContext().getSharedPreferences(
				ConstValue.INCALL_PF, Context.MODE_PRIVATE);

	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		Editor editor = prefs.edit();

		// ��������
		if ("���緭ת����".equals(settingName)) {
			editor.putBoolean("enableFlipSlient", isChecked);
		} else if ("����������".equals(settingName)) {
			Toast.makeText(context, isChecked ? "����" : "�ر�", 1000);
		}

		editor.commit();

		// ���Ŷ���
		RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) toggle_Button
				.getLayoutParams();
		if (isChecked) {
			// ����λ��
			params.addRule(RelativeLayout.ALIGN_LEFT, -1);
			if ("���緭ת����".equals(settingName)) {
				params.addRule(RelativeLayout.ALIGN_RIGHT,
						R.id.toggle_EnableFlipSlient);
			}
			toggle_Button.setLayoutParams(params);
			toggle_Button.setImageResource(R.drawable.progress_thumb_selector);
			toggle.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
			// ���Ŷ���
			TranslateAnimation animation = new TranslateAnimation(
					DisplayUtil.dip2px(context, -40), 0, 0, 0);
			animation.setDuration(200);
			toggle_Button.setScaleType(ScaleType.FIT_END);
			toggle_Button.startAnimation(animation);
		} else {
			// ����λ��
			if ("���緭ת����".equals(settingName)) {
				params.addRule(RelativeLayout.ALIGN_LEFT,
						R.id.toggle_EnableFlipSlient);
			}
			params.addRule(RelativeLayout.ALIGN_RIGHT, -1);
			toggle_Button.setLayoutParams(params);
			toggle_Button
					.setImageResource(R.drawable.progress_thumb_off_selector);

			toggle.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
			// ���Ŷ���
			TranslateAnimation animation = new TranslateAnimation(
					DisplayUtil.dip2px(context, 40), 0, 0, 0);
			animation.setDuration(200);
			toggle_Button.setScaleType(ScaleType.FIT_START);
			toggle_Button.startAnimation(animation);
		}
	}

}
