package com.ruoyi.generator.service.impl;

import java.util.Arrays;
import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.generator.domain.SqlStatement;
import com.ruoyi.generator.mapper.SqlStatementMapper;
import com.ruoyi.generator.service.ISqlStatementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.core.text.Convert;

/**
 * 【请填写功能名称】Service业务层处理
 * 
 * @author ruoyi
 * @date 2021-07-02
 */
@Service
public class SqlStatementServiceImpl implements ISqlStatementService
{

    @Autowired
    private SqlStatementMapper sqlStatementMapper;

    /**
     * 查询【请填写功能名称】
     * 
     * @param sqlId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    @Override
    public SqlStatement selectSqlStatementById(Long sqlId)
    {
        return sqlStatementMapper.selectSqlStatementById(sqlId);
    }

    /**
     * 查询【请填写功能名称】列表
     * 
     * @param sqlStatement 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<SqlStatement> selectSqlStatementList(SqlStatement sqlStatement)
    {
        return sqlStatementMapper.selectSqlStatementList(sqlStatement);
    }

    /**
     * 新增【请填写功能名称】
     * 
     * @param sqlStatement 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertSqlStatement(SqlStatement sqlStatement)
    {
        sqlStatement.setCreateTime(DateUtils.getNowDate());
        sqlStatementMapper.myInsert(sqlStatement.getSqlContent());
        return sqlStatementMapper.insertSqlStatement(sqlStatement);
    }

    /**
     * 修改【请填写功能名称】
     * 
     * @param sqlStatement 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateSqlStatement(SqlStatement sqlStatement)
    {

        sqlStatement.setUpdateTime(DateUtils.getNowDate());

        //先删除库中的table，然后创建新的
        String tableName = sqlStatement.getTableName();
        String deleteSql = "drop table if exists " + tableName;
        sqlStatementMapper.myInsert(deleteSql);//删除掉要删除的sql

        //创建sql
        sqlStatementMapper.myInsert(sqlStatement.getSqlContent());

        return sqlStatementMapper.updateSqlStatement(sqlStatement);
    }

    /**
     * 删除【请填写功能名称】对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteSqlStatementByIds(String ids)
    {
        //批量删除sql
        Arrays.stream(Convert.toStrArray(ids))
                .forEach(sqlId -> {
                    SqlStatement sqlStatement = sqlStatementMapper.selectSqlStatementById(Long.valueOf(sqlId));
                    String tableName = sqlStatement.getTableName();
                    String deleteSql = "drop table if exists " + tableName;
                    sqlStatementMapper.myInsert(deleteSql);//删除掉要删除的sql
                });

        return sqlStatementMapper.deleteSqlStatementByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除【请填写功能名称】信息
     * 
     * @param sqlId 【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteSqlStatementById(Long sqlId)
    {

        SqlStatement sqlStatement = sqlStatementMapper.selectSqlStatementById(Long.valueOf(sqlId));
        String tableName = sqlStatement.getTableName();
        String deleteSql = "drop table if exists " + tableName;
        sqlStatementMapper.myInsert(deleteSql);//删除掉要删除的sql

        return sqlStatementMapper.deleteSqlStatementById(sqlId);
    }
}
