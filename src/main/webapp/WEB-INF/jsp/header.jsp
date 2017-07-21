<%--
  Created by IntelliJ IDEA.
  User: alexandremasanes
  Date: 20/02/2017
  Time: 11:36
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
"http://www.w3.org/TR/html4/strict.dtd">
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title><spring:message code="head.titles.${headTitleCode}" arguments="${messageVars}"/> | Assurance Automobile Aixoise</title>
        <base href="${WEBROOT}/">
        <link href="${WEBROOT}/resources/style/default.css" rel="stylesheet" type="text/css" media="all" />
        <link rel="shortcut icon" href="${WEBROOT}/resources/image/aaa.ico" />
        <!-- Latest compiled and minified CSS -->
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous" onerror="src_on_error(this, '${WEBROOT}/resources/style/bootstrap.3.3.7.min.css')">
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap-theme.min.css" integrity="sha384-rHyoN1iRsVXV4nD0JutlnGaslCJuC7uwjduW9SVrLvRYooPp2bWYgmgJQIXwl/Sp" crossorigin="anonymous" onerror="src_on_error(this, '${WEBROOT}/resources/style/bootstrap-theme.3.3.7.min.css')">
        <link rel="stylesheet" href="https://ajax.googleapis.com/ajax/libs/jqueryui/1.12.1/themes/smoothness/jquery-ui.css" onerror="src_on_error(this, '${WEBROOT}/resources/style/jquery-ui.1.12.1.css')">
        <c:if test="${not empty styleSheets}">
            <c:forEach items="${styleSheets}" var="styleSheet">
                <link href="${WEBROOT}/resources/style/${styleSheet}" rel="style" type="text/style" media="all" />
            </c:forEach>
        </c:if>
    </head>
    <body>
    <noscript>
        <meta http-equiv="refresh" content="0;URL=${WEBROOT}/erreur/600">
    </noscript>