<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>  
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link href="${ctx}/static/ext-6.0.0/build/classic/theme-crisp/resources/theme-crisp-all.css" rel="stylesheet" />
<link href="${ctx}/static/css/hover-effects.css" rel="stylesheet" />
<link href="${ctx}/static/css/jquery.page.css" rel="stylesheet" />
<link href="${ctx}/static/css/bigPic.css" rel="stylesheet" />
<link href="${ctx}/static/bootstrap/bootstrap.min.css" rel="stylesheet" />
<link href="${ctx}/static/cropper/cropper.min.css" rel="stylesheet" />
<link href="${ctx}/static/css/main.css" rel="stylesheet" />
<script type="text/javascript">
$ctx = '${ctx}';
$userNickName='<shiro:principal property="nickName"/>';
$userName='<shiro:principal property="userName"/>';
//auditorFlag='<shiro:hasRole name="auditor">true</shiro:hasRole>'; 
var menuPermissions={};
menuPermissions['menu0']='<shiro:hasPermission name="menu:view:0">true</shiro:hasPermission>';
menuPermissions['menu1']='<shiro:hasPermission name="menu:view:1">true</shiro:hasPermission>';
menuPermissions['menu2']='<shiro:hasPermission name="menu:view:2">true</shiro:hasPermission>';
menuPermissions['menu3']='<shiro:hasPermission name="menu:view:3">true</shiro:hasPermission>';
menuPermissions['menu4']='<shiro:hasPermission name="menu:view:4">true</shiro:hasPermission>';
menuPermissions['menu5']='<shiro:hasPermission name="menu:view:5">true</shiro:hasPermission>';
menuPermissions['menu6']='<shiro:hasPermission name="menu:view:6">true</shiro:hasPermission>';
menuPermissions['menu7']='<shiro:hasPermission name="menu:view:7">true</shiro:hasPermission>';
menuPermissions['menu8']='<shiro:hasPermission name="menu:view:8">true</shiro:hasPermission>';
menuPermissions['menu9']='<shiro:hasPermission name="menu:view:9">true</shiro:hasPermission>';

menuPermissions['menu100']='<shiro:hasPermission name="menu:view:100">true</shiro:hasPermission>';
menuPermissions['menu101']='<shiro:hasPermission name="menu:view:101">true</shiro:hasPermission>';
menuPermissions['menu102']='<shiro:hasPermission name="menu:view:102">true</shiro:hasPermission>';

menuPermissions['menu200']='<shiro:hasPermission name="menu:view:200">true</shiro:hasPermission>';
menuPermissions['menu201']='<shiro:hasPermission name="menu:view:201">true</shiro:hasPermission>';
menuPermissions['menu202']='<shiro:hasPermission name="menu:view:202">true</shiro:hasPermission>';
menuPermissions['menu203']='<shiro:hasPermission name="menu:view:203">true</shiro:hasPermission>';
menuPermissions['menu204']='<shiro:hasPermission name="menu:view:204">true</shiro:hasPermission>';
menuPermissions['menu205']='<shiro:hasPermission name="menu:view:205">true</shiro:hasPermission>';
menuPermissions['menu206']='<shiro:hasPermission name="menu:view:206">true</shiro:hasPermission>';

menuPermissions['menu300']='<shiro:hasPermission name="menu:view:300">true</shiro:hasPermission>';
menuPermissions['menu301']='<shiro:hasPermission name="menu:view:301">true</shiro:hasPermission>';
menuPermissions['menu302']='<shiro:hasPermission name="menu:view:302">true</shiro:hasPermission>';
menuPermissions['menu303']='<shiro:hasPermission name="menu:view:303">true</shiro:hasPermission>';

var productPubPermission=false;
var subjectPubPermission=false;
var systemAuditorPermission=false;
var categoryDownloadPermission=false;
if('admin'==$userName){
	productPubPermission=true;
	subjectPubPermission=true;
	systemAuditorPermission=true;
	categoryDownloadPermission=true;
	
}else{
	productPubPermission='<shiro:hasPermission name="product:pub">true</shiro:hasPermission>';
	subjectPubPermission='<shiro:hasPermission name="subject:pub">true</shiro:hasPermission>';
	systemAuditorPermission='<shiro:hasPermission name="system:auditor">true</shiro:hasPermission>';
	categoryDownloadPermission='<shiro:hasPermission name="category:download">true</shiro:hasPermission>';

}


</script>


<style type="text/css">


/*替换左侧鼠标划入颜色 */
#westMenuMain .x-btn-default-small-focus{
	background:#ccc!important;
		
}

#westMenuMain .x-btn-over{
	background:#ccc!important;
}

.smallPic{
max-width:300px;_width:expression(this.width > 300 ? "300px" : this.width);
max-height:300px;_height:expression(this.height > 300 ? "300px" : this.height);
}

/* .itemDetail{
max-width:300px;_width:expression(this.width > 300 ? "300px" : this.width);
} */
.itemDetail p{
text-indent:2em
}


.uploadImage {
    position: relative;
    display: inline-block;
    background: #D0EEFF;
    border: 1px solid #99D3F5;
    border-radius: 4px;
    padding: 4px 12px;
    overflow: hidden;
    color: #1E88C7;
    text-decoration: none;
    text-indent: 0;
    line-height: 20px;
}
.uploadImage input {
    position: absolute;
    font-size: 100px;
    right: 0;
    top: 0;
    opacity: 0;
}
.uploadImage:hover {
    background: #AADFFD;
    border-color: #78C3F3;
    color: #004974;
    text-decoration: none;
}
</style>
<%-- <script src="${ctx}/static/jquery/jquery-1.9.1.min.js"></script> --%>

<script src="http://apps.bdimg.com/libs/jquery/2.1.1/jquery.min.js"></script>
<script src="http://vjs.zencdn.net/ie8/1.1.2/videojs-ie8.min.js"></script>

<script type="text/javascript" src="${ctx}/static/bootstrap/bootstrap.min.js"></script>
<script type="text/javascript" src="${ctx}/static/cropper/cropper.min.js"></script>
<script src="${ctx}/static/echarts/echarts.js"></script>
<script src="${ctx}/static/ext-6.0.0/build/ext-all.js"></script>
<script type="text/javascript" src="${ctx }/static/ux/TreePicker.js"></script>
<script type="text/javascript" src="${ctx }/static/plugin/picHover.js"></script>
<script type="text/javascript" src="${ctx }/static/plugin/jquey-bigic.js"></script>
<script type="text/javascript" src="${ctx }/static/plugin/jquery.page.js"></script>
<script src="${ctx}/static/ext-6.0.0/build/classic/locale/locale-zh_CN.js"></script>
<script src="${ctx}/static/ext-6.0.0/build/classic/theme-crisp/theme-crisp.js"></script>
<script type="text/javascript" src="${ctx }/static/js/commonUtil.js"></script>
<script type="text/javascript" src="${ctx }/static/js/common.js"></script>
<script type="text/javascript" src="${ctx }/static/js/item.js"></script>
<script type="text/javascript" src="${ctx }/static/js/pic.js"></script>
<script type="text/javascript" src="${ctx }/static/js/product.js"></script>
<script type="text/javascript" src="${ctx }/static/js/book.js"></script>
<script type="text/javascript" src="${ctx }/static/js/video.js"></script>
<script type="text/javascript" src="${ctx }/static/js/subject.js"></script>
<script type="text/javascript" src="${ctx }/static/js/magazine.js"></script>
<script type="text/javascript" src="${ctx }/static/js/chapter.js"></script>
<script type="text/javascript" src="${ctx }/static/js/case.js"></script>
<script type="text/javascript" src="${ctx }/static/js/user.js"></script>
<script type="text/javascript" src="${ctx }/static/js/role.js"></script>
<script type="text/javascript" src="${ctx }/static/js/category.js"></script>
<script type="text/javascript" src="${ctx }/static/js/group.js"></script>
<script type="text/javascript" src="${ctx }/static/js/statistics.js"></script>
<script type="text/javascript" src="${ctx }/static/js/interface.js"></script>
 <script type="text/javascript" src="${ctx }/static/js/permission.js"></script>
 <script type="text/javascript" src="${ctx }/static/js/logs.js"></script>
 <script type="text/javascript" src="${ctx }/static/js/indexWords.js"></script>
 <script type="text/javascript" src="${ctx }/static/js/sysParameter.js"></script>
 <script type="text/javascript" src="${ctx }/static/js/keyword.js"></script>
  <script type="text/javascript" src="${ctx }/static/js/standard.js"></script>
 <script type="text/javascript" src="${ctx }/static/js/main.js"></script>
  <script type="text/javascript" src="${ctx }/static/js/main2.js"></script>
 

<title>机械工业出版社</title>
</head>
<body>
<div id="mainPanel"></div>


 <button id="uploadPic2" style="display:none"></button>
 <div class="container" id="crop-avatar">
<!-- Cropping modal -->
    <div class="modal fade" id="avatar-modal" aria-hidden="true" aria-labelledby="avatar-modal-label" role="dialog" tabindex="-1">
      <div class="modal-dialog modal-lg">
        <div class="modal-content">
          <form class="avatar-form" action="photo/add" enctype="multipart/form-data" method="post">
            <div class="modal-header">
              <button type="button" class="close" data-dismiss="modal">&times;</button>
              <h4 class="modal-title" id="avatar-modal-label">图片信息</h4>
            </div>
            <div class="modal-body">
              <div class="avatar-body">

                <!-- Upload image and data -->
                <div class="avatar-upload">
                  <input type="hidden" class="avatar-src" name="avatar_src">
                  <input type="hidden" class="avatar-data" name="avatar_data">
                  <input type="text" style="display:none"  name="cid" value="3">
                  
                  <label for="avatarInput" style="line-height:20px">图片</label>
                  <a href="javascript:;" class="uploadImage">选择图片<input accept="image/png,image/gif,image/jpeg"  type="file" class="avatar-input" id="avatarInput" name="image"></a>
                  <label style="margin-top:10px;">标题</label>
                  <input style="margin-top:10px;margin-left:0" class="avatar-input" name="title">
                  <label style="margin-top:10px">原图 </label>
                     <input style="display:inline;margin-left:10px;margin-top:10px;margin-left:0" name="originalFlag" type="radio" value="0" checked="checked"/>是
                   <input style="display:inline;margin-left:50px" name="originalFlag" type="radio" value="1" />否
                </div>

                <!-- Crop and preview -->
                <div class="row">
                  <div class="col-md-9">
                    <div class="avatar-wrapper"></div>
                  </div>
                  <div class="col-md-3">
                    <div class="avatar-preview preview-lg"></div>
                    <div class="avatar-preview preview-md"></div>
                    <div class="avatar-preview preview-sm"></div>
                  </div>
                </div>

                <div class="row avatar-btns">
                  <div class="col-md-9">
                    <div class="btn-group">
                      <button type="button" class="btn btn-primary" data-method="rotate" data-option="-90" title="Rotate -90 degrees">逆时针旋转</button>
                     
                    </div>
                    <div class="btn-group">
                      <button type="button" class="btn btn-primary" data-method="rotate" data-option="90" title="Rotate 90 degrees">顺时针旋转</button>
                     
                    </div>
                  </div>
                  <div class="col-md-3">
                    <button type="submit"  class="btn btn-primary btn-block avatar-save">保存</button>
                  </div>
                </div>
              </div>
            </div>
            <!-- <div class="modal-footer">
              <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
            </div> -->
          </form>
        </div>
      </div>
    </div><!-- /.modal -->
    
    <!-- Loading state -->
    <div class="loading" aria-label="Loading" role="img" tabindex="-1"></div>
  </div>

</body>
<script type="text/javascript">
	//$(".full-length").myPlugin();
</script>
</html>