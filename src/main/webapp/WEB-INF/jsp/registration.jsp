<%--
  Created by IntelliJ IDEA.
  User: alexandremasanes
  Date: 04/03/2017
  Time: 23:12
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ include file="header.jsp" %>
<div>
    <form:form method="post" action="${WEBROOT}/inscription" modelAttribute="registration" class="sign" enctype="multipart/form-data">

        <form:label path="lastName">
            <spring:message code="registration.labels.lastName"/>
        </form:label>
        <br/>
        <form:input type="text" path="lastName" id="last_name" size="30"/>
        <form:errors path="lastName" cssClass="errors"/>
        <br/>

        <form:label path="firstName">
            <spring:message code="registration.labels.firstName"/>
        </form:label>
        <br/>
        <form:input type="text" path="firstName" id="first_name" size="30"/>
        <form:errors path="firstName" cssClass="errors"/>
        <br/>

        <form:label path="password">
            <spring:message code="registration.labels.password"/>
        </form:label>
        <br/>
        <form:input type="password" path="password" id="pwd" size="30"/>
        <form:errors path="password" cssClass="errors"/>
        <br/>

        <form:label path="passwordConfirm">
            <spring:message code="registration.labels.passwordConfirm"/>
        </form:label>
        <br/>
        <form:input type="password" path="passwordConfirm" size="30"/>
        <br/>

        <form:label path="address">
            <spring:message code="registration.labels.address"/>
        </form:label>
        <br/>
        <form:input type="text" path="address" id="address" size="30"/>
        <form:errors path="address" cssClass="errors"/>
        <br/>

        <form:label path="city">
            <spring:message code="registration.labels.city"/>
        </form:label>
        <br/>
        <form:input type="text" path="city" id="city" size="30"/>
        <form:errors path="city" cssClass="errors"/>
        <br/>

        <form:label path="zipCode">
            <spring:message code="registration.labels.zipCode"/>
        </form:label>
        <br/>
        <form:input type="number" path="zipCode" id="zip_code" size="30"/>
        <form:errors path="zipCode" cssClass="errors"/>
        <br/>

        <form:label path="phoneNumber">
            <spring:message code="registration.labels.phoneNumber"/>
        </form:label>
        <br/>
        <form:input type="text" path="phoneNumber" id="phone_number" size="30"/>
        <form:errors path="phoneNumber" cssClass="errors"/>
        <br/>

        <form:label path="emailAddress">
            <spring:message code="registration.labels.emailAddress"/>
        </form:label><br/>
        <form:input type="email" path="emailAddress" id="email_address" size="30"/>
        <form:errors path="emailAddress" cssClass="errors"/>
        <br/>

        <form:label path="idCard">
            <spring:message code="registration.labels.idCard"/>
        </form:label><br/>
        <form:input type="file" path="idCard" id="idCard"  style="position: relative; margin-left: 42.5%"/>
        <form:errors path="idCard" cssClass="errors"/>
        <br/>

        <input type="submit" value="Valider" class="valider"/>
    </form:form>
</div>
<%@ include file="footer.jsp" %>