package net.cmono.dtos;

import java.util.Date;

import org.litepal.crud.DataSupport;

public class NotificationInfo extends DataSupport {
	private int id;
	private String title;
	private String text;
	private String subtext;
	private String icon;
	private String pkgname;
	public String getPkgname() {
		return pkgname;
	}

	public void setPkgname(String pkgname) {
		this.pkgname = pkgname;
	}

	private int status;
	private Date date;

	public NotificationInfo() {
		super();
	}

	public NotificationInfo(int id, String title, String text, String subtext,
			String icon, int status, Date date,String pkgname) {
		super();
		this.id = id;
		this.title = title;
		this.text = text;
		this.subtext = subtext;
		this.icon = icon;
		this.status = status;
		this.date = date;
		this.pkgname = pkgname;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getSubtext() {
		return subtext;
	}

	public void setSubtext(String subtext) {
		this.subtext = subtext;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
}
