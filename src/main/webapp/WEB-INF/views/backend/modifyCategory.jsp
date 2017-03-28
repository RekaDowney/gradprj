<%--
  Created by IntelliJ IDEA.
  User: Zhong Junbin
  Date: 2017/3/24 21:10
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/include/staticRef.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>更新栏目</title>
    <link type="text/css" rel="stylesheet" href="${baseUrl}/resources/css/common.css"/>
    <link type="text/css" rel="stylesheet" href="${baseUrl}/resources/bootstrap/css/bootstrap.min.css"/>
    <%--<link type="text/css" rel="stylesheet" href="${baseUrl}/resources/backend/css/font-awesome.min.css"/>--%>
    <%--<link type="text/css" rel="stylesheet" href="${baseUrl}/resources/backend/css/animate.css"/>--%>

    <script type="text/javascript" src="${baseUrl}/resources/js/jquery.js"></script>
    <script type="text/javascript" src="${baseUrl}/resources/js/common.js"></script>

    <script type="text/javascript" src="${baseUrl}/resources/layer/layer.js"></script>
    <script type="text/javascript" src="${baseUrl}/resources/bootstrap/js/bootstrap.min.js"></script>

    <style type="text/css">
        hr {
            margin-top: 0;
            margin-bottom: 10px;
            margin-right: 10px;
            border: 0;
            border-top: 1px solid #8C0C0C;
        }
    </style>
</head>

<body>
<form role="form" id="categoryForm" style="width: 100%;height: 100%;padding-left: 10px;">
    <hr/>
    <div class="form-group">
        <label for="permName">栏目名称</label>
        <input id="permName" type="text" name="permName" value="${perm.permName}" class="form-control"
               style="width: 200px;"/>
    </div>
    <div class="form-group">
        <label for="weight">栏目权重（权重越大排序越靠后）</label>
        <input id="weight" type="text" name="weight" value="${perm.weight}" class="form-control" style="width: 200px;"/>
    </div>
    <c:choose>
        <c:when test="${perm.attachable}">
            <div class="form-group">
                该栏目是否可以上传文档
                <label>
                    <input type="radio" name="attachable" checked="checked" value="true"/>　是　
                </label>
                <label>
                    <input type="radio" name="attachable" value="false"/>　否　
                </label>
            </div>
        </c:when>
        <c:otherwise>
            <div class="form-group">
                该栏目是否可以上传文档
                <label>
                    <input type="radio" name="attachable" value="true"/>　是　
                </label>
                <label>
                    <input type="radio" name="attachable" checked="checked" value="false"/>　否　
                </label>
            </div>
        </c:otherwise>
    </c:choose>
    <input id="categoryId" type="hidden" value="${categoryId}"/>
    <button id="create" type="button" class="btn btn-primary pull-left">确定</button>
</form>

<script type="text/javascript">

    $('#categoryForm').on('click', '#create', function () {
        if (validate()) {
            var formData = new FormData($('#categoryForm')[0]);

            $.ajax({
                url: "${baseUrl}/category/" + $("#categoryId").val() + "/modify",
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
        var pn = $('#permName');
        if (pn.val().trim().length <= 0) {
            pn.parent('div.form-group').addClass('has-error');
            showError("请输入栏目名称");
            return false;
        }
        var weight = $('#weight');
        if (!(/[0-9]+/.test(weight.val()))) {
            weight.parent('div.form-group').addClass('has-error');
            showError("请输入权重（权重必须是纯数字）");
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
