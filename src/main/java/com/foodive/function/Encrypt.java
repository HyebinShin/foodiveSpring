package com.foodive.function;

import lombok.extern.log4j.Log4j;

import java.security.MessageDigest;

@Log4j
public class Encrypt {

    private static final String salt = "ewq43ea2";

    public static String getEncryptPassword(String password) {
        return getEncrypt(password);
    }

    private static String getEncrypt(String pw) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            md.update((pw+ Encrypt.salt).getBytes());
            byte[] pwSalt = md.digest();

            StringBuilder sb = new StringBuilder();
            for (byte ps : pwSalt) {
                sb.append(String.format("%02x", ps));
            }

            return sb.toString();
        } catch (Exception e) {
            log.info("getEncrypt ERROR..."+e.getMessage());
            return null;
        }
    }
}
