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
 * Created by pengkv on 15/10/12. ���ż������������Զ������֤��
 */
public class SMSContentObserver extends ContentObserver {
	public final String SMS_URI_INBOX = "content://sms/inbox";// ������
	private Activity activity = null;
	private String smsContent = "";// ��֤��
	private EditText verifyText = null;// ��֤��༭��
	private String SMS_ADDRESS_PRNUMBER = "18552015266";// ��Ϣ�����ṩ��

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
			Cursor cursor = null;// ���
			// ��ȡ�ռ�����ָ������Ķ���
			cursor = activity.getContentResolver().query(
					Uri.parse(SMS_URI_INBOX),
					new String[] { "_id", "address", "body", "read" }, // Ҫ��ȡ������
					"address=? and read=?", // ��ѯ������ʲô
					new String[] { SMS_ADDRESS_PRNUMBER, "0" },// ��ѯ������ֵ
					"date desc");// ����
			if (cursor != null) {
				cursor.moveToFirst();
				if (cursor.moveToFirst()) {
					String smsbody = cursor.getString(cursor
							.getColumnIndex("body"));

					// ��������ʽƥ����֤��
					Pattern pattern = Pattern.compile("[0-9]{6}");
					Matcher matcher = pattern.matcher(smsbody);

					if (matcher.find()) {// �Ƿ�ƥ��
						smsContent = matcher.group();// ��ȡƥ���ı�������֤��
						if (verifyText != null && null != smsContent
								&& !"".equals(smsContent)) {
							verifyText.requestFocus();// ��ȡ����
							verifyText.setText(smsContent);// �����ı�
							verifyText.setSelection(smsContent.length());// ���ù��λ��
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