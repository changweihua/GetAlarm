package net.cmono.utils;

//import com.amap.api.location.AMapLocation;

public class AMapUtil {

	/**
	 * ��ʼ��λ
	 */
	public final static int MSG_LOCATION_START = 0;
	/**
	 * ��λ���
	 */
	public final static int MSG_LOCATION_FINISH = 1;
	/**
	 * ֹͣ��λ
	 */
	public final static int MSG_LOCATION_STOP = 2;

	/**
	 * ���ݶ�λ������ض�λ��Ϣ���ַ���
	 * 
	 * @param loc
	 * @return
	 */
//	public synchronized static String getLocationStr(AMapLocation location) {
//		if (null == location) {
//			return null;
//		}
//		StringBuffer sb = new StringBuffer();
//		// errCode����0����λ�ɹ���������Ϊ��λʧ�ܣ�����Ŀ��Բ��չ�����λ������˵��
//		if (location.getErrorCode() == 0) {
//			sb.append("��λ�ɹ�" + "\n");
//			sb.append("��λ����: " + location.getLocationType() + "\n");
//			sb.append("��    ��    : " + location.getLongitude() + "\n");
//			sb.append("γ    ��    : " + location.getLatitude() + "\n");
//			sb.append("��    ��    : " + location.getAccuracy() + "��" + "\n");
//			sb.append("�ṩ��    : " + location.getProvider() + "\n");
//
//			if (location.getProvider().equalsIgnoreCase(
//					android.location.LocationManager.GPS_PROVIDER)) {
//				// ������Ϣֻ���ṩ����GPSʱ�Ż���
//				sb.append("��    ��    : " + location.getSpeed() + "��/��" + "\n");
//				sb.append("��    ��    : " + location.getBearing() + "\n");
//				// ��ȡ��ǰ�ṩ��λ��������Ǹ���
//				sb.append("��    ��    : "
//						+ location.getExtras().getInt("satellites", 0) + "\n");
//			} else {
//				// �ṩ����GPSʱ��û��������Ϣ��
//				sb.append("��    ��    : " + location.getCountry() + "\n");
//				sb.append("ʡ            : " + location.getProvince() + "\n");
//				sb.append("��            : " + location.getCity() + "\n");
//				sb.append("���б��� : " + location.getCityCode() + "\n");
//				sb.append("��            : " + location.getDistrict() + "\n");
//				sb.append("���� ��   : " + location.getAdCode() + "\n");
//				sb.append("��    ַ    : " + location.getAddress() + "\n");
//				sb.append("��Ȥ��    : " + location.getPoiName() + "\n");
//			}
//		} else {
//			// ��λʧ��
//			sb.append("��λʧ��" + "\n");
//			sb.append("������:" + location.getErrorCode() + "\n");
//			sb.append("������Ϣ:" + location.getErrorInfo() + "\n");
//			sb.append("��������:" + location.getLocationDetail() + "\n");
//		}
//		return sb.toString();
//	}
}
