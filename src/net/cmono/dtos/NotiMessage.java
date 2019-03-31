package net.cmono.dtos;


public class NotiMessage {
	 private int unreadCount;
	 private NotificationInfo notificationInfo;
	 private String pkgName;
	 
	 public String getPkgName() {
		return pkgName;
	}
	public void setPkgName(String pkgName) {
		this.pkgName = pkgName;
	}
	public int getUnreadCount() {
		return unreadCount;
	}
	public void setUnreadCount(int unreadCount) {
		this.unreadCount = unreadCount;
	}
	public NotificationInfo getNotificationInfo() {
		return notificationInfo;
	}
	public void setNotificationInfo(NotificationInfo notificationInfo) {
		this.notificationInfo = notificationInfo;
	}
	
	
}
