package com.renren.yourrenren;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class Signature {

	  public static String getSignature(Map<String, String> paramMap, String secret) {
			List<String> paramList = new ArrayList<String>(paramMap.size());
			//1、参数格式化
			for(Map.Entry<String,String> param:paramMap.entrySet()){
				paramList.add(param.getKey()+"="+param.getValue());
			}
			//2、排序并拼接成一个字符串
			Collections.sort(paramList);
			StringBuffer buffer = new StringBuffer();
			for (String param : paramList) {
				buffer.append(param);
			} 
			//3、追加script key
			buffer.append(secret);
			//4、将拼好的字符串转成MD5值
			try {
			    java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
			    StringBuffer result = new StringBuffer();
			    try {
			        for (byte b : md.digest(buffer.toString().getBytes("UTF-8"))) {
			            result.append(Integer.toHexString((b & 0xf0) >>> 4));
			            result.append(Integer.toHexString(b & 0x0f));
			        }
			    } catch (UnsupportedEncodingException e) {
			        for (byte b : md.digest(buffer.toString().getBytes())) {
			            result.append(Integer.toHexString((b & 0xf0) >>> 4));
			            result.append(Integer.toHexString(b & 0x0f));
			        }
			    }
			    return result.toString();
			} catch (java.security.NoSuchAlgorithmException ex) {
			    ex.printStackTrace();
			}
			return null;
		}
}
