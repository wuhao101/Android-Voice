package com.pyy.commandoperation;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.widget.Toast;


public class OperationConnect {
	
	SQLiteDatabase mDb;  
	SQLiteDatabaseDao dao;  
	
	private String HTTPPROTO = "http://"; //proto name
	private Context myContext = null;     //current activity context
	
	public void doOperation(Context context , String data) {
		myContext = context;
		dao = new SQLiteDatabaseDao(); 
		
		String tempdata = new String();
		tempdata = dao.searchData("student", data);
		
		if (tempdata == null) {
			Toast.makeText(myContext, "该网页未存储", Toast.LENGTH_LONG).show();  
		} else {
			String urlFromString = getUrlFromBaseData(tempdata);

			String url="";
			
			url = urlFromString;

			openUrl(url);
		}		
	}


	private String getUrlByName(String name) {
		//To find if the name exist in the keyTable,if exist , get the url address
		SharedPreferences keyTable = myContext.getSharedPreferences("keyTable", 0);
		return keyTable.getString(name, null);
	}

	private String getUrlFromBaseData(String data) {
		
		//To make up a url from Base argruments
		data = data.replace(" ", ".");
		String formatUrlString = HTTPPROTO;
		if (!data.startsWith(HTTPPROTO)) {
			formatUrlString += data;
		} else {
			formatUrlString = data;
		}
		String url = getUrlByName(data);
		if (url != null) {
			formatUrlString = "";
			if (!url.startsWith(HTTPPROTO))
				formatUrlString = HTTPPROTO;
			formatUrlString += url;
		}
		return formatUrlString;
	}

	private boolean openUrl(String url) {
		//To Start webClient to open a url
		if (url != null) {
			Intent i = new Intent(Intent.ACTION_VIEW);
			i.setData(Uri.parse(url));
			myContext.startActivity(i);
			return true;
		}
		return false;
	}
	
	/**
	 *  简单的数据库操作类  
	 */
	class SQLiteDatabaseDao {  
		public SQLiteDatabaseDao() {  
			mDb = myContext.openOrCreateDatabase("users.db", SQLiteDatabase.CREATE_IF_NECESSARY, null);  
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
					Toast.makeText(myContext, "数据表创建失败", Toast.LENGTH_LONG).show();  
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

