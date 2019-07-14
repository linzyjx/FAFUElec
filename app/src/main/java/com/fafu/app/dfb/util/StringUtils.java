package com.fafu.app.dfb.util;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import com.fafu.app.dfb.DFBao;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import permissions.dispatcher.NeedsPermission;

public class StringUtils {

    @SuppressLint({"MissingPermission", "HardwareIds"})
    @NeedsPermission(Manifest.permission.READ_PHONE_STATE)
    public static String imei() {
        String str;
        TelephonyManager telephonyManager = (TelephonyManager) DFBao.getContext().getSystemService(Context.TELEPHONY_SERVICE);
        try {
            if (telephonyManager.getDeviceId() != null) {
                str = telephonyManager.getDeviceId();
            } else {
                str = Settings.Secure.getString(DFBao.getContext().getApplicationContext().getContentResolver(), "android_id");
            }
        } catch(Exception e) {
            str = Settings.Secure.getString(DFBao.getContext().getApplicationContext().getContentResolver(), "android_id");
        }
        return md5(str);
    }

    private static String md5(String str) {
        try {
            return tc(MessageDigest.getInstance("MD5").digest(str.getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException unused) {
            return "";
        }
    }

    private static String tc(byte[] bArr) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bArr) {
            String hexString = Integer.toHexString(255 & b);
            if (hexString.length() == 1) {
                sb.append('0');
            }
            sb.append(hexString);
        }
        return sb.toString();
    }

}
