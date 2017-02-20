package me.junbin.gradprj.service;

import java.awt.image.BufferedImage;

/**
 * @author : Zhong Junbin
 * @email : <a href="mailto:rekadowney@163.com">发送邮件</a>
 * @createDate : 2017/2/7 20:23
 * @description :
 */
public interface CaptchaService {

    String createText();

    BufferedImage createImage(String text);

}