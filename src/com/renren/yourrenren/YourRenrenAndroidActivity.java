package com.renren.yourrenren;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class YourRenrenAndroidActivity extends Activity {
    /** Called when the activity is first created. */
	
	private Button login;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        
        login = (Button)findViewById(R.id.auth_site_mode);
        login.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				//Intent intent = new Intent(YourRenrenAndroidActivity.this, WebViewActivity.class);
				Intent intent = new Intent(YourRenrenAndroidActivity.this, WebViewActivity.class);
				startActivity(intent);
			}
		});
    }
} 