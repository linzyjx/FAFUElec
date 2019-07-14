package com.fafu.app.dfb.util;

public class CookieUtils {
    public static String getCookie() {
        return getACookie("ASP.NET_SessionId") +
                getACookie("imeiticket") +
                getACookie("hallticket") +
                getACookie("username") +
                getACookie("sourcetypeticket");
    }

    private static String getACookie(String name) {
        String value = SPUtils.getString(name);
        if (!value.isEmpty()) {
           return name + "=" + value + "; ";
        } else {
            return "";
        }
    }

    public static void clearCookie() {
        SPUtils.remove("ASP.NET_SessionId");
        SPUtils.remove("imeiticket");
        SPUtils.remove("hallticket");
        SPUtils.remove("username");
        SPUtils.remove("sourcetypeticket");
    }
}
