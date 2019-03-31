package net.cmono.dialogs;

import net.cmono.consts.ConstValue;
import net.cmono.getalarm.GetAlarmApplication;
import net.cmono.getalarm.R;
import net.cmono.utils.ViewFindUtil;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.flyco.animation.FadeEnter.FadeEnter;
import com.flyco.animation.FadeExit.FadeExit;
import com.flyco.dialog.utils.CornerUtils;
import com.flyco.dialog.widget.base.BaseDialog;

public class AppSettingDialog extends BaseDialog<AppSettingDialog> {

	SharedPreferences prefs;

	private Context mContext;

	private TextView tv_exit;

	private Spinner styleSpinner;
	private ArrayAdapter<?> styleAdapter;

	private Spinner languageSpinner;
	private ArrayAdapter<?> languageAdapter;

	public AppSettingDialog(Context context) {
		super(context);
		mContext = context;
	}

	@SuppressWarnings("deprecation")
	@Override
	public View onCreateView() {
		widthScale(0.85f);
		showAnim(new FadeEnter());
		dismissAnim(new FadeExit());

		View inflate = View
				.inflate(mContext, R.layout.dialog_app_setting, null);

		inflate.setBackgroundDrawable(CornerUtils.cornerDrawable(
				Color.parseColor("#ffffff"), dp2px(5)));

		styleSpinner = (Spinner) inflate.findViewById(R.id.SpinnerStyle);
		languageSpinner = (Spinner) inflate.findViewById(R.id.SpinnerLanguage);
		tv_exit = ViewFindUtil.find(inflate, R.id.apps_exit);

		prefs = GetAlarmApplication.getContext().getSharedPreferences(
				ConstValue.APP_PF, Context.MODE_PRIVATE);

		initViews();
		setListeners();

		return inflate;
	}

	private void initViews() {
		// 将可选内容与ArrayAdapter连接起来
		styleAdapter = ArrayAdapter.createFromResource(mContext,
				R.array.icon_styles, android.R.layout.simple_spinner_item);
		// 设置下拉列表的风格
		// android.R.layout.simple_list_item_checked
		styleAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// 将adapter2 添加到spinner中
		styleSpinner.setAdapter(styleAdapter);
		int style = prefs.getInt("IconStyle", 0);
		styleSpinner.setSelection(style);
		// 设置默认值
		styleSpinner.setVisibility(View.VISIBLE);

		languageAdapter = ArrayAdapter.createFromResource(mContext,
				R.array.app_languages, android.R.layout.simple_spinner_item);
		languageAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		languageSpinner.setAdapter(languageAdapter);
		int language = prefs.getInt("Language", 0);
		languageSpinner.setSelection(language);
		languageSpinner.setVisibility(View.VISIBLE);

	}

	// 使用XML形式操作
	class SpinnerXMLSelectedListener implements OnItemSelectedListener {
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			Editor editor = prefs.edit();
			editor.putInt("IconStyle", arg2);// adapter2.getItem(arg2)
			editor.commit();
			// spinner2.setSelection(arg2);
			// new D(getContext()).ShowMaterialSimpleConfirmDialog("重启APP查看效果");
		}

		public void onNothingSelected(AdapterView<?> arg0) {

		}

	}

	class LanguageSpinnerXMLSelectedListener implements OnItemSelectedListener {
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			Editor editor = prefs.edit();
			editor.putInt("Language", arg2);// adapter2.getItem(arg2)
			editor.commit();
		}

		public void onNothingSelected(AdapterView<?> arg0) {

		}

	}

	private void setListeners() {
		// 添加事件Spinner事件监听
		styleSpinner
				.setOnItemSelectedListener(new SpinnerXMLSelectedListener());
		languageSpinner
				.setOnItemSelectedListener(new LanguageSpinnerXMLSelectedListener());
	}

	@Override
	public void setUiBeforShow() {
		tv_exit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
	}

}
