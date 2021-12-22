package com.example.Java2.LocaleResolver;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

/**
 * 国际化
 *
 * @author cym    2021/12/21
 */
public class MyLocaleResolver implements LocaleResolver {
    //解析区域信息
    @Override
    public Locale resolveLocale(HttpServletRequest request) {
        String l = request.getParameter("ll");
        Locale locale = Locale.getDefault();
        if(!StringUtils.isEmpty(l)){
            String[] split = l.split("_");
            locale = new Locale(split[0], split[1]);
        }
        return locale;
    }

    @Override
    public void setLocale (HttpServletRequest request,HttpServletResponse response,Locale locale) {

    }



}
