package net.cmono.providers;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.litepal.crud.DataSupport;

import com.tencent.bugly.crashreport.CrashReport;

import net.cmono.consts.ConstValue;
import net.cmono.dtos.SpecialDay;
import net.cmono.providers.Configs.Config;
import net.cmono.utils.PathUtil;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;

public class SpecialDayContentProvider extends ContentProvider {

	// ����һ��UriMatcher���������ƥ��URL:�γ�ӳ�䣬�����Ƿ�Ϸ�
	public static final UriMatcher uriMatcher;
	// ��ʱ��ID
	public static final int INCOMING_DAY_COLLECTION = 1;
	// ����ʱ��ID
	public static final int INCOMING_DAY_SINGLE = 2;

	static {
		// UriMatcher.NO_MATCH��ʾ��ƥ���κ�·���ķ�����
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(Configs.SPAUTHORTY, "days", INCOMING_DAY_COLLECTION);
		// �������#��ʾΪ����:users�����ĳһ��users
		uriMatcher.addURI(Configs.SPAUTHORTY, "days/#", INCOMING_DAY_SINGLE);
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

		tdaysMap.put("0207", "j_chuxi.jpg");
		tdaysMap.put("0208", "j_springfestival.jpg");
		tdaysMap.put("0214", "j_valentineday.jpg");
		tdaysMap.put("0222", "j_lanternfestival.jpg");
		tdaysMap.put("0308", "j_womenday.jpg");
		tdaysMap.put("0312", "j_arborday.jpg");
		tdaysMap.put("0401", "j_foolsday.jpg");
		tdaysMap.put("0404", "j_tombsweepingday.jpg");
		tdaysMap.put("0501", "j_laborday.jpg");
		tdaysMap.put("0504", "j_youthday.jpg");
		tdaysMap.put("0508", "j_motherday.jpg");
		tdaysMap.put("0601", "j_childrenday.jpg");
		tdaysMap.put("0609", "j_dragonboatfestival.jpg");
		tdaysMap.put("0619", "j_fatherday.jpg");
		tdaysMap.put("0809", "j_doubleseventhday.jpg");
		tdaysMap.put("0910", "j_teacherday.jpg");
		tdaysMap.put("0915", "j_midautumnfestival.jpg");
		tdaysMap.put("1001", "j_nationalday.jpg");
		tdaysMap.put("1009", "j_doubleninthfestival.jpg");
		tdaysMap.put("1031", "j_halloween.jpg");
		tdaysMap.put("1124", "j_thanksgivingday.jpg");
		tdaysMap.put("1224", "j_christmaseve.jpg");
		tdaysMap.put("1225", "j_christmasday.jpg");
		tdaysMap.put("0204", "j_lichun.jpg");

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

	Map<String, String> tdaysMap = new HashMap<String, String>();

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		// TODO Auto-generated method stub

		String today = ConstValue.SpecialDayDateFormat.format(new Date());

		String[] columns = new String[] { "name", "description", "isSpecial",
				"showSpecialDay", "isTraditional", "traditionaldayCover" };
		MatrixCursor stringCursor = new MatrixCursor(columns);
		try {
			SharedPreferences sp = getContext().getSharedPreferences(
					ConstValue.SETTING_PF, Context.MODE_PRIVATE);
			String row[] = new String[columns.length];
			row[2] = null;
			row[3] = sp.getBoolean("showSpecialDay", true) ? "1" : null;

			// �ж��Ƿ�Ϊ��ɫ����
			row[4] = null;
			row[5] = "";// PathHelper.toUri(ConstValue.TDaysPath+"j_dongzhi.jpg");

			// Calendar calendar = Calendar.getInstance();
			// String ttoday =
			// ConstValue.SpecialDayDateFormat.format(calendar.getTime());
			if (tdaysMap.keySet().contains(today)) {
				row[4] = "1";
				row[5] = PathUtil.toUri(ConstValue.TDaysPath
						+ tdaysMap.get(today));
			}

			// ����
			List<SpecialDay> days = DataSupport.where(
					"day = ? order by id desc", today).find(SpecialDay.class);// DataSupport.find(SpecialDay.class,
																				// 3);

			if (days != null && days.size() > 0) {
				row[0] = days.get(0).getName();
				row[1] = days.get(0).getDescription();
				row[2] = "1";
			}
			stringCursor.addRow(row);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			CrashReport.postCatchedException(e);
		}
		return stringCursor;

	}

}
