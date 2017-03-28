<%--
  Created by IntelliJ IDEA.
  User: Zhong Junbin
  Date: 2017/3/12 17:07
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%-- PageOffice 不支持使用 include 引入其他 jsp 文件 --%>
<%--<%@include file="/WEB-INF/include/staticRef.jsp" %>--%>
<%@taglib prefix="po" uri="http://java.pageoffice.cn" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>并发</title>
    <%-- 订制通用变量 --%>
    <c:set var="scheme" value="${pageContext.request.scheme}"/>
    <c:set var="host" value="${pageContext.request.serverName}"/>
    <c:set var="port" value="${pageContext.request.serverPort}"/>
    <c:set var="ctx" value="${pageContext.request.contextPath}"/>
    <c:set var="baseUrl" value="${scheme}://${host}:${port}${ctx}"/>

    <link type="text/css" rel="stylesheet" href="${baseUrl}/resources/css/common.css"/>
    <script type="text/javascript" src="${baseUrl}/resources/js/jquery.js"></script>
    <link type="text/css" rel="stylesheet" href="${baseUrl}/resources/tip/popupTip.css"/>
    <script type="text/javascript" src="${baseUrl}/resources/tip/popupTip.js"></script>
</head>
<body>
<div style="width: 100%;height: 95%;">
    <po:PageOfficeCtrl id="docCtrl"/>
</div>
<%--<div style="width: 100%;height: 10%;"></div>--%>

<script type="text/javascript">

    var doc = document.getElementById("docCtrl");

    // 全屏切换
    function fullScreenSwitch() {
        doc.FullScreen = !doc.FullScreen;
    }

    /*
     // 保存
     function saveFile() {
     document.getElementById("excelCtrl").WebSave();
     }
     */

</script>
</body>
</html>
