package com.ruoyi.framework.aspectj;

import java.io.UnsupportedEncodingException;
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
	
	private static final String pageDirectory = "/htmlpages";

	
	@Autowired
	BlogConfig blogConfig;


    @Around("@annotation(staticPage)")
    public Object  preAuthority(ProceedingJoinPoint proceedingJoinPoint , StaticPage staticPage){

    	Object result = null;
    	//enable参数为true 则表示注解开启静态化
    	boolean pageEnable = staticPage.enable();
    	boolean blogWebEnable = blogConfig.getEnable();
    	
    	StaticPageLogic staticPageLogic = new StaticPageLogic(blogWebEnable, pageEnable);
//    	
//    	//业务方法
//        //访问目标方法的参数：
//        Object[] args = proceedingJoinPoint.getArgs();
    	
    	HttpServletRequest request = RequestHolder.getRequest();
    	
    	Boolean is_Createhtml = false;
    	//blogConfig.getEnable()决定整个网站的静态化功能是否开启。
    	if(staticPageLogic.enable)//enable = true
    	{
    		request.setAttribute("STATIC_PAGE", true);
	    	//执行 创建html静态文件 逻辑
			request.setAttribute("IS_CREATEHTML", staticPageLogic.is_Createhtml);
	    	//如果静态文件html存在  则直接返回html ,不需要后续的数据访问和模板解析
	    	if(staticPageLogic.isRedirect) 
	    	{
	    		log.info("发现存在该请求的静态html文件，直接重定向到请求对应的html文件,文件名：{}",staticPageLogic.redirectHtmlFileName);
	    		try {
	    			log.info("不执行controller逻辑：访问数据库、数据计算、模板解析等等。计算得到要重定向的html文件名：{}",staticPageLogic.redirectHtmlFileName);
	    	        
	    	        //父目录存在且html文件存在则返回 静态文件，否则直接走正常模板流程
    	        	request.getRequestDispatcher(staticPageLogic.responsePath).forward(request, RequestHolder.getResponse());
    	    		return null;
				} catch (Exception e) {
					// TODO Auto-generated catch block
		    		log.info("重定向失败：重定向到请求对应的html文件,文件名：{}",staticPageLogic.redirectHtmlFileName);
				}
	    	}
    	}else {
    		log.info("未开启静态化：{}",JdkThymeleafView.getRequestHTML(RequestHolder.getRequest()));
    	}
    	
    	try {
    		log.info("运行接口：{}",JdkThymeleafView.getRequestHTML(RequestHolder.getRequest()));
    		//执行正常的业务，例如  加载业务数据，再解析模板
			result = proceedingJoinPoint.proceed();
    		log.info("接口运行结束：{}",JdkThymeleafView.getRequestHTML(RequestHolder.getRequest()));
			
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	return result;
    	
    }
    
    class StaticPageLogic{
    	
    	boolean  is_Createhtml;//该请求是否  来自博客管理后台，要求网站生成静态文件。
    	boolean enable;//判断当前请求的页面是否开启静态化
    	boolean pageEnable;//当前请求是否开启静态化
    	boolean blogWebEnable;//整个网站是否开启静态化
    	boolean isExistHtmlFile;//该请求对应的html文件是否存在
    	boolean isRedirect;//是否重定向
    	String responsePath;//重定向的文件路径
    	String redirectHtmlFileName;//重定向的html文件名称
    	
    	
    	public StaticPageLogic(boolean blogWebEnable,boolean pageEnable) {
			// TODO Auto-generated constructor stub
    		//默认请求不是后台发送的生成html文件的请求
    		is_Createhtml = false;
    		//默认不重定向
    		isRedirect = false;
    		this.blogWebEnable=blogWebEnable;
    		this.pageEnable=pageEnable;
    		//后台发送生成html文件的请求  所携带的秘钥
    		String staticPageScretKey = RequestHolder.getRequest().getParameter("staticPageScretKey");
			//如果 url携带的参数 有秘钥，则该请求是后台发送生成html文件的请求
			if(!StringUtils.isEmpty(staticPageScretKey)&&blogConfig.getStaticPageScretKey().equals(staticPageScretKey))
				is_Createhtml = true;
			//判断当前请求是否要静态化
			enable=this.blogWebEnable&&this.pageEnable;
			
			this.redirectHtmlFileName= JdkThymeleafView.getRequestHTML(RequestHolder.getRequest());
			responsePath = pageDirectory + redirectHtmlFileName;  
			isExistHtmlFile=JdkThymeleafView.isExistHtmlFile();
			try {
				responsePath = URLDecoder.decode(responsePath, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		log.info("html路径统一编码失败：{}",responsePath);
    		
    		this.isRedirect = !this.is_Createhtml&&isExistHtmlFile;
		}
    	
    }
	
}
