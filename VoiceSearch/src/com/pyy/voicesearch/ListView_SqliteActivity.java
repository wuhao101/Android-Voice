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
	 * �洢���ݵ������б�
	 */
	ArrayList<HashMap<String, Object>> listData;
	/**
	 * ������
	 */
	SimpleAdapter listItemAdapter;
	/**
	 * ������ҳ�水ť
	 */
	private ImageButton btnBack;
	/**
	 * �������Ŀ��ť
	 */
	private ImageButton btnAdd;
	/**
	 * ɾ��ȫ����Ŀ��ť
	 */
	private ImageButton btnDeletAll;
	/**
	 * ҳ�����
	 */
	private TextView title;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.websetting);
		dao = new SQLiteDatabaseDao();

		// ��ʼ����ť
		btnBack = (ImageButton) findViewById(R.id.btn_back);
		btnBack.setOnClickListener(btnBackOnClickListener);

		btnAdd = (ImageButton) findViewById(R.id.btn_add);
		btnAdd.setOnClickListener(btnAddOnClickListener);

		btnDeletAll = (ImageButton) findViewById(R.id.btn_deleteall);
		btnDeletAll.setOnClickListener(btnDeleteAllOnClickListener);

		// ��ʼ������
		title = (TextView) findViewById(R.id.settitle);
		title.setText("Web Setting");

		// ��ʼ��LIST
		ListView list = (ListView) findViewById(R.id.list_items);
		listItemAdapter = new SimpleAdapter(ListView_SqliteActivity.this,
				listData,// ����Դ
				R.layout.item,// ListItem��XMLʵ��
				// ��̬������ImageItem��Ӧ������
				new String[] { "username", "birthday" },
				// ImageItem��XML�ļ����������TextView ID
				new int[] { R.id.username, R.id.birthday });
		list.setAdapter(listItemAdapter);
		list.setOnCreateContextMenuListener(listviewLongPress);
		list.setOnItemClickListener(ItemOnclickListener);
	}

	/**
	 * �򵥵����ݿ������
	 */
	class SQLiteDatabaseDao {
		public SQLiteDatabaseDao() {
			mDb = openOrCreateDatabase("users.db",
					SQLiteDatabase.CREATE_IF_NECESSARY, null);
			// ��ʼ��������
			createTable(mDb, "student");
			// ��ʼ����������
			// insert(mDb, "student");
			// ��ʼ����ȡ�������ݱ�����
			getAllData("student");
		}

		/**
		 * ����һ�����ݿ�
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
		 * ��������
		 * 
		 * @param mDb
		 * @param table
		 */
		// public void insert(SQLiteDatabase mDb, String table) {
		// // ��ʼ������3������
		// ContentValues values = new ContentValues();
		// values.put("username", "�ٶ�");
		// values.put("birthday", "www.baidu.com");
		// mDb.insert(table, null, values);
		//
		// values.put("username", "�ȸ�");
		// values.put("birthday", "www.google.cn");
		// mDb.insert(table, null, values);
		// }

		/**
		 * ��������
		 * 
		 * @param mDb
		 * @param table
		 * @param s1
		 * @param s2
		 */
		public boolean insert(SQLiteDatabase mDb, String table, String s1,
				String s2) {
			// ��ʼ������3������
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
		 * ��������
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
		 * ��ѯ��������
		 * 
		 * @param table
		 */
		public void getAllData(String table) {
			Cursor c = mDb.rawQuery("select * from " + table, null);
			int columnsSize = c.getColumnCount();
			listData = new ArrayList<HashMap<String, Object>>();
			// ��ȡ�������
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
		 * ɾ��һ������
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
		 * ɾ��ȫ������
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
		 * ���������Ƿ��ظ�
		 * 
		 * @param mDb
		 * @param table
		 * @param id
		 * @return
		 */
		public boolean check(SQLiteDatabase mDb, String table, String name) {
			Cursor c = mDb.rawQuery("select * from " + table, null);
			// ��ȡ�������
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
	 * �ж��Ƿ�Ϊ����
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
	 * �ж��Ƿ�Ϊ���ִ�
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
	 * �����¼���Ӧ ��ɾ����Ӧ��
	 */
	OnCreateContextMenuListener listviewLongPress = new OnCreateContextMenuListener() {
		public void onCreateContextMenu(ContextMenu menu, View v,
				ContextMenuInfo menuInfo) {
			// TODO Auto-generated method stub
			final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
			new AlertDialog.Builder(ListView_SqliteActivity.this)
					// �������ڵ�����ͷ����
					.setTitle("Delete")
					// ���õ������ڵ�ͼʽ
					.setIcon(android.R.drawable.ic_dialog_info)
					// ���õ������ڵ���Ϣ
					.setMessage("Confirm to delete")
					.setPositiveButton("Delete",
							new DialogInterface.OnClickListener() {
								public void onClick(
										DialogInterface dialoginterface, int i) {
									// ��ȡλ������
									int mListPos = info.position;
									// ��ȡ��ӦHashMap��������
									HashMap<String, Object> map = listData
											.get(mListPos);
									// ��ȡid
									int id = Integer.valueOf((map.get("id")
											.toString()));
									// ��ȡ�������ֵ��,���Զ����ݽ�����صĲ���,�����������
									if (dao.delete(mDb, "student", id)) {
										// �Ƴ�listData������
										listData.remove(mListPos);
										listItemAdapter.notifyDataSetChanged();
									}
								}
							})
					.setNegativeButton("Cancel",
							new DialogInterface.OnClickListener() {
								public void onClick(
										DialogInterface dialoginterface, int i) {
									// ʲôҲû��
								}
							}).show();
		}
	};

	/**
	 * ���ذ�ť����¼�
	 */
	View.OnClickListener btnBackOnClickListener = new OnClickListener() {

		public void onClick(View v) {
			// TODO Auto-generated method stub
			finish();
			mDb.close();
		}
	};

	/**
	 * ��Ӱ�ť����¼�
	 */
	View.OnClickListener btnAddOnClickListener = new OnClickListener() {

		public void onClick(View v) {
			// TODO Auto-generated method stub
			/**
			 * ���༭��Dialog��ʽ
			 */
			LayoutInflater inflater = (LayoutInflater) ListView_SqliteActivity.this
					.getSystemService(LAYOUT_INFLATER_SERVICE);
			final View view = inflater.inflate(R.layout.dialog, null);

			// HashMap
			final HashMap<String, Object> map = new HashMap<String, Object>();

			// ��ȡ�û����ı������
			final EditText nameEditText = (EditText) view
					.findViewById(R.id.editText1);

			// ��ȡ�����ı������
			final EditText passEditText = (EditText) view
					.findViewById(R.id.editText2);

			final Builder dialog = new AlertDialog.Builder(
					ListView_SqliteActivity.this);
			/* �������ڵ�����ͷ���� */
			dialog.setTitle("Add New Web")
					/* ���õ������ڵ�ͼʽ */
					.setIcon(android.R.drawable.ic_dialog_info)
					/* ���õ������ڵ���Ϣ */
					.setView(view)
					.setPositiveButton("Save",
							new DialogInterface.OnClickListener() {
								public void onClick(
										DialogInterface dialoginterface, int i) {
									// ��ȡ�û����ı��������������
									String username = nameEditText.getText()
											.toString();

									// ��ȡ�����ı����е�����*
									String userpass = passEditText.getText()
											.toString();

									if (username.matches("")
											|| userpass.matches("")) {
										Toast.makeText(getApplicationContext(),
												"Some item is empty!",
												Toast.LENGTH_LONG).show();
										// ���رնԻ���
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
										// ���رնԻ���
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

										// ��ȡ�������ֵ��,���Զ����ݽ�����صĲ���,�����������
										if (dao.check(mDb, "student", username)) {
											// ���listData������
											if (dao.insert(mDb, "student",
													username, userpass)) {
												map.put("username", username);
												map.put("birthday", userpass);
												listData.add(map);
												listItemAdapter
														.notifyDataSetChanged();
											}

											// �رնԻ���
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
											// ���رնԻ���
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
									// �رնԻ���
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
	 * ���ɾ��ȫ����Ŀ����¼�
	 */
	View.OnClickListener btnDeleteAllOnClickListener = new OnClickListener() {

		public void onClick(View v) {
			// TODO Auto-generated method stub

			final Builder dialog = new AlertDialog.Builder(
					ListView_SqliteActivity.this);
			/* �������ڵ�����ͷ���� */
			dialog.setTitle("Delete All Items")
					/* ���õ������ڵ�ͼʽ */
					.setIcon(android.R.drawable.ic_dialog_info)
					/* ���õ������ڵ���Ϣ */
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
									// ʲôҲû��

								}
							});

			dialog.show();
		}
	};

	/**
	 * ListItem����¼�
	 */
	AdapterView.OnItemClickListener ItemOnclickListener = new AdapterView.OnItemClickListener() {

		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub

			/**
			 * ���༭��Dialog��ʽ
			 */
			LayoutInflater inflater = (LayoutInflater) ListView_SqliteActivity.this
					.getSystemService(LAYOUT_INFLATER_SERVICE);
			final View view = inflater.inflate(R.layout.dialog, null);

			final int position = arg2;

			// ��ȡλ������
			int mListPos = position;
			// ��ȡ��ӦHashMap��������
			final HashMap<String, Object> map = listData.get(mListPos);
			// ��ȡid
			final int id = Integer.valueOf((map.get("id").toString()));

			// ��ȡ�û����ı������
			final EditText nameEditText = (EditText) view
					.findViewById(R.id.editText1);
			nameEditText.setText(map.get("username").toString());

			// ��ȡ�����ı������
			final EditText passEditText = (EditText) view
					.findViewById(R.id.editText2);
			passEditText.setText(map.get("birthday").toString());

			new AlertDialog.Builder(ListView_SqliteActivity.this)
					/* �������ڵ�����ͷ���� */
					.setTitle("Edit")
					/* ���õ������ڵ�ͼʽ */
					.setIcon(android.R.drawable.ic_dialog_info)
					/* ���õ������ڵ���Ϣ */
					.setView(view)
					.setPositiveButton("Save",
							new DialogInterface.OnClickListener() {
								public void onClick(
										DialogInterface dialoginterface, int i) {
									// ��ȡ�û����ı��������������
									String username = nameEditText.getText()
											.toString();

									// ��ȡ�����ı����е�����*
									String userpass = passEditText.getText()
											.toString();

									if (username == null || userpass == null) {
										Toast.makeText(getApplicationContext(),
												"Some item is empty!",
												Toast.LENGTH_LONG).show();
										// ���رնԻ���
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
										// ���رնԻ���
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
										// ��ȡ�������ֵ��,���Զ����ݽ�����صĲ���,�����������
										if (dao.check(mDb, "student", username)) {
											if (dao.update(mDb, "student", id,
													username, userpass)) {
												// �޸�listData������
												map.put("username", username);
												map.put("birthday", userpass);
												listItemAdapter
														.notifyDataSetChanged();
											}
											// �رնԻ���
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
											// ���رնԻ���
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
									// �رնԻ���
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
