package me.junbin.gradprj.web;

import com.google.gson.JsonObject;

/**
 * @author : Zhong Junbin
 * @email : <a href="mailto:rekadowney@163.com">发送邮件</a>
 * @createDate : 2017/3/25 11:40
 * @description :
 */
public class BaseRestController extends BasePoController {


    protected JsonObject getSuccessResult() {
        return getSuccessResult(null);
    }

    protected JsonObject getSuccessResult(String msg) {
        JsonObject res = new JsonObject();
        res.addProperty("status", true);
        res.addProperty("msg", msg);
        return res;
    }

    protected JsonObject getFailResult() {
        return getFailResult(null);
    }

    protected JsonObject getFailResult(String msg) {
        JsonObject res = new JsonObject();
        res.addProperty("status", false); // 空串代表 false，也可以使用 boolean 的 false
        res.addProperty("msg", msg);
        return res;
    }


}
