<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<!DOCTYPE html>

<html>
<head>
<title><tiles:getAsString name="title" /></title>
</head>

<body>

	<!-- Основной контент страницы -->
	<div class="container">
		<tiles:insertAttribute name="body" />
	</div>

	<div class="footer">

		<!-- футер страницы -->
		<tiles:insertAttribute name="footer" />

	</div>
</body>
</html>