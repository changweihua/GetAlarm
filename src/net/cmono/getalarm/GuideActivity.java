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
	 * �������ݸ�ֵ
	 */
	private void initDate() {

		list.add(new SpecialDay(1, "0101", "����Ԫ��", "   "));
		list.add(new SpecialDay(2, "0106", "�й�ʮ�����˿���", "   "));
		list.add(new SpecialDay(3, "0110", "�й�110������", "   "));

		list.add(new SpecialDay(11, "0117", "���˽�", "   "));

		// -------------------------------

		list.add(new SpecialDay(4, "0202", "����ʪ����", "   "));
		list.add(new SpecialDay(5, "0204", "���翹��֢��", "   "));
		list.add(new SpecialDay(6, "0210", "����������", "   "));
		list.add(new SpecialDay(7, "0214", "���˽�", "   "));
		list.add(new SpecialDay(8, "0221", "����ĸ���", "   "));

		list.add(new SpecialDay(12, "0201", "�����", "   "));
		list.add(new SpecialDay(13, "0207", "��Ϧ", "   "));
		list.add(new SpecialDay(14, "0208", "����", "   "));
		list.add(new SpecialDay(15, "0212", "����", "   "));
		list.add(new SpecialDay(16, "0222", "Ԫ����", "   "));

		// -------------------------------

		list.add(new SpecialDay(9, "0308", "��Ů��", "   "));
		list.add(new SpecialDay(10, "0312", "ֲ����", "   "));
		list.add(new SpecialDay(17, "0315", "����������", "   "));
		list.add(new SpecialDay(18, "0303", "ȫ��������", "   "));
		list.add(new SpecialDay(19, "0321", "����ɭ����", "   "));
		list.add(new SpecialDay(20, "0322", "����ˮ��", "   "));
		list.add(new SpecialDay(21, "0323", "����������", "   "));
		list.add(new SpecialDay(22, "0324", "������ν�˲���", "   "));

		list.add(new SpecialDay(23, "0310", "���¶���̧ͷ", "   "));

		// -------------------------------

		list.add(new SpecialDay(24, "0401", "���˽�", "   "));
		list.add(new SpecialDay(25, "0405", "������", "   "));
		list.add(new SpecialDay(26, "0407", "����������", "   "));
		list.add(new SpecialDay(27, "0422", "���������", "   "));

		// -------------------------------

		list.add(new SpecialDay(28, "0501", "�����Ͷ���", "   "));
		list.add(new SpecialDay(29, "0504", "�й������", "   "));
		list.add(new SpecialDay(30, "0505", "ȫ����ȱ����", "   "));
		list.add(new SpecialDay(31, "0508", "�����ʮ����", "   "));
		list.add(new SpecialDay(32, "0512", "���ʻ�ʿ��", "   "));
		list.add(new SpecialDay(33, "0515", "���ʼ�ͥ��", "   "));
		list.add(new SpecialDay(34, "0517", "���������", "   "));
		list.add(new SpecialDay(35, "0518", "���ʲ������", "   "));
		list.add(new SpecialDay(36, "0531", "����������", "   "));

		list.add(new SpecialDay(39, "0508", "ĸ�׽�", "   "));

		// -------------------------------

		list.add(new SpecialDay(37, "0601", "���ʶ�ͯ��", "   "));
		list.add(new SpecialDay(38, "0605", "���绷����", "   "));
		list.add(new SpecialDay(40, "0606", "ȫ��������", "   "));
		list.add(new SpecialDay(41, "0623", "���ʰ���ƥ����", "   "));
		list.add(new SpecialDay(42, "0625", "ȫ��������", "   "));

		list.add(new SpecialDay(43, "0609", "�����", "   "));
		list.add(new SpecialDay(44, "0619", "���׽�", "   "));

		// -------------------------------

		list.add(new SpecialDay(45, "0701", "��ۻع���", "   "));
		list.add(new SpecialDay(46, "0711", "�����˿���", "   "));

		// -------------------------------

		list.add(new SpecialDay(47, "0801", "��һ������", "   "));

		list.add(new SpecialDay(48, "0805", "��Լ���˻ῪĻ", "   "));
		list.add(new SpecialDay(49, "0821", "��Լ���˻��Ļ", "   "));
		list.add(new SpecialDay(50, "0809", "��Ϧ���˽�", "   "));
		list.add(new SpecialDay(51, "0817", "���", "   "));

		// -------------------------------

		list.add(new SpecialDay(52, "0908", "����ɨä��", "   "));
		list.add(new SpecialDay(53, "0910", "��ʦ��", "   "));
		list.add(new SpecialDay(54, "0917", "���ʺ�ƽ��", "   "));
		list.add(new SpecialDay(55, "0920", "���ʰ�����", "   "));
		list.add(new SpecialDay(56, "0927", "����������", "   "));

		list.add(new SpecialDay(57, "0915", "�����", "   "));

		// -------------------------------

		list.add(new SpecialDay(58, "1001", "�������ֽ�", "   "));
		list.add(new SpecialDay(59, "1007", "����ס����", "   "));
		list.add(new SpecialDay(60, "1008", "�����Ӿ���", "   "));
		list.add(new SpecialDay(61, "1009", "����������", "   "));
		list.add(new SpecialDay(62, "1010", "���羫��������", "   "));
		list.add(new SpecialDay(63, "1015", "����ä�˽�", "   "));
		list.add(new SpecialDay(64, "1016", "������ʳ��", "   "));
		list.add(new SpecialDay(65, "1021", "�й�(����)����������", "   "));
		list.add(new SpecialDay(66, "1022", "���紫ͳҽҩ��", "   "));
		list.add(new SpecialDay(67, "1024", "���Ϲ���", "   "));
		list.add(new SpecialDay(68, "1025", "�����컨������", "   "));
		list.add(new SpecialDay(69, "1026", "��������", "   "));
		list.add(new SpecialDay(70, "1031", "��ʥ��ǰҹ", "   "));

		list.add(new SpecialDay(86, "1009", "������", "   "));

		// -------------------------------

		list.add(new SpecialDay(71, "1108", "�й�������", "   "));
		list.add(new SpecialDay(72, "1109", "����������", "   "));
		list.add(new SpecialDay(73, "1111", "�����", "   "));
		list.add(new SpecialDay(74, "1114", "����������", "   "));
		list.add(new SpecialDay(75, "1117", "���ʴ�ѧ����", "   "));

		list.add(new SpecialDay(76, "1124", "�ж���", "   "));

		// -------------------------------

		list.add(new SpecialDay(77, "1201", "���簬�̲���", "   "));
		list.add(new SpecialDay(78, "1203", "����м�����", "   "));
		list.add(new SpecialDay(79, "1209", "����������", "   "));
		list.add(new SpecialDay(80, "1210", "��Ȩ��", "   "));
		list.add(new SpecialDay(81, "1220", "���Żع������", "   "));
		list.add(new SpecialDay(82, "1221", "����������", "   "));
		list.add(new SpecialDay(83, "1224", "ƽ��ҹ", "   "));
		list.add(new SpecialDay(84, "1225", "ʥ����", "   "));
		list.add(new SpecialDay(85, "1226", "ë�󶫵���", "   "));

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

		// ���Ĳ���һ��Ҫ�ͷ��ͷ�����ͬ������õ���ֵ
		String funcName = getIntent().getExtras().getString("FuncName");

		// �����ǲ鿴
		if (funcName.equals("Guidance")) {
			mEnterButton.setVisibility(View.GONE);
		} else {
			// ��ʼ��APP
			mEnterButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					try {
						// ���ɽ��ձ�
						db = Connector.getDatabase();
						list = new ArrayList<SpecialDay>();

						initDate();

						// ����
						DataSupport.saveAll(list);

						// ����Ĭ������
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
