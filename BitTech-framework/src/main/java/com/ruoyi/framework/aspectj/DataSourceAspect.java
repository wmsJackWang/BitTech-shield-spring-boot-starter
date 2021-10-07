package com.ruoyi.framework.aspectj;

import java.util.Objects;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import com.ruoyi.common.annotation.DataSource;
import com.ruoyi.common.config.datasource.DynamicDataSourceContextHolder;
import com.ruoyi.common.enums.DataSourceType;
import com.ruoyi.common.utils.StringUtils;

/**
 * 多数据源处理
 * 
 * @author ruoyi
 */
@Aspect
@Order(1)
@Component
public class DataSourceAspect
{
    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Pointcut("@annotation(com.ruoyi.common.annotation.DataSource)"
            + "|| @within(com.ruoyi.common.annotation.DataSource)")
    public void dsPointCut(){}
    
    @Pointcut("execution(* springboot.controller..*.*(..))" )
    public void jdkBlogDsPointCut() {}

    @Pointcut("execution(* com.ruoyi.generator.service.impl..*.*(..))" )
    public void genCodeDsPointCut() {}

//    com.ruoyi.generator.service.impl.GenTableServiceImpl.selectDbTableColumnsByName(String tableName)
//    @Pointcut("execution(* com.ruoyi.generator.service.impl.GenTableServiceImpl.selectDbTableColumnsByName(*) or "
//    		+ "" )
//    public void targetSourceDsPointCut() {
//    	
//    }
    @Around("jdkBlogDsPointCut()")
    public Object jdkBlogAround(ProceedingJoinPoint point) throws Throwable
    { 
    	logger.info("数据源切换到"+DataSourceType.JDKBLOG.name());
    	
    	//设置为jdkblog的数据源
        DynamicDataSourceContextHolder.setDataSourceType(DataSourceType.JDKBLOG.name());

        try
        {
            return point.proceed();
        }
        finally
        {
        	//设置为默认数据源
        	DynamicDataSourceContextHolder.setDataSourceType(DataSourceType.MASTER.name());
        	

        	logger.info("数据源切换回"+DataSourceType.MASTER.name());
        }
    }



    @Around("genCodeDsPointCut()")
    public Object genCodeAround(ProceedingJoinPoint point) throws Throwable
    {
        logger.info("数据源切换到"+DataSourceType.GENCODEDATASOURCE.name());

        //设置为jdkblog的数据源
        DynamicDataSourceContextHolder.setDataSourceType(DataSourceType.GENCODEDATASOURCE.name());

        try
        {
            return point.proceed();
        }
        finally
        {
            //设置为默认数据源
            DynamicDataSourceContextHolder.setDataSourceType(DataSourceType.MASTER.name());


            logger.info("数据源切换回"+DataSourceType.MASTER.name());
        }
    }
    

    @Around("dsPointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable
    {
        DataSource dataSource = getDataSource(point);

        if (StringUtils.isNotNull(dataSource))
        {
            DynamicDataSourceContextHolder.setDataSourceType(dataSource.value().name());
        }

        try
        {
            return point.proceed();
        }
        finally
        {
            // 销毁数据源 在执行方法之后
            DynamicDataSourceContextHolder.clearDataSourceType();
        }
    }

    /**
     * 获取需要切换的数据源
     */
    public DataSource getDataSource(ProceedingJoinPoint point)
    {
        MethodSignature signature = (MethodSignature) point.getSignature();
        DataSource dataSource = AnnotationUtils.findAnnotation(signature.getMethod(), DataSource.class);
        if (Objects.nonNull(dataSource))
        {
            return dataSource;
        }

        return AnnotationUtils.findAnnotation(signature.getDeclaringType(), DataSource.class);
    }
}
