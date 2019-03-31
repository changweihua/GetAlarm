package net.cmono.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

//DatabaseHelper ����Ϊһ������SQLite��������
//1, getReadablebase(),getWriteableDatabase()�Ի��SQLiteDatabse����ͨ���ö�����Զ����ݿ���в���  
//�ڶ����ṩ��onCreate()��onUpgrade()�����ص����������������ڴ������������ݿ�ʱ�������Լ��Ĳ��� 
public class DatabaseUtil extends SQLiteOpenHelper {

	// ���캯��
	private static final int VERSION = 1;

	public DatabaseUtil(Context context, String name, CursorFactory factory,
			int version) {
		// ����ͨ��super���ø��൱�еĹ��캯��
		// context��ACtivity����
		// name���������
		// version:��ǰ���ݿ�汾������������
		super(context, name, factory, version);
	}

	public DatabaseUtil(Context context, String name) {
		this(context, name, VERSION);
	}

	public DatabaseUtil(Context context, String name, int version) {
		this(context, name, null, VERSION);
	}

	// ���ú�������һ�δ������ݿ��ʱ��ִ��
	@Override
	public void onCreate(SQLiteDatabase db) {
		System.out.println("create a Database");
		// execSQL��������ִ��SQL���
		db.execSQL("create table config(_id int, name varchar(200), value text, avator blob)");

	}

	// ���ݿ��ļ��İ汾���ڸ��µ�ʱ�����,����Ϊ������µ��ֶΣ�Ҫ�޸İ汾��
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		System.out.println("update a Database");
	}
}
