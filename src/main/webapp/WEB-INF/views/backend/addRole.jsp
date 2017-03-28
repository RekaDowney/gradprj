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
    <title>添加角色</title>
    <link type="text/css" rel="stylesheet" href="${baseUrl}/resources/css/common.css"/>
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
<form role="form" id="roleForm" style="width: 100%;height: 100%;padding-left: 10px;">
    <hr/>
    <div class="form-group">
        <label for="roleName">角色英文名称</label>
        <input id="roleName" type="text" name="roleName" class="form-control" style="width: 200px;"/>
    </div>
    <div class="form-group">
        <label for="roleNameCn">角色中文名称</label>
        <input id="roleNameCn" type="text" name="roleNameCn" class="form-control" style="width: 200px;"/>
    </div>
    <div class="form-group">
        <label for="remarks">备注</label>
        <textarea id="remarks" name="remarks" class="form-control" style="width: 200px;height: 200px;"></textarea>
        <%--<input id="remarks" type="text" name="remarks" class="form-control" style="width: 200px;"/>--%>
    </div>
    <button id="create" type="button" class="btn btn-primary pull-left">确定</button>
</form>


<script type="text/javascript">

    $('#roleForm').on('click', '#create', function () {
        if (validate()) {
            var formData = new FormData($('#roleForm')[0]);
            $.ajax({
                url: "${baseUrl}/role/append",
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
    }).on('focus', 'input', function () {
        $(this).parent('div.form-group').removeClass('has-error');
    });

    function validate() {
        var roleName = $('#roleName');
        if (roleName.val().length <= 0) {
            roleName.parent('div.form-group').addClass('has-error');
            showError("请输入角色英文名称");
            return false;
        }
        var roleNameCn = $('#roleNameCn');
        if (roleNameCn.val().length <= 0) {
            roleNameCn.parent('div.form-group').addClass('has-error');
            showError("请输入角色中文名称");
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
