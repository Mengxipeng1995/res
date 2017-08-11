<%--
  Created by IntelliJ IDEA.
  User: mxp
  Date: 2017/8/2
  Time: 16:03
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    String serviceRoot=request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
    request.setAttribute("path",serviceRoot);
%>
<html>
<head>
    <title>Title</title>
    <link rel="stylesheet" href="${path}/static/css/bookdeta.css">
    <link rel="stylesheet" href="${path}/static/Boostrap/css/bootstrap.css">
    <link rel="stylesheet" href="${path}/static/css/book.css">
    <script type="text/javascript" src="${path}/static/Boostrap/js/jquery-2.1.1.js"></script>
    <script type="text/javascript" src="${path}/static/Boostrap/js/bootstrap.min.js"></script>
    <script type="text/javascript" src="${path}/static/js/bookdeta.js"></script>
</head>
<body>




<jsp:include page="include/uploadfile.jsp"></jsp:include>

<%--<form action="${path}/" enctype="multipart/form-data" method="post">--%>
    <%--上传文件1：<input type="file" name="filepath"><br/>--%>
    <%--<input type="submit" value="提交">--%>
<%--</form>--%>



<table  cellspacing="0" cellpadding="0" style="font-weight: normal">
    <tr>
        <td style="width: 106px;height: 40px;"><span>书名:</span></th>
        <td style="width: 300px;height: 40px;"><span>${book.title}</span></td>
        <td style="width: 250px;height: 40px;"><span>作者:</span><span>${book.author}</span></td>

        <td rowspan="4" style="width: 180px;height: 40px">
            <c:forEach items="${attachmen}" var="att">
            <c:if test="${att.typeName == 'COVER'}">
            <img src="${path}/cover/${att.fileName}" style="width: 150px;height: 200px">
            </c:if>
            </c:forEach>
        </td>

    </tr>
    <tr>
        <td style="width: 106px;height: 40px"><span>ISBN:</span></td>
        <td style="width: 250px;height: 40px"><span>${book.isbn}</span></td>
        <td style="width: 250px;height: 40px"><span>发行时间:</span><span>${book.shelvesDate}</span></td>
    </tr>

    <tr>
        <td style="width: 106px;height: 40px"><span>出版单位:</span></td>
        <td colspan="2" style="width: 250px;height: 40px"><span>${book.publish}</span></td>
    </tr>

    <tr>
        <td style="width: 180px;height: 100%;"><span>摘要:</span></td>
        <td colspan="2" style="width: 250px;height: 130px"><span>${book.contentValidity}</span></td>
    </tr>

    <tr>
        <td style="width: 180px;height: 100%;"><span>目录:</span></td>
        <td colspan="3" style="width: 250px;height:100%"><span>${cata}</span></td>
    </tr>

    <tr>
        <td style="width: 106px;height: 40px"><span>推荐语:</span></td>
        <td colspan="3" style="width: 250px;height: 40px"><span>${book.pressRecommendation}</span></td>
    </tr>

    <tr>
        <td style="width: 106px;height: 40px"><span>关键词:</span></td>
        <td colspan="3" style="width: 250px;height: 40px"><span>${book.keySentences}</span></td>
    </tr>

    <tr>
        <td style="width: 106px;height: 40px"><span>印次:</span></td>
        <td  style="width: 180px;height: 40px"><span>${book.printEdition}</span></td>
        <td style="width: 106px;height: 40px"><span>版次:</span></td>
        <td  style="width: 180px;height: 40px"><span>${book.revision}</span></td>

    </tr>
    <tr>
        <td style="width: 106px;height: 40px"><span>图书介质:</span></td>
        <td  style="width: 180px;height: 40px"><span>${book.binding}</span></td>
        <td style="width: 106px;height: 40px"><span>库存:</span></td>
        <td  style="width: 180px;height: 40px"></td>
    </tr>

    <tr>
        <td style="width: 106px;height: 40px"><span>价格:</span></td>
        <td  style="width: 180px;height: 40px"><span>${book.price}</span></td>
        <td style="width: 106px;height: 40px"><span>责任编辑:</span></td>
        <td  style="width: 180px;height: 40px"><span>${book.editorCharge}</span></td>
    </tr>


    <tr>
        <td style="width: 106px;height: 80px">PDF下载</td>

        <td  style="width: 106px;height: 80px">

            <c:forEach items="${attachmen}" var="att" varStatus="i">
                <c:if test="${att.typeName == 'PDF'}">
                    <a href="/newbook/uploadPDF?path=${att.filePath}">点击下载pdf_${i.index}</a><br/>
                </c:if>
            </c:forEach>

        </td>

        <td style="width: 106px;height: 80px">XML下载</td>

        <td  style="width: 106px;height: 80px">

            <c:forEach items="${attachmen}" var="att">
                <c:if test="${att.typeName == 'XML'}">
                    <a href="/newbook/uploadPDF?path=${att.filePath}">点击下载XML</a>
                </c:if>
            </c:forEach>

        </td>

    </tr>

    <tr>
        <td style="width: 106px;height: 80px">IMAGE下载</td>

        <td  style="width: 106px;height: 80px">
            <c:set var = "en" value="true"></c:set>
            <c:forEach items="${attachmen}" var="att" >
                <c:if test="${en==true}">
                    <c:if test="${att.typeName == '图片'}">
                        <c:set var="en" value="false"></c:set>
                        <a href="/newbook/downloadZip?imagefile=${att.filePath}">点击下载图片</a><br/>
                    </c:if>
                </c:if>
            </c:forEach>

        </td>

        <td style="width: 106px;height: 80px">EPUB下载</td>

        <td  style="width: 106px;height: 80px">

            <c:forEach items="${attachmen}" var="att">
                <c:if test="${att.typeName == 'EPUB'}">
                    <a href="/newbook/uploadPDF?path=${att.filePath}">点击下载EPUB</a>
                </c:if>
            </c:forEach>

        </td>

    </tr>



</table>
</body>
</html>
