<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <base href="<%=basePath%>">

    <link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
    <script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
    <script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>

    <script type="text/javascript">

        //默认情况下取消和保存按钮是隐藏的
        var cancelAndSaveBtnDefault = true;

        $(function () {
            $("#remark").focus(function () {
                if (cancelAndSaveBtnDefault) {
                    //设置remarkDiv的高度为130px
                    $("#remarkDiv").css("height", "130px");
                    //显示
                    $("#cancelAndSaveBtn").show("2000");
                    cancelAndSaveBtnDefault = false;
                }
            });

            $("#cancelBtn").click(function () {
                //显示
                $("#cancelAndSaveBtn").hide();
                //设置remarkDiv的高度为130px
                $("#remarkDiv").css("height", "90px");
                cancelAndSaveBtnDefault = true;
            });

            /*  $(".remarkDiv").mouseover(function () {
                  $(this).children("div").children("div").show();
              });*/
            $("#remarkListDiv").on("mouseover", ".remarkDiv", function () {
                $(this).children("div").children("div").show();
            });

            /* $(".remarkDiv").mouseout(function(){
                 $(this).children("div").children("div").hide();
             });*/
            $("#remarkListDiv").on("mouseout", ".remarkDiv", function () {
                $(this).children("div").children("div").hide();
            });

            /*$(".myHref").mouseover(function(){
                $(this).children("span").css("color","red");
            });*/
            $("#remarkListDiv").on("mouseover", ".myHref", function () {
                $(this).children("span").css("color", "red");
            });

            /*$(".myHref").mouseout(function(){
                $(this).children("span").css("color","#E6E6E6");
            });*/
            $("#remarkListDiv").on("mouseout", ".myHref", function () {
                $(this).children("span").css("color", "#E6E6E6");
            });


            $("#saveActivityRemarkBtn").click(function () {
                var noteContent = $("#remark").val();
                var activityId = $("#activityIdForRemark").val();

                if (noteContent == "") {
                    alert("备注内容不能为空");
                    return;
                }

                $.ajax({
                    url: "workbench/activity/saveCreateActivityRemark.do",
                    data: {
                        noteContent: noteContent,
                        activityId: activityId
                    },
                    type: "post",
                    dataType: "json",
                    success: function (data) {
                        if (data.code == 1) {
                            //保存成功,删除备注展示框中的内容，重新填充，所以携带的数据里需要包含备注列表
                            $("#remarkDisplayBox").empty();

                            var html = ""
                            html += '<div id="div_'+data.returnData.id+'" class="remarkDiv" style="height: 60px;">'
                            html += '<img title="${sessionScope.sessionUser.name}" src="image/user-thumbnail.png" style="width: 30px; height:30px;">'
                            html += '<div style="position: relative; top: -40px; left: 40px;" >'
                            html += '<h5>' + data.returnData.noteContent + '</h5>'
                            html += '<font color="gray">市场活动</font> <font color="gray">-</font> <b>${requestScope.activity.name}</b> <small style="color: gray;"> ' + data.returnData.createTime + ' 由${sessionScope.sessionUser.name}创建</small>'
                            html += '<div style="position: relative; left: 500px; top: -30px; height: 30px; width: 100px; display: none;">'
                            html += '<a class="myHref" name="editRemarkA" href="javascript:void(0);" remarkId="' + data.returnData.id + '"><span class="glyphicon glyphicon-edit" style="font-size: 20px; color: #E6E6E6;"></span></a>'
                            html += '&nbsp;&nbsp;&nbsp;&nbsp;'
                            html += '<a class="myHref" name="deleteRemarkA" href="javascript:void(0);" remarkId="' + data.returnData.id + '"><span class="glyphicon glyphicon-remove" style="font-size: 20px; color: #E6E6E6;"></span></a>'
                            html += '</div>'
                            html += '</div>'
                            html += '</div>'

                            $("#remarkDiv").before(html);

                            $("#remark").val("");
                        } else {
                            //保存失败
                            alert(data.massage);
                        }
                    }
                })
            });


            //根据备注id删除id
            $("#remarkListDiv").on("click", "a[name='deleteRemarkA']", function () {
                var id = $(this).attr("remarkId");

                $.ajax({
                    url: "workbench/activity/deleteActivityRemarkById.do",
                    data: {
                        id:id
                    },
                    dataType: "json",
                    type: "post",
                    success: function (data) {
                        if (data.code == "1") {
                            //删除成功,删除页面对应的备注
                            alert(789663)
                            $("#div_" + id).remove();

                        } else {
                            //删除失败
                            alert(data.massage);
                        }
                    }
                })
            });

            //打开修改备注信息模态窗口
            $("#remarkListDiv").on("click", "a[name='editRemarkA']", function () {
                var text = $(this).parent().parent().children("h5").text();
                $("#noteContent").val(text);
                var id = $(this).attr("remarkId");
                $("#remarkId").val(id);
                $("#editRemarkModal").modal("show");
            });

            //点击更新修改备注
            $("#updateRemarkBtn").click(function () {
                var noteContent = $.trim($("#noteContent").val());
                var id = $("#remarkId").val();

                if (noteContent == "") {
                    alert("修改的备注不能为空");
                    return;
                }

                $.ajax({
                    url: "workbench/activity/saveUpdateActivityRemark.do",
                    data:{
                        id:id,
                        noteContent:noteContent
                    },
                    type:"post",
                    dataType:"json",
                    success: function (data) {
                        if (data.code == "1") {
                            //成功，刷新
                            var html = "";
                            html+='<img title="zhangsan" src="image/user-thumbnail.png" style="width: 30px; height:30px;">'
                            html+='<div style="position: relative; top: -40px; left: 40px;" >'
                            html+='<h5>'+noteContent+'</h5>'
                            html +='<font color="gray">市场活动</font> <font color="gray">-</font> <b>${activity.name}</b> <small style="color: gray;">'+data.returnData.editTime+'  由${sessionScope.sessionUser.name}修改</small>'
                            html+='<div style="position: relative; left: 500px; top: -30px; height: 30px; width: 100px; display: none;">'
                            html += '<a class="myHref" name="editRemarkA" remarkId="'+id+'" href="javascript:void(0);" ><span class="glyphicon glyphicon-edit" style="font-size: 20px; color: #E6E6E6;"></span></a>';
                            html += '&nbsp;&nbsp;&nbsp;&nbsp;';
                            html += '<a class="myHref" name="deleteRemarkA" remarkId="'+id+'" href="javascript:void(0);" ><span class="glyphicon glyphicon-remove" style="font-size: 20px; color: #E6E6E6;"></span></a>';
                            html+='</div>'
                            html+='</div>'

                            $("#div_" + id).html(html);
                            $("#editRemarkModal").modal("hide");

                        } else {
                            alert(data.massage);
                        }
                    }
                })

            });




        });

    </script>

</head>
<body>

<!-- 修改市场活动备注的模态窗口 -->
<div class="modal fade" id="editRemarkModal" role="dialog">
    <%-- 备注的id --%>
    <input type="hidden" id="remarkId">
    <div class="modal-dialog" role="document" style="width: 40%;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">
                    <span aria-hidden="true">×</span>
                </button>
                <h4 class="modal-title" id="myModalLabel">修改备注</h4>
            </div>
            <div class="modal-body">
                <form class="form-horizontal" role="form">
                    <div class="form-group">
                        <label for="noteContent" class="col-sm-2 control-label">内容</label>
                        <div class="col-sm-10" style="width: 81%;">
                            <textarea class="form-control" rows="3" id="noteContent"></textarea>
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary" id="updateRemarkBtn">更新</button>
            </div>
        </div>
    </div>
</div>



<!-- 返回按钮 -->
<div style="position: relative; top: 35px; left: 10px;">
    <a href="javascript:void(0);" onclick="window.history.back();"><span class="glyphicon glyphicon-arrow-left" style="font-size: 20px; color: #DDDDDD"></span></a>
</div>

<!-- 大标题 -->
<div style="position: relative; left: 40px; top: -30px;">
    <div class="page-header">
        <h3>市场活动-${requestScope.activity.name} <small>${requestScope.activity.startDate} ~ ${requestScope.activity.endDate}</small></h3>
    </div>

</div>

<br/>
<br/>
<br/>

<!-- 详细信息 -->
<div style="position: relative; top: -70px;">
    <div style="position: relative; left: 40px; height: 30px;">
        <div style="width: 300px; color: gray;">所有者</div>
        <div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>${requestScope.activity.owner}</b></div>
        <div style="width: 300px;position: relative; left: 450px; top: -40px; color: gray;">名称</div>
        <div style="width: 300px;position: relative; left: 650px; top: -60px;"><b>${activity.name}</b></div>
        <div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px;"></div>
        <div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px; left: 450px;"></div>
    </div>

    <div style="position: relative; left: 40px; height: 30px; top: 10px;">
        <div style="width: 300px; color: gray;">开始日期</div>
        <div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>${activity.startDate}</b></div>
        <div style="width: 300px;position: relative; left: 450px; top: -40px; color: gray;">结束日期</div>
        <div style="width: 300px;position: relative; left: 650px; top: -60px;"><b>${activity.endDate}</b></div>
        <div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px;"></div>
        <div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px; left: 450px;"></div>
    </div>
    <div style="position: relative; left: 40px; height: 30px; top: 20px;">
        <div style="width: 300px; color: gray;">成本</div>
        <div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>${activity.cost}</b></div>
        <div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -20px;"></div>
    </div>
    <div style="position: relative; left: 40px; height: 30px; top: 30px;">
        <div style="width: 300px; color: gray;">创建者</div>
        <div style="width: 500px;position: relative; left: 200px; top: -20px;"><b>${activity.createBy}&nbsp;&nbsp;</b><small style="font-size: 10px; color: gray;">${activity.createTime}</small></div>
        <div style="height: 1px; width: 550px; background: #D5D5D5; position: relative; top: -20px;"></div>
    </div>
    <div style="position: relative; left: 40px; height: 30px; top: 40px;">
        <div style="width: 300px; color: gray;">修改者</div>
        <div style="width: 500px;position: relative; left: 200px; top: -20px;"><b>${activity.editBy}&nbsp;&nbsp;</b><small style="font-size: 10px; color: gray;">${activity.editTime}</small></div>
        <div style="height: 1px; width: 550px; background: #D5D5D5; position: relative; top: -20px;"></div>
    </div>
    <div style="position: relative; left: 40px; height: 30px; top: 50px;">
        <div style="width: 300px; color: gray;">描述</div>
        <div style="width: 630px;position: relative; left: 200px; top: -20px;">
            <b>
                ${activity.description}
            </b>
        </div>
        <div style="height: 1px; width: 850px; background: #D5D5D5; position: relative; top: -20px;"></div>
    </div>
</div>

<!-- 备注 -->
<div  id="remarkListDiv" style="position: relative; top: 30px; left: 40px;">
    <div class="page-header">
        <h4>备注</h4>
    </div>

    <!-- 备注1 -->

        <c:forEach items="${requestScope.activityRemarkList}" var="remark">
            <div id="div_${remark.id}" class="remarkDiv" style="height: 60px;">
                <img title="zhangsan" src="image/user-thumbnail.png" style="width: 30px; height:30px;">
                <div style="position: relative; top: -40px; left: 40px;" >
                    <h5>${remark.noteContent}</h5>
                    <font color="gray">市场活动</font> <font color="gray">-</font> <b>${activity.name}</b> <small style="color: gray;"> ${remark.editFlag==0?remark.createTime:remark.editTime} 由${remark.editFlag==0?remark.createBy:remark.editBy} ${remark.editFlag==0?"创建":"修改"}</small>
                    <div style="position: relative; left: 500px; top: -30px; height: 30px; width: 100px; display: none;">
                        <a class="myHref" name="editRemarkA" remarkId="${remark.id}" href="javascript:void(0);" ><span class="glyphicon glyphicon-edit" style="font-size: 20px; color: #E6E6E6;"></span></a>
                        &nbsp;&nbsp;&nbsp;&nbsp;
                        <a class="myHref" name="deleteRemarkA" remarkId="${remark.id}" href="javascript:void(0);" ><span class="glyphicon glyphicon-remove" style="font-size: 20px; color: #E6E6E6;"></span></a>
                    </div>
                </div>
            </div>
        </c:forEach>


    <div id="remarkDiv" style="background-color: #E6E6E6; width: 870px; height: 90px;">
        <form role="form" style="position: relative;top: 10px; left: 10px;">
            <input type="hidden" id="activityIdForRemark" value="${requestScope.activity.id}">
            <textarea id="remark" class="form-control" style="width: 850px; resize : none;" rows="2"  placeholder="添加备注..."></textarea>
            <p id="cancelAndSaveBtn" style="position: relative;left: 737px; top: 10px; display: none;">
                <button id="cancelBtn" type="button" class="btn btn-default">取消</button>
                <button type="button" class="btn btn-primary" id="saveActivityRemarkBtn">保存</button>
            </p>
        </form>
    </div>
</div>
<div style="height: 200px;"></div>
</body>
</html>