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
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>${document.docName}</title>
    <c:set var="scheme" value="${pageContext.request.scheme}"/>
    <c:set var="host" value="${pageContext.request.serverName}"/>
    <c:set var="port" value="${pageContext.request.serverPort}"/>
    <c:set var="ctx" value="${pageContext.request.contextPath}"/>
    <c:set var="baseUrl" value="${scheme}://${host}:${port}${ctx}"/>

    <link type="text/css" rel="stylesheet" href="${baseUrl}/resources/css/common.css"/>
    <link type="text/css" rel="stylesheet" href="${baseUrl}/resources/bootstrap/css/bootstrap.min.css"/>

    <script type="text/javascript" src="${baseUrl}/resources/js/jquery.js"></script>
    <link type="text/css" rel="stylesheet" href="${baseUrl}/resources/tip/popupTip.css"/>
    <script type="text/javascript" src="${baseUrl}/resources/tip/popupTip.js"></script>
    <script type="text/javascript" src="${baseUrl}/resources/js/common.js"></script>
</head>
<body>
<div class="container">
    <div class="btnContainer">
        <a class="btn btn-info" href="${document.docUrl}">
            弹出式编辑
        </a>
        <shiro:hasPermission name="doc:*:download">
            <a class="btn btn-primary" href="${baseUrl}/doc/${document.id}/download">下载</a>
        </shiro:hasPermission>
        <a id="returnPage" class="btn btn-info">返回列表页</a>
    </div>
    <div class="officeContainer">
        <po:PageOfficeCtrl id="docCtrl"/>
    </div>
</div>

<script type="text/javascript">

    $(function () {
        $("#returnPage").attr('href', "${baseUrl}/doc/${document.categoryId}/page/0/" + DEFAULT_PAGE_SIZE);
    });

    var doc = document.getElementById("docCtrl");

    // 全屏切换
    function fullScreenSwitch() {
        doc.FullScreen = !doc.FullScreen;
    }

    function saveFile() {
        doc.WebSave();
        Tip.success("保存成功！");
    }

</script>
</body>
</html>
