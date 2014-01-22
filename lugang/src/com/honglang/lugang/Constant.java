package com.honglang.lugang;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Constant {

	public static final String NAMESPACE = "http://www.6gang.com.cn/";
	public static final String SERVICE_URL = "http://service.6gang.com.cn/DataServer.asmx";
	
	/*校验手机号码*/
	public static boolean isMobileNO(String mobiles) {
        Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }
	
}
