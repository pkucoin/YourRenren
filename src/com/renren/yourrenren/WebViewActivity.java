package com.renren.yourrenren;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebViewActivity extends Activity{
	/** Called when the activity is first created. */
	public static WebView webview;
	String url1=""; 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview);
        webview = (WebView) findViewById(R.id.webviewid);
        webview.setWebViewClient(new WebViewClient()
		{
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url)
			{ // 重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
				view.loadUrl(url);
				return true;
			}
			public void onPageFinished(WebView view, String url)
			{
				url1 = webview.getUrl();
				int pos;
				if(url1 != null)
				{
					Log.v("url1", url1);
					if(url1.contains("access_token="))
					{
						Log.v("debug", "token returned!");
						pos = url1.indexOf("access_token=") + 13;
						String access_token = url1.substring(pos, url1.indexOf("&"));
						access_token = java.net.URLDecoder.decode(access_token);
						Log.v("access_token", access_token);
						Intent intent=new Intent(WebViewActivity.this, UIActivity.class);
						Bundle bundle = new Bundle();
						bundle.putString("access_token", access_token);
						intent.putExtras(bundle);
						startActivity(intent);
					}
				}
			}
		});
        webview.loadUrl("https://graph.renren.com/oauth/authorize?client_id=14b42a4c65de42778677f50c6f9facc7" +
        		"&redirect_uri=http://graph.renren.com/oauth/login_success.html" +
        		"&response_type=token"+ 
        		"&scope=read_user_status+status_update+read_user_album+publish_feed+read_user_feed+read_user_share+read_user_comment" +
        		"&display=mobile");
    }

}
