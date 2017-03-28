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
    <title>${category.permName}</title>
    <%--<title>文档共享平台</title>--%>
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

    </style>

</head>
<body>
<div class="header">
    <img src="${baseUrl}/resources/images/banner.png" height="40"/>
    <div class="btn-container">
        <shiro:hasPermission name="ask:for:leave">
            <button id="askForLeave" type="button" class="btn btn-info">我要请假</button>
        </shiro:hasPermission>
        <shiro:hasPermission name="channel:*:latest">
            <a class="btn btn-info" href="${baseUrl}/doc/latest">最新入库文档</a>
        </shiro:hasPermission>
        <shiro:hasPermission name="manage:*:backend">
            <a class="btn btn-info" href="${baseUrl}/backend/index">进入后台管理</a>
        </shiro:hasPermission>
        <%-- JSTL 支持在一个 EL 表达式中使用自定义函数 --%>
        <shiro:hasRole name="${global:registeredUserRoleKey()}">
            <c:if test="${loginAccount.principal ne global:visitorKey()}">
                <a class="btn btn-success" href="${baseUrl}/account/personal/center">个人中心</a>
                <span>　</span>
            </c:if>
        </shiro:hasRole>
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
<script type="text/javascript">
    var canDownload = false;
    <shiro:hasPermission name="doc:*:download">
    canDownload = true;
    </shiro:hasPermission>
</script>
<div class="main">
    <div class="main-left" style="background-color: #DCE2D9">
        <%-- 拥有上传权限 --%>
        <shiro:hasPermission name="doc:*:upload">
            <div class="function-btn-group" style="margin-top: 2px;margin-left: 2px;">
                <a id="upload" class="btn btn-primary">上传文档到当前栏目</a>
            </div>
        </shiro:hasPermission>
        <div id="search-box" class="input-group col-md-3" style="margin-top: 2px;margin-left: 2px;">
            <input id="searchText" type="text" class="form-control" placeholder="请输入文档名称" style="width: 180px;"/>
            <span class="input-group-btn">
                    <button id="search" type="button" class="btn btn-info">搜索</button>
                    <button id="searchCurCategory" type="button" class="btn btn-info"
                            style="margin-left: 2px;">本栏目搜索</button>
                </span>
            <input id="searchTextCache" type="hidden" name="searchText" value=""/>
            <input id="searchCategory" type="hidden" name="searchCategory" value="false"/>
        </div>
        <hr style="border: 0;border-top: 1px solid #3CADD8;margin-top: 5px;margin-bottom: 5px;"/>
        <div id="menuContainer">
            <ul id="categoryMenu" class="ztree" style=" width:100%;"></ul>
        </div>
    </div>
    <div class="main-right">
        <div class="main-right-body" style="height: 90%;">
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
        <div class="main-right-tail" style="height: 10%;">
            <ul id="paginator" class="pagination" style="padding-left: 35%;"></ul>
        </div>
    </div>
</div>
<div class="tail">
    CopyRight © 2017-2018
</div>
<input id="aflResult" type="hidden" value=""/>
<a id="openLeave" target="_blank" href="" hidden="hidden"></a>
<script type="text/javascript">

    $(function () {
        /*
         $("a.list-group-item").hover(
         function (e) {
         $(this).addClass("active");
         }, function (e) {
         $(this).removeClass("active");
         });
         */

        var setting = {
            view: {
                // 禁用双击非叶子节点时切换非叶子节点的展开和折叠状态
                // dblClickExpand: false,
                // 禁用多选
                selectedMulti: false
            },
            data: {
                simpleData: {
                    enable: true,
                    idKey: 'id',
                    pIdKey: "parentId",
                    rootPId: null
                }
            },
            callback: {
                beforeClick: beforeClick,
                onClick: onClick
            }
        };

        // ztree 节点
        var zNodes = ${menuTree};

        function beforeClick(treeId, treeNode) {
            var check = (treeNode && treeNode.attachable);
            if (!check) {
//                Tip.warning("请选择学科");
                return false;
            }
            return true;
        }

        function onClick(e, treeId, treeNode) {
            var pageOffset = 0;
            var pageSize = getPageSize();
            // 清空搜索字段缓存
            $('#searchTextCache').val('');
            $('#searchCategory').val('false');
            listChange(pageOffset, pageSize);
        }

        $.fn.zTree.init($("#categoryMenu"), setting, zNodes);
        // 立即执行函数，选中指定的节点
        (function () {
            var tree = $.fn.zTree.getZTreeObj("categoryMenu");
            var node = tree.getNodeByParam('id', "${category.id}");
            tree.selectNode(node);
            // 设置上传到本栏目的按钮链接
            $("#upload").attr('href', "${baseUrl}/doc/" + node.id + "/upload");
        })();

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
                    // 分页处理时确认是否有搜素条件
                    var searchText = $('#searchTextCache').val();
                    if (searchText.length == 0) { // 没有搜索条件
                        listChange(pageOffset, pageSize);
                    } else { // 有搜索条件
                        if ($('#searchCategory').val() === 'true') {
                            search(true, pageOffset);
                        } else {
                            search(false, pageOffset);
                        }
                    }
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
                    // 分页处理时确认是否有搜素条件
                    var searchText = $('#searchTextCache').val();
                    if (searchText.length == 0) { // 没有搜索条件
                        listChange(pageOffset, pageSize);
                    } else { // 有搜索条件
                        if ($('#searchCategory').val() === 'true') {
                            search(true, pageOffset);
                        } else {
                            search(false, pageOffset);
                        }
                    }
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
            var tree = $.fn.zTree.getZTreeObj("categoryMenu");
            var node = tree.getSelectedNodes()[0];
            $("title").html(node.name);
            var path = "${baseUrl}/doc/" + node.id + '/page/' + pageOffset + '/' + pageSize;
            // 设置上传到本栏目的按钮链接
            $("#upload").attr('href', "${baseUrl}/doc/" + node.id + "/upload");
            $.ajax({
                url: path,
                type: "POST",
                cache: false,
                success: function (data) {
                    updatePaginator(data);
                    if (data.content.length == 0) {
                        $(".main-right-body").empty().append("<span style='font-size: 20px;color: red;'>本栏目暂无文档！</span>");
                        return;
                    }
                    renderList(data);
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

        function renderList(data) {
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
            $(".main-right-body").empty().append(res);
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

        /*

         totalPages	0	设置分页的总页数
         totalCounts	0	设置分页的总条目数
         pageSize	0	设置每一页的条目数
         注意：要么设置totalPages，要么设置totalCounts + pageSize，否则报错；设置了totalCounts和pageSize后，会自动计算出totalPages。
         currentPage	1	设置当前的页码
         visiblePages	7	设置最多显示的页码数（例如有100也，当前第1页，则显示1 - 7页）

         */


//        var yMdHms = /([0-9]{4})-([0-9]{2})-([0-9]{2}) ([0-9]{2}):([0-9]{2}):([0-9]{2})/;
//        var yMdHms_yMdH = /([0-9]{4}-[0-9]{2}-[0-9]{2} [0-9]{2}):([0-9]{2}):([0-9]{2})/;
//
//        function timeBadgeFormat(fullDate) {
//            var res = yMdHms_yMdH.exec(fullDate);
//            return res[1];
//        }

        // ----------------------------- 搜索 -----------------------------
        $('#search-box').on('click', '#search', function () {
            search(false, 0);
        }).on('click', '#searchCurCategory', function () {
            search(true, 0);
        });

        /**
         *
         * @param scc 在当前栏目下搜索，true | false，特别注意：不是字符串的 'true' 和 'false'
         * @param pageOffset 页面偏移量
         */
        function search(scc, pageOffset) {
            var searchCategory = $('#searchCategory');
            searchCategory.val(scc);
            var searchText = $('#searchText');
            var text = searchText.val().trim();
            // 搜索内容为空
            if (text.length == 0) {
                layer.open({
                    type: 0,
                    title: '错误',
                    icon: 5,
                    content: '请输入搜索内容',
                    shadeClose: true
                });
                return;
            }
            $('#searchTextCache').val(text);
            var param;
            if (scc) {
                var tree = $.fn.zTree.getZTreeObj('categoryMenu');
                var node = tree.getSelectedNodes()[0];
                param = {searchText: text, categoryId: node.id};
            } else {
                param = {searchText: text};
            }
            $.ajax({
                url: "${baseUrl}/doc/search/page/" + pageOffset + '/' + getPageSize(),
                type: "POST",
                data: param,
                cache: false,
                success: function (data) {
                    updatePaginator(data);
                    if (data.content.length == 0) {
                        $(".main-right-body").empty().append("<span style='font-size: 20px;color: red;'>搜索结果为空</span>");
                        return;
                    }
                    renderList(data);
                }
            });
        }

        // ----------------------------- 搜索 -----------------------------

        // ----------------------------- 请假 -----------------------------
        $('.btn-container').on('click', '#askForLeave', function () {
            var layerIndex = layer.open({
                type: 2,
                title: '请假',
                content: '${baseUrl}/ask/for/leave',
                area: ['80%', '80%'],
                resize: false,
                end: function () {
                    var res = $('#aflResult').val();
                    if (res.length > 0) {
                        try {
                            var data = JSON.parse(res);
                            window.open(data.url);
//                            var openLeave = $('#openLeave');
//                            openLeave.attr('href', data.url);
//                            openLeave.trigger('click');
                        } catch (e) {
                            layer.open({
                                title: '错误',
                                icon: 5,
                                shadeClose: false,
                                content: res
                            });
                        }
                    }
                }
            });
        });
        // ----------------------------- 请假 -----------------------------

    });
</script>
</body>
</html>
