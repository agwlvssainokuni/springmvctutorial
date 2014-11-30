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
<%@ taglib prefix="foundation" uri="urn:cherry:foundation"%>
<%@ taglib prefix="mytag" tagdir="/WEB-INF/tags"%>
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
<c:if test="${pagedList != null && pagedList.list.isEmpty()}">
	<div class="has-warning">
		<div class="help-block">条件に該当する項目はありませn。</div>
	</div>
</c:if>
<f:form servletRelativeAction="/secure/todo/list/execute" method="POST"
	modelAttribute="todoListForm" role="form">
	<div class="form-group">
		<f:label path="postedFrom" cssErrorClass="has-error">登録日時</f:label>
		<f:input path="postedFrom" cssErrorClass="has-error" />
		-
		<f:input path="postedTo" cssErrorClass="has-error" />
	</div>
	<div class="form-group">
		<f:label path="dueDateFrom" cssErrorClass="has-error">期日</f:label>
		<f:input path="dueDateFrom" cssErrorClass="has-error" />
		-
		<f:input path="dueDateTo" cssErrorClass="has-error" />
	</div>
	<div class="form-group">
		<f:checkbox path="notDone" label="未完了" cssErrorClass="has-error" />
		<f:checkbox path="done" label="完了" cssErrorClass="has-error" />
	</div>
	<div class="form-group">
		<f:label path="orderBy" cssErrorClass="has-error">ソート列</f:label>
		<f:select path="orderBy" cssErrorClass="has-error">
			<c:set var="orderByList"
				value="${fwcore:getLabeledEnumList('cherry.spring.tutorial.web.secure.todo.OrderBy')}" />
			<f:options itemValue="enumName" itemLabel="enumLabel"
				items="${orderByList}" />
		</f:select>
	</div>
	<div class="form-group">
		<f:label path="orderDir" cssErrorClass="has-error">ソート順</f:label>
		<c:set var="orderDirList"
			value="${fwcore:getLabeledEnumList('cherry.spring.tutorial.web.secure.todo.OrderDir')}" />
		<f:radiobuttons path="orderDir" itemValue="enumName"
			itemLabel="enumLabel" items="${orderDirList}"
			cssErrorClass="has-error" />
	</div>
	<input type="hidden" name="pageNo" value="0" />
	<f:hidden path="pageSz" />
	<f:button type="submit" class="btn btn-default">検索</f:button>
	<f:button type="submit" class="btn btn-default" name="download">ダウンロード</f:button>
</f:form>
<c:if test="${pagedList != null && !pagedList.list.isEmpty()}">
	<f:form servletRelativeAction="/secure/todo/list/execute" method="POST"
		modelAttribute="todoListForm" id="todoListFormHidden"
		class="app-pager-form">
		<f:hidden path="postedFrom" id="postedFromHidden" />
		<f:hidden path="postedTo" id="postedToHidden" />
		<f:hidden path="dueDateFrom" id="dueDateFromHidden" />
		<f:hidden path="dueDateTo" id="dueDateToHidden" />
		<f:hidden path="notDone" id="notDoneHidden" />
		<f:hidden path="done" id="doneHidden" />
		<f:hidden path="orderBy" id="orderByHidden" />
		<f:hidden path="orderDir" id="orderDirHidden" />
		<f:hidden path="pageNo" id="pageNoHidden" cssClass="app-page-no" />
		<f:hidden path="pageSz" id="pageSzHidden" cssClass="app-page-sz" />
	</f:form>
	<mytag:pagerLink pageSet="${pagedList.pageSet}" />
	<table class="table table-striped">
		<thead>
			<tr>
				<th>#</th>
				<th>ID</th>
				<th>投稿日時</th>
				<th>期日</th>
				<th>状態</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="count" begin="1" end="${pagedList.list.size()}">
				<s:nestedPath path="pagedList.list[${count - 1}]">
					<s:bind path="id">
						<c:url var="url" value="/secure/todo/${status.value}" />
					</s:bind>
					<tr>
						<td>${pagedList.pageSet.current.from + count}</td>
						<td><a href="${url}"><s:bind path="id">${status.value}</s:bind></a></td>
						<td><a href="${url}"><s:bind path="postedAt">${status.value}</s:bind></a></td>
						<td><s:bind path="dueDate">${status.value}</s:bind></td>
						<td><s:bind path="doneFlg">${status.actualValue.booleanValue() ? "完了" : "未完了" }</s:bind></td>
					</tr>
				</s:nestedPath>
			</c:forEach>
		</tbody>
	</table>
	<mytag:pagerLink pageSet="${pagedList.pageSet}" />
</c:if>
