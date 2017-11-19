<%--
  Created by IntelliJ IDEA.
  User: alexandremasanes
  Date: 26/02/2017
  Time: 14:40
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ include file="header.jsp" %>
<div id="log_reg" class="container">
    <form:form method="post" id="auth_form" modelAttribute="login" class="form-horizontal">
        <div class="form-group">
            <form:label path="emailAddress">Email</form:label>
            <br/>
            <form:input type="email" path="emailAddress" id="email" size="30"/>
        </div>
        <div class="form-group">
            <form:label path="password">Mot de passe</form:label>
            <br/>
            <form:input type="password" path="password" id="pwd" size="30"/>
        </div>
        <div class="form-group">
            <button type="submit" id="log_button" class="btn btn-default">Connexion</button>
            <button type="submit" id="reg_button" class="btn btn-default">Inscription</button>
        </div>
        <form:input path="securityTimestamp" hidden="true"/>
    </form:form>
    <a href="${WEBROOT}/mot-de-passe-perdu"><spring:message code="loginForm.forgottenPasswordLinkText"/></a>
</div>
<script type="text/javascript" src="${WEBROOT}/resources/public/script/app/login.js" defer></script>
<%@ include file="footer.jsp" %>