package com.CPT202.cpt202.utils;


import org.springframework.stereotype.Component;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Component  // Add @Component to make it a Spring-managed bean
public class SecurityUtil {

    public static String md5(String nickname, String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update((nickname + password).getBytes());  // 使用昵称和密码拼接进行加密
        byte[] byteData = md.digest();

        StringBuilder hexString = new StringBuilder();
        for (byte byteItem : byteData) {
            String hex = Integer.toHexString(0xff & byteItem);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }


}
