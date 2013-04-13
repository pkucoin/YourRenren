package com.renren.yourrenren;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.widget.Toast;
/**
 *AlarmService的广播接收类  在接收到广播后执行短信发送
 */
public class AlarmReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context arg0, Intent arg1) {
		// TODO Auto-generated method stub
        Bundle data = arg1.getExtras();
        String phoneNo = (String)data.getSerializable("phoneNo");
        String message = (String)data.getSerializable("message");
		SmsManager sms = SmsManager.getDefault();
		sms.sendTextMessage(phoneNo, null, message, null, null);
		//Toast.makeText(arg0, "Alarm went off", Toast.LENGTH_SHORT).show();
	}
}
