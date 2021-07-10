package springboot.controller.admin;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageInfo;
import com.ruoyi.framework.util.ShiroUtils;
import com.ruoyi.system.domain.SysRole;
import com.ruoyi.system.domain.SysUser;

import springboot.constant.WebConst;
import springboot.controller.AbstractController;
import springboot.controller.helper.ExceptionHelper;
import springboot.dto.LogActions;
import springboot.dto.Types;
import springboot.enums.AriticleEditTypeEnum;
import springboot.exception.TipException;
import springboot.modal.bo.RestResponseBo;
import springboot.modal.vo.ContentVo;
import springboot.modal.vo.ContentVoExample;
import springboot.modal.vo.MetaVo;
import springboot.modal.vo.UserVo;
import springboot.service.IContentService;
import springboot.service.ILogService;
import springboot.service.IMetaService;
import springboot.util.Commons;

/**
 * 文章管理
 *
 * @author tangj
 * @date 2018/1/24 21:01
 */
@Controller
@RequestMapping("/admin/article")
@Transactional(rollbackFor = TipException.class)
public class ArticleController extends AbstractController {
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
    @GetMapping(value = "")
    public String index(@RequestParam(value = "page", defaultValue = "1") int page,
                        @RequestParam(value = "limit", defaultValue = "15") int limit,
                        HttpServletRequest request) {
    	
    	logger.info("page:{}",page);
    	

        SysUser user = ShiroUtils.getSysUser();
        Integer userId = user.getUserId().intValue();
        for(SysRole role: user.getRoles())
        	if(role.getRoleKey().equals(WebConst.ADMIN_KEY))
        		userId = 0;
        
        	
        
        ContentVoExample contentVoExample = new ContentVoExample();
        contentVoExample.setOrderByClause("isTop desc, created desc");
        List<String> statuss = new ArrayList<>();
        statuss.add(Types.PUBLISH.getType());
        statuss.add(Types.DRAFT.getType());
        
        //管理员看到所有的文章,非管理员只能看到自己的文章
        if(userId==0)
        	contentVoExample.createCriteria().andTypeEqualTo(Types.ARTICLE.getType()).andStatusIn(statuss);
        else
        	contentVoExample.createCriteria().andTypeEqualTo(Types.ARTICLE.getType()).andStatusIn(statuss).andAuthorIdEqualTo(userId);
        	
        PageInfo<ContentVo> contentsPaginator = contentService.getArticlesWithpage(contentVoExample, page, limit);
        request.setAttribute("articles", contentsPaginator);
        return "admin/article_list";
        
    }
    
    
    /**
     * 文章列表页面
     *
     * @param page
     * @param limit
     * @param request
     * @return
     */
    @GetMapping(value = "/review")
    public String review(@RequestParam(value = "page", defaultValue = "1") int page,
                        @RequestParam(value = "limit", defaultValue = "15") int limit,
                        HttpServletRequest request) {
    	
    	logger.info("page:{}",page);
    	

        SysUser user = ShiroUtils.getSysUser();
        Integer userId = user.getUserId().intValue();
        for(SysRole role: user.getRoles())
        	if(role.getRoleKey().equals(WebConst.ADMIN_KEY))
        		userId = 0;
        
    	
        ContentVoExample contentVoExample = new ContentVoExample();
        contentVoExample.setOrderByClause("isTop desc , isBottom  asc , created desc");
        
        //管理员看到所有的文章,非管理员只能看到自己的文章
        if(userId==0)
        	contentVoExample.createCriteria().andTypeEqualTo(Types.ARTICLE.getType()).andStatusEqualTo(Types.REVIEW.getType());
        else
        	contentVoExample.createCriteria().andTypeEqualTo(Types.ARTICLE.getType()).andStatusEqualTo(Types.REVIEW.getType()).andAuthorIdEqualTo(userId);        	
        
        
        PageInfo<ContentVo> contentsPaginator = contentService.getArticlesWithpage(contentVoExample, page, limit);
        request.setAttribute("articles", contentsPaginator);
        return "admin/articletemplate_list";
        
    }
    
    
    
    /**
     * 文章列表页面
     *
     * @param page
     * @param limit
     * @param request
     * @return
     */
    @GetMapping(value = "/myArticle")
    public String myIndex(@RequestParam(value = "page", defaultValue = "1") int page,
                        @RequestParam(value = "limit", defaultValue = "15") int limit,
                        HttpServletRequest request) {
    	
    	logger.info("page:{}",page);
    	

        SysUser user = ShiroUtils.getSysUser();
        Integer userId = user.getUserId().intValue();

        
        ContentVoExample contentVoExample = new ContentVoExample();
        contentVoExample.setOrderByClause("isTop desc, created desc");
        List<String> statuss = new ArrayList<>();
        statuss.add(Types.PUBLISH.getType());
        statuss.add(Types.DRAFT.getType());
        

        contentVoExample.createCriteria().andTypeEqualTo(Types.ARTICLE.getType()).andStatusIn(statuss).andAuthorIdEqualTo(userId);
        	
        PageInfo<ContentVo> contentsPaginator = contentService.getArticlesWithpage(contentVoExample, page, limit);
        request.setAttribute("articles", contentsPaginator);
        return "admin/article_list";
        
    }
    
    
    /**
     * 文章列表页面
     *
     * @param page
     * @param limit
     * @param request
     * @return
     */
    @GetMapping(value = "/myReview")
    public String myReview(@RequestParam(value = "page", defaultValue = "1") int page,
                        @RequestParam(value = "limit", defaultValue = "15") int limit,
                        HttpServletRequest request) {
    	
    	logger.info("page:{}",page);
    	

        SysUser user = ShiroUtils.getSysUser();
        Integer userId = user.getUserId().intValue();

        ContentVoExample contentVoExample = new ContentVoExample();
        contentVoExample.setOrderByClause("isTop desc , isBottom  asc , created desc");

        contentVoExample.createCriteria().andTypeEqualTo(Types.ARTICLE.getType()).andStatusEqualTo(Types.REVIEW.getType()).andAuthorIdEqualTo(userId);        	
        
        
        PageInfo<ContentVo> contentsPaginator = contentService.getArticlesWithpage(contentVoExample, page, limit);
        request.setAttribute("articles", contentsPaginator);
        return "admin/articletemplate_list";
        
    }
    
    

    /**
     * 文章发表页面
     *
     * @param request
     * @return
     */
    @GetMapping(value = "/publish")
    public String newArticle(HttpServletRequest request) {
        List<MetaVo> categories = metaService.getMetas(Types.CATEGORY.getType());
        request.setAttribute("categories", categories);
        request.setAttribute(Types.ATTACH_URL.getType(), Commons.site_option(Types.ATTACH_URL.getType()));
        return "admin/article_edit";
    }

    /**
     * 文章编辑页面
     *
     * @param cid
     * @param request
     * @return
     */
    @GetMapping(value = "/{cid}")
    public String editArticle(@PathVariable String cid, HttpServletRequest request) {
        ContentVo contents = contentService.getContents(cid);
        request.setAttribute("contents", contents);
        List<MetaVo> categories = metaService.getMetas(Types.CATEGORY.getType());
        request.setAttribute("categories", categories);
        request.setAttribute(Types.ATTACH_URL.getType(), Commons.site_option(Types.ATTACH_URL.getType()));
        request.setAttribute("active", "article");
        return "admin/article_edit";
    }

    /**
     * 文章发表 post
     *
     * @param contents
     * @param request
     * @return
     */
    @PostMapping(value = "/publish")
    @ResponseBody
    @Transactional(rollbackFor = TipException.class)
    public RestResponseBo publishArticle(ContentVo contents, HttpServletRequest request) {
//        UserVo users = this.user(request);

        SysUser user = ShiroUtils.getSysUser();
        contents.setAuthorId(user.getUserId().intValue());
        contents.setType(Types.ARTICLE.getType());
        if (StringUtils.isBlank(contents.getCategories())) {
            contents.setCategories("默认分类");
        }
        try {
            contentService.publish(contents);
        } catch (Exception e) {
            String msg = "文章发布失败";
            return ExceptionHelper.handlerException(logger, msg, e);
        }
        return RestResponseBo.ok();
    }

    /**
     * 文章更新 post
     *
     * @param contents
     * @param request
     * @return
     */
    @PostMapping(value = "/modify")
    @ResponseBody
    @Transactional(rollbackFor = TipException.class)
    public RestResponseBo modifyArticle(ContentVo contents, HttpServletRequest request) {
//        UserVo users = this.user(request);

        SysUser user = ShiroUtils.getSysUser();
        contents.setAuthorId(user.getUserId().intValue());
        contents.setType(Types.ARTICLE.getType());
        try {
            contentService.updateArticle(contents);
        } catch (Exception e) {
            String msg = "文章编辑失败";
            return ExceptionHelper.handlerException(logger, msg, e);
        }
        return RestResponseBo.ok();
    }

    /**
     * 删除文章 post
     *
     * @param cid
     * @param request
     * @return
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    @Transactional(rollbackFor = TipException.class)
    public RestResponseBo delete(@RequestParam int cid, HttpServletRequest request) {
        try {
            contentService.deleteByCid(cid);
            logService.insertLog(LogActions.DEL_ARTICLE.getAction(), cid + "", request.getRemoteAddr(), this.getUid(request));
        } catch (Exception e) {
            String msg = "文章删除失败";
            return ExceptionHelper.handlerException(logger, msg, e);
        }
        return RestResponseBo.ok();
    }
    
    
    /**
     * 修改文章数据    post
     *
     * @param cid
     * @param request
     * @return
     */
    @RequestMapping(value = "/updateV2/{type}/{value}/{id}")
    @ResponseBody
    @Transactional(rollbackFor = TipException.class)
    public RestResponseBo updateV2(@PathVariable("type")String type,@PathVariable("value")Long value, @PathVariable("id")int cid, HttpServletRequest request) {
        try {
        	ContentVo contentVo = new ContentVo();
        	contentVo.setCid(cid);
        	if(AriticleEditTypeEnum.LOCKED.getValue().equals(type)) {//文章锁 相关操作
        		contentVo.setLocked(value==1L?true:false);
        	}
        	contentService.updateContentByCid(contentVo);
        } catch (Exception e) {
            String msg = "文章修改失败";
            return ExceptionHelper.handlerException(logger, msg, e);
        }
        return RestResponseBo.ok();
    }    
    
    /**
     * 置顶文章 post
     *
     * @param cid
     * @param request
     * @return
     */
    @RequestMapping(value = "/setTop")
    @ResponseBody
    @Transactional(rollbackFor = TipException.class)
    public RestResponseBo setTop(@RequestParam int cid, HttpServletRequest request) {
        try {
        	ContentVo contentVo = new ContentVo();
        	contentVo.setCid(cid);
        	contentVo.setIsTop(true);
        	contentService.updateContentByCid(contentVo);
            logService.insertLog(LogActions.SETTOP_ARTITLE.getAction(), cid + "", request.getRemoteAddr(), this.getUid(request));
        } catch (Exception e) {
            String msg = "文章置顶失败";
            return ExceptionHelper.handlerException(logger, msg, e);
        }
        return RestResponseBo.ok();
    }    
    
    
    /**
     * 取消置顶文章 post
     *
     * @param cid
     * @param request
     * @return
     */
    @RequestMapping(value = "/cancelTop")
    @ResponseBody
    @Transactional(rollbackFor = TipException.class)
    public RestResponseBo cancelTop(@RequestParam int cid, HttpServletRequest request) {
        try {
        	ContentVo contentVo = new ContentVo();
        	contentVo.setCid(cid);
        	contentVo.setIsTop(false);
        	contentService.updateContentByCid(contentVo);
            logService.insertLog(LogActions.CANCELTOP_ARTITLE.getAction(), cid + "", request.getRemoteAddr(), this.getUid(request));
        } catch (Exception e) {
            String msg = "文章取消置顶失败";
            return ExceptionHelper.handlerException(logger, msg, e);
        }
        return RestResponseBo.ok();
    }    
    
    
    
    /**
     * 置顶文章 post
     *
     * @param cid
     * @param request
     * @return
     */
    @RequestMapping(value = "/setBottom")
    @ResponseBody
    @Transactional(rollbackFor = TipException.class)
    public RestResponseBo setBottom(@RequestParam int cid, HttpServletRequest request) {
        try {
        	ContentVo contentVo = new ContentVo();
        	contentVo.setCid(cid);
        	contentVo.setIsBottom(true);
        	contentService.updateContentByCid(contentVo);
            logService.insertLog(LogActions.SETTOP_ARTITLE.getAction(), cid + "", request.getRemoteAddr(), this.getUid(request));
        } catch (Exception e) {
            String msg = "文章置顶失败";
            return ExceptionHelper.handlerException(logger, msg, e);
        }
        return RestResponseBo.ok();
    }    
    
    
    /**
     * 取消置顶文章 post
     *
     * @param cid
     * @param request
     * @return
     */
    @RequestMapping(value = "/cancelBottom")
    @ResponseBody
    @Transactional(rollbackFor = TipException.class)
    public RestResponseBo cancelBottom(@RequestParam int cid, HttpServletRequest request) {
        try {
        	ContentVo contentVo = new ContentVo();
        	contentVo.setCid(cid);
        	contentVo.setIsBottom(false);
        	contentService.updateContentByCid(contentVo);
            logService.insertLog(LogActions.CANCELTOP_ARTITLE.getAction(), cid + "", request.getRemoteAddr(), this.getUid(request));
        } catch (Exception e) {
            String msg = "文章取消置顶失败";
            return ExceptionHelper.handlerException(logger, msg, e);
        }
        return RestResponseBo.ok();
    } 
}
