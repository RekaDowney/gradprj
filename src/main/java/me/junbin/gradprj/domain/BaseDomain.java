package me.junbin.gradprj.domain;

import me.junbin.commons.gson.Gsonor;

/**
 * @author : Zhong Junbin
 * @email : <a href="mailto:rekadowney@163.com">发送邮件</a>
 * @createDate : 2017/2/2 10:45
 * @description :
 */
public class BaseDomain {

    protected String id;
    protected boolean valid = true;

    public BaseDomain() {
    }

    @Override
    public String toString() {
        return Gsonor.SIMPLE.toJson(this);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }


}
