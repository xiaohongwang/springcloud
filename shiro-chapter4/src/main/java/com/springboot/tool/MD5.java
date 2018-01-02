package com.springboot.tool;

import org.apache.shiro.crypto.hash.Md5Hash;

public class MD5 {
	public static void main(String[] args) {
		
		/*source  明文密码
		 *salt    盐   作料自己定义
		 *hashIterations  哈希次数
		 */
		//Md5Hash md5Hash = new Md5Hash("xiaohong","xiaoxiao", 3);
		MD5.getMd5Hash("xiaoxiao","xiaoxiao");
		//System.out.println(md5Hash);
	}
	
	
	public static String getMd5Hash(String userName,String password){
		
		Md5Hash md5Hash = new Md5Hash(password,userName, 3);
		return md5Hash.toString();
	}
}
