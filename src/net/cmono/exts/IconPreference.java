package net.cmono.exts;

import net.cmono.getalarm.R;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

public class IconPreference extends Preference {
	private Drawable mIcon;

	public IconPreference(final Context context, final AttributeSet attrs,
			final int defStyle) {
		super(context, attrs);

		this.setLayoutResource(R.layout.icon_list_preference);

		// 这里设置的是icon初始化的图标
		this.mIcon = context.getResources().getDrawable(
				R.drawable.horoscope_icon);
	}

	public IconPreference(final Context context, final AttributeSet attrs) {
		this(context, attrs, 0);
	}

	@Override
	protected void onBindView(final View view) {
		super.onBindView(view);
		final ImageView imageView = (ImageView) view.findViewById(R.id.icon);
		if ((imageView != null) && (this.mIcon != null)) {
			imageView.setImageDrawable(this.mIcon);
		}
	}

	/**
	 * Sets the icon for this Preference with a Drawable.
	 * 
	 * @param icon
	 *            The icon for this Preference
	 */
	@Override
	public void setIcon(final Drawable icon) {
		if (((icon == null) && (this.mIcon != null))
				|| ((icon != null) && (!icon.equals(this.mIcon)))) {
			this.mIcon = icon;
			this.notifyChanged();
		}
	}

	@Override
	public void setIcon(int iconRes) {
		if (R.drawable.horoscope_icon != iconRes) {
			this.mIcon = getContext().getResources().getDrawable(iconRes);
			this.notifyChanged();
		}
	}

	/**
	 * Returns the icon of this Preference.
	 * 
	 * @return The icon.
	 * @see #setIcon(Drawable)
	 */
	@Override
	public Drawable getIcon() {
		return this.mIcon;
	}
}
