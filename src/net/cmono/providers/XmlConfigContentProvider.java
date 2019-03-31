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

	// ����һ��UriMatcher���������ƥ��URL:�γ�ӳ�䣬�����Ƿ�Ϸ�
	public static final UriMatcher uriMatcher;
	// ��ʱ��ID
	public static final int INCOMING_USER_COLLECTION = 1;
	// ����ʱ��ID
	public static final int INCOMING_USER_SIGNAL = 2;
	// ����һ��DatabaseHelper����
	private DatabaseUtil dh;

	static {
		// UriMatcher.NO_MATCH��ʾ��ƥ���κ�·���ķ�����
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(Configs.XMLAUTHORTY, "configs",
				INCOMING_USER_COLLECTION);
		// �������#��ʾΪ����:users�����ĳһ��users
		uriMatcher.addURI(Configs.XMLAUTHORTY, "configs/#",
				INCOMING_USER_SIGNAL);
	}
	// �½�һ��HashMap,����ִ�в������ʱ����
	public static HashMap<String, String> userProjectionMap;
	static {
		userProjectionMap = new HashMap<String, String>();
		// �����ݿ���е���ȡ����
		userProjectionMap.put(BaseColumns._ID, BaseColumns._ID);
		userProjectionMap.put(Config.NAME, Config.NAME);
		userProjectionMap.put(Config.Avator, Config.Avator);
	}

	// �õ�ContentProvider���������ͣ����صĲ���URL���������������
	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		System.out.println("getType");
		switch (uriMatcher.match(uri)) {
		// matcher����URL��ǰ2���Э��+·����Ϊ��1�����ʱ��switch����ֵΪURL�ĵ�3��˴�ΪINCOMING_USER_COLLECTION
		case INCOMING_USER_COLLECTION:
			return Config.CONTENT_TYPE;
		case INCOMING_USER_SIGNAL:
			return Config.CONTENT_TYPE_ITEM;
		default:
			throw new IllegalArgumentException("Unknown URI" + uri);
		}
	}

	// �ú����ķ���ֵ��һ��URL
	// ���URL��ʾ���Ǹո�ʹ����������������������
	// content://zzl.FirstContentProvider/users/
	@Override
	public Uri insert(Uri uri, ContentValues values) {
		// TODO Auto-generated method stub
		System.out.println("insert");
		SQLiteDatabase db = dh.getWritableDatabase();
		long rowId = db.insert(Config.TABLE_NAME, null, values);
		if (rowId > 0) {
			// ����֪ͨ����������˵�������Ѿ��ı�
			// ContentUrisΪ������
			Uri insertedUserUri = ContentUris.withAppendedId(
					Configs.Config.CONTENT_URI, rowId);
			getContext().getContentResolver().notifyChange(insertedUserUri,
					null);

			return insertedUserUri;
		}
		throw new SQLException("Failed to insert row into" + uri);
	}

	// �ص���������ContentProvider������ʱ�����
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
		// qb.setTables(Config.TABLE_NAME);//���ñ������
		// qb.setProjectionMap(userProjectionMap);//����userProjectionMapΪ���潨�����˵�Hashmap
		// break;
		// case INCOMING_USER_SIGNAL:
		// qb.setTables(Config.TABLE_NAME);//���ñ������
		// qb.setProjectionMap(userProjectionMap);//����userProjectionMapΪ���潨�����˵�hashmap
		// //uri.getPathSegments()�õ�Path���֣�����URL��Э��+authory����ȥ������ʣ�µĲ��ֶַλ�ȡ������ȡ��
		// //һ����
		// //qb.appendWhere(UserTableMetaData._ID + "="
		// +uri.getPathSegments().get(1));//����where����
		// break;
		// }
		// //����
		// String orderBy;
		// if(TextUtils.isEmpty(sortOrder)){
		// orderBy = Config.DEFAULT_SORT_ORDER;//������������Ϊ�յ�ʱ�����Ĭ�ϵ�����
		// }
		// else{
		// orderBy = sortOrder;//��Ϊ��ʱ��ָ�������򷽷���������
		// }
		// SQLiteDatabase db = dh.getWritableDatabase();
		// //���ô���Ĳ������в�ѯ
		// Cursor c = qb.query(db, projection, selection, selectionArgs, null,
		// null, sortOrder);
		// //����֪ͨ
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

			// ��������

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
