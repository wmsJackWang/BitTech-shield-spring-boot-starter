package springboot.controller.admin;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageInfo;
import com.ruoyi.framework.util.ShiroUtils;
import com.ruoyi.system.domain.SysRole;
import com.ruoyi.system.domain.SysUser;
import com.vdurmont.emoji.EmojiParser;

import springboot.constant.WebConst;
import springboot.controller.AbstractController;
import springboot.controller.helper.ExceptionHelper;
import springboot.exception.TipException;
import springboot.modal.bo.RestResponseBo;
import springboot.modal.vo.CommentVo;
import springboot.modal.vo.CommentVoExample;
import springboot.modal.vo.UserVo;
import springboot.service.ICommentService;
import springboot.util.MyUtils;


/**
 * 评论模块
 *
 * @author tangj
 * @date 2018/1/27 17:05
 */
@Controller
@RequestMapping("admin/comments")
public class CommentController extends AbstractController {
    private static final Logger logger = LoggerFactory.getLogger(CommentController.class);

    @Resource
    private ICommentService commentServcie;

    @GetMapping(value = "")
    public String index(@RequestParam(value = "page", defaultValue = "1") int page,
                        @RequestParam(value = "limit", defaultValue = "15") int limit, HttpServletRequest request) {
//        UserVo users = this.user(request);
        SysUser user = ShiroUtils.getSysUser();
        CommentVoExample commentVoExample = new CommentVoExample();
        commentVoExample.setOrderByClause("coid desc");
        
        Integer userId = user.getUserId().intValue();
        for(SysRole role: user.getRoles())
        	if(role.getRoleKey().equals(WebConst.ADMIN_KEY))
        		userId = 0;
        if(userId==0)
        	;
        else 
        	commentVoExample.createCriteria().andOwnerIdEqualTo(user.getUserId().intValue());
        
        PageInfo<CommentVo> commentPaginator = commentServcie.getCommentsWithPage(commentVoExample, page, limit);
        request.setAttribute("comments", commentPaginator);
        return "admin/comment_list";
    }

    @PostMapping(value = "delete")
    @ResponseBody
    @Transactional(rollbackFor = TipException.class)
    public RestResponseBo delete(@RequestParam Integer coid) {
        try {

            CommentVo comments = commentServcie.getCommentById(coid);
            if (null == comments) {
                return RestResponseBo.fail("不存在该评论");
            }
            commentServcie.delete(coid, comments.getCid());
        } catch (Exception e) {
            String msg = "评论删除失败";
            return ExceptionHelper.handlerException(logger, msg, e);
        }
        return RestResponseBo.ok();
    }

    @PostMapping(value = "status")
    @ResponseBody
    @Transactional(rollbackFor = TipException.class)
    public RestResponseBo updateStatus(@RequestParam Integer coid, @RequestParam String status) {
        try {
            CommentVo comments = new CommentVo();
            comments.setCoid(coid);
            comments.setStatus(status);
            commentServcie.update(comments);
        } catch (Exception e) {
            String msg = "操作失败";
            return ExceptionHelper.handlerException(logger, msg, e);
        }
        return RestResponseBo.ok();
    }

    @PostMapping(value = "")
    @ResponseBody
    @Transactional(rollbackFor = TipException.class)
    public RestResponseBo reply(@RequestParam Integer coid, @RequestParam String content, HttpServletRequest request) {
        if (null == coid || StringUtils.isBlank(content)) {
            return RestResponseBo.fail("请输入完整后评论");
        }

        if (content.length() > 2000) {
            return RestResponseBo.fail("请输入2000个字符以内的回复");
        }

        CommentVo temp = commentServcie.getCommentById(coid);
        if (null == temp) {
            return RestResponseBo.fail("不存在该评论");
        }

//        UserVo users = this.user(request);
        SysUser user = ShiroUtils.getSysUser();
        content = MyUtils.cleanXSS(content);
        content = EmojiParser.parseToAliases(content);

        CommentVo comments = new CommentVo();
        comments.setAuthor(user.getUserName());
        comments.setAuthorId(user.getUserId().intValue());
        comments.setCid(temp.getCid());
        comments.setIp(request.getRemoteAddr());
//        comments.setUrl(user);
        comments.setContent(content);
        comments.setMail(user.getEmail());
        comments.setParent(coid);
        try {
            commentServcie.insertComment(comments);
            return RestResponseBo.ok();
        } catch (Exception e) {
            String msg = "回复失败";
            return ExceptionHelper.handlerException(logger, msg, e);
        }
    }
}
