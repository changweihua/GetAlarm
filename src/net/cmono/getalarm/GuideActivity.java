package net.cmono.getalarm;

import java.util.ArrayList;
import java.util.List;

import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import com.tencent.bugly.crashreport.CrashReport;

import net.cmono.consts.ConstValue;
import net.cmono.dtos.SpecialDay;
import net.cmono.utils.Logger;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;

public class GuideActivity extends Activity implements OnClickListener {

	private ViewPager mViewPager;
	private PagerAdapter mAdapter;
	private List<View> mViews = new ArrayList<View>();
	// TAB

	private LinearLayout mTabWeixin;
	private LinearLayout mTabFrd;
	private LinearLayout mTabAddress;
	private LinearLayout mTabSetting;

	private Button mEnterButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_guid);

		initView();
		initEvents();

	}

	private void initEvents() {

		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {

			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});
	}

	private SQLiteDatabase db;
	private List<SpecialDay> list;

	/**
	 * 表中数据赋值
	 */
	private void initDate() {

		list.add(new SpecialDay(1, "0101", "新年元旦", "   "));
		list.add(new SpecialDay(2, "0106", "中国十三亿人口日", "   "));
		list.add(new SpecialDay(3, "0110", "中国110宣传日", "   "));

		list.add(new SpecialDay(11, "0117", "腊八节", "   "));

		// -------------------------------

		list.add(new SpecialDay(4, "0202", "世界湿地日", "   "));
		list.add(new SpecialDay(5, "0204", "世界抗癌症日", "   "));
		list.add(new SpecialDay(6, "0210", "世界气象日", "   "));
		list.add(new SpecialDay(7, "0214", "情人节", "   "));
		list.add(new SpecialDay(8, "0221", "国际母语节", "   "));

		list.add(new SpecialDay(12, "0201", "祭灶节", "   "));
		list.add(new SpecialDay(13, "0207", "除夕", "   "));
		list.add(new SpecialDay(14, "0208", "春节", "   "));
		list.add(new SpecialDay(15, "0212", "破五", "   "));
		list.add(new SpecialDay(16, "0222", "元宵节", "   "));

		// -------------------------------

		list.add(new SpecialDay(9, "0308", "妇女节", "   "));
		list.add(new SpecialDay(10, "0312", "植树节", "   "));
		list.add(new SpecialDay(17, "0315", "国际消费日", "   "));
		list.add(new SpecialDay(18, "0303", "全国爱耳日", "   "));
		list.add(new SpecialDay(19, "0321", "世界森林日", "   "));
		list.add(new SpecialDay(20, "0322", "世界水日", "   "));
		list.add(new SpecialDay(21, "0323", "世界气象日", "   "));
		list.add(new SpecialDay(22, "0324", "世界防治结核病日", "   "));

		list.add(new SpecialDay(23, "0310", "二月二龙抬头", "   "));

		// -------------------------------

		list.add(new SpecialDay(24, "0401", "愚人节", "   "));
		list.add(new SpecialDay(25, "0405", "清明节", "   "));
		list.add(new SpecialDay(26, "0407", "世界卫生日", "   "));
		list.add(new SpecialDay(27, "0422", "世界地球日", "   "));

		// -------------------------------

		list.add(new SpecialDay(28, "0501", "国际劳动节", "   "));
		list.add(new SpecialDay(29, "0504", "中国青年节", "   "));
		list.add(new SpecialDay(30, "0505", "全国碘缺乏病", "   "));
		list.add(new SpecialDay(31, "0508", "世界红十字日", "   "));
		list.add(new SpecialDay(32, "0512", "国际护士节", "   "));
		list.add(new SpecialDay(33, "0515", "国际家庭日", "   "));
		list.add(new SpecialDay(34, "0517", "世界电信日", "   "));
		list.add(new SpecialDay(35, "0518", "国际博物馆日", "   "));
		list.add(new SpecialDay(36, "0531", "世界无烟日", "   "));

		list.add(new SpecialDay(39, "0508", "母亲节", "   "));

		// -------------------------------

		list.add(new SpecialDay(37, "0601", "国际儿童节", "   "));
		list.add(new SpecialDay(38, "0605", "世界环境日", "   "));
		list.add(new SpecialDay(40, "0606", "全国爱眼日", "   "));
		list.add(new SpecialDay(41, "0623", "国际奥林匹克日", "   "));
		list.add(new SpecialDay(42, "0625", "全国土地日", "   "));

		list.add(new SpecialDay(43, "0609", "端午节", "   "));
		list.add(new SpecialDay(44, "0619", "父亲节", "   "));

		// -------------------------------

		list.add(new SpecialDay(45, "0701", "香港回归日", "   "));
		list.add(new SpecialDay(46, "0711", "世界人口日", "   "));

		// -------------------------------

		list.add(new SpecialDay(47, "0801", "八一建军节", "   "));

		list.add(new SpecialDay(48, "0805", "里约奥运会开幕", "   "));
		list.add(new SpecialDay(49, "0821", "里约奥运会闭幕", "   "));
		list.add(new SpecialDay(50, "0809", "七夕情人节", "   "));
		list.add(new SpecialDay(51, "0817", "鬼节", "   "));

		// -------------------------------

		list.add(new SpecialDay(52, "0908", "国际扫盲日", "   "));
		list.add(new SpecialDay(53, "0910", "教师节", "   "));
		list.add(new SpecialDay(54, "0917", "国际和平日", "   "));
		list.add(new SpecialDay(55, "0920", "国际爱牙日", "   "));
		list.add(new SpecialDay(56, "0927", "世界旅游日", "   "));

		list.add(new SpecialDay(57, "0915", "中秋节", "   "));

		// -------------------------------

		list.add(new SpecialDay(58, "1001", "国际音乐节", "   "));
		list.add(new SpecialDay(59, "1007", "国际住房日", "   "));
		list.add(new SpecialDay(60, "1008", "世界视觉日", "   "));
		list.add(new SpecialDay(61, "1009", "世界邮政日", "   "));
		list.add(new SpecialDay(62, "1010", "世界精神卫生日", "   "));
		list.add(new SpecialDay(63, "1015", "国际盲人节", "   "));
		list.add(new SpecialDay(64, "1016", "世界粮食节", "   "));
		list.add(new SpecialDay(65, "1021", "中国(揭阳)国际玉器节", "   "));
		list.add(new SpecialDay(66, "1022", "世界传统医药日", "   "));
		list.add(new SpecialDay(67, "1024", "联合国日", "   "));
		list.add(new SpecialDay(68, "1025", "人类天花绝迹日", "   "));
		list.add(new SpecialDay(69, "1026", "足球诞生日", "   "));
		list.add(new SpecialDay(70, "1031", "万圣节前夜", "   "));

		list.add(new SpecialDay(86, "1009", "重阳节", "   "));

		// -------------------------------

		list.add(new SpecialDay(71, "1108", "中国记者日", "   "));
		list.add(new SpecialDay(72, "1109", "消防宣传日", "   "));
		list.add(new SpecialDay(73, "1111", "光棍节", "   "));
		list.add(new SpecialDay(74, "1114", "世界糖尿病日", "   "));
		list.add(new SpecialDay(75, "1117", "国际大学生节", "   "));

		list.add(new SpecialDay(76, "1124", "感恩节", "   "));

		// -------------------------------

		list.add(new SpecialDay(77, "1201", "世界艾滋病日", "   "));
		list.add(new SpecialDay(78, "1203", "世界残疾人日", "   "));
		list.add(new SpecialDay(79, "1209", "世界足球日", "   "));
		list.add(new SpecialDay(80, "1210", "人权日", "   "));
		list.add(new SpecialDay(81, "1220", "澳门回归纪念日", "   "));
		list.add(new SpecialDay(82, "1221", "国际篮球日", "   "));
		list.add(new SpecialDay(83, "1224", "平安夜", "   "));
		list.add(new SpecialDay(84, "1225", "圣诞节", "   "));
		list.add(new SpecialDay(85, "1226", "毛泽东诞辰", "   "));

	}

	private void initView() {
		mViewPager = (ViewPager) findViewById(R.id.id_viewpager);

		LayoutInflater mInflater = LayoutInflater.from(this);
		View tab01 = mInflater.inflate(R.layout.tab01, null);
		View tab02 = mInflater.inflate(R.layout.tab02, null);
		View tab03 = mInflater.inflate(R.layout.tab03, null);
		View tab04 = mInflater.inflate(R.layout.tab04, null);
		mViews.add(tab01);
		mViews.add(tab02);
		mViews.add(tab03);
		mViews.add(tab04);

		mEnterButton = (Button) tab04.findViewById(R.id.imgbtn_enter);

		// 最后的参数一定要和发送方的相同，否则得到空值
		String funcName = getIntent().getExtras().getString("FuncName");

		// 仅仅是查看
		if (funcName.equals("Guidance")) {
			mEnterButton.setVisibility(View.GONE);
		} else {
			// 初始化APP
			mEnterButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					try {
						// 生成节日表
						db = Connector.getDatabase();
						list = new ArrayList<SpecialDay>();

						initDate();

						// 保存
						DataSupport.saveAll(list);

						// 加载默认设置
						SharedPreferences prefs = getSharedPreferences(
								ConstValue.SETTING_PF, Context.MODE_PRIVATE);
						Editor editor = prefs.edit();
						editor.putBoolean("showWord", true);
						editor.putBoolean("showSIMInfo", true);
						editor.putBoolean("showNickName", true);
						editor.putBoolean("autoWord", false);
						editor.putString("nickName", "Lance Chang");
						editor.putString("word",
								"Early to bed and early to rise makes a man healthy, wealthy, and wise. ");
						editor.putBoolean("showSpecialDay", true);
						editor.putBoolean("showBatterCharging", true);
						editor.putBoolean("showBatteryLosing", true);
						editor.putBoolean("showMsg", true);
						editor.putString("horoscope", "gn_day_ic_aquarius");
						editor.putBoolean("showHoroscope", true);
						editor.commit();

						// APPGCHelper.SaveAsJson(GuideActivity.this,
						// APPGCHelper.FILTER_THIRD_APP);

					} catch (Exception e) {
						// TODO: handle exception
						Logger.d("GuideActivity", e.getMessage());
						CrashReport.postCatchedException(e);
					}

					Intent intent = new Intent(GuideActivity.this,
							SplashActivity.class);
					startActivity(intent);
					GuideActivity.this.finish();
				}
			});
		}

		mAdapter = new PagerAdapter() {

			@Override
			public void destroyItem(ViewGroup container, int position,
					Object object) {
				container.removeView(mViews.get(position));
			}

			@Override
			public Object instantiateItem(ViewGroup container, int position) {
				View view = mViews.get(position);
				container.addView(view);
				return view;
			}

			@Override
			public boolean isViewFromObject(View arg0, Object arg1) {
				return arg0 == arg1;
			}

			@Override
			public int getCount() {
				return mViews.size();
			}
		};

		mViewPager.setAdapter(mAdapter);

	}

	@Override
	public void onClick(View v) {

	}

}
