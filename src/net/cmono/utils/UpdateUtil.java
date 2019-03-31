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
	/* 下载中 */
	private static final int DOWNLOAD = 1;
	/* 下载结束 */
	private static final int DOWNLOAD_FINISH = 2;
	/* 保存解析的XML信息 */
	HashMap<String, String> mHashMap;
	/* 下载保存路径 */
	private String mSavePath;
	/* 记录进度条数量 */
	private int progress;
	/* 是否取消更新 */
	private boolean cancelUpdate = false;

	private Context mContext;
	/* 更新进度条 */
	private ProgressBar mProgress;
	private ProgressDialog mDownloadDialog;

	ProgressDialog checkProgressDialog;

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			// 正在下载
			case DOWNLOAD:
				// 设置进度条位置
				// mProgress.setProgress(progress);
				mDownloadDialog.setProgress(progress);
				break;
			case DOWNLOAD_FINISH:
				// 安装文件
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
	 * 检测软件更新
	 */
	public void checkUpdate() {
		checkProgressDialog = ProgressDialog.show(mContext, "检测更新",
				"Please wait...", true, false);

		new Thread(runnable).start();

		// if (isUpdate())
		// {
		// // 显示提示对话框
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
				// 显示提示对话框
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

			// 获取当前软件版本
			int versionCode = getVersionCode(mContext);
			// 把version.xml放到网络上，然后获取文件信息
			// InputStream inStream =
			// ParseXmlService.class.getClassLoader().getResourceAsStream("version.xml");
			// 解析XML文件。 由于XML文件比较小，因此使用DOM方式进行解析
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

					// 版本判断
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
				// new AlertDialog.Builder(mContext).setTitle("异常信息")
				// .setMessage(e.getMessage()).setPositiveButton("OK", null)
				// .show();
			}
		}
	};

	/**
	 * 检查软件是否有更新版本
	 * 
	 * @return
	 */
	private boolean isUpdate() {
		// 获取当前软件版本
		int versionCode = getVersionCode(mContext);
		// 把version.xml放到网络上，然后获取文件信息
		// InputStream inStream =
		// ParseXmlService.class.getClassLoader().getResourceAsStream("version.xml");
		// 解析XML文件。 由于XML文件比较小，因此使用DOM方式进行解析
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
				// 版本判断
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
	 * 获取软件版本号
	 * 
	 * @param context
	 * @return
	 */
	private int getVersionCode(Context context) {
		int versionCode = 0;
		try {
			// 获取软件版本号，对应AndroidManifest.xml下android:versionCode
			versionCode = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0).versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();

		}
		return versionCode;
	}

	/**
	 * 获取软件版本号
	 * 
	 * @param context
	 * @return
	 */
	private String getVersionName(Context context) {
		String versionName = "0";
		try {
			// 获取软件版本号，对应AndroidManifest.xml下android:versionCode
			versionName = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();

		}
		return versionName;
	}

	/**
	 * 显示软件更新对话框
	 */
	private void showNoticeDialog() {
		// // 构造对话框
		// AlertDialog.Builder builder = new Builder(mContext);
		// builder.setTitle(R.string.soft_update_title);
		// builder.setMessage(R.string.soft_update_info);
		// // 更新
		// builder.setPositiveButton(R.string.soft_update_updatebtn,
		// new OnClickListener() {
		// @Override
		// public void onClick(DialogInterface dialog, int which) {
		// dialog.dismiss();
		// // 显示下载对话框
		// showDownloadDialog();
		// }
		// });
		// // 稍后更新
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
		dialog.title("更细确认").btnNum(2).content("是否现在进行更新？")//
				.btnText("稍后更新", "马上更新")//
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
	 * 显示软件下载对话框
	 */
	private void showDownloadDialog() {
		// // 构造软件下载对话框
		// AlertDialog.Builder builder = new Builder(mContext);
		// builder.setTitle(R.string.soft_updating);
		// // 给下载对话框增加进度条
		// final LayoutInflater inflater = LayoutInflater.from(mContext);
		// View v = inflater.inflate(R.layout.softupdate_progress, null);
		// mProgress = (ProgressBar) v.findViewById(R.id.update_progress);
		// builder.setView(v);
		// // 取消更新
		// builder.setNegativeButton(R.string.soft_update_cancel, new
		// OnClickListener()
		// {
		// @Override
		// public void onClick(DialogInterface dialog, int which)
		// {
		// dialog.dismiss();
		// // 设置取消状态
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
		// 取消更新 R.string.soft_update_cancel
		progressDialog.setButton("取消更新", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				// 设置取消状态
				cancelUpdate = true;
			}
		});
		mDownloadDialog = progressDialog;// .show();//"Loading...",
											// "Please wait...", true, false);
		mDownloadDialog.show();
		// 下载文件
		downloadApk();

		// LayoutInflater inflater = LayoutInflater.from(mContext);
		// View v = inflater.inflate(R.layout.loading_dialog, null);// 得到加载view
		// LinearLayout layout = (LinearLayout)
		// v.findViewById(R.id.dialog_view);// 加载布局
		// // main.xml中的ImageView
		// ImageView spaceshipImage = (ImageView) v.findViewById(R.id.img);
		// TextView tipTextView = (TextView) v.findViewById(R.id.tipTextView);//
		// 提示文字
		// // 加载动画
		// Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(
		// mContext, R.anim.loading_animation);
		// // 使用ImageView显示动画
		// spaceshipImage.startAnimation(hyperspaceJumpAnimation);
		// tipTextView.setText("正在下载。。。。。。");// 设置加载信息
		//
		// loadingDialog = new Dialog(mContext, R.style.loading_dialog);//
		// 创建自定义样式dialog
		//
		// loadingDialog.setCancelable(false);// 不可以用“返回键”取消
		// loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
		// LinearLayout.LayoutParams.FILL_PARENT,
		// LinearLayout.LayoutParams.FILL_PARENT));// 设置布局
		// loadingDialog.show();
		//
		// downloadApk();
	}

	/**
	 * 下载apk文件
	 */
	private void downloadApk() {
		// 启动新线程下载软件
		new downloadApkThread().start();
	}

	/**
	 * 下载文件线程
	 */
	private class downloadApkThread extends Thread {
		@Override
		public void run() {

			try {
				// 判断SD卡是否存在，并且是否具有读写权限
				if (Environment.getExternalStorageState().equals(
						Environment.MEDIA_MOUNTED)) {
					// 获得存储卡的路径
					String sdpath = Environment.getExternalStorageDirectory()
							+ "/";
					mSavePath = sdpath + "download";
					URL url = new URL(mHashMap.get("url"));
					// 创建连接
					HttpURLConnection conn = (HttpURLConnection) url
							.openConnection();
					conn.connect();
					// 获取文件大小
					int length = conn.getContentLength();
					// 创建输入流
					InputStream is = conn.getInputStream();

					File file = new File(mSavePath);
					// 判断文件目录是否存在
					if (!file.exists()) {
						file.mkdir();
					}
					File apkFile = new File(mSavePath, mHashMap.get("name"));
					FileOutputStream fos = new FileOutputStream(apkFile);
					int count = 0;
					// 缓存
					byte buf[] = new byte[1024];
					// 写入到文件中
					do {
						int numread = is.read(buf);
						count += numread;
						// 计算进度条位置
						progress = (int) (((float) count / length) * 100);
						// 更新进度
						mHandler.sendEmptyMessage(DOWNLOAD);
						if (numread <= 0) {
							// 下载完成
							mHandler.sendEmptyMessage(DOWNLOAD_FINISH);
							break;
						}
						// 写入文件
						fos.write(buf, 0, numread);
					} while (!cancelUpdate);// 点击取消就停止下载.
					fos.close();
					is.close();
				}
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			// 取消下载对话框显示
			// mDownloadDialog.dismiss();
			mDownloadDialog.dismiss();

		}
	};

	/**
	 * 安装APK文件
	 */
	private void installApk() {
		File apkfile = new File(mSavePath, mHashMap.get("name"));
		if (!apkfile.exists()) {
			return;
		}
		// 通过Intent安装APK文件
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setDataAndType(Uri.parse("file://" + apkfile.toString()),
				"application/vnd.android.package-archive");
		mContext.startActivity(i);
	}
}
