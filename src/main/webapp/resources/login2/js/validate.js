jQuery(document).ready(function () {

    // 登陆验证
    $('.page-container form').submit(function () {
        var loginType = $("#loginType").text();
        if (loginType === 'loginAsVisitor') {
            $(".loginAsVisitor").val(true);
            return true;
        }
        var principal = $(this).find('.principal').val();
        var password = $(this).find('.password').val();
        if (principal == '') {
            $(this).find('.error').fadeOut('fast', function () {
                $(this).css('top', '27px');
            });
            $(this).find('.error').fadeIn('fast', function () {
                $(this).parent().find('.username').focus();
            });
            return false;
        }
        if (password == '') {
            $(this).find('.error').fadeOut('fast', function () {
                $(this).css('top', '96px');
            });
            $(this).find('.error').fadeIn('fast', function () {
                $(this).parent().find('.password').focus();
            });
            return false;
        }
        return true;
    });

    $('.page-container form .username, .page-container form .password').keyup(function () {
        $(this).parent().find('.error').fadeOut('fast');
    });

});
