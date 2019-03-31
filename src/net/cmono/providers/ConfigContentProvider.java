package net.cmono.providers;

import java.util.HashMap;

import net.cmono.providers.Configs.Config;
import net.cmono.utils.DatabaseUtil;
import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.TextUtils;

public class ConfigContentProvider extends ContentProvider {

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
		uriMatcher
				.addURI(Configs.AUTHORTY, "configs", INCOMING_USER_COLLECTION);
		// �������#��ʾΪ����:users�����ĳһ��users
		uriMatcher.addURI(Configs.AUTHORTY, "configs/#", INCOMING_USER_SIGNAL);
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
		System.out.println("query");
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		switch (uriMatcher.match(uri)) {
		case INCOMING_USER_COLLECTION:
			qb.setTables(Config.TABLE_NAME);// ���ñ������
			qb.setProjectionMap(userProjectionMap);// ����userProjectionMapΪ���潨�����˵�Hashmap
			break;
		case INCOMING_USER_SIGNAL:
			qb.setTables(Config.TABLE_NAME);// ���ñ������
			qb.setProjectionMap(userProjectionMap);// ����userProjectionMapΪ���潨�����˵�hashmap
			// uri.getPathSegments()�õ�Path���֣�����URL��Э��+authory����ȥ������ʣ�µĲ��ֶַλ�ȡ������ȡ��
			// һ����
			// qb.appendWhere(UserTableMetaData._ID + "="
			// +uri.getPathSegments().get(1));//����where����
			break;
		}
		// ����
		String orderBy = sortOrder;// ��Ϊ��ʱ��ָ�������򷽷���������
		if (TextUtils.isEmpty(sortOrder)) {
			orderBy = Config.DEFAULT_SORT_ORDER;// ������������Ϊ�յ�ʱ�����Ĭ�ϵ�����
		} 
		SQLiteDatabase db = dh.getWritableDatabase();
		// ���ô���Ĳ������в�ѯ
		Cursor c = qb.query(db, projection, selection, selectionArgs, null,
				null, orderBy);
		// ����֪ͨ
		c.setNotificationUri(getContext().getContentResolver(), uri);
		return c;
	}
}
