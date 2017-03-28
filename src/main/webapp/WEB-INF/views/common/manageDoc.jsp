<%--suppress ALL --%>
<%--suppress JSUnusedLocalSymbols --%>
<%--
  Created by IntelliJ IDEA.
  User: Zhong Junbin
  Date: 2017/3/24 21:01
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/include/staticRef.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>账户管理</title>

    <!--[if lt IE 9]>
    <meta http-equiv="refresh" content="0;ie.html"/>
    <![endif]-->

    <link type="text/css" rel="stylesheet" href="${baseUrl}/resources/css/common.css"/>
    <link rel="shortcut icon" href="${baseUrl}/resources/backend/img/favicon.ico"/>
    <link type="text/css" rel="stylesheet" href="${baseUrl}/resources/bootstrap/css/bootstrap.min.css"/>
    <link type="text/css" rel="stylesheet" href="${baseUrl}/resources/backend/css/font-awesome.min.css"/>
    <link type="text/css" rel="stylesheet" href="${baseUrl}/resources/backend/css/animate.css"/>

    <link type="text/css" rel="stylesheet" href="${baseUrl}/resources/css/require.css"/>

    <style type="text/css">
        td.text-danger {
            text-align: center;
            font-size: 20px;
        }
    </style>
</head>

<body>
<div class="wrapper wrapper-content animated fadeInRight">
    <%--
        <div class="row">
            <div class="alert alert-info" style="width: 98%;margin-left: 1%;">
                <a href="#" class="close" data-dismiss="alert">
                    &times;
                </a>
                <strong style="font-weight: bold">注意！</strong>visitor 和 System 账户是基础账户，不允许删除和修改
            </div>
        </div>
    --%>
    <div class="row">
        <div class="col-sm-1 pull-right">
            <button id="reset" type="button" class="btn btn-primary">
                <span class="glyphicon glyphicon-refresh"></span>
                重置
            </button>
        </div>
        <div class="col-sm-4 pull-right">
            <div class="input-group search-box">
                <label for="docName"></label>
                <input id="docName" type="text" name="docName" class="form-control"
                       placeholder="输入文档名称"/>
                <span class="input-group-btn">
                    <button type="button" class="btn btn-info search">
                        <span class="glyphicon glyphicon-search"></span>
                        搜索
                    </button>
                </span>
            </div>
        </div>
    </div>

    <div class="row">
        <div class="col-sm-12 col-center">
            <table class="table table-bordered table-condensed table-responsive" style="margin: 10px auto;">
                <thead>
                <tr>
                    <td class="hide">ID</td>
                    <th style="min-width: 20px">序号</th>
                    <th style="min-width: 120px">名称</th>
                    <th style="min-width: 100px">操作</th>
                </tr>
                </thead>
                <tbody>
                <c:choose>
                    <c:when test="${empty page.content}">
                        <tr>
                            <td colspan="4" class="text-danger">
                                还没有上传过任何文档！
                            </td>
                        </tr>
                    </c:when>
                    <c:otherwise>
                        <c:forEach items="${page.content}" var="doc" varStatus="status">
                            <tr>
                                <td class="hide">${doc.id}</td>
                                <td>${status.count}</td>
                                <td>${doc.docName}</td>
                                <td>
                                    <button href="${baseUrl}/account/doc/${doc.id}/delete"
                                            class="btn btn-sm btn-danger doc-delete">删除
                                    </button>
                                </td>
                            </tr>
                        </c:forEach>
                    </c:otherwise>
                </c:choose>
                </tbody>
            </table>
        </div>
    </div>

    <div class="row pull-right">
        <ul id="paginator" class="pagination"></ul>
    </div>
</div>

<%-- 存储 iframe 页面相关请求（增/改）的返回值，JSON 字符串 --%>
<input type="hidden" id="iframeResult" value=""/>
<%-- iframe 页面是否按了右上角的取消键，因为取消键除了触发 cancel 事件外，还会再触发 end 事件 --%>
<input type="hidden" id="iframeCancel" value=""/>

<script type="text/javascript" src="${baseUrl}/resources/js/jquery.js"></script>
<script type="text/javascript" src="${baseUrl}/resources/js/common.js"></script>

<script type="text/javascript" src="${baseUrl}/resources/layer/layer.js"></script>
<script type="text/javascript" src="${baseUrl}/resources/bootstrap/js/bootstrap.min.js"></script>
<script type="text/javascript" src="${baseUrl}/resources/js/jqPaginator.min.js"></script>

<script type="text/javascript">

    $(function () {

        var baseUrl = "${baseUrl}";

        // 立即执行
        var total = ${page.totalPages};
        if (total == 0) {
            $.jqPaginator('#paginator', {
                totalPages: 1,
                visiblePages: 7,
                currentPage: 1,
                first: '<li class="first"><a href="javascript:void(0);">首页</button></li>',
                prev: '<li class="prev"><a href="javascript:void(0);">上一页</button></li>',
                next: '<li class="next"><a href="javascript:void(0);">下一页</button></li>',
                last: '<li class="last"><a href="javascript:void(0);">尾页</button></li>',
                page: '<li class="page"><a href="javascript:void(0);">{{page}}</button></li>',
                onPageChange: onPageChange
            });
            $('#paginator').hide();
        } else {
            $.jqPaginator('#paginator', {
                totalPages: ${page.totalPages},
                visiblePages: 7,
                currentPage: ${page.curPageOffset + 1},
                first: '<li class="first"><a href="javascript:void(0);">首页</button></li>',
                prev: '<li class="prev"><a href="javascript:void(0);">上一页</button></li>',
                next: '<li class="next"><a href="javascript:void(0);">下一页</button></li>',
                last: '<li class="last"><a href="javascript:void(0);">尾页</button></li>',
                page: '<li class="page"><a href="javascript:void(0);">{{page}}</button></li>',
                onPageChange: onPageChange
            });
        }

        function onPageChange(num, type) {
            if (type !== 'change') { // 只处理 change 事件
                return;
            }
            var pageOffset = num - 1;
            var pageSize = DEFAULT_PAGE_SIZE;
            // 分页处理时确认是否有搜素条件
            var searchText = $('#docName').val();
            renderPageChange(pageOffset, pageSize, searchText);
        }

        function renderPageChange(pageOffset, pageSize, docName) {
            if (!docName) {
                docName = '';
            }
            $.ajax({
                url: "${baseUrl}/account/doc/page/" + pageOffset + "/" + pageSize,
                data: {docName: docName},
                type: "POST",
                cache: false,
                success: function (data) {
                    updatePaginator(data);
                    renderTable(data);
                }
            });
        }

        function renderTable(page) {
            var body = $('table.table > tbody');
            if (page.content.length == 0) {
                var searchText = $("#docName").val();
                if (searchText.length > 0) {
                    body.html("<tr><td colspan='4' class='text-danger'>暂无名称中包含“" + searchText + "”的文档</td></tr>");
                } else {
                    body.html("<tr><td colspan='4' class='text-danger'>暂无数据</td></tr>");
                }
                return;
            }
            var docPage = page.content;
            var i, doc, index = page.reqPageOffset * page.reqPageSize + 1;
            var result = "";
            var resetPwdUrl;
            var deleteUrl;
            var grantRoleUrl;

            for (i = 0; doc = docPage[i]; i++, index++) {
                deleteUrl = baseUrl + "/account/doc/" + doc.id + "/delete";

                result += "<tr>";
                result += "<td class='hide'>" + doc.id + "</td>";
                result += "<td>" + index + "</td>";
                result += "<td>" + doc.docName + "</td>";
                result += "<td>" +
                        "<button href='" + deleteUrl + "' class='btn btn-sm btn-danger doc-delete'>删除</button> " +
                        "</td>";
                result += "</tr>";
            }
            body.html(result);
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

        // ---------------------------------- 删 ----------------------------------

        $('.table').on('click', '.doc-delete', function () {
            var url = $(this).attr('href');
            var docName = $(this).parent('td').prev().text();
            var text = "确定要删除“" + docName + "”文档，该操作不可撤销，请谨慎操作！";
            layer.confirm(text, {
                btn: ['确认', '取消'],
                yes: function (index, layero) {
                    $.ajax({
                        type: "POST",
                        url: url,
                        success: function (data) {
                            if (data.status) {
                                renderPageChange(0, 10);
                                layer.msg(data.msg);
                            } else {
                                layer.alert(data.msg, {icon: 5, shadeClose: true});
                            }
                        }
                    });
                },
                btn2: function (index, layero) {
                    // layer.msg('不删除该角色');
                },
                cancel: function () { // 右上角关闭按钮
                    // return false; // 返回 false 将无法关闭该确认框
                    // layer.msg('不删除该角色');
                    return true;
                }
            });
        });
        // ---------------------------------- 删 ----------------------------------

        // ---------------------------------- 搜索 ----------------------------------
        $('.search-box').on('click', '.search', function () {
            var text = $('#docName').val().trim();
            if (text.length == 0) {
                layer.open({
                    type: 0,
                    title: '错误',
                    icon: 5,
                    content: '请输入要搜索的文档名称',
                    shadeClose: true
                });
                return;
            }
            renderPageChange(0, DEFAULT_PAGE_SIZE, text);
        });

        $('#reset').on('click', function () {
            $("#docName").val('');
            renderPageChange(0, DEFAULT_PAGE_SIZE);
        });
        // ---------------------------------- 搜索 ----------------------------------

    });
</script>
</body>
</html>
