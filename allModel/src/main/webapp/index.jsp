<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Insert title here</title>
</head>
<body>
<h2>Hello World!</h2>
<form action="showUser">
<input name="userId"/>
<input type="submit" value="submit">  
</form>

<form name="Form" action="springUpload" method="post"  enctype="multipart/form-data">
<h1>使用spring mvc提供的类的方法上传文件</h1>
<input type="file" name="file">
<input type="submit" value="upload"/>
</form>
<h1>使用spring mvc提供的类的方法下载文件</h1>
 <a href="springDownload?fileName=downTest.txt">文件下载</a><br />
 
 <h1>使用POI提供的类的方法下载文件</h1>
 <a href="user/down">文件下载</a><br />
 
 <form name="Form2" action="user/upLoad" method="post"  enctype="multipart/form-data">
<h1>使用poi提供的类的方法上传文件</h1>
<input type="file" name="myfiles">
<input type="submit" value="poi上传"/>
</form>
</body>
</html>
</body>
</html>
