package net.cmono.receivers;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.tencent.bugly.crashreport.CrashReport;

import net.cmono.consts.ConstValue;
import net.cmono.dtos.Card;
import net.cmono.getalarm.GetAlarmApplication;
import net.cmono.getalarm.MainActivity;
import net.cmono.utils.FileUtil;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;

public class TodayHistoryAlarmReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		TodayHistoryTask task = new TodayHistoryTask(context);
		task.execute("");

	}

	class TodayHistoryTask extends AsyncTask<String, Integer, List<Card>> {
		private Context context;
		String date;

		TodayHistoryTask(Context context) {
			this.context = context;
		}

		/**
		 * ������UI�߳��У��ڵ���doInBackground()֮ǰִ��
		 */
		@Override
		protected void onPreExecute() {
			// Toast.makeText(context, "��ʼִ�л�ȡ", Toast.LENGTH_SHORT).show();
		}

		/**
		 * ��̨���еķ������������з�UI�̣߳�����ִ�к�ʱ�ķ���
		 */
		@Override
		protected List<Card> doInBackground(String... params) {

			date = params[0];

			String ExternalStorePath = ConstValue.ExternalStorePath;
			String FileName = ConstValue.PATHFORMATE.format(new Date())
					+ ".json";
			File file = new File(ExternalStorePath + FileName);
			if (file.exists()) {
				file.delete();
			}
			String url = "http://www.ipip5.com/today/api.php?type=json";
			HttpGet httpGet = new HttpGet(url);
			HttpResponse httpResponse = null;
			try {
				httpResponse = new DefaultHttpClient().execute(httpGet);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				CrashReport.postCatchedException(e);
			}
			if (httpResponse.getStatusLine().getStatusCode() == 200) {

				try {
					String source = EntityUtils.toString(httpResponse
							.getEntity());

					// ������浽�ļ���
					FileUtil.saveFile(source, ExternalStorePath + FileName);

				} catch (ParseException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					CrashReport.postCatchedException(e);
				}
			}

			return null;

		}

		/**
		 * ������ui�߳��У���doInBackground()ִ����Ϻ�ִ��
		 */
		@Override
		protected void onPostExecute(List<Card> cards) {

			SharedPreferences sp = GetAlarmApplication.getAppContext()
					.getSharedPreferences(ConstValue.SETTING_PF,
							Context.MODE_PRIVATE);
			Editor editor = sp.edit();

			editor.putString("todayHistoryDate", date);

			editor.commit();
			Intent i = new Intent(context, MainActivity.class);
			i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(i);
		}

		/**
		 * ��publishProgress()�������Ժ�ִ�У�publishProgress()���ڸ��½���
		 */
		@Override
		protected void onProgressUpdate(Integer... values) {
		}
	}

}
