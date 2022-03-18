<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
%>
<html>
<head>
	<base href="<%=basePath%>">
<meta charset="UTF-8">

<link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<link href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" type="text/css" rel="stylesheet" />

<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>
<script type="text/javascript" src="jquery/bs_typeahead/bootstrap3-typeahead.min.js"></script>
<script type="text/javascript">

	$(function () {

		//日期
		$(".myTime").datetimepicker({
			language: "zh-CN",
			format: "yyyy-mm-dd",
			minView: "month",
			autoclose: true,
			todayBtn: true,
			pickerPosition: "button-right",
			clearBtn: true,
			initialDate: new Date()
		})

		/*
		* 给阶段下拉框绑定change事件
		* */
		$("#create-stage").change(function () {
			var stageText = $("#create-stage option:selected").text();

			if (stageText == "") {
				$("#create-possibility").val("");
				return;
			}
			$.ajax({
				url: "workbench/transaction/getPossibility.do",
				data: {stageText: stageText},
				type: "post",
				dataType: "json",
				success: function (data) {
					$("#create-possibility").val(data + "%");
				}
			});
		});

		/**
		 * 自动补全客户姓名
		 * */
		$("#create-customerName").typeahead({
			source: function (jquery, process) {
				$.ajax({
					url: "workbench/transaction/queryCustomerNameByName.do",
					data: {
						name: jquery
					},
					type: "post",
					dataType: "json",
					success: function (data) {
						process(data);
					}
				})
			}
		})


		//打开搜索市场活动模态窗口
		$("#openActivityModel-A").click(function () {
			$("#queryActivityText").val("");
			$("#activityListtBody").html("");
			$("#findMarketActivity").modal("show");

		});
		/*
		* 查找市场活动
		* */
		$("#queryActivityText").keyup(function () {
			var activityName = $("#queryActivityText").val();

			$.ajax({
				url: "workbench/transaction/queryActivityByNameForCreateTran.do",
				data: {name: activityName},
				dataType: "json",
				type: "post",
				success: function (data) {

					var html = "";
					$.each(data, function (i, activity) {
						html += '<tr>';
						html += '	<td><input type="radio" name="activity" activityName="' + activity.name + '" value="' + activity.id + '"/></td>';
						html += '	<td>' + activity.name + '</td>';
						html += '	<td>' + activity.startDate + '</td>';
						html += '	<td>' + activity.endDate + '</td>';
						html += '	<td>' + activity.owner + '</td>';
						html += '</tr>';
					});
					$("#activityListtBody").html(html);
				}
			})
		});

		/*
		* 将选中的市场活动添加到页面，并关闭模态窗口
		* */
		$("#activityListtBody").on("click", "input[name='activity']", function () {
			$("#activityId").val($("input[name='activity']:checked").val())
			$("#create-activityName").val($("input[name='activity']:checked").attr("activityName"))
			$("#findMarketActivity").modal("hide");
		});


		/*
		* 打开查找联系人的模态窗口
		* */
		$("#openContactsModel-A").click(function () {
			$("#queryContactsText").val("");
			$("#contactsListtBody").html("");
			$("#findContacts").modal("show");
		});

		/*
		* 查找市场活动
		* */
		$("#queryContactsText").keyup(function () {
			var contactsName = $("#queryContactsText").val();

			$.ajax({
				url: "workbench/transaction/queryContactsByNameForCreateTran.do",
				data: {fullname: contactsName},
				dataType: "json",
				type: "post",
				success: function (data) {

					var html = "";
					$.each(data, function (i, contacts) {
						html+='<tr>';
						html+='	<td><input type="radio" name="contacts" contactsName="'+contacts.fullname+'" id="'+contacts.id+'"/></td>';
						html+='	<td>'+contacts.fullname+'</td>';
						html+='	<td>'+contacts.email+'</td>';
						html+='	<td>'+contacts.mphone+'</td>';
						html+='</tr>';
					});
					$("#contactsListtBody").html(html);
				}
			})
		});

		/*
		* 将选中的联系人添加到页面，并关闭模态窗口
		* */
		$("#contactsListtBody").on("click", "input[name='contacts']", function () {
			$("#create-contactsId").val($("input[name='contacts']:checked").val())
			$("#create-contactsName").val($("input[name='contacts']:checked").attr("contactsName"))
			$("#findContacts").modal("hide");
		});

		/*
		* 点击保存按钮保存交易
		* */
		$("#saveBtn").click(function () {

			var owner=$("#create-owner").val();
			var money=$.trim($("#create-money").val());
			var name=$.trim($("#create-name").val());
			var expectedDate=$("#create-expectedDate").val();
			var customerName=$.trim($("#create-customerName").val());
			var stage=$("#create-stage").val();
			var type=$("#create-type").val();
			var source=$("#create-source").val();
			var activityId=$("#create-activityId").val();
			var contactsId=$("#create-contactsId").val();
			var description=$.trim($("#create-description").val());
			var contactSummary=$.trim($("#create-contactSummary").val());
			var nextContactTime=$("#create-nextContactTime").val();

			if (owner == "") {
				alert("所有者不能为空");
				return;
			}
			if (name == "") {
				alert("名称不能为空");
				return;
			}
			if (expectedDate == "") {
				alert("预计成交日期不能为空");
				return;
			}
			if (customerName == "") {
				alert("客户名称不能为空");
				return;
			}
			if (stage == "阶段不能为空") {
				alert("");
				return;
			}

			$.ajax({
				url:"workbench/transaction/saveCreateTran.do",
				data:{
					owner:owner,
					money:money,
					name:name,
					expectedDate:expectedDate,
					customerName:customerName,
					stage:stage,
					type:type,
					source:source,
					activityId:activityId,
					contactsId:contactsId,
					description:description,
					contactSummary:contactSummary,
					nextContactTime:nextContactTime
				},
				dataType:"json",
				type:"post",
				success: function (data) {
					if (data.code == '1') {
						window.location.href = "workbench/transaction/index.do";
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

	<!-- 查找市场活动 -->	
	<div class="modal fade" id="findMarketActivity" role="dialog">
		<div class="modal-dialog" role="document" style="width: 80%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title">查找市场活动</h4>
				</div>
				<div class="modal-body">
					<div class="btn-group" style="position: relative; top: 18%; left: 8px;">
						<form class="form-inline" role="form">
						  <div class="form-group has-feedback">
						    <input type="text" class="form-control" id="queryActivityText" style="width: 300px;" placeholder="请输入市场活动名称，支持模糊查询">
						    <span class="glyphicon glyphicon-search form-control-feedback"></span>
						  </div>
						</form>
					</div>
					<table id="activityTable3" class="table table-hover" style="width: 900px; position: relative;top: 10px;">
						<thead>
							<tr style="color: #B3B3B3;">
								<td></td>
								<td>名称</td>
								<td>开始日期</td>
								<td>结束日期</td>
								<td>所有者</td>
							</tr>
						</thead>
						<tbody id="activityListtBody">

						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>

	<!-- 查找联系人 -->	
	<div class="modal fade" id="findContacts" role="dialog">
		<div class="modal-dialog" role="document" style="width: 80%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title">查找联系人</h4>
				</div>
				<div class="modal-body">
					<div class="btn-group" style="position: relative; top: 18%; left: 8px;">
						<form class="form-inline" role="form">
						  <div class="form-group has-feedback">
						    <input type="text" class="form-control" id="queryContactsText" style="width: 300px;" placeholder="请输入联系人名称，支持模糊查询">
						    <span class="glyphicon glyphicon-search form-control-feedback"></span>
						  </div>
						</form>
					</div>
					<table id="activityTable" class="table table-hover" style="width: 900px; position: relative;top: 10px;">
						<thead>
							<tr style="color: #B3B3B3;">
								<td></td>
								<td>名称</td>
								<td>邮箱</td>
								<td>手机</td>
							</tr>
						</thead>
						<tbody id="contactsListtBody">

						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>
	
	
	<div style="position:  relative; left: 30px;">
		<h3>创建交易</h3>
	  	<div style="position: relative; top: -40px; left: 70%;">
			<button type="button" class="btn btn-primary" id="saveBtn">保存</button>
			<button type="button" class="btn btn-default">取消</button>
		</div>
		<hr style="position: relative; top: -40px;">
	</div>
	<form class="form-horizontal" role="form" style="position: relative; top: -30px;">
		<div class="form-group">
			<label for="create-owner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
			<div class="col-sm-10" style="width: 300px;">
				<select class="form-control" id="create-owner">
				 <c:forEach items="${userList}" var="user">
					 <option value="${user.id}">${user.name}</option>
				 </c:forEach>
				</select>
			</div>
			<label for="create-money" class="col-sm-2 control-label">金额</label>
			<div class="col-sm-10" style="width: 300px;">
				<input type="text" class="form-control" id="create-money">
			</div>
		</div>
		
		<div class="form-group">
			<label for="create-name" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
			<div class="col-sm-10" style="width: 300px;">
				<input type="text" class="form-control" id="create-name">
			</div>
			<label for="create-expectedDate" class="col-sm-2 control-label">预计成交日期<span style="font-size: 15px; color: red;">*</span></label>
			<div class="col-sm-10" style="width: 300px;">
				<input type="text" class="form-control myTime" id="create-expectedDate" readonly>
			</div>
		</div>
		
		<div class="form-group">
			<label for="create-customerName" class="col-sm-2 control-label">客户名称<span style="font-size: 15px; color: red;">*</span></label>
			<div class="col-sm-10" style="width: 300px;">
				<input type="text" class="form-control" id="create-customerName" placeholder="支持自动补全，输入客户不存在则新建">
			</div>
			<label for="create-stage" class="col-sm-2 control-label">阶段<span style="font-size: 15px; color: red;">*</span></label>
			<div class="col-sm-10" style="width: 300px;">
			  <select class="form-control" id="create-stage">
			  	<option></option>
			  	<c:forEach items="${stageList}" var="stage">
					<option value="${stage.id}">${stage.value}</option>
				</c:forEach>
			  </select>
			</div>
		</div>
		
		<div class="form-group">
			<label for="create-type" class="col-sm-2 control-label">类型</label>
			<div class="col-sm-10" style="width: 300px;">
				<select class="form-control" id="create-type">
				  <option></option>
				  <c:forEach var="type" items="${typeList}">
					  <option value="${type.id}">${type.value}</option>
				  </c:forEach>
				</select>
			</div>
			<label for="create-possibility" class="col-sm-2 control-label">可能性</label>
			<div class="col-sm-10" style="width: 300px;">
				<input type="text" class="form-control" id="create-possibility" readonly>
			</div>
		</div>
		
		<div class="form-group">
			<label for="create-source" class="col-sm-2 control-label">来源</label>
			<div class="col-sm-10" style="width: 300px;">
				<select class="form-control" id="create-source">
				  <option></option>
				  <c:forEach items="${sourceList}" var="source">
					  <option value="${source.id}">${source.value}</option>
				  </c:forEach>
				</select>
			</div>
			<label for="create-activityName" class="col-sm-2 control-label">市场活动源&nbsp;&nbsp;<a href="javascript:void(0);" id="openActivityModel-A" <%--data-toggle="modal" data-target="#findMarketActivity"--%>><span class="glyphicon glyphicon-search"></span></a></label>
			<div class="col-sm-10" style="width: 300px;">
				<input type="hidden" id="create-activityId">
				<input type="text" class="form-control" id="create-activityName" readonly>
			</div>
		</div>
		
		<div class="form-group">
			<label for="create-contactsName" class="col-sm-2 control-label">联系人名称&nbsp;&nbsp;<a href="javascript:void(0);" id="openContactsModel-A" <%--data-toggle="modal" data-target="#findContacts"--%>><span class="glyphicon glyphicon-search"></span></a></label>
			<div class="col-sm-10" style="width: 300px;">
				<input type="hidden" id="create-contactsId">
				<input type="text" class="form-control" id="create-contactsName" readonly>
			</div>
		</div>
		
		<div class="form-group">
			<label for="create-description" class="col-sm-2 control-label">描述</label>
			<div class="col-sm-10" style="width: 70%;">
				<textarea class="form-control" rows="3" id="create-description"></textarea>
			</div>
		</div>
		
		<div class="form-group">
			<label for="create-contactSummary" class="col-sm-2 control-label">联系纪要</label>
			<div class="col-sm-10" style="width: 70%;">
				<textarea class="form-control" rows="3" id="create-contactSummary"></textarea>
			</div>
		</div>
		
		<div class="form-group">
			<label for="create-nextContactTime" class="col-sm-2 control-label " >下次联系时间</label>
			<div class="col-sm-10" style="width: 300px;">
				<input type="text" class="form-control myTime" id="create-nextContactTime" readonly>
			</div>
		</div>
		
	</form>
</body>
</html>