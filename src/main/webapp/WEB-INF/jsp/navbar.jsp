<%--
  Created by IntelliJ IDEA.
  User: alexandremasanes
  Date: 20/02/2017
  Time: 13:45
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<c:set var="requestURI" value="${WEBROOT.concat(requestScope['javax.servlet.forward.request_uri'])}"/>
<nav class="navbar navbar-inverse navbar-fixed-top">
    <div class="container-fluid">
        <!-- Brand and toggle get grouped for better mobile display -->
        <div class="navbar-header">
            <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1" aria-expanded="false">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand"><img src="${WEBROOT}/resources/image/aaa.png" class="img-responsive" style="margin-top: -0.4rem;" width="50px" height="60px" /></a>
        </div>
        <!-- Collect the nav links, forms, and other content for toggling -->
        <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
            <ul class="nav navbar-nav">
                <li <c:if test='${requestURI == WEBROOT.concat("/")}'>class="active"</c:if>><a href="${WEBROOT}/" accesskey="1" title=""><span><spring:message code="navbar.homeTitle"/></span></a></li>
                <li <c:if test='${requestURI == WEBROOT.concat("/nos-services")}'>class="active"</c:if>><a href="${WEBROOT}/nos-services" accesskey="2" title=""><span><spring:message code="navbar.servicesTitle"/> </span></a>
                <li <c:if test='${requestURI == WEBROOT.concat("/press")}'>class="active"</c:if>><a href="#" accesskey="4" title=""><span><spring:message code="navbar.pressTitle"/></span></a>
                <li <c:if test='${requestURI == WEBROOT.concat("/a-propos")}'>class="active"</c:if>><a href="${WEBROOT}/a-propos" accesskey="3" title=""><span><spring:message code="navbar.aboutTitle"/></span></a></li>
            </ul>
            <ul class="nav navbar-nav navbar-right">
                <c:if test="${not empty link1}">
                    <li>
                        <a href="${link1.url}">
                            <span>
                                ${link1.name}
                            </span>
                        </a>
                    </li>
                </c:if>
                <c:if test="${not empty link2}">
                    <li role="presentation">
                        <a href="${link2.url}">
                            <span>
                                ${link2.name}
                            </span>
                        </a>
                    </li>
                </c:if>
            </ul>
        </div><!-- /.navbar-collapse -->
    </div><!-- /.container-fluid -->
</nav>