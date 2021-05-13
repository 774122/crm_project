<%@ page import="com.chen.settings.domain.DicValue" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
String basePath = request.getScheme() + "://" + request.getServerName() + ":" +
request.getServerPort() + request.getContextPath() + "/";

/*
* JSP默认内置的九个对象：
*   application、config
*   session
*   request、response
*   pageContext、page
*   exception
*   out
*
* */
String clue_id = request.getParameter("id");
String fullname = request.getParameter("fullname");
String appellation = request.getParameter("appellation");
String company = request.getParameter("company");
String owner = request.getParameter("owner");

%>
<!DOCTYPE html>
<html>
<head>
    <base href="<%=basePath%>" />
    <meta charset="UTF-8">

    <link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
    <script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
    <script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>


    <link href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" type="text/css" rel="stylesheet" />
    <script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
    <script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>

    <script type="text/javascript">
    	$(function(){

            // 日历控件
            $(".time").datetimepicker({
                minView:"month",
                language:"zh-CN",
                format:"yyyy-mm-dd",
                autoclose:true,
                todayBtn:true,
                pickerPosition:"top-left"
            });

    		$("#isCreateTransaction").click(function(){
    			if(this.checked){
    				$("#create-transaction2").show(200);
    			}else{
    				$("#create-transaction2").hide(200);
    			}
    		});

    		// 为放大镜图标绑定事件
    		$("#openSearchModalBtn").click(function () {
                $("#searchActivityModal").modal("show");
            })

            // 为搜索窗口的搜索框绑定按钮
            $("#search").keydown(function (event) {
                if(event.keyCode==13){

                    $.ajax({
                        url:"Clue/getActivityListByName.do",
                        data:{"aname":$.trim($("#search").val())},
                        type:"get",
                        dataType:"json",
                        success:function (resp) {
                            $("#activitySearchBody").empty()
                            $.each(resp, function (i,n) {
                                $("#activitySearchBody").append("<tr>" +
                                    "<td><input type='radio' value='"+n.id+"' onclick='fillActName(\""+n.id+"\",\""+n.name+"\")' name='cb'/></td>" +
                                    "<td>"+n.name+"</td>" +
                                    "<td>"+n.startDate+"</td>" +
                                    "<td>"+n.endDate+"</td>" +
                                    "<td>"+n.owner+"</td>" +
                                    "</tr>")
                            })
                        }
                    })

                    return false;
                }
            })
            
            // 为转换按钮绑定事件
            $("#convertBtn").click(function () {
                // 提交请求到后台，执行线索的转换操作，应该发出传统请求
                // 删除一条线索，增加相应的联系人和交易记录
                // 根据“为客户创建交易”的复选框是否钩选，决定是否创建交易
                if($("#isCreateTransaction").prop("checked")){
                    //alert("创建交易")

                    //window.location.href = "Clue/convert.do?id=xxx&money=xxx&"; // 把线索id传到后台
                    // 提交表单，不用担心请求参数过长的问题，且可以以post方式提交
                    $("#convertForm").submit();

                }else{
                    //alert("不创建")

                    window.location.href = "Clue/convert.do?clueId=${param.id}"; // 把线索id传到后台

                }
            })
    	});

    	// 在转换页面的活动搜索页面，給单条活动记录赋函数：name填充，id(活动)保存到隐藏域
    	function fillActName(id,name) {
             $("#activity").val(name);
             $("#hiddenID").val(id);
             $("#searchActivityModal").modal("hide");
        }

        id
    </script>

</head>
<body>
	
	<!-- 搜索市场活动的模态窗口 -->
	<div class="modal fade" id="searchActivityModal" role="dialog" >
		<div class="modal-dialog" role="document" style="width: 90%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title">搜索市场活动</h4>
				</div>
				<div class="modal-body">
					<div class="btn-group" style="position: relative; top: 18%; left: 8px;">
						<form class="form-inline" role="form">
						  <div class="form-group has-feedback">
						    <input type="text" class="form-control" style="width: 300px;" id="search" placeholder="请输入市场活动名称，支持模糊查询">
						    <span class="glyphicon glyphicon-search form-control-feedback"></span>
						  </div>
						</form>
					</div>
					<table id="activityTable" class="table table-hover" style="width: 900px; position: relative;top: 10px;">
						<thead>
							<tr style="color: #B3B3B3;">
								<td></td>
								<td>名称</td>
								<td>开始日期</td>
								<td>结束日期</td>
								<td>所有者</td>
								<td></td>
							</tr>
						</thead>
						<tbody id="activitySearchBody">
							<%--<tr>
								<td><input type="radio" name="activity"/></td>
								<td>发传单</td>
								<td>2020-10-10</td>
								<td>2020-10-20</td>
								<td>zhangsan</td>
							</tr>
							<tr>
								<td><input type="radio" name="activity"/></td>
								<td>发传单</td>
								<td>2020-10-10</td>
								<td>2020-10-20</td>
								<td>zhangsan</td>
							</tr>--%>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>

	<div id="title" class="page-header" style="position: relative; left: 20px;">
		<h4>转换线索 <%=fullname%><small><%=appellation%>-<%=company%></small></h4>
	</div>
	<div id="create-customer" style="position: relative; left: 40px; height: 35px;">
		新建客户：<%=company%>
	</div>
	<div id="create-contact" style="position: relative; left: 40px; height: 35px;">
		新建联系人：<%=fullname%><%=appellation%>
	</div>
	<div id="create-transaction1" style="position: relative; left: 40px; height: 35px; top: 25px;">
		<input type="checkbox" id="isCreateTransaction"/>
		为客户创建交易
	</div>
	<div id="create-transaction2" style="position: relative; left: 40px; top: 20px; width: 80%; background-color: #F7F7F7; display: none;" >



	        <%--表单形式提交--%>
		<form id="convertForm" action="Clue/convert.do" method="post">

            <input type="hidden" value="${param.id}" name="clueId">

            <input type="hidden" value="a" name="flag"> <%--用于給后台判断是否提交了表单--%>

		  <div class="form-group" style="width: 400px; position: relative; left: 20px;">
		    <label for="amountOfMoney">金额</label>
		    <input type="text" class="form-control" id="amountOfMoney" name="money">
		  </div>
		  <div class="form-group" style="width: 400px;position: relative; left: 20px;">
		    <label for="tradeName">交易名称</label>
		    <input type="text" class="form-control" id="tradeName" name="name">
		  </div>
		  <div class="form-group" style="width: 400px;position: relative; left: 20px;">
		    <label for="expectedClosingDate">预计成交日期</label>
		    <input type="text" class="form-control time" id="expectedClosingDate" name="expectedDate">
		  </div>
		  <div class="form-group" style="width: 400px;position: relative; left: 20px;">
		    <label for="stage">阶段</label>
		    <select id="stage"  class="form-control" name="stage">
		    	<option></option>
                <%
                    for(DicValue s : (List<DicValue>)request.getServletContext().getAttribute("stageList")){
                %>
                <option value="<%=s.getValue()%>"><%=s.getText()%></option>
                <%
                    }
                %>
		    </select>
		  </div>
		  <div class="form-group" style="width: 400px;position: relative; left: 20px;">
		    <label for="activity">市场活动源&nbsp;&nbsp;<a href="javascript:void(0);" id="openSearchModalBtn" style="text-decoration: none;"><span class="glyphicon glyphicon-search"></span></a></label>
		    <input type="text" class="form-control" id="activity" placeholder="点击上面搜索" readonly>
		  </div>
            <input type="hidden" id="hiddenID" name="activityId"><%--用于储存提交的活动id--%>
        </form>
	</div>
	
	<div id="owner" style="position: relative; left: 40px; height: 35px; top: 50px;">
		记录的所有者：<br>
		<b><%=owner%></b>
	</div>
	<div id="operation" style="position: relative; left: 40px; height: 35px; top: 100px;">
		<input class="btn btn-primary" type="button" id="convertBtn" value="转换">
		&nbsp;&nbsp;&nbsp;&nbsp;
        <a href="javascript:history.back(-1)"><input class="btn btn-default" type="button" value="取消"></a>
	</div>
</body>
</html>