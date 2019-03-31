package net.cmono.obvs;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.tencent.bugly.crashreport.CrashReport;

import android.app.Activity;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by pengkv on 15/10/12. 短信监听器，用于自动填充验证码
 */
public class SMSContentObserver extends ContentObserver {
	public final String SMS_URI_INBOX = "content://sms/inbox";// 收信箱
	private Activity activity = null;
	private String smsContent = "";// 验证码
	private EditText verifyText = null;// 验证码编辑框
	private String SMS_ADDRESS_PRNUMBER = "18552015266";// 短息发送提供商

	public SMSContentObserver(Activity activity, Handler handler,
			EditText verifyText) {
		super(handler);
		this.activity = activity;
		this.verifyText = verifyText;
	}

	public SMSContentObserver(Activity activity, Handler handler) {
		super(handler);
	}

	@Override
	public void onChange(boolean selfChange) {
		super.onChange(selfChange);
		try {
			Cursor cursor = null;// 光标
			// 读取收件箱中指定号码的短信
			cursor = activity.getContentResolver().query(
					Uri.parse(SMS_URI_INBOX),
					new String[] { "_id", "address", "body", "read" }, // 要读取的属性
					"address=? and read=?", // 查询条件是什么
					new String[] { SMS_ADDRESS_PRNUMBER, "0" },// 查询条件赋值
					"date desc");// 排序
			if (cursor != null) {
				cursor.moveToFirst();
				if (cursor.moveToFirst()) {
					String smsbody = cursor.getString(cursor
							.getColumnIndex("body"));

					// 用正则表达式匹配验证码
					Pattern pattern = Pattern.compile("[0-9]{6}");
					Matcher matcher = pattern.matcher(smsbody);

					if (matcher.find()) {// 是否匹配
						smsContent = matcher.group();// 获取匹配文本，即验证码
						if (verifyText != null && null != smsContent
								&& !"".equals(smsContent)) {
							verifyText.requestFocus();// 获取焦点
							verifyText.setText(smsContent);// 设置文本
							verifyText.setSelection(smsContent.length());// 设置光标位置
							Toast.makeText(activity, smsContent,
									Toast.LENGTH_LONG).show();
						}
					}

				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			CrashReport.postCatchedException(e);
		}

	}
}