package me.junbin.gradprj.web.exception;

import me.junbin.gradprj.util.Global;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author : Zhong Junbin
 * @email : <a href="mailto:rekadowney@163.com">发送邮件</a>
 * @createDate : 2017/2/25 18:30
 * @description : 由异常控制器发起重定向请求将异常
 */
@Controller
public class ExceptionController {

    @RequestMapping(value = "/error/50x", method = RequestMethod.POST)
    public String error50x(@ModelAttribute RuntimeException e, Model model) {
        model.addAttribute(Global.ERROR_50X_KEY, e);
        return "ex/50x";
    }

    @RequestMapping(value = "/error/40x", method = RequestMethod.POST)
    public String error40x(@ModelAttribute RuntimeException e, Model model) {
        model.addAttribute(Global.ERROR_40X_KEY, e);
        return "ex/40x";
    }

    @RequestMapping(value = "/error/unauthorized", method = RequestMethod.GET)
    public String unauthorized() {
        return "ex/unauthorized";
    }

}
