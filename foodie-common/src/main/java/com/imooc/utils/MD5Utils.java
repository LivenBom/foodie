package com.imooc.utils;

import org.apache.commons.codec.binary.Base64;
import java.security.MessageDigest;

public class MD5Utils {

    /**
     * 使用Base64编码的MD5加密（用于普通用户密码）
     */
    public static String getMD5Str(String strValue) throws Exception {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        String newstr = Base64.encodeBase64String(md5.digest(strValue.getBytes()));
        return newstr;
    }

    /**
     * 使用普通MD5加密（用于管理员密码）
     */
    public static String getMD5StrAdmin(String strValue) throws Exception {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        byte[] bytes = md5.digest(strValue.getBytes());
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(b & 0xff);
            if (hex.length() == 1) {
                sb.append("0");
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        try {
            String md5 = getMD5StrAdmin("admin123");
            System.out.println("Admin MD5: " + md5);  // 应该输出: 0192023a7bbd73250516f069df18b500
            
            String userMd5 = getMD5Str("test123");
            System.out.println("User MD5: " + userMd5);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
