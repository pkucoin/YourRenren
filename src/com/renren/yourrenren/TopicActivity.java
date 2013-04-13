package com.renren.yourrenren;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
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
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class TopicActivity extends Activity{
	private EditText editText;
	private Button button;
	private TextView text;
	private ListView listView;
	private List<HashMap<String, Object>> statusList;
	//private List<HashMap<String, Object>> finalResultList = new ArrayList<HashMap<String, Object>>();
	
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
	//private int friend_num = 0;
	private int status_num = 0;
	private int final_result_num = 0;
	//private String uid;
	private String target_string;
	//private String cur_type;
	
	 @Override
	    public void onCreate(Bundle savedInstanceState) {
		 	SearchActivity.dismissProgress();
	    	super.onCreate(savedInstanceState);
	    	setContentView(R.layout.topic);
	    	
	    	editText = (EditText)findViewById(R.id.editText);
	    	button = (Button)findViewById(R.id.button);
	    	//text = (TextView)findViewById(R.id.text);
	    	listView = (ListView)findViewById(R.id.listView);
	    	
	    	Bundle bundle=getIntent().getExtras();
	        access_token=bundle.getString("access_token");
	        
	        button.setOnClickListener(new View.OnClickListener() {
				
				public void onClick(View v) {
					showProgress("请稍后", "搜索中");
					final_result_num = 0;
					method = "feed.get";
					page_count = "50";
					Editable target = editText.getText();
					target_string = target.toString();
					statusList = new ArrayList<HashMap<String, Object>>();
					for(cur_page_num = 1; cur_page_num < 5; cur_page_num ++)
					{
						page = Integer.toString(cur_page_num);
						Log.v("Debug", "page number " + page);
						HashMap<String, String> parameters = new HashMap<String, String>();
						parameters.put("v", version);
						parameters.put("access_token", access_token);
						parameters.put("method", method);
						parameters.put("format", format);
						parameters.put("type", type);
						parameters.put("count", page_count);
						parameters.put("page", page);
						sig = getSignature(parameters, secret_key);		//calculate signature
						
						url = "http://api.renren.com/restserver.do";
						HttpPost postmethod = new HttpPost(url);
						List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);  

						nameValuePairs.add(new BasicNameValuePair("v", version));  
						nameValuePairs.add(new BasicNameValuePair("access_token", access_token));  
						nameValuePairs.add(new BasicNameValuePair("method", method));  
						nameValuePairs.add(new BasicNameValuePair("format", format));  
						nameValuePairs.add(new BasicNameValuePair("sig", sig));
						nameValuePairs.add(new BasicNameValuePair("count", page_count));
						nameValuePairs.add(new BasicNameValuePair("type", type));
						nameValuePairs.add(new BasicNameValuePair("page", page));
						
						try{
							postmethod.setEntity(new UrlEncodedFormEntity(nameValuePairs));
						}
						catch(Exception e)
						{
						}
						HttpClient client = new DefaultHttpClient();
						String result ="";
						
						try
						{
							HttpResponse response = client.execute(postmethod);
							result = EntityUtils.toString(response.getEntity());
							JSONArray statusArray = new JSONArray(result);
							//statusList = new ArrayList<HashMap<String, Object>>();
							if(statusArray.length() == 0)
								break;
							for(status_num = 1; status_num<statusArray.length(); status_num++) {
								JSONObject statusObject = (JSONObject) statusArray.get(status_num - 1);
								String cur_type = statusObject.getString("feed_type");
								String message = "";
								String prefix = "";
								if(cur_type.equals("10"))
								{
									message = statusObject.getString("message");
									prefix = "发表状态：";
								}
								if(cur_type.equals("20"))
								{
									message = statusObject.getString("title");
									prefix = "发表日志：";
								}
								if(cur_type.equals("21"))
								{
									message = statusObject.getString("title");
									prefix = "分享日志：";
								}
								if(cur_type.equals("32"))
								{
									message = statusObject.getString("title");
									prefix = "分享照片：";
								}
								if(cur_type.equals("33"))
								{
									message = statusObject.getString("title");
									prefix = "分享相册：";
								}
								if(cur_type.equals("50"))
								{
									message = statusObject.getString("title");
									prefix = "分享视频：";
								}
								if(cur_type.equals("51")) 
								{
									message = statusObject.getString("title");
									prefix = "分享链接：";
								}
								//if(cur_type == "10")
								//	message = statusObject.getString("message");
								//else
								//	message = statusObject.getString("title");
								Log.v("Debug", cur_type + ": " + message);
								//uid = friendArray.getString(friend_num);
								//HashMap<String, Object> map = new HashMap<String, Object>();
								//map.put("num", friend_num);
								//map.put("uid", uid);
								//friendList.add(map);
								if(message.contains(target_string))
								{
									final_result_num++;
									HashMap<String, Object> map = new HashMap<String, Object>();
									String name = statusObject.getString("name");
									map.put("num", final_result_num);
									map.put("name", name);
									map.put("prefix", prefix);
									map.put("message", message);
									statusList.add(map);
								}
							}
							
						}
						catch(Exception e)
						{
							e.printStackTrace();
						}
					}
					if(statusList.isEmpty())
					{
						HashMap<String, Object> map = new HashMap<String, Object>();
						map.put("name", "搜索结果为空");
						statusList.add(map);
					}
					SimpleAdapter adapter = new SimpleAdapter(TopicActivity.this, statusList, R.layout.list, new String[]{"name", "prefix", "message"}, new int[]{R.id.user, R.id.prefix, R.id.message});
					listView.setAdapter(adapter);
					dismissProgress();
				}
					
			});
	 }
	 
	 
	 public String getSignature(Map<String, String> paramMap, String secret) {
			List<String> paramList = new ArrayList<String>(paramMap.size());
			//1、参数格式化
			for(Map.Entry<String,String> param:paramMap.entrySet()){
				paramList.add(param.getKey()+"="+param.getValue());
			}
			//2、排序并拼接成一个字符串
			Collections.sort(paramList);
			StringBuffer buffer = new StringBuffer();
			for (String param : paramList) {
				buffer.append(param);
			}
			//3、追加script key
			buffer.append(secret);
			//4、将拼好的字符串转成MD5值
			try {
			    java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
			    StringBuffer result = new StringBuffer();
			    try {
			        for (byte b : md.digest(buffer.toString().getBytes("UTF-8"))) {
			            result.append(Integer.toHexString((b & 0xf0) >>> 4));
			            result.append(Integer.toHexString(b & 0x0f));
			        }
			    } catch (UnsupportedEncodingException e) {
			        for (byte b : md.digest(buffer.toString().getBytes())) {
			            result.append(Integer.toHexString((b & 0xf0) >>> 4));
			            result.append(Integer.toHexString(b & 0x0f));
			        }
			    }
			    return result.toString();
			} catch (java.security.NoSuchAlgorithmException ex) {
			    ex.printStackTrace();
			}
			return null;
		}
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
}
