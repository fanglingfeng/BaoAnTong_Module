package com.tjsoft.webhall.db;



import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
/**
 * sqlite数据库创建和建表、更新
 * @author Administrator
 *
 */
public class DBHandler extends SQLiteOpenHelper {
	private static int DB_VERSION = 4;
	private static String DB_NAME = "data.db";
	private static SQLiteDatabase database;
	public DBHandler(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}
	public static SQLiteDatabase getWritableDatabase(Context context){
		if (null == database){
			SQLiteDatabase db = new DBHandler(context).getWritableDatabase();
			database =db ;
			return db;
		}
		else{
			return database;
		}
		}
	/**
	 * 创建favotite表单
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		System.out.println("fuchl  db  onCreate");
		db.execSQL("drop table if exists favorite");
		db.execSQL("create table favorite( ID  varchar(20) not null , LARGEITEMID varchar(20) , SMALLITEMID varchar(20) , SXZXNAME varchar(20) , ITEMVERSION varchar(20) , ITEMLIMITTIME varchar(20) , ITEMLIMITUNIT varchar(20) , REGIONID varchar(20) , DEPTID varchar(20) , DEPTNAME varchar(20) , LAWADDR varchar(20) , REALADDR varchar(20) , XZXK varchar(20) , SFYDSB varchar(20), ISRESERVE varchar(20),CODE1 varchar(20),CODE2 varchar(20),CODE3 varchar(20),CODE4 varchar(20),WSSBDZ text,WSZXDZ text,JGCXDZ text,JDCXDZ text);");
		
//		db.execSQL("drop table if exists guide");
//		db.execSQL("create table guide(ID varchar(50) not null,SXZXNAME text,DEPTID text,DEPTNAME text,XZXK text,SPTJ text,SQCL text,SPCX text,WLBLLC text,LIMITDAYS text,CHARGE text,CJWTJD text,LAWPRODUCE text,FORMS text,WINDOWS text)");

	}
	/**
	 * 更新数据库的方法
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		System.out.println("fuchl  db  onUpgrade");
		db.execSQL("drop table if exists favorite");
		db.execSQL("create table favorite( ID  varchar(20) not null , LARGEITEMID varchar(20) , SMALLITEMID varchar(20) , SXZXNAME varchar(20) , ITEMVERSION varchar(20) , ITEMLIMITTIME varchar(20) , ITEMLIMITUNIT varchar(20) , REGIONID varchar(20) , DEPTID varchar(20) , DEPTNAME varchar(20) , LAWADDR varchar(20) , REALADDR varchar(20) , XZXK varchar(20) , SFYDSB varchar(20), ISRESERVE varchar(20),CODE1 varchar(20),CODE2 varchar(20),CODE3 varchar(20),CODE4 varchar(20),WSSBDZ text,WSZXDZ text,JGCXDZ text,JDCXDZ text);");
//		db.execSQL("drop table if exists guide");
//		db.execSQL("create table guide(ID varchar(50) not null,SXZXNAME text,DEPTID text,DEPTNAME text,XZXK text,SPTJ text,SQCL text,SPCX text,WLBLLC text,LIMITDAYS text,CHARGE text,CJWTJD text,LAWPRODUCE text,FORMS text,WINDOWS text)");

		
		//	    if (oldVersion==2){
//	        db.execSQL("ALTER TABLE favorite TO favorite_temp");
//	        db.execSQL("create table favorite_new( ID  varchar(20) not null , LARGEITEMID varchar(20) , SMALLITEMID varchar(20) , SXZXNAME varchar(20) , ITEMVERSION varchar(20) , ITEMLIMITTIME varchar(20) , ITEMLIMITUNIT varchar(20) , REGIONID varchar(20) , DEPTID varchar(20) , DEPTNAME varchar(20) , LAWADDR varchar(20) , REALADDR varchar(20) , XZXK varchar(20) , SFYDSB varchar(20), ISRESERVE varchar(20),PERMCODE varchar(20),WSSBDZ text,WSZXDZ text,JGCXDZ text,JDCXDZ text);");
//	        db.execSQL("insert into favorite_new(_id, region, code, country) " 
//	            + "select _id, region, code, \"CHINA\" from t_region_temp");
//	        db.execSQL("DROP TABLE t_region_temp");
//	      }
	}

}
