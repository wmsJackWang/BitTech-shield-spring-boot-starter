package com.ruoyi.framework.aspectj;

import java.net.URLDecoder;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.ruoyi.framework.anoation.StaticPage;
import com.ruoyi.framework.config.BlogConfig;
import com.ruoyi.framework.holder.RequestHolder;
import com.ruoyi.framework.jdkutils.JdkThymeleafView;

@Component
@Aspect
@Order(Integer.MAX_VALUE)
public class StaticPageAspects {
	
	
	private static final Logger log = LoggerFactory.getLogger(StaticPageAspects.class);

	
	@Autowired
	BlogConfig blogConfig;


    @Around("@annotation(staticPage)")
    public Object  preAuthority(ProceedingJoinPoint proceedingJoinPoint , StaticPage staticPage){

    	//enable参数为true 则表示注解开启静态化
    	boolean enable = staticPage.enable();
    	
    	//业务方法
        //访问目标方法的参数：
        Object[] args = proceedingJoinPoint.getArgs();
    	Object result = null;
    	
    	HttpServletRequest request = RequestHolder.getRequest();
    	
    	Boolean is_Createhtml = false;
    	System.out.println("blogConfig:"+blogConfig.toString());
    	//blogConfig.getEnable()决定整个网站的静态化功能是否开启。
    	if(blogConfig.getEnable()&&enable)//enable = true
    	{
    		request.setAttribute("STATIC_PAGE", true);
	        String staticPageScretKey = RequestHolder.getRequest().getParameter("staticPageScretKey");
			//如果 url携带的参数 有秘钥，则执行静态文件生成逻辑
			if(!StringUtils.isEmpty(staticPageScretKey)&&blogConfig.getStaticPageScretKey().equals(staticPageScretKey))
				is_Createhtml = true;

    	
	    	//执行 创建html静态文件 逻辑
			request.setAttribute("IS_CREATEHTML", is_Createhtml);
	    	//如果静态文件html存在  则直接返回html ,不需要后续的数据访问和模板解析
	    	if(!is_Createhtml&&JdkThymeleafView.isExistHtmlFile()) 
	    	{
	    		try {
	    			log.info("不执行controller逻辑、访问数据库，直接重定向html");
	    	        String requestHTML = JdkThymeleafView.getRequestHTML(RequestHolder.getRequest());  
//	    	        // 静态页面保存的绝对路径  
//	    	        String htmlPath = basePath + requestHTML;  
//	    	        // response路径  
	    	        String responsePath = "/htmlpages" + requestHTML;  
////	    	        System.out.println(responsePath);
//	    	        File htmlFile = new File(htmlPath);  
	    	        //父目录存在且html文件存在则返回 静态文件，否则直接走正常模板流程
    	        	log.info("有静态html直接重定向到html，直接重定向到模板对应的html文件路径：{}",responsePath);
//    	        	RequestHolder.getRequest().getRequestDispatcher(responsePath).forward(RequestHolder.getRequest(), RequestHolder.getResponse());
    	        	responsePath = URLDecoder.decode(responsePath, "UTF-8");
    	        	request.getRequestDispatcher(responsePath).forward(request, RequestHolder.getResponse());  
//    	        	System.out.println("return null");
    	    		return null;
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    	}
    	}
    	
    	
    	
    	try {
    		log.info("没有静态化html文件，直接运行接口：{}",JdkThymeleafView.getRequestHTML(RequestHolder.getRequest()));
    		//执行正常的业务，例如  加载业务数据，再解析模板
			result = proceedingJoinPoint.proceed();
    		log.info("接口运行结束：{}",JdkThymeleafView.getRequestHTML(RequestHolder.getRequest()));
			
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	return result;
    	
    }
    
	
}
