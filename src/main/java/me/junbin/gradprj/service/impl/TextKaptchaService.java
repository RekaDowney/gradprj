package me.junbin.gradprj.service.impl;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import me.junbin.gradprj.service.CaptchaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Properties;

/**
 * @author : Zhong Junbin
 * @email : <a href="mailto:rekadowney@163.com">发送邮件</a>
 * @createDate : 2017/2/7 20:25
 * @description :
 */
@Service(value = "textKaptchaService")
public class TextKaptchaService implements CaptchaService {

    private DefaultKaptcha kaptcha;
    private static final Logger log = LoggerFactory.getLogger(TextKaptchaService.class);

    public TextKaptchaService() {
        Properties properties = new Properties();
        String location = "classpath:bundle/kaptcha.properties";
        log.debug("加载 kaptcha 配置文件 {}", location);
        Resource resource = new DefaultResourceLoader().getResource(location);
        try {
            properties.load(resource.getInputStream());
        } catch (IOException e) {
            log.error("加载资源文件失败！异常：{}", e.getMessage());
            throw new RuntimeException(e);
        }
        Config config = new Config(properties);
        kaptcha = new DefaultKaptcha();
        kaptcha.setConfig(config);
    }

    @Override
    public String createText() {
        return kaptcha.createText();
    }

    @Override
    public BufferedImage createImage(String text) {
        return kaptcha.createImage(text);
    }
}
