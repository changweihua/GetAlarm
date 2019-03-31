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

	public static final int FILTER_ALL_APP = 0; // ����Ӧ�ó���
	public static final int FILTER_SYSTEM_APP = 1; // ϵͳ����
	public static final int FILTER_THIRD_APP = 2; // ������Ӧ�ó���
	public static final int FILTER_SDCARD_APP = 3; // ��װ��SDCard��Ӧ�ó���

	public static void SaveAsJson(Context context, int filter) {
		List<AppInfo> apps = queryFilterAppInfo(context, filter);
		Gson gson = new Gson();
		String json = gson.toJson(apps);
		Logger.d("APPGCHelper", json);
	}

	// ���ݲ�ѯ��������ѯ�ض���ApplicationInfo
	public static List<AppInfo> queryFilterAppInfo(Context context, int filter) {
		PackageManager pm = context.getPackageManager();
		// ��ѯ�����Ѿ���װ��Ӧ�ó���
		List<ApplicationInfo> listAppcations = pm
				.getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES);
		Collections.sort(listAppcations,
				new ApplicationInfo.DisplayNameComparator(pm));// ����
		List<AppInfo> appInfos = new ArrayList<AppInfo>(); // ������˲鵽��AppInfo
		// ��������������
		switch (filter) {
		case FILTER_ALL_APP: // ����Ӧ�ó���
			appInfos.clear();
			for (ApplicationInfo app : listAppcations) {
				appInfos.add(getAppInfo(app, pm));
			}
			return appInfos;
		case FILTER_SYSTEM_APP: // ϵͳ����
			appInfos.clear();
			for (ApplicationInfo app : listAppcations) {
				if ((app.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
					appInfos.add(getAppInfo(app, pm));
				}
			}
			return appInfos;
		case FILTER_THIRD_APP: // ������Ӧ�ó���
			appInfos.clear();
			for (ApplicationInfo app : listAppcations) {
				// ��ϵͳ����
				if ((app.flags & ApplicationInfo.FLAG_SYSTEM) <= 0) {
					appInfos.add(getAppInfo(app, pm));
				}
				// ������ϵͳ���򣬱��û��ֶ����º󣬸�ϵͳ����Ҳ��Ϊ������Ӧ�ó�����
				else if ((app.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0) {
					appInfos.add(getAppInfo(app, pm));
				}
			}
			break;
		case FILTER_SDCARD_APP: // ��װ��SDCard��Ӧ�ó���
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

	// ����һ��AppInfo���� ������ֵ
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
			// �жϸ�������Ƿ���/data/appĿ¼��
			ApplicationInfo appInfo = p.applicationInfo;
			/**
			 * Value for {@link #flags}: if set, this application is installed
			 * in the device's system image.
			 */
			if ((appInfo.flags & ApplicationInfo.FLAG_SYSTEM) > 0) {
				// ϵͳ����
				name = p.applicationInfo.loadLabel(pckMan).toString();
				Logger.d("APP GC", "ϵͳ���� " + name);
				// Log.e(" ϵͳ����app name==", name);
			} else {
				// ����ϵͳ����
				name = p.applicationInfo.loadLabel(pckMan).toString();
				// Log.e(" ����ϵͳ����app name==", name);
				Logger.d("APP GC", "�������� " + name);
			}
		}
	}
}
