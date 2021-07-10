package com.ruoyi.common.enums;

/**
 * 数据源
 * 
 * @author ruoyi
 */
public enum DataSourceType
{

    /**
     * 主库
     */
    MASTER("masterDataSource"),

    /**
     * 从库
     */
    SLAVE("slaveDataSource"),
    
    /**
     * 比特科技博客数据源
     */
    JDKBLOG("jdkBlogDataSource"),
    /**
     * 目标库
     */
    GENCODEDATASOURCE("genCodeDataSource");


    private String sourceBeanName;

    DataSourceType(String sourceBeanName){
        this.sourceBeanName = sourceBeanName;
    }

    public String getSourceBeanName(){
        return sourceBeanName;
    }

}
