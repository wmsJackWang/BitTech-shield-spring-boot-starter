package com.ruoyi.property;

import com.ruoyi.ShieldCacheType;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.StringUtils;


@ConfigurationProperties("shield.limit.access")
public class ShieldProperties {

    /**
     * switcher: 开启——open ， 关闭——close
     * 单位：次 默认为20
     */
    private String switcher = "open";

    /**
     * 连续访问最高阀值，超过该值则认定为恶意操作的IP
     * 单位：次 默认为20
     */
    private int threshold = 20;

    /**
     * 间隔时间，在该时间内如果访问次数大于阀值，则记录为恶意IP，否则视为正常访问
     * 单位：毫秒(ms)，默认为 5秒
     */
    private long interval = 5000;

    /**
     * 限制访问的容错值，容错值范围内(0 < x < faultTolerance)过了限制时间就可正常访问，一旦大于容错值，则进行限制访问
     * 默认为-1，表示不进行直接限制
     *
     */
    private int faultTolerance = -1;

    /**
     * 当检测到恶意访问时，对恶意访问的ip进行限制的时间
     * 单位：毫秒(ms)，默认为 1分钟
     */
    private long limitedTime = 60000;

    /**
     * 黑名单存在的时间，在单位时间内用户访问受限的次数累加
     * 单位：毫秒(ms)，默认为 1个月
     */
    private long blacklistTime = 2592000000L;

    /**
     * 缓存类型，默认为map存储
     */
    private ShieldCacheType type = ShieldCacheType.MAP;

    /**
     * 静态路径是否需要防护，默认不防护
     */
    private boolean limitStaticPath = false;

    public String getSwitcher(){
        return switcher;
    }

    public void setSwitcher(String switcher) {
        this.switcher = switcher;
    }

    public int getThreshold() {
        return threshold;
    }

    public void setThreshold(int threshold) {
        this.threshold = threshold;
    }

    public long getInterval() {
        return interval;
    }

    public void setInterval(long interval) {
        this.interval = interval;
    }

    public int getFaultTolerance() {
        return faultTolerance;
    }

    public void setFaultTolerance(int faultTolerance) {
        this.faultTolerance = faultTolerance;
    }

    public long getLimitedTime() {
        return limitedTime;
    }

    public void setLimitedTime(long limitedTime) {
        this.limitedTime = limitedTime;
    }

    public long getBlacklistTime() {
        return blacklistTime;
    }

    public void setBlacklistTime(long blacklistTime) {
        this.blacklistTime = blacklistTime;
    }

    public ShieldCacheType getType() {
        return type;
    }

    public ShieldProperties setType(String type) {
        if (StringUtils.isEmpty(type)) {
            return this;
        }
        this.type = ShieldCacheType.valueOf(type.toUpperCase());
        return this;
    }
    public boolean getLimitStaticPath(){
        return limitStaticPath;
    }

    public void setLimitStaticPath(boolean limitStaticPath) {
        this.limitStaticPath = limitStaticPath;
    }

    @Override
    public String toString() {
        return "ShieldProperties{" +
                "switcher='" + switcher + '\'' +
                ", threshold=" + threshold +
                ", interval=" + interval +
                ", faultTolerance=" + faultTolerance +
                ", limitedTime=" + limitedTime +
                ", blacklistTime=" + blacklistTime +
                ", type=" + type +
                ", limitStaticPath=" + limitStaticPath +
                '}';
    }
}
