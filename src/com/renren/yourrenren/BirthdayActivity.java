package com.renren.yourrenren;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
/**
 *显示好友头像、生日日期以及短信编辑按钮
 */
public class BirthdayActivity extends Activity {
	   
	    Button button;
	    
	   @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.birthday);
	        
	        Intent intent = getIntent();
	        Bundle data = intent.getExtras();
	        
	        final String name = (String)data.getSerializable("name");
	        final String [] birthday = (String[])data.getSerializable("birthday");
	        String logourl = (String)data.getSerializable("logourl");
	        
	        TextView text = (TextView)findViewById(R.id.birthday_info);
	        text.setText(name+"的生日是"+birthday[1]+"月"+birthday[2]+"日");
	        
	        try{
	        //通过url获取头像
            URL url = new URL(logourl); 
            URLConnection conn = url.openConnection(); 
            conn.connect(); 
            InputStream is = conn.getInputStream(); 
            Bitmap bm = BitmapFactory.decodeStream(is); 
            ImageView image = (ImageView)findViewById(R.id.userlogo); 
            image.setImageBitmap(bm); 
            
            
            button = (Button)findViewById(R.id.setsms);
            button.setOnClickListener(new View.OnClickListener() {
    			public void onClick(View v) {
    				Intent smsintent = new Intent(BirthdayActivity.this, SetSMSActivity.class);
    				Bundle smsdata = new Bundle();
            		smsdata.putSerializable("Name", name);
            		smsdata.putSerializable("birthday", birthday);
            		smsintent.putExtras(smsdata);
            		startActivity(smsintent);
    			}
    		});
            
	        }
	        catch(Exception e)
	        {
	        	e.printStackTrace();
	        }
	        
	   }
}
