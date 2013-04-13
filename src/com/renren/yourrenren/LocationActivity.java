package com.renren.yourrenren;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class LocationActivity extends Activity{
	String access_token;
	String api_key = "14b42a4c65de42778677f50c6f9facc7";
	String secret_key = "133d903cd49d4245bd377e421e088030";
	String version = "1.0";
	String method = "places.create";
	//String method = "users.getLoggedInUser";
	String format = "JSON";
	String address = "北京大学";
	String place_id;
	String sig;
	String url = "http://api.renren.com/restserver.do";
	
	HashMap<String, String> parameters = new HashMap<String, String>();
	 @Override
	    public void onCreate(Bundle savedInstanceState) {
		 super.onCreate(savedInstanceState);
			setContentView(R.layout.location);
			UIActivity.dismissProgress();
			Intent intent = getIntent();
			Bundle data = intent.getExtras();
			
			//首先获取本地IP
			WifiManager wm = (WifiManager)getSystemService(Context.WIFI_SERVICE);
			WifiInfo wi = wm.getConnectionInfo(); 
			int ipAddress = wi.getIpAddress();
			int [] ip = new int[4];
			ip[0] = ipAddress & 0xff;
			ip[1] = (ipAddress >> 8) & 0xff;
			ip[2] = (ipAddress >> 16) & 0xff;
			ip[3] = (ipAddress >> 24) & 0xff;
			
			String location = "Unknown";
			
			if (ip[0] == 61 && ip[1] == 50)
			{
				location = "北京大学软件与微电子学院";
			}
			if (ip[0] == 115 && ip[1] == 27)
			{
				if(ip[2] >= 0 && ip[2] <= 15)
					location = "未知";
				if(ip[2] >= 16 && ip[2] <= 18)
					location = "北京大学一教";
				if(ip[2] == 19)
					location = "北京大学理教";
				if(ip[2] == 20)
					location = "北京大学国关楼";
				if(ip[2] == 21)
					location = "北京大学万柳自习室";
				if(ip[2] == 22)
					location = "北京大学一教二楼/文史楼";
				if(ip[2] == 23)
					location = "北京大学文史楼";
				if(ip[2] >= 24 && ip[2] <= 26)
					location = "北京大学理教";
				if(ip[2] == 27)
					location = "北京大学理教三层";
				if(ip[2] >= 28 && ip[2] <= 33)
					location = "北京大学三教";
				if(ip[2] == 34)
					location = "北京大学三教四教";
				if(ip[2] == 35)
					location = "北京大学三教四层";
				if(ip[2] == 36)
					location = "北京大学图书馆4楼期刊阅览室";
				if(ip[2] == 37)
					location = "北京大学图书馆工具书阅览室";
				if(ip[2] >= 38 && ip[2] <= 41)
					location = "北京大学图书馆";
				if(ip[2] == 42)
					location = "北京大学图书馆四楼期刊";
				if(ip[2] >= 43 && ip[2] <= 47)
					location = "北京大学图书馆";
				if(ip[2] >= 48 && ip[2] <= 51)
					location = "未知";
				if(ip[2] == 52)
					location = "北京大学百年纪念讲堂";
				if(ip[2] == 53)
					location = "北京大学红二楼";
				if(ip[2] >= 54 && ip[2] <= 55)
					location = "北京大学未名湖/朗润园";
				if(ip[2] == 56)
					location = "北京大学教育学院";
				if(ip[2] == 57)
					location = "未知";
				if(ip[2] == 58)
					location = "北京大学化院（东门外）";
				if(ip[2] == 59)
					location = "未知";
				if(ip[2] == 60)
					location = "北京大学计算中心";
				if(ip[2] >= 61 && ip[2] <= 63)
					location = "北京大学理科一号楼";
				if(ip[2] == 64)
					location = "北京大学新光华楼";
				if(ip[2] >= 65 && ip[2] <= 66)
					location = "北京大学老光华楼/电教";
				if(ip[2] == 67)
					location = "北京大学老光华楼";
				if(ip[2] >= 68 && ip[2] <= 70)
					location = "北京大学二教一楼";
				if(ip[2] == 71)
					location = "北京大学二教二楼";
				if(ip[2] >= 72 && ip[2] <= 75)
					location = "北京大学二教一/二楼";
				if(ip[2] == 76)
					location = "北京大学二教四楼";
				if(ip[2] >= 77 && ip[2] <= 78)
					location = "北京大学二教四楼";
				if(ip[2] == 79)
					location = "北京大学二教四/五楼";
				if(ip[2] == 80)
					location = "北京大学二教";
				if(ip[2] == 81)
					location = "北京大学二教五楼";
				if(ip[2] >= 82 && ip[2] <= 83)
					location = "未知";
				if(ip[2] == 84)
					location = "北京大学法学院图书馆";
				if(ip[2] == 85)
					location = "北京大学理教三楼";
				if(ip[2] == 86)
					location = "未知";
				if(ip[2] == 87)
					location = "北京大学理教";
				if(ip[2] == 88)
					location = "北京大学理教四楼";
				if(ip[2] == 89)
					location = "北京大学理教四楼";
				if(ip[2] == 90)
					location = "北京大学理教四楼";
				if(ip[2] == 91)
					location = "北京大学理教";
				if(ip[2] == 92)
					location = "北京大学理教三楼/微纳电子大厦";
				if(ip[2] >= 93 && ip[2] <= 94)
					location = "北京大学理教三楼四楼";
				if(ip[2] == 95)
					location = "北京大学理教一楼";
				if(ip[2] >= 96 && ip[2] <= 97)
					location = "北京大学理教二楼/理教三楼";
				if(ip[2] >= 98 && ip[2] <= 99)
					location = "北京大学理教三楼";
				if(ip[2] == 100)
					location = "北京大学理教四楼";
				if(ip[2] >= 101 && ip[2] <= 108)
					location = "未知";
				if(ip[2] == 109)
					location = "北京大学朗润园万众楼";
				if(ip[2] >= 110 && ip[2] <= 117)
					location = "未知";
				if(ip[2] == 118)
					location = "北京大学化学与分子工程学院";
				if(ip[2] >= 119 && ip[2] <= 126)
					location = "未知";
				if(ip[2] == 127)
					location = "北京大学英杰交流大厅月光厅";
				if(ip[2] >= 128 && ip[2] <= 129)
					location = "北京大学28楼";
				if(ip[2] == 130)
					location = "北京大学28/29楼";
				if(ip[2] >= 131 && ip[2] <= 139)
					location = "北京大学28楼";
			}
			if(ip[0] == 162 && ip[1] == 105)
			{
				if(ip[2] >= 0 && ip[2] <= 2)
					location = "未知";
				if(ip[2] == 3)
					location = "北京大学网络1/覆盖图书馆/一教/电教";
				if(ip[2] == 4)
					location = "未知";
				if(ip[2] == 5)
					location = "北京大学师生缘/各食堂";
				if(ip[2] == 6)
					location = "北京大学核磁共振中心/老生物楼";
				if(ip[2] == 7)
					location = "北京大学理科二号楼/法学楼";
				if(ip[2] == 8)
					location = "北京大学红一楼/红二楼/研究生院";
				if(ip[2] == 9)
					location = "北京大学红四楼";
				if(ip[2] == 10)
					location = "北京大学考古楼/红楼";
				if(ip[2] == 11)
					location = "北京大学校医院/静园两侧各院";
				if(ip[2] == 12)
					location = "北京大学中国经济研究中心机房";
				if(ip[2] == 13)
					location = "北京大学信息管理系";
				if(ip[2] == 14)
					location = "北京大学电教";
				if(ip[2] == 15)
					location = "北京大学老光华楼";
				if(ip[2] == 16)
					location = "北京大学生物技术楼/老生物楼";
				if(ip[2] == 17)
					location = "北京大学遥感所";
				if(ip[2] == 18)
					location = "北京大学哲学楼";
				if(ip[2] == 19)
					location = "北京大学逸夫二楼";
				if(ip[2] == 20)
					location = "北京大学逸夫二楼";
				if(ip[2] == 21)
					location = "北京大学物理楼";
				if(ip[2] == 22)
					location = "北京大学化学与分子工程学院";
				if(ip[2] == 23)
					location = "北京大学地球物理系";
				if(ip[2] == 24)
					location = "北京大学电教";
				if(ip[2] == 25)
					location = "北京大学力学楼";
				if(ip[2] == 26)
					location = "北京大学电子系";
				if(ip[2] == 27)
					location = "北京大学化学与分子工程学院";
				if(ip[2] == 28)
					location = "北京大学图书馆";
				if(ip[2] == 29)
					location = "北京大学老光华楼机房";
				if(ip[2] == 30)
					location = "北京大学理科一号楼/计算机系";
				if(ip[2] == 31)
					location = "北京大学计算中心机房";
				if(ip[2] >= 32 && ip[2] <= 35)
					location = "北京大学45楼";
				if(ip[2] >= 36 && ip[2] <= 39)
					location = "北京大学46楼";
				if(ip[2] >= 40 && ip[2] <= 43)
					location = "北京大学47楼";
				if(ip[2] == 44)
					location = "北京大学44/48楼";
				if(ip[2] >= 45 && ip[2] <= 47)
					location = "北京大学48楼";
				if(ip[2] == 48)
					location = "北京大学25楼";
				if(ip[2] == 49)
					location = "北京大学30楼";
				if(ip[2] >= 50 && ip[2] <= 51)
					location = "北京大学45乙楼";
				if(ip[2] >= 52 && ip[2] <= 55)
					location = "北京大学45甲";
				if(ip[2] == 56)
					location = "北京大学畅春新园4号3/4楼";
				if(ip[2] == 57)
					location = "北京大学45甲";
				if(ip[2] >= 58 && ip[2] <= 63)
					location = "北京大学燕北园";
				if(ip[2] >= 64 && ip[2] <= 67)
					location = "北京大学理教机房/计算中心";
				if(ip[2] == 68)
					location = "北京大学理科一号楼/数学系";
				if(ip[2] >= 69 && ip[2] <= 70)
					location = "北京大学理科二号楼/数学系";
				if(ip[2] == 71)
					location = "北京大学理科二号楼/信科";
				if(ip[2] == 72)
					location = "北京大学理科二号楼";
				if(ip[2] == 73)
					location = "北京大学理科二号楼/地球物理系";
				if(ip[2] >= 74 && ip[2] <= 75)
					location = "北京大学理科二号楼/电子系";
				if(ip[2] == 76)
					location = "北京大学理科二号楼-电子系/地球物理系";
				if(ip[2] == 77)
					location = "北京大学理科二号楼/交流中心";
				if(ip[2] == 78)
					location = "北京大学理科二号楼/交流中心";
				if(ip[2] == 79)
					location = "北京大学理科一号楼";
				if(ip[2] == 80)
					location = "北京大学理科一号楼/信科";
				if(ip[2] == 81)
					location = "北京大学交流中心";
				if(ip[2] == 82)
					location = "北京大学畅春新园1号1/2/3楼";
				if(ip[2] == 83)
					location = "北京大学畅春新园1号4/5/6楼";
				if(ip[2] == 84)
					location = "北京大学畅春新园2号2楼";
				if(ip[2] == 85)
					location = "北京大学畅春新园2号1/4/5楼";
				if(ip[2] == 86)
					location = "北京大学畅春新园2号6楼|3号1/2楼";
				if(ip[2] == 87)
					location = "北京大学畅春新园2号3楼|3号5/6楼";
				if(ip[2] == 88)
					location = "北京大学畅春新园4号1/2楼";
				if(ip[2] == 89)
					location = "北京大学26楼";
				if(ip[2] >= 90 && ip[2] <= 92)
					location = "北京大学28楼";
				if(ip[2] == 93)
					location = "北京大学28/29楼";
				if(ip[2] == 94)
					location = "北京大学29楼";
				if(ip[2] == 95)
					location = "北京大学29/31楼";
				if(ip[2] >= 96 && ip[2] <= 98)
					location = "北京大学31楼";
				if(ip[2] == 99)
					location = "北京大学45甲";
				if(ip[2] == 100)
					location = "北京大学45甲";
				if(ip[2] == 101)
					location = "北京大学34A楼4/5/6楼";
				if(ip[2] >= 102 && ip[2] <= 103)
					location = "北京大学35楼";
				if(ip[2] == 104)
					location = "北京大学32楼";
				if(ip[2] == 105)
					location = "北京大学36楼1/2楼";
				if(ip[2] == 106)
					location = "北京大学36楼2/3/4楼";
				if(ip[2] == 107)
					location = "北京大学勺园";
				if(ip[2] == 108)
					location = "北京大学畅春新园4号5/6楼";
				if(ip[2] == 109)
					location = "北京大学畅春新园3号3/4楼";
				if(ip[2] == 110)
					location = "北京大学36楼";
				if(ip[2] == 111)
					location = "北京大学34A楼1/2/3楼";
				if(ip[2] >= 112 && ip[2] <= 113)
					location = "北京大学勺园";
				if(ip[2] == 114)
					location = "北京大学21/22/24楼";
				if(ip[2] == 115)
					location = "北京大学20楼";
				if(ip[2] == 116)
					location = "北京大学36楼4/5楼";
				if(ip[2] == 117)
					location = "北京大学36楼5/6楼";
				if(ip[2] == 118)
					location = "北京大学37楼1/2楼";
				if(ip[2] == 119)
					location = "北京大学37楼2/3楼";
				if(ip[2] == 120)
					location = "北京大学37楼3/4/5/6楼";
				if(ip[2] == 121)
					location = "北京大学37楼3/4/5/6楼";
				if(ip[2] == 122)
					location = "北京大学中国古代史研究中心";
				if(ip[2] == 123)
					location = "北京大学方正集团";
				if(ip[2] == 124)
					location = "北京大学外文楼";
				if(ip[2] == 125)
					location = "北京大学勺园";
				if(ip[2] >= 126 && ip[2] <= 128)
					location = "北京大学科技发展中心";
				if(ip[2] >= 129 && ip[2] <= 130)
					location = "北京大学计算中心";
				if(ip[2] == 131)
					location = "未知";
				if(ip[2] == 132)
					location = "北京大学理科一号楼";
				if(ip[2] >= 133 && ip[2] <= 135)
					location = "未知";
				if(ip[2] == 136)
					location = "北京大学图书馆";
				if(ip[2] == 137)
					location = "北京大学电教中心";
				if(ip[2] == 138)
					location = "北京大学图书馆";
				if(ip[2] == 139)
					location = "北京大学图书馆/《科学》杂志社";
				if(ip[2] >= 140 && ip[2] <= 141)
					location = "北京大学图书馆";
				if(ip[2] == 142)
					location = "北京大学教育学院";
				if(ip[2] == 143)
					location = "北京大学技物楼";
				if(ip[2] >= 144 && ip[2] <= 145)
					location = "未知";
				if(ip[2] == 146)
					location = "北京大学理科一号楼计算机系";
				if(ip[2] == 147)
					location = "北京大学加速器楼";
				if(ip[2] == 148)
					location = "北京大学逸夫一楼/法律系";
				if(ip[2] == 149)
					location = "北京大学逸夫二楼";
				if(ip[2] == 150)
					location = "北京大学经济中心";
				if(ip[2] == 151)
					location = "北京大学未名湖";
				if(ip[2] == 152)
					location = "未知";
				if(ip[2] == 153)
					location = "北京大学新化学楼";
				if(ip[2] == 154)
					location = "未知";
				if(ip[2] == 155)
					location = "北京大学资源大厦";
				if(ip[2] == 156)
					location = "北京大学郎润园";
				if(ip[2] == 157)
					location = "北京大学老地学楼";
				if(ip[2] == 158)
					location = "北京大学数学学院";
				if(ip[2] == 159)
					location = "北京大学物理系";
				if(ip[2] == 160)
					location = "北京大学现代物理中心";
				if(ip[2] == 161)
					location = "北京大学老化学楼";
				if(ip[2] >= 162 && ip[2] <= 163)
					location = "北京大学方正大厦";
				if(ip[2] == 164)
					location = "北京大学廖凯原楼";
				if(ip[2] == 165)
					location = "北京大学廖凯原楼";
				if(ip[2] == 166)
					location = "北京大学老地学楼(环境学院)";
				if(ip[2] == 167)
					location = "未知";
				if(ip[2] == 168)
					location = "北京大学昌平校区";
				if(ip[2] == 169)
					location = "北京大学经济中心";
				if(ip[2] == 170)
					location = "北京大学计算所";
				if(ip[2] == 171)
					location = "北京大学45乙楼4层";
				if(ip[2] >= 172 && ip[2] <= 174)
					location = "北京大学45乙楼";
				if(ip[2] == 175)
					location = "北京大学万柳学区3区";
				if(ip[2] == 176)
					location = "北京大学心理系";
				if(ip[2] == 177)
					location = "北京大学物化所";
				if(ip[2] == 178)
					location = "北京大学信息中心";
				if(ip[2] == 179)
					location = "北京大学计算机系";
				if(ip[2] == 180)
					location = "北京大学地质系";
				if(ip[2] == 181)
					location = "北京大学红五楼";
				if(ip[2] == 182)
					location = "北京大学光华管理学院";
				if(ip[2] >= 183 && ip[2] <= 184)
					location = "北京大学计算所";
				if(ip[2] == 185)
					location = "北京大学朗润园";
				if(ip[2] >= 186 && ip[2] <= 187)
					location = "北京大学西二旗";
				if(ip[2] == 188)
					location = "北京大学出版社";
				if(ip[2] == 189)
					location = "北京大学勺园";
				if(ip[2] == 190)
					location = "北京大学行政管理系";
				if(ip[2] == 191)
					location = "北京大学人口所";
				if(ip[2] == 192)
					location = "北京大学全斋";
				if(ip[2] == 193)
					location = "北京大学社会学系";
				if(ip[2] == 194)
					location = "北京大学马列学院";
				if(ip[2] == 195)
					location = "北京大学力学大院";
				if(ip[2] == 196)
					location = "北京大学畅春园52楼";
				if(ip[2] >= 197 && ip[2] <= 198)
					location = "北京大学承泽园";
				if(ip[2] == 199)
					location = "北京大学畅春园61楼";
				if(ip[2] == 200)
					location = "北京大学畅春园62楼";
				if(ip[2] == 201)
					location = "北京大学微纳电子大厦";
				if(ip[2] == 202)
					location = "北京大学青鸟公寓";
				if(ip[2] == 203)
					location = "北京大学理科一号楼/信科";
				if(ip[2] == 204)
					location = "北京大学理科二号楼/计算中心";
				if(ip[2] == 205)
					location = "北京大学中文系";
				if(ip[2] == 206)
					location = "北京大学方正大厦";
				if(ip[2] == 207)
					location = "北京大学北佳/资源公司";
				if(ip[2] == 208)
					location = "北京大学VPN";
				if(ip[2] == 209)
					location = "北京大学畅春园61甲";
				if(ip[2] >= 210 && ip[2] <= 212)
					location = "北京大学万柳1区";
				if(ip[2] >= 213 && ip[2] <= 215)
					location = "北京大学万柳2区";
				if(ip[2] == 216)
					location = "北京大学万柳3区";
				if(ip[2] == 217)
					location = "北京大学畅春园60甲楼";
				if(ip[2] == 218)
					location = "北京大学畅春园63楼";
				if(ip[2] == 219)
					location = "北京大学畅春园64楼";
				if(ip[2] == 220)
					location = "北京大学畅春园64楼";
				if(ip[2] == 221)
					location = "北京大学畅春园65楼";
				if(ip[2] == 222)
					location = "北京大学畅春园65楼";
				if(ip[2] == 223)
					location = "北京大学万柳3区";
				if(ip[2] == 224)
					location = "未知";
				if(ip[2] == 225)
					location = "北京大学38楼5/6楼";
				if(ip[2] == 226)
					location = "北京大学南阁";
				if(ip[2] == 227)
					location = "北京大学海外教育学院";
				if(ip[2] == 228)
					location = "北京大学财务处";
				if(ip[2] == 229)
					location = "北京大学38/39楼地下";
				if(ip[2] == 230)
					location = "北京大学电教楼";
				if(ip[2] == 231)
					location = "北京大学办公楼";
				if(ip[2] == 232)
					location = "北京大学党办/校办/财务处";
				if(ip[2] == 233)
					location = "北京大学39楼1/2楼";
				if(ip[2] == 234)
					location = "北京大学39楼2/3楼";
				if(ip[2] == 235)
					location = "北京大学39楼3/4/5楼";
				if(ip[2] == 236)
					location = "北京大学39楼5/6楼";
				if(ip[2] == 237)
					location = "北京大学38楼1/2楼";
				if(ip[2] == 238)
					location = "北京大学38楼3/4楼";
				if(ip[2] == 239)
					location = "北京大学软件与微电子学院(大兴)";
				if(ip[2] >= 240 && ip[2] <= 242)
					location = "北京大学蓝旗营";
				if(ip[2] == 243)
					location = "北京大学物理学院";
				if(ip[2] == 244)
					location = "北京大学技物楼";
				if(ip[2] >= 245 && ip[2] <= 246)
					location = "北京大学物理学院";
				if(ip[2] == 247)
					location = "北京大学物理学院/电子系/燕东园/出版社";
				if(ip[2] >= 248 && ip[2] <= 250)
					location = "北京大学新生物楼";
				if(ip[2] >= 251 && ip[2] <= 252)
					location = "未知";
				if(ip[2] >= 253 && ip[2] <= 254)
					location = "北京大学交换机";
			}
			if(ip[0] == 202 && ip[1] == 112)
			{
				if(ip[2] == 176)
					location = "北京大学医学部中心楼";
				if(ip[2] == 177)
					location = "北京大学医学部逸夫楼";
				if(ip[2] == 178)
					location = "北京大学医学部公卫学院";
				if(ip[2] == 179)
					location = "北京大学医学部药学院";
				if(ip[2] == 180)
					location = "北京大学医学部基础医学院";
				if(ip[2] == 181)
					location = "北京大学医学部医学图书馆";
				if(ip[2] == 181)
					location = "北京大学医学部医学图书馆";
				if(ip[2] == 181)
					location = "北京大学医学部医学图书馆";
				if(ip[2] == 182)
					location = "北京大学医学部行政楼/后勤楼";
				if(ip[2] == 183)
					location = "北京大学医学部公共卫生学院";
				if(ip[2] == 184)
					location = "北京大学医学部北大第一医院";
				if(ip[2] == 184)
					location = "北京大学医学部肾脏病研究所";
				if(ip[2] == 184)
					location = "北京大学医学部北大第一医院";
				if(ip[2] == 185)
					location = "北京大学医学部卫生部";
				if(ip[2] == 186)
					location = "北京大学医学部北医三院";
				if(ip[2] == 187)
					location = "北京大学医学部口腔医院";
				if(ip[2] == 187)
					location = "北京大学医学部生理楼";
				if(ip[2] == 187)
					location = "北京大学医学部口腔医院";
				if(ip[2] == 188)
					location = "北京大学医学部肿瘤医院";
				if(ip[2] == 188)
					location = "北京大学医学部网络";
				if(ip[2] == 188)
					location = "北京大学医学部肿瘤医院";
				if(ip[2] == 188)
					location = "北京大学医学部中心实验楼";
				if(ip[2] == 188)
					location = "北京大学医学部肿瘤医院";
				if(ip[2] == 188)
					location = "北京大学医学部中心实验楼";
				if(ip[2] == 188)
					location = "北京大学医学部肿瘤医院";
				if(ip[2] >= 189 && ip[2] <= 190)
					location = "北京大学医学部CZ88.NET";
			}
			if(ip[0] == 211 && ip[1] == 71)
			{
				if(ip[2] == 48)
					location = "北京大学博士生楼";
				if(ip[2] == 49)
					location = "北京大学医学部";
				if(ip[2] >= 50 && ip[2] <= 52)
					location = "北京大学医学部3号楼";
				if(ip[2] == 53)
					location = "北京大学医学部5号楼";
				if(ip[2] == 54)
					location = "北京大学医学部2号楼";
				if(ip[2] >= 55 && ip[2] <= 56)
					location = "北京大学医学部";
				if(ip[2] >= 57 && ip[2] <= 58)
					location = "北京大学医学部5号楼";
				if(ip[2] == 58)
					location = "北京大学医学部5号楼529";
				if(ip[2] == 58)
					location = "北京大学医学部5号楼";
				if(ip[2] == 58)
					location = "北京大学5号楼529";
				if(ip[2] == 58)
					location = "北京大学5号楼";
				if(ip[2] == 58)
					location = "北京大学医学部5号楼5层";
				if(ip[2] >= 58 && ip[2] <= 59)
					location = "北京大学5号楼";
				if(ip[2] == 59)
					location = "北京大学医学院5号楼1239";
				if(ip[2] == 59)
					location = "北京大学医学部5号楼";
				if(ip[2] >= 60 && ip[2] <= 61)
					location = "北京大学CZ88.NET";
			}
			if(ip[0] == 211 && ip[1] == 82)
			{
				if(ip[2] >= 64 && ip[2] <= 75)
					location = "北京大学医学部";
				if(ip[2] == 76)
					location = "北京大学医学部3号楼";
			}
			if(ip[0] == 222 && ip[1] == 29)
			{
				if(ip[2] == 18)
					location = "北京大学燕北园";
				if(ip[2] == 19)
					location = "未知";
				if(ip[2] == 20)
					location = "北京大学燕北园";
				if(ip[2] == 21)
					location = "未知";
				if(ip[2] >= 22 && ip[2] <= 24)
					location = "北京大学燕东园";
				if(ip[2] >= 25 && ip[2] <= 27)
					location = "北京大学蔚秀园";
				if(ip[2] >= 28 && ip[2] <= 31)
					location = "北京大学中关园";
				if(ip[2] >= 32 && ip[2] <= 34)
					location = "北京大学40楼";
				if(ip[2] == 35)
					location = "北京大学41楼12楼/42楼";
				if(ip[2] == 36)
					location = "北京大学41楼34楼";
				if(ip[2] == 37)
					location = "北京大学41楼56楼";
				if(ip[2] >= 38 && ip[2] <= 40)
					location = "北京大学42楼";
				if(ip[2] == 41)
					location = "北京大学勺园留学生宿舍";
				if(ip[2] == 42)
					location = "未知";
				if(ip[2] == 43)
					location = "北京大学四教";
				if(ip[2] == 44)
					location = "未知";
				if(ip[2] >= 45 && ip[2] <= 46)
					location = "北京大学化学新南楼";
				if(ip[2] == 47)
					location = "未知";
				if(ip[2] >= 48 && ip[2] <= 51)
					location = "北京大学中关新园";
				if(ip[2] == 52)
					location = "北京大学中关新园4号楼";
				if(ip[2] >= 53 && ip[2] <= 55)
					location = "北京大学中关新园";
				if(ip[2] == 56)
					location = "北京大学中关新园2号楼";
				if(ip[2] >= 57 && ip[2] <= 60)
					location = "未知";
				if(ip[2] == 61)
					location = "北京大学中关新园";
				if(ip[2] == 62)
					location = "北京大学中关新园1号楼";
				if(ip[2] == 63)
					location = "未知";
				if(ip[2] >= 64 && ip[2] <= 65)
					location = "北京大学万柳";
				if(ip[2] >= 66 && ip[2] <= 69)
					location = "北京大学33楼/34B";
				if(ip[2] >= 70 && ip[2] <= 71)
					location = "未知";
				if(ip[2] == 72)
					location = "北京大学校医院";
				if(ip[2] == 73)
					location = "未知";
				if(ip[2] == 74)
					location = "北京大学勺园";
				if(ip[2] >= 75 && ip[2] <= 78)
					location = "北京大学勺园4号楼";
				if(ip[2] == 79)
					location = "北京大学万柳";
				if(ip[2] == 80)
					location = "未知";
				if(ip[2] == 81)
					location = "北京大学47楼1单元";
				if(ip[2] == 82)
					location = "北京大学百年纪念讲堂";
				if(ip[2] >= 83 && ip[2] <= 84)
					location = "未知";
				if(ip[2] == 85)
					location = "北京大学电教";
				if(ip[2] == 86)
					location = "北京大学英杰交流中心/理科二号楼";
				if(ip[2] >= 87 && ip[2] <= 95)
					location = "未知";
				if(ip[2] == 96)
					location = "北京大学教育学院/化学南楼";
				if(ip[2] == 97)
					location = "未知";
				if(ip[2] == 98)
					location = "北京大学理科五号楼";
				if(ip[2] == 99)
					location = "北京大学(廖)凯原楼";
				if(ip[2] >= 100 && ip[2] <= 101)
					location = "未知";
				if(ip[2] == 102)
					location = "北京大学万柳";
				if(ip[2] == 103)
					location = "未知";
				if(ip[2] == 104)
					location = "北京大学讲堂地下室咖啡厅";
				if(ip[2] == 105)
					location = "北京大学理教/未名湖";
				if(ip[2] >= 106 && ip[2] <= 107)
					location = "未知";
				if(ip[2] == 108)
					location = "北京大学哲学楼/国关/理教有线";
				if(ip[2] >= 109 && ip[2] <= 116)
					location = "未知";
				if(ip[2] == 117)
					location = "北京大学光华楼/未名湖";
				if(ip[2] >= 118 && ip[2] <= 120)
					location = "未知";
				if(ip[2] == 121)
					location = "北京大学理科二号楼地下室";
				if(ip[2] == 122)
					location = "北京大学33/34B";
				if(ip[2] >= 123 && ip[2] <= 125)
					location = "未知";
				if(ip[2] == 126)
					location = "北京大学38/39楼";
				if(ip[2] >= 127 && ip[2] <= 128)
					location = "未知";
				if(ip[2] == 129)
					location = "北京大学未名湖";
				if(ip[2] == 130)
					location = "未知";
				if(ip[2] == 131)
					location = "北京大学昌平校区1号楼";
				if(ip[2] >= 132 && ip[2] <= 133)
					location = "未知";
				if(ip[2] == 134)
					location = "北京大学昌平校区2号楼";
				if(ip[2] >= 135 && ip[2] <= 138)
					location = "北京大学昌平校区";
				if(ip[2] == 138)
					location = "北京大学昌平校区1号楼";
				if(ip[2] == 138)
					location = "北京大学昌平校区2号楼";
				if(ip[2] == 138)
					location = "北京大学昌平校区3号楼";
				if(ip[2] == 138)
					location = "北京大学昌平校区4号楼";
				if(ip[2] == 138)
					location = "北京大学圆明园校区";
				if(ip[2] >= 138 && ip[2] <= 142)
					location = "北京大学昌平校区";
				if(ip[2] >= 143 && ip[2] <= 145)
					location = "北京大学圆明园校区";
				if(ip[2] == 146)
					location = "北京大学圆明园校区6号楼";
				if(ip[2] == 147)
					location = "北京大学圆明园校区9号楼";
				if(ip[2] >= 148 && ip[2] <= 149)
					location = "北京大学圆明园校区6号楼";
				if(ip[2] == 150)
					location = "北京大学圆明园校区";
				if(ip[2] >= 151 && ip[2] <= 156)
					location = "未知";
				if(ip[2] == 157)
					location = "北京大学方正大厦";
			}
			
			TextView place_name = (TextView)findViewById(R.id.place_name);
			place_name.setText("在" + location);
			
			//调用Places.Create, 得到所在地点的places.id
			access_token = (String)data.getSerializable("access_token");
			
			parameters.put("v", version);
			parameters.put("access_token", access_token);
			parameters.put("method", method);
			parameters.put("format", format);
			parameters.put("name", location);
			parameters.put("address", address);
			parameters.put("poi_id", location);
			parameters.put("longitude", "+116.3110");
			parameters.put("latitude", "+39.9899");
			sig = Signature.getSignature(parameters, secret_key);

			
			
			HttpPost postmethod = new HttpPost(url);
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);  

			nameValuePairs.add(new BasicNameValuePair("v", version));  
			nameValuePairs.add(new BasicNameValuePair("access_token", access_token));  
			nameValuePairs.add(new BasicNameValuePair("method", method));  
			nameValuePairs.add(new BasicNameValuePair("format", format));  
			nameValuePairs.add(new BasicNameValuePair("name", location));
			nameValuePairs.add(new BasicNameValuePair("address", address));
			nameValuePairs.add(new BasicNameValuePair("poi_id", location));
			nameValuePairs.add(new BasicNameValuePair("longitude", "+116.3110"));
			nameValuePairs.add(new BasicNameValuePair("latitude", "+39.9899"));
			nameValuePairs.add(new BasicNameValuePair("sig", sig));
			

			try{
			postmethod.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			
//			postmethod.addParameter("v", version);
//			postmethod.addParameter("access_token", access_token);
//			postmethod.addParameter("method", method);
//			postmethod.addParameter("format", format);
//			postmethod.addParameter("sig", sig);
			
			HttpClient client = new DefaultHttpClient();
			String result ="";
			try
			{
				HttpResponse response = client.execute(postmethod);
				result = EntityUtils.toString(response.getEntity());
				//JSONArray placeArray = new JSONArray(result);
				JSONObject placeObject = new JSONObject(result);
				place_id = placeObject.getString("place");
				place_id = place_id.substring(place_id.indexOf(":")+2 ,place_id.lastIndexOf("\""));
			}
			catch(Exception e)
			{
			}
				Button btnpub = (Button)findViewById(R.id.statusSend);
				btnpub.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {

		
				HashMap<String, String> parameters2 = new HashMap<String, String>();
				parameters2.put("v", version);
				parameters2.put("access_token", access_token);
				method = "status.set";
				
				EditText et = (EditText)findViewById(R.id.statusContent);
				String status = et.getText().toString();
				if (status.length() > 0)
				{
				parameters2.put("status", status);
				parameters2.put("method", method);
				parameters2.put("format", format);
				parameters2.put("place_id", place_id);
				sig = Signature.getSignature(parameters2, secret_key);

				
				//String url = "http://api.renren.com/restserver.do";
				HttpPost postmethod2 = new HttpPost(url);
				List<NameValuePair> nameValuePairs2 = new ArrayList<NameValuePair>(2);  

				nameValuePairs2.add(new BasicNameValuePair("v", version));  
				nameValuePairs2.add(new BasicNameValuePair("access_token", access_token));  
				nameValuePairs2.add(new BasicNameValuePair("method", method));  
				nameValuePairs2.add(new BasicNameValuePair("format", format));  
				nameValuePairs2.add(new BasicNameValuePair("status", status));
				nameValuePairs2.add(new BasicNameValuePair("place_id", place_id));
				nameValuePairs2.add(new BasicNameValuePair("sig", sig));
				
				try{
				postmethod2.setEntity(new UrlEncodedFormEntity(nameValuePairs2, "UTF-8"));
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
				
//				postmethod.addParameter("v", version);
//				postmethod.addParameter("access_token", access_token);
//				postmethod.addParameter("method", method);
//				postmethod.addParameter("format", format);
//				postmethod.addParameter("sig", sig);
				
				HttpClient client2 = new DefaultHttpClient();
				String result2 ="";
				try
				{
					HttpResponse response2 = client2.execute(postmethod2);
					result2 = EntityUtils.toString(response2.getEntity());
					//if (result2.equals("\"result\" : 1"))
					//{
						Toast.makeText(getBaseContext(), "发送成功",Toast.LENGTH_SHORT).show();
//					}
//					else
//					{
//						Toast.makeText(getBaseContext(), "发送失败请重试",Toast.LENGTH_SHORT).show();
//					}
					//result2 = result2 + "";
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
				}
				else
				{
					Toast.makeText(getBaseContext(), "请输入状态",Toast.LENGTH_SHORT).show();
				}
			}});
				
		
	 }

}
