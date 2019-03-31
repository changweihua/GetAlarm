package net.cmono.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import net.cmono.getalarm.R;

import com.flyco.animation.BounceEnter.BounceTopEnter;
import com.flyco.animation.SlideExit.SlideBottomExit;
import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.widget.MaterialDialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.widget.ProgressBar;
import android.widget.Toast;

public class UpdateUtil {
	/* ������ */
	private static final int DOWNLOAD = 1;
	/* ���ؽ��� */
	private static final int DOWNLOAD_FINISH = 2;
	/* ���������XML��Ϣ */
	HashMap<String, String> mHashMap;
	/* ���ر���·�� */
	private String mSavePath;
	/* ��¼���������� */
	private int progress;
	/* �Ƿ�ȡ������ */
	private boolean cancelUpdate = false;

	private Context mContext;
	/* ���½����� */
	private ProgressBar mProgress;
	private ProgressDialog mDownloadDialog;

	ProgressDialog checkProgressDialog;

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			// ��������
			case DOWNLOAD:
				// ���ý�����λ��
				// mProgress.setProgress(progress);
				mDownloadDialog.setProgress(progress);
				break;
			case DOWNLOAD_FINISH:
				// ��װ�ļ�
				installApk();
				break;
			default:
				break;
			}
		};
	};

	public UpdateUtil(Context context) {
		this.mContext = context;
	}

	/**
	 * ����������
	 */
	public void checkUpdate() {
		checkProgressDialog = ProgressDialog.show(mContext, "������",
				"Please wait...", true, false);

		new Thread(runnable).start();

		// if (isUpdate())
		// {
		// // ��ʾ��ʾ�Ի���
		// showNoticeDialog();
		// } else
		// {
		// Toast.makeText(mContext, R.string.soft_update_no,
		// Toast.LENGTH_LONG).show();
		// }
	}

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			Bundle data = msg.getData();
			Boolean flag = data.getBoolean("flag");
			checkProgressDialog.dismiss();
			if (flag) {
				// ��ʾ��ʾ�Ի���
				showNoticeDialog();
			} else {
				Toast.makeText(mContext, R.string.soft_update_no,
						Toast.LENGTH_LONG).show();
			}
		}
	};

	Runnable runnable = new Runnable() {
		@Override
		public void run() {
			// TODO: http request.
			Message msg = new Message();
			Bundle data = new Bundle();
			Boolean flag = false;

			// ��ȡ��ǰ����汾
			int versionCode = getVersionCode(mContext);
			// ��version.xml�ŵ������ϣ�Ȼ���ȡ�ļ���Ϣ
			// InputStream inStream =
			// ParseXmlService.class.getClassLoader().getResourceAsStream("version.xml");
			// ����XML�ļ��� ����XML�ļ��Ƚ�С�����ʹ��DOM��ʽ���н���
			ParseXmlService service = new ParseXmlService();
			try {
				String path = "http://www.cmono.net/version.xml";
				URL url = new URL(path);
				HttpURLConnection conn = (HttpURLConnection) url
						.openConnection();
				conn.setReadTimeout(5 * 1000);
				conn.setRequestMethod("GET");
				InputStream inStream = conn.getInputStream();
				mHashMap = service.parseXml(inStream);
				if (null != mHashMap) {
					int serviceCode = Integer.valueOf(mHashMap.get("version"));

					// �汾�ж�
					if (serviceCode > versionCode) {
						flag = true;
					}
				}
				data.putBoolean("flag", flag);
				msg.setData(data);
				handler.sendMessage(msg);
			} catch (Exception e) {
				e.printStackTrace();
				Logger.d("UpdateHelper", e.getMessage());
				handler.sendEmptyMessage(0);
				// new AlertDialog.Builder(mContext).setTitle("�쳣��Ϣ")
				// .setMessage(e.getMessage()).setPositiveButton("OK", null)
				// .show();
			}
		}
	};

	/**
	 * �������Ƿ��и��°汾
	 * 
	 * @return
	 */
	private boolean isUpdate() {
		// ��ȡ��ǰ����汾
		int versionCode = getVersionCode(mContext);
		// ��version.xml�ŵ������ϣ�Ȼ���ȡ�ļ���Ϣ
		// InputStream inStream =
		// ParseXmlService.class.getClassLoader().getResourceAsStream("version.xml");
		// ����XML�ļ��� ����XML�ļ��Ƚ�С�����ʹ��DOM��ʽ���н���
		ParseXmlService service = new ParseXmlService();
		try {
			String path = "http://www.cmono.net/version.xml";
			URL url = new URL(path);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setReadTimeout(5 * 1000);
			conn.setRequestMethod("GET");
			InputStream inStream = conn.getInputStream();
			mHashMap = service.parseXml(inStream);
			if (null != mHashMap) {
				int serviceCode = Integer.valueOf(mHashMap.get("version"));
				// �汾�ж�
				if (serviceCode > versionCode) {
					return true;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	/**
	 * ��ȡ����汾��
	 * 
	 * @param context
	 * @return
	 */
	private int getVersionCode(Context context) {
		int versionCode = 0;
		try {
			// ��ȡ����汾�ţ���ӦAndroidManifest.xml��android:versionCode
			versionCode = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0).versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();

		}
		return versionCode;
	}

	/**
	 * ��ȡ����汾��
	 * 
	 * @param context
	 * @return
	 */
	private String getVersionName(Context context) {
		String versionName = "0";
		try {
			// ��ȡ����汾�ţ���ӦAndroidManifest.xml��android:versionCode
			versionName = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();

		}
		return versionName;
	}

	/**
	 * ��ʾ������¶Ի���
	 */
	private void showNoticeDialog() {
		// // ����Ի���
		// AlertDialog.Builder builder = new Builder(mContext);
		// builder.setTitle(R.string.soft_update_title);
		// builder.setMessage(R.string.soft_update_info);
		// // ����
		// builder.setPositiveButton(R.string.soft_update_updatebtn,
		// new OnClickListener() {
		// @Override
		// public void onClick(DialogInterface dialog, int which) {
		// dialog.dismiss();
		// // ��ʾ���ضԻ���
		// showDownloadDialog();
		// }
		// });
		// // �Ժ����
		// builder.setNegativeButton(R.string.soft_update_later,
		// new OnClickListener() {
		// @Override
		// public void onClick(DialogInterface dialog, int which) {
		// dialog.dismiss();
		// }
		// });
		// Dialog noticeDialog = builder.create();
		// noticeDialog.show();

		final MaterialDialog dialog = new MaterialDialog(mContext);
		dialog.title("��ϸȷ��").btnNum(2).content("�Ƿ����ڽ��и��£�")//
				.btnText("�Ժ����", "���ϸ���")//
				.showAnim(new BounceTopEnter())//
				.dismissAnim(new SlideBottomExit())//
				.show();

		dialog.setOnBtnClickL(new OnBtnClickL() {
			@Override
			public void onBtnClick() {
				dialog.dismiss();
			}
		}, new OnBtnClickL() {
			@Override
			public void onBtnClick() {
				showDownloadDialog();
				dialog.dismiss();
			}
		});

	}

	/**
	 * ��ʾ������ضԻ���
	 */
	private void showDownloadDialog() {
		// // ����������ضԻ���
		// AlertDialog.Builder builder = new Builder(mContext);
		// builder.setTitle(R.string.soft_updating);
		// // �����ضԻ������ӽ�����
		// final LayoutInflater inflater = LayoutInflater.from(mContext);
		// View v = inflater.inflate(R.layout.softupdate_progress, null);
		// mProgress = (ProgressBar) v.findViewById(R.id.update_progress);
		// builder.setView(v);
		// // ȡ������
		// builder.setNegativeButton(R.string.soft_update_cancel, new
		// OnClickListener()
		// {
		// @Override
		// public void onClick(DialogInterface dialog, int which)
		// {
		// dialog.dismiss();
		// // ����ȡ��״̬
		// cancelUpdate = true;
		// }
		// });
		// mDownloadDialog = builder.create();
		// mDownloadDialog.show();

		// ProgressDialog progressDialog = ProgressDialog.show(mContext,
		// "Loading...", "Please wait...", true, false);

		final ProgressDialog progressDialog = new ProgressDialog(mContext);
		progressDialog.setTitle(R.string.soft_updating);
		progressDialog.setMax(100);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		// ȡ������ R.string.soft_update_cancel
		progressDialog.setButton("ȡ������", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				// ����ȡ��״̬
				cancelUpdate = true;
			}
		});
		mDownloadDialog = progressDialog;// .show();//"Loading...",
											// "Please wait...", true, false);
		mDownloadDialog.show();
		// �����ļ�
		downloadApk();

		// LayoutInflater inflater = LayoutInflater.from(mContext);
		// View v = inflater.inflate(R.layout.loading_dialog, null);// �õ�����view
		// LinearLayout layout = (LinearLayout)
		// v.findViewById(R.id.dialog_view);// ���ز���
		// // main.xml�е�ImageView
		// ImageView spaceshipImage = (ImageView) v.findViewById(R.id.img);
		// TextView tipTextView = (TextView) v.findViewById(R.id.tipTextView);//
		// ��ʾ����
		// // ���ض���
		// Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(
		// mContext, R.anim.loading_animation);
		// // ʹ��ImageView��ʾ����
		// spaceshipImage.startAnimation(hyperspaceJumpAnimation);
		// tipTextView.setText("�������ء�����������");// ���ü�����Ϣ
		//
		// loadingDialog = new Dialog(mContext, R.style.loading_dialog);//
		// �����Զ�����ʽdialog
		//
		// loadingDialog.setCancelable(false);// �������á����ؼ���ȡ��
		// loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
		// LinearLayout.LayoutParams.FILL_PARENT,
		// LinearLayout.LayoutParams.FILL_PARENT));// ���ò���
		// loadingDialog.show();
		//
		// downloadApk();
	}

	/**
	 * ����apk�ļ�
	 */
	private void downloadApk() {
		// �������߳��������
		new downloadApkThread().start();
	}

	/**
	 * �����ļ��߳�
	 */
	private class downloadApkThread extends Thread {
		@Override
		public void run() {

			try {
				// �ж�SD���Ƿ���ڣ������Ƿ���ж�дȨ��
				if (Environment.getExternalStorageState().equals(
						Environment.MEDIA_MOUNTED)) {
					// ��ô洢����·��
					String sdpath = Environment.getExternalStorageDirectory()
							+ "/";
					mSavePath = sdpath + "download";
					URL url = new URL(mHashMap.get("url"));
					// ��������
					HttpURLConnection conn = (HttpURLConnection) url
							.openConnection();
					conn.connect();
					// ��ȡ�ļ���С
					int length = conn.getContentLength();
					// ����������
					InputStream is = conn.getInputStream();

					File file = new File(mSavePath);
					// �ж��ļ�Ŀ¼�Ƿ����
					if (!file.exists()) {
						file.mkdir();
					}
					File apkFile = new File(mSavePath, mHashMap.get("name"));
					FileOutputStream fos = new FileOutputStream(apkFile);
					int count = 0;
					// ����
					byte buf[] = new byte[1024];
					// д�뵽�ļ���
					do {
						int numread = is.read(buf);
						count += numread;
						// ���������λ��
						progress = (int) (((float) count / length) * 100);
						// ���½���
						mHandler.sendEmptyMessage(DOWNLOAD);
						if (numread <= 0) {
							// �������
							mHandler.sendEmptyMessage(DOWNLOAD_FINISH);
							break;
						}
						// д���ļ�
						fos.write(buf, 0, numread);
					} while (!cancelUpdate);// ���ȡ����ֹͣ����.
					fos.close();
					is.close();
				}
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			// ȡ�����ضԻ�����ʾ
			// mDownloadDialog.dismiss();
			mDownloadDialog.dismiss();

		}
	};

	/**
	 * ��װAPK�ļ�
	 */
	private void installApk() {
		File apkfile = new File(mSavePath, mHashMap.get("name"));
		if (!apkfile.exists()) {
			return;
		}
		// ͨ��Intent��װAPK�ļ�
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setDataAndType(Uri.parse("file://" + apkfile.toString()),
				"application/vnd.android.package-archive");
		mContext.startActivity(i);
	}
}
