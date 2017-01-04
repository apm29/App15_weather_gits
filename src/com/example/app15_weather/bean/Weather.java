package com.example.app15_weather.bean;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Weather {
	public static void main(String[] args) {
		System.out.println(11);
	}
	public Weather(JSONObject json) throws JSONException{
		aqi=json.getString("aqi");
		city=json.getString("city");
		city=json.getString("city");
		status=json.getInt("status");
		manmao=json.getString("ganmao");
		wendu=json.getString("wendu");
		JSONArray arr=json.getJSONArray("forcast");
		for(int i=0;i<arr.length();i++){
			JSONObject day=(JSONObject) arr.get(i);
			Weather.DayInfo info=new DayInfo();
			info.date=(String) arr.get(0);
			info.fengli=(String) arr.get(1);
			info.fengxiang=(String) arr.get(2);
			info.high=(String) arr.get(3);
			info.low=(String) arr.get(4);
			info.type=(String) arr.get(5);
		}
	}
	public String aqi;

	public String city;

	public ArrayList<DayInfo> forcast;

	public static class DayInfo {
		String date;
		String fengli;
		String fengxiang;
		String high;
		String low;
		String type;
	}

	public String desc;
	public int status;
	public String manmao;
	public String wendu;
	
}
