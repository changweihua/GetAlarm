package net.cmono.utils;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.util.Date;

import com.tencent.bugly.crashreport.CrashReport;

import net.cmono.consts.ConstValue;

/**
 * 日志打印类，提供了一个开关来决定是否打印日志
 */
public class Logger {

	private static boolean IsRecord = true;// 是否记录日志
//	private static boolean IsRecord = BuildConfig.DEBUG;

	// 存储卡目录和手机内部目录
	private static String ExternalStorePath = ConstValue.ExternalStorePath;

	public static void d(String tag, String msg) {
		if (IsRecord) {
			try {
				SaveToSD(tag, msg);
			} catch (Exception ex) {
				ex.printStackTrace();
				CrashReport.postCatchedException(ex);
			}
		}
	}

	private static void SaveToSD(String tag, String msg) {
		try {
			String filePath = ConstValue.ROOTFOLDER
					+ ConstValue.PATHFORMATE.format(new Date())
					+ File.separator;
			File path = new File(filePath);
			if (!path.exists()) {
				path.mkdirs();
			}
			String FileName = ConstValue.PATHFORMATE1.format(new Date())
					+ ".txt";
			File mLogFile = new File(filePath + FileName);
			if (!mLogFile.exists()) {
				mLogFile.createNewFile();
			}

			StringBuffer sb = new StringBuffer();
			sb.append(ConstValue.DATEFORMAT.format(new Date()));
			sb.append(": ");
			sb.append("DEBUG");
			sb.append(": ");
			sb.append(tag);
			sb.append(": ");
			sb.append(msg);
			sb.append("\n");
			RandomAccessFile raf = null;
			try {
				raf = new RandomAccessFile(mLogFile, "rw");
				raf.seek(mLogFile.length());
				raf.write(sb.toString().getBytes("UTF-8"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				CrashReport.postCatchedException(e);
			} catch (IOException e) {
				e.printStackTrace();
				CrashReport.postCatchedException(e);
			} finally {
				if (raf != null) {
					try {
						raf.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						CrashReport.postCatchedException(e);
					}
				}

			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
			CrashReport.postCatchedException(ioe);
		}
	}

}