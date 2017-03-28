<%--
  Created by IntelliJ IDEA.
  User: Zhong Junbin
  Date: 2017/3/25 22:39
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/include/staticRef.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>添加账户</title>
    <link type="text/css" rel="stylesheet" href="${baseUrl}/resources/bootstrap/css/bootstrap.min.css"/>
    <%--<link type="text/css" rel="stylesheet" href="${baseUrl}/resources/backend/css/font-awesome.min.css"/>--%>
    <%--<link type="text/css" rel="stylesheet" href="${baseUrl}/resources/backend/css/animate.css"/>--%>

    <link type="text/css" rel="stylesheet" href="${baseUrl}/resources/css/require.css"/>

    <script type="text/javascript" src="${baseUrl}/resources/js/jquery.js"></script>
    <script type="text/javascript" src="${baseUrl}/resources/js/common.js"></script>
    <script type="text/javascript" src="${baseUrl}/resources/layer/layer.js"></script>
    <script type="text/javascript" src="${baseUrl}/resources/bootstrap/js/bootstrap.min.js"></script>

    <style type="text/css">
    </style>
</head>

<body>
<form role="form" id="accountForm" style="width: 100%;height: 100%;padding-left: 10px;">
    <hr/>
    <div class="form-group">
        <label for="new_principal">账户名称（仅支持大小写字母）</label>
        <input id="new_principal" type="text" name="aPrincipal" class="form-control" style="width: 200px;"/>
    </div>
    <div class="form-group">
        <label for="aPassword">账户密码</label>
        <input id="aPassword" autocomplete="off" type="text" onfocus="this.type ='password'" name="aPassword"
               class="form-control"
               style="width: 200px;"/>
    </div>
    <div class="form-group">
        <label for="rePassword">确认密码</label>
        <input id="rePassword" autocomplete="off" type="text" onfocus="this.type ='password'" name="rePassword"
               class="form-control"
               style="width: 200px;"/>
    </div>
    <button id="create" type="button" class="btn btn-primary pull-left">确定</button>
</form>

<script type="text/javascript">

    var baseUrl = "${baseUrl}";

    $('#accountForm').on('click', '#create', function () {
        if (validate()) {
            var formData = new FormData($('#accountForm')[0]);
            $.ajax({
                url: "${baseUrl}/account/append",
                type: "POST",
                data: formData,
                async: false,
                cache: false,
                contentType: false,
                processData: false,
                success: function (data) {
                    parent.$('#iframeResult').val(JSON.stringify(data)); // 为父页面的 iframeCallback 隐藏域设置值
                    var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
                    parent.layer.close(index); //再执行关闭
                }
            });
        }
    }).on('blur', '#new_principal', function () {
        var principal = $(this).val();
        if ($.fn.isPureCharacterString(principal)) {
            $.ajax({
                url: baseUrl + "/account/" + principal + "/exists",
                type: "POST",
                success: function (data) {
                    if (data.exists) {
                        $('#new_principal').val('');
                        showError("账户名“" + principal + "”已经存在，请重新输入账户名");
                    }
                }
            });
        }
    }).on('focus', 'input', function () {
        $(this).parent('div.form-group').removeClass('has-error');
    });

    function validate() {
        var principal = $('#new_principal');
        if (!$.fn.isPureCharacterString(principal.val())) {
            principal.parent('div.form-group').addClass('has-error');
            showError("请输入账户名称（仅支持大小写字母）");
            return false;
        }
        /*
         if (principal.val().length <= 0) {
         principal.parent('div.form-group').addClass('has-error');
         showError("请输入账户名称（仅支持大小写字母）");
         return false;
         } else {
         var arr = principal.val().match(/[a-zA-Z]+/);
         if (!(arr && arr.length == 1 && arr[0] === principal.val())) {
         principal.parent('div.form-group').addClass('has-error');
         showError("请输入账户名称（仅支持大小写字母）");
         return false;
         }
         }
         */
        var pwd = $('#aPassword');
        if (pwd.val().length <= 0) {
            pwd.parent('div.form-group').addClass('has-error');
            showError("请输入密码");
            return false;
        }
        var rePwd = $('#rePassword');
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
