package com.example.app15_weather;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class ConnectResHelper {
	public static String getSrc(String path) throws IOException{
		URL url=new URL(path);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setConnectTimeout(5000);
		InputStream is = conn.getInputStream();
		ByteArrayOutputStream baos=new ByteArrayOutputStream();
		byte[] buf=new byte[2048];
		int len=-1;
		while((len=is.read(buf))!=-1){
			baos.write(buf, 0, len);
		}
		is.close();
		String result = baos.toString();
		baos.close();
		return result;
	}
	public static Bitmap getImg(String path) throws IOException{
		URL url=new URL(path);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setConnectTimeout(5000);
		InputStream is = conn.getInputStream();
		Bitmap bm=BitmapFactory.decodeStream(is);
		return bm;
	}
}
