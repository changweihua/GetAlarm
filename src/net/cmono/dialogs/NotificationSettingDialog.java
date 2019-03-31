package net.cmono.dialogs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



import net.cmono.dtos.AppInfo;
import net.cmono.getalarm.GetAlarmApplication;
import net.cmono.getalarm.R;
import net.cmono.utils.APPGCUtil;
import net.cmono.utils.Logger;
import net.cmono.utils.ViewFindUtil;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.flyco.animation.FadeEnter.FadeEnter;
import com.flyco.animation.FadeExit.FadeExit;
import com.flyco.dialog.utils.CornerUtils;
import com.flyco.dialog.widget.base.BaseDialog;

public class NotificationSettingDialog extends
		BaseDialog<NotificationSettingDialog> {

	// ------

	private TextView tv_export;
	private TextView tv_exit;

	
	private ListView listView;

	private Map<Integer, Boolean> isSelected;

	private List beSelectedData = new ArrayList();

	APPListAdapter adapter;

	View rootView;

	private List cs = null;

	// ------

	private Context mContext;

	public NotificationSettingDialog(Context context) {
		super(context);
		mContext = context;
		this.cancel=false;
	}

	@SuppressWarnings("deprecation")
	@Override
	public View onCreateView() {
		widthScale(0.85f);
		showAnim(new FadeEnter());
		dismissAnim(new FadeExit());

		rootView = View.inflate(mContext, R.layout.dialog_notification_setting,
				null);

		rootView.setBackgroundDrawable(CornerUtils.cornerDrawable(
				Color.parseColor("#ffffff"), dp2px(5)));

		// ------
		tv_export = ViewFindUtil.find(rootView, R.id.ns_export);
		tv_exit = ViewFindUtil.find(rootView, R.id.ns_exit);
		listView = (ListView) rootView.findViewById(R.id.lv_app_list);
		// ViewStub viewStub= new ViewStub(rootView.getContext());
		// listView.addHeaderView(viewStub);
		// listView.addFooterView(viewStub);
		cs = new ArrayList();

		List<AppInfo> apps = APPGCUtil
				.queryFilterAppInfo(GetAlarmApplication.getAppContext(),
						APPGCUtil.FILTER_ALL_APP);

		for (AppInfo appInfo : apps) {
			cs.add(appInfo.getPkgName());
			//Logger.d("==", appInfo.getPkgName());
		}

		initList();

		// ------

		return rootView;
	}

	void initList() {

		if (cs == null || cs.size() == 0)
			return;
		if (isSelected != null)
			isSelected = null;
		isSelected = new HashMap<Integer, Boolean>();
		for (int i = 0; i < cs.size(); i++) {
			isSelected.put(i, false);
		}
		// 清除已经选择的项
		if (beSelectedData.size() > 0) {
			beSelectedData.clear();
		}
		adapter = new APPListAdapter(rootView.getContext(), cs);
		listView.setAdapter(adapter);
		listView.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
		adapter.notifyDataSetChanged();
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Logger.d("map", cs.get(position).toString());
			}
		});

	}

	class APPListAdapter extends BaseAdapter {

		private Context context;

		private List cs;

		private LayoutInflater inflater;

		public APPListAdapter(Context context, List data) {
			this.context = context;
			this.cs = data;
			initLayoutInflater();
		}

		void initLayoutInflater() {
			inflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			return cs.size();
		}

		@Override
		public Object getItem(int position) {
			return cs.get(position);
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position1, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			View view = null;
			final int position = position1;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.item_app_list_adapter,
						null);
				holder = new ViewHolder();
				holder.checkBox = (CheckBox) convertView
						.findViewById(R.id.item_cb_section);
				holder.tv_sequence = (TextView) convertView
						.findViewById(R.id.tv_zxing_section_sequence);
				holder.tv_sectionname = (TextView) convertView
						.findViewById(R.id.tv_zxing_sectionname);
				convertView.setTag(holder);
			} else {
				view = convertView;
				holder = (ViewHolder) view.getTag();
			}
			holder.checkBox.setOnClickListener(new CheckBox.OnClickListener() {
				@Override
				public void onClick(View v) {
					// 当前点击的CB
					boolean cu = !isSelected.get(position);
					// 先将所有的置为FALSE
					for (Integer p : isSelected.keySet()) {
						isSelected.put(p, false);
					}
					// 再将当前选择CB的实际状态
					isSelected.put(position, cu);
					APPListAdapter.this.notifyDataSetChanged();
					beSelectedData.clear();
					if (cu)
						beSelectedData.add(cs.get(position));
				}

			});
			holder.tv_sequence.setText(String.valueOf(position + 1));
			holder.tv_sectionname.setText(cs.get(position).toString());
			holder.checkBox.setChecked(isSelected.get(position));
			return convertView;
		}
	}

	class ViewHolder {

		CheckBox checkBox;

		TextView tv_sequence;

		TextView tv_sectionname;

	}

	@Override
	public void setUiBeforShow() {
		tv_export.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
		tv_exit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				dismiss();
			}
		});
	}

}
