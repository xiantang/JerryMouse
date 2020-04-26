package info.xiantang.jerrymouse.http.utils;

import info.xiantang.jerrymouse.http.session.Cookies;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @Author: xiantang
 * @Date: 2020/4/26 22:33
 */
public class CookiesTest {

    @Test
    public void testCanParseCookies() {
        String rawCookie = "yummy_cookie=choco; tasty_cookie=strawberry; SESSION_ID=SDFSDSDSGSFSGSDCSDC";
        Cookies cookies = CookiesUtils.parse(rawCookie);
        Cookies expect = new Cookies();
        expect.put("yummy_cookie", "choco");
        expect.put("tasty_cookie", "strawberry");
        expect.put("SESSION_ID", "SDFSDSDSGSFSGSDCSDC");
        assertEquals(expect, cookies);
    }

}
