package com.ruoyi.generator.mapper;

import java.util.List;

import com.ruoyi.generator.domain.SqlStatement;

/**
 * 【请填写功能名称】Mapper接口
 * 
 * @author ruoyi
 * @date 2021-07-02
 */
public interface SqlStatementMapper 
{

    public void myInsert(String sql);

    /**
     * 查询【请填写功能名称】
     * 
     * @param sqlId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    public SqlStatement selectSqlStatementById(Long sqlId);

    /**
     * 查询【请填写功能名称】列表
     * 
     * @param sqlStatement 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<SqlStatement> selectSqlStatementList(SqlStatement sqlStatement);

    /**
     * 新增【请填写功能名称】
     * 
     * @param sqlStatement 【请填写功能名称】
     * @return 结果
     */
    public int insertSqlStatement(SqlStatement sqlStatement);

    /**
     * 修改【请填写功能名称】
     * 
     * @param sqlStatement 【请填写功能名称】
     * @return 结果
     */
    public int updateSqlStatement(SqlStatement sqlStatement);

    /**
     * 删除【请填写功能名称】
     * 
     * @param sqlId 【请填写功能名称】ID
     * @return 结果
     */
    public int deleteSqlStatementById(Long sqlId);

    /**
     * 批量删除【请填写功能名称】
     * 
     * @param sqlIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteSqlStatementByIds(String[] sqlIds);
}
