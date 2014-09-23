<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" trimDirectiveWhitespaces="true" session="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8" />
<meta name="viewport"
	content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=5.0, user-scalable=yes" />
<title>Spring MVCチュートリアル</title>
<link rel="stylesheet" media="screen"
	href="<c:url value="/style/general.css"/>" />
<script type="text/javascript" src="//code.jquery.com/jquery-1.11.0.js"></script>
<script type="text/javascript"
	src="<c:url value="/script/general.js" />"></script>
</head>
<body>
	<div>
		<form action="<c:url value="/logout" />" method="POST">
			<button type="submit">ログアウト</button>
			<input type="hidden" name="${_csrf.parameterName}"
				value="${_csrf.token}">
		</form>
	</div>
	<div>
		<tiles:insertAttribute name="content" />
	</div>
</body>
</html>
