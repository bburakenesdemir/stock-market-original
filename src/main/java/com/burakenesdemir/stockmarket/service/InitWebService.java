package com.burakenesdemir.stockmarket.service;

import com.burakenesdemir.stockmarket.exception.SpringContextUtil;
import com.burakenesdemir.stockmarket.util.ConfigLoaderUtil;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class InitWebService implements InitializingBean {

    @Autowired
    private SpringContextUtil springContextUtil;

    @Override
    public void afterPropertiesSet() {
        ConfigLoaderUtil.setEnvironment(springContextUtil.getBean(Environment.class));
    }
}
