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
<h2>TODO登録</h2>
<f:form servletRelativeAction="/secure/todo/create/confirm"
	method="POST" modelAttribute="todoCreateForm" role="form">
	<div class="form-group">
		<label for="dueDate">期日</label>
		<f:input path="dueDate" cssClass="form-control"
			cssErrorClass="form-control has-error" />
	</div>
	<div class="form-group">
		<label for="description">内容</label>
		<f:textarea path="description" cssClass="form-control"
			cssErrorClass="form-control has-error" />
	</div>
	<f:button type="submit" class="btn btn-default">確認</f:button>
</f:form>
