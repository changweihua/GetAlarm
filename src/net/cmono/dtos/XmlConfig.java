package net.cmono.dtos;

import java.util.ArrayList;

public class XmlConfig {

	private int id;
	private String name;
	private float price;

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

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	// @Override
	// public String toString() {
	// return "id:" + id + ", name:" + name + ", price:" + price;
	// }

	public ArrayList<String> ToAObj() {
		ArrayList<String> list = new ArrayList<String>();
		list.add(0, Integer.toString(id));
		list.add(1, name);
		return list;
	}

}
