package info.xiantang.jerrymouse.http.utils;

import info.xiantang.jerrymouse.http.session.Cookies;

/**
 * @Author: xiantang
 * @Date: 2020/4/26 22:35
 */
public class CookiesUtils {
    public static Cookies parse(String rawCookie) {
        String[] cookieArray = rawCookie.split("; ");
        Cookies cookies = new Cookies();
        for (int i = 0; i < cookieArray.length; i++) {
            String cookie = cookieArray[i];
            String[] c = cookie.split("=");
            cookies.put(c[0], c[1]);
        }
        return cookies;
    }
}
