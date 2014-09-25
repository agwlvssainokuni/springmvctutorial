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
<s:nestedPath path="todo">
	<div class="form-group">
		<f:label path="dueDate">期日</f:label>
		<f:input path="dueDate" cssClass="form-control" readonly="true" />
	</div>
	<div class="form-group">
		<f:label path="description">内容</f:label>
		<f:textarea path="description" cssClass="form-control" readonly="true" />
	</div>
	<div class="form-group">
		<f:label path="id">TODO番号</f:label>
		<f:input path="id" cssClass="form-control" readonly="true" />
	</div>
	<div class="form-group">
		<f:label path="postedAt">登録日時</f:label>
		<f:input path="postedAt" cssClass="form-control" readonly="true" />
	</div>
</s:nestedPath>
