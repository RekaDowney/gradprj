package me.junbin.gradprj.listener;

import me.junbin.gradprj.util.WebAppCtxHolder;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;

/**
 * @author : Zhong Junbin
 * @email : <a href="mailto:rekadowney@163.com">发送邮件</a>
 * @createDate : 2017/2/22 21:08
 * @description : 参考 <a href="http://www.tuicool.com/articles/VbaIviF">spring 容器加载完成后执行某个方法</a>
 * <p>
 * 但是这个时候，会存在一个问题，在web 项目中（spring mvc），
 * 系统会存在两个容器，一个是root application context ,
 * 另一个就是我们自己的 projectName-servlet context（作为root application context的子容器）。
 * <p>
 * 这种情况下，就会造成onApplicationEvent方法被执行两次。
 * 为了避免上面提到的问题，我们可以只在root application context初始化完成后调用逻辑代码，
 * 其他的容器的初始化完成，则不做任何处理，修改后代码
 */
@Service("webAppListener")
public class WebAppListener implements ApplicationListener<ContextRefreshedEvent> {

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        // 只有当加载的容器是 Root WebApplicationContext 时才执行setter 注入
        if (event.getApplicationContext().getParent() == null) {
            WebAppCtxHolder.setAppCtx(event.getApplicationContext());
        }
    }

}
