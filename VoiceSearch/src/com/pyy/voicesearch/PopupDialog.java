package com.pyy.voicesearch;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class PopupDialog extends Dialog {

	private Context context;
	private ListView listview;
	private View parentView;
	private int bias;
	private int itemCount;
	
	public PopupDialog(Context context, View parent, int bias, int count) {
		super(context);
		this.context = context;
		this.parentView = parent;
		this.bias = bias;
		this.itemCount = count;

		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setContentView(R.layout.dialog_layout);
		this.setCanceledOnTouchOutside(true);
		
		InitUI();
	}
	
	private void InitUI()
	{
		this.listview = (ListView) findViewById(R.id.setuplist);
		this.listview.setSelector(android.R.color.transparent);
		
		DisplayMetrics dm = new DisplayMetrics();
		dm = context.getApplicationContext().getResources().getDisplayMetrics();
		
		Window w = this.getWindow();
		Drawable drawable = context.getResources().getDrawable(R.drawable.dialog_bg);
		
		WindowManager.LayoutParams wl = w.getAttributes();
		wl.height = (int)(50*itemCount*dm.density + itemCount - 1 + drawable.getIntrinsicHeight()-50*4*dm.density);
		wl.width = drawable.getIntrinsicWidth();
		wl.dimAmount= 0.3f;
		
		Rect r = new Rect();
		parentView.getGlobalVisibleRect(r);

		wl.gravity = Gravity.LEFT|Gravity.TOP;
		wl.x = (int) ((r.right+r.left)/2f-this.bias*dm.density);
		wl.y = (int) (r.bottom-35*dm.density);
		
		w.setAttributes(wl);

		this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
	}
	
	public void setListAdapter(ListAdapter adapter)
	{
		listview.setAdapter(adapter);
	}
	
	public void setListClickListener(OnItemClickListener listener)
	{
		listview.setOnItemClickListener(listener);
	}
	
	public void setBlankClickListener(View.OnClickListener listener) {
		View blank = findViewById(R.id.top_blank);
		blank.setOnClickListener(listener);
	}

	public PopupDialog(Context context, int theme) {
		super(context, theme);
		// TODO Auto-generated constructor stub
	}

	public PopupDialog(Context context, boolean cancelable,
			OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
		// TODO Auto-generated constructor stub
	}

}

