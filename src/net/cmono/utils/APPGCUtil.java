package net.cmono.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.gson.Gson;

import net.cmono.dtos.AppInfo;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

public class APPGCUtil {

	public static final int FILTER_ALL_APP = 0; // 所有应用程序
	public static final int FILTER_SYSTEM_APP = 1; // 系统程序
	public static final int FILTER_THIRD_APP = 2; // 第三方应用程序
	public static final int FILTER_SDCARD_APP = 3; // 安装在SDCard的应用程序

	public static void SaveAsJson(Context context, int filter) {
		List<AppInfo> apps = queryFilterAppInfo(context, filter);
		Gson gson = new Gson();
		String json = gson.toJson(apps);
		Logger.d("APPGCHelper", json);
	}

	// 根据查询条件，查询特定的ApplicationInfo
	public static List<AppInfo> queryFilterAppInfo(Context context, int filter) {
		PackageManager pm = context.getPackageManager();
		// 查询所有已经安装的应用程序
		List<ApplicationInfo> listAppcations = pm
				.getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES);
		Collections.sort(listAppcations,
				new ApplicationInfo.DisplayNameComparator(pm));// 排序
		List<AppInfo> appInfos = new ArrayList<AppInfo>(); // 保存过滤查到的AppInfo
		// 根据条件来过滤
		switch (filter) {
		case FILTER_ALL_APP: // 所有应用程序
			appInfos.clear();
			for (ApplicationInfo app : listAppcations) {
				appInfos.add(getAppInfo(app, pm));
			}
			return appInfos;
		case FILTER_SYSTEM_APP: // 系统程序
			appInfos.clear();
			for (ApplicationInfo app : listAppcations) {
				if ((app.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
					appInfos.add(getAppInfo(app, pm));
				}
			}
			return appInfos;
		case FILTER_THIRD_APP: // 第三方应用程序
			appInfos.clear();
			for (ApplicationInfo app : listAppcations) {
				// 非系统程序
				if ((app.flags & ApplicationInfo.FLAG_SYSTEM) <= 0) {
					appInfos.add(getAppInfo(app, pm));
				}
				// 本来是系统程序，被用户手动更新后，该系统程序也成为第三方应用程序了
				else if ((app.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0) {
					appInfos.add(getAppInfo(app, pm));
				}
			}
			break;
		case FILTER_SDCARD_APP: // 安装在SDCard的应用程序
			appInfos.clear();
			for (ApplicationInfo app : listAppcations) {
				if ((app.flags & ApplicationInfo.FLAG_EXTERNAL_STORAGE) != 0) {
					appInfos.add(getAppInfo(app, pm));
				}
			}
			return appInfos;
		default:
			return null;
		}
		return appInfos;
	}

	// 构造一个AppInfo对象 ，并赋值
	private static AppInfo getAppInfo(ApplicationInfo app, PackageManager pm) {
		AppInfo appInfo = new AppInfo();
		appInfo.setAppLabel((String) app.loadLabel(pm));
		appInfo.setAppIcon(app.loadIcon(pm));
		appInfo.setPkgName(app.packageName);
		return appInfo;
	}

	public static void GC(Context context) {
		int count;
		PackageManager pckMan = context.getPackageManager();
		List<PackageInfo> packs = pckMan.getInstalledPackages(0);
		count = packs.size();
		String name;
		for (int i = 0; i < count; i++) {
			PackageInfo p = packs.get(i);
			if (p.versionName == null) {
				continue;
			}
			// 判断该软件包是否在/data/app目录下
			ApplicationInfo appInfo = p.applicationInfo;
			/**
			 * Value for {@link #flags}: if set, this application is installed
			 * in the device's system image.
			 */
			if ((appInfo.flags & ApplicationInfo.FLAG_SYSTEM) > 0) {
				// 系统程序
				name = p.applicationInfo.loadLabel(pckMan).toString();
				Logger.d("APP GC", "系统程序 " + name);
				// Log.e(" 系统程序app name==", name);
			} else {
				// 不是系统程序
				name = p.applicationInfo.loadLabel(pckMan).toString();
				// Log.e(" 不是系统程序app name==", name);
				Logger.d("APP GC", "三方程序 " + name);
			}
		}
	}
}
