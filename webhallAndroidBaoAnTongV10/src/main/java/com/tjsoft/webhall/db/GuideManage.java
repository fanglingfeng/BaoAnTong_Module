package com.tjsoft.webhall.db;

import com.tjsoft.webhall.entity.Guide;
import com.tjsoft.webhall.entity.Permission;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class GuideManage {
	
	public static boolean exist(String id, Context context) {
		String sql = "select * from guide where id=?;";
		Cursor c = DBHandler.getWritableDatabase(context).rawQuery(sql, new String[] { id });
		if (c.getCount() == 1) {
			c.close();
			return true;
		} else {
			c.close();
			return false;
		}
	}
	
	public static void update(Guide g, Context context) {

		if (!exist(g.getID(), context)) {
			String sql = "insert into guide values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";
			DBHandler.getWritableDatabase(context).execSQL(sql, new Object[] { g.getID(),g.getSXZXNAME(),g.getDEPTID(),
					g.getDEPTNAME(),g.getXZXK(),g.getSPTJ(),g.getSQCL(),g.getSPCX(),g.getWLBLLC(),g.getLIMITDAYS(),g.getCHARGE(),g.getCJWTJD(),
					g.getLAWPRODUCE(),g.getFORMS(),g.getWINDOWS()});			
		} else {
			String sql="update guide set "
					+ "SXZXNAME=?,DEPTID=?,"
					+ "DEPTNAME=?,XZXK=?,SPTJ=?, "
					+ "SQCL=?,SPCX=?,WLBLLC=?,"
					+ "LIMITDAYS=?,CHARGE=?,CJWTJD=?,"
					+ "LAWPRODUCE=?,FORMS=?,WINDOWS=? "
					+ "where ID=?";
//			String sql1="update guide set SXZXNAME='"+g.getSXZXNAME()+"',"
//					+ "DEPTID='"+g.getDEPTID()+"' where ID=?";
			DBHandler.getWritableDatabase(context).execSQL(sql,
					new Object[] { g.getSXZXNAME(),g.getDEPTID(),
							g.getDEPTNAME(),g.getXZXK(),g.getSPTJ(),
							g.getSQCL(),g.getSPCX(),g.getWLBLLC(),
							g.getLIMITDAYS(),g.getCHARGE(),g.getCJWTJD(),
							g.getLAWPRODUCE(),g.getFORMS(),g.getWINDOWS(),
							g.getID()});
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
