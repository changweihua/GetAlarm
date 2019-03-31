package net.cmono.utils;

import android.util.SparseArray;
import android.view.View;

@SuppressWarnings({ "unchecked", "hiding" })
public class ViewFindUtil {
	/**
	 * ViewHolder���д��,�������������ظ�����ViewHolder,���ٴ����� �÷�:
	 * 
	 * <pre>
	 * if (convertView == null) {
	 * 	convertView = View.inflate(context, R.layout.ad_demo, null);
	 * }
	 * TextView tv_demo = ViewHolderUtils.get(convertView, R.id.tv_demo);
	 * ImageView iv_demo = ViewHolderUtils.get(convertView, R.id.iv_demo);
	 * </pre>
	 */
	public static <T extends View> T hold(View view, int id) {
		SparseArray<View> viewHolder = (SparseArray<View>) view.getTag();

		if (viewHolder == null) {
			viewHolder = new SparseArray<View>();
			view.setTag(viewHolder);
		}

		View childView = viewHolder.get(id);

		if (childView == null) {
			childView = view.findViewById(id);
			viewHolder.put(id, childView);
		}

		return (T) childView;
	}

	/**
	 * ���findviewById����
	 */
	public static <T extends View> T find(View view, int id) {
		return (T) view.findViewById(id);
	}
}
