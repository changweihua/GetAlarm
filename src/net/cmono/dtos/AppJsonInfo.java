package net.cmono.dtos;

public class AppJsonInfo {
	private String appLabel; // 应用程序标签
	public String getAppLabel() {
		return appLabel;
	}
	public void setAppLabel(String appLabel) {
		this.appLabel = appLabel;
	}
	public String getPkgName() {
		return pkgName;
	}
	public void setPkgName(String pkgName) {
		this.pkgName = pkgName;
	}
	public String getIconJson() {
		return iconJson;
	}
	public void setIconJson(String iconJson) {
		this.iconJson = iconJson;
	}
	private String pkgName; // 应用程序所对应的包名
	private String iconJson; // APP Icon Base64 String
}
