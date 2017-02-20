package me.junbin.gradprj.enumeration;

import me.junbin.commons.converter.custom.CustomEnum;
import me.junbin.commons.util.Args;

import java.util.NoSuchElementException;

/**
 * @author : Zhong Junbin
 * @email : <a href="mailto:rekadowney@163.com">发送邮件</a>
 * @createDate : 2017/1/31 19:46
 * @description :
 */
public enum PermType implements CustomEnum<PermType> {

    /**
     * 菜单类权限分为 分类栏目下的 XX 学院，XX 专业，XX 年级；
     * 此外还有专题栏目，最新入库栏目，优质文档栏目，求助栏目 等。
     * 某种程度上 栏目 与 菜单 能够起到相同的作用。但菜单的范围较栏目要更细小一些，因此使用菜单作为权限依据
     */
    MENU("菜单类", "menu"),

    /**
     * 功能类权限分为 文档浏览，文档格式转换，文档下载，文档上传，请假 等。
     * 某种程度上 菜单 实际上就是一个 功能 的载体，但因为有些菜单只是起到导向作用，因此这里将两者分离出来
     *
     * @description 功能类权限都没有父权限
     */
    FUNCTION("功能类", "function"),

    /**
     * 管理类权限分为 用户管理，角色管理，权限管理，文档管理 等。
     * 管理类权限 permUrl 属性不为空，该地址是相对于 contextPath 而言的，通过该地址可以进入相应资源的管理页面。
     * 某种程度上 管理 也是属于菜单，但是他是一种相当特殊的菜单，它的地址通常可以固定化，因此也将两者分离出来
     *
     * @description 管理类权限必须有 permUrl
     */
    MANAGE("管理类", "manage");

    private final String displayName;
    private final String name;

    PermType(String displayName, String name) {
        this.displayName = displayName;
        this.name = name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getName() {
        return name;
    }

    @Override
    public String convert() {
        return this.displayName;
    }

    @Override
    public PermType recover(String data) {
        return parse(data);
    }

    public static PermType of(final String nameOrDisplayName) {
        return parse(nameOrDisplayName);
    }

    public static PermType parse(final String name) {
        String notNullName = Args.notNull(name).trim().toLowerCase();
        switch (notNullName) {
            case "菜单类":
            case "menu":
                return MENU;
            case "功能类":
            case "function":
                return FUNCTION;
            case "管理类":
            case "manage":
                return MANAGE;
            default:
                throw new NoSuchElementException(name);
        }
    }

}
