package springboot.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ruoyi.framework.util.ShiroUtils;
import com.ruoyi.system.domain.SysRole;
import com.ruoyi.system.domain.SysUser;

import org.springframework.stereotype.Service;

import springboot.constant.WebConst;
import springboot.dao.AttachVoMapper;
import springboot.modal.vo.AttachVo;
import springboot.modal.vo.AttachVoExample;
import springboot.service.IAttachService;
import springboot.util.DateKit;

import javax.annotation.Resource;
import java.util.List;

@Service
public class AttachServiceImpl implements IAttachService {

    @Resource
    private AttachVoMapper attachDao;

    @Override
    public PageInfo<AttachVo> getAttachs(Integer page, Integer limit) {
        PageHelper.startPage(page, limit);
        
        SysUser user = ShiroUtils.getSysUser();
        Integer userId = user.getUserId().intValue();
        for(SysRole role: user.getRoles())
        	if(role.getRoleKey().equals(WebConst.ADMIN_KEY))
        		userId = 0;
        AttachVoExample attachVoExample = new AttachVoExample();
        attachVoExample.setOrderByClause("id desc");
        
        if(userId == 0)	
        	;
        else
        	attachVoExample.createCriteria().andAuthorIdEqualTo(userId);
        	
        List<AttachVo> attachVos = attachDao.selectByExample(attachVoExample);
        return new PageInfo<>(attachVos);
    }

    @Override
    public void save(String fname, String fkey, String ftype, Integer author) {
        AttachVo attach = new AttachVo();
        attach.setFname(fname);
        attach.setAuthorId(author);
        attach.setFkey(fkey);
        attach.setFtype(ftype);
        attach.setCreated(DateKit.getCurrentUnixTime());
        attachDao.insertSelective(attach);
    }

    @Override
    public AttachVo selectById(Integer id) {
        if (null != id) {
            return attachDao.selectByPrimaryKey(id);
        }
        return null;
    }

    @Override
    public void deleteById(Integer id) {
        if (null != id) {
            attachDao.deleteByPrimaryKey(id);
        }
    }
}
