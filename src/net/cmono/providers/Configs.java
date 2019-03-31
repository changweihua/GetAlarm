package net.cmono.providers;

import android.net.Uri;
import android.provider.BaseColumns;

public final class Configs {

	// 这里的AUTHORTY为包的全名+ContentProvider子类的全名
	public static final String AUTHORTY = "net.cmono.providers.configcontentprovider";
	public static final String XMLAUTHORTY = "net.cmono.providers.xmlconfigcontentprovider";
	// 节日Provider
	public static final String SPAUTHORTY = "net.cmono.providers.specialdaycontentprovider";
	// 天天动听封面 Provider
	public static final String TTPODAUTHORTY = "net.cmono.providers.ttpodcontentprovider";
	// 虾米音乐封面 Provider
	public static final String XIAMIAUTHORTY = "net.cmono.providers.xiamicontentprovider";
	// 锁屏第三方信息 Provider
	public static final String LCMSGAUTHORTY = "net.cmono.providers.lockscreenmessagecontentprovider";
	// 数据库名称
	public static final String DATABASE_NAME = "GetAlarm.db";
	// 数据库版本
	public static final int DATABASE_VERSION = 1;
	// 数据库表名
	public static final String LSC_TABLE_NAME = "configs";

	// 表中的字表
	public static final class Config implements BaseColumns {

		// 子表名
		public static final String TABLE_NAME = "config";
		// 访问ContentProvider的URL：CONTENT_URI为常量URL; parse是将文本转换成URL
		public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORTY
				+ "/configs");

		// 返回ContentProvider中表的数据类型
		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.cmono.providers.lockscreenconfigs";
		// 返回ContentProvider表中item的一条数据类型
		public static final String CONTENT_TYPE_ITEM = "vnd.android.cursor.item/vnd.cmono.providers.lockscreenconfigs";

		// 表中记录的默认排序算法，这里是降序排列
		public static final String DEFAULT_SORT_ORDER = "_id desc";

		// 数据库中的表字段
		public static final String NAME = "name";// 配置项名
		public static final String Value = "value";// 配置项值
		public static final String Avator = "avator";
	}

}