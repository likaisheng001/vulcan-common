package com.vulcan.common.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * MD5签名处理核心文件
 * */

public class MD5Util {
    
    private static MessageDigest md;
    
    /**
     * 签名字符串
     *
     * @param text
     *            需要签名的字符串
     * @param key
     *            密钥
     * @param input_charset
     *            编码格式
     * @return 签名结果
     */
    public static String sign(String text, String key, String charset) throws Exception {
        text = text + key;
        return DigestUtils.md5Hex(getContentBytes(text, charset));
        
    }

    /**
     * 签名字符串
     *
     * @param text
     *            需要签名的字符串
     * @param sign
     *            签名结果
     * @param key
     *            密钥
     * @param input_charset
     *            编码格式
     * @return 签名结果
     */
    public static boolean verify(String text, String sign, String key, String charset) throws Exception {
        text = text + key;
        String mysign = DigestUtils.md5Hex(getContentBytes(text, charset));
        if (mysign.equals(sign)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * @param content
     * @param charset
     * @return
     * @throws SignatureException
     * @throws UnsupportedEncodingException
     */
    private static byte[] getContentBytes(String content, String charset) {
        if (charset == null || "".equals(charset)) {
            return content.getBytes();
        }
        try {
            return content.getBytes(charset);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("签名过程中出现错误,指定的编码集不对,您目前指定的编码集是:" + charset);
        }
    }
    
    /**
     * 通联签名
     * @param b
     * @return
     */
    public static String computeDigest(byte[] b) {
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return new String(b);
        }
        md.reset();
        md.update(b);
        byte[] hash = md.digest();
        StringBuffer outStrBuf = new StringBuffer(32);
        for (int i = 0; i < hash.length; i++) {
            int v = hash[i] & 0xFF;
            if (v < 16) {
                outStrBuf.append('0');
            }
            outStrBuf.append(Integer.toString(v, 16).toLowerCase());
        }
        return outStrBuf.toString();
    }
    
    public static void main(String[] args) {
        try {
            String key = "yongda123";
            String str = "asdc1234";
            String strSign = sign(str,key,"UTF-8");
            System.out.println("加签结果："+strSign);
            
            System.out.println(verify(str,strSign,key,"UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}