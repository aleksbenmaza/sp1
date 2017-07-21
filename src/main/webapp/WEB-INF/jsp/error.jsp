<%--
  Created by IntelliJ IDEA.
  User: alexandremasanes
  Date: 21/02/2017
  Time: 13:32
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="spring" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="header.jsp"/>
<div class='erreur'>
    <h2>
        <b>
            <spring:message key="error.message${pageContext.errorData.statusCode}"/>
        </b>
    </h2>
</div>
<jsp:include page="footer.jsp"/>