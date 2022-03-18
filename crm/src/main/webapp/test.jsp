
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
%>

<html>
<head>
    <base href="<%=basePath%>">
    <title>Title</title>
    <script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
    <script type="text/javascript" src="jquery/echarts/echarts.min.js"></script>

    <script>
        $(function () {

            var myChart = echarts.init(document.getElementById("main"));
            var option={
                title:{
                    text:"交易阶段漏斗图",
                    subtext: "各阶段数量"
                },
                tooltip:{
                    trigger:'item',
                    formatter:'{a} <br/>{b} : {c}'
                },
                toolbox:{
                    feature:{
                        dataView:{readonly: false},
                        restore:{},
                        saveAsImage:{}
                    }
                },
                series:[
                    {
                        name:"数据量",
                        type:"funnel",
                        left:"10%",
                        width: "80%",
                        label:{
                            formatter: '{b}'
                        },
                        labelLine: {
                            show: true
                        },
                        itemStyle: {
                            opacity: 0.7
                        },
                        emphasis: {
                            label: {
                                position: 'inside',
                                formatter: '{b}: {c}'
                            }
                        },
                        data: [
                            { value: 60, name: '审计' },
                            { value: 40, name: '商议' },
                            { value: 20, name: '决策' },
                            { value: 80, name: '定案' },
                            { value: 100, name: '成功' }
                        ]
                }]
            }

            myChart.setOption(option);
        });
    </script>
</head>
<body>
<div id="main" style="width: 600px;height:400px;"></div>
</body>
</html>
