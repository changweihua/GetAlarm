package net.cmono.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.cmono.consts.ConstValue;
import net.cmono.dtos.AppInfo;
import net.cmono.dtos.AppJsonInfo;
import net.cmono.getalarm.GetAlarmApplication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

import com.google.gson.Gson;
import com.tencent.bugly.crashreport.CrashReport;

public class NotiRuleUtil {

	public static void ReloadNotiIds() {
		
		if(GetAlarmApplication.packageNames.size() == 0){
			ReloadNotis();
		}
		
		for (int i = 0; i < GetAlarmApplication.packageNames.size(); i++) {// 遍历JSONArray
			GetAlarmApplication.iNotiIdList.put(i,
					GetAlarmApplication.packageNames.get(i));
		}
	}

	class AppJsons {
		private String version;
		private ArrayList<AppJsonInfo> appJsonInfoList;
	}

	public static void ReloadAppInfos() {
		List<AppInfo> apps = APPGCUtil.queryFilterAppInfo(
				GetAlarmApplication.getAppContext(), 0);
		// GetAlarmApplication.AppInfos = apps;

		if (GetAlarmApplication.packageNames.size() == 0) {
			ReloadNotis();
		}

		ArrayList<AppInfo> list = new ArrayList<AppInfo>();
		
		for (String pkgName : GetAlarmApplication.packageNames) {
			for (AppInfo appInfo : apps) {
				if (appInfo.getPkgName().equals(pkgName)) {
					list.add(appInfo);
					break;
				}
			}
		}
		
		GetAlarmApplication.AppInfos = list;
	}

	public static void ReloadAppJsonInfos() {
		if (GetAlarmApplication.AppInfos.size() == 0) {
			ReloadAppInfos();
		}
		// 实际上这是一个BitmapDrawable对象
		BitmapDrawable bitmapDrawable = null;

		for (AppInfo appInfo : GetAlarmApplication.AppInfos) {
			GetAlarmApplication.AppInfos.add(appInfo);
			bitmapDrawable = (BitmapDrawable) appInfo.getAppIcon();
			// 可以在调用getBitmap方法，得到这个位图
			Bitmap bitmap = bitmapDrawable.getBitmap();
			AppJsonInfo appJsonInfo = new AppJsonInfo();
			appJsonInfo.setAppLabel(appInfo.getAppLabel());
			appJsonInfo.setIconJson(ImageUtil.ImageToBase64(bitmap));
			appJsonInfo.setPkgName(appInfo.getPkgName());
			GetAlarmApplication.appJsonInfoList.add(appJsonInfo);
		}

		// 序列化到文件
		Gson gson = new Gson();
		String json = gson.toJson(GetAlarmApplication.appJsonInfoList);
		String fName = ConstValue.APP_INFOS_JSON;
		// 结果保存到文件中
		FileUtil.saveFile(json, fName);

	}

	public static void ReloadNotis() {

		GetAlarmApplication.packageNames.add("com.smartisan.email");
		GetAlarmApplication.packageNames.add("com.tencent.mobileqq");
		GetAlarmApplication.packageNames.add("com.tencent.mobileqqi");
		GetAlarmApplication.packageNames.add("com.alibaba.android.rimet");
		GetAlarmApplication.packageNames.add("com.tencent.qqlite");
		GetAlarmApplication.packageNames.add("com.tencent.mobileqq2");
		GetAlarmApplication.packageNames.add("com.kingsoft.email");
		GetAlarmApplication.packageNames.add("com.tencent.mm");
		GetAlarmApplication.packageNames
				.add("com.aliyun.wireless.vos.appstore");
		GetAlarmApplication.packageNames
				.add("com.yunos.baseservice.cmns_client");
		GetAlarmApplication.packageNames.add("com.android.calendar");
		GetAlarmApplication.packageNames.add("com.android.providers.calendar");
		GetAlarmApplication.packageNames.add("com.aliyun.video");
		GetAlarmApplication.packageNames.add("com.aliyun.video.youku");
		GetAlarmApplication.packageNames.add("com.aliyun.SecurityCenter");
		GetAlarmApplication.packageNames.add("com.aliyun.SecurityService");

		// 读取自定义读取APP信息
		String fName = ConstValue.ROOTFOLDER + "notis.json";
		File file = new File(fName);
		if (file.exists()) {
			String source;
			try {
				source = FileUtil.readFile(fName);
				// 创建一个JSON对象
				JSONObject jsonObject = new JSONObject(source);
				JSONArray array = jsonObject.getJSONArray("pkgs");
				int length = array.length();
				SharedPreferences sharedPreferences = GetAlarmApplication
						.getAppContext().getSharedPreferences(
								ConstValue.APP_PF,
								android.content.Context.MODE_PRIVATE);
				sharedPreferences.edit()
						.putString("pkgsV", jsonObject.getString("version"))
						.commit();
				for (int i = 0; i < length; i++) {// 遍历JSONArray
					JSONObject oj = array.getJSONObject(i);
					String pkgname = oj.getString("pkgname").trim();
					String pkgicon = oj.getString("pkgicon").trim();
					GetAlarmApplication.packageNames.add(pkgname);
				}
			} catch (IOException | JSONException e) {
				// TODO Auto-generated catch block
				CrashReport.postCatchedException(e);
			}

		}

	}

	public static void ReloadiGnores() {

		// 读取消息过滤规则
		String fIGName = ConstValue.ROOTFOLDER + "ignores.json";
		File fIG = new File(fIGName);
		if (fIG.exists()) {
			String source;
			try {
				source = FileUtil.readFile(fIGName);
				// 创建一个JSON对象
				JSONObject jsonObject = new JSONObject(source);
				JSONArray array = jsonObject.getJSONArray("igs");
				SharedPreferences sharedPreferences = GetAlarmApplication
						.getAppContext().getSharedPreferences(
								ConstValue.APP_PF,
								android.content.Context.MODE_PRIVATE);
				sharedPreferences.edit()
						.putString("igsV", jsonObject.getString("version"))
						.commit();
				int length = array.length();
				for (int i = 0; i < length; i++) {// 遍历JSONArray
					JSONObject oj = array.getJSONObject(i);
					String igpkg = oj.getString("igpkg").trim();
					String igtitle = oj.getString("igtitle").trim();
					String igtext = oj.getString("igtext").trim();
					GetAlarmApplication.iGnorePkgList.add(igpkg);
					GetAlarmApplication.iGnoreTitleList.add(igtitle);
					GetAlarmApplication.iGnoreTextList.add(igtext);
				}
			} catch (IOException | JSONException e) {
				// TODO Auto-generated catch block
				CrashReport.postCatchedException(e);
			}

		}

	}

}
