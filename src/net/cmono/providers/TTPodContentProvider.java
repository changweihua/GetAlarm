package net.cmono.providers;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.cmono.consts.ConstValue;
import net.cmono.dtos.ArtistFilenameFilter;
import net.cmono.getalarm.GetAlarmApplication;
import net.cmono.providers.Configs.Config;
import net.cmono.utils.Logger;
import net.cmono.utils.PathUtil;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;

import com.tencent.bugly.crashreport.CrashReport;

public class TTPodContentProvider extends ContentProvider {

	// ����һ��UriMatcher���������ƥ��URL:�γ�ӳ�䣬�����Ƿ�Ϸ�
	public static final UriMatcher uriMatcher;
	// ��ʱ��ID
	public static final int INCOMING_DAY_COLLECTION = 1;
	// ����ʱ��ID
	public static final int INCOMING_DAY_SINGLE = 2;

	static {
		// UriMatcher.NO_MATCH��ʾ��ƥ���κ�·���ķ�����
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(Configs.TTPODAUTHORTY, "covers",
				INCOMING_DAY_COLLECTION);
		// �������#��ʾΪ����:users�����ĳһ��users
		uriMatcher.addURI(Configs.TTPODAUTHORTY, "covers/#",
				INCOMING_DAY_SINGLE);
	}

	// �õ�ContentProvider���������ͣ����صĲ���URL���������������
	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		System.out.println("getType");
		switch (uriMatcher.match(uri)) {
		// matcher����URL��ǰ2���Э��+·����Ϊ��1�����ʱ��switch����ֵΪURL�ĵ�3��˴�ΪINCOMING_USER_COLLECTION
		case INCOMING_DAY_COLLECTION:
			return Config.CONTENT_TYPE;
		case INCOMING_DAY_SINGLE:
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
		Uri uri2 = Uri.parse("content://" + Configs.TTPODAUTHORTY + "/covers");
		this.getContext().getContentResolver().notifyChange(uri2, null);
		return 0;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		// TODO Auto-generated method stub

		String[] columns = new String[] { "talbumcover", "ttalbumcover" };
		MatrixCursor stringCursor = new MatrixCursor(columns);
		try {

			// String folder = ConstValue.TArtistPath +
			// GetAlarmApplication.TTPod_Artist + File.separator;
			String folder = ConstValue.TCoverPath
					+ GetAlarmApplication.TTPod_Artist;
			Logger.d("folder", folder);
			String cover = "";
			File root = new File(folder);
			// Logger.d("W", Boolean.toString(root.exists()));
			if (root.exists()) {
				File[] files = root.listFiles(new ArtistFilenameFilter());
				if (files != null && files.length > 0) {
					Random random=new Random();
					int index = Math.abs(random.nextInt(files.length));
					cover = folder + File.separator + files[index].getName();
					Logger.d("Cover", cover);
				}
			}
			if (cover.length() == 0) {
				cover = ConstValue.TTPod_Default_Cover;
			}
			Logger.d("Cover", cover);
			Object row[] = new Object[columns.length];
			row[0] = PathUtil.toUri(cover);
			row[1] = image2byte(cover);
			stringCursor.addRow(row);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			CrashReport.postCatchedException(e);
		}
		return stringCursor;

	}

	// ͼƬ��byte����
	public byte[] image2byte(String path) {
		byte[] data = null;
		FileInputStream input = null;
		try {
			input = new FileInputStream(new File(path));
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			byte[] buf = new byte[1024];
			int numBytesRead = 0;
			while ((numBytesRead = input.read(buf)) != -1) {
				output.write(buf, 0, numBytesRead);
			}
			data = output.toByteArray();
			output.close();
			input.close();
		} catch (FileNotFoundException ex1) {
			ex1.printStackTrace();
		} catch (IOException ex1) {
			ex1.printStackTrace();
		}
		return data;
	}

}
