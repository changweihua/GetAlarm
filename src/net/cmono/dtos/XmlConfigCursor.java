package net.cmono.dtos;

import java.util.ArrayList;

import android.database.AbstractCursor;

public class XmlConfigCursor extends AbstractCursor {

	private String[] columnNames = { "id", "name" };// ����cursorʱ�����ȴ������������Թ涨����
	private int allDataCnt = 0;// �ܼ�¼����
	private int columnNum = 0;
	private int logicNum = 0;
	private int currentPosition = 0;
	private final int MAX_SHOW_NUM = 10;

	/**
	 * ��������
	 */
	private ArrayList<ArrayList<String>> allDatas = new ArrayList<ArrayList<String>>();// �ڹ����ʱ��������ݣ�������ݵ�size=columnNames.leng
	private ArrayList<ArrayList<String>> currentDatas = null;// ��fillwindowʱ���
	private ArrayList<String> oneLineData = null;// onMoveʱ���

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
