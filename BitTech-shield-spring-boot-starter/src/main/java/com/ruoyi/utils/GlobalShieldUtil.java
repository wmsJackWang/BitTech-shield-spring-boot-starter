package com.ruoyi.utils;


public enum GlobalShieldUtil {

    INSTANCE;

    private static final String cacheKeyPrefix = "shield_";
    private static final String blacklistKeyPrefix = "shield_blacklist_";

    public String getLockKey(String ip) {
        return formatKey(cacheKeyPrefix + ip);
    }

    public String getBlacklistKey(String ip) {
        return formatKey(blacklistKeyPrefix + ip);
    }

    public String formatKey(String key) {
        if (null == key || key.isEmpty()) {
            return null;
        }
        return key.replaceAll("[.:]", "_");
    }

}
