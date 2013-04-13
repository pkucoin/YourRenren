package com.renren.yourrenren;

import java.util.Calendar;
import java.util.Date;


import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SetSMSActivity extends Activity {
	private Button btnSend;
	private EditText edtPhoneNo;
	private EditText edtContent;
	
	 @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.sms);
	        

	        btnSend = (Button) findViewById(R.id.btnSend);
			edtPhoneNo = (EditText) findViewById(R.id.edtPhoneNo);
			edtContent = (EditText) findViewById(R.id.edtContent);
			
			btnSend.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					String phoneNo = edtPhoneNo.getText().toString();
					String message = edtContent.getText().toString();
					if (phoneNo.length() > 0 && message.length() > 0) {
						// call sendSMS to send message to phoneNo
				        Calendar c=Calendar.getInstance();
				        Intent dataintent = getIntent();
				        Bundle data = dataintent.getExtras();
				        String[] birthday = (String[])data.getSerializable("birthday");
				        //c.set(Calendar.MONTH, Integer.valueOf(birthday[1]));//也可以填数字，0-11,一月为0
				        //c.set(Calendar.DAY_OF_MONTH, Integer.valueOf(birthday[2]));
				        //c.set(Calendar.HOUR_OF_DAY, 0);
				        //c.set(Calendar.MINUTE, 0);
				        //c.set(Calendar.SECOND, 0);
				        c.set(Calendar.SECOND, c.get(Calendar.SECOND) + 120);
				        
				        Bundle bdata = new Bundle();
				        bdata.putSerializable("phoneNo", phoneNo);
				        bdata.putSerializable("message", message);
				        
				        Intent intent=new Intent(SetSMSActivity.this,AlarmReceiver.class);
				        intent.putExtras(bdata);
				        PendingIntent pi=PendingIntent.getBroadcast(SetSMSActivity.this, 0, intent,0);
				        //设置一个PendingIntent对象，发送广播
				        AlarmManager am=(AlarmManager)getSystemService(ALARM_SERVICE);
				        //获取AlarmManager对象
				        am.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pi);


				        //时间到时，执行PendingIntent，只执行一次
				        //AlarmManager.RTC_WAKEUP休眠时会运行，如果是AlarmManager.RTC,在休眠时不会运行
				        //am.setRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), 10000, pi);
				        //如果需要重复执行，使用上面一行的setRepeating方法，倒数第二参数为间隔时间,单位为毫秒
				        Toast.makeText(getBaseContext(), "定时短信将在生日当天零点发送", Toast.LENGTH_SHORT).show();
				        finish();
					} else
						Toast.makeText(getBaseContext(),
								"请输入号码和短信内容",
								Toast.LENGTH_SHORT).show();
				}
			});
		 

	 
	    }
}
