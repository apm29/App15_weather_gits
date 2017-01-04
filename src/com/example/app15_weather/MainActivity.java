package com.example.app15_weather;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.ProgressDialog;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {
	String path = "http://wthrcdn.etouch.cn/weather_mini?city=";
	Button btn;
	EditText edt;
	TextView tv;
	ProgressDialog pd;
	ProgressBar pb;
	LinearLayout layout;
	private final int SUCCESS = 1;
	private final int FAIL = 2;
	private final int WRONGCITY = 3;

	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SUCCESS:
				tv.setText(msg.obj.toString());
				break;
			case FAIL:
				Toast.makeText(MainActivity.this, "fail1", 0).show();
				tv.setText(msg.obj.toString());
				break;
			case WRONGCITY:
				Toast.makeText(MainActivity.this, "fail2", 0).show();
				tv.setText(msg.obj.toString());
				break;
			}
			layout.setVisibility(View.INVISIBLE);
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		btn = (Button) findViewById(R.id.button1);
		edt = (EditText) findViewById(R.id.editText1);
		tv = (TextView) findViewById(R.id.textView1);
		pb=(ProgressBar)findViewById(R.id.pb);
		layout =(LinearLayout)findViewById(R.id.layout);
		btn.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		layout.setVisibility(View.VISIBLE);

		final String city = edt.getText().toString().trim();
		if (city != null && city != "") {
			new Thread() {
				
				public void run() {
					try {
						path = "http://wthrcdn.etouch.cn/weather_mini?city=";
						String encode = URLEncoder.encode(city, "utf-8");
						path += encode;
						// path =
						// "http://wthrcdn.etouch.cn/weather_mini?city=%E6%9D%AD%E5%B7%9E";
						Log.e("success", path);
						String src = ConnectResHelper.getSrc(path);
						JSONObject result = new JSONObject(src);

						if (result.getString("desc").equals("OK")) {

							JSONArray jsonArray = result.getJSONObject("data")
									.getJSONArray("forecast");
							Message msg = Message.obtain();
							msg.obj = jsonArray;
							msg.what = SUCCESS;
							handler.sendMessage(msg);

						} else {
							Message msg = Message.obtain();
							msg.obj = "查无此城";
							msg.what = WRONGCITY;
							handler.sendMessage(msg);
						}
					} catch (Exception e) {
						e.printStackTrace();
						Message msg = Message.obtain();
						msg.obj = "连接失败";
						msg.what = FAIL;
						handler.sendMessage(msg);

					}
				}
			}.start();
		} else {
			Toast.makeText(this, "没有输入城市", 0).show();
			return;
		}

	}

}
