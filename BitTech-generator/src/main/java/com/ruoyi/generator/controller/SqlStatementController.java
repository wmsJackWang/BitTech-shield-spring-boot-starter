package com.ruoyi.generator.controller;

import java.util.List;

import com.ruoyi.framework.util.ShiroUtils;
import com.ruoyi.generator.domain.SqlStatement;
import com.ruoyi.generator.service.ISqlStatementService;
import com.ruoyi.system.domain.SysUser;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;
import springboot.constant.WebConst;

/**
 * 【请填写功能名称】Controller
 * 
 * @author ruoyi
 * @date 2021-07-02
 */
@Controller
@RequestMapping("/system/statement")
public class SqlStatementController extends BaseController
{
    private String prefix = "tool/statement";

    @Autowired
    private ISqlStatementService sqlStatementService;

    @RequiresPermissions("system:statement:view")
    @GetMapping()
    public String statement()
    {
        return prefix + "/statement";
    }

    /**
     * 查询【请填写功能名称】列表
     */
    @RequiresPermissions("system:statement:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(SqlStatement sqlStatement)
    {
        SysUser user = getUserInfo();
        sqlStatement.setUserId(user.getUserId());//查出这个用户的sql语句
        user.getRoles().forEach((role)->{
            if(role.getRoleKey().equals(WebConst.ADMIN_KEY))
                sqlStatement.setUserId(null);//查出所有的id
        });
        startPage();
        List<SqlStatement> list = sqlStatementService.selectSqlStatementList(sqlStatement);
        return getDataTable(list);
    }

    /**
     * 导出【请填写功能名称】列表
     */
    @RequiresPermissions("system:statement:export")
    @Log(title = "【请填写功能名称】", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(SqlStatement sqlStatement)
    {
        List<SqlStatement> list = sqlStatementService.selectSqlStatementList(sqlStatement);
        ExcelUtil<SqlStatement> util = new ExcelUtil<SqlStatement>(SqlStatement.class);
        return util.exportExcel(list, "statement");
    }

    /**
     * 新增【请填写功能名称】
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存【请填写功能名称】
     */
    @RequiresPermissions("system:statement:add")
    @Log(title = "【请填写功能名称】", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(SqlStatement sqlStatement)
    {

        SysUser user = getUserInfo();
        sqlStatement.setUserId(user.getUserId());//查出这个用户的sql语句
        return toAjax(sqlStatementService.insertSqlStatement(sqlStatement));
    }

    /**
     * 修改【请填写功能名称】
     */
    @GetMapping("/edit/{sqlId}")
    public String edit(@PathVariable("sqlId") Long sqlId, ModelMap mmap)
    {
        SqlStatement sqlStatement = sqlStatementService.selectSqlStatementById(sqlId);
        mmap.put("sqlStatement", sqlStatement);
        return prefix + "/edit";
    }

    /**
     * 修改保存【请填写功能名称】
     */
    @RequiresPermissions("system:statement:edit")
    @Log(title = "【请填写功能名称】", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(SqlStatement sqlStatement)
    {
        return toAjax(sqlStatementService.updateSqlStatement(sqlStatement));
    }

    /**
     * 删除【请填写功能名称】
     */
    @RequiresPermissions("system:statement:remove")
    @Log(title = "【请填写功能名称】", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(sqlStatementService.deleteSqlStatementByIds(ids));
    }



    public static SysUser getUserInfo(){
        return ShiroUtils.getSysUser();
//        Integer userId = user.getUserId().intValue();
//        for(SysRole role: user.getRoles())
//            if(role.getRoleKey().equals(WebConst.ADMIN_KEY))
//                userId = 0;
    }

}
