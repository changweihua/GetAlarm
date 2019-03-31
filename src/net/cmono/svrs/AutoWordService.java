package net.cmono.svrs;

import java.io.IOException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.tencent.bugly.crashreport.CrashReport;

import net.cmono.consts.ConstValue;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;

public class AutoWordService extends Service {

	private final String Tag = "AutoWordService";

	public class LocalBinder extends Binder {
		String stringToSend = "I'm the test String";

		AutoWordService getService() {
			return AutoWordService.this;
		}
	}

	private final IBinder mBinder = new LocalBinder();

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		// IBinder myIBinder = null;
		// if ( null == myIBinder )
		// myIBinder = new LocalBinder() ;
		// return myIBinder;
		return mBinder; // 也可以像上面几个语句那样重新new一个IBinder
		// 如果这边不返回一个IBinder的接口实例，那么ServiceConnection中的onServiceConnected就不会被调用
		// 那么bind所具有的传递数据的功能也就体现不出来~\(≧▽≦)/~啦啦啦（这个返回值是被作为onServiceConnected中的第二个参数的）
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();

	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	class GetWordTask extends AsyncTask<String, Integer, String[]> {
		private Context context;
		String date;

		GetWordTask(Context context) {
			this.context = context;
		}

		/**
		 * 运行在UI线程中，在调用doInBackground()之前执行
		 */
		@Override
		protected void onPreExecute() {
			// Toast.makeText(context, "开始执行获取", Toast.LENGTH_SHORT).show();
		}

		/**
		 * 后台运行的方法，可以运行非UI线程，可以执行耗时的方法
		 */
		@Override
		protected String[] doInBackground(String... params) {
			try {
				date = params[0];

				String url = "http://open.iciba.com/dsapi/?date=";

				HttpGet httpGet = new HttpGet(url + date);
				HttpResponse httpResponse = null;
				try {
					httpResponse = new DefaultHttpClient().execute(httpGet);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					CrashReport.postCatchedException(e);
				}
				if (httpResponse.getStatusLine().getStatusCode() == 200) {
					String enResult = "";
					String cnResult = "";
					try {
						// 创建一个JSON对象
						JSONObject jsonObject = new JSONObject(
								EntityUtils.toString(httpResponse.getEntity()));
						enResult = URLDecoder.decode(
								jsonObject.getString("content"), "UTF-8");
						// 中文
						cnResult = URLDecoder.decode(
								jsonObject.getString("note"), "UTF-8");

						return new String[] { enResult, cnResult };
					} catch (ParseException | IOException | JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						CrashReport.postCatchedException(e);
					}
				}
			} catch (Throwable thr) {
				CrashReport.postCatchedException(thr); // bugly会将这个throwable上报
			}
			return null;
		}

		/**
		 * 运行在ui线程中，在doInBackground()执行完毕后执行
		 */
		@Override
		protected void onPostExecute(String[] result) {
			SharedPreferences sp = context.getSharedPreferences(
					ConstValue.SETTING_PF, Context.MODE_PRIVATE);
			Editor editor = sp.edit();

			editor.putString("wordDate", date);

			int mode = sp.getBoolean("wordMode", true) ? 0 : 1;
			editor.putString("word", result[0]);
			if (mode == 1) {
				editor.putString("word", result[1]);
			}

			editor.putString("cnWord", result[1]);

			editor.commit();
		}

		/**
		 * 在publishProgress()被调用以后执行，publishProgress()用于更新进度
		 */
		@Override
		protected void onProgressUpdate(Integer... values) {
		}
	}

	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);

		// 查询网络
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String date = format.format(new Date());

		GetWordTask task = new GetWordTask(this);
		task.execute(date);

	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public boolean onUnbind(Intent intent) {
		// TODO Auto-generated method stub
		return super.onUnbind(intent);
	}
}
