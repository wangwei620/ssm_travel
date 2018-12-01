package com.itheima.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 写一个MD5算法,运行结果与MySQL的md5()函数相同
 * 将明文密码转成MD5密码
 * 123456->e10adc3949ba59abbe56e057f20f883e
 */
public class MD5Utils {
	/**
	 * 将明文密码转成MD5密码 
	 */
	public static String encodeByMd5(String text) throws Exception{
		byte[] secretBytes = null;
		try {
			//Java中MessageDigest类封装了MD5算法
			secretBytes = MessageDigest.getInstance("md5").digest(
					text.getBytes());
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("没有md5这个算法！");
		}
		String md5code = new BigInteger(1, secretBytes).toString(16);// 16进制数字
		// 如果生成数字未满32位，需要前面补0
		for (int i = 0; i < 32 - md5code.length(); i++) {
			md5code = "0" + md5code;
		}
		return md5code;
	}
	
	/**
	 * 测试
	 */
	public static void main(String[] args) throws Exception{
		String password = "123456";
		String passwordMD5 = MD5Utils.encodeByMd5(password);
		System.out.println(password);
		System.out.println(passwordMD5);
	}
}
