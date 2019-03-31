package net.cmono.utils;

import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.apache.http.util.EncodingUtils;

import android.os.Environment;

public class MyLogger {
	public static String getExternalSdCardPath() {

		// if (SDCardUtils.isSDCardEnable()) {
		if (true) {
			File sdCardFile = new File(Environment
					.getExternalStorageDirectory().getAbsolutePath());
			return sdCardFile.getAbsolutePath();
		}

		String path = null;

		File sdCardFile = null;

		ArrayList<String> devMountList = getDevMountList();

		for (String devMount : devMountList) {
			File file = new File(devMount);

			if (file.isDirectory() && file.canWrite()) {
				path = file.getAbsolutePath();

				String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmmss")
						.format(new Date());
				File testWritable = new File(path, "test_" + timeStamp);

				if (testWritable.mkdirs()) {
					testWritable.delete();
				} else {
					path = null;
				}
			}
		}

		if (path != null) {
			sdCardFile = new File(path);
			return sdCardFile.getAbsolutePath();
		}

		return null;
	}

	// ¶ÁÎÄ¼þ
	public static String readFile(String fileName) {

		try {
			File file = new File(fileName);

			FileInputStream fis = new FileInputStream(file);

			int length = fis.available();

			byte[] buffer = new byte[length];
			fis.read(buffer);

			String res = EncodingUtils.getString(buffer, "UTF-8");

			fis.close();
			return res;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}

	}

	private static ArrayList<String> getDevMountList() {
		String[] toSearch = readFile("/etc/vold.fstab").split(" ");
		ArrayList<String> out = new ArrayList<String>();
		for (int i = 0; i < toSearch.length; i++) {
			if (toSearch[i].contains("dev_mount")) {
				if (new File(toSearch[i + 2]).exists()) {
					out.add(toSearch[i + 2]);
				}
			}
		}
		return out;
	}

}
