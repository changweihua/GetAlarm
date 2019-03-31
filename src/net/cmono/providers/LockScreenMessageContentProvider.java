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

	// ����һ��UriMatcher���������ƥ��URL:�γ�ӳ�䣬�����Ƿ�Ϸ�
	public static final UriMatcher uriMatcher;
	// ��ʱ��ID
	public static final int INCOMING_MSG_COLLECTION = 1;
	// ����ʱ��ID
	public static final int INCOMING_MSG_SINGLE = 2;

	static {
		// UriMatcher.NO_MATCH��ʾ��ƥ���κ�·���ķ�����
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(Configs.LCMSGAUTHORTY, "msgs",
				INCOMING_MSG_COLLECTION);
		// �������#��ʾΪ����:users�����ĳһ��users
		uriMatcher.addURI(Configs.LCMSGAUTHORTY, "msgs/#", INCOMING_MSG_SINGLE);
	}

	// �õ�ContentProvider���������ͣ����صĲ���URL���������������
	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		switch (uriMatcher.match(uri)) {
		// matcher����URL��ǰ2���Э��+·����Ϊ��1�����ʱ��switch����ֵΪURL�ĵ�3��˴�ΪINCOMING_USER_COLLECTION
		case INCOMING_MSG_COLLECTION:
			return Config.CONTENT_TYPE;
		case INCOMING_MSG_SINGLE:
			return Config.CONTENT_TYPE_ITEM;
		default:
			throw new IllegalArgumentException("Unknown URI" + uri);
		}
	}

	// �ú����ķ���ֵ��һ��URL
	// ���URL��ʾ���Ǹո�ʹ����������������������
	@Override
	public Uri insert(Uri uri, ContentValues values) {
		// TODO Auto-generated method stub
		return null;
	}

	// �ص���������ContentProvider������ʱ�����
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
