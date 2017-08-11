<%--
  Created by IntelliJ IDEA.
  User: mxp
  Date: 2017/8/2
  Time: 15:54
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String serviceRoot=request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
    request.setAttribute("path",serviceRoot);

%>
<html>
<head>
    <title>Title</title>
    <link rel="stylesheet" href="${path}/static/Boostrap/css/bootstrap.css">
    <link rel="stylesheet" href="${path}/static/css/book.css">
    <script type="text/javascript" src="${path}/static/Boostrap/js/jquery-2.1.1.js"></script>
    <script type="text/javascript" src="${path}/static/Boostrap/js/bootstrap.min.js"></script>
</head>
<body>

<nav class="navbar navbar-inverse" >
    <div class="container-fluid" style="background-image: url('/static/images/top3.jpg');background-size: cover;border-radius:0;">
        <div class="navbar-header" >
            <img src="/static/images/top3.jpg">
        </div>
        <div class="collapse navbar-collapse">
            <ul class="nav navbar-nav navbar-right">

            </ul>
        </div>
    </div>
</nav>
<!-- 中间主体内容部分 -->
<div class="pageContainer">
    <!-- 左侧导航栏 -->
    <div style="padding-top: 20px"></div>
    <div class="pageSidebar">
        <ul class="nav nav-stacked nav-pills">
            <li role="presentation" style="background-color: #F5F5F5;height: 35px">
              <span style="line-height: 35px">功能菜单</span>
            </li>
            <li role="presentation" style="background: #3892D4;border-radius: 5px">
                <a href="${path}/newbook/details?id=${id}" target="mainFrame" >图书信息</a>
            </li>
            <%--<li role="presentation" style="background: #3892D4">--%>
                <%--<a href="nav2.html" target="mainFrame">图书章节</a>--%>
            <%--</li>--%>
            <%--<li role="presentation" style="background: #3892D4">--%>
                <%--<a href="nav3.html" target="mainFrame">相关图书</a>--%>
            <%--</li>--%>
            <%--<li class="dropdown">--%>

            <%--</li>--%>
            <%--<li role="presentation">--%>
                <%--<a href="nav4.html" target="mainFrame">扩展属性维护</a>--%>
            <%--</li>--%>
        </ul>
    </div>
    <div style="padding-top: 20px"></div>
    <!-- 左侧导航和正文内容的分隔线 -->
    <div class="splitter" ></div>
    <!-- 正文内容部分 -->
    <div class="pageContent">
        <div style="padding-top: 25px"></div>
        <iframe src="${path}/newbook/details?id=${id}" name="mainFrame"  id="mainFrame" frameborder="0" width="100%" height="100%" frameBorder="0"></iframe>
    </div>
</div>
<!-- 底部页脚部分 -->
<%--<div class="footer">--%>
    <%--<p class="text-center">--%>
        <%--2017 &copy; NeoYang.--%>
    <%--</p>--%>
<%--</div>--%>

</body>
</html>
