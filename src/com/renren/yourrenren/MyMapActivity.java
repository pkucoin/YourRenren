package com.renren.yourrenren;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
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
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Toast;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.GeoPoint;
import com.baidu.mapapi.ItemizedOverlay;
import com.baidu.mapapi.LocationListener;
import com.baidu.mapapi.MKAddrInfo;
import com.baidu.mapapi.MKDrivingRouteResult;
import com.baidu.mapapi.MKGeneralListener;
import com.baidu.mapapi.MKLocationManager;
import com.baidu.mapapi.MKPlanNode;
import com.baidu.mapapi.MKPoiResult;
import com.baidu.mapapi.MKSearch;
import com.baidu.mapapi.MKSearchListener;
import com.baidu.mapapi.MKTransitRouteResult;
import com.baidu.mapapi.MKWalkingRouteResult;
import com.baidu.mapapi.MKSuggestionResult;
import com.baidu.mapapi.MapActivity;
import com.baidu.mapapi.MapController;
import com.baidu.mapapi.MapView;
import com.baidu.mapapi.MyLocationOverlay;
import com.baidu.mapapi.Overlay;
import com.baidu.mapapi.OverlayItem;
import com.baidu.mapapi.PoiOverlay;
import com.baidu.mapapi.RouteOverlay;
import com.baidu.mapapi.TransitOverlay;

public class MyMapActivity extends MapActivity {
	BMapManager mBMapMan = null;
	// private List<HashMap<String, Object>> statusList;
	// private List<HashMap<String, Object>> finalResultList = new
	// ArrayList<HashMap<String, Object>>();

	private String url;

	private String access_token;
	private String api_key = "14b42a4c65de42778677f50c6f9facc7";
	private String secret_key = "133d903cd49d4245bd377e421e088030";
	private String version = "1.0";
	private String method;

	private String page = "1";
	private String page_count = "200";
	private String format = "JSON";
	private String sig = "";
	// private int friend_num = 0;
	private int status_num = 0;
	private int final_result_num = 0;
	private String target_uid = "";
	private double[] longitude;
	private double[] latitude;
	private String[] place_names;
	private String[] place_time;
	private int place_cnt = 0;
	private int cur_place = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map);

		Bundle bundle = getIntent().getExtras();
		access_token = bundle.getString("access_token");
		target_uid = bundle.getString("uid");

		longitude = new double[Integer.parseInt(page_count)];
		latitude = new double[Integer.parseInt(page_count)];
		place_names = new String[Integer.parseInt(page_count)];
		place_time = new String[Integer.parseInt(page_count)];

		HashMap<String, String> parameters = new HashMap<String, String>();
		parameters.put("v", version);
		parameters.put("access_token", access_token);
		method = "status.gets";
		parameters.put("method", method);
		parameters.put("format", format);
		parameters.put("count", page_count);
		parameters.put("uid", target_uid);

		sig = Signature.getSignature(parameters, secret_key); // calculate
																// signature

		url = "http://api.renren.com/restserver.do";
		HttpPost postmethod = new HttpPost(url);
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);

		nameValuePairs.add(new BasicNameValuePair("v", version));
		nameValuePairs
				.add(new BasicNameValuePair("access_token", access_token));
		nameValuePairs.add(new BasicNameValuePair("method", method));
		nameValuePairs.add(new BasicNameValuePair("format", format));
		nameValuePairs.add(new BasicNameValuePair("sig", sig));
		nameValuePairs.add(new BasicNameValuePair("count", page_count));
		nameValuePairs.add(new BasicNameValuePair("uid", target_uid));

		try {
			postmethod.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		} catch (Exception e) {
		}
		HttpClient client = new DefaultHttpClient();
		String result = "";

		int i;
		try {
			HttpResponse response = client.execute(postmethod);
			result = EntityUtils.toString(response.getEntity());
			JSONArray statusArray = new JSONArray(result);
			for (i = 0; i < statusArray.length(); i++) {
				JSONObject statusObject = (JSONObject) statusArray.get(i);
				if (statusObject.has("place")) {
					JSONObject tmp = statusObject.getJSONObject("place");
					longitude[place_cnt] = Double.parseDouble(tmp
							.getString("longitude"));
					latitude[place_cnt] = Double.parseDouble(tmp
							.getString("latitude"));
					place_names[place_cnt] = tmp.getString("name");
					place_time[place_cnt] = statusObject.getString("time");
					place_cnt++;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			InputStream is1 = getResources().openRawResource(R.raw.js1);
			BufferedReader br1 = new BufferedReader(new InputStreamReader(is1));

			String MapHtml = new String();
			String line = br1.readLine(); // 读取第一行
			while (line != null) { // 如果 line 为空说明读完了
				MapHtml += line; // 将读到的内容添加到 buffer 中
				MapHtml += "\n"; // 添加换行符
				line = br1.readLine(); // 读取下一行
			}
			br1.close();
			for (i = 0; i < place_cnt; i++) {
				MapHtml += "pt = new BMap.Point(" + longitude[i] + ","
						+ latitude[i] + ");";
				MapHtml += "\n";
				MapHtml += "markers.push(new BMap.Marker(pt));";
				MapHtml += "\n";
				MapHtml += "markers["
						+ i
						+ "].addEventListener(\"click\", function(){this.openInfoWindow(new BMap.InfoWindow(\""
						+ place_time[i] + " " + place_names[i] + "\"))});";
				MapHtml += "\n";
			}

			InputStream is2 = getResources().openRawResource(R.raw.js2);
			BufferedReader br2 = new BufferedReader(new InputStreamReader(is2));
			line = br2.readLine(); // 读取第一行
			while (line != null) { // 如果 line 为空说明读完了
				MapHtml += line; // 将读到的内容添加到 buffer 中
				MapHtml += "\n"; // 添加换行符
				line = br2.readLine(); // 读取下一行
			}
			br2.close();
			//File file2 = new File("/data/data/com.renren.yourrenren/files/Map.html"); // 因为路径是自己指定的，
//			File file2 = new File(Environment.getExternalStorageDirectory() + "/Map.html"); // 因为路径是自己指定的，
//			// 在此小马就直接合上面定义过的路径了
//			if (!file2.exists()) {
//
//				// 方式一：
//				try {
//					file2.createNewFile();
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
			FileOutputStream fout = new FileOutputStream(
					Environment.getExternalStorageDirectory() + "/Map.html");
			// OutputStream os = getResources().openRawResource(R.raw.map);
			// OutputStreamWriter osw = new OutputStreamWriter(fout, "UTF-8");
			fout.write(MapHtml.getBytes());
			fout.close();
			// osw.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		WebView webview = (WebView) findViewById(R.id.mapwebview);
		webview.getSettings().setJavaScriptEnabled(true);
		webview.loadUrl("file://" + Environment.getExternalStorageDirectory() + "/Map.html");

	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected void onDestroy() {
		if (mBMapMan != null) {
			mBMapMan.destroy();
			mBMapMan = null;
		}
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		if (mBMapMan != null) {
			mBMapMan.stop();
		}
		super.onPause();
	}

	@Override
	protected void onResume() {
		if (mBMapMan != null) {
			mBMapMan.start();
		}
		super.onResume();
	}

}
