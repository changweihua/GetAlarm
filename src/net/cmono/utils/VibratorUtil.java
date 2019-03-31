package net.cmono.utils;

import android.app.Service;
import android.content.Context;
import android.os.Vibrator;

public class VibratorUtil {
	/**
	 * final Activity activity �����ø÷�����Activityʵ�� long milliseconds ���𶯵�ʱ������λ�Ǻ���
	 * long[] pattern ���Զ�����ģʽ �����������ֵĺ���������[��ֹʱ������ʱ������ֹʱ������ʱ��������]ʱ���ĵ�λ�Ǻ���
	 * boolean isRepeat �� �Ƿ񷴸��𶯣������true�������𶯣������false��ֻ��һ��
	 */

	public static void Vibrate(final Context context, long milliseconds) {
		Vibrator vib = (Vibrator) context
				.getSystemService(Context.VIBRATOR_SERVICE);
		vib.vibrate(milliseconds);
	}

	public static void Vibrate(final Context context, long[] pattern,
			boolean isRepeat) {
		Vibrator vib = (Vibrator) context
				.getSystemService(Context.VIBRATOR_SERVICE);
		vib.vibrate(pattern, isRepeat ? 1 : -1);
	}
}
