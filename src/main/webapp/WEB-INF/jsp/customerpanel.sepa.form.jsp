<%--
  Created by IntelliJ IDEA.
  User: alexandremasanes
  Date: 25/03/2017
  Time: 13:38
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<p>
    Veuillez retourner
    <a href="${WEBROOT}telechargement/sepa/">
        le document SEPA</a>
    une fois rempli :
</p>
<form:form method="post" modelAttribute="sepaUploading" action="${WEBROOT}upload/sepa/" enctype="multipart/form-data">
    <p>
        <form:input path="sepa" type="file"/>
    </p>
    <p>
        <input type="submit" name="submit_sepa" value="Valider"/>
    </p>
</form:form>
