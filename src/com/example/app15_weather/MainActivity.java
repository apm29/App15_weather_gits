package com.example.app15_weather;

import java.net.URLEncoder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.app15_weather.bean.Weather;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.ProgressDialog;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
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
	ListView lv;
	JSONArray forecast = null;
	private final int SUCCESS = 1;
	private final int FAIL = 2;
	private final int WRONGCITY = 3;

	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SUCCESS:
				tv.setText(msg.obj.toString());
				// 显示在listview
				Log.e("success", "s");
				forecast = (JSONArray) msg.obj;
				break;
			case FAIL:
				Toast.makeText(MainActivity.this, "fail1", 0).show();
				Log.e("success", "s1");
				tv.setText(msg.obj.toString());
				break;
			case WRONGCITY:
				Toast.makeText(MainActivity.this, "fail2", 0).show();
				tv.setText(msg.obj.toString());
				Log.e("success", "s2");
				break;
			}
			layout.setVisibility(View.INVISIBLE);
			tv.setVisibility(View.INVISIBLE);
			lv.setAdapter(new MyAdapter());
		}

	};

	class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return forecast.length();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			if (convertView == null) {
				// 生成view
				convertView = (LinearLayout) View.inflate(MainActivity.this,
						R.layout.listitem, null);
			}
			JSONObject item = null;
			try {
				item = (JSONObject) forecast.get(position);
				// 天
				TextView tv_sub1 = (TextView) convertView
						.findViewById(R.id.tv_sub1);
				tv_sub1.setText(item.getString("date"));
				// 天气信息
				TextView tv_sub2 = (TextView) convertView
						.findViewById(R.id.tv_sub2);
				tv_sub2.setText(item.getString("fengli")
						+ item.getString("fengxiang") + item.getString("low")
						+ item.getString("high") + item.getString("type"));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return convertView;
		}

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		btn = (Button) findViewById(R.id.button1);
		edt = (EditText) findViewById(R.id.editText1);
		tv = (TextView) findViewById(R.id.textView1);
		pb = (ProgressBar) findViewById(R.id.pb);
		layout = (LinearLayout) findViewById(R.id.layout);
		btn.setOnClickListener(this);
		lv = (ListView) findViewById(R.id.listView1);

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
						Log.e("success", src);
						JSONObject result = new JSONObject(src);
						Log.e("success", result.toString());
						if (result.getString("desc").equals("OK")) {

							JSONArray jsonArray = result.getJSONObject("data")
									.getJSONArray("forecast");
							Log.e("success", jsonArray.toString());
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
						// lv.setAdapter(new MyAdapter());
						Log.e("success", "111");
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
