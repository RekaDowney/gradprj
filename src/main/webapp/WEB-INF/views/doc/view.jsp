<%--
  Created by IntelliJ IDEA.
  User: Zhong Junbin
  Date: 2017/3/12 17:07
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%-- PageOffice 不支持使用 include 引入其他 jsp 文件 --%>
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
    <script type="text/javascript" src="${baseUrl}/resources/js/common.js"></script>
</head>
<body class="container">

<div class="btnContainer">
    <a class="btn btn-info" href="${document.docUrl}">
        弹出式阅读
    </a>
    <shiro:hasPermission name="doc:*:download">
        <a class="btn btn-primary" href="${baseUrl}/doc/${document.id}/download">下载</a>
    </shiro:hasPermission>
    <button>
        <a id="returnPage" class="btn btn-info">返回列表页</a>
    </button>
    <shiro:hasPermission name="doc:*:edit">
        <a class="btn btn-primary" href="${baseUrl}/doc/${document.id}/edit">编辑</a>
    </shiro:hasPermission>
</div>
<div class="officeContainer">
    <po:PageOfficeCtrl id="docCtrl"/>
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

    function afterDocumentOpened() {
//        1：文件->打开菜单
//        2：文件->关闭菜单
//        3：文件->保存菜单
//        4：文件->另存为菜单
//        5：文件->打印菜单
//        6：文件->打印设置菜单
//        7：文件->文档属性菜单
//        8：文件->打印预览菜单
        doc.SetEnableFileCommand(4, false); //禁止另存
        doc.SetEnableFileCommand(5, false); //禁止打印
        doc.SetEnableFileCommand(6, false); //禁止页面设置
        doc.SetEnableFileCommand(8, false); //禁止打印预览
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
