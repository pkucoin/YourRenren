package com.renren.yourrenren;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.Button;

public class SearchActivity extends Activity {
	private Button search_topic;
	private Button search_status;
	private String access_token;
	
	private static ProgressDialog progressDialog;
	
	public void showProgress(String title, String message) {
		progressDialog = ProgressDialog.show(this, title, message);
	}

	/**
	 * 取消等待框 
	 */
	public static void dismissProgress() {
		if (progressDialog != null) {
			try {
				progressDialog.dismiss();
			} catch (Exception e) {
			}
		}
	}
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.search);
    	
    	search_topic = (Button)findViewById(R.id.search_topic);
    	search_status = (Button)findViewById(R.id.search_status);
    	
    	
    	Intent intent = getIntent();
    	Bundle bundle = intent.getExtras();
    	access_token = (String)(bundle.getSerializable("access_token"));
    	//access_token = "195336|6.8e176cea8f3f88554904663d7ddc2017.2592000.1355817600-285319651";
    	search_topic.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				showProgress("请稍后", "获取新鲜事中");
				Intent intent = new Intent(SearchActivity.this, TopicActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("access_token", access_token);
				intent.putExtras(bundle);
				startActivity(intent);
			}
    	});
    	search_status.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				showProgress("请稍后", "获取好友信息中");
				Intent intent = new Intent(SearchActivity.this, MyKeyword.class);
				Bundle bundle = new Bundle();
				bundle.putString("access_token", access_token);
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});

    }

}
