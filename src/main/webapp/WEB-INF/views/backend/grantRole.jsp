<%--
  Created by IntelliJ IDEA.
  User: Zhong Junbin
  Date: 2017/3/25 22:47
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/include/staticRef.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>角色分配</title>
    <link type="text/css" rel="stylesheet" href="${baseUrl}/resources/bootstrap/css/bootstrap.min.css"/>
    <link type="text/css" rel="stylesheet" href="${baseUrl}/resources/ztree/css/zTreeStyle.css"/>
    <%--<link type="text/css" rel="stylesheet" href="${baseUrl}/resources/backend/css/font-awesome.min.css"/>--%>
    <%--<link type="text/css" rel="stylesheet" href="${baseUrl}/resources/backend/css/animate.css"/>--%>

    <link type="text/css" rel="stylesheet" href="${baseUrl}/resources/css/require.css"/>

    <script type="text/javascript" src="${baseUrl}/resources/js/jquery.js"></script>

    <script type="text/javascript" src="${baseUrl}/resources/layer/layer.js"></script>
    <script type="text/javascript" src="${baseUrl}/resources/bootstrap/js/bootstrap.min.js"></script>

    <script type="text/javascript" src="${baseUrl}/resources/ztree/js/jquery.ztree.core.min.js"></script>
    <script type="text/javascript" src="${baseUrl}/resources/ztree/js/jquery.ztree.excheck.min.js"></script>

    <style type="text/css">
    </style>
</head>
<body>

<div class="row">
    <div class="col-sm-11 pull-left">
        <ul id="roleTree" class="ztree" style="min-width: 80%;height: 100%;"></ul>
    </div>
    <div class="col-sm-1 pull-right">
        <button id="submit" type="button" class="btn btn-primary">
            <span class="glyphicon glyphicon-ok"></span>
            确定
        </button>
    </div>
</div>

<script type="text/javascript">

    var baseUrl = "${baseUrl}";
    var zNodes = ${tree};
    var accountId = "${account.id}";

    var setting = {
        check: {
            // 启动勾选功能
            enable: true,
            // 选中勾选框类型，默认为 checkbox 复选框
            chkStyle: "checkbox", // 除了 checkbox 外还有一个 radio，radio 要求同一节点下最多有一个被选中
            // Y 表示勾选后；N 表示取消勾选后
            // p 表示该操作影响父级节点
            // s 表示该操作影响子级节点
            // 这里我们要求取消勾选的时候不要将父级节点也给取消掉
            chkboxType: {"Y": "ps", "N": "s"} // 默认为 {"Y": "ps", "N": "ps"}
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
            // （取消）勾选某个元素之前触发事件
            // beforeCheck: beforeCheck,
            // 勾选某个元素之后触发事件
            // onCheck: onCheck
        }
    };

    function getTree(treeContainerId) {
        if (!treeContainerId) {
            treeContainerId = 'roleTree';
        }
        return $.fn.zTree.getZTreeObj(treeContainerId);
    }

    $(function () {
        var tree = $.fn.zTree.init($("#roleTree"), setting, zNodes);
        tree.expandAll(true);

        $('#submit').on('click', function () {
            var nodes = getTree().getCheckedNodes(true);
            if (nodes.length <= 0) {
                layer.open({
                    type: 0,
                    title: '错误',
                    icon: 5,
                    shadeClose: true,
                    content: '请至少勾选一种角色'
                });
                return;
            }
            var roleIdArr = [];
            for (var i = 0, node; node = nodes[i]; i++) {
                roleIdArr.push(node.id);
            }

            var param = JSON.stringify(roleIdArr);
            $.ajax({
                url: baseUrl + "/account/" + accountId + "/role/grant",
                type: "POST",
                contentType: "application/json",
                data: param,
                processData: false,
                success: function (data) {
                    parent.$('#iframeResult').val(JSON.stringify(data)); // 为父页面的 iframeCallback 隐藏域设置值
                    var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
                    parent.layer.close(index); //再执行关闭
                }
            });
        });
    });
</script>
</body>
</html>
