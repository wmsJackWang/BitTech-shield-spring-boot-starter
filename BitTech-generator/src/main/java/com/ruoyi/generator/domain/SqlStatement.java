package com.ruoyi.generator.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 【请填写功能名称】对象 sql_statement
 * 
 * @author ruoyi
 * @date 2021-07-02
 */
public class SqlStatement extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 编号 */
    private Long sqlId;

    /** 用户id */
    @Excel(name = "用户id")
    private Long userId;

    /** 表名称 */
    @Excel(name = "表名称")
    private String tableName;

    /** 具体的sql语句，一定是create ta ble语句 */
    @Excel(name = "具体的sql语句，一定是create ta ble语句")
    private String sqlContent;

    public void setSqlId(Long sqlId) 
    {
        this.sqlId = sqlId;
    }

    public Long getSqlId() 
    {
        return sqlId;
    }
    public void setUserId(Long userId) 
    {
        this.userId = userId;
    }

    public Long getUserId() 
    {
        return userId;
    }
    public void setTableName(String tableName) 
    {
        this.tableName = tableName;
    }

    public String getTableName() 
    {
        return tableName;
    }
    public void setSqlContent(String sqlContent) 
    {
        this.sqlContent = sqlContent;
    }

    public String getSqlContent() 
    {
        return sqlContent;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("sqlId", getSqlId())
            .append("userId", getUserId())
            .append("tableName", getTableName())
            .append("sqlContent", getSqlContent())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
