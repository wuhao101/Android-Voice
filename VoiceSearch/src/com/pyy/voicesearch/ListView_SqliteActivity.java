package com.pyy.voicesearch;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class ListView_SqliteActivity extends Activity {
	SQLiteDatabase mDb;
	SQLiteDatabaseDao dao;
	/**
	 * 存储数据的数组列表
	 */
	ArrayList<HashMap<String, Object>> listData;
	/**
	 * 适配器
	 */
	SimpleAdapter listItemAdapter;
	/**
	 * 返回主页面按钮
	 */
	private ImageButton btnBack;
	/**
	 * 添加新项目按钮
	 */
	private ImageButton btnAdd;
	/**
	 * 删除全部项目按钮
	 */
	private ImageButton btnDeletAll;
	/**
	 * 页面标题
	 */
	private TextView title;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.websetting);
		dao = new SQLiteDatabaseDao();

		// 初始化按钮
		btnBack = (ImageButton) findViewById(R.id.btn_back);
		btnBack.setOnClickListener(btnBackOnClickListener);

		btnAdd = (ImageButton) findViewById(R.id.btn_add);
		btnAdd.setOnClickListener(btnAddOnClickListener);

		btnDeletAll = (ImageButton) findViewById(R.id.btn_deleteall);
		btnDeletAll.setOnClickListener(btnDeleteAllOnClickListener);

		// 初始化标题
		title = (TextView) findViewById(R.id.settitle);
		title.setText("Web Setting");

		// 初始化LIST
		ListView list = (ListView) findViewById(R.id.list_items);
		listItemAdapter = new SimpleAdapter(ListView_SqliteActivity.this,
				listData,// 数据源
				R.layout.item,// ListItem的XML实现
				// 动态数组与ImageItem对应的子项
				new String[] { "username", "birthday" },
				// ImageItem的XML文件里面的两个TextView ID
				new int[] { R.id.username, R.id.birthday });
		list.setAdapter(listItemAdapter);
		list.setOnCreateContextMenuListener(listviewLongPress);
		list.setOnItemClickListener(ItemOnclickListener);
	}

	/**
	 * 简单的数据库操作类
	 */
	class SQLiteDatabaseDao {
		public SQLiteDatabaseDao() {
			mDb = openOrCreateDatabase("users.db",
					SQLiteDatabase.CREATE_IF_NECESSARY, null);
			// 初始化创建表
			createTable(mDb, "student");
			// 初始化插入数据
			// insert(mDb, "student");
			// 初始化获取所有数据表数据
			getAllData("student");
		}

		/**
		 * 创建一个数据库
		 * 
		 * @param mDb
		 * @param table
		 */
		public void createTable(SQLiteDatabase mDb, String table) {
			try {
				mDb.execSQL("create table if not exists " + table
						+ " (id integer primary key autoincrement, "
						+ "username text not null, birthday text not null);");
			} catch (SQLException e) {
				Toast.makeText(getApplicationContext(), "Creating Failure!",
						Toast.LENGTH_LONG).show();
			}
		}

		/**
		 * 插入数据
		 * 
		 * @param mDb
		 * @param table
		 */
		// public void insert(SQLiteDatabase mDb, String table) {
		// // 初始化插入3条数据
		// ContentValues values = new ContentValues();
		// values.put("username", "百度");
		// values.put("birthday", "www.baidu.com");
		// mDb.insert(table, null, values);
		//
		// values.put("username", "谷歌");
		// values.put("birthday", "www.google.cn");
		// mDb.insert(table, null, values);
		// }

		/**
		 * 插入数据
		 * 
		 * @param mDb
		 * @param table
		 * @param s1
		 * @param s2
		 */
		public boolean insert(SQLiteDatabase mDb, String table, String s1,
				String s2) {
			// 初始化插入3条数据
			ContentValues values = new ContentValues();
			values.put("username", s1);
			values.put("birthday", s2);
			try {
				mDb.insert(table, null, values);
			} catch (SQLException e) {
				Toast.makeText(getApplicationContext(), "Adding Failure!",
						Toast.LENGTH_LONG).show();
				return false;
			}
			return true;
		}

		/**
		 * 更新数据
		 * 
		 * @param mDb
		 * @param table
		 */
		public boolean update(SQLiteDatabase mDb, String table, int id,
				String name, String addr) {
			ContentValues values = new ContentValues();

			String where = "id=?";
			String[] strwhere = { Integer.toString(id) };

			values.put("username", name);
			values.put("birthday", addr);

			try {
				mDb.update(table, values, where, strwhere);
			} catch (SQLException e) {
				Toast.makeText(getApplicationContext(), "Updating Failure!",
						Toast.LENGTH_LONG).show();
				return false;
			}
			return true;
		}

		/**
		 * 查询所有数据
		 * 
		 * @param table
		 */
		public void getAllData(String table) {
			Cursor c = mDb.rawQuery("select * from " + table, null);
			int columnsSize = c.getColumnCount();
			listData = new ArrayList<HashMap<String, Object>>();
			// 获取表的内容
			while (c.moveToNext()) {
				HashMap<String, Object> map = new HashMap<String, Object>();
				for (int i = 0; i < columnsSize; i++) {
					map.put("id", c.getString(0));
					map.put("username", c.getString(1));
					map.put("birthday", c.getString(2));
				}
				listData.add(map);
			}
		}

		/**
		 * 删除一条数据
		 * 
		 * @param mDb
		 * @param table
		 * @param id
		 * @return
		 */
		public boolean delete(SQLiteDatabase mDb, String table, int id) {
			String whereClause = "id=?";
			String[] whereArgs = new String[] { String.valueOf(id) };
			try {
				mDb.delete(table, whereClause, whereArgs);
			} catch (SQLException e) {
				Toast.makeText(getApplicationContext(), "Deleting Failure!",
						Toast.LENGTH_LONG).show();
				return false;
			}
			return true;
		}

		/**
		 * 删除全部数据
		 * 
		 * @param mDb
		 * @param table
		 * @param id
		 * @return
		 */
		public boolean deleteall(SQLiteDatabase mDb, String table) {
			try {
				mDb.delete(table, null, null);
			} catch (SQLException e) {
				Toast.makeText(getApplicationContext(), "Deleting Failure!",
						Toast.LENGTH_LONG).show();
				return false;
			}
			return true;
		}

		/**
		 * 检索命名是否重复
		 * 
		 * @param mDb
		 * @param table
		 * @param id
		 * @return
		 */
		public boolean check(SQLiteDatabase mDb, String table, String name) {
			Cursor c = mDb.rawQuery("select * from " + table, null);
			// 获取表的内容
			while (c.moveToNext()) {
				if (name.matches(c.getString(1))) {
					Toast.makeText(getApplicationContext(),
							"This name has existed. Please input another one!",
							Toast.LENGTH_LONG).show();
					return false;
				}
			}
			return true;
		}
	}

	/**
	 * 判断是否为中文
	 * 
	 * @param c
	 * @return
	 */
	// private final static boolean isChinese(char c) {
	// Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
	// if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
	// || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
	// || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A) {
	// return true;
	// }
	// return false;
	// }
	//
	// public static final boolean isChinese(String strName) {
	// char[] ch = strName.toCharArray();
	// for (int i = 0; i < ch.length; i++) {
	// char c = ch[i];
	// if (!isChinese(c)) {
	// return false;
	// }
	//
	// }
	// return true;
	// }

	/**
	 * 判断是否为数字串
	 * 
	 * @param strName
	 * @return
	 */
	public static final boolean isNumberic(String strName) {
		char[] ch = strName.toCharArray();
		for (int i = 0; i < ch.length; i++) {
			char c = ch[i];
			if (c > '9' || c < '0') {
				return false;
			}

		}
		return true;
	}

	/**
	 * 长按事件响应 ，删除对应项
	 */
	OnCreateContextMenuListener listviewLongPress = new OnCreateContextMenuListener() {
		public void onCreateContextMenu(ContextMenu menu, View v,
				ContextMenuInfo menuInfo) {
			// TODO Auto-generated method stub
			final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
			new AlertDialog.Builder(ListView_SqliteActivity.this)
					// 弹出窗口的最上头文字
					.setTitle("Delete")
					// 设置弹出窗口的图式
					.setIcon(android.R.drawable.ic_dialog_info)
					// 设置弹出窗口的信息
					.setMessage("Confirm to delete")
					.setPositiveButton("Delete",
							new DialogInterface.OnClickListener() {
								public void onClick(
										DialogInterface dialoginterface, int i) {
									// 获取位置索引
									int mListPos = info.position;
									// 获取对应HashMap数据内容
									HashMap<String, Object> map = listData
											.get(mListPos);
									// 获取id
									int id = Integer.valueOf((map.get("id")
											.toString()));
									// 获取数组具体值后,可以对数据进行相关的操作,例如更新数据
									if (dao.delete(mDb, "student", id)) {
										// 移除listData的数据
										listData.remove(mListPos);
										listItemAdapter.notifyDataSetChanged();
									}
								}
							})
					.setNegativeButton("Cancel",
							new DialogInterface.OnClickListener() {
								public void onClick(
										DialogInterface dialoginterface, int i) {
									// 什么也没做
								}
							}).show();
		}
	};

	/**
	 * 返回按钮点击事件
	 */
	View.OnClickListener btnBackOnClickListener = new OnClickListener() {

		public void onClick(View v) {
			// TODO Auto-generated method stub
			finish();
			mDb.close();
		}
	};

	/**
	 * 添加按钮点击事件
	 */
	View.OnClickListener btnAddOnClickListener = new OnClickListener() {

		public void onClick(View v) {
			// TODO Auto-generated method stub
			/**
			 * 带编辑框Dialog样式
			 */
			LayoutInflater inflater = (LayoutInflater) ListView_SqliteActivity.this
					.getSystemService(LAYOUT_INFLATER_SERVICE);
			final View view = inflater.inflate(R.layout.dialog, null);

			// HashMap
			final HashMap<String, Object> map = new HashMap<String, Object>();

			// 获取用户名文本框组件
			final EditText nameEditText = (EditText) view
					.findViewById(R.id.editText1);

			// 获取密码文本框组件
			final EditText passEditText = (EditText) view
					.findViewById(R.id.editText2);

			final Builder dialog = new AlertDialog.Builder(
					ListView_SqliteActivity.this);
			/* 弹出窗口的最上头文字 */
			dialog.setTitle("Add New Web")
					/* 设置弹出窗口的图式 */
					.setIcon(android.R.drawable.ic_dialog_info)
					/* 设置弹出窗口的信息 */
					.setView(view)
					.setPositiveButton("Save",
							new DialogInterface.OnClickListener() {
								public void onClick(
										DialogInterface dialoginterface, int i) {
									// 获取用户名文本框中输入的内容
									String username = nameEditText.getText()
											.toString();

									// 获取密码文本框中的内容*
									String userpass = passEditText.getText()
											.toString();

									if (username.matches("")
											|| userpass.matches("")) {
										Toast.makeText(getApplicationContext(),
												"Some item is empty!",
												Toast.LENGTH_LONG).show();
										// 不关闭对话框
										try {
											Field field = dialoginterface
													.getClass()
													.getSuperclass()
													.getDeclaredField(
															"mShowing");
											field.setAccessible(true);
											field.set(dialoginterface, false);
										} catch (Exception e) {
											e.printStackTrace();
										}
									} else if (!isLetter(username)
											&& !isNumberic(username)) {
										Toast.makeText(
												getApplicationContext(),
												"Please input alphabet or digit!",
												Toast.LENGTH_LONG).show();
										// 不关闭对话框
										try {
											Field field = dialoginterface
													.getClass()
													.getSuperclass()
													.getDeclaredField(
															"mShowing");
											field.setAccessible(true);
											field.set(dialoginterface, false);
										} catch (Exception e) {
											e.printStackTrace();
										}
									} else {

										// 获取数组具体值后,可以对数据进行相关的操作,例如更新数据
										if (dao.check(mDb, "student", username)) {
											// 添加listData的数据
											if (dao.insert(mDb, "student",
													username, userpass)) {
												map.put("username", username);
												map.put("birthday", userpass);
												listData.add(map);
												listItemAdapter
														.notifyDataSetChanged();
											}

											// 关闭对话框
											try {
												Field field = dialoginterface
														.getClass()
														.getSuperclass()
														.getDeclaredField(
																"mShowing");
												field.setAccessible(true);
												field.set(dialoginterface, true);
											} catch (Exception e) {
												e.printStackTrace();
											}
										} else {
											// 不关闭对话框
											try {
												Field field = dialoginterface
														.getClass()
														.getSuperclass()
														.getDeclaredField(
																"mShowing");
												field.setAccessible(true);
												field.set(dialoginterface,
														false);
											} catch (Exception e) {
												e.printStackTrace();
											}
										}
									}

								}
							})
					.setNegativeButton("Cancel",
							new DialogInterface.OnClickListener() {
								public void onClick(
										DialogInterface dialoginterface, int i) {
									// 关闭对话框
									try {
										Field field = dialoginterface
												.getClass().getSuperclass()
												.getDeclaredField("mShowing");
										field.setAccessible(true);
										field.set(dialoginterface, true);
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
							});

			dialog.show();
		}
	};

	/**
	 * 添加删除全部项目点击事件
	 */
	View.OnClickListener btnDeleteAllOnClickListener = new OnClickListener() {

		public void onClick(View v) {
			// TODO Auto-generated method stub

			final Builder dialog = new AlertDialog.Builder(
					ListView_SqliteActivity.this);
			/* 弹出窗口的最上头文字 */
			dialog.setTitle("Delete All Items")
					/* 设置弹出窗口的图式 */
					.setIcon(android.R.drawable.ic_dialog_info)
					/* 设置弹出窗口的信息 */
					.setMessage("Confirm to delete")
					.setPositiveButton("Delete",
							new DialogInterface.OnClickListener() {
								public void onClick(
										DialogInterface dialoginterface, int i) {

									dao.deleteall(mDb, "student");
									listData.removeAll(listData);
									listItemAdapter.notifyDataSetChanged();
								}
							})
					.setNegativeButton("Cancel",
							new DialogInterface.OnClickListener() {
								public void onClick(
										DialogInterface dialoginterface, int i) {
									// 什么也没做

								}
							});

			dialog.show();
		}
	};

	/**
	 * ListItem点击事件
	 */
	AdapterView.OnItemClickListener ItemOnclickListener = new AdapterView.OnItemClickListener() {

		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub

			/**
			 * 带编辑框Dialog样式
			 */
			LayoutInflater inflater = (LayoutInflater) ListView_SqliteActivity.this
					.getSystemService(LAYOUT_INFLATER_SERVICE);
			final View view = inflater.inflate(R.layout.dialog, null);

			final int position = arg2;

			// 获取位置索引
			int mListPos = position;
			// 获取对应HashMap数据内容
			final HashMap<String, Object> map = listData.get(mListPos);
			// 获取id
			final int id = Integer.valueOf((map.get("id").toString()));

			// 获取用户名文本框组件
			final EditText nameEditText = (EditText) view
					.findViewById(R.id.editText1);
			nameEditText.setText(map.get("username").toString());

			// 获取密码文本框组件
			final EditText passEditText = (EditText) view
					.findViewById(R.id.editText2);
			passEditText.setText(map.get("birthday").toString());

			new AlertDialog.Builder(ListView_SqliteActivity.this)
					/* 弹出窗口的最上头文字 */
					.setTitle("Edit")
					/* 设置弹出窗口的图式 */
					.setIcon(android.R.drawable.ic_dialog_info)
					/* 设置弹出窗口的信息 */
					.setView(view)
					.setPositiveButton("Save",
							new DialogInterface.OnClickListener() {
								public void onClick(
										DialogInterface dialoginterface, int i) {
									// 获取用户名文本框中输入的内容
									String username = nameEditText.getText()
											.toString();

									// 获取密码文本框中的内容*
									String userpass = passEditText.getText()
											.toString();

									if (username == null || userpass == null) {
										Toast.makeText(getApplicationContext(),
												"Some item is empty!",
												Toast.LENGTH_LONG).show();
										// 不关闭对话框
										try {
											Field field = dialoginterface
													.getClass()
													.getSuperclass()
													.getDeclaredField(
															"mShowing");
											field.setAccessible(true);
											field.set(dialoginterface, false);
										} catch (Exception e) {
											e.printStackTrace();
										}
									} else if (!isLetter(username)
											&& !isNumberic(username)) {
										Toast.makeText(
												getApplicationContext(),
												"Please input alphabet or digit!",
												Toast.LENGTH_LONG).show();
										// 不关闭对话框
										try {
											Field field = dialoginterface
													.getClass()
													.getSuperclass()
													.getDeclaredField(
															"mShowing");
											field.setAccessible(true);
											field.set(dialoginterface, false);
										} catch (Exception e) {
											e.printStackTrace();
										}
									} else {
										// 获取数组具体值后,可以对数据进行相关的操作,例如更新数据
										if (dao.check(mDb, "student", username)) {
											if (dao.update(mDb, "student", id,
													username, userpass)) {
												// 修改listData的数据
												map.put("username", username);
												map.put("birthday", userpass);
												listItemAdapter
														.notifyDataSetChanged();
											}
											// 关闭对话框
											try {
												Field field = dialoginterface
														.getClass()
														.getSuperclass()
														.getDeclaredField(
																"mShowing");
												field.setAccessible(true);
												field.set(dialoginterface, true);
											} catch (Exception e) {
												e.printStackTrace();
											}
										} else {
											// 不关闭对话框
											try {
												Field field = dialoginterface
														.getClass()
														.getSuperclass()
														.getDeclaredField(
																"mShowing");
												field.setAccessible(true);
												field.set(dialoginterface,
														false);
											} catch (Exception e) {
												e.printStackTrace();
											}
										}
									}
								}
							})
					.setNegativeButton("Cancel",
							new DialogInterface.OnClickListener() {
								public void onClick(
										DialogInterface dialoginterface, int i) {
									// 关闭对话框
									try {
										Field field = dialoginterface
												.getClass().getSuperclass()
												.getDeclaredField("mShowing");
										field.setAccessible(true);
										field.set(dialoginterface, true);
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
							}).show();

		}
	};

	private final static boolean isLetter(char c) {
		if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || c == ' '
				|| c == '.' || c == '_' || c == '-' || c == '/' || c == '\\'
				|| (c >= '0' && c <= '9') || c == '\'' || c == '\"' || c == '+'
				|| c == '=' || c == '~' || c == '%' || c == '#' || c == '$'
				|| c == '&' || c == '*' || c == '@' || c == '^' || c == '('
				|| c == ')' || c == '[' || c == ']' || c == '{' || c == '}'
				|| c == '|' || c == ';' || c == ':' || c == '?' || c == '<'
				|| c == '>') {
			return true;
		}
		return false;
	}

	public static final boolean isLetter(String strName) {
		char[] ch = strName.toCharArray();
		for (int i = 0; i < ch.length; i++) {
			char c = ch[i];
			if (!isLetter(c)) {
				return false;
			}

		}
		return true;
	}
}
