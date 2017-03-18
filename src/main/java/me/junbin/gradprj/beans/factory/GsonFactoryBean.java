package me.junbin.gradprj.beans.factory;

import com.google.gson.Gson;
import me.junbin.gradprj.enumeration.MyGsonor;
import org.springframework.beans.factory.FactoryBean;

/**
 * @author : Zhong Junbin
 * @email : <a href="mailto:rekadowney@163.com">发送邮件</a>
 * @createDate : 2017/2/23 20:12
 * @description :
 */
public class GsonFactoryBean implements FactoryBean<Gson> {

    @Override
    public Gson getObject() throws Exception {
        return MyGsonor.SN_SIMPLE.getGson();
    }

    @Override
    public Class<?> getObjectType() {
        return Gson.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

}
