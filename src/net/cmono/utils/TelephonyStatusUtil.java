package net.cmono.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.cmono.getalarm.GetAlarmApplication;
import net.cmono.getalarm.R;
import android.content.Context;
import android.telephony.TelephonyManager;

public class TelephonyStatusUtil {

	private Context mContext;

	// 创建一个tManager类的实例
	TelephonyManager tManager;
	// 声明一个表示Sim卡状态名的数组
	String[] statusName = new String[] {};
	// 声明一个表示Sim卡状态值得集合
	ArrayList<String> statusValue = new ArrayList<String>();

	public TelephonyStatusUtil() {
		mContext = GetAlarmApplication.getAppContext();
		// 获取系统的tManager对象
		tManager = (TelephonyManager) mContext
				.getSystemService(Context.TELEPHONY_SERVICE);
	}

	public List<Map<String, Object>> GetStatus() {

		List<Map<String, Object>> listItems;

		// 获取表示各种状态名的数组
		statusName = mContext.getResources().getStringArray(R.array.statusName);
		// 获取表示sim卡状态的的数组
		String simStatus[] = mContext.getResources().getStringArray(
				R.array.simStatus);
		// 获取表示手机类型的数组
		String phoneType[] = mContext.getResources().getStringArray(
				R.array.phoneType);
		// 获取设备编号
		statusValue.add(tManager.getDeviceId());
		// 获取设备类型
		statusValue.add(phoneType[tManager.getPhoneType()]);
		// 获取软件版本
		statusValue.add(tManager.getDeviceSoftwareVersion() == null ? "未知"
				: tManager.getDeviceSoftwareVersion());
		// 获取设备当前位置
		statusValue.add(tManager.getCellLocation() == null ? "未知" : tManager
				.getCellLocation().toString());
		statusValue.add(tManager.getNeighboringCellInfo() == null ? "未知"
				: tManager.getCellLocation().toString());
		// 获取设备呼叫状态
		switch (tManager.getCallState()) {
		case TelephonyManager.CALL_STATE_IDLE:
			statusValue.add("空闲");
			break;
		case TelephonyManager.CALL_STATE_OFFHOOK:
			statusValue.add("正在通话");
			break;
		case TelephonyManager.CALL_STATE_RINGING:
			statusValue.add("等待接听");
			break;
		default:
			break;
		}
		// 获取电话号码
		statusValue.add(tManager.getLine1Number());
		// 获取运营商的国家代码
		statusValue.add(tManager.getNetworkCountryIso());
		// 获取运营商的名称
		statusValue.add(tManager.getNetworkOperatorName());
		// 获取网络类型
		statusValue.add(getNetworkType(tManager.getNetworkType()));
		// 获取SPN
		statusValue.add(tManager.getSimOperatorName().equals("") ? "未知"
				: tManager.getSimOperatorName());
		// 获取SIM卡的序列号
		statusValue.add(tManager.getSimSerialNumber());
		// 获取SIM卡状态
		statusValue.add(simStatus[tManager.getSimState()]);
		statusValue.add(isNetworkRoaming() ? "正在漫游" : "没有漫游");
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

	/**
	 * 获取ISO标准的国家码，即国际长途区号。<br/>
	 * 注意：仅当用户已在网络注册后有效。<br/>
	 * 在CDMA网络中结果也许不可靠。<br/>
	 * 
	 * @return
	 */
	private String getNetworkCountryIso() {
		return tManager.getNetworkCountryIso();
	}

	/**
	 * MCC+MNC(mobile country code + mobile network code)<br/>
	 * 注意：仅当用户已在网络注册时有效。<br/>
	 * 在CDMA网络中结果也许不可靠。<br/>
	 * 
	 * @return
	 */
	private String getNetworkOperator() {
		return tManager.getNetworkOperator();
	}

	/**
	 * 按照字母次序的current registered operator(当前已注册的用户)的名字<br/>
	 * 注意：仅当用户已在网络注册时有效。<br/>
	 * 在CDMA网络中结果也许不可靠。
	 * 
	 * @return
	 */
	private String getNetworkOperatorName() {
		return tManager.getNetworkOperatorName();
	}

	/**
	 * 当前使用的网络类型：<br/>
	 * NETWORK_TYPE_UNKNOWN 网络类型未知 0<br/>
	 * NETWORK_TYPE_GPRS GPRS网络 1<br/>
	 * NETWORK_TYPE_EDGE EDGE网络 2<br/>
	 * NETWORK_TYPE_UMTS UMTS网络 3<br/>
	 * NETWORK_TYPE_HSDPA HSDPA网络 8<br/>
	 * NETWORK_TYPE_HSUPA HSUPA网络 9<br/>
	 * NETWORK_TYPE_HSPA HSPA网络 10<br/>
	 * NETWORK_TYPE_CDMA CDMA网络,IS95A 或 IS95B. 4<br/>
	 * NETWORK_TYPE_EVDO_0 EVDO网络, revision 0. 5<br/>
	 * NETWORK_TYPE_EVDO_A EVDO网络, revision A. 6<br/>
	 * NETWORK_TYPE_1xRTT 1xRTT网络 7<br/>
	 * 在中国，联通的3G为UMTS或HSDPA，移动和联通的2G为GPRS或EGDE，电信的2G为CDMA，电信的3G为EVDO<br/>
	 * 
	 * @return
	 */
	private int getNetworkType() {
		return tManager.getNetworkType();
	}

	/**
	 * 返回移动终端的类型：<br/>
	 * PHONE_TYPE_CDMA 手机制式为CDMA，电信<br/>
	 * PHONE_TYPE_GSM 手机制式为GSM，移动和联通<br/>
	 * PHONE_TYPE_NONE 手机制式未知<br/>
	 * 
	 * @return
	 */
	private int getPhoneType() {
		return tManager.getPhoneType();
	}

	/**
	 * 获取ISO国家码，相当于提供SIM卡的国家码。
	 * 
	 * @return Returns the ISO country code equivalent for the SIM provider's
	 *         country code.
	 */
	private String getSimCountryIso() {
		return tManager.getSimCountryIso();
	}

	/**
	 * 获取SIM卡提供的移动国家码和移动网络码.5或6位的十进制数字.<br/>
	 * SIM卡的状态必须是 SIM_STATE_READY(使用getSimState()判断).
	 * 
	 * @return Returns the MCC+MNC (mobile country code + mobile network code)
	 *         of the provider of the SIM. 5 or 6 decimal digits.
	 */
	private String getSimOperator() {
		return tManager.getSimOperator();
	}

	/**
	 * 服务商名称：<br/>
	 * 例如：中国移动、联通<br/>
	 * SIM卡的状态必须是 SIM_STATE_READY(使用getSimState()判断).
	 * 
	 * @return
	 */
	private String getSimOperatorName() {
		return tManager.getSimOperatorName();
	}

	/**
	 * SIM卡的序列号：<br/>
	 * 需要权限：READ_PHONE_STATE
	 * 
	 * @return
	 */
	private String getSimSerialNumber() {
		return tManager.getSimSerialNumber();
	}

	/**
	 * SIM的状态信息：<br/>
	 * SIM_STATE_UNKNOWN 未知状态 0<br/>
	 * SIM_STATE_ABSENT 没插卡 1<br/>
	 * SIM_STATE_PIN_REQUIRED 锁定状态，需要用户的PIN码解锁 2<br/>
	 * SIM_STATE_PUK_REQUIRED 锁定状态，需要用户的PUK码解锁 3<br/>
	 * SIM_STATE_NETWORK_LOCKED 锁定状态，需要网络的PIN码解锁 4<br/>
	 * SIM_STATE_READY 就绪状态 5
	 * 
	 * @return
	 */
	private int getSimState() {
		return tManager.getSimState();
	}

	/**
	 * 唯一的用户ID：<br/>
	 * 例如：IMSI(国际移动用户识别码) for a GSM phone.<br/>
	 * 需要权限：READ_PHONE_STATE
	 * 
	 * @return
	 */
	private String getSubscriberId() {
		return tManager.getSubscriberId();
	}

	/**
	 * 取得和语音邮件相关的标签，即为识别符<br/>
	 * 需要权限：READ_PHONE_STATE
	 * 
	 * @return
	 */
	private String getVoiceMailAlphaTag() {
		return tManager.getVoiceMailAlphaTag();
	}

	/**
	 * 获取语音邮件号码：<br/>
	 * 需要权限：READ_PHONE_STATE
	 * 
	 * @return
	 */
	private String getVoiceMailNumber() {
		return tManager.getVoiceMailNumber();
	}

	/**
	 * ICC卡是否存在
	 * 
	 * @return
	 */
	private boolean hasIccCard() {
		return tManager.hasIccCard();
	}

	/**
	 * 是否漫游:(在GSM用途下)
	 * 
	 * @return
	 */
	private boolean isNetworkRoaming() {
		return tManager.isNetworkRoaming();
	}

	/**
	 * 获取数据活动状态<br/>
	 * DATA_ACTIVITY_IN 数据连接状态：活动，正在接受数据<br/>
	 * DATA_ACTIVITY_OUT 数据连接状态：活动，正在发送数据<br/>
	 * DATA_ACTIVITY_INOUT 数据连接状态：活动，正在接受和发送数据<br/>
	 * DATA_ACTIVITY_NONE 数据连接状态：活动，但无数据发送和接受<br/>
	 * 
	 * @return
	 */
	private int getDataActivity() {
		return tManager.getDataActivity();
	}

	/**
	 * 获取数据连接状态<br/>
	 * DATA_CONNECTED 数据连接状态：已连接<br/>
	 * DATA_CONNECTING 数据连接状态：正在连接<br/>
	 * DATA_DISCONNECTED 数据连接状态：断开<br/>
	 * DATA_SUSPENDED 数据连接状态：暂停<br/>
	 * 
	 * @return
	 */
	private int getDataState() {
		return tManager.getDataState();
	}

	// 获取手机网络类型
	private String getNetworkType(int networkType) {
		// TODO Auto-generated method stub
		switch (networkType) {
		case TelephonyManager.NETWORK_TYPE_1xRTT:
			return "1xRTT";
		case TelephonyManager.NETWORK_TYPE_CDMA:
			return "CDMA";
		case TelephonyManager.NETWORK_TYPE_EDGE:
			return "EDGE";
		case TelephonyManager.NETWORK_TYPE_EHRPD:
			return "EHRPD";
		case TelephonyManager.NETWORK_TYPE_EVDO_0:
			return "EVDO_0";
		case TelephonyManager.NETWORK_TYPE_EVDO_A:
			return "EVDO_A";
		case TelephonyManager.NETWORK_TYPE_EVDO_B:
			return "EVDO_B";
		case TelephonyManager.NETWORK_TYPE_GPRS:
			return "GPRS";
		case TelephonyManager.NETWORK_TYPE_HSDPA:
			return "HSDPA";
		case TelephonyManager.NETWORK_TYPE_HSPA:
			return "HSPA";
		case TelephonyManager.NETWORK_TYPE_HSPAP:
			return "HSPAP";
		case TelephonyManager.NETWORK_TYPE_HSUPA:
			return "HSUPA";
		case TelephonyManager.NETWORK_TYPE_IDEN:
			return "IDEN";
		case TelephonyManager.NETWORK_TYPE_LTE:
			return "LTE";
		case TelephonyManager.NETWORK_TYPE_UMTS:
			return "UMTS";
		case TelephonyManager.NETWORK_TYPE_UNKNOWN:
			return "UNKNOWN";
		default:
			return "UNKNOWN";
		}
	}

}
