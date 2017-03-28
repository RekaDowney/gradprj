<%--
  Created by IntelliJ IDEA.
  User: Zhong Junbin
  Date: 2017/3/25 22:44
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/include/staticRef.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>更新账户</title>
    <link type="text/css" rel="stylesheet" href="${baseUrl}/resources/bootstrap/css/bootstrap.min.css"/>
    <%--<link type="text/css" rel="stylesheet" href="${baseUrl}/resources/backend/css/font-awesome.min.css"/>--%>
    <%--<link type="text/css" rel="stylesheet" href="${baseUrl}/resources/backend/css/animate.css"/>--%>

    <link type="text/css" rel="stylesheet" href="${baseUrl}/resources/css/require.css"/>

    <script type="text/javascript" src="${baseUrl}/resources/layer/layer.js"></script>
    <script type="text/javascript" src="${baseUrl}/resources/bootstrap/js/bootstrap.min.js"></script>

    <style type="text/css">
        #accountForm .form-group {
            margin-left: 20px;
        }
    </style>
</head>

<body>
<form role="form" id="accountForm" style="width: 100%;height: 100%;padding-left: 10px;">
    <hr/>
    <div class="form-group" style="margin-top: 10px">
        <label for="srcPwd">原密码</label>
        <input id="srcPwd" autocomplete="off" type="text" name="srcPwd"
               onfocus="this.type = 'password'"
               class="form-control"
               style="width: 200px;"/>
    </div>
    <div class="form-group">
        <label for="newPwd">新密码</label>
        <input id="newPwd" autocomplete="off" type="text" name="newPwd"
               onfocus="this.type = 'password'"
               class="form-control"
               style="width: 200px;"/>
    </div>
    <div class="form-group">
        <label for="cfmPwd">确认新密码</label>
        <input id="cfmPwd" autocomplete="off" type="text" name="cfmPwd"
               onfocus="this.type = 'password'"
               class="form-control"
               style="width: 200px;"/>
    </div>
    <div class="form-group">
        <button id="modify" type="button" class="btn btn-primary pull-left">确定</button>
    </div>
</form>

<script type="text/javascript">

    $('#accountForm').on('click', '#modify', function () {
        if (validate()) {
            var formData = new FormData($('#accountForm')[0]);
            $.ajax({
                url: "${baseUrl}/account/pwd/modify",
                type: "POST",
                data: formData,
                async: false,
                cache: false,
                contentType: false,
                processData: false,
                success: function (data) {
                    if (data.status) {
                        $('#srcPwd').val('');
                        $('#newPwd').val('');
                        $('#cfmPwd').val('');
                        layer.open({
                            type: 0,
                            icon: 6,
                            title: "成功",
                            content: data.msg,
                            shadeClose: true, // 开启遮罩关闭
                            resize: false
                        });
                    } else {
                        showError(data.msg);
                    }
                }
            });

        }
    }).on('focus', 'input', function () {
        $(this).parent('div.form-group').removeClass('has-error');
    });

    function validate() {
        var srcPwd = $('#srcPwd');
        if (srcPwd.val().length < 6) {
            srcPwd.parent('div.form-group').addClass('has-error');
            showError("原密码的长度至少是6位数");
            return false;
        }

        var pwd = $('#newPwd');
        if (pwd.val().length < 6) {
            pwd.parent('div.form-group').addClass('has-error');
            showError("请输入密码（密码长度至少6位数）");
            return false;
        }
        var rePwd = $('#cfmPwd');
        if (rePwd.val() !== pwd.val()) {
            pwd.parent('div.form-group').addClass('has-error');
            showError("两次输入的密码不一致");
            return false;
        }

        return true;
    }

    function showError(msg) {
        layer.open({
            type: 0,
            icon: 5,
            title: "错误",
            content: msg,
            shadeClose: true, // 开启遮罩关闭
            resize: false
        });
    }
</script>
</body>
</html>
