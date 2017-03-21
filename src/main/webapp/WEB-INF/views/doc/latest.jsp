<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: Zhong Junbin
  Date: 2017/3/6 20:07
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/include/staticRef.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>最新入库</title>
    <%-- ztree --%>
    <link type="text/css" rel="stylesheet" href="${baseUrl}/resources/ztree/css/zTreeStyle.css"/>
    <script type="text/javascript" src="${baseUrl}/resources/ztree/js/jquery.ztree.core.js"></script>
    <%-- ztree --%>

    <%-- bootstrap --%>
    <link type="text/css" rel="stylesheet" href="${baseUrl}/resources/bootstrap/css/bootstrap.min.css"/>
    <script type="text/javascript" src="${baseUrl}/resources/bootstrap/js/bootstrap.min.js"></script>
    <%-- bootstrap --%>

    <script type="text/javascript" src="${baseUrl}/resources/js/jqPaginator.min.js"></script>

    <script type="text/javascript" src="${baseUrl}/resources/layer/layer.js"></script>

    <script type="text/javascript" src="${baseUrl}/resources/moment/moment.js"></script>
    <script type="text/javascript" src="${baseUrl}/resources/moment/DateTime.js"></script>

    <style type="text/css">
        .time-badge {
            float: right;
            padding-right: 7px;
        }

        .badge {
            padding: 4px 7px;
            background-color: #5bc0de;
            border-color: #46b8da;
        }

        .pageContainer {
            min-width: 400px;
            width: 70%;
            margin-left: 30%;
            padding-top: 20px;
            min-height: 500px;
            height: 100%;
        }

        li.page a {
            width: 40px;
        }

        a.list-group-item:hover {
            z-index: 2;
            color: #fff;
            background-color: #337ab7;
            border-color: #337ab7;
        }

        .header {
            background: -ms-linear-gradient(top, #FFF, #CAC8C8);
            background: -moz-linear-gradient(top, #FFF, #CAC8C8);
            background: -webkit-gradient(linear, 0% 0%, 0% 100%, from(#FFF), to(#CAC8C8));
            background: -webkit-gradient(linear, 0% 0%, 0% 100%, from(#FFF), to(#CAC8C8));
            background: -webkit-linear-gradient(top, #FFF, #CAC8C8);
            background: -o-linear-gradient(top, #FFF, #CAC8C8);
        }

        .tail {
            background: -ms-linear-gradient(top, #CAC8C8, #FFF);
            background: -moz-linear-gradient(top, #CAC8C8, #FFF);
            background: -webkit-gradient(linear, 0% 0%, 0% 100%, from(#CAC8C8), to(#FFF));
            background: -webkit-gradient(linear, 0% 0%, 0% 100%, from(#CAC8C8), to(#FFF));
            background: -webkit-linear-gradient(top, #CAC8C8, #FFF);
            background: -o-linear-gradient(top, #CAC8C8, #FFF);
            text-align: center;
            padding-top: 10px;
        }

        span.download.btn.btn-info {
            float: right;
            padding: 0 10px;
        }

        .btn-container {
            float: right;
            top: 3px;
            right: 5px;
        }

        .btn-container > .btn {
            margin-top: 6px;
            padding: 3px 10px;
        }

        .main-body {
            width: 100%;
            height: 90%;
        }

        .main-tail {
            width: 100%;
            height: 10%;
        }

        #paginator {
            padding-left: 40%;
        }

    </style>

</head>
<body>
<div class="header">
    <img src="${baseUrl}/resources/images/banner.png" height="40"/>
    <div class="btn-container">
        <a class="btn btn-info" href="${baseUrl}/index">首页</a>
        <c:choose>
            <c:when test="${loginAccount.principal eq global:visitorKey()}">
                <a class="btn btn-primary" href="${baseUrl}/auth/login">登陆</a>
                <span>　</span>
            </c:when>
            <c:otherwise>
                <a class="btn btn-primary" href="${baseUrl}/logout">登出</a>
                <span>　</span>
            </c:otherwise>
        </c:choose>
    </div>
</div>
<div class="main">
    <div class="main-body">
        <c:choose>
            <c:when test="${empty page.content}">
                <span style='font-size: 20px;color: red;'>本栏目暂无文档！</span>
            </c:when>
            <c:otherwise>
                <div class="list-group">
                        <%-- 拥有下载权限 --%>
                    <shiro:hasPermission name="doc:*:download">
                        <c:forEach items="${page.content}" var="doc">
                            <a href="${baseUrl}${doc.docUrl}" target="_blank" class="list-group-item">
                                <span class="docId" hidden>${doc.id}</span>
                                <span class="docName">${doc.docName}</span>

                                    <%-- 添加下载按钮 --%>
                                <span class="download btn btn-info"
                                      href="${baseUrl}/doc/${doc.id}/download">下载</span>
                                    <%-- 添加下载按钮 --%>

                                <span class="time-badge">${global:format(doc.createdTime, "yyyy-MM-dd HH")}</span>
                                <c:if test="${global:oneDayAgo(doc.createdTime)}">
                                    <span class="badge">新</span>
                                </c:if>
                            </a>
                        </c:forEach>
                    </shiro:hasPermission>
                        <%-- 没有下载权限 --%>
                    <shiro:lacksPermission name="doc:*:download">
                        <c:forEach items="${page.content}" var="doc">
                            <a href="${baseUrl}${doc.docUrl}" target="_blank" class="list-group-item">
                                <span class="docId" hidden>${doc.id}</span>
                                <span class="docName">${doc.docName}</span>
                                <span class="time-badge">${global:format(doc.createdTime, "yyyy-MM-dd HH")}</span>
                                <c:if test="${global:oneDayAgo(doc.createdTime)}">
                                    <span class="badge">新</span>
                                </c:if>
                            </a>
                        </c:forEach>
                    </shiro:lacksPermission>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
    <div class="main-tail">
        <ul id="paginator" class="pagination"></ul>
    </div>
</div>
<div class="tail">
    CopyRight © 2017-2018
</div>
<script type="text/javascript">
    $(function () {

        var canDownload = false;
        <shiro:hasPermission name="doc:*:download">
        canDownload = true;
        </shiro:hasPermission>

        var total = ${page.totalPages};
        // 如果数据为空，此时需要特殊处理，
        // 因为初始化 jqPaginator 的时候必须保证 totalPages 大于 0 或者 totalCounts 大于 0
        if (total == 0) {
            $.jqPaginator('#paginator', {
                totalPages: 1,
                visiblePages: 7,
                currentPage: 1,
                first: '<li class="first"><a href="javascript:void(0);">首页</a></li>',
                prev: '<li class="prev"><a href="javascript:void(0);">上一页</a></li>',
                next: '<li class="next"><a href="javascript:void(0);">下一页</a></li>',
                last: '<li class="last"><a href="javascript:void(0);">尾页</a></li>',
                page: '<li class="page"><a href="javascript:void(0);">{{page}}</a></li>',
                onPageChange: function (num, type) {
                    if (type !== 'change') { // 只处理 change 事件
                        return;
                    }
                    var pageOffset = num - 1;
                    var pageSize = getPageSize();
                    listChange(pageOffset, pageSize);
                }
            });
            $('#paginator').hide();
        } else {
            $.jqPaginator('#paginator', {
                totalPages: ${page.totalPages},
                visiblePages: 7,
                currentPage: ${page.curPageOffset + 1},
                first: '<li class="first"><a href="javascript:void(0);">首页</a></li>',
                prev: '<li class="prev"><a href="javascript:void(0);">上一页</a></li>',
                next: '<li class="next"><a href="javascript:void(0);">下一页</a></li>',
                last: '<li class="last"><a href="javascript:void(0);">尾页</a></li>',
                page: '<li class="page"><a href="javascript:void(0);">{{page}}</a></li>',
                onPageChange: function (num, type) {
                    if (type !== 'change') { // 只处理 change 事件
                        return;
                    }
                    var pageOffset = num - 1;
                    var pageSize = getPageSize();
                    listChange(pageOffset, pageSize);
                }
            });
        }

        function getPageSize() {
            var p = window.location.pathname;
            if (p.endsWith('/')) {
                p = p.substr(0, p.length - 1);
            }
            return p.substr(p.lastIndexOf('/') + 1);
        }

        function listChange(pageOffset, pageSize) {
            if (!pageOffset || pageOffset < 0) {
                pageOffset = 0;
            }
            if (!pageSize || pageSize < 0) {
                pageSize = DEFAULT_PAGE_SIZE;
            }
            var path = "${baseUrl}/doc/latest/" + pageOffset + '/' + pageSize;
            var listBody = $(".main-body");
            $.ajax({
                url: path,
                type: "POST",
                cache: false,
                success: function (data) {
                    updatePaginator(data);
                    if (data.content.length == 0) {
                        listBody.empty().append("<span style='font-size: 20px;color: red;'>本栏目暂无文档！</span>");
                        return;
                    }
                    var res = "<div class='list-group'>";
                    var docPage = data.content;
                    var dateTime;
                    var i, doc;
                    if (canDownload) { // 有下载权限
                        for (i = 0; doc = docPage[i]; i++) {
                            res += "<a href='${baseUrl}" + doc.docUrl + "' target='_blank' class='list-group-item'>";
                            res += "<span class='docId' hidden>" + doc.id + "</span>";
                            res += "<span class='docName'>" + doc.docName + "</span>";
                            res += "<span class='download btn btn-info' href='${baseUrl}/doc/" + doc.id + "/download'>下载</span>";
                            dateTime = DateTime.parse(doc.createdTime, 'yyyy-MM-dd HH:mm:ss');
                            res += "<span class='time-badge'>" + dateTime.format('yyyy-MM-dd HH') + "</span>";
                            if (dateTime.plusDays(1).isAfterNow()) {
                                res += "<span class='badge'>新</span>";
                            }
                        }
                    } else {
                        for (i = 0; doc = docPage[i]; i++) {
                            res += "<a href='${baseUrl}" + doc.docUrl + "' target='_blank' class='list-group-item'>";
                            res += "<span class='docId' hidden>" + doc.id + "</span>";
                            res += "<span class='docName'>" + doc.docName + "</span>";
                            dateTime = DateTime.parse(doc.createdTime, 'yyyy-MM-dd HH:mm:ss');
                            res += "<span class='time-badge'>" + dateTime.format('yyyy-MM-dd HH') + "</span>";
                            if (dateTime.plusDays(1).isAfterNow()) {
                                res += "<span class='badge'>新</span>";
                            }
                        }
                    }
                    res += "</div>";
                    listBody.empty().append(res);
                }
            })
        }

        function updatePaginator(page) {
            var paginator = $('#paginator');
            if (page.totalPages == 0) {
                paginator.hide();
            } else {
                paginator.show();
                paginator.jqPaginator('option', {
                    totalPages: page.totalPages,
                    currentPage: page.curPageOffset + 1
                });
            }
        }

        $(".main-right-body").on('click', 'span.download.btn.btn-info', function (event) {
            event.stopPropagation(); //阻止事件冒泡
            event.preventDefault(); //阻止默认事件的执行，比如a的跳转。
            var href = $(this).attr('href');
            window.open(href, '_blank');
//            var a = $("<a>");
//            a.attr("href", href);
//            $("body").append(a);
//            a.trigger('click');
//            a.remove();
        });
    })
</script>
</body>
</html>
