package com.pyy.commandoperation;

import java.util.List;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

public class OperationStartApp {
	private Context context;
	SQLiteDatabase mDb;  
	SQLiteDatabaseDao dao;

	public void doOperation(Context context, String opData) {
		if (context != null && opData != null)  {
			this.context = context;
			String apppack = new String();
			dao = new SQLiteDatabaseDao();
			apppack = dao.searchData("application", opData);
			
			if (apppack != null) {
				openApp(apppack);
			} else {
				startAPP(opData);
			}
		}
	}

	/**
	 * 启动自定义应用
	 * @param packageName
	 * @return
	 */
	private boolean openApp(String packageName) {
		PackageInfo pi = null;
		try {
			pi = context.getPackageManager().getPackageInfo(packageName, 0);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
		
		Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
		resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		resolveIntent.setPackage(pi.packageName);
		PackageManager pm = context.getPackageManager();
		List<ResolveInfo> apps = pm.queryIntentActivities(resolveIntent, 0);

		ResolveInfo ri = apps.iterator().next();
		if (ri != null) {
			String packName = ri.activityInfo.packageName;
			String className = ri.activityInfo.name;

			Intent intent = new Intent(Intent.ACTION_MAIN);
			intent.addCategory(Intent.CATEGORY_LAUNCHER);
			ComponentName cn = new ComponentName(packName, className);

			intent.setComponent(cn);
			context.startActivity(intent);
		}
		return true;
	}
	
	/**
	 * 打开系统应用 
	 * 
	 * @param appName application's name
	 * @return 
	 */
	private void startAPP(String appName) {
		Intent resolveIntent = new Intent();
		resolveIntent.setAction(Intent.ACTION_MAIN);
		resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		PackageManager pm = context.getPackageManager();
		List<ResolveInfo> list = pm.queryIntentActivities(resolveIntent, 0);

		for (ResolveInfo info : list) {
			if (appName.equalsIgnoreCase(info.activityInfo.loadLabel(pm).toString())) {
				Intent intent = new Intent(Intent.ACTION_MAIN);
				intent.addCategory(Intent.CATEGORY_LAUNCHER);
				intent.setComponent(new ComponentName(info.activityInfo.packageName, info.activityInfo.name));

				context.startActivity(intent);
				return;
			}
		}
		//FIXME: we should return this message to invoker.
		Toast.makeText(context, "没有找打该应用!", Toast.LENGTH_SHORT).show();
	}
	
	/**
	 *  简单的数据库操作类  
	 */
	class SQLiteDatabaseDao {  
		public SQLiteDatabaseDao() {  
			mDb = context.openOrCreateDatabase("users.db", SQLiteDatabase.CREATE_IF_NECESSARY, null);  
			//初始化创建表  
			createTable(mDb, "student");  
			//初始化插入数据  
//			insert(mDb, "student");  
		}  
		
		/**
		 *  创建一个数据库  
		 * @param mDb
		 * @param table
		 */
		public void createTable(SQLiteDatabase mDb, String table) {  
			try {  
				mDb.execSQL("create table if not exists "  
						+ table  
						+ " (id integer primary key autoincrement, "  
						+ "username text not null, birthday text not null);");  
			} catch (SQLException e) {  
					Toast.makeText(context, "数据表创建失败", Toast.LENGTH_LONG).show();  
			}  
		}  
  

		
		/**
		 *  查询数据  
		 * @param table
		 */
		public String searchData(String table, String key) {  
			String addr = new String();
			addr = null;
			Cursor c = mDb.rawQuery("select * from " + table, null);  
			// 获取表的内容  
			while (c.moveToNext()) { 
				if (c.getString(1).matches(key)) {
					addr = c.getString(2);
				}
			} 
			
			return addr;
		}  

	}
}
