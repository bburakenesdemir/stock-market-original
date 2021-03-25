package com.burakenesdemir.stockmarket.util;

import org.springframework.core.env.Environment;

public class ConfigLoaderUtil {
    private static Environment environment;

    public static void setEnvironment(Environment env){
        environment = env;
    }

    public static String getProperty(String key) {
        return environment.getProperty(key);
    }
}