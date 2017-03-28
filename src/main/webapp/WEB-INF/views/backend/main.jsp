<%--
  Created by IntelliJ IDEA.
  User: Zhong Junbin
  Date: 2017/3/24 20:46
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/include/staticRef.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>主页</title>
    <!--[if lt IE 9]>
    <meta http-equiv="refresh" content="0;ie.html"/>
    <![endif]-->

    <link type="text/css" rel="stylesheet" href="${baseUrl}/resources/css/common.css"/>
    <link rel="shortcut icon" href="${baseUrl}/resources/backend/img/favicon.ico"/>
    <link type="text/css" rel="stylesheet" href="${baseUrl}/resources/bootstrap/css/bootstrap.min.css"/>
    <link type="text/css" rel="stylesheet" href="${baseUrl}/resources/backend/css/font-awesome.min.css"/>
    <link type="text/css" rel="stylesheet" href="${baseUrl}/resources/backend/css/animate.css"/>
    <link type="text/css" rel="stylesheet" href="${baseUrl}/resources/backend/css/style.css"/>
    <link type="text/css" rel="stylesheet" href="${baseUrl}/resources/highcharts/css/highcharts.css"/>

</head>

<body class="gray-bg">

<div class="wrapper wrapper-content">

    <div class="row">
        <div class="col-sm-4">
            <div id="horizon-timeline" class="vertical-container light-timeline">
                <shiro:hasPermission name="manage:*:user">
                    <div class="vertical-timeline-block">
                        <div class="vertical-timeline-icon navy-bg">
                            <i class="fa fa-users"></i>
                        </div>
                        <div class="vertical-timeline-content">
                            <h2>用户管理</h2>
                            <p>
                                对用户进行管理，同时可以管理用户拥有的角色
                            </p>
                            <a href="${baseUrl}/backend/user/manage" class="btn btn-sm btn-info">进入用户管理</a>
                        </div>
                    </div>
                </shiro:hasPermission>

                <shiro:hasPermission name="manage:*:role">
                    <div class="vertical-timeline-block">
                        <div class="vertical-timeline-icon blue-bg">
                            <i class="fa fa-bars"></i>
                        </div>
                        <div class="vertical-timeline-content">
                            <h2>角色管理</h2>
                            <p>
                                对角色进行管理，同时可以管理角色拥有的权限
                            </p>
                            <a href="${baseUrl}/backend/role/manage" class="btn btn-sm btn-primary">进入角色管理</a>
                        </div>
                    </div>
                </shiro:hasPermission>

                <shiro:hasPermission name="manage:*:perm">
                    <div class="vertical-timeline-block">
                        <div class="vertical-timeline-icon yellow-bg">
                            <i class="fa fa-gears"></i>
                        </div>
                        <div class="vertical-timeline-content">
                            <h2>栏目管理</h2>
                            <p>
                                对栏目树进行管理
                            </p>
                            <a href="${baseUrl}/backend/category/manage" class="btn btn-sm btn-warning">进入栏目管理</a>
                        </div>
                    </div>
                </shiro:hasPermission>
            </div>
        </div>

        <div class="col-sm-8">
            <div class="row">
                <div class="col-sm-11">
                    <div id="pieContainer" style="min-width: 300px;height: 300px;">
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-sm-11">
                    <div id="columnContainer" style="min-width: 300px;height: 300px;">
                    </div>
                </div>
            </div>
        </div>
    </div>

</div>
<script type="text/javascript" src="${baseUrl}/resources/js/jquery.js"></script>
<script type="text/javascript" src="${baseUrl}/resources/highcharts/js/highcharts.js"></script>
<script type="text/javascript" src="${baseUrl}/resources/highcharts/js/highcharts-zh_CN.js"></script>

<script type="text/javascript" src="${baseUrl}/resources/layer/layer.js"></script>
<script type="text/javascript" src="${baseUrl}/resources/bootstrap/js/bootstrap.min.js"></script>

<script src="${baseUrl}/resources/backend/js/plugins/flot/jquery.flot.js"></script>
<script src="${baseUrl}/resources/backend/js/plugins/flot/jquery.flot.tooltip.min.js"></script>
<script src="${baseUrl}/resources/backend/js/plugins/flot/jquery.flot.resize.js"></script>
<script src="${baseUrl}/resources/backend/js/plugins/flot/jquery.flot.pie.js"></script>
<script src="${baseUrl}/resources/backend/js/content.js"></script>

<script type="text/javascript">
    $(function () {

        var pieData = ${pieData};
        var columnData = ${columnData};

        console.log(pieData);
        console.log(columnData);

        // 饼图
        $('#pieContainer').highcharts({
            chart: {
                plotBackgroundColor: null,
                plotBorderWidth: null,
                plotShadow: false
            },
            title: {
                text: '文档类型占比',
                style: {}
            },
            tooltip: {
                headerFormat: '{series.name}<br>',
                pointFormat: '{point.name}: <b>{point.percentage:.1f}%</b>'
            },
            plotOptions: {
                pie: {
//                    allowPointSelect: true,
                    cursor: 'pointer',
                    dataLabels: {
                        enabled: true,
                        /*

                         this.percentage	Stacked series and pies only. The point's percentage of the total.
                         this.point	The point object. The point name, if defined, is available through this.point.name.
                         this.series:	The series object. The series name is available through this.series.name.
                         this.total	Stacked series only. The total value at this point's x value.
                         this.x:	The x value.
                         this.y:	The y value.

                         */
                        format: '<b>{point.name}</b>: {point.percentage:.1f} %'
                    },
                    showInLegend: true // 显示图例
                }
            },
            series: [{
                type: 'pie',
                name: '文档类型占比',
                data: pieData
                /*
                 data: [
                 ['Word', 48.0],
                 ['Presentation', 4.3],
                 ['Excel', 5.3],
                 ['Pdf', 42.4]
                 ]
                 */
            }]
        });

        // 柱状图
        $('#columnContainer').highcharts({
            chart: {
                type: 'column',
                plotBackgroundColor: null,
                plotBorderWidth: null,
                plotShadow: false
            },
            title: {
                text: '文档统计'
            },
            credits: {
                enabled: false
            },
            xAxis: {
                categories: [
                    'Word',
                    'Presentation',
                    'Excel',
                    'Pdf'
                ]
            },
            yAxis: {
                min: 0,
                title: {
                    text: '文档数量（份）'
                },
                labels: { //格式化纵坐标的显示风格
                    formatter: function () {
                        return this.value;
                    }
                },
                opposite: false //反转
            },
            legend: { //是否显示图例
                enabled: false
            },
            tooltip: {
                shared: true,
                useHTML: true,
                formatter: function () {
                    return this.x + '文档数：' + this.y;
                }
            },
            plotOptions: {
                column: {
                    pointPadding: 0.2,
                    borderWidth: 0
                }
            },
            series: [{
                colorByPoint: true, //为每个柱子显示不同颜色
                // data: [19, 10, 9, 1]
                data: columnData
            }]
        });

    });
</script>
</body>
</html>
