package com.ruoyi.framework.jdkutils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.view.ThymeleafView;

import com.ruoyi.framework.config.BlogApplicationConfig;
import com.ruoyi.framework.config.BlogConfig;
import com.ruoyi.framework.holder.RequestHolder;
import com.ruoyi.framework.holder.SpringContextHolder;

/*
 * thymeleaf静态化类
 * 这个  视图解析  影响面非常大， 不再使用这种方式
 */
public class JdkThymeleafView extends ThymeleafView{
	

	private static final Logger log = LoggerFactory.getLogger(JdkThymeleafView.class);
	
	@Override
	public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// TODO Auto-generated method stub
		Map<String, Object> modelMap = (Map<String, Object>) model;
		System.out.println("测试 render");
//		model = getAllAttribute(request);
		
		 /* 
         * 默认不生成静态文件,除非在Action中进行如下设置  
         * model.addAttribute("STATIC_PAGE", true); 
         */  
        if(request.getAttribute("STATIC_PAGE") == null || Boolean.FALSE.equals(request.getAttribute("STATIC_PAGE"))){//接口没有配置静态化注解，则走正常模板流程  
        	log.info("请求走正常模板流程");
    		renderFragment(null, model, request, response); 
        }else if(request.getAttribute("IS_CREATEHTML") == null || Boolean.FALSE.equals(request.getAttribute("IS_CREATEHTML"))) {
        	//接口配置了静态化注解，请求由普通用户发起，则判断该请求是否有静态化html文件，有则直接返回静态文件，没有则走正常模板流程
        	log.info("有静态html直接重定向到html，没有则生成html文件  并重定向");
        	getBackHtml(request, response,modelMap);  
        }else{  //接口配置了静态化注解，且请求是由blog后台发出的  创建静态化html请求。
        	log.info("该请求只创建html文件");
            createHTML(request, response,true,modelMap);  //创建静态文件html,后台定时任务调用返回
        }
		
	}
	

    //验证html静态文件是否存在
    public static boolean isExistHtmlFile(){
    	// TODO Auto-generated method stub
    	String basePath = getStaticPageBasePath() + "/htmlpages";//configHelper.getProperty("static_html_path");  
    	System.out.println("判断文件是否存在使用的路径："+JdkThymeleafView.getStaticPageBasePath());
        // String basePath =  
        // "D:\\Program Files\\Apache Software Foundation\\Tomcat 7.0\\webapps\\ROOT\\static\\";  
        // 访问的URL(根目录以后,如xxx/113.html)  
        String requestHTML = getRequestHTML(RequestHolder.getRequest());  
        // 静态页面保存的绝对路径  
        String htmlPath = basePath + requestHTML;  
        // response路径  
        String responsePath = "/htmlpages" + requestHTML;  
//        System.out.println(responsePath);
        File htmlFile = new File(htmlPath);  
        
        return htmlFile.getParentFile().exists()&&htmlFile.exists();
    }
  
    private void getBackHtml(HttpServletRequest request, HttpServletResponse response,Map<String, Object> model){
		// TODO Auto-generated method stub
//    	String basePath = getStaticPageBasePath() + "/htmlpages";//configHelper.getProperty("static_html_path");  
//        // String basePath =  
//        // "D:\\Program Files\\Apache Software Foundation\\Tomcat 7.0\\webapps\\ROOT\\static\\";  
//        // 访问的URL(根目录以后,如xxx/113.html)  
        String requestHTML = getRequestHTML(request);  
//        // 静态页面保存的绝对路径  
//        String htmlPath = basePath + requestHTML;  
//        // response路径  
        String responsePath = "/htmlpages" + requestHTML;  
////        System.out.println(responsePath);
//        File htmlFile = new File(htmlPath);  
        try {
	        //父目录存在且html文件存在则返回 静态文件，否则直接走正常模板流程
	        if (isExistHtmlFile()) { 
	        	log.info("有静态html直接重定向到html，直接重定向到模板对应的html文件路径：{}",responsePath);
	        	responsePath = URLDecoder.decode(responsePath, "UTF-8");//转发前对含有中文的url进行解码
	        	request.getRequestDispatcher(responsePath).forward(request, response);  
	            return;
	        }  
	        //这个应该要静态化的接口  ，没有静态化html，则主动生成html文件
	        createHTML(request, response,false,model);  //创建静态文件html ,并且重定向到html地址
        }catch (Exception e) {
			// TODO: handle exception
        	log.info("重定向失败，重定向到模板对应的html文件路径：{}",responsePath);
        	
		} finally {
			// TODO: handle finally clause
		}
	}

	public void createHTML(HttpServletRequest request,  
            HttpServletResponse response,boolean createOnly,Map<String, Object> model) throws IOException, ServletException {  
//        // 静态文件根目录的绝对路径  
//        ApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(request  
//                .getSession().getServletContext());  
        
//        PropsUtil configHelper = (PropsUtil) context.getBean("configHelper");  
		
        String basePath = getStaticPageBasePath() + "/htmlpages";//configHelper.getProperty("static_html_path");
        System.out.println("创建文件的路径："+JdkThymeleafView.getStaticPageBasePath());
        pushStaticPageUrl(request);
        // String basePath =  
        // "D:\\Program Files\\Apache Software Foundation\\Tomcat 7.0\\webapps\\ROOT\\static\\";  
        // 访问的URL(根目录以后,如xxx/113.html)  
        String requestHTML = this.getRequestHTML(request);  
        // 静态页面保存的绝对路径  
        String htmlPath = basePath + requestHTML;  
        // response路径  
        String responsePath = "/htmlpages" + requestHTML;  
        System.out.println("htmlPath:"+htmlPath);
        File htmlFile = new File(htmlPath);  
        if (!htmlFile.getParentFile().exists()) {  
            htmlFile.getParentFile().mkdirs();  
        }  
        if (!htmlFile.exists()) {  
            htmlFile.createNewFile();  
        }
        Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(htmlFile),  
                "UTF-8"));  
        // 处理模版  
        TemplateEngine templateEngine = SpringContextHolder.getAppContext().getBean(TemplateEngine.class);
        //获取数据模型,findBySpuId是一个方法,传入spuid获取所需数据
//        Map<String, Object> variables = getAllAttribute(request);
        //创建Context对象
        WebContext webContext = new WebContext(request, response,
                request.getServletContext(), request.getLocale(), model);
        templateEngine.process(getTemplateName(),webContext,out);
        //这是静态页面生成的地址路径,在配置文件中有配置
    	log.info("创建路径url模板对应的html静态文件，保存html的相对路径：{}",responsePath);
        out.flush();  
        out.close();  
  
        if(!createOnly)
        {
        	log.info("路径url模板对应的html静态文件创建成功，重定向到html路径：{}",responsePath); 
        	responsePath = URLDecoder.decode(responsePath, "UTF-8");
        	request.getRequestDispatcher(responsePath).forward(request, response);  
        	return;
        }else{//只创建html，不重定向到html
            PrintWriter printWriter = response.getWriter();
            printWriter.write("SUCCESS");
            printWriter.flush();
            printWriter.close();
        }
    }  
      
//    private Map<String, Object> getAllAttribute(HttpServletRequest request) {
//		// TODO Auto-generated method stub
//    	Map<String, Object> result = new HashMap<String, Object>();
//    	Enumeration<String> enumerations = request.getAttributeNames();
//    	String key = null;
//    	while(enumerations.hasMoreElements())
//    	{
//    		key = enumerations.nextElement();
////    		System.out.println(key);
//    		result.put(key, request.getAttribute(key));
//    	}
//		return result;
//	}


	/** 
     * 获取要生成的静态文件相对路径 
     *  
     * @param request HttpServletRequest 
     * @return /目录/*.html 
     */  
    public static String getRequestHTML(HttpServletRequest request) {  
        // web应用名称,部署在ROOT目录时为空  
        String contextPath = request.getContextPath();  
        // web应用/目录/文件,如/xxxx/1  
        String requestURI = request.getRequestURI();  
        
        // basePath里面已经有了web应用名称，所以直接把它replace掉，以免重复  
        requestURI = requestURI.replaceFirst(contextPath, "");  
  
        BlogConfig blogConfig = SpringContextHolder.getBean(BlogConfig.class);
        
        if(blogConfig.getPageNameWithParams())
        {
	        // 得到参数  
	        Enumeration<?> pNames = request.getParameterNames();  
	        while (pNames.hasMoreElements()) {  
	            String name = (String) pNames.nextElement();  
	            String value = request.getParameter(name);  
	            requestURI = requestURI + "_" + name + "=" + value;  
	        } 
        }
  
        // 加上.html后缀  
        requestURI = requestURI + ".html";  
  
        try {
			return URLDecoder.decode(requestURI, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return requestURI;  
    }  
    
    public void pushStaticPageUrl(HttpServletRequest request) {  
    	
        BlogConfig blogConfig = SpringContextHolder.getBean(BlogConfig.class);      

        String staticPageUrl = request.getRequestURI();//静态 HTML的页面url

        if(blogConfig.getPageNameWithParams())
        	staticPageUrl += "?" +request.getQueryString();
        
    	/*
         * 这段逻辑，是将静态页面的url集中存储到redis中
         * 
         * 本地也有url缓存集合，是为了避免 url一直重复放入到redis中。
         * 
         * redis中的url集合是为了方便后台admin项目  来主动调用 需要重新生成静态页面的url。
         */
        //判断这个url是否在已经生成静态html的 url集合中
        if(!BlogApplicationConfig.staticPageUrls.contains(staticPageUrl)) 
        {
        	//添加到本地的url集合中去
        	BlogApplicationConfig.staticPageUrls.add(staticPageUrl);
//        	RedisService redisService = BlogApplicationContext.contex;
        	RedisTemplate<String, Object> redisTemplate = (RedisTemplate<String, Object>) SpringContextHolder.getAppContext().getBean("staticPageRedisTemplate");
        	//添加到redis的url集合中去
        	redisTemplate.opsForSet().add("JDKBlog:staticPageUrls","http://bittechblog.com"+staticPageUrl);
        }}
    
    
     
    public static String getStaticPageBasePath() {
        String path = JdkThymeleafView.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        path = path.substring(1, path.length());
        try {
            path = URLDecoder.decode(path, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        int lastIndex = path.lastIndexOf("/") + 1;	
        path = path.substring(0, lastIndex);
        File file = new File("");
        return file.getAbsolutePath() + "/";

    }

}
