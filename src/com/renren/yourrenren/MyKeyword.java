package com.renren.yourrenren;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;

public class MyKeyword extends Activity {
	private String [] StatusInfo;
	private TextView textArea;
	private Button button;
	private String keyword;
	private ListView listview;
	private List<HashMap<String, Object>> statusList;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.keyword);
		SearchActivity.dismissProgress();
    	textArea = (EditText)findViewById(R.id.keywordedittext);
    	button = (Button)findViewById(R.id.keywordbutton);	
    	listview = (ListView)findViewById(R.id.keywordlistview);
    	
		Intent intent = getIntent();
		Bundle data = intent.getExtras();
		
		String access_token = (String)data.getSerializable("access_token");
		String api_key = "14b42a4c65de42778677f50c6f9facc7";
		String secret_key = "133d903cd49d4245bd377e421e088030";
		String version = "1.0";
		String method = "status.gets";
		String format = "JSON";
		String count = "1000";
		HashMap<String, String> parameters = new HashMap<String, String>();
		parameters.put("v", version);
		parameters.put("access_token", access_token);
		parameters.put("method", method);
		parameters.put("format", format);
		parameters.put("count", count);
		String sig = Signature.getSignature(parameters, secret_key);

		
		String url = "http://api.renren.com/restserver.do";
		HttpPost postmethod = new HttpPost(url);
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);  

		nameValuePairs.add(new BasicNameValuePair("v", version));  
		nameValuePairs.add(new BasicNameValuePair("access_token", access_token));  
		nameValuePairs.add(new BasicNameValuePair("method", method));  
		nameValuePairs.add(new BasicNameValuePair("format", format));  
		nameValuePairs.add(new BasicNameValuePair("sig", sig));
		nameValuePairs.add(new BasicNameValuePair("count", count));
		
		try{
		postmethod.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		HttpClient client = new DefaultHttpClient();
		String result ="";
		try
		{
			HttpResponse response = client.execute(postmethod);
			result = EntityUtils.toString(response.getEntity());
			final JSONArray statuses = new JSONArray(result);
			final int cnt = statuses.length();
			Log.v("total", Integer.toString(cnt));
			
			button.setOnClickListener(new View.OnClickListener() 
			{
				public void onClick(View v)
				{
					statusList = new ArrayList<HashMap<String, Object>>();
					keyword = textArea.getText().toString();
					Log.v("keyword", keyword);
					
					HashMap<String, Object> data = null;
					String status = null, time = null;
					for(int i = 0; i < cnt; i++)
					{
						try 
						{
							status = new String(statuses.getJSONObject(i).getString("message"));
							time = new String(statuses.getJSONObject(i).getString("time"));
							if(status.contains(keyword))
							{
								data = new HashMap<String, Object>();
								data.put("time", time);
								data.put("message", status);
								statusList.add(data);
							}
						} 
						catch (JSONException e) 
						{
							e.printStackTrace();
						}
					}
					if(statusList.isEmpty())
					{
						data = new HashMap<String, Object>();
						data.put("time", "");
						data.put("message", "没有相关结果");
						statusList.add(data);
					}					
					SimpleAdapter adapter = new SimpleAdapter(MyKeyword.this, statusList, R.layout.klist, new String[]{"time", "message"}, new int[]{R.id.time, R.id.message});
					listview.setAdapter(adapter);
				}
			});					
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}		
	}
}
