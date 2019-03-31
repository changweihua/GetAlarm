package net.cmono.dtos;

import org.litepal.crud.DataSupport;

public class SpecialDay extends DataSupport {

	private int id;
	private String day;
	private String name;
	private String description;

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public SpecialDay() {
		super();
	}

	public SpecialDay(int id, String day, String name, String description) {
		super();
		this.id = id;
		this.day = day;
		this.name = name;
		this.description = description;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
