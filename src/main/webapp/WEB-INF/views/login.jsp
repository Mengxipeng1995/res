<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<html>
<head>
<title>机械工业出版社</title>
		<link rel="stylesheet" href="${ctx}/static/css/login/reset.css">
       <%--  <link rel="stylesheet" href="${ctx}/static/css/login/supersized.css"> --%>
        <link rel="stylesheet" href="${ctx}/static/css/login/style.css">
       
</head>
<input type="hidden" value="res_login_jsp_tag_trs_20161114"></input>
<body style="background:url(${ctx}/static/images/login/bgimg.jpg) no-repeat center top;overflow: hidden;">
		<div class="page-container">
            <h1><img src="${ctx}/static/images/login/logo.png" width="343" height="31"></h1>
            <div class="formdiv"></div>
            <form action="${ctx}/login" method="post">
				<div>
					<input type="text" name="userName" class="username" placeholder="用户名" autocomplete="off"/>
				</div>
                <div>
					<input type="password" name="password" class="password" placeholder="密码" oncontextmenu="return false" onpaste="return false" />
                </div>
                <button id="submit" type="submit">登　录</button>
            </form>
            
        </div>
		<div class="alert" style="display:none">
			<h2>消息</h2>
			<div class="alert_con">
				<p id="ts"></p>
				<a><div class="btn" style="width:65px;padding:0px 0px;letter-spacing:8px;margin-left:123px;line-height:25px">确定</div></a>
			</div>
		</div>
		
		
		<script src="${ctx}/static/jquery/jquery-1.9.1.min.js" type="text/javascript"></script>
        <script src="${ctx}/static/js/login/supersized.3.2.7.min.js"></script>
       <%--  <script src="${ctx}/static/js/login/supersized-init.js"></script> --%>
		<script type="text/javascript">
		if('${authticationError}'){
			$("#ts").html("${authticationError}");
			is_show();
		}
		$(".btn").click(function(){
			is_hide();
		})
		var u = $("input[name=userName]");
		var p = $("input[name=password]");
		$("#submit").on('click',function(){
			if(u.val() == '' || p.val() =='')
			{
				$("#ts").html("用户名或密码不能为空~");
				is_show();
				return false;
			}
			/* else{
				var reg = /^[0-9A-Za-z]+$/;
				if(!reg.exec(u.val()))
				{
					$("#ts").html("用户名错误");
					is_show();
					return false;
				}
			} */
		});
		window.onload = function()
		{
			//设置鼠标焦点为用户名输入框
			$("input[name=userName]").focus();
			$(".connect p").eq(0).animate({"left":"0%"}, 600);
			$(".connect p").eq(1).animate({"left":"0%"}, 400);
		}
		function is_hide(){ $(".alert").animate({"top":"-40%"}, 300) }
		function is_show(){ $(".alert").show().animate({"top":"45%"}, 300) }
		</script>
		
</body>
</html>