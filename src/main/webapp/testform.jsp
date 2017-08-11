<%@ page contentType="text/html;charset=UTF-8"%>
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Insert title here</title>
</head>
<body>
<CENTER>
 <form action="/res/test/bookImp" method="post" target="_blank" >
    <table border="1" >
       <tr><td colspan="2">图书入库</td></tr>
       <tr><td width="100">路径</td><td><input name="path" type="text" /></td></tr>
       <tr><td width="100">cid</td><td><input name="cid" type="text" /></td></tr>
       <tr><td width="100">type</td><td><input name="type" type="text" /></td></tr>
       
      <tr><td colspan="2"> <input type="submit" value="提交"></td></tr>
    </table>
   </form>
   
   
   </CENTER>
</body>
</html>