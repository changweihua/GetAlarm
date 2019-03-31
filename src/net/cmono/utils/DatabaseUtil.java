package net.cmono.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

//DatabaseHelper 是作为一个访问SQLite的助手类
//1, getReadablebase(),getWriteableDatabase()以获得SQLiteDatabse对象，通过该对象可以对数据库进行操作  
//第二，提供了onCreate()和onUpgrade()两个回调函数，允许我们在创建和升级数据库时，进行自己的操作 
public class DatabaseUtil extends SQLiteOpenHelper {

	// 构造函数
	private static final int VERSION = 1;

	public DatabaseUtil(Context context, String name, CursorFactory factory,
			int version) {
		// 必须通过super调用父类当中的构造函数
		// context：ACtivity对象
		// name：表的名字
		// version:当前数据库版本（正数递增）
		super(context, name, factory, version);
	}

	public DatabaseUtil(Context context, String name) {
		this(context, name, VERSION);
	}

	public DatabaseUtil(Context context, String name, int version) {
		this(context, name, null, VERSION);
	}

	// 调用函数，第一次创建数据库的时候执行
	@Override
	public void onCreate(SQLiteDatabase db) {
		System.out.println("create a Database");
		// execSQL函数用于执行SQL语句
		db.execSQL("create table config(_id int, name varchar(200), value text, avator blob)");

	}

	// 数据库文件的版本号在更新的时候调用,所以为了添加新的字段，要修改版本号
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		System.out.println("update a Database");
	}
}
