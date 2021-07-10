package springboot.controller.admin;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;

import springboot.dto.Types;
import springboot.exception.TipException;
import springboot.modal.vo.ContentVo;
import springboot.modal.vo.ContentVoExample;
import springboot.service.IContentService;
import springboot.service.ILogService;
import springboot.service.IMetaService;

@RestController
@RequestMapping("/admin/restapi")
@Transactional(rollbackFor = TipException.class)
public class RestArticleController {
	
	private static final Logger logger = LoggerFactory.getLogger(ArticleController.class);

    @Resource
    private IContentService contentService;

    @Resource
    private IMetaService metaService;

    @Resource
    private ILogService logService;

    /**
     * 文章列表页面
     *
     * @param page
     * @param limit
     * @param request
     * @return
     */
    @GetMapping(value = "/article")
    public JSONObject index(@RequestParam(value = "pageNumber", defaultValue = "1") int page,
                        @RequestParam(value = "pageSize", defaultValue = "15") int limit,
                        HttpServletRequest request) {
    	
    	logger.info("page:{}",page);
    	

//        SysUser user = ShiroUtils.getSysUser();
//        Integer userId = user.getUserId().intValue();
//        for(SysRole role: user.getRoles())
//        	if(role.getRoleKey().equals(WebConst.ADMIN_KEY))
//        		userId = 0;
//        
//        	
        
        ContentVoExample contentVoExample = new ContentVoExample();
        contentVoExample.setOrderByClause("isTop desc, created desc");
        List<String> statuss = new ArrayList<>();
        statuss.add(Types.PUBLISH.getType());
        statuss.add(Types.DRAFT.getType());
        
        //管理员看到所有的文章,非管理员只能看到自己的文章
        contentVoExample.createCriteria().andTypeEqualTo(Types.ARTICLE.getType()).andStatusIn(statuss);
        	
        PageInfo<ContentVo> contentsPaginator = contentService.getArticlesWithpage_(contentVoExample, page, limit);
        JSONObject result = new JSONObject();
        result.put("total", contentsPaginator.getList().size());
        result.put("rows", contentsPaginator.getList());
        return result;
        
    }
    

}
