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

public class Constant {

	public static final String NAMESPACE = "http://www.6gang.com.cn/";
	public static final String SERVICE_URL = "http://service.6gang.com.cn/DataServer.asmx";
	public static final String loginAction = "Login";
	
	/*校验手机号码*/
	public static boolean isMobileNO(String mobiles) {
        Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }
	
	public static String login(String userNo, String pass){
		//指定WebService的命名空间和调用方法
		SoapObject request = new SoapObject(Constant.NAMESPACE, loginAction);
		//设置需要调用WebService接口的参数
		request.addProperty("userNO", userNo);
		request.addProperty("pass", pass);
		//获得序列化的Envelope
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
	
}
