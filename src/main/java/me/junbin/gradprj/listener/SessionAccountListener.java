package me.junbin.gradprj.listener;

import me.junbin.gradprj.util.AccountMgr;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionListener;
import org.apache.shiro.session.SessionListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpSessionListener;
import java.io.Serializable;

/**
 * @author : Zhong Junbin
 * @email : <a href="mailto:rekadowney@163.com">发送邮件</a>
 * @createDate : 2017/3/26 14:03
 * @description : 由于使用 Shiro，此时如果想要管理 Session 的话必须就不能使用
 * {@link HttpSessionListener} 了，而是应该继承 {@link SessionListenerAdapter }
 * 或者实现 {@link SessionListener}
 */
public class SessionAccountListener implements SessionListener {

    private static final Logger log = LoggerFactory.getLogger(SessionAccountListener.class);

/*
    public void sessionCreated(HttpSessionEvent se) {
        HttpSession session = se.getSession();
        String sessionId = session.getId();
        AccountMgr.store(sessionId);
    }

    public void sessionDestroyed(HttpSessionEvent se) {
        HttpSession session = se.getSession();
        AccountMgr.remove(session.getId());
    }
*/

    // 会话创建时触发
    @Override
    public void onStart(Session session) {
        Serializable sessionId = session.getId();
        log.debug("会话创建：{}", sessionId);
        AccountMgr.store(sessionId.toString());
    }

    // 会话停止时触发，调用 session.invalidate() 或者 subject.logout()
    @Override
    public void onStop(Session session) {
        Serializable sessionId = session.getId();
        log.debug("会话停止：{}", sessionId);
        AccountMgr.remove(sessionId.toString());
    }

    // 会话过期时触发
    @Override
    public void onExpiration(Session session) {
        Serializable sessionId = session.getId();
        log.debug("会话过期：{}", sessionId);
        AccountMgr.remove(sessionId.toString());
    }

}
