package springboot.dto;

/**
 * 日志表的action字段
 * Created by tangj on 2017/3/4.
 */
public enum LogActions {

    LOGIN("登录后台"), UP_PWD("修改密码"), UP_INFO("修改个人信息"),
    DEL_ARTICLE("删除文章"), DEL_PAGE("删除页面"), SYS_BACKUP("系统备份"),
    SYS_SETTING("保存系统设置"), INIT_SITE("初始化站点"),DEL_ATTACH("删除附件"),
    SETTOP_ARTITLE("文章置顶"),CANCELTOP_ARTITLE("文章取消置顶"),SETLOCKED_ARTITLE("文章锁操作");

    private String action;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    LogActions(String action) {
        this.action = action;
    }
}
