package net.cmono.dtos;

public class NotifiedApp {
	private String pkgname;
	private String pkgicon;

	public NotifiedApp() {

	}

	public NotifiedApp(String pkgname, String pkgicon) {
		this.pkgname = pkgname;
		this.pkgicon = pkgicon;
	}

	public String getPkgicon() {
		return pkgicon;
	}

	public void setPkgicon(String pkgicon) {
		this.pkgicon = pkgicon;
	}

	public String getPkgname() {
		return pkgname;
	}

	public void setPkgname(String pkgname) {
		this.pkgname = pkgname;
	}

	
}
