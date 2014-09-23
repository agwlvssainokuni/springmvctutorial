<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" trimDirectiveWhitespaces="true" session="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<meta name="viewport" content="width=device-width, initial-scale=1" />
<title>Sprint MVCチュートリアル</title>
<link rel="stylesheet" media="screen"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap.min.css" />
<link rel="stylesheet" media="screen"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap-theme.min.css">
<link rel="stylesheet" media="screen"
	href="<c:url value="/style/general.css"/>" />
<script type="text/javascript" src="//code.jquery.com/jquery-1.11.0.js"></script>
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/js/bootstrap.min.js"></script>
<script type="text/javascript"
	src="<c:url value="/script/general.js" />"></script>
</head>
<body>
	<div class="container">
		<div class="nav navbar-header">
			<div class="navbar-brand">Spring MVCチュートリアル</div>
		</div>
	</div>
	<div class="container" role="main">
		<p>HTTPステータスコード 404: 指定されたリソースが存在しません。</p>
	</div>
</body>
</html>
