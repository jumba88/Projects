package com.honglang.lugang;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpResponseException;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.ListAdapter;
import android.widget.ListView;

public class Constant {

	public static final String NAMESPACE = "http://www.6gang.com.cn/";
//	public static final String SERVICE_URL = "http://service.6gang.com.cn/DataServer.asmx";
	public static final String SERVICE_URL = "http://6gang.gnway.cc:9898/DataServer.asmx";
//	public static final String SERVICE_URL = "http://192.168.1.168:84/DataServer.asmx";
	public static final String loginAction = "Login";
	
	/*
	 * verify mobile number
	 */
	public static boolean isMobileNO(String mobiles) {
        Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }
	/*
	 * verify number
	 */
	public static boolean isNum(String str){
		if (str == null || str.equals("")) {
			return false;
		}
		return str.matches("^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$");
	}
	
	public static String login(String userNo, String pass){
		SoapObject request = new SoapObject(Constant.NAMESPACE, loginAction);
		request.addProperty("userNO", userNo);
		request.addProperty("pass", pass);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
		HttpTransportSE transport = new HttpTransportSE(Constant.SERVICE_URL);
		transport.debug = true;
		envelope.dotNet = true;
		envelope.setOutputSoapObject(request);
		
		try {
			transport.call(Constant.NAMESPACE + loginAction, envelope);
			SoapObject obj = (SoapObject) envelope.bodyIn;
			if(obj != null){
				return obj.getProperty("LoginResult").toString();
			}else{
				return null;
			}
			
		} catch (HttpResponseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		}
		return null;
	}
	/** 解决scrollview与listview共存 */
	public static void setListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			return;
		}

		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount()));
		((MarginLayoutParams) params).setMargins(0, 0, 0, 0);
		listView.setLayoutParams(params);
	}
	
}
