package com.pyy.voicesearch;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.pyy.commandoperation.OperationConnect;
import com.pyy.commandoperation.OperationDialing;
import com.pyy.commandoperation.OperationSearch;
import com.pyy.commandoperation.OperationStartApp;
import com.pyy.commandoperation.OperationType;
import com.pyy.commandoperation.SpeechCommandResult;
import com.pyy.commandoperation.SpeechEngineTool;
import com.pyy.sentencesegment.TrieTree;
import com.pyy.smilarity.WordSimilarity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public  class VoiceSearchActivity extends Activity implements OnClickListener {
	/**
	 * 语音识别启动按钮
	 */
    private Button btn;
    /**
     * 显示识别出来的语句
     */
//    private ListView listview; 
    
    private static final int VOICE_RECOGNITION_REQUEST_CODE = 1234;
    /**
     * Trie树
     */
    public TrieTree tt = new TrieTree(); 
    /**
     * Trie树输出结果
     */
    public static ArrayList<String> out = new ArrayList<String>(); 
    /**
     * 词语相似度匹配
     */
    public WordSimilarity w;
    /**
     * 设置按钮
     */
    private ImageButton btnMenu;
    /**
     * 关闭按钮
     */
    private ImageButton btnClose;
    
    public static ArrayList<String> segout = new ArrayList<String>();
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        btn=(Button)this.findViewById(R.id.btn);
//        listview=(ListView)this.findViewById(R.id.listview);
        
        btnMenu = (ImageButton)findViewById(R.id.btn_setting);
        btnMenu.setOnClickListener(btnMenuOnclickListener);
        
        btnClose = (ImageButton)findViewById(R.id.btn_cancel);
        btnClose.setOnClickListener(btnCloseOnClickListener);
        
//        initiate(); //初始化载入字典
        AssetManager am0 = getResources().getAssets();
        AssetManager am1 = getResources().getAssets();
        AssetManager am2 = getResources().getAssets();
        try {
    		/*
    		 * 初始化分词的TIRE树
    		 */
			InputStream is = am0.open("dic.txt");
			BufferedReader br = new BufferedReader(new InputStreamReader(is,"GBK"));
			String text;
			while((text=br.readLine())!=null){
				String[]key=text.split(",");
				tt.insertTrieTree(key[0]);
			}  
        	br.close();
        	
			InputStream is1 = am1.open("glossary.txt");
			InputStream is2 = am2.open("WHOLE.txt");
			BufferedReader br1 = new BufferedReader(new InputStreamReader(is1,"GBK"));
			BufferedReader br2 = new BufferedReader(new InputStreamReader(is2,"GBK"));
			WordSimilarity w = new WordSimilarity(br2, br1);
			
			br1.close();
			br2.close();
            
//			String word1 = "访问";
//			String word2 = "访问";
//			double sim = w.simWord(word1, word2);
//            Toast.makeText(this, Double.toString(sim), Toast.LENGTH_SHORT).show();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
//        
        /*
         * 检测语音识别设备是否可用
         */
        PackageManager pm =getPackageManager();
        List<ResolveInfo> list = pm.queryIntentActivities(new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
        if(list.size()!=0){
        	btn.setOnClickListener(this);

        }else{
        	btn.setEnabled(false);
        }
     }
    
    
    protected void startVoiceRecognitionActivity()  
    {  
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);  
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);  
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speech recognition");  
        startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE);  
    } 
    
    
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v.getId()==R.id.btn)
		{
			// 启动语音识别模式
			startVoiceRecognitionActivity();
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == VOICE_RECOGNITION_REQUEST_CODE && resultCode == RESULT_OK)  
        { 
			List<String> list = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
//			listview.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,list));
			
            
            /*
             * 去识别第一条内容进行分析
             */
            SpeechCommandResult spCommand;
            spCommand = SpeechEngineTool.analysisSystemCommand(w, tt, list.get(0));
         
            /*
             * 根据分析得到的指令，转入相应操作
             */
            getVoiceCommand(spCommand);
            

        }  
  
        super.onActivityResult(requestCode, resultCode, data);  
	}
	
	public void getVoiceCommand(SpeechCommandResult spCommand) 
	{
		if (spCommand != null) 
		{
			Log.v("TAG", spCommand.getOpType().toString() + ":"	+ spCommand.getOpData());

			switch (spCommand.getOpType()) 
			{
			case Dialing: //拨打电话功能
				OperationDialing oDialing = new OperationDialing();
				oDialing.doOperation(VoiceSearchActivity.this, spCommand.getOpData());
				break;
			case SearchGoogle: //搜索功能
				OperationSearch oSearch = new OperationSearch();
				oSearch.doOperation(VoiceSearchActivity.this, spCommand.getOpData());
				break;
			case StartApp: //启动应用功能
				OperationStartApp oStartApp = new OperationStartApp();
				oStartApp.doOperation(VoiceSearchActivity.this, spCommand.getOpData());
				break;
			case GotoWeb: //访问网页功能
				OperationConnect oConnect = new OperationConnect();
				oConnect.doOperation(VoiceSearchActivity.this, spCommand.getOpData());
				break;
			default:
				Toast.makeText(this, "无该操作", Toast.LENGTH_SHORT).show();
				break;
			}
		}
	}
	
	View.OnClickListener btnCloseOnClickListener = new OnClickListener() {
		
		public void onClick(View v) {
			// TODO Auto-generated method stub
			final Builder dialog = new AlertDialog.Builder(VoiceSearchActivity.this); 
			/* 弹出窗口的最上头文字 */  
            dialog.setTitle("提示")  
			/* 设置弹出窗口的图式 */  
			.setIcon(android.R.drawable.ic_dialog_info)  
			/* 设置弹出窗口的信息 */ 
			.setMessage("确定要退出语音识别吗？")
			.setPositiveButton("确定",  new DialogInterface.OnClickListener() {  
				public void onClick(DialogInterface dialoginterface, int i) {              
	                
					finish();  
				}  
			})  
			.setNegativeButton("取消", new DialogInterface.OnClickListener() {  
				public void onClick(DialogInterface dialoginterface, int i) {
					//什么也没做
					
				}  
			});
            
			dialog.show(); 
			
		}
	};
	
	View.OnClickListener btnMenuOnclickListener = new OnClickListener() {
		
		public void onClick(View v) {
			// TODO Auto-generated method stub
			String[] strings;
			strings = VoiceSearchActivity.this.getResources().getStringArray(R.array.index_menu_item);
			final PopupDialog dialog;
			dialog = new PopupDialog(VoiceSearchActivity.this, v, 170, strings.length);
			
			dialog.setListAdapter(new DialogSetupListAdapter(VoiceSearchActivity.this, strings));
			dialog.setListClickListener(new OnItemClickListener() {

				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					
					switch(position){
					case 0://设置网页
						Intent intent = new Intent();
						intent.setClass(VoiceSearchActivity.this, ListView_SqliteActivity.class);
						startActivity(intent);
						break;
					case 1://设置应用
						Intent intent1 = new Intent();
						intent1.setClass(VoiceSearchActivity.this, AppSetting.class);
						startActivity(intent1);
						break;
					}
					//System.out.println("Item" + position + " press");
					dialog.dismiss();
				}
			});
			dialog.setBlankClickListener(new OnClickListener() {
				public void onClick(View v) {
					dialog.dismiss();
				}
			});
			dialog.show();
		}
	};
	
	class DialogSetupListAdapter extends BaseAdapter {
		Context context;
		String[] strings;
		LayoutInflater inflater;
		
		public DialogSetupListAdapter(Context context, String[] strings) {
			this.context = context;
			this.strings = strings;
			this.inflater = LayoutInflater.from(context);
		}
		
		public int getCount() {
			return strings.length;
		}

		public Object getItem(int position) {
			return strings[position];
		}

		public long getItemId(int position) {
			return 0;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			RelativeLayout view;
			if (convertView == null) {
				view = (RelativeLayout) inflater.inflate(R.layout.dialog_item, null);
			} else {
				view = (RelativeLayout) convertView;
			}

			TextView setupText =  (TextView) view.findViewById(R.id.setting_text);
			setupText.setText(strings[position]);
			
			return view;
		}
	};
}


