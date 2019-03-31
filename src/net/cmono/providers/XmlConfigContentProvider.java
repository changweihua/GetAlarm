package net.cmono.providers;

import java.util.HashMap;

import com.tencent.bugly.crashreport.CrashReport;

import net.cmono.consts.ConstValue;
import net.cmono.providers.Configs.Config;
import net.cmono.utils.DatabaseUtil;
import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.BaseColumns;

public class XmlConfigContentProvider extends ContentProvider {

	// 定义一个UriMatcher类对象，用来匹配URL:形成映射，规则是否合法
	public static final UriMatcher uriMatcher;
	// 组时的ID
	public static final int INCOMING_USER_COLLECTION = 1;
	// 单个时的ID
	public static final int INCOMING_USER_SIGNAL = 2;
	// 定义一个DatabaseHelper对象
	private DatabaseUtil dh;

	static {
		// UriMatcher.NO_MATCH表示不匹配任何路径的返回码
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(Configs.XMLAUTHORTY, "configs",
				INCOMING_USER_COLLECTION);
		// 后面加了#表示为单个:users下面的某一个users
		uriMatcher.addURI(Configs.XMLAUTHORTY, "configs/#",
				INCOMING_USER_SIGNAL);
	}
	// 新建一个HashMap,后面执行插入操作时有用
	public static HashMap<String, String> userProjectionMap;
	static {
		userProjectionMap = new HashMap<String, String>();
		// 给数据库表中的列取别名
		userProjectionMap.put(BaseColumns._ID, BaseColumns._ID);
		userProjectionMap.put(Config.NAME, Config.NAME);
		userProjectionMap.put(Config.Avator, Config.Avator);
	}

	// 得到ContentProvider的数据类型，返回的参数URL所代表的数据类型
	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		System.out.println("getType");
		switch (uriMatcher.match(uri)) {
		// matcher满足URL的前2项（即协议+路径）为第1种情况时，switch语句的值为URL的第3项，此处为INCOMING_USER_COLLECTION
		case INCOMING_USER_COLLECTION:
			return Config.CONTENT_TYPE;
		case INCOMING_USER_SIGNAL:
			return Config.CONTENT_TYPE_ITEM;
		default:
			throw new IllegalArgumentException("Unknown URI" + uri);
		}
	}

	// 该函数的返回值是一个URL
	// 这个URL表示的是刚刚使用这个函数的所插入的数据
	// content://zzl.FirstContentProvider/users/
	@Override
	public Uri insert(Uri uri, ContentValues values) {
		// TODO Auto-generated method stub
		System.out.println("insert");
		SQLiteDatabase db = dh.getWritableDatabase();
		long rowId = db.insert(Config.TABLE_NAME, null, values);
		if (rowId > 0) {
			// 发出通知给监听器，说明数据已经改变
			// ContentUris为工具类
			Uri insertedUserUri = ContentUris.withAppendedId(
					Configs.Config.CONTENT_URI, rowId);
			getContext().getContentResolver().notifyChange(insertedUserUri,
					null);

			return insertedUserUri;
		}
		throw new SQLException("Failed to insert row into" + uri);
	}

	// 回调函数，在ContentProvider创建的时候调用
	@Override
	public boolean onCreate() {
		// TODO Auto-generated method stub
		System.out.println("onCreate");
		dh = new DatabaseUtil(getContext(), Configs.DATABASE_NAME);
		return true;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		System.out.println("delete");
		return 0;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// TODO Auto-generated method stub
		System.out.println("update");
		return 0;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		// TODO Auto-generated method stub
		// System.out.println("query");
		// SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		// switch(uriMatcher.match(uri)){
		// case INCOMING_USER_COLLECTION:
		// qb.setTables(Config.TABLE_NAME);//设置表的名称
		// qb.setProjectionMap(userProjectionMap);//其中userProjectionMap为上面建立好了的Hashmap
		// break;
		// case INCOMING_USER_SIGNAL:
		// qb.setTables(Config.TABLE_NAME);//设置表的名称
		// qb.setProjectionMap(userProjectionMap);//其中userProjectionMap为上面建立好了的hashmap
		// //uri.getPathSegments()得到Path部分，即把URL的协议+authory部分去掉，把剩下的部分分段获取，这里取第
		// //一部分
		// //qb.appendWhere(UserTableMetaData._ID + "="
		// +uri.getPathSegments().get(1));//设置where条件
		// break;
		// }
		// //排序
		// String orderBy;
		// if(TextUtils.isEmpty(sortOrder)){
		// orderBy = Config.DEFAULT_SORT_ORDER;//传入的排序参数为空的时候采用默认的排序
		// }
		// else{
		// orderBy = sortOrder;//不为空时用指定的排序方法进行排序
		// }
		// SQLiteDatabase db = dh.getWritableDatabase();
		// //采用传入的参数进行查询
		// Cursor c = qb.query(db, projection, selection, selectionArgs, null,
		// null, sortOrder);
		// //发出通知
		// c.setNotificationUri(getContext().getContentResolver(), uri);
		/*
		 * Looper.prepare(); List<XmlConfig> cfgs = new ArrayList<XmlConfig>();
		 * 
		 * try {
		 * 
		 * InputStream is = getContext(). getAssets().open("config.xml");
		 * 
		 * PullXmlConfigParser parser = new PullXmlConfigParser();
		 * 
		 * cfgs = parser.parse(is);
		 * 
		 * Logger.d("XmlCfgProvider",Integer.toString(cfgs.size()));
		 * 
		 * } catch (Exception e) { // TODO Auto-generated catch block
		 * Logger.d("XmlCfgProvider", e.getMessage()); e.printStackTrace(); }
		 * 
		 * String[] columns = new String[] { "_id", "name", "price" };
		 * MatrixCursor stringCursor = new MatrixCursor(columns); String row[] =
		 * new String[3]; row[0] = Integer.toString(cfgs.get(0).getId()) ;
		 * row[1] = cfgs.get(0).getName(); row[2] =
		 * Float.toString(cfgs.get(0).getPrice()) ; stringCursor.addRow(row);
		 * Logger.d("XmlCfgProvider", row[1]); //Looper.loop();
		 * Looper.myLooper().quit();
		 */
		String[] columns = new String[] { "_id", "nickName", "showSIMInfo",
				"showNickName", "word", "showWord", "showBatteryCharging",
				"showBatteryLosing", "showMsg", "showWeekDay",
				"showUnlockInfo", "showHoroscope", "horoscope", "showFish",
				"showKJGJP", "showTraditionalDayCover" };
		MatrixCursor stringCursor = new MatrixCursor(columns);
		try {

			// 访问网络

			SharedPreferences sp = getContext().getSharedPreferences(
					ConstValue.SETTING_PF, Context.MODE_PRIVATE);

			String row[] = new String[columns.length];
			row[0] = "1";
			row[1] = sp.getString("nickName", "");
			row[2] = sp.getBoolean("showSIMInfo", true) ? "1" : null;
			row[3] = sp.getBoolean("showNickName", true) ? "1" : null;
			row[4] = sp.getString("word",
					"A plant may produce new flowers; man is young but once. ");
			row[5] = sp.getBoolean("showWord", true) ? "1" : null;
			row[6] = sp.getBoolean("showBatteryCharging", true) ? "1" : null;
			row[7] = sp.getBoolean("showBatteryLosing", true) ? "1" : null;
			row[8] = sp.getBoolean("showMsg", true) ? "1" : null;
			row[9] = sp.getBoolean("showWeekDay", true) ? "1" : null;
			row[10] = sp.getBoolean("showUnlockInfo", true) ? "1" : null;
			row[11] = sp.getBoolean("showHoroscope", true) ? "1" : null;
			row[12] = sp.getString("horoscope", "horoscope_icon");
			row[13] = sp.getBoolean("showFish", true) ? "1" : null;
			row[14] = sp.getBoolean("showKJGJP", false) ? "1" : null;
			row[15] = sp.getBoolean("showTraditionalDayCover", false) ? "1" : null;
			stringCursor.addRow(row);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			CrashReport.postCatchedException(e);
		}
		return stringCursor;

	}

}
