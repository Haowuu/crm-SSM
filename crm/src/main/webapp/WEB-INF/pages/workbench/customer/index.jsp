<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<html>
<head>
	<base href="<%=basePath%>">
<meta charset="UTF-8">

<link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<link href="jquery/bs_pagination-master/css/jquery.bs_pagination.min.css" type="text/css" rel="stylesheet" />
<link href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" type="text/css" rel="stylesheet" />

<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
<script type="text/javascript" src="jquery/bs_pagination-master/js/jquery.bs_pagination.min.js"></script>
<script type="text/javascript" src="jquery/bs_pagination-master/localization/en.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.min.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>

<script type="text/javascript">

	$(function(){
		pageList(1, 3);

		//定制字段
		$("#definedColumns > li").click(function(e) {
			//防止下拉菜单消失
	        e.stopPropagation();
	    });

		//日历
		$(".myTime").datetimepicker({
			language:"zh-CN",
			format:"yyyy-mm-dd",
			minView:"month",
			autoclose:true,
			todayBtn:true,
			pickerPosition:"top-left",
			clearBtn:true,
			initialDate: new Date()

		})

		//打开创建的模态窗口
		$("#openCreateBtn").click(function () {
			$("#createCustomerModal").modal("show");
		});

		/*
		* 保存新创建的客户数据
		* */
		$("#saveCreateBtn").click(function () {
			//获取表单数据
			var owner=$("#create-owner").val();
			var name=$.trim($("#create-name").val());
			var website=$.trim($("#create-website").val());
			var phone=$.trim($("#create-phone").val());
			var description=$.trim($("#create-description").val());
			var contactSummary = $.trim($("#create-contactSummary").val());
			var nextContactTime=$("#create-nextContactTime").val();
			var address=$.trim($("#create-address").val());
			//表单验证
			if (owner == "") {
				alert("所有者不能为空");
				return;
			}
			if (name == "") {
				alert("名称不能为空");
				return;
			}

			$.ajax({
				url: "workbench/customer/saveCreateCustomer.do",
				data: {
					owner:owner,
					name:name,
					website:website,
					phone:phone,
					description:description,
					contactSummary:contactSummary,
					nextContactTime:nextContactTime,
					address:address
				},
				type:"post",
				dataType: "json",
				success: function (data) {
					if (data.code == "1") {
						pageList(1, $("#changePageDiv").bs_pagination("getOption", "rowsPerPage"));
						$("#createCustomerModal").modal("hide");
						$("#create-form")[0].reset();
					} else {
						alert(data.massage);
					}
				}
			})

		});

		$("#searchBtn").click(function () {
			pageList(1, 3);
		});

		/*
		* 全选和全不选
		* */
		$("#checkAllBox").click(function () {
			$("#pageListBody input[type='checkbox']").prop("checked", this.checked);
		});

		$("#pageListBody").on("click", "input[type='checkbox']", function () {
			$("#checkAllBox").prop("checked",$("#pageListBody input[type='checkbox']:checked").length==$("#pageListBody input[type='checkbox']").length)
		});

		/*
		* 删除市场活动
		* */
		$("#deleteBtn").click(function () {
			var checked = $("#pageListBody input[type='checkbox']:checked");
			if (checked.length == 0) {
				alert("请选择要删除的客户");
				return;
			}
			var ids=""
			$.each(checked, function (i, o) {
				ids += "id=" + o.value;
				if (i < checked.length - 1) {
					ids += "&";
				}
			});

			$.ajax({
				url:"workbench/customer/deleteCustomerByIds.do",
				data:ids,
				dataType:"json",
				type:"post",
				success: function (data) {
					if (data.code == '1') {
						pageList(1, $("#changePageDiv").bs_pagination("getOption", "rowsPerPage"));
					} else {
						alert(data.massage);
					}
				}
			})

		});

		//点击修改按钮，打开模态窗口
		$("#openEditBtn").click(function () {
			var checked = $("#pageListBody input[type='checkbox']:checked");
			if (checked.length == 0) {
				alert("请选择要修改的客户项");
				return;
			}
			if (checked.length > 1) {
				alert("一次只能修改一项");
				return;
			}
			var id = checked[0].value;

			$.ajax({
				url:"workbench/customer/queryCustomerByIdToEdit.do",
				data:{
					id: id
				},
				dataType:"json",
				type: "post",
				success: function (data) {
					$("#edit-customerId").val(data.id);
					$("#edit-owner").val(data.owner);
					$("#edit-name").val(data.name);
					$("#edit-website").val(data.website);
					$("#edit-phone").val(data.phone);
					$("#edit-description").val(data.description);
					$("#edit-contactSummary").val(data.contactSummary);
					$("#edit-nextContactTime").val(data.nextContactTime);
					$("#edit-address").val(data.address);

					$("#editCustomerModal").modal("show");
				}
			})
		});

		/*
		* 保存修改的客户信息
		* */
		$("#editBtn").click(function () {
			var id=$("#edit-customerId").val();
			var owner=$("#edit-owner").val();
			var name=$("#edit-name").val();
			var website=$("#edit-website").val();
			var phone=$("#edit-phone").val();
			var description=$("#edit-description").val();
			var contactSummary=$("#edit-contactSummary").val();
			var nextContactTime=$("#edit-nextContactTime").val();
			var address=$("#edit-address").val();

			//表单验证
			if (owner == "") {
				alert("所有者不能为空");
				return;
			}
			if (name == "") {
				alert("名称不能为空");
				return;
			}

			$.ajax({
				url:"workbench/customer/saveEditCustomer.do",
				data:{
					id:id,
					owner:owner,
					name:name,
					website:website,
					phone:phone,
					description:description,
					contactSummary:contactSummary,
					nextContactTime:nextContactTime,
					address:address
				},
				dataType:"json",
				type:"post",
				success: function (data) {
					if (data.code == '1') {
						pageList($("#changePageDiv").bs_pagination("getOption", "currentPage"), $("#changePageDiv").bs_pagination("getOption", "rowsPerPage"));
						$("#editCustomerModal").modal("hide");
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
		var name=$("#search-name").val()
		var owner=$("#search-owner").val()
		var phone=$("#search-phone").val()
		var website=$("#search-website").val()

		$.ajax({
			url:"workbench/customer/pageList.do",
			data: {
				name: name,
				owner: owner,
				phone: phone,
				website: website,
				pageNo: pageNo,
				pageSize:pageSize
			},
			dataType:"json",
			type:"post",
			success: function (data) {

				var html = "";
				$.each(data.customerList, function (i, customer) {
					html+='<tr>';
					html+='	<td><input type="checkbox" value="'+customer.id+'" "/></td>';
					html+='	<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href=\'workbench/customer/toCustomerDetail.do?id='+customer.id+'\'">'+customer.name+'</a></td>';
					html+='	<td>'+customer.owner+'</td>';
					html+='	<td>'+customer.phone+'</td>';
					html+='	<td>'+customer.website+'</td>';
					html+='</tr>';
				});
				$("#pageListBody").html(html);

				//取消全选框的选中状态
				$("#checkAllBox").prop("checked", false);



				$("#changePageDiv").bs_pagination({
					currentPage: pageNo,
					rowsPerPage:pageSize,
					totalPages: data.total % pageSize == 0 ? data.total / pageSize : parseInt(data.total / pageSize) + 1,
					totalRows:data.total,

					visiblePageLinks:5,

					showGoToPage:true,
					showRowsInfo:true,
					showRowsPerPage:true,

					onChangePage: function (event, pageObj) {
						pageList(pageObj.currentPage, pageObj.rowsPerPage);
					}
				})

			}
		})

	}
	
</script>
</head>
<body>

	<!-- 创建客户的模态窗口 -->
	<div class="modal fade" id="createCustomerModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel1">创建客户</h4>
				</div>
				<div class="modal-body">
					<form class="form-horizontal" role="form" id="create-form">
					
						<div class="form-group">
							<label for="create-owner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="create-owner">
									<c:forEach items="${userList}" var="user">
										<option value="${user.id}" ${sessionScope.sessionUser.id==user.id?"selected":""}>${user.name}</option>
									</c:forEach>
								  <%--<option>zhangsan</option>
								  <option>lisi</option>
								  <option>wangwu</option>--%>
								</select>
							</div>
							<label for="create-name" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="create-name">
							</div>
						</div>
						
						<div class="form-group">
                            <label for="create-website" class="col-sm-2 control-label">公司网站</label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-website">
                            </div>
							<label for="create-phone" class="col-sm-2 control-label">公司座机</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="create-phone">
							</div>
						</div>
						<div class="form-group">
							<label for="create-description" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="create-description"></textarea>
							</div>
						</div>
						<div style="height: 1px; width: 103%; background-color: #D5D5D5; left: -13px; position: relative;"></div>

                        <div style="position: relative;top: 15px;">
                            <div class="form-group">
                                <label for="create-contactSummary" class="col-sm-2 control-label">联系纪要</label>
                                <div class="col-sm-10" style="width: 81%;">
                                    <textarea class="form-control" rows="3" id="create-contactSummary"></textarea>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="create-nextContactTime" class="col-sm-2 control-label">下次联系时间</label>
                                <div class="col-sm-10" style="width: 300px;">
                                    <input type="text" class="form-control myTime" id="create-nextContactTime" readonly>
                                </div>
                            </div>
                        </div>

                        <div style="height: 1px; width: 103%; background-color: #D5D5D5; left: -13px; position: relative; top : 10px;"></div>

                        <div style="position: relative;top: 20px;">
                            <div class="form-group">
                                <label for="create-address" class="col-sm-2 control-label">详细地址</label>
                                <div class="col-sm-10" style="width: 81%;">
                                    <textarea class="form-control" rows="1" id="create-address"></textarea>
                                </div>
                            </div>
                        </div>
					</form>
					
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="saveCreateBtn" <%--data-dismiss="modal"--%>>保存</button>
				</div>
			</div>
		</div>
	</div>
	
	<!-- 修改客户的模态窗口 -->
	<div class="modal fade" id="editCustomerModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel">修改客户</h4>
				</div>
				<div class="modal-body">
					<form class="form-horizontal" role="form">
					<input type="hidden" id="edit-customerId">
						<div class="form-group">
							<label for="edit-owner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="edit-owner">
									<c:forEach items="${userList}" var="user">
										<option value="${user.id}">${user.name}</option>
									</c:forEach>
									<%--<option>zhangsan</option>
								  <option>lisi</option>
								  <option>wangwu</option>--%>
								</select>
							</div>
							<label for="edit-name" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-name" value="动力节点">
							</div>
						</div>
						
						<div class="form-group">
                            <label for="edit-website" class="col-sm-2 control-label">公司网站</label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="edit-website" value="http://www.bjpowernode.com">
                            </div>
							<label for="edit-phone" class="col-sm-2 control-label">公司座机</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-phone" value="010-84846003">
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-description" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="edit-description"></textarea>
							</div>
						</div>
						
						<div style="height: 1px; width: 103%; background-color: #D5D5D5; left: -13px; position: relative;"></div>

                        <div style="position: relative;top: 15px;">
                            <div class="form-group">
                                <label for="edit-contactSummary" class="col-sm-2 control-label">联系纪要</label>
                                <div class="col-sm-10" style="width: 81%;">
                                    <textarea class="form-control" rows="3" id="edit-contactSummary"></textarea>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="edit-nextContactTime" class="col-sm-2 control-label">下次联系时间</label>
                                <div class="col-sm-10" style="width: 300px;">
                                    <input type="text" class="form-control myTime" id="edit-nextContactTime" readonly>
                                </div>
                            </div>
                        </div>

                        <div style="height: 1px; width: 103%; background-color: #D5D5D5; left: -13px; position: relative; top : 10px;"></div>

                        <div style="position: relative;top: 20px;">
                            <div class="form-group">
                                <label for="edit-address" class="col-sm-2 control-label">详细地址</label>
                                <div class="col-sm-10" style="width: 81%;">
                                    <textarea class="form-control" rows="1" id="edit-address">北京大兴大族企业湾</textarea>
                                </div>
                            </div>
                        </div>
					</form>
					
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="editBtn" <%--data-dismiss="modal"--%>>更新</button>
				</div>
			</div>
		</div>
	</div>
	
	
	
	
	<div>
		<div style="position: relative; left: 10px; top: -10px;">
			<div class="page-header">
				<h3>客户列表</h3>
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
				      <div class="input-group-addon">公司座机</div>
				      <input class="form-control" type="text" id="search-phone">
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">公司网站</div>
				      <input class="form-control" type="text" id="search-website">
				    </div>
				  </div>
				  
				  <button type="button" id="searchBtn" class="btn btn-default">查询</button>
				  
				</form>
			</div>
			<div class="btn-toolbar" role="toolbar" style="background-color: #F7F7F7; height: 50px; position: relative;top: 5px;">
				<div class="btn-group" style="position: relative; top: 18%;">
				  <button type="button" class="btn btn-primary" id="openCreateBtn" <%--data-toggle="modal" data-target="#createCustomerModal"--%>><span class="glyphicon glyphicon-plus"></span> 创建</button>
				  <button type="button" class="btn btn-default" id="openEditBtn" <%--data-toggle="modal" data-target="#editCustomerModal"--%>><span class="glyphicon glyphicon-pencil"></span> 修改</button>
				  <button type="button" class="btn btn-danger" id="deleteBtn"><span class="glyphicon glyphicon-minus"></span> 删除</button>
				</div>
				
			</div>
			<div style="position: relative;top: 10px;">
				<table class="table table-hover">
					<thead>
						<tr style="color: #B3B3B3;">
							<td><input type="checkbox" id="checkAllBox"/></td>
							<td>名称</td>
							<td>所有者</td>
							<td>公司座机</td>
							<td>公司网站</td>
						</tr>
					</thead>
					<tbody id="pageListBody">
						<%--<tr>
							<td><input type="checkbox" /></td>
							<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='detail.jsp';">动力节点</a></td>
							<td>zhangsan</td>
							<td>010-84846003</td>
							<td>http://www.bjpowernode.com</td>
						</tr>
                        <tr class="active">
                            <td><input type="checkbox" /></td>
                            <td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='detail.jsp';">动力节点</a></td>
                            <td>zhangsan</td>
                            <td>010-84846003</td>
                            <td>http://www.bjpowernode.com</td>
                        </tr>--%>
					</tbody>
				</table>
			</div>
			<div id="changePageDiv"></div>
			
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