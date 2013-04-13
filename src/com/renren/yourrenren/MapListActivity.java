package com.renren.yourrenren;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
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
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleAdapter.ViewBinder;

public class MapListActivity extends ListActivity {
	private String [] Names;
	private String [] Uids;
	private String [][] Birthdays;
	private String[] Headurls;
	private String access_token;
	int [] flag;
	EditText inputSearch;
	SimpleAdapter adapter;
	//Bitmap [] bm;
	
	ArrayList<HashMap<String, Object>> users;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{

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
			JSONArray friends = new JSONArray(result);
			Names = new String[friends.length()];
			Uids = new String[friends.length()];
			Headurls = new String[friends.length()];
			flag = new int[friends.length()];
			String uidparam = new String();
			
			for (int i = 0;i < friends.length();i++)
			{
				JSONObject tmp = friends.getJSONObject(i);
				Names[i] = tmp.getString("name");
				Uids[i] = tmp.getString("id");
				Headurls[i] = tmp.getString("tinyurl");
				uidparam = uidparam + Uids[i] + ",";
			}
			
			//bm = new Bitmap[friends.length()];
			users = new ArrayList<HashMap<String, Object>>(); 
			//adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, Names);
			adapter = new SimpleAdapter(this, users, R.layout.listitem, new String[]{"username"}, new int[]{R.id.list_name});
			for (int i = 0;i < friends.length();i++)
			{
				HashMap<String, Object> user = new HashMap<String, Object>();
				user.put("username", Names[i]);
				users.add(user);
			}
			
//			adapter.setViewBinder(new ViewBinder(){
//
//				  public boolean setViewValue(View view, Object data,  
//		                    String textRepresentation) {  
//		                //判断是否为我们要处理的对象  
//		                if(view instanceof ImageView  && data instanceof Bitmap){  
//		                    ImageView iv = (ImageView) view;  
//		                  
//		                    iv.setImageBitmap((Bitmap) data);  
//		                    return true;  
//		                }else  
//		                return false;  
//		            }  
//				});
			
//			for (int i = 0;i < friends.length();i++)
//			{	
//				HashMap<String, Object> user = new HashMap<String, Object>();
//				URL logourl = new URL(Headurls[i]);
//		        URLConnection conn = logourl.openConnection(); 
//		        conn.connect(); 
//		        InputStream is = conn.getInputStream(); 
//	            bm[i] = BitmapFactory.decodeStream(is); 
//				user.put("img", bm[i]); 
//				user.put("username",Names[i]); 
//				users.add(user); 
//			}
			
			UIActivity.dismissProgress();
			super.onCreate(savedInstanceState);
			setContentView(R.layout.focuslist);
			
			setListAdapter(adapter);
			//getListView().setTextFilterEnabled(true);
			
			inputSearch = (EditText) findViewById(R.id.inputSearch);
			inputSearch.addTextChangedListener(new TextWatcher() {
				 
		    public void onTextChanged(CharSequence cs,int arg1, int arg2, int arg3) {
		        // When user changed the Text
		       MapListActivity.this.adapter.getFilter().filter(cs);
		 
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
		
		
		Bundle data = new Bundle();
		data.putSerializable("name", Names[position]);
		data.putSerializable("uid", Uids[position]);
		data.putSerializable("access_token", access_token);
		
		Intent intent = new Intent(MapListActivity.this, MyMapActivity.class);
		intent.putExtras(data);
		startActivity(intent);
	}
	


}
