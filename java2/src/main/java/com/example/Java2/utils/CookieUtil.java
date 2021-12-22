//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.example.Java2.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

public final class CookieUtil {
    public CookieUtil() {
    }

    public static String getCookieValue(HttpServletRequest request, String cookieName) {
        return getCookieValue(request, cookieName, false);
    }

    public static String getCookieValue(HttpServletRequest request, String cookieName, boolean isDecoder) {
        Cookie[] cookieList = request.getCookies();
        if (cookieList != null && cookieName != null) {
            String retValue = null;

            try {
                for(int i = 0; i < cookieList.length; ++i) {
                    if (cookieList[i].getName().equals(cookieName)) {
                        if (isDecoder) {
                            retValue = URLDecoder.decode(cookieList[i].getValue(), "UTF-8");
                        } else {
                            retValue = cookieList[i].getValue();
                        }
                        break;
                    }
                }
            } catch (UnsupportedEncodingException var6) {
                var6.printStackTrace();
            }

            return retValue;
        } else {
            return null;
        }
    }

    public static String getCookieValue(HttpServletRequest request, String cookieName, String encodeString) {
        Cookie[] cookieList = request.getCookies();
        if (cookieList != null && cookieName != null) {
            String retValue = null;

            try {
                for(int i = 0; i < cookieList.length; ++i) {
                    if (cookieList[i].getName().equals(cookieName)) {
                        retValue = URLDecoder.decode(cookieList[i].getValue(), encodeString);
                        break;
                    }
                }
            } catch (UnsupportedEncodingException var6) {
                var6.printStackTrace();
            }

            return retValue;
        } else {
            return null;
        }
    }

    public static void setCookie(HttpServletRequest request, HttpServletResponse response, String cookieName, String cookieValue) {
        setCookie(request, response, cookieName, cookieValue, -1);
    }

    public static void setCookie(HttpServletRequest request, HttpServletResponse response, String cookieName, String cookieValue, int cookieMaxage) {
        setCookie(request, response, cookieName, cookieValue, cookieMaxage, false);
    }

    public static void setCookie(HttpServletRequest request, HttpServletResponse response, String cookieName, String cookieValue, boolean isEncode) {
        setCookie(request, response, cookieName, cookieValue, -1, isEncode);
    }

    public static void setCookie(HttpServletRequest request, HttpServletResponse response, String cookieName, String cookieValue, int cookieMaxage, boolean isEncode) {
        doSetCookie(request, response, cookieName, cookieValue, cookieMaxage, isEncode);
    }

    public static void setCookie(HttpServletRequest request, HttpServletResponse response, String cookieName, String cookieValue, int cookieMaxage, String encodeString) {
        doSetCookie(request, response, cookieName, cookieValue, cookieMaxage, encodeString);
    }

    public static void deleteCookie(HttpServletRequest request, HttpServletResponse response, String cookieName) {
        doSetCookie(request, response, cookieName, "", -1, false);
    }

    private static final void doSetCookie(HttpServletRequest request, HttpServletResponse response, String cookieName, String cookieValue, int cookieMaxage, boolean isEncode) {
        try {
            if (cookieValue == null) {
                cookieValue = "";
            } else if (isEncode) {
                cookieValue = URLEncoder.encode(cookieValue, "utf-8");
            }

            Cookie cookie = new Cookie(cookieName, cookieValue);
            if (cookieMaxage > 0) {
                cookie.setMaxAge(cookieMaxage);
            }

            cookie.setPath("/");
            response.addCookie(cookie);
        } catch (Exception var7) {
            var7.printStackTrace();
        }

    }

    private static final void doSetCookie(HttpServletRequest request, HttpServletResponse response, String cookieName, String cookieValue, int cookieMaxage, String encodeString) {
        try {
            if (cookieValue == null) {
                cookieValue = "";
            } else {
                cookieValue = URLEncoder.encode(cookieValue, encodeString);
            }

            Cookie cookie = new Cookie(cookieName, cookieValue);
            if (cookieMaxage > 0) {
                cookie.setMaxAge(cookieMaxage);
            }

            if (null != request) {
                String domainName = getDomainName(request);
                System.out.println(domainName);
                if (!"localhost".equals(domainName)) {
                    cookie.setDomain(domainName);
                }
            }

            cookie.setPath("/");
            response.addCookie(cookie);
        } catch (Exception var8) {
            var8.printStackTrace();
        }

    }

    private static final String getDomainName(HttpServletRequest request) {
        String domainName = null;
        String serverName = request.getRequestURL().toString();
        if ("".equals(serverName)) {
            domainName = "";
        } else {
            serverName = serverName.toLowerCase();
            if (serverName.startsWith("http://")) {
                serverName = serverName.substring(7);
            }

            int end = serverName.length();
            if (serverName.contains("/")) {
                end = serverName.indexOf("/");
            }

            serverName = serverName.substring(0, end);
            String[] domains = serverName.split("\\.");
            int len = domains.length;
            if (len > 3) {
                domainName = domains[len - 3] + "." + domains[len - 2] + "." + domains[len - 1];
            } else if (len > 1) {
                domainName = domains[len - 2] + "." + domains[len - 1];
            } else {
                domainName = serverName;
            }
        }

        if (domainName.indexOf(":") > 0) {
            String[] ary = domainName.split("\\:");
            domainName = ary[0];
        }

        return domainName;
    }
}
