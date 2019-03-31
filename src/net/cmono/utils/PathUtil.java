package net.cmono.utils;

import java.io.File;

import com.tencent.bugly.crashreport.CrashReport;

import net.cmono.getalarm.GetAlarmApplication;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;

public class PathUtil {
	public static String toUri(String picPath) {
		File imageFile = new File(picPath);
		String filePath = imageFile.getAbsolutePath();
		Cursor cursor = GetAlarmApplication
				.getAppContext()
				.getContentResolver()
				.query(Images.Media.EXTERNAL_CONTENT_URI,
						new String[] { MediaStore.Images.ImageColumns._ID },
						MediaStore.Images.ImageColumns.DATA + "=? ",
						new String[] { filePath }, null);

		if (cursor != null && cursor.moveToFirst()) {
			int id = cursor.getInt(cursor
					.getColumnIndex(MediaStore.Images.ImageColumns._ID));
			Uri baseUri = Uri.parse("content://media/external/images/media");
			return Uri.withAppendedPath(baseUri, "" + id).toString();

		}
		return "content://media/external/images/media/1";
		// return Uri.fromFile(new File(picPath)).toString();

		// Uri mUri = Uri.parse("content://media/external/images/media");
		// Uri mImageUri = null;
		//
		// try {
		// Cursor cursor = GetAlarmApplication
		// .getAppContext()
		// .getContentResolver()
		// .query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[] {
		// Images.ImageColumns._ID },
		// null, null,
		// MediaStore.Images.Media.DEFAULT_SORT_ORDER);
		// cursor.moveToFirst();
		// // for (cur.moveToFirst(); !cur.isAfterLast(); cur
		// // .moveToNext()) {
		// // index = cur.getColumnIndex(Images.ImageColumns._ID);
		// // // set _id value
		// // index = cur.getInt(index);
		// // }
		// while (!cursor.isAfterLast()) {
		// String data = cursor.getString(cursor
		// .getColumnIndex(MediaStore.MediaColumns.DATA));
		// if (picPath.equals(data)) {
		// int ringtoneID = cursor.getInt(cursor
		// .getColumnIndex(MediaStore.MediaColumns._ID));
		// mImageUri = Uri.withAppendedPath(mUri, "" + ringtoneID);
		// Logger.d("PathHelper", mImageUri.toString());
		// break;
		// }
		// cursor.moveToNext();
		// }
		//
		// } catch (Exception e) {
		// // TODO: handle exception
		// CrashReport.postCatchedException(e);
		// } finally {
		//
		// }
		//
		// return mImageUri == null ? "" : mImageUri.toString();
	}
}
