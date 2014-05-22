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
 * ��绰����ʵ��
 */
public class OperationDialing {
	private Context context;
	
	public void doOperation(Context context, String opData) {
		if (opData != null && context != null) {
			this.context = context;
			if (PhoneNumberUtils.isGlobalPhoneNumber(opData)) { //��ʶ����ǿɲ�����룬�������
				callPhoneNumber(opData);
			} else { //����ȥͨѶ¼����ƥ�����ϵ��
				callContact(opData);
			}
		}
	}
	
	/**
	 *  ͨ�� �绰���벦��
	 */
	private void callPhoneNumber(String phoneNumber) {
		Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber));
		context.startActivity(intent);
	}
	
	/**
	 *  ͨ��ͨѶ¼��ϵ�˲���
	 */
	private void callContact(String sname) {
		Cursor cursor = null;
		try {
			//��ȡ������ϵ����Ϣ�������α�		
			cursor = context.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
		} catch(SQLiteException e) { }
		
		if (cursor != null && cursor.getCount() > 0) {
			while(cursor.moveToNext()){
				//��ȡ��ǰ��ϵ������
	        	int nameFieldIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);	        	
	        	String name = cursor.getString(nameFieldIndex);
	        	String tname = new String(sname);
	        	
	        	Cursor phonecursor = null; 
	        	
	        	if (name.equals(tname)) { //ƥ�������ɹ�
	        		//��ȡ��ǰ��ϵ��������
	        		int idFieldIndex = cursor.getColumnIndex(ContactsContract.Contacts._ID);
	            	int id = cursor.getInt(idFieldIndex);
	            	
	            	//��ȡ��ǰ��ϵ�˵绰����
	            	phonecursor = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID+"=?", new String[]{Integer.toString(id)}, null);
	            	phonecursor.moveToFirst();
	            	String phonenumber = phonecursor.getString(phonecursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
	            	if (phonenumber != null) { //����ú���
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