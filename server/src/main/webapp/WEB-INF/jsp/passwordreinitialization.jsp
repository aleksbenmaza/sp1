<%--
  Created by IntelliJ IDEA.
  User: alexandremasanes
  Date: 22/06/2017
  Time: 09:52
  To change this template use File | Settings | File Templates.
--%>
<%@ include file="header.jsp" %>
<div id="log_reg" class="container">
    <form method="post" id="password_reinitialization_form"  class="form-horizontal">
        <div class="form-group">
            <label>Mot de passe</label>
            <br/>
            <input type="password" name="password" id="password" size="30"/>
        </div>
        <div class="form-group">
            <button class="btn btn-default">Reinitialiser mon mot de passe</button>
        </div>
    </form>
</div>
<%@ include file="footer.jsp" %>