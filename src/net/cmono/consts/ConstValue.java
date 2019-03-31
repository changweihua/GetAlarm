package net.cmono.consts;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.os.Environment;

public final class ConstValue {

	public final static String ROOTFOLDER = Environment
			.getExternalStorageDirectory().getAbsolutePath()
			+ File.separator
			+ "GetAlarm" + File.separator;

	public final static SimpleDateFormat DATEFORMAT = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
	public static final SimpleDateFormat PATHFORMATE = new SimpleDateFormat(
			"yyyyMMdd");
	public static final SimpleDateFormat PATHFORMATE1 = new SimpleDateFormat(
			"HH");
	public static final SimpleDateFormat SpecialDayDateFormat = new SimpleDateFormat(
			"MMdd");

	public static final String ExternalStorePath = ROOTFOLDER
			+ PATHFORMATE.format(new Date()) + File.separator;
	
	public static final String TDaysPath = Environment
			.getExternalStorageDirectory().getAbsolutePath()
			+ File.separator
			+ "TDays" + File.separator;
	
	public static final String TArtistPath = Environment
			.getExternalStorageDirectory().getAbsolutePath()
			+ File.separator
			+ "artist" + File.separator;
	
	public static final String TCoverPath = Environment
			.getExternalStorageDirectory().getAbsolutePath()
			+ File.separator
			+ "ttpod" + File.separator + "artist"+ File.separator;
	
	public static String TTPod_Default_Cover = ROOTFOLDER + "dcover.png"; 
	
	public static String XiaMi_Default_Cover = ROOTFOLDER + "dcover.png";
	
	public static String APP_INFOS_JSON = ROOTFOLDER + "appinfos.json"; 


	//锁屏设置
	public static final String SETTING_PF = "SETTING_PF";
	//初始化
	public static final String INIT_PF = "INIT_PF";
	//登录
	public static final String LOGON_PF = "LOGON_PF";
	//锁屏消息
	public static final String LCMSG_PF = "LCMSG_PF";
	//来电设置
	public static final String INCALL_PF = "INCALL_PF";
	//APP 信息
	public static final String APP_PF = "APP_PF";

	public static final String YUNOS_TOKEN_URL = "https://oauth.taobao.com/token";
	public static final String YUNOS_GRANT_TYPE = "authorization_code";
	public static final String YUNOS_APPKEY = "23253254";
	public static final String YUNOS_SECRET = "05a97589cebe6dc1b327ee3aece68a9b";
	public static final String YUNOS_REDIRECT = "http://www.cmono.net/getalarm.php";

	public static final String EXIT_ACTION = "net.cmono.getalarm.exit";
	
	public static final String HEADD_NOTI = "net.cmono.getalarm.headnoti";
	
	

}
