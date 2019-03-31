package net.cmono.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.cmono.getalarm.GetAlarmApplication;
import net.cmono.getalarm.R;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

public class WifiStatusUtil {

	private Context mContext;

	// 创建一个tManager类的实例
	WifiManager wManager;
	WifiInfo wifiInfo;
	// 声明一个表示WIFI状态名的数组
	String[] statusName = new String[] {};
	// 声明一个表示WIFI状态值得集合
	ArrayList<String> statusValue = new ArrayList<String>();

	public WifiStatusUtil() {
		mContext = GetAlarmApplication.getAppContext();
		wManager = (WifiManager) mContext
				.getSystemService(Context.WIFI_SERVICE);
		wifiInfo = wManager.getConnectionInfo();
	}

	public boolean IsWifiEnabled() {
		return wManager.getWifiState() == WifiManager.WIFI_STATE_ENABLED;
	}

	private String intToIp(int ip) {
		return (ip & 0xFF) + "." + ((ip >> 8) & 0xFF) + "."
				+ ((ip >> 16) & 0xFF) + "." + ((ip >> 24) & 0xFF);
	}

	public List<Map<String, Object>> GetStatus() {
		List<Map<String, Object>> listItems;

		// 获取表示各种状态名的数组
		statusName = mContext.getResources().getStringArray(
				R.array.wifiStatusName);

		statusValue.add(wifiInfo.getSSID());
		statusValue.add(wifiInfo.getBSSID());
		statusValue.add(Boolean.toString(wifiInfo.getHiddenSSID()));
		statusValue.add(intToIp(wifiInfo.getIpAddress()));
		statusValue.add(Integer.toString(wifiInfo.getLinkSpeed()));
		statusValue.add(wifiInfo.getMacAddress());
		statusValue.add(Integer.toString(wifiInfo.getRssi()));

		listItems = new ArrayList<Map<String, Object>>();
		// 遍历statusValues集合，将statusNames、statusValues
		// 的数据封装到List<Map<String , String>>集合中
		for (int i = 0; i < statusName.length; i++) {
			Map<String, Object> listItem = new HashMap<String, Object>();
			listItem.put("name", statusName[i]);
			listItem.put("value", statusValue.get(i));
			listItems.add(listItem);
		}

		return listItems;
	}

}
