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
	<link href="jquery/bs_pagination-master/css/jquery.bs_pagination.min.css" type="text/css" rel="stylesheet">

	<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
	<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
	<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
	<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>
	<script type="text/javascript" src="jquery/bs_pagination-master/js/jquery.bs_pagination.min.js"></script>
	<script type="text/javascript" src="jquery/bs_pagination-master/localization/en.js"></script>

<script type="text/javascript">

	$(function(){
		//加载交易信息
		pageList(1,6)

		//安条件搜索
		$("#searchBtn").click(function () {
			pageList(1,$("#changePageDiv").bs_pagination("getOption","rowsPerPage"))
		});



		
	});

	/*
	* 分页查询
	* */
	function pageList(pageNo,pageSize) {
		var owner = $("#search-owner").val();
		var name = $("#search-name").val();
		var customerName=$("#search-customerName").val();
		var stage = $("#search-stage").val();
		var type = $("#search-type").val();
		var source = $("#search-source").val();
		var contactName = $("#search-contactName").val();

		$.ajax({
			url:"workbench/transaction/tranPageList.do",
			data:{
				owner:owner,
				name:name,
				customerName:customerName,
				stage:stage,
				type:type,
				source:source,
				contactName:contactName,
				pageNo:pageNo,
				pageSize:pageSize
			},
			dataType:"json",
			type:"post",
			success: function (data) {
				var html = "";
				$.each(data.transactionList, function (i, tran) {
					html+='<tr>';
					html+='	<td><input type="checkbox" '+tran.id+'/></td>';
					html+='	<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href=\'workbench/transaction/toDetail.do?id='+tran.id+'\';">'+tran.name+'</a></td>';
					html+='	<td>'+tran.customerId+'</td>';
					html+='	<td>'+tran.stage+'</td>';
					html+='	<td>'+tran.type+'</td>';
					html+='	<td>'+tran.owner+'</td>';
					html+='	<td>'+tran.source+'</td>';
					html+='	<td>'+tran.contactsId+'</td>';
					html+='</tr>';
				});
				$("#pageListBody").html(html);
				var total = data.total;

				$("#changePageDiv").bs_pagination({
					rowsPerPage:pageSize,
					currentPage:pageNo,
					totalPages: total % pageSize == 0 ? total / pageSize : parseInt(total / pageSize) + 1,
					totalRows:total,

					visiblePageLinks:5,

					showGoToPage:true,
					showRowsPerPage:true,
					showRowsInfo:true,

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

	
	
	<div>
		<div style="position: relative; left: 10px; top: -10px;">
			<div class="page-header">
				<h3>交易列表</h3>
			</div>
		</div>
	</div>
	
	<div style="position: relative; top: -20px; left: 0px; width: 100%; height: 100%;">
	
		<div style="width: 100%; position: absolute;top: 5px; left: 10px;">
		
			<div class="btn-toolbar" role="toolbar" style="height: 80px;">
				<form class="form-inline" role="form" style="position: relative;top: 8%; left: 5px;">
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">所有者</div>
				      <input class="form-control" type="text" id="search-owner">
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">名称</div>
				      <input class="form-control" type="text" id="search-name">
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">客户名称</div>
				      <input class="form-control" type="text" id="search-customerName">
				    </div>
				  </div>
				  
				  <br>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">阶段</div>
					  <select class="form-control" id="search-stage">
					  	<option></option>
						  <c:forEach items="${stageList}" var="stage">
							  <option value="${stage.id}">${stage.value}</option>
						  </c:forEach>
					  </select>
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">类型</div>
					  <select class="form-control" id="search-type">
					  	<option></option>
					  	<c:forEach items="${typeList}" var="type">
							<option value="${type.id}">${type.value}</option>
						</c:forEach>
					  </select>
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">来源</div>
				      <select class="form-control" id="search-source">
						  <option></option>
						  <c:forEach items="${sourceList}" var="source">
							  <option value="${source.id}">${source.value}</option>
						  </c:forEach>
						</select>
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">联系人名称</div>
				      <input class="form-control" type="text" id="search-contactName">
				    </div>
				  </div>
				  
				  <button type="button" class="btn btn-default" id="searchBtn">查询</button>
				  
				</form>
			</div>
			<div class="btn-toolbar" role="toolbar" style="background-color: #F7F7F7; height: 50px; position: relative;top: 10px;">
				<div class="btn-group" style="position: relative; top: 18%;">
				  <button type="button" class="btn btn-primary" onclick="window.location.href='workbench/transaction/toSave.do';"><span class="glyphicon glyphicon-plus"></span> 创建</button>
				  <button type="button" class="btn btn-default" onclick="window.location.href='workbench/transaction/toEdit.do';"><span class="glyphicon glyphicon-pencil"></span> 修改</button>
				  <button type="button" class="btn btn-danger"><span class="glyphicon glyphicon-minus"></span> 删除</button>
				</div>
				
				
			</div>
			<div style="position: relative;top: 10px;">
				<table class="table table-hover">
					<thead>
						<tr style="color: #B3B3B3;">
							<td><input type="checkbox" /></td>
							<td>名称</td>
							<td>客户名称</td>
							<td>阶段</td>
							<td>类型</td>
							<td>所有者</td>
							<td>来源</td>
							<td>联系人名称</td>
						</tr>
					</thead>
					<tbody id="pageListBody">

					</tbody>
				</table>
			</div>
			<div id="changePageDiv"></div>

			</div>
			
		</div>
		
	</div>
</body>
</html>