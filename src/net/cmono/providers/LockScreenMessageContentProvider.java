package net.cmono.providers;


import com.tencent.bugly.crashreport.CrashReport;

import net.cmono.dtos.NotiMessage;
import net.cmono.getalarm.GetAlarmApplication;
import net.cmono.providers.Configs.Config;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;

public class LockScreenMessageContentProvider extends ContentProvider {

	// 定义一个UriMatcher类对象，用来匹配URL:形成映射，规则是否合法
	public static final UriMatcher uriMatcher;
	// 组时的ID
	public static final int INCOMING_MSG_COLLECTION = 1;
	// 单个时的ID
	public static final int INCOMING_MSG_SINGLE = 2;

	static {
		// UriMatcher.NO_MATCH表示不匹配任何路径的返回码
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(Configs.LCMSGAUTHORTY, "msgs",
				INCOMING_MSG_COLLECTION);
		// 后面加了#表示为单个:users下面的某一个users
		uriMatcher.addURI(Configs.LCMSGAUTHORTY, "msgs/#", INCOMING_MSG_SINGLE);
	}

	// 得到ContentProvider的数据类型，返回的参数URL所代表的数据类型
	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		switch (uriMatcher.match(uri)) {
		// matcher满足URL的前2项（即协议+路径）为第1种情况时，switch语句的值为URL的第3项，此处为INCOMING_USER_COLLECTION
		case INCOMING_MSG_COLLECTION:
			return Config.CONTENT_TYPE;
		case INCOMING_MSG_SINGLE:
			return Config.CONTENT_TYPE_ITEM;
		default:
			throw new IllegalArgumentException("Unknown URI" + uri);
		}
	}

	// 该函数的返回值是一个URL
	// 这个URL表示的是刚刚使用这个函数的所插入的数据
	@Override
	public Uri insert(Uri uri, ContentValues values) {
		// TODO Auto-generated method stub
		return null;
	}

	// 回调函数，在ContentProvider创建的时候调用
	@Override
	public boolean onCreate() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		// TODO Auto-generated method stub

		String[] columns = new String[] { "icon", "title", "text", "subtext",
				"hasMsg" };
		MatrixCursor stringCursor = new MatrixCursor(columns);
		try {

			for (int i = 0; i < GetAlarmApplication.NotiMessageList.size(); i++) {
				NotiMessage noti = GetAlarmApplication.NotiMessageList.get(i);
				String row[] = new String[columns.length];
				row[0] = noti.getNotificationInfo().getIcon();
				row[1] = noti.getNotificationInfo().getTitle();
				row[2] = noti.getNotificationInfo().getText();
				row[3] = noti.getNotificationInfo().getSubtext();
				row[4] = "1";
				stringCursor.addRow(row);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			CrashReport.postCatchedException(e);
		}
		return stringCursor;

	}

}
