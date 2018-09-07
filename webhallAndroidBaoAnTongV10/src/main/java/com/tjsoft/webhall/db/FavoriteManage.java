package com.tjsoft.webhall.db;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;

import com.tjsoft.util.DialogUtil;
import com.tjsoft.webhall.entity.Guide;
import com.tjsoft.webhall.entity.Permission;

/**
 * 我的收藏数据管理类
 * 
 * @author Administrator
 * 
 */
public class FavoriteManage {
	public static boolean exist(String id, Context context) {
		String sql = "select * from favorite where id=?;";
		Cursor c = DBHandler.getWritableDatabase(context).rawQuery(sql, new String[] { id });
		if (c.getCount() == 1) {
			c.close();
			return true;
		} else {
			c.close();
			return false;
		}
	}

	/**
	 * 查寻所有搜藏
	 * 
	 * @param context
	 *            应用上下文
	 * @return 事项列表
	 */
	public static List<Permission> selectAll(Context context) {
		String sql = "select * from favorite";
		List<Permission> permissions = new ArrayList<Permission>();
		Cursor c = DBHandler.getWritableDatabase(context).rawQuery(sql, new String[] {});
		if (c.getCount() > 0 && c.moveToFirst()) {
			for (int i = 0; i < c.getCount(); i++) {

				String ID = c.getString(c.getColumnIndex("ID"));
				String LARGEITEMID = c.getString(c.getColumnIndex("LARGEITEMID"));
				String SMALLITEMID = c.getString(c.getColumnIndex("SMALLITEMID"));
				String SXZXNAME = c.getString(c.getColumnIndex("SXZXNAME"));
				String ITEMVERSION = c.getString(c.getColumnIndex("ITEMVERSION"));
				String ITEMLIMITTIME = c.getString(c.getColumnIndex("ITEMLIMITTIME"));
				String ITEMLIMITUNIT = c.getString(c.getColumnIndex("ITEMLIMITUNIT"));
				String REGIONID = c.getString(c.getColumnIndex("REGIONID"));
				String DEPTID = c.getString(c.getColumnIndex("DEPTID"));
				String DEPTNAME = c.getString(c.getColumnIndex("DEPTNAME"));
				String LAWADDR = c.getString(c.getColumnIndex("LAWADDR"));
				String REALADDR = c.getString(c.getColumnIndex("REALADDR"));
				String XZXK = c.getString(c.getColumnIndex("XZXK"));
				String SFYDSB = c.getString(c.getColumnIndex("SFYDSB"));
				String ISRESERVE = c.getString(c.getColumnIndex("ISRESERVE"));
				String CODE1 = c.getString(c.getColumnIndex("CODE1"));
				String CODE2 = c.getString(c.getColumnIndex("CODE2"));
				String CODE3 = c.getString(c.getColumnIndex("CODE3"));
				String CODE4 = c.getString(c.getColumnIndex("CODE4"));
				String WSSBDZ = c.getString(c.getColumnIndex("WSSBDZ"));
				String WSZXDZ = c.getString(c.getColumnIndex("WSZXDZ"));
				String JGCXDZ = c.getString(c.getColumnIndex("JGCXDZ"));
				String JDCXDZ = c.getString(c.getColumnIndex("JDCXDZ"));

				Permission permission = new Permission(ID, LARGEITEMID, SMALLITEMID, SXZXNAME, ITEMVERSION, ITEMLIMITTIME, ITEMLIMITUNIT, REGIONID,
						DEPTID, DEPTNAME, LAWADDR, REALADDR, XZXK, SFYDSB, ISRESERVE,
						"", "", "", "", "", "", "",CODE1,CODE2,CODE3,CODE4,"",WSSBDZ,WSZXDZ,JGCXDZ,JDCXDZ);
				permissions.add(permission);
				c.moveToNext();
			}
			c.close();
			return permissions;
		} else {
			c.close();
			return new ArrayList<Permission>();
		}
	}

	/**
	 * 删除全部
	 * 
	 * @param context
	 *            应用上下文
	 */
	public static void deleteAll(Context context) {
		String sql = "delete from favorite";
		DBHandler.getWritableDatabase(context).execSQL(sql);
	}

	/**
	 * 
	 * @param p
	 *            事项对象
	 * @param context
	 *            应用上下文
	 */
	public static boolean add(Permission p, Context context) {

		if (!exist(p.getID(), context)) {
			String sql = "insert into favorite values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";
			DBHandler.getWritableDatabase(context).execSQL(sql, new Object[] { p.getID(), p.getLARGEITEMID(), 
					p.getSMALLITEMID(), p.getSXZXNAME(), p.getITEMVERSION(), p.getITEMLIMITTIME(),
					p.getITEMLIMITUNIT(), p.getREGIONID(), p.getDEPTID(), p.getDEPTNAME(), p.getLAWADDR(),
					p.getREALADDR(), p.getXZXK(), p.getSFYDSB(),p.getISRESERVE() ,
					p.getCODE1(),p.getCODE2(),p.getCODE3(),p.getCODE4(),p.getWSSBDZ(),p.getWSZXDZ(),p.getJGCXDZ(),p.getJDCXDZ()});

			return true;
		} else {

			return false;
		}
	}

	/**
	 * 
	 * @param id
	 *            事项id
	 * @param context
	 *            应用上下文
	 */
	public static void delete(String id, Context context) {
		String sql = "delete from favorite where id='" + id + "';";
		DBHandler.getWritableDatabase(context).execSQL(sql);

	}
	

}
