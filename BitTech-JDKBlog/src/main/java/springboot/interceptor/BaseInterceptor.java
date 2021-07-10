package springboot.interceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import springboot.constant.WebConst;
import springboot.service.IUserService;
import springboot.util.AdminCommons;
import springboot.util.Commons;
import springboot.util.IpUtil;
import springboot.util.MapCache;

/**
 * @author tangj
 * @date 2018/1/21 22:27
 */
@Component
public class BaseInterceptor implements HandlerInterceptor {
	
	
    private static final Logger logger = LoggerFactory.getLogger(BaseInterceptor.class);
    private static final String USER_AGENT = "user-agent";

    @Resource
    private IUserService userService;

    private MapCache cache = MapCache.single();

    @Autowired
    private Commons commons;

    @Autowired
    private AdminCommons adminCommons;
    
    @Value("${contextPath}")
    private String contextPath ;

    /**
     * 在处理方法之前执行，一般用来做一些准备工作：比如日志，权限检查
     * 如果返回false 表示被拦截，将不会执行处理方法
     * 返回true继续执行处理方法
     */
    
    //jdkblog融入到ruoyi的后台权限管理系统，不在使用自身的权限验证。
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String uri = request.getRequestURI();
        String ip = IpUtil.getIpAddrByRequest(request);
        logger.info("UserAgent: {}", request.getHeader(USER_AGENT));
        logger.info("用户访问地址: {}, 来路地址: {}", uri, ip);
        logger.info("禁止访问的IP: {}", WebConst.BLOCK_IPS.toString());

        //请求拦截处理,从session会话中获取用户信息
//        UserVo user = MyUtils.getLoginUser(request);
//        
//        if (null == user) {
//        	//从用户的cookie加密信息中获取  uuid, 然后获取user 再放入到会话当中
//        	
//        	logger.info("user信息不存在");
//            Integer uid = MyUtils.getCooKieUid(request);
//            request.getSession().setAttribute(WebConst.LOGIN_SESSION_KEY, user);
//        }
//        
////        // 处理uri
////        if (uri.startsWith("/admin") && !uri.startsWith("/admin/login") && null == user) {
////            response.sendRedirect(request.getContextPath() + "/admin/login");
////            return false;
////        }
//        // 处理uri , session为空 ，user为空 ， 所以跳转到登入界面。
//        if (uri.startsWith("/admin") && !uri.startsWith("/admin/login") && null == user) {
//            response.sendRedirect(contextPath+"/admin/login");
//            return false;
//        }
//        
////		// 处理uri
////		if (uri.startsWith("/admin") && !uri.startsWith("/admin/login") && null == user) {
////			response.sendRedirect(request.getRequestURI() + "/admin/login");
////			return false;
////		}
//        
//        // 设置get请求的token,缓存token，默认30分钟
//        if (request.getMethod().equals("GET")) {
//        	
//
//        	logger.info("设置token");
//            String csrf_token = UUID.UU64();
//            // 默认存储30分钟
//            cache.hset(Types.CSRF_TOKEN.getType(), csrf_token, uri, 30 * 60);
//            request.setAttribute("_csrf_token", csrf_token);
//        }
        return true;
    }

    
    /**
     * 在处理方法执行之后，在渲染视图执行之前执行，一般用来做一些清理工作
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        String ip = IpUtil.getIpAddrByRequest(request);
        // 禁止该ip访问
        if (WebConst.BLOCK_IPS.contains(ip)) {
            // 强制跳转
            modelAndView.setViewName("comm/ipban");
        }

        request.setAttribute("commons",commons);
        request.setAttribute("adminCommons",adminCommons);
    }

    
    
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
