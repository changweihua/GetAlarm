package net.cmono.adapters;

import net.cmono.getalarm.R;
import net.cmono.utils.BaseViewHolderUtil;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class BorderedGridAdapter extends BaseAdapter {
	private Context mContext;

	public String[] img_text = { "�������", "���ע��", "��������", "ϵͳ����", "�û��˻�",
			"����ģʽ", "��Ϣ֪ͨ", "��������", "��������" };
	public int[] imgs = { R.drawable.app_phonecharge, R.drawable.htc_8x,
			R.drawable.app_fund, R.drawable.app_movie, R.drawable.app_group,
			R.drawable.app_engine, R.drawable.app_notification,
			R.drawable.app_financial, R.drawable.app_plane };

	public BorderedGridAdapter(Context mContext) {
		super();
		this.mContext = mContext;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return img_text.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.bordered_grid_item, parent, false);
		}
		TextView tv = BaseViewHolderUtil.get(convertView, R.id.tv_item);
		ImageView iv = BaseViewHolderUtil.get(convertView, R.id.iv_item);
		iv.setBackgroundResource(imgs[position]);

		tv.setText(img_text[position]);
		return convertView;
	}

}
