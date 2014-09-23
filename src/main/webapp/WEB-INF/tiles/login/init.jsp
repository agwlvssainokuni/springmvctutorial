<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" trimDirectiveWhitespaces="true" session="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="f" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<h2>ログイン</h2>
<c:if test="${loginFailed}">
	<div class="has-error">
		<div class="help-block">ログインに失敗しました。</div>
	</div>
</c:if>
<c:if test="${loggedOut}">
	<div class="has-success">
		<div class="help-block">ログアウトしました。</div>
	</div>
</c:if>
<form id="login" action="<c:url value="/login/req" />" method="POST"
	role="form">
	<div class="form-group">
		<label for="loginId">ログインID</label>
		<input type="text" id="loginId" name="loginId" class="form-control" />
	</div>
	<div class="form-group">
		<label for="password">パスワード</label>
		<input type="password" id="password" name="password" class="form-control" />
	</div>
	<button class="btn btn-default" type="submit">ログイン</button>
</form>
