package com.pyy.commandoperation;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.provider.ContactsContract;
import android.telephony.PhoneNumberUtils;
import android.widget.Toast;

/**
 * 打电话操作实现
 */
public class OperationDialing {
	private Context context;
	
	public void doOperation(Context context, String opData) {
		if (opData != null && context != null) {
			this.context = context;
			if (PhoneNumberUtils.isGlobalPhoneNumber(opData)) { //若识别的是可拨打号码，拨打号码
				callPhoneNumber(opData);
			} else { //否则去通讯录搜索匹配的联系人
				callContact(opData);
			}
		}
	}
	
	/**
	 *  通过 电话号码拨打
	 */
	private void callPhoneNumber(String phoneNumber) {
		Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber));
		context.startActivity(intent);
	}
	
	/**
	 *  通过通讯录联系人拨打
	 */
	private void callContact(String sname) {
		Cursor cursor = null;
		try {
			//提取所有联系人信息，存入游标		
			cursor = context.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
		} catch(SQLiteException e) { }
		
		if (cursor != null && cursor.getCount() > 0) {
			while(cursor.moveToNext()){
				//获取当前联系人姓名
	        	int nameFieldIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);	        	
	        	String name = cursor.getString(nameFieldIndex);
	        	String tname = new String(sname);
	        	
	        	Cursor phonecursor = null; 
	        	
	        	if (name.equals(tname)) { //匹配姓名成功
	        		//获取当前联系人自增键
	        		int idFieldIndex = cursor.getColumnIndex(ContactsContract.Contacts._ID);
	            	int id = cursor.getInt(idFieldIndex);
	            	
	            	//获取当前联系人电话号码
	            	phonecursor = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID+"=?", new String[]{Integer.toString(id)}, null);
	            	phonecursor.moveToFirst();
	            	String phonenumber = phonecursor.getString(phonecursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
	            	if (phonenumber != null) { //拨打该号码
	            		Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phonenumber));
		        		context.startActivity(intent);
	            	}
	        		if (phonecursor != null) phonecursor.close();
	            	break;
	        	}
	        	else
	        		continue; 
			}
	    }
		else {
			Toast.makeText(context, "no contact", Toast.LENGTH_SHORT).show();
		}
	}
	
}