<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
String basePath = request.getScheme() + "://" +
request.getServerName() + ":" +
request.getServerPort() +
request.getContextPath() + "/";
%>
<!DOCTYPE html>
<html>
<head>
    <base href="<%=basePath%>" />
    <meta charset="UTF-8">
    <link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
    <link href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" type="text/css" rel="stylesheet" />

    <script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
    <script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
    <script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
    <script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>

    <link rel="stylesheet" type="text/css" href="jquery/bs_pagination/jquery.bs_pagination.min.css">
    <script type="text/javascript" src="jquery/bs_pagination/jquery.bs_pagination.min.js"></script>
    <script type="text/javascript" src="jquery/bs_pagination/en.js"></script>

    <script type="text/javascript">

	$(function(){

        $(".time").datetimepicker({
            minView:"month",
            language:"zh-CN",
            format:"yyyy-mm-dd",
            autoclose:true,
            todayBtn:true,
            pickerPosition:"bottom-left"
        });

        $("#addBtn").click(function () {

            // 先走后台，获取所有所有者的信息并且铺在下拉条中：使用ajax
            $.ajax({
                url:"Activity/getUserList.do",
                type:"get",
                dataType:"json",
                success:function (resp) {
                    $("#create-marketActivityOwner").empty();
                    $.each(resp,function (i,n) {
                        var sonOwner = "<option value='"+ n.id +"'>"+ n.name +"</option>"
                        $("#create-marketActivityOwner").append(sonOwner)
                    })
                    // 模态窗口打开后所有者默认选中登陆人
                    $("#create-marketActivityOwner").val("${sessionScope.user.id}");
                },

            })
            /*操作模态窗口：找到模态窗口的jquery对象，调用modal方法，该方法为传递参数show:打开  hide:关闭*/
            $("#createActivityModal").modal("show");
        })

        /*为保存按钮添加事件*/
        $("#saveBtn").click(function () {

            if($("#create-startTime").val() > $("#create-endTime").val()){
                alert("日期错误");
                return;
            }

            $.ajax({
                url:"Activity/save.do",
                data:{
                    "owner":       $.trim($("#create-marketActivityOwner").val()),
                    "name":        $.trim($("#create-name").val()),
                    "startDate":   $.trim($("#create-startTime").val()),
                    "endDate":     $.trim($("#create-endTime").val()),
                    "cost":        $.trim($("#create-cost").val()),
                    "description": $.trim($("#create-describe").val())
                },
                dataType:"json",
                type:"post",
                success:function (resp) {
                    // resp: {"success":true/false}
                    if(resp.success){
                        // 刷新市场列表
                        listActivity(1,4);
                        // 清空添加操作模态窗口中的数据
                        $("#activityAddForm")[0].reset();
                        // 关闭添加操作的模态窗口
                        $("#createActivityModal").modal("hide");
                    }else{
                        alert("市场添加操作失败");
                    }
                }
            })

            listActivity(1,4);
        })

        listActivity(1,4);

        // 点击查询按钮
        $("#searchBtn").click(function () {

            $("#hidden-name").val($.trim($("#search-name").val()));
            $("#hidden-owner").val($.trim($("#search-owner").val()));
            $("#hidden-startDate").val($.trim($("#search-startDate").val()));
            $("#hidden-endDate").val($.trim($("#search-endDate").val()));

            listActivity(1,4);
        })

        // 全选框操作
        $("#allCheck").click(function () {$("input[name=cb]").prop("checked",this.checked);})
        $("#activityTableBody").on("click",$("input[name=cb]"),function () {
            // 选中状态的复选框数量和复选框总数量相等，则将全选框选中，反之将其取消选中
            $("#allCheck").prop("checked",$("input[name=cb]").length==$("input[name=cb]:checked").length)
        })

        //删除按钮绑定事件，删除市场活动，难点：批量删除；关联的市场活动备注表删除，一对多
        $("#deleteBtn").click(function () {
            var cbList = $("input[name=cb]:checked");
            if(cbList.length==0) {
                alert("请选择需要删除的记录");
            }else{

                if(confirm("确定删除选中的记录吗？")){
                    // url:/delete.do
                    var param = "";
                    for(var i = 0; i<cbList.length ; i++){
                        param += ("id="+$(cbList[i]).val());
                        param += (i<cbList.length-1 ? "&" : "");
                    }
                    $.ajax({
                        url:"Activity/delete.do",
                        data:param,
                        type:"get",
                        dataType:"json",
                        success:function (resp) {
                            // 返回boolean值
                            if(resp.success){
                                listActivity(1,4);
                            }else{
                                alert("删除市场活动失败"); // 最好给个异常
                            }
                        }
                    })
                }
            }
        })

        // 绑定修改按钮操作
        $("#modifyBtn").click(function () {
            var cbList = $("input[name=cb]:checked");
            if(cbList.length == 0){
                alert("请选中修改项");
            }else if(cbList.length > 1){
                alert("一次只能修改一项");
            }else{
                var id = cbList[0].value;

                // 填充活动详细信息
                var owner_act = "";
                $.ajax({
                    /*这个方法返回的Activity对象的owner属性是用户的名字,
                    id属性是用户的ID而非活动的ID*/
                    url:"Activity/searchOne.do",
                    type:"get",
                    data:{"id":id},
                    dataType:"json",
                    success:function (resp) {
                        $("#edit-name").val(resp.name);
                        $("#edit-startTime").val(resp.startDate);
                        $("#edit-endTime").val(resp.endDate);
                        $("#edit-cost").val(resp.cost);
                        $("#edit-describe").val(resp.description);
                        owner_act = resp.id;// 用户id
                    }
                })

                // 先走后台，获取所有所有者的信息并且铺在下拉条中：使用ajax
                $.ajax({
                    url:"Activity/getUserList.do",
                    type:"get",
                    dataType:"json",
                    success:function (resp) {
                        $("#edit-marketActivityOwner").empty();
                        $.each(resp,function (i,n) {
                            var sonOwner = "<option value='"+ n.id +"'>"+ n.name +"</option>"
                            $("#edit-marketActivityOwner").append(sonOwner)
                        })
                        $("#edit-marketActivityOwner").val(owner_act); //"{sessionScope.user.id}"
                    },
                })
                // 打开模态窗口
                $("#editActivityModal").modal("show");
            }
        })

        /*为更新按钮添加事件*/
        $("#edit-saveBtn").click(function () {
            // 获取id
            var cbList = $("input[name=cb]:checked");
            var id = cbList[0].value;

            // 检查日期
            if($("#edit-startTime").val() > $("#edit-endTime").val()){
                alert("日期错误");
                return;
            }

            // 发送更新数据
            $.ajax({
                url:"Activity/update.do",
                data:{
                    "id":          id,
                    "owner":       $.trim($("#edit-marketActivityOwner").val()),
                    "name":        $.trim($("#edit-name").val()),
                    "startDate":   $.trim($("#edit-startTime").val()),
                    "endDate":     $.trim($("#edit-endTime").val()),
                    "cost":        $.trim($("#edit-cost").val()),
                    "description": $.trim($("#edit-describe").val())
                },
                dataType:"json",
                type:"post",
                success:function (resp) {
                    // resp: {"success":true/false}
                    if(resp.success){
                        // 弹出修改信息
                        alert("修改成功！");
                        // 刷新市场列表
                        listActivity(1,4);
                        // 清空添加操作模态窗口中的数据
                        $("#activityAddForm")[0].reset();
                        // 关闭添加操作的模态窗口
                        $("#editActivityModal").modal("hide");
                    }else if(null==resp.success){  // 其实这里只需要在前端判断一下数据是否有修改就行了，不需要走后台
                        $("#editActivityModal").modal("hide");
                    }else{
                        alert("市场信息修改失败！");
                    }
                }
            })
            listActivity(1,4);
        })

	});

	// 展现活动列表的方法，需要参数：每页几条数据，以及页码.页面加载完毕后进入到第一页
	function listActivity(index,pageSize) {

	    // 将全选框的勾去掉
        $("#allCheck").prop("checked",false);

        $("#search-name").val($.trim($("#hidden-name").val()));
        $("#search-owner").val($.trim($("#hidden-owner").val()));
        $("#search-startDate").val($.trim($("#hidden-startDate").val()));
        $("#search-endDate").val($.trim($("#hidden-endDate").val()));

        $.ajax({
            url:"Activity/pageSearch.do",
            data:{
                "index":index,
                "pageSize":pageSize,
                "name":$.trim($("#search-name").val()),    // 条件查询
                "owner":$.trim($("#search-owner").val()),    // 条件查询
                "startDate":$.trim($("#search-startDate").val()),    // 条件查询
                "endDate":$.trim($("#search-endDate").val())    // 条件查询
            },
            type:"get",
            dataType:"json",
            success:function (resp) { // resp是多个Activity转换成的json的数组
                $("#activityTableBody").empty();
                $.each(resp.dataList,function (i,n) {    // n是一个Activity转换成的json
                    $("#activityTableBody").append("<tr class='active'>" +
                        "<td><input type='checkbox' name ='cb' value="+n.id+"></td>" +
                        "<td><a style='text-decoration: none; cursor: pointer;' onclick=\"window.location.href='Activity/detail.do?id="+n.id+"' \">"+n.name+"</a></td>" +
                        "<td>"+n.owner+"</td>" +
                        "<td>"+n.startDate+"</td>" +
                        "<td>"+n.endDate+"</td>" +
                        "</tr>");
                })

                // 计算总页数
                var totalPages = Math.ceil(resp.total / pageSize);

                //分页插件
                $("#activityPage").bs_pagination({
                    currentPage: index, // 页码
                    rowsPerPage: pageSize, // 每页显示的记录条数
                    maxRowsPerPage: 20, // 每页最多显示的记录条数
                    totalPages: totalPages, // 总页数
                    totalRows: resp.total, // 总记录条数

                    visiblePageLinks: 3, // 显示几个卡片

                    showGoToPage: true,
                    showRowsPerPage: true,
                    showRowsInfo: true,
                    showRowsDefaultInfo: true,

                    onChangePage : function(event, resp){
                        listActivity(resp.currentPage , resp.rowsPerPage);
                    }
                })
            }
        })
    }
	
</script>
</head>
<body>

    <input type="hidden" id="hidden-name">
    <input type="hidden" id="hidden-owner">
    <input type="hidden" id="hidden-startDate">
    <input type="hidden" id="hidden-endDate">

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
				
					<form id="activityAddForm" class="form-horizontal" role="form">
					
						<div class="form-group">
							<label for="create-marketActivityOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="create-marketActivityOwner">
                                </select>
							</div>
                            <label for="create-name" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-name">
                            </div>
						</div>
						
						<div class="form-group">
							<label for="create-startTime" class="col-sm-2 control-label">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="create-startTime" readonly/>
							</div>
							<label for="create-endTime" class="col-sm-2 control-label">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="create-endTime" readonly/>
							</div>
						</div>
                        <div class="form-group">

                            <label for="create-cost" class="col-sm-2 control-label">成本</label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-cost">
                            </div>
                        </div>
						<div class="form-group">
							<label for="create-describe" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="create-describe"></textarea>
							</div>
						</div>
						
					</form>
					
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="saveBtn">保存</button>
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
					
						<div class="form-group">
							<label for="edit-marketActivityOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="edit-marketActivityOwner">
								</select>
							</div>
                            <label for="edit-name" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="edit-name">
                            </div>
						</div>

						<div class="form-group">
							<label for="edit-startTime" class="col-sm-2 control-label">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="edit-startTime" readonly>
							</div>
							<label for="edit-endTime" class="col-sm-2 control-label">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="edit-endTime" readonly>
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-cost" class="col-sm-2 control-label">成本</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-cost">
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-describe" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="edit-describe"></textarea>
							</div>
						</div>
						
					</form>
					
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="edit-saveBtn">更新</button>
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
					  <input class="form-control time" type="text" id="startTime" id="search-startDate"/>
				    </div>
				  </div>
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">结束日期</div>
					  <input class="form-control time" type="text" id="endTime" id="search-endDate">
				    </div>
				  </div>
				  
				  <button type="button" class="btn btn-default" id="searchBtn">查询</button>
				  
				</form>
			</div>
			<div class="btn-toolbar" role="toolbar" style="background-color: #F7F7F7; height: 50px; position: relative;top: 5px;">
				<div class="btn-group" style="position: relative; top: 18%;">
				  <button type="button" class="btn btn-primary" id="addBtn"><span class="glyphicon glyphicon-plus"></span> 创建</button>
				  <button type="button" class="btn btn-default" <%--data-toggle="modal"--%> data-target="#editActivityModal" id="modifyBtn"><span class="glyphicon glyphicon-pencil"></span> 修改</button>
				  <button type="button" class="btn btn-danger" id="deleteBtn"><span class="glyphicon glyphicon-minus"></span> 删除</button>
				</div>
				
			</div>
			<div style="position: relative;top: 10px;">
				<table class="table table-hover">
					<thead>
						<tr style="color: #B3B3B3;">
							<td><input type="checkbox" id="allCheck"/></td>
							<td>名称</td>
                            <td>所有者</td>
							<td>开始日期</td>
							<td>结束日期</td>
						</tr>
					</thead>
					<tbody id="activityTableBody">
					</tbody>
				</table>
			</div>

			<div style="height: 50px; position: relative;top: 30px;">
                <div id="activityPage"></div>
			</div>
			
		</div>
		
	</div>
</body>
</html>