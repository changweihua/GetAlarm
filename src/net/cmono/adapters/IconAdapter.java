package net.cmono.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public abstract class IconAdapter<T> extends BaseAdapter {

	private ArrayList<T> mData;
	private int mLayoutRes; // ����id

	public IconAdapter() {
	}

	public IconAdapter(ArrayList<T> mData, int mLayoutRes) {
		this.mData = mData;
		this.mLayoutRes = mLayoutRes;
	}

	@Override
	public int getCount() {
		return mData != null ? mData.size() : 0;
	}

	@Override
	public T getItem(int position) {
		return mData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = ViewHolder.bind(parent.getContext(), convertView,
				parent, mLayoutRes, position);
		bindView(holder, getItem(position));
		return holder.getItemView();
	}

	public abstract void bindView(ViewHolder holder, T obj);

	// ���һ��Ԫ��
	public void add(T data) {
		if (mData == null) {
			mData = new ArrayList<>();
		}
		mData.add(data);
		notifyDataSetChanged();
	}

	// ���ض�λ�ã����һ��Ԫ��
	public void add(int position, T data) {
		if (mData == null) {
			mData = new ArrayList<>();
		}
		mData.add(position, data);
		notifyDataSetChanged();
	}

	public void remove(T data) {
		if (mData != null) {
			mData.remove(data);
		}
		notifyDataSetChanged();
	}

	public void remove(int position) {
		if (mData != null) {
			mData.remove(position);
		}
		notifyDataSetChanged();
	}

	public void clear() {
		if (mData != null) {
			mData.clear();
		}
		notifyDataSetChanged();
	}

	public static class ViewHolder {

		private SparseArray<View> mViews; // �洢ListView �� item�е�View
		private View item; // ���convertView
		private int position; // �α�
		private Context context; // Context������

		// ���췽���������س�ʼ��
		private ViewHolder(Context context, ViewGroup parent, int layoutRes) {
			mViews = new SparseArray<>();
			this.context = context;
			View convertView = LayoutInflater.from(context).inflate(layoutRes,
					parent, false);
			convertView.setTag(this);
			item = convertView;
		}

		// ��ViewHolder��item
		public static ViewHolder bind(Context context, View convertView,
				ViewGroup parent, int layoutRes, int position) {
			ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder(context, parent, layoutRes);
			} else {
				holder = (ViewHolder) convertView.getTag();
				holder.item = convertView;
			}
			holder.position = position;
			return holder;
		}

		@SuppressWarnings("unchecked")
		public <T extends View> T getView(int id) {
			T t = (T) mViews.get(id);
			if (t == null) {
				t = (T) item.findViewById(id);
				mViews.put(id, t);
			}
			return t;
		}

		/**
		 * ��ȡ��ǰ��Ŀ
		 */
		public View getItemView() {
			return item;
		}

		/**
		 * ��ȡ��Ŀλ��
		 */
		public int getItemPosition() {
			return position;
		}

		/**
		 * ��������
		 */
		public ViewHolder setText(int id, CharSequence text) {
			View view = getView(id);
			if (view instanceof TextView) {
				((TextView) view).setText(text);
			}
			return this;
		}

		/**
		 * ����ͼƬ
		 */
		public ViewHolder setImageResource(int id, int drawableRes) {
			View view = getView(id);
			if (view instanceof ImageView) {
				((ImageView) view).setImageResource(drawableRes);
			} else {
				view.setBackgroundResource(drawableRes);
			}
			return this;
		}

		/**
		 * ���õ������
		 */
		public ViewHolder setOnClickListener(int id,
				View.OnClickListener listener) {
			getView(id).setOnClickListener(listener);
			return this;
		}

		/**
		 * ���ÿɼ�
		 */
		public ViewHolder setVisibility(int id, int visible) {
			getView(id).setVisibility(visible);
			return this;
		}

		/**
		 * ���ñ�ǩ
		 */
		public ViewHolder setTag(int id, Object obj) {
			getView(id).setTag(obj);
			return this;
		}

		// ����������������չ

	}

}
