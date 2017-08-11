<%--
  Created by IntelliJ IDEA.
  User: mxp
  Date: 2017/7/12
  Time: 18:55
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String serviceRoot=request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
    request.setAttribute("path",serviceRoot);

%>
<!-- Modal -->
<!-- Button trigger modal -->
<button type="button" class="btn btn-primary btn-lg" data-toggle="modal" data-target="#myModal">
    上传附件
</button>

<!-- Modal -->
<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" onsubmit="return upoload()">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title" id="myModalLabel">上传附件</h4>
            </div>
            <form  name="form1"  action="${path}/newbook/uploadfiles" enctype="multipart/form-data" method="post">
                <div class="modal-body">
                    <input type="file" class="file" id="filepath"  name="filepath"><br/>
                    <input type="hidden" name="bookid" value="${book.id}">
                </div>

                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                    <button type="submit"  name="uploadsub" class="btn btn-primary">提交</button>
                </div>
            </form>
        </div>
    </div>
</div>