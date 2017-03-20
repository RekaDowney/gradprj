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
    <a id="returnPage" class="btn btn-info">返回列表页</a>
</div>
<div class="officeContainer">
    <po:PDFCtrl id="pdfCtrl"/>
</div>
<script type="text/javascript">
    $(function () {
        $("#returnPage").attr('href', "${baseUrl}/doc/${document.categoryId}/page/0/" + DEFAULT_PAGE_SIZE);
    });

    var doc = document.getElementById("pdfCtrl");

    // 全屏切换
    function fullScreenSwitch() {
        doc.FullScreen = !doc.FullScreen;
    }

    // 打印 PDF
    function print() {
        doc.ShowDialog(4);
    }

    // 显示/关闭 书签
    function bookmarkSwitch() {
        doc.BookmarksVisible = !doc.BookmarksVisible;
    }

    // 文档实际大小宽度
    function viewRealWidth() {
        doc.SetPageFit(1);
    }

    // 适合页面宽度
    function viewFitScreenWidth() {
        doc.SetPageFit(2);
    }

    // 适合宽度
    function viewFitWidth() {
        doc.SetPageFit(3);
    }

    // 定位到首页
    function gotoFirstPage() {
        doc.GoToFirstPage();
    }

    // 定位到上一页
    function gotoPreviousPage() {
        doc.GoToPreviousPage();
    }

    // 定位到下一页
    function gotoNextPage() {
        doc.GoToNextPage();
    }

    // 定位到尾页
    function gotoLastPage() {
        doc.GoToLastPage();
    }

    // 放大
    function zoomIn() {
        doc.ZoomIn();
    }

    // 缩小
    function zoomOut() {
        doc.ZoomOut();
    }

    // 右旋 90°
    function rotateRight() {
        doc.RotateRight();
    }

    // 左旋 90°
    function rotateLeft() {
        doc.RotateLeft();
    }

</script>
</body>
</html>
