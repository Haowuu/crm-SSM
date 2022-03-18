<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
	<base href="<%=basePath%>">

<link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<link href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" type="text/css" rel="stylesheet" />
<link href="jquery/bs_pagination-master/css/jquery.bs_pagination.min.css" type="text/css" rel="stylesheet">

<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>
<script type="text/javascript" src="jquery/bs_pagination-master/js/jquery.bs_pagination.min.js"></script>
<script type="text/javascript" src="jquery/bs_pagination-master/localization/en.js"></script>

<script type="text/javascript">

	$(function () {

		//进入页面后调用分页方法发起请求，默认第一页，每页10条
		pageList(1,5);

		//单击创建按钮打开模态窗口
		$("#createActivity").click(function () {
			//清空模态窗口之前的内容
			$("#create-form")[0].reset();
			$("#createActivityModal").modal("show");
		});

		//点击保存按钮，验证创建市场活动中的各项值
		$("#saveCreateActivity").click(function () {
			var owner = $("#create-ActivityOwner").val();
			var name = $.trim($("#create-ActivityName").val());
			var startDate = $("#create-startTime").val();
			var endDate = $("#create-endTime").val();
			var cost = $.trim($("#create-cost").val());
			var description = $.trim($("#create-description").val());

			if (owner == "") {
				alert("所有者不能为空");
				return;
			}
			if (name == "") {
				alert("名称不能为空");
				return;
			}
			if (startDate != "" && endDate != "") {
				if (startDate > endDate) {
					alert("开始日期不应在结束日期之后");
					return;
				}
			}
			var regExp = /^(([1-9]\d*)|0)$/;
			if (!regExp.test(cost)) {
				alert("成本只能为非负整数");
				return;
			}

			$.ajax({
				url: "workbench/activity/saveCreateActivity.do",
				data: {
					owner: owner,
					name: name,
					startDate: startDate,
					endDate, endDate,
					cost, cost,
					description: description
				},
				dataType: "json",
				type: "post",
				success: function (data) {
					if (data.code == "1") {
						//保存成功，关闭模态窗口，刷新页面列表
						$("#createActivityModal").modal("hide");
						pageList(1, $("#activityPages").bs_pagination('getOption', 'rowsPerPage'))

					} else {
						alert(data.massage);
						//保存失败
					}
				}
			})
		});

		//日历
		$(".myTime").datetimepicker({
			language: "zh-CN",
			format: "yyyy-mm-dd",
			minView: "month",
			autoclose: true,
			todayBtn: true,
			pickerPosition: "button-right",
			clearBtn: true,
			initialDate: new Date()
		});

		//点击查询按钮查询分页信息
		$("#searchBtn").click(function () {
			pageList(1, $("#activityPages").bs_pagination('getOption', 'rowsPerPage'));
		});


		/*
		* 	复选框的全选和全不选
		* */
		$("#checkAll").click(function () {
			$("#activityListBody input[type='checkbox']").prop("checked", this.checked)
		});


		$("#activityListBody").on("click", $("#activityListBody input[type='checkbox']"), function () {
			$("#checkAll").prop("checked", $("#activityListBody input[type='checkbox']:checked").length == $("#activityListBody input[type='checkbox']").length)
			/*if ($("#activityListBody input[type='checkbox']:checked").size() == $("#activityListBody input[type='checkbox']").size()) {
				$("#checkAll").prop("checked", true);
			} else {
				$("#checkAll").prop("checked", false);
			}*/
		});

		//删除活动
		$("#deleteActivityBtn").click(function () {
			var checkids = $("#activityListBody input[type='checkbox']:checked");
			if (checkids.length == 0) {
				alert("请选择要删除的活动");
				return;
			}

			if (window.confirm("确定要删除吗？")) {

				var ids = "";

				$.each(checkids, function (i, obj) {
					ids += "id=" + obj.value;
					if (i < checkids.length - 1) {
						ids += "&"
					}
				});

				$.ajax({
					url: "workbench/activity/deleteActivityByIds.do",
					data: ids,
					type: "post",
					dataType: "json",
					success: function (data) {
						if (data.code == 1) {
							pageList(1, $("#activityPages").bs_pagination("getOption", "rowsPerPage"));
						} else {
							alert(data.massage);
						}
					}
				})
			}
		});

		//点击修改按钮查询模态窗口的数据
		$("#updateActivityBtn").click(function () {
			var checkAct = $("#activityListBody input[type='checkbox']:checked");
			if (checkAct.size() == 0) {
				alert("请选择要修改的活动");
				return;
			} else if (checkAct.size() > 1) {
				alert("一次只能修改一个活动哦！");
				return;
			} else {
				var activityId = checkAct[0].value;
				//发送请求查询当前活动的详细信息
				$.ajax({
					url: "workbench/activity/queryActivityById.do",
					data: {
						id: activityId
					},
					dataType: "json",
					type: "post",
					success: function (data) {

						$("#edit-activityName").val(data.name);
						$("#edit-startDate").val(data.startDate);
						$("#edit-endDate").val(data.endDate);
						$("#edit-cost").val(data.cost);
						$("#edit-description").val(data.description);
						$("#edit-activityId").val(data.id);
						$("#edit-activityOwner").val(data.owner);
					}
				})
				$("#editActivityModal").modal("show");
			}

		});

		//点击更新
		$("#editActivityBtn").click(function () {
			var id = $("#edit-activityId").val();
			var owner = $("#edit-activityOwner").val();
			var name = $.trim($("#edit-activityName").val());
			var startDate = $("#edit-startDate").val();
			var endDate = $("#edit-endDate").val();
			var cost = $.trim($("#edit-cost").val());
			var description = $.trim($("#edit-description").val());

			if (owner == "") {
				alert("所有者不能为空");
				return;
			}
			if (name == "") {
				alert("名称不能为空");
				return;
			}
			if (startDate != "" && endDate != "") {
				if (startDate > endDate) {
					alert("开始日期不应在结束日期之后");
					return;
				}
			}
			var regExp = /^(([1-9]\d*)|0)$/;
			if (!regExp.test(cost)) {
				alert("成本只能为非负整数");
				return;
			}

			$.ajax({
				url: "workbench/activity/saveUpdateActivity.do",
				data: {
					id: id,
					owner: owner,
					name: name,
					startDate: startDate,
					endDate, endDate,
					cost, cost,
					description: description
				},
				dataType: "json",
				type: "post",
				success: function (data) {
					if (data.code == "1") {
						//保存成功，关闭模态窗口，刷新页面列表
						$("#createActivityModal").modal("hide");
						pageList($("#activityPages").bs_pagination('getOption', 'currentPage'), $("#activityPages").bs_pagination('getOption', 'rowsPerPage'))
						$("#editActivityModal").modal("hide");
					} else {
						alert(data.massage);
						//保存失败
					}

				}

			})

		});

		//批量导出
		$("#exportActivityAllBtn").click(function () {
			window.location.href="workbench/activity/exportToFileWithAllActivity.do"
		});

		//选择导出
		$("#exportActivityXzBtn").click(function () {
			var $check = $("#activityListBody input[type='checkbox']:checked");
			if ($check.size() < 1) {
				alert("每次至少选择导出一条记录")
				return;
			}
			var ids = "";
			$.each($check, function (i, check) {
				ids += "id=" + check.value
				if (i < $check.size()-1) {
					 ids+= "&";
				}
			});

			window.location.href = "workbench/activity/exportChangeActivity.do?" + ids;
		});

		//导入文件
		$("#importActivityBtn").click(function () {
			var fileName = $("#activityFile").val();
			var str = fileName.substr(fileName.lastIndexOf(".") + 1).toLocaleLowerCase();
			if ("xls" != str) {
				alert("只支持.xls文件");
				return;
			}
			var activityFile = $("#activityFile")[0].files[0];//文件
			if (activityFile.size > 5 * 1024 * 1024) {
				alert("文件大小不超过5MB")
				return;
			}
			var formData = new FormData();
			formData.append("activityFile", activityFile);
			$.ajax({
				url:"workbench/activity/importActivityWithFile.do",
				data: formData,
				processData:false,
				contentType: false,
				dataType:"json",
				type:"post",
				success: function (data) {
					if (data.code == 1) {
						alert("成功导入"+data.returnData+"条数据")
						pageList(1, $("#activityPages").bs_pagination("getOption", "rowsPerPage"));
						$("#importActivityModal").modal("hide");
					} else {
						alert(data.massage);
					}
				}
			})
		});



	});

	/*
	* 分页查询
	* */
	function pageList(pageNo, pageSize) {
		var name = $.trim($("#search-name").val());
		var owner = $.trim($("#search-owner").val());
		var startDate = $("#search-startTime").val();
		var endDate = $("#search-endTime").val();

		$.ajax({
			url: "workbench/activity/queryActivityByConditionForPage.do",
			data: {
				name:name,
				owner:owner,
				startDate:startDate,
				endDate:endDate,
				pageNo:pageNo,
				pageSize,pageSize
			},
			type: "get",
			dataType:"json",
			success: function (data) {
				var html=""

				$.each(data.activityList, function (i, activity) {
					html+='<tr class="active">';
					html+='<td><input type="checkbox" value="'+activity.id+'"/></td>';
					html+='<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href=\'workbench/activity/goActivityDetail.do?id='+activity.id+'\'">'+activity.name+'</a></td>'
					html+='<td>'+activity.owner+'</td>'
					html+='<td>'+activity.startDate+'</td>'
					html+='<td>'+activity.endDate+'</td>'
					html+='</tr>'

				});
				$("#activityListBody").html(html);

				//元素生成结束后，将全选框设置为不选中
				$("#activityListBody").prop("checked", false);

				$("#activityPages").bs_pagination({
					currentPage: pageNo,//页码
					rowsPerPage:pageSize,//每页显示的记录条数
					totalPages: data.total % pageSize == 0 ? data.total / pageSize : parseInt(data.total / pageSize) + 1,//总页数
					totalRows:data.total,//总记录条数

					visiblePageLinks: 3,

					showGoToPage: true,//是否显示跳转到
					showRowsPerPage: true,//是否显示每页显示条数
					showRowsInfo: true,//是否显示记录的信息
					
					onChangePage:function (event,pageObj) {
						pageList(pageObj.currentPage,pageObj.rowsPerPage);
					}


				})

			}

		})

	}
</script>
</head>
<body>

	<!-- 创建市场活动的模态窗口 -->
	<div class="modal fade" id="createActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel1">创建市场活动</h4>
				</div>
				<div class="modal-body">
				
					<form class="form-horizontal" role="form" id="create-form">
					
						<div class="form-group">
							<label for="create-ActivityOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="create-ActivityOwner" >
									<c:forEach items="${requestScope.userList}" var="user">
										<option value="${user.id}" ${sessionScope.sessionUser.id==user.id?'selected':''}>${user.name}</option>
									</c:forEach>
								 <%-- <option>zhangsan</option>
								  <option>lisi</option>
								  <option>wangwu</option>--%>
								</select>
							</div>
                            <label for="create-ActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-ActivityName">
                            </div>
						</div>
						
						<div class="form-group">
							<label for="create-startTime" class="col-sm-2 control-label">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control myTime" id="create-startTime" readonly>
							</div>
							<label for="create-endTime" class="col-sm-2 control-label">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control myTime" id="create-endTime" readonly>
							</div>
						</div>
                        <div class="form-group">

                            <label for="create-cost" class="col-sm-2 control-label">成本</label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-cost">
                            </div>
                        </div>
						<div class="form-group">
							<label for="create-description" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="create-description"></textarea>
							</div>
						</div>
						
					</form>
					
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="saveCreateActivity" <%--data-dismiss="modal"--%>>保存</button>
				</div>
			</div>
		</div>
	</div>
	
	<!-- 修改市场活动的模态窗口 -->
	<div class="modal fade" id="editActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel2">修改市场活动</h4>
				</div>
				<div class="modal-body">
				
					<form class="form-horizontal" role="form">
						<input type="hidden" id="edit-activityId">
						<div class="form-group">
							<label for="edit-activityOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="edit-activityOwner">
								 <c:forEach items="${requestScope.userList}" var="user">
									 <option value="${user.id}">${user.name}</option>
								 </c:forEach>
									<%-- <option>zhangsan</option>
								  <option>lisi</option>
								  <option>wangwu</option>--%>
								</select>
							</div>
                            <label for="edit-activityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="edit-activityName" value="发传单">
                            </div>
						</div>

						<div class="form-group">
							<label for="edit-startDate" class="col-sm-2 control-label">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-startDate" >
							</div>
							<label for="edit-endDate" class="col-sm-2 control-label">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-endDate" >
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-cost" class="col-sm-2 control-label">成本</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-cost" value="5,000">
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-description" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="edit-description">市场活动Marketing，是指品牌主办或参与的展览会议与公关市场活动，包括自行主办的各类研讨会、客户交流会、演示会、新产品发布会、体验会、答谢会、年会和出席参加并布展或演讲的展览会、研讨会、行业交流会、颁奖典礼等</textarea>
							</div>
						</div>
						
					</form>
					
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="editActivityBtn" <%--data-dismiss="modal"--%>>更新</button>
				</div>
			</div>
		</div>
	</div>
	
	<!-- 导入市场活动的模态窗口 -->
    <div class="modal fade" id="importActivityModal" role="dialog">
        <div class="modal-dialog" role="document" style="width: 85%;">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">
                        <span aria-hidden="true">×</span>
                    </button>
                    <h4 class="modal-title" id="myModalLabel">导入市场活动</h4>
                </div>
                <div class="modal-body" style="height: 350px;">
                    <div style="position: relative;top: 20px; left: 50px;">
                        请选择要上传的文件：<small style="color: gray;">[仅支持.xls]</small>
                    </div>
                    <div style="position: relative;top: 40px; left: 50px;">
                        <input type="file" id="activityFile">
                    </div>
                    <div style="position: relative; width: 400px; height: 320px; left: 45% ; top: -40px;" >
                        <h3>重要提示</h3>
                        <ul>
                            <li>操作仅针对Excel，仅支持后缀名为XLS的文件。</li>
                            <li>给定文件的第一行将视为字段名。</li>
                            <li>请确认您的文件大小不超过5MB。</li>
                            <li>日期值以文本形式保存，必须符合yyyy-MM-dd格式。</li>
                            <li>日期时间以文本形式保存，必须符合yyyy-MM-dd HH:mm:ss的格式。</li>
                            <li>默认情况下，字符编码是UTF-8 (统一码)，请确保您导入的文件使用的是正确的字符编码方式。</li>
                            <li>建议您在导入真实数据之前用测试文件测试文件导入功能。</li>
                        </ul>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                    <button id="importActivityBtn" type="button" class="btn btn-primary">导入</button>
                </div>
            </div>
        </div>
    </div>
	
	
	<div>
		<div style="position: relative; left: 10px; top: -10px;">
			<div class="page-header">
				<h3>市场活动列表</h3>
			</div>
		</div>
	</div>
	<div style="position: relative; top: -20px; left: 0px; width: 100%; height: 100%;">
		<div style="width: 100%; position: absolute;top: 5px; left: 10px;">
		
			<div class="btn-toolbar" role="toolbar" style="height: 80px;">
				<form class="form-inline" role="form" style="position: relative;top: 8%; left: 5px;">
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">名称</div>
				      <input class="form-control" type="text" id="search-name">
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">所有者</div>
				      <input class="form-control" type="text" id="search-owner">
				    </div>
				  </div>


				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">开始日期</div>
					  <input class="form-control myTime" type="text" id="search-startTime" />
				    </div>
				  </div>
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">结束日期</div>
					  <input class="form-control myTime" type="text" id="search-endTime">
				    </div>
				  </div>
				  
				  <button type="button" class="btn btn-default" id="searchBtn">查询</button>
				  
				</form>
			</div>
			<div class="btn-toolbar" role="toolbar" style="background-color: #F7F7F7; height: 50px; position: relative;top: 5px;">
				<div class="btn-group" style="position: relative; top: 18%;">
				  <button type="button" class="btn btn-primary" id="createActivity" <%--data-toggle="modal" data-target="#createActivityModal"--%>><span class="glyphicon glyphicon-plus"></span> 创建</button>
				  <button type="button" class="btn btn-default" id="updateActivityBtn" <%--data-toggle="modal" data-target="#editActivityModal"--%>><span class="glyphicon glyphicon-pencil"></span> 修改</button>
				  <button type="button" class="btn btn-danger" id="deleteActivityBtn"><span class="glyphicon glyphicon-minus"></span> 删除</button>
				</div>
				<div class="btn-group" style="position: relative; top: 18%;">
                    <button type="button" class="btn btn-default" data-toggle="modal" data-target="#importActivityModal" ><span class="glyphicon glyphicon-import"></span> 上传列表数据（导入）</button>
                    <button id="exportActivityAllBtn" type="button" class="btn btn-default"><span class="glyphicon glyphicon-export"></span> 下载列表数据（批量导出）</button>
                    <button id="exportActivityXzBtn" type="button" class="btn btn-default"><span class="glyphicon glyphicon-export"></span> 下载列表数据（选择导出）</button>
                </div>
			</div>
			<div style="position: relative;top: 10px;">
				<table class="table table-hover">
					<thead>
						<tr style="color: #B3B3B3;">
							<td><input type="checkbox" id="checkAll"/></td>
							<td>名称</td>
                            <td>所有者</td>
							<td>开始日期</td>
							<td>结束日期</td>
						</tr>
					</thead>
					<tbody id="activityListBody">
						<%--<tr class="active">
							<td><input type="checkbox" /></td>
							<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='detail.jsp';">发传单</a></td>
                            <td>zhangsan</td>
							<td>2020-10-10</td>
							<td>2020-10-20</td>
						</tr>
                        <tr class="active">
                            <td><input type="checkbox" /></td>
                            <td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='detail.jsp';">发传单</a></td>
                            <td>zhangsan</td>
                            <td>2020-10-10</td>
                            <td>2020-10-20</td>
                        </tr>--%>
					</tbody>
				</table>
			</div>

			<div id="activityPages"></div>
			
			<%--<div style="height: 50px; position: relative;top: 30px;">
				<div>
					<button type="button" class="btn btn-default" style="cursor: default;">共<b>50</b>条记录</button>
				</div>
				<div class="btn-group" style="position: relative;top: -34px; left: 110px;">
					<button type="button" class="btn btn-default" style="cursor: default;">显示</button>
					<div class="btn-group">
						<button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">
							10
							<span class="caret"></span>
						</button>
						<ul class="dropdown-menu" role="menu">
							<li><a href="#">20</a></li>
							<li><a href="#">30</a></li>
						</ul>
					</div>
					<button type="button" class="btn btn-default" style="cursor: default;">条/页</button>
				</div>
				<div style="position: relative;top: -88px; left: 285px;">
					<nav>
						<ul class="pagination">
							<li class="disabled"><a href="#">首页</a></li>
							<li class="disabled"><a href="#">上一页</a></li>
							<li class="active"><a href="#">1</a></li>
							<li><a href="#">2</a></li>
							<li><a href="#">3</a></li>
							<li><a href="#">4</a></li>
							<li><a href="#">5</a></li>
							<li><a href="#">下一页</a></li>
							<li class="disabled"><a href="#">末页</a></li>
						</ul>
					</nav>
				</div>
			</div>--%>
			
		</div>
		
	</div>
</body>
</html>