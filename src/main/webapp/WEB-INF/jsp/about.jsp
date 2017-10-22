<%--
  Created by IntelliJ IDEA.
  User: alexandremasanes
  Date: 21/02/2017
  Time: 12:10
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ include file="header.jsp" %>
<%@ include file="navbar.jsp" %>
<%@ include file="banner.jsp" %>
<div id="container">
    <div id="banner">
        <div class="image-border"><a href="#"><img src="${WEBROOT}/resources/public/image/aix-en-provence-161731.jpg" width="870" height="253" alt="" class="img-responsive" /></a></div>
    </div>
    <div class="col-md-offset-2 same-line">
        <div class="panel panel-default col-md-4">
            <div class="panel-heading " style = " background: #353535; color: #AFAFAF;">
                <h3 class="panel-title">
                    <spring:message code="about.panel1.title"/>
                </h3>
            </div>
            <div class="panel-body">
                <p>
                    <spring:message code="about.panel1.content"/>
                </p>
            </div>
        </div>
        <div class="panel panel-default col-md-4">
            <div class="panel-heading" style = " background: #353535; color: #AFAFAF;">
                <h3 class="panel-title">
                    <spring:message code="about.panel2.title"/>
                </h3>
            </div>
            <div class="panel-body">
                <spring:message code="about.panel2.content"/>
            </div>
        </div>
        <div class="panel panel-default col-md-4">
            <div class="panel-heading" style = " background: #353535; color: #AFAFAF;">
                <h3 class="panel-title">
                    <spring:message code="about.panel3.title"/>
                </h3>
            </div>
            <div class="panel-body">
                <spring:message code="about.panel3.content"/>
            </div>
        </div>
    </div>
</div>
<%@include file="footer.jsp" %>