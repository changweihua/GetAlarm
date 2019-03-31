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

	// ����һ��tManager���ʵ��
	TelephonyManager tManager;
	// ����һ����ʾSim��״̬��������
	String[] statusName = new String[] {};
	// ����һ����ʾSim��״ֵ̬�ü���
	ArrayList<String> statusValue = new ArrayList<String>();

	public TelephonyStatusUtil() {
		mContext = GetAlarmApplication.getAppContext();
		// ��ȡϵͳ��tManager����
		tManager = (TelephonyManager) mContext
				.getSystemService(Context.TELEPHONY_SERVICE);
	}

	public List<Map<String, Object>> GetStatus() {

		List<Map<String, Object>> listItems;

		// ��ȡ��ʾ����״̬��������
		statusName = mContext.getResources().getStringArray(R.array.statusName);
		// ��ȡ��ʾsim��״̬�ĵ�����
		String simStatus[] = mContext.getResources().getStringArray(
				R.array.simStatus);
		// ��ȡ��ʾ�ֻ����͵�����
		String phoneType[] = mContext.getResources().getStringArray(
				R.array.phoneType);
		// ��ȡ�豸���
		statusValue.add(tManager.getDeviceId());
		// ��ȡ�豸����
		statusValue.add(phoneType[tManager.getPhoneType()]);
		// ��ȡ����汾
		statusValue.add(tManager.getDeviceSoftwareVersion() == null ? "δ֪"
				: tManager.getDeviceSoftwareVersion());
		// ��ȡ�豸��ǰλ��
		statusValue.add(tManager.getCellLocation() == null ? "δ֪" : tManager
				.getCellLocation().toString());
		statusValue.add(tManager.getNeighboringCellInfo() == null ? "δ֪"
				: tManager.getCellLocation().toString());
		// ��ȡ�豸����״̬
		switch (tManager.getCallState()) {
		case TelephonyManager.CALL_STATE_IDLE:
			statusValue.add("����");
			break;
		case TelephonyManager.CALL_STATE_OFFHOOK:
			statusValue.add("����ͨ��");
			break;
		case TelephonyManager.CALL_STATE_RINGING:
			statusValue.add("�ȴ�����");
			break;
		default:
			break;
		}
		// ��ȡ�绰����
		statusValue.add(tManager.getLine1Number());
		// ��ȡ��Ӫ�̵Ĺ��Ҵ���
		statusValue.add(tManager.getNetworkCountryIso());
		// ��ȡ��Ӫ�̵�����
		statusValue.add(tManager.getNetworkOperatorName());
		// ��ȡ��������
		statusValue.add(getNetworkType(tManager.getNetworkType()));
		// ��ȡSPN
		statusValue.add(tManager.getSimOperatorName().equals("") ? "δ֪"
				: tManager.getSimOperatorName());
		// ��ȡSIM�������к�
		statusValue.add(tManager.getSimSerialNumber());
		// ��ȡSIM��״̬
		statusValue.add(simStatus[tManager.getSimState()]);
		statusValue.add(isNetworkRoaming() ? "��������" : "û������");
		listItems = new ArrayList<Map<String, Object>>();
		// ����statusValues���ϣ���statusNames��statusValues
		// �����ݷ�װ��List<Map<String , String>>������
		for (int i = 0; i < statusName.length; i++) {
			Map<String, Object> listItem = new HashMap<String, Object>();
			listItem.put("name", statusName[i]);
			listItem.put("value", statusValue.get(i));
			listItems.add(listItem);
		}

		return listItems;
	}

	/**
	 * ��ȡISO��׼�Ĺ����룬�����ʳ�;���š�<br/>
	 * ע�⣺�����û���������ע�����Ч��<br/>
	 * ��CDMA�����н��Ҳ���ɿ���<br/>
	 * 
	 * @return
	 */
	private String getNetworkCountryIso() {
		return tManager.getNetworkCountryIso();
	}

	/**
	 * MCC+MNC(mobile country code + mobile network code)<br/>
	 * ע�⣺�����û���������ע��ʱ��Ч��<br/>
	 * ��CDMA�����н��Ҳ���ɿ���<br/>
	 * 
	 * @return
	 */
	private String getNetworkOperator() {
		return tManager.getNetworkOperator();
	}

	/**
	 * ������ĸ�����current registered operator(��ǰ��ע����û�)������<br/>
	 * ע�⣺�����û���������ע��ʱ��Ч��<br/>
	 * ��CDMA�����н��Ҳ���ɿ���
	 * 
	 * @return
	 */
	private String getNetworkOperatorName() {
		return tManager.getNetworkOperatorName();
	}

	/**
	 * ��ǰʹ�õ��������ͣ�<br/>
	 * NETWORK_TYPE_UNKNOWN ��������δ֪ 0<br/>
	 * NETWORK_TYPE_GPRS GPRS���� 1<br/>
	 * NETWORK_TYPE_EDGE EDGE���� 2<br/>
	 * NETWORK_TYPE_UMTS UMTS���� 3<br/>
	 * NETWORK_TYPE_HSDPA HSDPA���� 8<br/>
	 * NETWORK_TYPE_HSUPA HSUPA���� 9<br/>
	 * NETWORK_TYPE_HSPA HSPA���� 10<br/>
	 * NETWORK_TYPE_CDMA CDMA����,IS95A �� IS95B. 4<br/>
	 * NETWORK_TYPE_EVDO_0 EVDO����, revision 0. 5<br/>
	 * NETWORK_TYPE_EVDO_A EVDO����, revision A. 6<br/>
	 * NETWORK_TYPE_1xRTT 1xRTT���� 7<br/>
	 * ���й�����ͨ��3GΪUMTS��HSDPA���ƶ�����ͨ��2GΪGPRS��EGDE�����ŵ�2GΪCDMA�����ŵ�3GΪEVDO<br/>
	 * 
	 * @return
	 */
	private int getNetworkType() {
		return tManager.getNetworkType();
	}

	/**
	 * �����ƶ��ն˵����ͣ�<br/>
	 * PHONE_TYPE_CDMA �ֻ���ʽΪCDMA������<br/>
	 * PHONE_TYPE_GSM �ֻ���ʽΪGSM���ƶ�����ͨ<br/>
	 * PHONE_TYPE_NONE �ֻ���ʽδ֪<br/>
	 * 
	 * @return
	 */
	private int getPhoneType() {
		return tManager.getPhoneType();
	}

	/**
	 * ��ȡISO�����룬�൱���ṩSIM���Ĺ����롣
	 * 
	 * @return Returns the ISO country code equivalent for the SIM provider's
	 *         country code.
	 */
	private String getSimCountryIso() {
		return tManager.getSimCountryIso();
	}

	/**
	 * ��ȡSIM���ṩ���ƶ���������ƶ�������.5��6λ��ʮ��������.<br/>
	 * SIM����״̬������ SIM_STATE_READY(ʹ��getSimState()�ж�).
	 * 
	 * @return Returns the MCC+MNC (mobile country code + mobile network code)
	 *         of the provider of the SIM. 5 or 6 decimal digits.
	 */
	private String getSimOperator() {
		return tManager.getSimOperator();
	}

	/**
	 * ���������ƣ�<br/>
	 * ���磺�й��ƶ�����ͨ<br/>
	 * SIM����״̬������ SIM_STATE_READY(ʹ��getSimState()�ж�).
	 * 
	 * @return
	 */
	private String getSimOperatorName() {
		return tManager.getSimOperatorName();
	}

	/**
	 * SIM�������кţ�<br/>
	 * ��ҪȨ�ޣ�READ_PHONE_STATE
	 * 
	 * @return
	 */
	private String getSimSerialNumber() {
		return tManager.getSimSerialNumber();
	}

	/**
	 * SIM��״̬��Ϣ��<br/>
	 * SIM_STATE_UNKNOWN δ֪״̬ 0<br/>
	 * SIM_STATE_ABSENT û�忨 1<br/>
	 * SIM_STATE_PIN_REQUIRED ����״̬����Ҫ�û���PIN����� 2<br/>
	 * SIM_STATE_PUK_REQUIRED ����״̬����Ҫ�û���PUK����� 3<br/>
	 * SIM_STATE_NETWORK_LOCKED ����״̬����Ҫ�����PIN����� 4<br/>
	 * SIM_STATE_READY ����״̬ 5
	 * 
	 * @return
	 */
	private int getSimState() {
		return tManager.getSimState();
	}

	/**
	 * Ψһ���û�ID��<br/>
	 * ���磺IMSI(�����ƶ��û�ʶ����) for a GSM phone.<br/>
	 * ��ҪȨ�ޣ�READ_PHONE_STATE
	 * 
	 * @return
	 */
	private String getSubscriberId() {
		return tManager.getSubscriberId();
	}

	/**
	 * ȡ�ú������ʼ���صı�ǩ����Ϊʶ���<br/>
	 * ��ҪȨ�ޣ�READ_PHONE_STATE
	 * 
	 * @return
	 */
	private String getVoiceMailAlphaTag() {
		return tManager.getVoiceMailAlphaTag();
	}

	/**
	 * ��ȡ�����ʼ����룺<br/>
	 * ��ҪȨ�ޣ�READ_PHONE_STATE
	 * 
	 * @return
	 */
	private String getVoiceMailNumber() {
		return tManager.getVoiceMailNumber();
	}

	/**
	 * ICC���Ƿ����
	 * 
	 * @return
	 */
	private boolean hasIccCard() {
		return tManager.hasIccCard();
	}

	/**
	 * �Ƿ�����:(��GSM��;��)
	 * 
	 * @return
	 */
	private boolean isNetworkRoaming() {
		return tManager.isNetworkRoaming();
	}

	/**
	 * ��ȡ���ݻ״̬<br/>
	 * DATA_ACTIVITY_IN ��������״̬��������ڽ�������<br/>
	 * DATA_ACTIVITY_OUT ��������״̬��������ڷ�������<br/>
	 * DATA_ACTIVITY_INOUT ��������״̬��������ڽ��ܺͷ�������<br/>
	 * DATA_ACTIVITY_NONE ��������״̬������������ݷ��ͺͽ���<br/>
	 * 
	 * @return
	 */
	private int getDataActivity() {
		return tManager.getDataActivity();
	}

	/**
	 * ��ȡ��������״̬<br/>
	 * DATA_CONNECTED ��������״̬��������<br/>
	 * DATA_CONNECTING ��������״̬����������<br/>
	 * DATA_DISCONNECTED ��������״̬���Ͽ�<br/>
	 * DATA_SUSPENDED ��������״̬����ͣ<br/>
	 * 
	 * @return
	 */
	private int getDataState() {
		return tManager.getDataState();
	}

	// ��ȡ�ֻ���������
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
