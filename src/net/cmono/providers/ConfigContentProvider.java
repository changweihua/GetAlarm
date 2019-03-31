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
		uriMatcher
				.addURI(Configs.AUTHORTY, "configs", INCOMING_USER_COLLECTION);
		// 后面加了#表示为单个:users下面的某一个users
		uriMatcher.addURI(Configs.AUTHORTY, "configs/#", INCOMING_USER_SIGNAL);
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
		System.out.println("query");
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		switch (uriMatcher.match(uri)) {
		case INCOMING_USER_COLLECTION:
			qb.setTables(Config.TABLE_NAME);// 设置表的名称
			qb.setProjectionMap(userProjectionMap);// 其中userProjectionMap为上面建立好了的Hashmap
			break;
		case INCOMING_USER_SIGNAL:
			qb.setTables(Config.TABLE_NAME);// 设置表的名称
			qb.setProjectionMap(userProjectionMap);// 其中userProjectionMap为上面建立好了的hashmap
			// uri.getPathSegments()得到Path部分，即把URL的协议+authory部分去掉，把剩下的部分分段获取，这里取第
			// 一部分
			// qb.appendWhere(UserTableMetaData._ID + "="
			// +uri.getPathSegments().get(1));//设置where条件
			break;
		}
		// 排序
		String orderBy = sortOrder;// 不为空时用指定的排序方法进行排序
		if (TextUtils.isEmpty(sortOrder)) {
			orderBy = Config.DEFAULT_SORT_ORDER;// 传入的排序参数为空的时候采用默认的排序
		} 
		SQLiteDatabase db = dh.getWritableDatabase();
		// 采用传入的参数进行查询
		Cursor c = qb.query(db, projection, selection, selectionArgs, null,
				null, orderBy);
		// 发出通知
		c.setNotificationUri(getContext().getContentResolver(), uri);
		return c;
	}
}
