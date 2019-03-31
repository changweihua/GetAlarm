package net.cmono.dialogs;


import net.cmono.consts.ConstValue;
import net.cmono.getalarm.GetAlarmApplication;
import net.cmono.getalarm.R;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ToggleButton;

import com.flyco.animation.FadeEnter.FadeEnter;
import com.flyco.animation.FadeExit.FadeExit;
import com.flyco.dialog.utils.CornerUtils;
import com.flyco.dialog.widget.base.BaseDialog;

public class IncallSettingDialog extends BaseDialog<IncallSettingDialog> {

	private LinearLayout layout_EnableFlipSlient;
	private ToggleButton toggle_EnableFlipSlient;
	private ImageButton toggleButton_EnableFlipSlient;

	SharedPreferences prefs;

	private Context mContext;

	public IncallSettingDialog(Context context) {
		super(context);
		mContext = context;
	}

	@SuppressWarnings("deprecation")
	@Override
	public View onCreateView() {
		widthScale(0.85f);
		showAnim(new FadeEnter());
		dismissAnim(new FadeExit());

		View inflate = View.inflate(mContext, R.layout.dialog_incall_setting,
				null);

		inflate.setBackgroundDrawable(CornerUtils.cornerDrawable(
				Color.parseColor("#ffffff"), dp2px(5)));

		// 找到控件
		layout_EnableFlipSlient = (LinearLayout) inflate
				.findViewById(R.id.layout_EnableFlipSlient);
		toggle_EnableFlipSlient = (ToggleButton) inflate
				.findViewById(R.id.toggle_EnableFlipSlient);
		toggleButton_EnableFlipSlient = (ImageButton) inflate
				.findViewById(R.id.toggleButton_EnableFlipSlient);

		prefs = GetAlarmApplication.getContext().getSharedPreferences(
				ConstValue.INCALL_PF, Context.MODE_PRIVATE);

		initViews();
		setListeners();

		return inflate;
	}

	private void initViews() {
		// 是否开启，获取SharePerference保存的用户配置
		boolean enableFlipSlient = prefs.getBoolean("enableFlipSlient", false);
		toggle_EnableFlipSlient.setChecked(enableFlipSlient);
		RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) toggleButton_EnableFlipSlient
				.getLayoutParams();
		if (enableFlipSlient) {
			// 调整位置
			params.addRule(RelativeLayout.ALIGN_LEFT, -1);
			params.addRule(RelativeLayout.ALIGN_RIGHT,
					R.id.toggle_EnableFlipSlient);
			toggleButton_EnableFlipSlient.setScaleType(ScaleType.FIT_END);
			toggleButton_EnableFlipSlient.setLayoutParams(params);
			toggleButton_EnableFlipSlient
					.setImageResource(R.drawable.progress_thumb_selector);
			toggle_EnableFlipSlient.setGravity(Gravity.RIGHT
					| Gravity.CENTER_VERTICAL); // 文字
		} else {
			// 调整位置
			params.addRule(RelativeLayout.ALIGN_LEFT,
					R.id.toggle_EnableFlipSlient);
			params.addRule(RelativeLayout.ALIGN_RIGHT, -1);
			toggleButton_EnableFlipSlient.setLayoutParams(params);
			toggleButton_EnableFlipSlient.setScaleType(ScaleType.FIT_START);
			toggleButton_EnableFlipSlient
					.setImageResource(R.drawable.progress_thumb_off_selector);
			toggle_EnableFlipSlient.setGravity(Gravity.LEFT
					| Gravity.CENTER_VERTICAL);
		}

	}

	private void setListeners() {
		toggle_EnableFlipSlient.setOnCheckedChangeListener(new ToggleListener(
				getContext(), "来电翻转静音", toggle_EnableFlipSlient,
				toggleButton_EnableFlipSlient));

		// UI事件，按钮点击事件
		android.view.View.OnClickListener clickToToggleListener = new android.view.View.OnClickListener() {

			@Override
			public void onClick(View v) {
				toggle_EnableFlipSlient.toggle();
			}
		};

		toggleButton_EnableFlipSlient.setOnClickListener(clickToToggleListener);
		layout_EnableFlipSlient.setOnClickListener(clickToToggleListener);
	}

	@Override
	public void setUiBeforShow() {
		// tv_cancel.setOnClickListener(new View.OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// dismiss();
		// }
		// });
		// tv_exit.setOnClickListener(new View.OnClickListener() {
		// @Override
		// public void onClick(View v) {
		//
		// // 生成节日表
		// SQLiteDatabase db = Connector.getDatabase();
		// List<SpecialDay> list = new ArrayList<SpecialDay>();
		//
		// int id = DataSupport.max(SpecialDay.class, "id", int.class) + 1;
		// int y = dp_date.getYear();
		// int m = dp_date.getMonth() + 1;
		// int d = dp_date.getDayOfMonth();
		// String name = ed_name.getText().toString();
		// String description = ed_description.getText().toString();
		//
		// SpecialDay day = new SpecialDay(id, m + "" + d, name,
		// description);
		// list.add(day);
		// // 保存
		// DataSupport.saveAll(list);
		//
		// new D(getContext()).ShowMaterialSimpleConfirmDialog("添加成功");
		//
		// dismiss();
		// }
		// });
	}
	// new
	// D(mContext).ShowMaterialSimpleConfirmDialog(String.valueOf(checkedId));

}
