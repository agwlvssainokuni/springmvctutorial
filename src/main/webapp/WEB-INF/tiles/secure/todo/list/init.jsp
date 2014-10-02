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
<%@ taglib prefix="common" uri="urn:springapp:common"%>
<h2>TODO検索</h2>
<s:hasBindErrors name="todoListForm">
	<div class="form-group has-error">
		<div class="help-block bg-danger">
			<f:errors path="todoListForm" element="div" />
			<s:nestedPath path="todoListForm">
				<f:errors path="postedFrom" element="div" />
				<f:errors path="postedTo" element="div" />
				<f:errors path="dueDateFrom" element="div" />
				<f:errors path="dueDateTo" element="div" />
				<f:errors path="done" element="div" />
				<f:errors path="notDone" element="div" />
				<f:errors path="orderBy" element="div" />
				<f:errors path="orderDir" element="div" />
			</s:nestedPath>
		</div>
	</div>
</s:hasBindErrors>
<f:form servletRelativeAction="/secure/todo/list/execute" method="POST"
	modelAttribute="todoListForm" role="form">
	<div class="form-group">
		<f:label path="postedFrom" cssErrorClass="has-error">登録日時(FROM)</f:label>
		<f:input path="postedFrom" cssClass="form-control"
			cssErrorClass="form-control has-error" />
	</div>
	<div class="form-group">
		<f:label path="postedTo" cssErrorClass="has-error">登録日時(TO)</f:label>
		<f:input path="postedTo" cssClass="form-control"
			cssErrorClass="form-control has-error" />
	</div>
	<div class="form-group">
		<f:label path="dueDateFrom" cssErrorClass="has-error">期日(FROM)</f:label>
		<f:input path="dueDateFrom" cssClass="form-control"
			cssErrorClass="form-control has-error" />
	</div>
	<div class="form-group">
		<f:label path="dueDateTo" cssErrorClass="has-error">期日(TO)</f:label>
		<f:input path="dueDateTo" cssClass="form-control"
			cssErrorClass="form-control has-error" />
	</div>
	<div class="form-group">
		<f:checkbox path="notDone" label="未完了" cssClass="form-control"
			cssErrorClass="form-control has-error" />
		<f:checkbox path="done" label="完了" cssClass="form-control"
			cssErrorClass="form-control has-error" />
	</div>
	<div class="form-group">
		<f:label path="orderBy" cssErrorClass="has-error">ソート列</f:label>
		<f:select path="orderBy" cssClass="form-control"
			cssErrorClass="form-control has-error">
			<c:set var="orderByList"
				value="${common:getLabeledEnumList('cherry.spring.tutorial.web.secure.todo.OrderBy')}" />
			<f:options itemValue="enumName" itemLabel="enumLabel"
				items="${orderByList}" />
		</f:select>
	</div>
	<div class="form-group">
		<f:label path="orderDir" cssErrorClass="has-error">ソート順</f:label>
		<c:set var="orderDirList"
			value="${common:getLabeledEnumList('cherry.spring.tutorial.web.secure.todo.OrderDir')}" />
		<f:radiobuttons path="orderDir" itemValue="enumName"
			itemLabel="enumLabel" items="${orderDirList}"
			cssErrorClass="has-error" />
	</div>
	<f:hidden path="pageNo" />
	<f:hidden path="pageSz" />
	<f:button type="submit" class="btn btn-default">検索</f:button>
</f:form>
<f:form servletRelativeAction="/secure/todo/list/execute" method="POST"
	modelAttribute="todoListForm" id="todoListFormHidden">
	<f:hidden path="postedFrom" id="postedFromHidden" />
	<f:hidden path="postedTo" id="postedToHidden" />
	<f:hidden path="dueDateFrom" id="dueDateFromHidden" />
	<f:hidden path="dueDateTo" id="dueDateToHidden" />
	<f:hidden path="notDone" id="notDoneHidden" />
	<f:hidden path="done" id="doneHidden" />
	<f:hidden path="orderBy" id="orderByHidden" />
	<f:hidden path="orderDir" id="orderDirHidden" />
	<f:hidden path="pageNo" id="pageNoHidden" />
	<f:hidden path="pageSz" id="pageSzHidden" />
</f:form>
