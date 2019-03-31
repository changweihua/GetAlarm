package net.cmono.dtos;

import android.widget.Adapter;

public class Info {

	private String mTitle;

	private Adapter mAdapter;

	public Info(String title, Adapter adapter) {
		mTitle = title;
		mAdapter = adapter;
	}

	public void setTile(String title) {
		mTitle = title;
	}

	public String getTitle() {
		return mTitle;
	}

	public void setAdapter(Adapter adapter) {
		mAdapter = adapter;
	}

	public Adapter getAdapter() {
		return mAdapter;
	}

}