package net.cmono.dtos;

import net.cmono.utils.ImageUtil;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

//Model�� �������洢Ӧ�ó�����Ϣ
public class AppInfo {

	private String appLabel; // Ӧ�ó����ǩ
	private Drawable appIcon; // Ӧ�ó���ͼ��

	/*public String getAppBase64Icon() {
		// ʵ��������һ��BitmapDrawable����
		BitmapDrawable bitmapDrawable = (BitmapDrawable) appIcon;
		// �����ڵ���getBitmap�������õ����λͼ
		Bitmap bitmap = bitmapDrawable.getBitmap();
		return ImageUtil.ImageToBase64(bitmap);
	}*/

	private Intent intent; // ����Ӧ�ó����Intent
							// ��һ����ActionΪMain��CategoryΪLancher��Activity
	private String pkgName; // Ӧ�ó�������Ӧ�İ���

	public AppInfo() {
	}

	public String getAppLabel() {
		return appLabel;
	}

	public void setAppLabel(String appName) {
		this.appLabel = appName;
	}

	public Drawable getAppIcon() {
		return appIcon;
	}

	public void setAppIcon(Drawable appIcon) {
		this.appIcon = appIcon;
	}

	public Intent getIntent() {
		return intent;
	}

	public void setIntent(Intent intent) {
		this.intent = intent;
	}

	public String getPkgName() {
		return pkgName;
	}

	public void setPkgName(String pkgName) {
		this.pkgName = pkgName;
	}
}
