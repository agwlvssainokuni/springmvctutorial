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
<%@ taglib prefix="mytag" tagdir="/WEB-INF/tags"%>
<h2>TODO編集</h2>
<s:nestedPath path="todoEditForm">
	<div class="form-group">
		<f:label path="dueDate">期日</f:label>
		<f:input path="dueDate" cssClass="form-control" readonly="true" />
	</div>
	<div class="form-group">
		<f:checkbox path="doneFlg" disabled="true" label="完了" />
	</div>
	<div class="form-group">
		<f:label path="description">内容</f:label>
		<f:textarea path="description" cssClass="form-control" readonly="true" />
	</div>
</s:nestedPath>
<f:form servletRelativeAction="/secure/todo/${id}/execute" method="POST"
	modelAttribute="todoEditForm" id="todoEditFormHidden">
	<f:hidden path="dueDate" id="dueDateHidden" />
	<f:hidden path="doneFlg" id="doneFlgHidden" />
	<f:hidden path="description" id="descriptionHidden" />
	<f:hidden path="lockVersion" id="lockVersionHidden" />
	<f:button type="submit" class="btn btn-default">変更</f:button>
	<mytag:onetimetoken />
</f:form>
