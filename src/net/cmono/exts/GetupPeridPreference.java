package net.cmono.exts;

import net.cmono.getalarm.R;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

public class GetupPeridPreference extends ListPreference implements
		OnPreferenceClickListener {
	private Drawable icon;
	private boolean[] mClickedDialogEntryIndices;
	String[] weeks;

	public GetupPeridPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.setLayoutResource(R.layout.icon_list_preference);
		// TODO Auto-generated constructor stub
		icon = context.getResources().getDrawable(R.mipmap.ic_launcher_tips);
		weeks = GetupPeridPreference.this.getContext().getResources()
				.getStringArray(R.array.horoscopes);
		mClickedDialogEntryIndices = new boolean[weeks.length];
		// TODO Auto-generated constructor stub
	}

	public GetupPeridPreference(Context context) {
		super(context);
		this.setLayoutResource(R.layout.icon_list_preference);
		// TODO Auto-generated constructor stub
		icon = context.getResources().getDrawable(R.mipmap.ic_launcher_tips);
		weeks = GetupPeridPreference.this.getContext().getResources()
				.getStringArray(R.array.horoscopes);
		mClickedDialogEntryIndices = new boolean[weeks.length];
		// TODO Auto-generated constructor stub
	}

	public boolean[] getmClickedDialogEntryIndices() {
		return mClickedDialogEntryIndices;
	}

	public void setmClickedDialogEntryIndices(
			boolean[] mClickedDialogEntryIndices) {
		this.mClickedDialogEntryIndices = mClickedDialogEntryIndices;
	}

	@Override
	protected void onBindView(View view) {
		// TODO Auto-generated method stub
		super.onBindView(view);
		final ImageView imageView = (ImageView) view.findViewById(R.id.icon);
		if (imageView != null && icon != null) {
			imageView.setImageDrawable(icon);
		}
	}

	@Override
	public Drawable getIcon() {
		return icon;
	}

	@Override
	public void setIcon(Drawable icon) {
		if ((icon == null && icon != null)
				|| (icon != null && !icon.equals(icon))) {
			this.icon = icon;
			notifyChanged();
		}
	}

	@Override
	public void setIcon(int icon) {
		if (icon != R.mipmap.ic_launcher_tips) {
			this.icon = getContext().getResources().getDrawable(icon);
			notifyChanged();
		}

	}

	@Override
	public void setEntries(CharSequence[] entries) {
		// TODO Auto-generated method stub
		super.setEntries(entries);
		mClickedDialogEntryIndices = new boolean[weeks.length];
	}

	@Override
	public boolean onPreferenceClick(Preference preference) {
		// TODO Auto-generated method stub
		return false;
	}

}