package net.cmono.providers;

import android.net.Uri;
import android.provider.BaseColumns;

public final class Configs {

	// �����AUTHORTYΪ����ȫ��+ContentProvider�����ȫ��
	public static final String AUTHORTY = "net.cmono.providers.configcontentprovider";
	public static final String XMLAUTHORTY = "net.cmono.providers.xmlconfigcontentprovider";
	// ����Provider
	public static final String SPAUTHORTY = "net.cmono.providers.specialdaycontentprovider";
	// ���춯������ Provider
	public static final String TTPODAUTHORTY = "net.cmono.providers.ttpodcontentprovider";
	// Ϻ�����ַ��� Provider
	public static final String XIAMIAUTHORTY = "net.cmono.providers.xiamicontentprovider";
	// ������������Ϣ Provider
	public static final String LCMSGAUTHORTY = "net.cmono.providers.lockscreenmessagecontentprovider";
	// ���ݿ�����
	public static final String DATABASE_NAME = "GetAlarm.db";
	// ���ݿ�汾
	public static final int DATABASE_VERSION = 1;
	// ���ݿ����
	public static final String LSC_TABLE_NAME = "configs";

	// ���е��ֱ�
	public static final class Config implements BaseColumns {

		// �ӱ���
		public static final String TABLE_NAME = "config";
		// ����ContentProvider��URL��CONTENT_URIΪ����URL; parse�ǽ��ı�ת����URL
		public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORTY
				+ "/configs");

		// ����ContentProvider�б����������
		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.cmono.providers.lockscreenconfigs";
		// ����ContentProvider����item��һ����������
		public static final String CONTENT_TYPE_ITEM = "vnd.android.cursor.item/vnd.cmono.providers.lockscreenconfigs";

		// ���м�¼��Ĭ�������㷨�������ǽ�������
		public static final String DEFAULT_SORT_ORDER = "_id desc";

		// ���ݿ��еı��ֶ�
		public static final String NAME = "name";// ��������
		public static final String Value = "value";// ������ֵ
		public static final String Avator = "avator";
	}

}