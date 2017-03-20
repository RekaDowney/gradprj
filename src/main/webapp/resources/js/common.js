/**
 * @author : Zhong Junbin
 * @email : <a href="mailto:rekadowney@163.com">发送邮件</a>
 * @createDate : 2017/3/12 13:16
 * @description :
 */
(function ($) {
    $.fn.isArray = function (variable) {
        return Object.prototype.toString.call(variable) === "[object Array]";
    };

    $.fn.isObject = function (variable) {
        return Object.prototype.toString.call(variable) === "[object Object]";
        /*
         return Object.prototype.toString.call(variable) === "[object Object]" &&
         (typeof variable) === "object";
         */
    };

    $.fn.isFunction = function (variable) {
        return Object.prototype.toString.call(variable) === "[object Function]";
    }

})(jQuery);

// 每页默认显示记录条数
var DEFAULT_PAGE_SIZE = 10;