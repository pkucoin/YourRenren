package com.renren.yourrenren;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.Button;

public class UIActivity extends Activity {
	private Button birthday;
	private Button topic;
	private Button location;
	private Button focus;
	private Button mymap;
	private Button logout;
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
    	setContentView(R.layout.main);
    	
    	birthday = (Button)findViewById(R.id.birthday);
    	topic = (Button)findViewById(R.id.topic);
    	location = (Button)findViewById(R.id.location);
    	focus = (Button)findViewById(R.id.focus);
    	mymap = (Button)findViewById(R.id.mymap);
    	logout = (Button)findViewById(R.id.logout);
    	
    	Intent intent = getIntent();
    	Bundle bundle = intent.getExtras();
    	access_token = (String)(bundle.getSerializable("access_token"));
    	//access_token = "195336|6.ce53394e3e51d6ee1f0dc9037a875e71.2592000.1356145200-285319651";
    	birthday.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				showProgress("请稍后", "获取好友信息中");
				Intent intent = new Intent(UIActivity.this, MyFriendListActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("access_token", access_token);
				intent.putExtras(bundle);
				startActivity(intent);
			}
    	});
        topic.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(UIActivity.this, SearchActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("access_token", access_token);
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});

        location.setOnClickListener(new View.OnClickListener() {
  			public void onClick(View v) {
  				Intent intent = new Intent(UIActivity.this, LocationActivity.class);
  				Bundle bundle = new Bundle();
  				bundle.putString("access_token", access_token);
  				intent.putExtras(bundle);
  				startActivity(intent);
  			}
  		});
        
        focus.setOnClickListener(new View.OnClickListener() {
  			public void onClick(View v) {
				showProgress("请稍后", "获取好友信息中");
  				Intent intent = new Intent(UIActivity.this, FocusListActivity.class);
  				Bundle bundle = new Bundle();
  				bundle.putString("access_token", access_token);
  				intent.putExtras(bundle);
  				startActivity(intent);
  			}
  		});
 
        mymap.setOnClickListener(new View.OnClickListener() {
  			public void onClick(View v) {
  				showProgress("请稍后", "获取好友信息中");
  				Intent intent = new Intent(UIActivity.this, MapListActivity.class);
  				Bundle bundle = new Bundle();
  				bundle.putString("access_token", access_token);
  				intent.putExtras(bundle);
  				startActivity(intent);
 
  			}
  		}); 
        
        logout.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
		        CookieSyncManager cookieSyncMngr = CookieSyncManager.createInstance(UIActivity.this);
		        CookieManager cookieManager = CookieManager.getInstance();
		        cookieManager.removeAllCookie();
		        Intent intent = new Intent(UIActivity.this, YourRenrenAndroidActivity.class);
		        startActivity(intent);
		        finish();
			}
		});
    }

}
