package com.renren.yourrenren;

import java.util.ArrayList;
import java.util.Calendar;
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
import org.json.JSONObject;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class FocusListActivity extends ListActivity {
	private String [] Names;
	private String [] Uids;
	private String [][] Birthdays;
	private String[] Headurls;
	private String access_token;
	int [] flag;
	EditText inputSearch;
	ArrayList<HashMap<String, Object>> users;
	SimpleAdapter adapter;
	JSONArray friends;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.focuslist);
		UIActivity.dismissProgress();
		Intent intent = getIntent();
		Bundle data = intent.getExtras();
		
		access_token = (String)data.getSerializable("access_token");
		String api_key = "14b42a4c65de42778677f50c6f9facc7";
		String secret_key = "133d903cd49d4245bd377e421e088030";
		String version = "1.0";
		String method = "friends.getFriends";
		//String method = "users.getLoggedInUser";
		String format = "JSON";
		HashMap<String, String> parameters = new HashMap<String, String>();
		parameters.put("v", version);
		parameters.put("access_token", access_token);
		parameters.put("method", method);
		parameters.put("format", format);
		String sig = Signature.getSignature(parameters, secret_key);

		
		String url = "http://api.renren.com/restserver.do";
		HttpPost postmethod = new HttpPost(url);
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);  

		nameValuePairs.add(new BasicNameValuePair("v", version));  
		nameValuePairs.add(new BasicNameValuePair("access_token", access_token));  
		nameValuePairs.add(new BasicNameValuePair("method", method));  
		nameValuePairs.add(new BasicNameValuePair("format", format));  
		nameValuePairs.add(new BasicNameValuePair("sig", sig));
		
		try{
		postmethod.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
//		postmethod.addParameter("v", version);
//		postmethod.addParameter("access_token", access_token);
//		postmethod.addParameter("method", method);
//		postmethod.addParameter("format", format);
//		postmethod.addParameter("sig", sig);
		
		HttpClient client = new DefaultHttpClient();
		String result ="";
		try
		{
			HttpResponse response = client.execute(postmethod);
			result = EntityUtils.toString(response.getEntity());
			//JSONObject obj = new JSONObject(result);
			friends = new JSONArray(result);
			Names = new String[friends.length()];
			Uids = new String[friends.length()];
			String uidparam = new String();
			for (int i = 0;i < friends.length();i++)
			{
				Names[i] = friends.getJSONObject(i).getString("name");
				Uids[i] = friends.getJSONObject(i).getString("id");
				uidparam = uidparam + Uids[i] + ",";
			}
			//adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, Names);
			users = new ArrayList<HashMap<String, Object>>(); 
			adapter = new SimpleAdapter(this, users, R.layout.listitem, new String[]{"username"}, new int[]{R.id.list_name});
			for (int i = 0;i < friends.length();i++)
			{
				HashMap<String, Object> user = new HashMap<String, Object>();
				user.put("username", Names[i]);
				users.add(user);
			}
			setListAdapter(adapter);
			inputSearch = (EditText) findViewById(R.id.inputSearch);
			//设置过滤器
			inputSearch.addTextChangedListener(new TextWatcher() {
				 
		    public void onTextChanged(CharSequence cs,int arg1, int arg2, int arg3) {
		        // When user changed the Text
		       FocusListActivity.this.adapter.getFilter().filter(cs);
		 
		    }
		 
		    public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
		 
		            int arg3) { 
		        // TODO Auto-generated method stub	   
		    }
		 
		    public void afterTextChanged(Editable arg0){	 
		        // TODO Auto-generated method stub		 
		    }
			 
			});
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		
		
	}
	
	public void onListItemClick(ListView l, View v, int position, long id)
	{
		showProgress("请稍后", "搜索中");
		Bundle data = new Bundle();
		//TextView tmp = (TextView)l.getChildAt(position);
		String tmpname = ((HashMap<String, String>)l.getItemAtPosition(position)).get("username");
		data.putSerializable("name", tmpname);
		int uid_num = 0;
		for (int i = 0;i < friends.length();i++)
		{
			if (tmpname.equals(Names[i]))
			{
				uid_num = i;
				break;
			}
		}
		data.putSerializable("uid", Uids[uid_num]);
		data.putSerializable("access_token", access_token);
		
		
		Intent intent = new Intent(FocusListActivity.this, FocusActivity.class);
		intent.putExtras(data);
		startActivity(intent);
	}
	
	private static ProgressDialog progressDialog;
	
	public void showProgress(String title, String message) {
		progressDialog = ProgressDialog.show(this, title, message);
	}
	public static void dismissProgress() {
		if (progressDialog != null) {
			try {
				progressDialog.dismiss();
			} catch (Exception e) {
			}
		}
	}
}
