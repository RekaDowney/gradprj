package me.junbin.gradprj.enumeration;

import me.junbin.commons.converter.gson.enumeration.GsonEnum;
import me.junbin.commons.util.Args;

import java.util.NoSuchElementException;

/**
 * @author : Zhong Junbin
 * @email : <a href="mailto:rekadowney@163.com">发送邮件</a>
 * @createDate : 2017/1/31 19:46
 * @description :
 */
public enum PermType implements GsonEnum<PermType> {

    CHANNEL("栏目", "channel"), MENU("菜单", "menu");

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
    public String serialize() {
        return this.displayName;
    }

    @Override
    public PermType deserialize(String json) {
        return parse(json);
    }

    public static PermType of(final String nameOrDisplayName) {
        return parse(nameOrDisplayName);
    }

    public static PermType parse(final String name) {
        String notNullName = Args.notNull(name).trim().toLowerCase();
        switch (notNullName) {
            case "栏目":
            case "channel":
                return CHANNEL;
            case "菜单":
            case "menu":
                return MENU;
            default:
                throw new NoSuchElementException();
        }
    }

}
