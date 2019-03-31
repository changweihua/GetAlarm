package net.cmono.dtos;

import net.cmono.utils.ImageUtil;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

//Model类 ，用来存储应用程序信息
public class AppInfo {

	private String appLabel; // 应用程序标签
	private Drawable appIcon; // 应用程序图像

	/*public String getAppBase64Icon() {
		// 实际上这是一个BitmapDrawable对象
		BitmapDrawable bitmapDrawable = (BitmapDrawable) appIcon;
		// 可以在调用getBitmap方法，得到这个位图
		Bitmap bitmap = bitmapDrawable.getBitmap();
		return ImageUtil.ImageToBase64(bitmap);
	}*/

	private Intent intent; // 启动应用程序的Intent
							// ，一般是Action为Main和Category为Lancher的Activity
	private String pkgName; // 应用程序所对应的包名

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
