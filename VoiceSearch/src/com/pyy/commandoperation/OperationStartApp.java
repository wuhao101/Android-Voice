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
	 * �����Զ���Ӧ��
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
	 * ��ϵͳӦ�� 
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
		Toast.makeText(context, "û���Ҵ��Ӧ��!", Toast.LENGTH_SHORT).show();
	}
	
	/**
	 *  �򵥵����ݿ������  
	 */
	class SQLiteDatabaseDao {  
		public SQLiteDatabaseDao() {  
			mDb = context.openOrCreateDatabase("users.db", SQLiteDatabase.CREATE_IF_NECESSARY, null);  
			//��ʼ��������  
			createTable(mDb, "student");  
			//��ʼ����������  
//			insert(mDb, "student");  
		}  
		
		/**
		 *  ����һ�����ݿ�  
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
					Toast.makeText(context, "���ݱ���ʧ��", Toast.LENGTH_LONG).show();  
			}  
		}  
  

		
		/**
		 *  ��ѯ����  
		 * @param table
		 */
		public String searchData(String table, String key) {  
			String addr = new String();
			addr = null;
			Cursor c = mDb.rawQuery("select * from " + table, null);  
			// ��ȡ�������  
			while (c.moveToNext()) { 
				if (c.getString(1).matches(key)) {
					addr = c.getString(2);
				}
			} 
			
			return addr;
		}  

	}
}
