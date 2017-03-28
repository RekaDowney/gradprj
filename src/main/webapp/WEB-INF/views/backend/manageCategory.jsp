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
    <title>栏目管理</title>

    <!--[if lt IE 9]>
    <meta http-equiv="refresh" content="0;ie.html"/>
    <![endif]-->

    <link type="text/css" rel="stylesheet" href="${baseUrl}/resources/css/common.css"/>
    <link rel="shortcut icon" href="${baseUrl}/resources/backend/img/favicon.ico"/>
    <link type="text/css" rel="stylesheet" href="${baseUrl}/resources/bootstrap/css/bootstrap.min.css"/>
    <link type="text/css" rel="stylesheet" href="${baseUrl}/resources/backend/css/font-awesome.min.css"/>
    <link type="text/css" rel="stylesheet" href="${baseUrl}/resources/backend/css/animate.css"/>
    <!-- 该样式文件会对 zTree 造成影响 -->
    <%--<link type="text/css" rel="stylesheet" href="${baseUrl}/resources/backend/css/style.css"/>--%>
    <link type="text/css" rel="stylesheet" href="${baseUrl}/resources/ztree/css/demo.css"/>
    <link type="text/css" rel="stylesheet" href="${baseUrl}/resources/ztree/css/zTreeStyle.css"/>
</head>

<body class="gray-bg">

<div class="wrapper wrapper-content animated fadeInRight">
    <div class="row">
        <div class="alert alert-info" style="width: 98%;margin-left: 1%;">
            <a href="#" class="close" data-dismiss="alert">
                &times;
            </a>
            <strong style="font-weight: bold">注意！</strong>能够上传文档的节点都必须被处理成叶子节点
        </div>
    </div>
    <div class="row">
        <div class="col-sm-4">
            <ul id="permTree" class="ztree" style="width: 400px;height: 100%;"></ul>
        </div>
        <div class="col-sm-8">
            <div class="function-container" style="padding-top: 10px;">
                <div id="node-container" style="height: 20px; width: 100%;">
                    <span class="node-msg"></span>
                </div>
                <div id="btn-container">
                    <button id="appendPeer" type="button" class="btn btn-sm btn-primary">添加同级栏目</button>
                    <button id="appendSon" type="button" class="btn btn-sm btn-primary">添加子级栏目</button>
                    <button id="update" type="button" class="btn btn-sm btn-info">修改</button>
                    <button id="delete" type="button" class="btn btn-sm btn-danger">删除</button>
                </div>
            </div>
        </div>
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
<script type="text/javascript" src="${baseUrl}/resources/ztree/js/jquery.ztree.core.min.js"></script>
<script type="text/javascript" src="${baseUrl}/resources/ztree/js/jquery.ztree.excheck.min.js"></script>

<script type="text/javascript">
    $(document).ready(function () {
        var setting = {
            view: {
                // 禁用双击非叶子节点时切换非叶子节点的展开和折叠状态
                // dblClickExpand: false,
                // 禁用多选
                selectedMulti: false
            },
            data: {
                keep: {
                    parent: true,
                    leaf: true
                },
                simpleData: {
                    enable: true,
                    idKey: 'id',
                    pIdKey: "parentId",
                    rootPId: null
                }
            },
            callback: {
                // beforeClick: beforeClick,
                onClick: onClick
            }
        };

        var zNodes = ${tree};

        /*
         初始化 zTree 并将整棵树都伸展开来
         */
        $.fn.zTree.init($("#permTree"), setting, zNodes).expandAll(true);
        selectFirstNode();

        /*
         function beforeClick(treeId, treeNode) {
         /!*
         var check = (treeNode && treeNode.canSelected);
         if (!check) {
         return false;
         }
         *!/
         return true;
         }
         */

        /**
         * 构造面包屑导航条信息
         */
        function constructBreadcrumb(node) {
            if (!node) {
                node = getSelectedNode();
            }
            var text = "当前栏目：";
            var nodeArr = [];
            var tree = getTree();
            while (node) {
                nodeArr.push(node.name);
                if (node.parentId) {
                    node = tree.getNodeByParam('id', node.parentId);
                } else {
                    node = null;
                }
            }
            nodeArr.reverse();
            text += nodeArr.join(" >> ");
            return text;
        }

        function onClick(e, treeId, treeNode) {
            var text = constructBreadcrumb(treeNode);

            // 修改右侧节点名称
            $('.node-msg').text(text);
            // 处理右侧功能按钮
            dealFunctionalBtn(treeNode);
        }

        /**
         * 获取当前页面指定的 ztree 对象，默认为 permTree 栏目树
         */
        function getTree(treeId) {
            if (!treeId) {
                treeId = 'permTree';
            }
            return $.fn.zTree.getZTreeObj(treeId);
        }

        /**
         * 选中第一个节点并将节点名称显示到右侧的信息框中
         */
        function selectFirstNode() {
            // var tree = $.fn.zTree.getZTreeObj('permTree');
            var tree = getTree();
            // 得到所有的节点
            var nodes = tree.getNodes();
            if (nodes.length > 0) {
                $('.node-msg').text(constructBreadcrumb(nodes[0]));
                tree.selectNode(nodes[0]);
                dealFunctionalBtn(nodes[0]);
            }
        }

        /**
         * 获取选中的第一个节点
         * @returns {*} treeNode 节点对象
         */
        function getSelectedNode() {
            // var tree = $.fn.zTree.getZTreeObj('permTree');
            var tree = getTree();
            return tree.getSelectedNodes()[0];
        }

        /**
         * 移除被选中的第一个节点
         */
        function removeSelectedNode() {
            // var tree = $.fn.zTree.getZTreeObj('permTree');
            var tree = getTree();
            var nodes = tree.getSelectedNodes();
            if (nodes.length > 0) {
                tree.removeNode(nodes[0]);
            }
        }

        function dealFunctionalBtn(node) {
            var appendPeerBtn = $('#appendPeer');
            var appendSonBtn = $('#appendSon');
            var modifyBtn = $('#update');
            var deleteBtn = $('#delete');
            if (!node) {
                node = getSelectedNode();
            }

            if (node.attachable) {
                appendSonBtn.hide();
            } else {
                appendSonBtn.show();
            }

            if (node.attachable || !node.children) {
                deleteBtn.show();
            } else {
                deleteBtn.hide();
            }

            if (node.name == '分类文档') {
                appendPeerBtn.hide();
                deleteBtn.hide();
                modifyBtn.hide();
            } else {
                appendPeerBtn.show();
                modifyBtn.show();
            }
        }

        $('.function-container').on('click', '#appendPeer, #appendSon', function (e) {
            var node = getSelectedNode();
            var url = "${baseUrl}/category/append";
            var title;
            var peer = false;
            if ($(this)[0].id == 'appendSon') {
                url += "?parentId=" + getSelectedNode().id;
                title = "当前栏目：" + node.name + ' >> 添加子级栏目';
            } else {
                url += "?parentId=" + getSelectedNode().parentId;
                title = "当前栏目：" + node.name + ' >> 添加同级栏目';
                peer = true;
            }
            layer.open({
                type: 2,
                title: title,
                content: url,
                area: ['50%', '50%'],
                resize: false,
                moveOut: true,
                cancel: function (index, layero) { // 这里必须添加 cancel 方法来避免 end 方法发生冲突
                    $('#iframeCancel').val(true);
                    layer.close(index);
                },
                end: function () { // 弹出层销毁后的回调方法
                    var cancel = $('#iframeCancel');
                    if (cancel.val() == 'true') {
                        cancel.val('');
                        return;
                    }
                    var cb = $('#iframeResult').val();
                    try {
                        var data = JSON.parse(cb);
                        if (data.status) {
                            // 将节点挂到树上
                            var node = data.node;
                            var tree = getTree();
                            if (node.parentId && node.parentId.length == 32) {
                                if (peer) {
                                    tree.addNodes(getSelectedNode().getParentNode(), -1, node);
                                } else {
                                    tree.addNodes(getSelectedNode(), -1, node);
                                }
                            } else {
                                tree.addNodes(null, -1, node);
                            }
                            // 给出提示
                            // layer.msg(data.msg);
                            layer.open({
                                title: '添加栏目 -- ' + node.name,
                                icon: 6,
                                shadeClose: true,
                                content: data.msg
                            });
                        } else {
                            // layer.msg(data.msg);
                            layer.open({
                                title: '添加栏目',
                                icon: 5,
                                shadeClose: true,
                                content: data.msg
                            });
                        }
                    } catch (e) {
                        layer.open({
                            title: '解析失败',
                            icon: 5,
                            shadeClose: true,
                            content: e
                        });
                    }
                }
            });
        }).on('click', '#update', function (e) {
            var node = getSelectedNode();
            layer.open({
                type: 2,
                title: '修改栏目 -- ' + node.name,
                content: "${baseUrl}/category/" + node.id + "/modify/",
                area: ['50%', '50%'],
                resize: false,
                moveOut: true,
                cancel: function (index, layero) {
                    $('#iframeCancel').val(true);
                    layer.close(index);
                },
                end: function () { // 弹出层销毁后的回调方法
                    var cancel = $('#iframeCancel');
                    if (cancel.val() == 'true') {
                        cancel.val('');
                        return;
                    }
                    var cb = $('#iframeResult').val();
                    try {
                        var data = JSON.parse(cb);
                        if (data.status) {
                            // 修改节点
                            var newNode = data.node;
                            var tree = getTree();
                            var preNode = getSelectedNode();
                            preNode.name = newNode.name;
                            preNode.isParent = !!newNode.isParent;
                            preNode.attachable = !!newNode.attachable;
                            tree.updateNode(preNode);
                            // 给出提示
                            // layer.msg(data.msg);
                            layer.open({
                                title: '修改栏目 -- ' + preNode.name,
                                icon: 6,
                                shadeClose: true,
                                content: data.msg
                            });
                        } else {
                            // layer.msg(data.msg);
                            layer.open({
                                title: '修改栏目 -- ' + preNode.name,
                                icon: 5,
                                shadeClose: true,
                                content: data.msg
                            });
                        }
                    } catch (e) {
                        layer.open({
                            title: '解析失败',
                            icon: 5,
                            shadeClose: true,
                            content: e
                        });
                    }
                }
            });
        }).on('click', '#delete', function (e) {
            layer.confirm('确定要删除\"' + getSelectedNode().name + '\"栏目，该操作不可撤销，请谨慎操作！', {
                btn: ['确认', '取消'],
                yes: function (index, layero) {
                    $.ajax({
                        type: "POST",
                        url: "${baseUrl}/category/" + getSelectedNode().id + "/delete",
                        success: function (data) {
                            if (data.status) {
                                removeSelectedNode();
                                layer.msg(data.msg);
                            } else {
                                layer.alert(data.msg, {icon: 5, shadeClose: true});
                            }
                        }
                    });
                },
                btn2: function (index, layero) {
                    // layer.msg('不删除该节点');
                },
                cancel: function () { // 右上角关闭按钮
                    // return false; // 返回 false 将无法关闭该确认框
                    // layer.msg('不删除该节点');
                    return true;
                }
            });
        });

    });
</script>
</body>
</html>
