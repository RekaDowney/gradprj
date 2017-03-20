<%--
  Created by IntelliJ IDEA.
  User: Zhong Junbin
  Date: 2017/2/23 22:05
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/include/staticRef.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>登陆</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel='stylesheet' type="text/css" href='http://fonts.googleapis.com/css?family=PT+Sans:400,700'>
    <link rel="stylesheet" type="text/css" href="${baseUrl}/resources/login2/css/reset.css">
    <link rel="stylesheet" type="text/css" href="${baseUrl}/resources/login2/css/supersized.css">
    <link rel="stylesheet" type="text/css" href="${baseUrl}/resources/login2/css/style.css">
</head>
<body>

<div class="page-container">
    <h1>Login</h1>
    <form action="${baseUrl}/login" method="post">
        <input type="text" name="principal" class="principal" value="${principal}" placeholder="用户名"/>
        <input type="password" name="password" class="password" placeholder="密码"/>
        <c:if test="${not empty sessionScope.errorTime and sessionScope.errorTime ge 3}">
            <div class="captchaContainer">
                <img id="captchaImg" src="${baseUrl}/captcha" style="cursor: pointer;float: left;" width="120"
                     height="42">　　
                <input type="text" name="kaptcha" class="captcha" placeholder="验证码" style="float: right;"/>
            </div>
        </c:if>
        <input type="hidden" name="loginAsVisitor" value="false" class="loginAsVisitor"/>

        <c:if test="${not empty loginError}">
            <div id="errorTip">
                <div style="padding: 13px 0;">${loginError}</div>
            </div>
        </c:if>

        <div>
            <button id="asVisitor" type="submit" style="float: left;">以游客身份访问</button>
            <button id="login" type="submit" style="float: right;">登陆</button>
        </div>

        <div class="error"><span>+</span></div>
        <div id="loginType" style="display: none;">login</div>
    </form>
</div>

<!-- Javascript -->
<script src="${baseUrl}/resources/login2/js/jquery-1.8.2.min.js"></script>
<script src="${baseUrl}/resources/login2/js/supersized.3.2.7.min.js"></script>
<script src="${baseUrl}/resources/login2/js/supersized-init.js"></script>
<script src="${baseUrl}/resources/login2/js/validate.js"></script>

<script type="text/javascript">
    function clearErrorTip() {
        document.getElementById('errorTip').style.display = 'none';
    }

    $(function () {

        $("#asVisitor").off('click').on('click', function (e) {
            $("#loginType").text('loginAsVisitor');
        });

        $("#login").off('click').on('click', function (e) {
            $("#loginType").text('login');
        });

        var captcha = $("#captchaImg");
        captcha.on("click", function () {
            // 淡出淡入效果
            captcha.hide(30)
                    .attr("src", "${baseUrl}/captcha?random=" + new Date().getTime())
                    .fadeIn(50);
        });
    });
</script>
</body>
</html>