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
</head>
<body>
<div class="container">
    <div class="btnContainer">
        <button>
            <a href="${document.docUrl}">弹出式编辑</a>
        </button>
        <shiro:hasPermission name="doc:*:download">
            <a href="${baseUrl}/doc/${document.id}/download">下载</a>
        </shiro:hasPermission>
        <button>
            <a href="${baseUrl}/doc/${document.categoryId}/list">返回列表页</a>
        </button>
    </div>
    <div class="officeContainer">
        <po:PageOfficeCtrl id="docCtrl"/>
    </div>
</div>

<script type="text/javascript">

    var doc = document.getElementById("docCtrl");

    // 全屏切换
    function fullScreenSwitch() {
        doc.FullScreen = !doc.FullScreen;
    }

    function saveFile() {
        doc.WebSave();
    }

</script>
</body>
</html>
