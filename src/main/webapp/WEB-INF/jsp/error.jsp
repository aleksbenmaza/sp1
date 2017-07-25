<%--
  Created by IntelliJ IDEA.
  User: alexandremasanes
  Date: 21/02/2017
  Time: 13:32
  To change this template use File | Settings | File Templates.
--%>
<%@ include file="header.jsp" %>
<div class='erreur'>
    <h2>
        <b>
            <spring:message code="error.message${pageContext.errorData.statusCode}"/>
        </b>
    </h2>
</div>
<%@ include file="footer.jsp" %>