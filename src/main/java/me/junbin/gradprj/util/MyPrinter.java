package me.junbin.gradprj.util;

import me.junbin.gradprj.domain.BaseDomain;

import java.util.Collection;

import static me.junbin.commons.ansi.ColorfulPrinter.green;
import static me.junbin.commons.ansi.ColorfulPrinter.yellow;

/**
 * @author : Zhong Junbin
 * @email : <a href="mailto:rekadowney@163.com">发送邮件</a>
 * @createDate : 2017/2/16 8:12
 * @description :
 */
public abstract class MyPrinter {

    public static void print(Collection<? extends BaseDomain> collection) {
        int count = 0;
        for (BaseDomain domain : collection) {
            if (count % 2 == 0) {
                green(domain);
            } else {
                yellow(domain);
            }
            count++;
        }
    }

}
