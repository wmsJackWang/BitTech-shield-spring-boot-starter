package com.ruoyi.framework.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "staticpage")
public class BlogConfig{
	
	public String staticPageScretKey;//创建静态页面  所需要的秘钥
	
	public Boolean pageNameWithParams;//静态页面名称是否开启使用  url的参数来生成唯一的文件名
	
	public Boolean enable;//控制整个网站的静态化  是否开启

	public String getStaticPageScretKey() {
		return staticPageScretKey;
	}

	public void setStaticPageScretKey(String staticPageScretKey) {
		this.staticPageScretKey = staticPageScretKey;
	}	

	public Boolean getPageNameWithParams() {
		return pageNameWithParams;
	}

	public void setPageNameWithParams(Boolean pageNameWithParams) {
		this.pageNameWithParams = pageNameWithParams;
	}

	public Boolean getEnable() {
		return enable;
	}

	public void setEnable(Boolean enable) {
		this.enable = enable;
	}

	@Override
	public String toString() {
		return "BlogConfig [staticPageScretKey=" + staticPageScretKey + ", pageNameWithParams=" + pageNameWithParams
				+ ", enable=" + enable + "]";
	}

}
