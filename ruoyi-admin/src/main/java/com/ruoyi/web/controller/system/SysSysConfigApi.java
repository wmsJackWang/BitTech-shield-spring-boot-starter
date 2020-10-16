package com.ruoyi.web.controller.system;

import java.util.List;

import javax.validation.constraints.NotBlank;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.SysConfig;
import com.ruoyi.system.service.ISysConfigService;


@Controller
@RequestMapping("/api/system/config")
public class SysSysConfigApi  extends BaseController{
	
	
	
	private static final Logger log = LoggerFactory.getLogger(SysSysConfigApi.class);


    @Autowired
    private ISysConfigService configService;

	
    /**
     * 查询参数配置
     */
    @RequestMapping("/getSysConfig")
    @ResponseBody
    public SysConfig list(@Validated @NotBlank(message = "configKey参数不能为空")String configKey)
    {
    	log.info("接收到的参数configKey:{}",configKey);
    	
    	if(StringUtils.isEmpty(configKey))
    		return null;
    	
    	SysConfig config = new SysConfig();
    	config.setConfigKey(configKey);
        startPage();
        List<SysConfig> list = configService.selectConfigList(config);
        
        SysConfig result = list.isEmpty()?null:list.get(0);
        log.info("返回的参数result:{}",result.toString());
        
        return result;
    }


}
