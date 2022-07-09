package springboot.controller.admin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.framework.util.ShiroUtils;
import com.ruoyi.system.domain.SysUser;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import springboot.constant.WebConst;
import springboot.dto.Types;
import springboot.modal.vo.AttachVo;
import springboot.service.IAttachService;
import springboot.service.ILogService;
import springboot.util.MyUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;

@Controller
@RequestMapping("/admin/attach")
public class MdUploadImg {


    public static final String CLASSPATH = MyUtils.getUploadFilePath();

    @Resource
    private IAttachService attachService;

    @Resource
    private ILogService logService;

    @PostMapping("/uploadImg")
    @ResponseBody
    public JSONObject uploadImg(@RequestParam(value = "editormd-image-file", required = false) MultipartFile multipartFile, HttpServletRequest request) {
        // 使用自定义的上传路径

        JSONObject result = new JSONObject();
        SysUser user = ShiroUtils.getSysUser();
        Integer uid = user.getUserId().intValue();

        AttachVo attach = new AttachVo();
        try {
            String name = multipartFile.getOriginalFilename();
            //这里控制　文件(图片，文本等)大小  不能超过1Ｍ
            if (multipartFile.getSize() <= WebConst.MAX_FILE_SIZE) {
                // 获取文件相对路径名，并文件目录
                String fkey = MyUtils.getFileKey(name);
                // 判断文件是否是图片
                String ftype = MyUtils.isImage(multipartFile.getInputStream()) ? Types.IMAGE.getType() : Types.FILE.getType();
                File file = new File(CLASSPATH + fkey);
                FileCopyUtils.copy(multipartFile.getInputStream(), new FileOutputStream(file));
                attachService.save(name, fkey, ftype, uid);
                attach.setFkey(fkey);

            }
        } catch (Exception e) {
            result.put("msg", "文件上传失败");
            return result;
        }
        return JSON.parseObject(JSON.toJSONString(attach));
    }

}