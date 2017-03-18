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
    <po:PageOfficeCtrl id="docCtrl"/>
</div>

<script type="text/javascript">
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

</script>
</body>
</html>
