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
    };

    $.fn.isString = function (variable) {
        return Object.prototype.toString.call(variable) === "[object String]"
            && (typeof variable) === "string";
    };

    $.fn.isNumber = function (variable) {
        return Object.prototype.toString.call(variable) === "[object Number]"
            && (typeof variable) === "number";
    };

    $.fn.isBoolean = function (variable) {
        return Object.prototype.toString.call(variable) === "[object Boolean]"
            && (typeof variable) === "boolean";
    };

    $.fn.isUndefined = function (variable) {
        return Object.prototype.toString.call(variable) === "[object Undefined]"
            && (typeof variable) === "undefined";
    };

    $.fn.isNull = function (variable) {
        return Object.prototype.toString.call(variable) === "[object Null]";
    };

    $.fn.isFunction = function (variable) {
        return Object.prototype.toString.call(variable) === "[object Function]";
    };

    $.fn.doMatch = function (str, regex) {
        if (!!str) { // 只有字符串存在才执行判断
            str += '';
            var arr = str.match(regex);
            if (arr && arr.length == 1) {
                return arr[0] === str;
            }
        }
        return false;
    };

    $.fn.isPureCharacterString = function (str) {
        var regex = /[a-zA-Z]+/;
        return $.fn.doMatch(str, regex);
    };

    $.fn.isPureNumberString = function (str) {
        var regex = /[0-9]+/;
        return $.fn.doMatch(str, regex);
    };

})(jQuery);

// 每页默认显示记录条数
var DEFAULT_PAGE_SIZE = 10;

