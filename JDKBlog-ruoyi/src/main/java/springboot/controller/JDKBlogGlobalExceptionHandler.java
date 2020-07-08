package springboot.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import springboot.exception.TipException;

@ControllerAdvice
public class JDKBlogGlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(JDKBlogGlobalExceptionHandler.class);

    @ExceptionHandler(value = TipException.class)
    public String tipException(Exception e){
        logger.error("find exception:e+{}",e.getMessage());
        e.printStackTrace();
        return "comm/error_500";
    }

    @ExceptionHandler(value = Exception.class)
    public String exceptionHandler(Exception e){
        logger.error("find exception:e+{}",e.getMessage());
        e.printStackTrace();
        return "comm/error_404";
    }

}
