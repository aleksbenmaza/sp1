<%--
  Created by IntelliJ IDEA.
  User: alexandremasanes
  Date: 22/06/2017
  Time: 09:37
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<jsp:include page="header.jsp"/>
<div id="log_reg" class="container">
    <form method="post" class="form-horizontal">
        <div class="form-group">
            <label >Email</label>
            <br/>
            <input name="emailAddress" id="email" size="30"/>
        </div>
        <div class="form-group">
            <button class="btn btn-default">Reinitialiser mon mot de passe</button>
        </div>
    </form>
</div>
<jsp:include page="footer.jsp"/>
