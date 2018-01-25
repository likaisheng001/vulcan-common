package com.vulcan.common.util;

import java.security.MessageDigest;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;



/** 
 * 订单组签名或验证签名方法 
 */
public class EncryptUtil {
	/**
	 * 创建md5摘要,规则是:按参数名称a-z排序,遇到空值的参数不参加签名。
	* @param key 签名key
	* @param parameters SortedMap表示的待签名键值对
	* @param charset String表示的字符编码
	* @return 返回签名
	 */
	@SuppressWarnings("rawtypes")
	public static String createSign(String key,SortedMap parameters, String charset) {
		StringBuffer sb = new StringBuffer();
		Set es = parameters.entrySet();
		Iterator it = es.iterator();
		while(it.hasNext()) {
			Map.Entry entry = (Map.Entry)it.next();
			String k = (String)entry.getKey();
			String v = (String)entry.getValue();
			if(null != v && !"".equals(v) 
					&& !"sign".equals(k) && !"key".equals(k)) {
				sb.append(k + "=" + v + "&");
			}
		}
		sb.append("key=" + key);
		String sign = mD5Encode(sb.toString(), charset).toLowerCase();	
		return sign;			
	}
	
	/**
	 * 创建md5摘要,规则是:按参数名称a-z排序,遇到空值的参数不参加签名。
	* @param key 签名app_key
	* @param parameters SortedMap表示的待签名键值对
	* @param secret 认证
	* @param charset String表示的字符编码
	* @return 返回签名
	 */
	@SuppressWarnings("rawtypes")
	public static String createSignOFdangdang(String key,SortedMap parameters,String secret, String charset) {
		StringBuffer sb = new StringBuffer();
		sb.append(secret);
		Set es = parameters.entrySet();
		Iterator it = es.iterator();
		while(it.hasNext()) {
			Map.Entry entry = (Map.Entry)it.next();
			String k = (String)entry.getKey();
			String v = (String)entry.getValue();
			if(null != v && !"".equals(v) 
					&& !"sign".equals(k)) {
				sb.append(k + v);
			}
		}
		sb.append(secret);
		String sign = mD5Encode(sb.toString(), charset).toUpperCase();	
		return sign;			
	}
	/**
	 * 创建md5摘要,规则是:按参数名称a-z排序,遇到空值的参数不参加签名。
	 * @param parameters SortedMap表示的待签名键值对
	 * @param secret 认证
	 * @param charset String表示的字符编码
	 * @return 返回签名
	 */
	@SuppressWarnings("rawtypes")
	public static String createSignOFxiachufang(SortedMap parameters,String secret, String charset) {
		StringBuffer sb = new StringBuffer();
		Set es = parameters.entrySet();
		Iterator it = es.iterator();
		while(it.hasNext()) {
			Map.Entry entry = (Map.Entry)it.next();
			String k = (String)entry.getKey();
			String v = (String)entry.getValue();
			if(null != v ) {
					sb.append(k + v);
			}
		}
		sb.append(secret);
		String sign = mD5Encode(sb.toString(), charset);
		return sign;			
	}
	private static String byteArrayToHexString(byte b[]) {
		StringBuffer resultSb = new StringBuffer();
		for (int i = 0; i < b.length; i++)
			resultSb.append(byteToHexString(b[i]));

		return resultSb.toString();
	}

	private static String byteToHexString(byte b) {
		int n = b;
		if (n < 0)
			n += 256;
		int d1 = n / 16;
		int d2 = n % 16;
		return hexDigits[d1] + hexDigits[d2];
	}

	public static String mD5Encode(String origin, String charset) {
		String resultString = null;
		try {
			resultString = new String(origin);
			MessageDigest md = MessageDigest.getInstance("MD5");
			if (charset == null)
				resultString = byteArrayToHexString(md.digest(resultString
						.getBytes()));
			else
				resultString = byteArrayToHexString(md.digest(resultString
						.getBytes(charset)));
		} catch (Exception exception) {
		}
		return resultString;
	}
	
	private static final String hexDigits[] = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };
	
//	@Test
//	public void test01(){
//		String str = "我";
//
//		String mD5Encode = mD5Encode(str, "utf-8");
//		System.out.println("--------:" + mD5Encode);
//	}
}
