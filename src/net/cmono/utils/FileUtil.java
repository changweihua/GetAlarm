package net.cmono.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.tencent.bugly.crashreport.CrashReport;

public class FileUtil {

	public static void saveFile(String source, String filePath) {
		// boolean hasSDCard =
		// Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
		// if (hasSDCard) {
		// filePath = Environment.getExternalStorageDirectory().toString() +
		// File.separator + "hello.txt";
		// } else
		// filePath = Environment.getDownloadCacheDirectory().toString() +
		// File.separator + "hello.txt";

		try {
			File file = new File(filePath);
			if (!file.exists()) {
				File dir = new File(file.getParent());
				dir.mkdirs();
				file.createNewFile();
			}
			FileOutputStream outStream = new FileOutputStream(file);
			outStream.write(source.getBytes());
			outStream.close();
		} catch (Exception e) {
			e.printStackTrace();
			CrashReport.postCatchedException(e);
		}
	}

	/**
	 * ���ı��ļ��е����ݶ��뵽buffer��
	 * 
	 * @param buffer
	 *            buffer
	 * @param filePath
	 *            �ļ�·��
	 * @throws IOException
	 *             �쳣
	 * @author cn.outofmemory
	 * @date 2013-1-7
	 */
	public static void readToBuffer(StringBuffer buffer, String filePath)
			throws IOException {
		InputStream is = new FileInputStream(filePath);
		String line; // ��������ÿ�ж�ȡ������
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		line = reader.readLine(); // ��ȡ��һ��
		while (line != null) { // ��� line Ϊ��˵��������
			buffer.append(line); // ��������������ӵ� buffer ��
			buffer.append("\n"); // ��ӻ��з�
			line = reader.readLine(); // ��ȡ��һ��
		}
		reader.close();
		is.close();
	}

	/**
	 * ��ȡ�ı��ļ�����
	 * 
	 * @param filePath
	 *            �ļ�����·��
	 * @return �ı�����
	 * @throws IOException
	 *             �쳣
	 * @author cn.outofmemory
	 * @date 2013-1-7
	 */
	public static String readFile(String filePath) throws IOException {
		StringBuffer sb = new StringBuffer();
		readToBuffer(sb, filePath);
		return sb.toString();
	}

	/**
	 * ���Ƶ����ļ�
	 * 
	 * @param oldPath
	 *            String ԭ�ļ�·�� �磺c:/fqf.txt
	 * @param newPath
	 *            String ���ƺ�·�� �磺f:/fqf.txt
	 * @return boolean
	 * @throws Exception
	 */
	public static void copyFile(String oldPath, String newPath)
			throws Exception {
		try {
			int bytesum = 0;
			int byteread = 0;
			File oldfile = new File(oldPath);
			if (oldfile.exists()) { // �ļ�����ʱ
				InputStream inStream = new FileInputStream(oldPath); // ����ԭ�ļ�
				FileOutputStream fs = new FileOutputStream(newPath);
				byte[] buffer = new byte[1444];
				int length;
				while ((byteread = inStream.read(buffer)) != -1) {
					bytesum += byteread; // �ֽ��� �ļ���С
					System.out.println(bytesum);
					fs.write(buffer, 0, byteread);
				}
				inStream.close();
				fs.close();
			}
		} catch (Exception e) {
			CrashReport.postCatchedException(e);
		}

	}

	/**
	 * ���������ļ�������
	 * 
	 * @param oldPath
	 *            String ԭ�ļ�·�� �磺c:/fqf
	 * @param newPath
	 *            String ���ƺ�·�� �磺f:/fqf/ff
	 * @return boolean
	 * @throws Exception
	 */
	public void copyFolder(String oldPath, String newPath) throws Exception {

		try {
			(new File(newPath)).mkdirs(); // ����ļ��в����� �������ļ���
			File a = new File(oldPath);
			String[] file = a.list();
			File temp = null;
			for (int i = 0; i < file.length; i++) {
				if (oldPath.endsWith(File.separator)) {
					temp = new File(oldPath + file[i]);
				} else {
					temp = new File(oldPath + File.separator + file[i]);
				}

				if (temp.isFile()) {
					FileInputStream input = new FileInputStream(temp);
					FileOutputStream output = new FileOutputStream(newPath
							+ "/" + (temp.getName()).toString());
					byte[] b = new byte[1024 * 5];
					int len;
					while ((len = input.read(b)) != -1) {
						output.write(b, 0, len);
					}
					output.flush();
					output.close();
					input.close();
				}
				if (temp.isDirectory()) {// ��������ļ���
					copyFolder(oldPath + "/" + file[i], newPath + "/" + file[i]);
				}
			}
		} catch (Exception e) {
			throw e;
		}

	}
}
