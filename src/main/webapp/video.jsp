<%@ page language="java" contentType="text/html; charset=utf8"
    pageEncoding="utf8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf8">
<title>机械工业出版社</title>
  <style type="text/css">

		body{

			font:14px/28px "微软雅黑";

		}

		.contact *:focus{outline :none;}

		.contact{

			width: 700px;

			height: auto;

			background: #f6f6f6;

			margin: 40px auto;

			padding: 10px;

		}

		.contact ul {

			width: 650px;

			margin: 0 auto;

		}

		.contact ul li{

			border-bottom: 1px solid #dfdfdf;

			list-style: none;

			padding: 12px;

		}

		.contact ul li label {

			width: 120px;

			display: inline-block;

			float: left;

		}

		.contact ul li input[type=text],.contact ul li input[type=password],.contact ul li textarea{

			width: 220px;

			height: 25px;

			border :1px solid #aaa;

			padding: 3px 8px;

			border-radius: 5px;

		}

		.contact ul li input:focus{

			border-color: #c00;

			

		}

		.contact ul li input[type=text],contact ul li textarea{

			transition: padding .25s;

			-o-transition: padding  .25s;

			-moz-transition: padding  .25s;

			-webkit-transition: padding  .25s;

		}


		.contact ul li input:focus{

			padding-right: 70px;

		}

		.btn{

			position: relative;

			left: 300px;

		}

		.tips{

			color: rgba(0, 0, 0, 0.5);

			padding-left: 10px;

		}

		.tips_true,.tips_false{

			padding-left: 10px;

		}

		.tips_true{

			color: green;

		}

		.tips_false{

			color: red;

		}

  </style>
</head>
<body>

	<div class="contact" >

		<form name="form1" method="post">
		<input name="catid" style="display:none" value=${catid}/>
		<input name="masId" style="display:none" value=${masId}/>
			<ul>

				<li>

					<label>标题：</label>

					<input type="text" name="title" placeholder="请输入标题"  onblur="checkna()" required/><span class="tips" id="divname">长度1~50个字符</span>

				</li>

				<li>

					<label>分类名称：</label>

					<input type="text"  readonly="readonly"/>

				</li>
				


				<li>

					<label>描述：</label>

					<textarea  name="desc"></textarea>

				</li>

			</ul>

			<b class="btn" onClick='submit()'><input type="button"  value="提交"/>

			<input type="reset" value="取消" onclick="window.close()"/></b>

		</form>

	</div>

<script type="text/javascript">
function checkna(){

	na=form1.title.value;

  	if( na.length <1 || na.length >50)  

		{  	

			divname.innerHTML='<font class="tips_false">长度是1~50个字符</font>';

		     return false;

		}else{  

		    divname.innerHTML='<font class="tips_true">输入正确</font>';

		    return true;

		}  

	

}
function submit(){
	alert(1);
	//检查参数
	if(checkna()){
		$.ajax({
			type: "POST",
		   url: "video/save",
		   data: {'catid':form1.catid.value,'title':form1.title.value,'desc':form1.desc.innerHTML,'masId':form1.masId.value},
		   async:false,
		   dataType: 'json',
		   success: function(msg){
			  alert("保存成功")
			  window.close();
			   
		   }
		});
		
	}
	
	
}


</script>
<%--<form action="/item/download">
	<button type="submit">下载</button>
</form>--%>
</body>
</html>