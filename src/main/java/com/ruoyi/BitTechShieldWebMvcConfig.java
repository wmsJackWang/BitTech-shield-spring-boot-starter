package com.ruoyi;

import com.ruoyi.intercepter.ShieldIntercepter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

public class BitTechShieldWebMvcConfig  extends WebMvcConfigurerAdapter {

    private ShieldIntercepter shieldIntercepter;

    public void setShieldIntercepter(ShieldIntercepter shieldIntercepter) {
        this.shieldIntercepter = shieldIntercepter;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(shieldIntercepter);
    }
}
