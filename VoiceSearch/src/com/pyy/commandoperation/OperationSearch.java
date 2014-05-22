package com.pyy.commandoperation;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * ËÑË÷²Ù×÷
 */
public class OperationSearch {
	private Context myContext;  
	
	public void doOperation(Context context , String data) {
		myContext = context;
		beginSearch(data);
	}
	
	private boolean beginSearch(String key) {
		if (key != null) {			
			Intent i = new Intent(Intent.ACTION_VIEW);
			// ³õÊ¼»¯ÍøÖ·
			String url = new String("http://www.google.com.hk/search?hl=zh-CN&source=hp&q="+key+"&meta=&aq=f&aqi=g10&aql=&oq=&gs_rfai=");			
			i.setData(Uri.parse(url));
			myContext.startActivity(i);
			return true;
		}
		return false;
	}
}
