<%--
  Created by IntelliJ IDEA.
  User: Zhong Junbin
  Date: 2017/3/12 17:07
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%-- PageOffice 不支持使用 include 引入其他 jsp 文件 --%>
<%--<%@include file="/WEB-INF/include/staticRef.jsp" %>--%>
<%@taglib prefix="po" uri="http://java.pageoffice.cn" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>${document.docName}</title>

    <link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/common.css"/>
</head>
<body>

<div style="width: 100%;height: 100%;">
    <po:PDFCtrl id="pdfCtrl"/>
</div>

<script type="text/javascript">
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

    function RotateRight() {
        doc.RotateRight();
    }
    function RotateLeft() {
        doc.RotateLeft();
    }

</script>
</body>
</html>
