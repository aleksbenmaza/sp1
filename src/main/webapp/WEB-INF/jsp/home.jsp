<%--
  Created by IntelliJ IDEA.
  User: alexandremasanes
  Date: 17/02/2017
  Time: 16:26
  To change this template use File | Settings | File Templates.
--%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<jsp:include page="header.jsp"/>
<jsp:include page="navbar.jsp"/>
<jsp:include page="banner.jsp"/>
    <div id="container">
        <div id="banner">
            <div class="image-border">
                <a href="#">
                    <img src="${WEBROOT}/resources/image/provence_ban.jpg" class="img-responsive" alt="" />
                </a>
            </div>
        </div>
        <div class="panel panel-default">
            <div class="panel-heading" style = " background: #353535; color: #AFAFAF;">
                <h3 class="panel-title">
                    <spring:message code="home.welcomeTitle"/>
                </h3>
            </div>
            <div class="panel-body">
                <spring:message code="home.welcomeMessage"/>
            </div>
        </div>
    </div>
<jsp:include page="footer.jsp"/>