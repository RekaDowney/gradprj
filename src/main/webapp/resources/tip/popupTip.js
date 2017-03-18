/**
 * @author : Zhong Junbin
 * @email : <a href="mailto:rekadowney@163.com">发送邮件</a>
 * @createDate : 2017/3/6 20:17
 * @description :
 */
var Tip = function () {
};

function closeTip(element) {
    if (typeof  element !== 'undefined') {
        element.fadeOut('fast', function () {
            $(this).remove();
        });
    } else {
        $('.alert').fadeOut('fast', function () {
            $(this).remove();
        })
    }
}

/**
 * 页面右下角弹出危险提示框
 * @param text 危险信息
 * @param mills 多少毫秒后隐藏提示框，默认为 3000 毫秒
 */
Tip.danger = function (text, mills) {
    var html;
    var ms = 3000;
    if (mills > 0) {
        ms = mills;
    }
    var content = $('#dm-notify');
    html = $("<div class='dm-notification dm-notify-danger'>" + text + "</div>");
    content.append(html);

    html.on('click', function () {
        closeTip($(this));
    });

    setTimeout(function () {
        closeTip(content.children('.dm-notification').first());
    }, ms);
};


/**
 * 页面右下角弹出警告提示框
 * @param text 警告信息
 * @param mills 多少毫秒后隐藏提示框，默认为 3000 毫秒
 */
Tip.warning = function (text, mills) {
    var html;
    var ms = 3000;
    if (mills > 0) {
        ms = mills;
    }
    var content = $('#dm-notify');
    html = $("<div class='dm-notification dm-notify-warning'>" + text + "</div>");
    content.append(html);

    html.on('click', function () {
        closeTip($(this));
    });

    setTimeout(function () {
        closeTip(content.children('.dm-notification').first());
    }, ms);
};

/**
 * 页面右下角弹出通知提示框
 * @param text 通知信息
 * @param mills 多少毫秒后隐藏提示框，默认为 3000 毫秒
 */
Tip.info = function (text, mills) {
    var html;
    var ms = 3000;
    if (mills > 0) {
        ms = mills;
    }
    var content = $('#dm-notify');
    html = $("<div class='dm-notification dm-notify-info'>" + text + "</div>");
    content.append(html);

    html.on('click', function () {
        closeTip($(this));
    });

    setTimeout(function () {
        closeTip(content.children('.dm-notification').first());
    }, ms);
};

/**
 * 页面右下角弹出成功提示框
 * @param text 成功信息
 * @param mills 多少毫秒后隐藏提示框，默认为 3000 毫秒
 */
Tip.success = function (text, mills) {
    var html;
    var ms = 3000;
    if (mills > 0) {
        ms = mills;
    }
    var content = $('#dm-notify');
    html = $("<div class='dm-notification dm-notify-success'>" + text + "</div>");
    content.append(html);

    html.on('click', function () {
        closeTip($(this));
    });

    setTimeout(function () {
        closeTip(content.children('.dm-notification').first());
    }, ms);
};

/**
 * 页面右下角弹出首选项提示框
 * @param text 首选项信息
 * @param mills 多少毫秒后隐藏提示框，默认为 3000 毫秒
 */
Tip.primary = function (text, mills) {
    var html;
    var ms = 3000;
    if (mills > 0) {
        ms = mills;
    }
    var content = $('#dm-notify');
    html = $("<div class='dm-notification dm-notify-primary'>" + text + "</div>");
    content.append(html);

    html.on('click', function () {
        closeTip($(this));
    });

    setTimeout(function () {
        closeTip(content.children('.dm-notification').first());
    }, ms);
};

(function ($) {
    // 为所有加载了 popupTip 的页面添加一个信息提示框容器
    $(function () {
        $("body").append("<div id='dm-notify'></div>");
    });
})(jQuery);
