package net.cmono.getalarm;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class AlarmHandlerActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		final Window win = getWindow();

		// �ĸ���־λ����˼�壬�ֱ�������״̬����ʾ��������������Ļ����������Ļ��������Activity������ʱ�����������������ʾ��
		win.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
				| WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
				| WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
				| WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

		// ��ȥ��Ӧ�ó�������� ע�⣺һ��Ҫ��setContentView֮ǰ
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		// �Լ��Ĵ���
		setContentView(R.layout.activity_alarm);
	}
}
