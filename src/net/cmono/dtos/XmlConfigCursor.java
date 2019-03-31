package net.cmono.dtos;

import java.util.ArrayList;

import android.database.AbstractCursor;

public class XmlConfigCursor extends AbstractCursor {

	private String[] columnNames = { "id", "name" };// 构建cursor时必须先传入列明数组以规定列数
	private int allDataCnt = 0;// 总记录行数
	private int columnNum = 0;
	private int logicNum = 0;
	private int currentPosition = 0;
	private final int MAX_SHOW_NUM = 10;

	/**
	 * 数据区域
	 */
	private ArrayList<ArrayList<String>> allDatas = new ArrayList<ArrayList<String>>();// 在构造的时候填充数据，里层数据的size=columnNames.leng
	private ArrayList<ArrayList<String>> currentDatas = null;// 在fillwindow时填充
	private ArrayList<String> oneLineData = null;// onMove时填充

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return allDataCnt;
	}

	@Override
	public String[] getColumnNames() {
		// TODO Auto-generated method stub
		return columnNames;
	}

	@Override
	public String getString(int column) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public short getShort(int column) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getInt(int column) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getLong(int column) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float getFloat(int column) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getDouble(int column) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isNull(int column) {
		// TODO Auto-generated method stub
		return false;
	}

	public ArrayList<ArrayList<String>> getAllDatas() {
		return allDatas;
	}

	public void setAllDatas(ArrayList<ArrayList<String>> allDatas) {
		this.allDatas = allDatas;
	}

	public int getColumnNum() {
		return columnNum;
	}

	public void setColumnNum(int columnNum) {
		this.columnNum = columnNum;
	}

}
