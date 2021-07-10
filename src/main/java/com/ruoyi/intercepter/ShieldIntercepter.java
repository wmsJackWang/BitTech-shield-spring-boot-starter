package com.ruoyi.intercepter;


import com.ruoyi.ShieldProcessor;
import com.ruoyi.ShieldResponse;
import com.ruoyi.property.ShieldProperties;
import com.ruoyi.utils.RequestUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;


@Slf4j
public class ShieldIntercepter implements HandlerInterceptor {

    private static final int SUCCESS = 1;

    private static final String shieldforbidenurl = "/shield/shieldforbiden.html";

    public static final String staticPathSurfix= ".jpg,.png,.js,.css,.gif,.ico,/fonts/,.txt,.html,/css/,/js/";

    private ShieldProcessor processor;

    private ShieldProperties shieldProperties;

    public void setProcessor(ShieldProcessor processor) {
        this.processor = processor;
    }

    public void setShieldProperties(ShieldProperties shieldProperties) {
        this.shieldProperties = shieldProperties;
    }

    public static void main(String[] args) {
        System.out.println("null".equals(Arrays.stream(staticPathSurfix.split(",")).filter(surfix->
           "/admin/article".contains(surfix)).findAny().orElse("null")));
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        ShieldResponse br = processor.process(request);
        String requestUri = request.getRequestURI();
        //开启限制静态url  则不再区分静态资源， 未开启  则区分静态资源，静态资源直接通过
        if(!shieldProperties.getLimitStaticPath() && !"null".equals(Arrays.stream(staticPathSurfix.split(",")).filter(surfix->
                requestUri.contains(surfix)).findAny().orElse("null")))
            return true;

        log.info("enter ShieldIntercepter begin to limit  requestUri:{}",requestUri);
        if(br.getCode() == SUCCESS || shieldforbidenurl.equals(requestUri)) {
            return true;
        }
        String errorMsg = String.format("第%s次被限制！", br.getLimitCount());
        log.warn(errorMsg);
        if(RequestUtil.isAjax(request)) {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/html;charset=utf-8");
            PrintWriter writer = response.getWriter();
            writer.write(initErrorResp(errorMsg));
            writer.flush();
            writer.close();
            return false;
        }
        request.setAttribute("errorMsg", errorMsg);
        request.setAttribute("expire", TimeUnit.MILLISECONDS.toSeconds(br.getExpire()));
        request.getRequestDispatcher("/shield/shieldforbiden.html").forward(request, response);
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }

    private String initErrorResp(String errMsg){

        String result = "{"+ "\"errMsg\":\"" + errMsg + "\",\"status\":" + "\"FAIL\"" +"}";
        return result.toString();
    }
}
