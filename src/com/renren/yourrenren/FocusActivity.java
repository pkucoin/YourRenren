package com.renren.yourrenren;

import java.io.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class FocusActivity extends Activity {

	final private String feedCache = "/YourRenrenFeedCache.txt";
	final private String commentCache = "/YourRenrenCommentCache.txt";
	boolean fromCache = false;
	
	private WebView webview;
	private ListView listView;
	private List<HashMap<String, Object>> statusList;
	// private List<HashMap<String, Object>> finalResultList = new
	// ArrayList<HashMap<String, Object>>();

	private String url;
	private String access_token;
	private String api_key = "14b42a4c65de42778677f50c6f9facc7";
	private String secret_key = "133d903cd49d4245bd377e421e088030";
	private String version = "1.0";
	private String method;
	private String type = "10,20,21,32,33,50,51";
	private String page = "1";
	private int cur_page_num = 1;
	private String page_count;
	private String format = "JSON";
	private String sig = "";
	// private int friend_num = 0;
	private int status_num = 0;
	private int final_result_num = 0;
	// private String uid;
	private String target_string;
	private String target_uid;
	private String target_name;
	private String[] webaddress;
	private int cnt_address = 0;

	// private String cur_type;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		setContentView(R.layout.focus);
		FocusListActivity.dismissProgress();

		listView = (ListView) findViewById(R.id.mylist);

		Bundle bundle = getIntent().getExtras();
		access_token = bundle.getString("access_token");
		target_uid = bundle.getString("uid");
		target_name = bundle.getString("name");

		webaddress = new String[50];

		final_result_num = 0;
		method = "feed.get";
		page_count = "30";
		statusList = new ArrayList<HashMap<String, Object>>();
		for (cur_page_num = 1; cur_page_num < 2; cur_page_num++) {
			page = Integer.toString(cur_page_num);
			Log.v("Debug", "page number " + page);
			HashMap<String, String> parameters = new HashMap<String, String>();
			parameters.put("v", version);
			parameters.put("access_token", access_token);
			method = "feed.get";
			parameters.put("method", method);
			parameters.put("format", format);
			parameters.put("type", type);
			parameters.put("count", page_count);
			parameters.put("page", page);
			sig = Signature.getSignature(parameters, secret_key); // calculate
																	// signature

			url = "http://api.renren.com/restserver.do";
			HttpPost postmethod = new HttpPost(url);
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);

			nameValuePairs.add(new BasicNameValuePair("v", version));
			nameValuePairs.add(new BasicNameValuePair("access_token",
					access_token));
			nameValuePairs.add(new BasicNameValuePair("method", method));
			nameValuePairs.add(new BasicNameValuePair("format", format));
			nameValuePairs.add(new BasicNameValuePair("sig", sig));
			nameValuePairs.add(new BasicNameValuePair("count", page_count));
			nameValuePairs.add(new BasicNameValuePair("type", type));
			nameValuePairs.add(new BasicNameValuePair("page", page));

			try {
				postmethod.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			} catch (Exception e) {
			}
			HttpClient client = new DefaultHttpClient();
			String result = null;

			try {
				result = readCache(feedCache, true);
				if (result == null) 
				{
					fromCache = false;
					HttpResponse response = client.execute(postmethod);
					result = EntityUtils.toString(response.getEntity());
					
					Calendar ca = Calendar.getInstance();
					int year = ca.get(Calendar.YEAR);
					int day = ca.get(Calendar.DAY_OF_YEAR);
					int hour = ca.get(Calendar.HOUR_OF_DAY);
					int minute = ca.get(Calendar.MINUTE);
					String time = Integer.toString(year) + ' ' + Integer.toString(day)
							+ ' ' + Integer.toString(hour) + ' ' + Integer.toString(minute)
							+ "\r\n";
					writeCache(time + result, feedCache);
				}
				else
					fromCache = true;
				
				JSONArray statusArray = new JSONArray(result);
				// statusList = new ArrayList<HashMap<String, Object>>();
				if (statusArray.length() == 0)
					break;
				
				String cache = "";
				String feedComment[] = new String[statusArray.length()];
				if(fromCache)
				{
					cache = readCache(commentCache, false);
					feedComment = cache.split("\r\n");
				}
				
				for (status_num = 0; status_num < statusArray.length(); status_num++) 
				{
					JSONObject statusObject = (JSONObject) statusArray	.get(status_num);
					String cur_type = statusObject.getString("feed_type");
					String message = "";
					String prefix = "";
					String result2 = "";
					String status_id = statusObject.getString("source_id");
					String owner_id = statusObject.getString("actor_id");
					
					if (cur_type.equals("10")) 
					{
						prefix = "发表的状态";
						message = statusObject.getString("message");

						try 
						{
							if(fromCache)
								result2 = feedComment[status_num];
							else
							{
								HashMap<String, String> parameters2 = new HashMap<String, String>();
								method = "status.getComment";
		
								parameters2.put("v", version);
								parameters2.put("access_token", access_token);
								parameters2.put("method", method);
								parameters2.put("format", format);
								parameters2.put("status_id", status_id);
								parameters2.put("owner_id", owner_id);
								sig = Signature.getSignature(parameters2, secret_key);
		
								url = "http://api.renren.com/restserver.do";
								HttpPost postmethod2 = new HttpPost(url);
								List<NameValuePair> nameValuePairs2 = new ArrayList<NameValuePair>(2);
		
								nameValuePairs2.add(new BasicNameValuePair("v", version));
								nameValuePairs2.add(new BasicNameValuePair("access_token", access_token));
								nameValuePairs2.add(new BasicNameValuePair("method",	method));
								nameValuePairs2.add(new BasicNameValuePair("format", format));
								nameValuePairs2.add(new BasicNameValuePair("status_id",status_id));
								nameValuePairs2.add(new BasicNameValuePair("owner_id",owner_id));
								nameValuePairs2.add(new BasicNameValuePair("sig", sig));
							
								postmethod2.setEntity(new UrlEncodedFormEntity(nameValuePairs2));
								HttpClient client2 = new DefaultHttpClient();
								HttpResponse response2 = client2.execute(postmethod2);
								result2 = EntityUtils.toString(response2.getEntity());
								cache = cache + result2 + "\r\n";
							}

							JSONArray st = new JSONArray(result2);
							boolean flag = false;
							JSONObject tmp = new JSONObject();
							for (int i = 0; i < st.length(); i++) {
								tmp = (JSONObject) st.get(i);
								if (tmp.getString("uid").equals(target_uid)) {
									flag = true;
									break;
								}
							}

							if (flag) {
								final_result_num++;
								HashMap<String, Object> map = new HashMap<String, Object>();
								String name = statusObject.getString("name");
								map.put("name", target_name + "回复了" + name);
								map.put("prefix", prefix);
								map.put("message", message);

								webaddress[cnt_address++] = "http://status.renren.com/getdoing.do?id=" + owner_id + "&doingId=" + status_id;
								statusList.add(map);
							}
						} catch (Exception e) {
						}

					} else {
						if (cur_type.equals("20")) {
							message = statusObject.getString("title");
							prefix = "发表日志：";
						} else {

							message = statusObject.getString("title");
							prefix = "的分享";
							HashMap<String, String> parameters2 = new HashMap<String, String>();
							method = "share.getComments";
							String share_id = statusObject.getString("source_id");
							String user_id = statusObject.getString("actor_id");

							try 
							{
								if(fromCache)
									result2 = feedComment[status_num];
								else
								{
									parameters2.put("v", version);
									parameters2.put("access_token", access_token);
									parameters2.put("method", method);
									parameters2.put("format", format);
									parameters2.put("share_id", share_id);
									parameters2.put("user_id", user_id);
									sig = Signature.getSignature(parameters2, secret_key);
		
									url = "http://api.renren.com/restserver.do";
									HttpPost postmethod2 = new HttpPost(url);
									List<NameValuePair> nameValuePairs2 = new ArrayList<NameValuePair>(2);
		
									nameValuePairs2.add(new BasicNameValuePair("v",version));
									nameValuePairs2.add(new BasicNameValuePair("access_token", access_token));
									nameValuePairs2.add(new BasicNameValuePair("method", method));
									nameValuePairs2.add(new BasicNameValuePair("format", format));
									nameValuePairs2.add(new BasicNameValuePair("share_id", share_id));
									nameValuePairs2.add(new BasicNameValuePair("user_id", user_id));
									nameValuePairs2.add(new BasicNameValuePair("sig", sig));
	
									postmethod2.setEntity(new UrlEncodedFormEntity(nameValuePairs2));
									HttpClient client2 = new DefaultHttpClient();
									HttpResponse response2 = client2.execute(postmethod2);
									result2 = EntityUtils.toString(response2.getEntity());
									cache = cache + result2 + "\r\n";
								}

								JSONObject comments_response = new JSONObject(result2);
								JSONArray comments = comments_response.getJSONArray("comments");

								boolean flag = false;
								JSONObject tmp = new JSONObject();
								for (int i = 0; i < comments.length(); i++) {
									tmp = (JSONObject) comments.get(i);
									if (tmp.getString("uid").equals(target_uid)) {
										flag = true;
										break;
									}
								}

								if (flag) {
									final_result_num++;
									HashMap<String, Object> map = new HashMap<String, Object>();
									String name = statusObject
											.getString("name");
									map.put("name", target_name + "回复了" + name);
									map.put("prefix", prefix);
									map.put("message", message);

									webaddress[cnt_address++] = "http://share.renren.com/share/"
											+ user_id + "/" + share_id;
									statusList.add(map);
								}

							} catch (Exception e) {

							}

							// if(cur_type == "10")
							// message = statusObject.getString("message");
							// else
							// message = statusObject.getString("title");
							Log.v("Debug", cur_type + ": " + message);
							// uid = friendArray.getString(friend_num);
							// HashMap<String, Object> map = new HashMap<String,
							// Object>();
							// map.put("num", friend_num);
							// map.put("uid", uid);
							// friendList.add(map);
						}
					}

				}
				
				if(!fromCache)
					writeCache(cache, commentCache);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (statusList.isEmpty()) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("name", "搜索结果为空");
			statusList.add(map);
		}
		SimpleAdapter adapter = new SimpleAdapter(FocusActivity.this,
				statusList, R.layout.list, new String[] { "name",
						"prefix", "message"}, new int[] {R.id.user, R.id.prefix, R.id.message});
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// webview = (WebView)findViewById(R.id.webviewid);
				webview = new WebView(FocusActivity.this);
				webview.getSettings().setJavaScriptEnabled(true);
				webview.loadUrl(webaddress[arg2]);
				 webview.requestFocusFromTouch();

				setContentView(webview);
			}
		});

	}

	private String readCache(String filename, boolean checkTime) {
		String content = null;
		try {
			if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
				File sdcard = Environment.getExternalStorageDirectory();
				FileInputStream fis = new FileInputStream(sdcard.getCanonicalPath() + filename);
				BufferedReader br = new BufferedReader(new InputStreamReader(fis));

				if(checkTime)
				{
					boolean read;
					String time = br.readLine();
	
					String cacheTime[] = new String[4];
					int timeValue[] = new int[4];
					cacheTime = time.split(" ");
					int cacheYear = Integer.parseInt(cacheTime[0]);
					int cacheDay = Integer.parseInt(cacheTime[1]);
					int cacheHour = Integer.parseInt(cacheTime[2]);
					int cacheMinute = Integer.parseInt(cacheTime[3]);
	
					Calendar ca = Calendar.getInstance();
					int year = ca.get(Calendar.YEAR);
					int day = ca.get(Calendar.DAY_OF_YEAR);
					int hour = ca.get(Calendar.HOUR_OF_DAY);
					int minute = ca.get(Calendar.MINUTE);
	
					if (year == cacheYear) {
						int minutePassed = day * 24 * 60	+ hour	* 60 + minute	- (cacheDay * 24 * 60 + cacheHour * 60 + cacheMinute);
						if (minutePassed < 60)
							read = true;
						else
							read = false;
					} else
						read = false;
	
					if (read)
						content = br.readLine();
				}
				else
				{
					int ch;
					StringBuffer sb = new StringBuffer();
					while((ch = br.read()) != -1)
						sb.append((char)ch);
					return sb.toString();		
				}
			}
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return content;
	}

	private void writeCache(String content, String filename) {
		try {
			if (Environment.getExternalStorageState().equals(
					Environment.MEDIA_MOUNTED)) {
				File sdcard = Environment.getExternalStorageDirectory();
				File target = new File(sdcard.getCanonicalPath() + filename);
				FileOutputStream fos = new FileOutputStream(target);
				fos.write(content.getBytes());
				fos.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
